package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;

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
}
