package server.api;

import java.util.List;

import commons.CardList;
import commons.CustomPair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import commons.Card;
import server.database.CardListRepository;
import server.database.CardRepository;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardRepository repo;
    private final CardListRepository listRepo;
    private final SimpMessagingTemplate msgs;

    public CardController(CardRepository repo,
                          CardListRepository listRepo,
                          SimpMessagingTemplate msgs) {
        this.repo = repo;
        this.listRepo = listRepo;
        this.msgs = msgs;
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

    @MessageMapping("/cards/delete")// /app/cards/delete
    @SendTo("/topic/cards/delete")
    public Long deleteCardMessage(Long id){
        deleteCard(id);
        msgs.convertAndSend("/topic/cards/delete", id);
        return id;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCard(@PathVariable("id") Long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        ResponseEntity removingResponse = removeCardFromItsList(id);
        if (removingResponse.getStatusCode() == HttpStatus.BAD_REQUEST)
            return ResponseEntity.badRequest().build();

        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/cards/update")// /app/cards/update
    @SendTo("/topic/cards/update")
    public Card updateCardMessage(CustomPair<Long, Card> pair){
        Long id = pair.getId();
        Card card = pair.getVar();

        updateCardTitle(id, card);
        msgs.convertAndSend("/topic/cards/update", card);
        return card;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCardTitle(
            @PathVariable("id") Long id,
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
        List<CardList> allLists = listRepo.findAll();
        for(CardList list : allLists){
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