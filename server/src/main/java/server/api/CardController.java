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

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @MessageMapping("/cards/delete")// /app/cards/delete
    @SendTo("/topic/lists/update")
    public void deleteCardMessage(Long id){
        ResponseEntity responseEntity = deleteCard(id);
        msgs.convertAndSend("/topic/lists/update", responseEntity.getStatusCode());
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
    @SendTo("/topic/lists/update")
    public void updateCardMessage(CustomPair<Long, Card> pair){
        Long id = pair.getId();
        Card card = pair.getVar();

        ResponseEntity responseEntity = updateCardTitle(id, card);
        msgs.convertAndSend("/topic/lists/update", responseEntity.getStatusCode());
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

    @MessageMapping("/cards/move")// /app/cards/move
    @SendTo("/topic/lists/update")
    public CardList moveCardMessage(CustomPair<Card, CustomPair<CardList, Integer>> pair){
        Card card = pair.getId();
        CardList list = pair.getVar().getId();
        Integer newPos = pair.getVar().getVar();

        ResponseEntity responseEntity = moveCard(card.id, list.id, newPos);
        msgs.convertAndSend("/topic/lists/update", responseEntity.getStatusCode());
        return list;
    }

    @PutMapping("/{id}/list/{listId}/{newPos}")
    public ResponseEntity<Card> moveCard(
            @PathVariable("id") long id,
            @PathVariable("listId") long listId,
            @PathVariable("newPos") int newPos
    ){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        if (listId < 0 || !listRepo.existsById(listId)) {
            return ResponseEntity.badRequest().build();
        }

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
        for(CardList list : allLists) {
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