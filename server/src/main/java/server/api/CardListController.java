package server.api;

import java.util.List;


import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import commons.CardList;
import server.database.CardListRepository;
import server.database.CardRepository;

@RestController
@RequestMapping("/api/lists")
public class CardListController {

    private final CardListRepository repo;
    private final CardRepository cardRepo;

    private final SimpMessagingTemplate msgs;

    public CardListController(CardListRepository repo, CardRepository cardRepo,
                              SimpMessagingTemplate msgs) {
        this.repo = repo;
        this.cardRepo = cardRepo;
        this.msgs = msgs;
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

    @MessageMapping("/lists/add")// /app/lists/add
    @SendTo("/topic/lists/add")
    public CardList addMessage(CardList list){
        add(list);
        msgs.convertAndSend("/topic/lists/add", list);
        return list;
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
    @MessageMapping("/lists/delete")// /app/lists/delete
    @SendTo("/topic/lists/delete")
    public Long deleteListMessage(Long id){
        deleteList(id);
        msgs.convertAndSend("/topic/lists/delete", id);
        return id;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteList(@PathVariable("id") Long id){
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