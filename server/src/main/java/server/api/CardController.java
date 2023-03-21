package server.api;

import java.util.List;

import commons.CardList;
import org.springframework.http.HttpStatus;
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


    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCard(@PathVariable("id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        removeCardFromItsList(id);
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCardTitle(
            @PathVariable("id") long id,
            @RequestBody Card card
    ){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        
        Card cardFromRepo = repo.findById(id).get();

        cardFromRepo.title = card.title;
        Card saved = repo.save(cardFromRepo);

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/list/{listId}/{newPos}")
    public ResponseEntity<Card> moveCard(
            @PathVariable("id") long id,
            @PathVariable("listId") long listId,
            @PathVariable("newPos") int newPos
    ){
        Card card = repo.findById(id).get();
        removeCardFromItsList(id);

        CardList list = listRepo.findById(listId).get();
        list.cards.add(newPos, card);
        listRepo.save(list);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/list")
    public ResponseEntity<CardList> getList(@PathVariable("id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Card card = repo.findById(id).get();
        for(CardList list : listRepo.findAll()){
            if(list.cards.contains(card)){
                return ResponseEntity.ok(list);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    private ResponseEntity removeCardFromItsList(long id) {
        Card card = repo.findById(id).get();
        var listResponse = getList(id);
        if (listResponse.getStatusCode() == HttpStatus.BAD_REQUEST)
            return ResponseEntity.badRequest().build();

        CardList prevList = listResponse.getBody();
        prevList.cards.remove(card);
        listRepo.save(prevList);

        return ResponseEntity.ok().build();
    }
}