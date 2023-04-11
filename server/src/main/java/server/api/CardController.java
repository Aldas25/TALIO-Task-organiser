package server.api;

import java.util.List;

import commons.CardList;
import commons.CustomPair;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import commons.Card;
import server.services.CardService;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;
    private final SimpMessagingTemplate msgs;

    public CardController(CardService cardService,
                          SimpMessagingTemplate msgs) {
        this.cardService = cardService;
        this.msgs = msgs;
    }

    @GetMapping(path = { "", "/" })
    public List<Card> getAll() {
        return cardService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        try {
            Card card = cardService.getById(id);
            return ResponseEntity.ok(card);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @MessageMapping("/cards/delete")// /app/cards/delete
    @SendTo("/topic/lists/update")
    public void deleteCardMessage(Long id){
        ResponseEntity responseEntity = deleteCard(id);
        msgs.convertAndSend("/topic/lists/update", responseEntity.getStatusCode());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCard(@PathVariable("id") long id){
        try {
            cardService.deleteCard(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
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
            @PathVariable("id") long id,
            @RequestBody Card card
    ){
        try{
            Card saved = cardService.updateCardTitle(id, card);
            return ResponseEntity.ok(saved);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
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
        try{
            Card saved = cardService.moveCard(id, listId, newPos);
            return ResponseEntity.ok(saved);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/list")
    public ResponseEntity<CardList> getCardListById(@PathVariable("id") long id){
        try {
            CardList list = cardService.getCardListById(id);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}