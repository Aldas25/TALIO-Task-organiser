package server.services;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;

import java.util.List;

@Service
public class CardListService {

    private final CardListRepository repo;
    private final CardRepository cardRepo;
    private final BoardRepository boardRepo;

    public CardListService(CardListRepository repo,
                              CardRepository cardRepo,
                              BoardRepository boardRepo) {
        this.repo = repo;
        this.cardRepo = cardRepo;
        this.boardRepo = boardRepo;
    }

    public List<CardList> getAll() {
        return repo.findAll();
    }

    public CardList getById(long id) throws Exception {
        checkId(id);
        return repo.findById(id).orElseThrow(Exception::new);
    }

    public List<Card> getCardsForList(long id) throws Exception {
        checkId(id);
        CardList list = repo.findById(id).orElseThrow(Exception::new);
        return list.cards;
    }

    public Card addCard(Long id, Card card) throws Exception {
        checkId(id);
        if(isNullOrEmpty(card.title)){
            throw new Exception();
        }

        Card saved = cardRepo.save(card);
        CardList list = repo.findById(id).orElseThrow(Exception::new);
        list.cards.add(saved);
        repo.save(list);

        return saved;
    }

    public void deleteList(Long id) throws Exception {
        checkId(id);
        removeCardListsFromItsBoard(id);
        repo.deleteById(id);
    }

    public CardList updateListTitle(long id, String newTitle) throws Exception {
        checkId(id);
        CardList listFromRepo = repo.findById(id).orElseThrow(Exception::new);
        listFromRepo.title = newTitle;
        return repo.save(listFromRepo);
    }

    public Board getBoard(long id) throws Exception {
        checkId(id);
        CardList cardList = repo.findById(id).orElseThrow(Exception::new);
        for(Board board : boardRepo.findAll()){
            if(board.lists.contains(cardList)){
                return board;
            }
        }
        throw new Exception(); // board was not found
    }

    private void checkId(long id) throws Exception {
        if (id < 0 || !repo.existsById(id))
            throw new Exception();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private void removeCardListsFromItsBoard(long id) throws Exception {
        CardList cardList = repo.findById(id).orElseThrow(Exception::new);
        Board prevBoard = getBoard(id);
        prevBoard.lists.remove(cardList);
        boardRepo.save(prevBoard);
    }

}
