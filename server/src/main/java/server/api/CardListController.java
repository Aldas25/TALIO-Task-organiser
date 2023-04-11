package server.api;

import java.util.List;

import commons.Board;
import commons.Card;
import commons.CustomPair;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import commons.CardList;
import server.services.CardListService;

@RestController
@RequestMapping("/api/lists")
public class CardListController {

    private final CardListService listService;
    private final SimpMessagingTemplate msgs;

    public CardListController(CardListService listService,
                              SimpMessagingTemplate msgs) {
        this.listService = listService;
        this.msgs = msgs;
    }

    @GetMapping(path = { "", "/" })
    public List<CardList> getAll() {
        return listService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id) {
        try {
            CardList list = listService.getById(id);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/cards")
    public ResponseEntity<List<Card>> getCardsForList(@PathVariable("id") long id) {
        try {
            List<Card> cards = listService.getCardsForList(id);
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @MessageMapping("/cards/add")// /app/cards/add
    @SendTo("/topic/lists/update")
    public void addCardMessage(CustomPair<Long, Card> pair){
        Long id = pair.getId();
        Card card = pair.getVar();

        ResponseEntity<Card> responseEntity = addCard(id, card);
        msgs.convertAndSend("/topic/lists/update", responseEntity.getStatusCode());
    }

    @PostMapping("/{id}/cards")
    public ResponseEntity<Card> addCard(@PathVariable("id") Long id, @RequestBody Card card){
        try {
            Card saved = listService.addCard(id, card);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @MessageMapping("/lists/delete")// /app/lists/delete
    @SendTo("/topic/lists/update")
    public void deleteListMessage(Long id){
        ResponseEntity<Boolean> responseEntity = deleteList(id);
        msgs.convertAndSend("/topic/lists/update", responseEntity.getStatusCode());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteList(@PathVariable("id") Long id){
        try {
            listService.deleteList(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @MessageMapping("/lists/update")// /app/lists/update
    @SendTo("/topic/lists/update")
    public void updateListTitleMessage(CustomPair<Long, CardList> pair) {
        ResponseEntity<CardList> responseEntity = updateListTitle(pair.getId(), pair.getVar());
        msgs.convertAndSend("/topic/lists/update", responseEntity.getStatusCode());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardList> updateListTitle(
            @PathVariable("id") long id,
            @RequestBody CardList list
    ){
        try {
            CardList saved = listService.updateListTitle(id, list.title);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/board")
    public ResponseEntity<Board> getBoard(@PathVariable("id") long id){
        try {
            Board b = listService.getBoard(id);
            return ResponseEntity.ok(b);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}