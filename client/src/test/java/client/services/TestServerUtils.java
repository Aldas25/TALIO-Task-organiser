package client.services;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardList;

import java.util.ArrayList;
import java.util.List;

public class TestServerUtils extends ServerUtils {

    public List<String> log = new ArrayList<>();

    @Override
    public List<CardList> getCardListForBoard(Board board) {
        log.add("getCardListForBoard " + board.id);
        return board.lists;
    }

    @Override
    public List<Card> getCardsForList(CardList list){
        log.add("getCardsForList " + list.id);
        return list.cards;
    }


    @Override
    public void addCard(Card card, CardList list) {
        list.cards.add(card);
        log.add("addCard " + list.id + " " + card.id + " " + card.title);
    }

    @Override
    public void updateCardTitle(Card card) {
        log.add("updateCardTitle " + card.id + " " + card.title);
    }

}
