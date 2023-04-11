package server.services;

import commons.Card;
import commons.CardList;
import org.springframework.stereotype.Service;
import server.database.CardListRepository;
import server.database.CardRepository;

import java.util.List;

@Service
public class CardService {

    private final CardRepository repo;
    private final CardListRepository cardListRepo;

    public CardService(CardRepository repo, CardListRepository cardListRepo) {
        this.repo = repo;
        this.cardListRepo = cardListRepo;
    }

    public List<Card> getAll() {
        return repo.findAll();
    }

    public Card getById(long id) throws Exception {
        checkCardId(id);
        return repo.findById(id).orElseThrow(Exception::new);
    }

    public void deleteCard(long id) throws Exception {
        removeCardFromItsCardList(id);
        repo.deleteById(id);
    }

    public Card updateCardTitle(long id, Card card) throws Exception {
        Card cardFromRepo = getById(id);
        cardFromRepo.title = card.title;
        return repo.save(cardFromRepo);
    }

    public Card moveCard(long id, long listId, int newPos) throws Exception {
        Card card = getById(id);
        removeCardFromItsCardList(id);

        CardList list = cardListRepo.findById(listId).get();
        list.cards.add(newPos, card);
        cardListRepo.save(list);

        return card;
    }

    public CardList getCardListById(long id) throws Exception {
        Card card = getById(id);
        for(CardList list : cardListRepo.findAll()) {
            if(list.cards.contains(card)){
                return list;
            }
        }

        throw new Exception();
    }

    private void removeCardFromItsCardList(long id) throws Exception {
        Card card = getById(id);
        CardList cardList = getCardListById(id);
        // cardListRepo.delete(cardList);
        cardList.cards.remove(card);
        cardListRepo.save(cardList);
    }

    private void checkCardId(long id) throws Exception {
        if (id < 0 || !repo.existsById(id))
            throw new Exception();
    }

}
