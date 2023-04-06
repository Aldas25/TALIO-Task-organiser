package server.api;

import commons.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.BoardRepository;
import server.database.CardListRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardRepository repo;
    private final CardListRepository cardListRepo;
    private final SimpMessagingTemplate msgs;

    public BoardController(BoardRepository repo, CardListRepository cardListRepo,
                           SimpMessagingTemplate msgs){
        this.repo = repo;
        this.cardListRepo = cardListRepo;
        this.msgs = msgs;
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    private Map<Object, Consumer<Board>> listeners = new HashMap<>();

    /**
     * We wrap in DeferredResult which makes this request asynchronous.
     * This method is in the waiting state once called.
     * @return board that was updated
     */
    @GetMapping("/admin/update")
    public DeferredResult<ResponseEntity<Board>> getUpdates(){
        var noContent =
                ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        // it will take up to 5s to get an answer
        var res = new DeferredResult<ResponseEntity<Board>>(5000L, noContent);

        // when we call this method the waiting is over,
        // we wait for who is first: setResult() called or timeout.

        // we will be notified when someone calls the consumer
        var key = new Object();
        listeners.put(key, board ->{
            res.setResult(ResponseEntity.ok(board));
        });

        // once we have listener we remove it, so we would be prepared for another listener.
        res.onCompletion(() -> {
            listeners.remove(key);
        });

        return res;
    }

    @PostMapping(path = {"", "/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {
        if (isNullOrEmpty(board.title)) {
            return ResponseEntity.badRequest().build();
        }
        Board saved = repo.save(board);

        listeners.forEach((key, listener) -> listener.accept(board));

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Board> updateBoardTitle(
            @PathVariable("id") long id,
            @RequestBody Board board
    ){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Board boardFromRepo = repo.findById(id).get();
        boardFromRepo.title = board.title;
        Board saved = repo.save(boardFromRepo);

        listeners.forEach((key, listener) -> listener.accept(board));

        return ResponseEntity.ok(saved);
    }

    @MessageMapping("/lists/add")// /app/lists/add
    @SendTo("/topic/lists/update")
    public void addListMessage(CustomPair<Long, CardList> pair){
        Long id = pair.getId();
        CardList list = pair.getVar();

        ResponseEntity responseEntity = addCardList(id, list);
        msgs.convertAndSend("/topic/lists/update", responseEntity.getStatusCode());
    }

    @PostMapping("/{id}/lists")
    public ResponseEntity<CardList> addCardList(@PathVariable("id") long id,
                                                @RequestBody CardList cardList){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        if(isNullOrEmpty(cardList.title)){
            return ResponseEntity.badRequest().build();
        }

        CardList saved = cardListRepo.save(cardList);
        Board board = repo.findById(id).get();
        board.lists = removeDuplicateLists(board.lists);
        board.lists.add(saved);
        Board savedBoard = repo.save(board);

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}/lists")
    public ResponseEntity<List<CardList>> getCardListsById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Board board = repo.findById(id).get();
        List<CardList> cardList = removeDuplicateLists(board.lists);

        return ResponseEntity.ok(cardList);
    }

    public List<CardList> removeDuplicateLists(List<CardList> lists) {
        return lists
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBoard(@PathVariable("id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        repo.deleteById(id);

        listeners.forEach((key, listener) -> listener.accept(getById(id).getBody()));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/byKey/{inviteKey}")
    public ResponseEntity<Board> getBoardbyInviteKey(@PathVariable("inviteKey") String enteredKey){
        if(enteredKey.length() != 4){
            return ResponseEntity.badRequest().build();
        }
        for(Board board: repo.findAll()){
            if(enteredKey.equals(board.inviteKey)){
                return ResponseEntity.ok(board);
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
