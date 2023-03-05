package server.api;

import java.util.List;
//import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.CardList;
import server.database.CardListRepository;
import server.database.CardRepository;

@RestController
@RequestMapping("/api/lists")
public class CardListController {

    private final Random random;
    private final CardListRepository repo;
    private final CardRepository cardRepo;

    public CardListController(Random random, CardListRepository repo, CardRepository cardRepo) {
        this.random = random;
        this.repo = repo;
        this.cardRepo = cardRepo;
    }

    @GetMapping(path = { "", "/" })
    public List<CardList> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @GetMapping("/{id}/cards")
    public ResponseEntity<List<Card>> getCardsForList(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        List<Card> cards = cardRepo.findAll().stream()
                .filter(card -> card.cardListId == id)
                .collect(Collectors.toList());
//        List<Card> cards = Objects.requireNonNull(getById(id).getBody()).cards;
        return ResponseEntity.ok(cards);
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<CardList> add(@RequestBody CardList list) {

        if (isNullOrEmpty(list.title)) {
            return ResponseEntity.badRequest().build();
        }

        CardList saved = repo.save(list);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping("rnd")
    public ResponseEntity<CardList> getRandom() {
        var lists = repo.findAll();
        var idx = random.nextInt((int) repo.count());
        return ResponseEntity.ok(lists.get(idx));
    }
}