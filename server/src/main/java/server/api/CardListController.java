package server.api;

import java.util.List;


import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import commons.CardList;
import server.database.CardListRepository;
import server.database.CardRepository;

@RestController
@RequestMapping("/api/lists")
public class CardListController {

    private final CardListRepository repo;
    private final CardRepository cardRepo;

    public CardListController(CardListRepository repo, CardRepository cardRepo) {
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
        CardList list = repo.findById(id).get();
        List<Card> cards = list.cards;
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

    @PostMapping("/{id}/cards")
    public ResponseEntity<Card> addCard(@PathVariable("id") long id, @RequestBody Card card){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        if(isNullOrEmpty(card.title)){
            return ResponseEntity.badRequest().build();
        }

        Card saved = cardRepo.save(card);
        CardList list = repo.findById(id).get();
        list.cards.add(saved);
        repo.save(list);

        return ResponseEntity.ok(card);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteList(@PathVariable("id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardList> updateListTitle(
            @PathVariable("id") long id,
            @RequestBody CardList list
    ){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        CardList listFromRepo = repo.findById(id).get();
        listFromRepo.title = list.title;
        CardList saved = repo.save(listFromRepo);

        return ResponseEntity.ok(saved);
    }
}