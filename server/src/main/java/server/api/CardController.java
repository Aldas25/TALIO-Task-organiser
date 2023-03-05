package server.api;

import java.util.List;
import java.util.Random;

//import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.Card;
import server.database.CardListRepository;
import server.database.CardRepository;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final Random random;
    private final CardRepository repo;
    private final CardListRepository listRepo;

    public CardController(Random random, CardRepository repo, CardListRepository listRepo) {
        this.random = random;
        this.repo = repo;
        this.listRepo = listRepo;
    }

    @GetMapping(path = { "", "/" })
    public List<Card> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = {"/", ""})
    public ResponseEntity<Card> add(@RequestBody Card card) {

        if (isNullOrEmpty(card.title)) {
            return ResponseEntity.badRequest().build();
        }

        Card saved = repo.save(card);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping("rnd")
    public ResponseEntity<Card> getRandom() {
        var lists = repo.findAll();
        var idx = random.nextInt((int) repo.count());
        return ResponseEntity.ok(lists.get(idx));
    }
}