package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;

import java.util.ArrayList;
import java.util.List;

public class BoardService {

    private final ServerUtils server;
    private final JoinedBoardsService joinedBoardsService;

    private Board currentBoard;

    /**
     * Constructor of BoardService
     * @param server Reference to ServerUtils
     * @param joinedBoardsService Reference to JoinedBoardsService
     */
    @Inject
    public BoardService(ServerUtils server,
                        JoinedBoardsService joinedBoardsService) {
        this.server = server;
        this.joinedBoardsService = joinedBoardsService;
    }

    /**
     * Retrieves the current board
     * @return The current board
     */
    public Board getCurrentBoard() {
        return currentBoard;
    }

    /**
     * Sets the current board
     * @param currentBoard The current board
     */
    public void setCurrentBoard(Board currentBoard) {
        this.currentBoard = currentBoard;
    }

    /**
     * Retrieves the invite key of a board
     * @return The invite key of the current board
     */
    public String getBoardInviteKey() {
        return currentBoard.inviteKey;
    }

    /**
     * Gets list of CardList for current board
     * @return The lists of CardList of the current board
     */
    public List<CardList> getListsForCurrentBoard() {
        return server.getCardListForBoard(currentBoard);
    }

    /**
     * Adds a new board to the server
     * @param title The title of the new board
     */
    public void addBoard(String title) {
        Board newBoard = new Board(title, new ArrayList<>());
        server.addBoard(newBoard);
        joinedBoardsService.joinBoardAndSave(newBoard);
    }

    /**
     * Adds a new CardList to a board
     * @param title The title of the CardList
     */
    public void addListToCurrentBoard(String title) {
        CardList list = new CardList(title, new ArrayList<>());
        server.addListToCurrentBoard(currentBoard, list);
    }

    /**
     * Updates the title of the board
     * @param board The current board
     * @param newTitle The new title of the board
     */
    public void updateBoardTitle(Board board, String newTitle) {
        board.title = newTitle;
        server.updateBoardTitle(board);
    }

}
