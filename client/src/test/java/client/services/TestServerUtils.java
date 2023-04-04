package client.services;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardList;

import java.util.ArrayList;
import java.util.List;

public class TestServerUtils extends ServerUtils {

    public List<String> log = new ArrayList<>();

    public List<Board> boards = new ArrayList<>();

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

    @Override
    public boolean isServerOk() {
        log.add("isServerOk");
        return true;
    }

    @Override
    public String getServer() {
        log.add("getServer");
        return "url";
    }

    @Override
    public Board getBoardbyInviteKey(String enteredKey) {
        log.add("getBoardByInviteKey " + enteredKey);
        Board board = null;
        if (enteredKey.equals("key")) {
            board = new Board("Board title", new ArrayList<>());
            board.id = 5;
            board.inviteKey = "key";
        }
        return board;
    }

    @Override
    public void addBoard(Board board){
        log.add("addBoard "+ board.id + " " + board.title);
        boards.add(board);
    }

    @Override
    public void addListToCurrentBoard(Board board, CardList cardList){
        log.add("addListToCurrentBoard " + board.id + " " + cardList.id + " " + cardList.title);
        board.lists.add(cardList);
    }

}
