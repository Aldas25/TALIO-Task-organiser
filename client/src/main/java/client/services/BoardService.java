package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;

import java.util.ArrayList;
import java.util.List;

public class BoardService {

    private final ServerUtils server;

    private Board currentBoard;

    @Inject
    public BoardService(ServerUtils server) {
        this.server = server;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public void setCurrentBoard(Board currentBoard) {
        this.currentBoard = currentBoard;
    }

    public String getBoardInviteKey() {
        return currentBoard.inviteKey;
    }

    public List<CardList> getListsForCurrentBoard() {
        return server.getCardListForBoard(currentBoard);
    }

    public void addBoard(String title) {
        Board newBoard = new Board(title, new ArrayList<>());
        server.addBoard(newBoard);
    }

    public void addListToCurrentBoard(String title) {
        CardList list = new CardList(title, new ArrayList<>());
        server.addListToCurrentBoard(currentBoard, list);
    }

}
