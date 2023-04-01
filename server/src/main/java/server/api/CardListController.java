package server.api;

import java.util.List;

import commons.Board;
import commons.Card;
import commons.CustomPair;
import commons.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import commons.CardList;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;
import server.database.TagRepository;

@RestController
@RequestMapping("/api/lists")
public class CardListController {

    private final CardListRepository repo;
    private final CardRepository cardRepo;
    private final BoardRepository boardRepo;

    private final TagRepository tagRepo;

    private final SimpMessagingTemplate msgs;

    public CardListController(CardListRepository repo,
                              CardRepository cardRepo,
                              BoardRepository boardRepo,
                              TagRepository tagRepo,
                              SimpMessagingTemplate msgs) {
        this.repo = repo;
        this.cardRepo = cardRepo;
        this.boardRepo = boardRepo;
        this.tagRepo = tagRepo;
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

    @MessageMapping("/cards/add")// /app/cards/add
    @SendTo("/topic/cards/add")
    public Card addCardMessage(CustomPair<Long, Card> pair){
        Long id = pair.getId();
        Card card = pair.getVar();

        addCard(id, card);
        msgs.convertAndSend("/topic/cards/add", card);
        return card;
    }

    @PostMapping("/{id}/cards")
    public ResponseEntity<Card> addCard(@PathVariable("id") Long id, @RequestBody Card card){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        if(isNullOrEmpty(card.title)){
            return ResponseEntity.badRequest().build();
        }

        for(Tag tag : card.tagList){
            tagRepo.save(tag);
        }

        Card saved = cardRepo.save(card);
        CardList list = repo.findById(id).get();
        list.cards.add(saved);
        CardList savedList = repo.save(list);

        return ResponseEntity.ok(saved);
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

        removeCardListFromItsBoard(id);
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/lists/update")// /app/cards/update
    @SendTo("/topic/lists/update")
    public CardList deleteCardMessage(CustomPair<Long, CardList> pair) {
        updateListTitle(pair.getId(), pair.getVar());
        msgs.convertAndSend("/topic/cards/delete", pair.getVar());
        return pair.getVar();
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

    @PutMapping("/{id}/board/{boardId}/{newPos}")
    public ResponseEntity<CardList> moveList(
            @PathVariable("id") long id,
            @PathVariable("boardId") long boardId,
            @PathVariable("newPos") int newPos
    ){
        CardList cardList = repo.findById(id).get();
        removeCardListFromItsBoard(id);

        Board board = boardRepo.findById(boardId).get();
        board.lists.add(newPos, cardList);
        boardRepo.save(board);

        return ResponseEntity.ok().build();
    }

    private ResponseEntity removeCardListFromItsBoard(long id) {
        CardList cardList = repo.findById(id).get();
        var boardResponse = getBoard(id);
        if (boardResponse.getStatusCode() == HttpStatus.BAD_REQUEST)
            return ResponseEntity.badRequest().build();

        Board prevBoard = boardResponse.getBody();
        prevBoard.lists.remove(cardList);
        boardRepo.save(prevBoard);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/board")
    public ResponseEntity<Board> getBoard(@PathVariable("id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        CardList cardList = repo.findById(id).get();
        for(Board board : boardRepo.findAll()){
            if(board.lists.contains(cardList)){
                return ResponseEntity.ok(board);
            }
        }
        return ResponseEntity.badRequest().build();
    }

}