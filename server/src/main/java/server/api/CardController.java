package server.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import commons.Card;
import server.database.CardListRepository;
import server.database.CardRepository;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardRepository repo;
    private final CardListRepository listRepo;

    public CardController(CardRepository repo, CardListRepository listRepo) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCard(@PathVariable("id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCardTitle(@PathVariable("id") long id, @RequestBody Card card){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Card cardFromRepo = repo.findById(id).get();
        cardFromRepo.setTitle(card.title);
        cardFromRepo.setCardListId(card.cardListId);
        Card saved = repo.save(cardFromRepo);

        return ResponseEntity.ok(saved);

    }
}