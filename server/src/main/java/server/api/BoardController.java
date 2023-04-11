package server.api;

import commons.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.services.BoardService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;
    private final SimpMessagingTemplate msgs;

    public BoardController(BoardService boardService,
                           SimpMessagingTemplate msgs) {
        this.boardService = boardService;
        this.msgs = msgs;
    }

    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        return boardService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        try {
            Board board = boardService.getById(id);
            return ResponseEntity.ok(board);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private final Map<Object, Consumer<Board>> listeners = new HashMap<>();

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
        listeners.put(key, board -> res.setResult(ResponseEntity.ok(board)));

        // once we have listener we remove it, so we would be prepared for another listener.
        res.onCompletion(() -> listeners.remove(key));

        return res;
    }

    @PostMapping(path = {"", "/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {
        Board saved;
        try {
            saved = boardService.add(board);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        listeners.forEach((key, listener) -> listener.accept(board));

        return ResponseEntity.ok(saved);
    }

    @MessageMapping("/boards/update")// /app/lists/update
    @SendTo("/topic/boards/update")
    public void updateBoardTitleMessage(CustomPair<Long, Board> pair) {
        ResponseEntity<Board> responseEntity = updateBoardTitle(pair.getId(), pair.getVar());
        msgs.convertAndSend("/topic/boards/update", responseEntity.getStatusCode());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Board> updateBoardTitle(
            @PathVariable("id") long id,
            @RequestBody Board board
    ){
        Board saved;
        try {
            saved = boardService.updateBoardTitle(id, board.title);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        listeners.forEach((key, listener) -> listener.accept(board));
        return ResponseEntity.ok(saved);
    }

    @MessageMapping("/lists/add")// /app/lists/add
    @SendTo("/topic/lists/update")
    public void addListMessage(CustomPair<Long, CardList> pair){
        Long id = pair.getId();
        CardList list = pair.getVar();

        ResponseEntity<CardList> responseEntity = addCardList(id, list);
        msgs.convertAndSend("/topic/lists/update", responseEntity.getStatusCode());
    }

    @PostMapping("/{id}/lists")
    public ResponseEntity<CardList> addCardList(@PathVariable("id") long id,
                                                @RequestBody CardList cardList){
        try {
            CardList saved = boardService.addCardList(id, cardList);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/lists")
    public ResponseEntity<List<CardList>> getCardListsById(@PathVariable("id") long id) {
        try {
            List<CardList> lists = boardService.getCardListsById(id);
            return ResponseEntity.ok(lists);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @MessageMapping("/boards/delete")
    @SendTo("/topic/boards/update")
    public void deleteBoardMessage(Long id){
        ResponseEntity<Boolean> responseEntity = deleteBoard(id);
        msgs.convertAndSend(("/topic/boards/update"), responseEntity.getStatusCode());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBoard(@PathVariable("id") long id){
        try {
            boardService.deleteBoard(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        listeners.forEach((key, listener) -> listener.accept(getById(id).getBody()));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/byKey/{inviteKey}")
    public ResponseEntity<Board> getBoardByInviteKey(@PathVariable("inviteKey") String enteredKey){
        try {
            Board board = boardService.getBoardByInviteKey(enteredKey);
            return ResponseEntity.ok(board);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
