package server.api;

import commons.*;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.CardListRepository;

import java.util.List;
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

    @MessageMapping("/boards/add")// /app/boards/add
    @SendTo("/topic/boards/add")
    public Board addMessage(Board board){
        add(board);
        msgs.convertAndSend("/topic/boards/add", board);
        return board;
    }

    @PostMapping(path = {"", "/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {
        if (isNullOrEmpty(board.title)) {
            return ResponseEntity.badRequest().build();
        }
        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }

    @MessageMapping("/lists/add")// /app/lists/add
    @SendTo("/topic/lists/add")
    public CardList addListMessage(CustomPair<Long, CardList> pair){
        Long id = pair.getId();
        CardList list = pair.getVar();

        addCardList(id, list);
        msgs.convertAndSend("/topic/lists/add", list);
        return list;
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

        return ResponseEntity.ok(saved);
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
        board.lists.add(saved);
        repo.save(board);

        return ResponseEntity.ok(cardList);
    }

    @GetMapping("/{id}/lists")
    public ResponseEntity<List<CardList>> getCardListsById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Board board = repo.findById(id).get();
        List<CardList> cardList = board.lists
                .stream()
                .distinct()
                .collect(Collectors.toList());

        return ResponseEntity.ok(cardList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBoard(@PathVariable("id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        repo.deleteById(id);
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
