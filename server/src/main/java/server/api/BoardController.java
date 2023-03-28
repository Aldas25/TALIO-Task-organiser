package server.api;

import commons.Board;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.CardListRepository;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardRepository repo;
    private final CardListRepository cardListRepo;

    public BoardController(BoardRepository repo, CardListRepository cardListRepo){
        this.repo = repo;
        this.cardListRepo = cardListRepo;
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

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {
        if (isNullOrEmpty(board.title)) {
            return ResponseEntity.badRequest().build();
        }
        Board saved = repo.save(board);
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

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBoard(@PathVariable("id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
