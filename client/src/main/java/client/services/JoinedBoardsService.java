package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class JoinedBoardsService {

    private final ServerUtils server;
    private String filename;
    private List<JoinedBoard> joinedBoards = new ArrayList<>();

    /**
     * Constructor for JoinedBoardService
     * @param server Reference to ServerUtils
     */
    @Inject
    public JoinedBoardsService(ServerUtils server) {
        this.server = server;
    }

    /**
     * Gets current file name
     * @return The string with the file name
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * Sets the file name
     * @param filename The file name
     */
    public void setFilename(String filename) {
        String filePrefix = "client/";
        String fileSuffix = ".txt";
        this.filename = filePrefix + filename + fileSuffix;
    }

    /**
     * Reads joined boards from a file
     */
    public void readJoinedBoardsFromFile() {
        try {
            Scanner scanner = new Scanner(new File(filename));
            readJoinedBoards(scanner);
            scanner.close();
        } catch (FileNotFoundException e) {
            this.joinedBoards = new ArrayList<>();
            writeJoinedBoardsToFileInThread();
        }
    }

    /**
     * Reads joined boards
     * @param scanner The java iterator to scan strings
     */
    public void readJoinedBoards(Scanner scanner) {
        joinedBoards = new ArrayList<>();
        while (scanner.hasNext()) {
            String url = scanner.next();
            String key = scanner.next();
            joinedBoards.add(new JoinedBoard(url, key));
        }
    }

    /**
     * Writes joined boards to file
     */
    public void writeJoinedBoardsToFileInThread() {
        Executors.newSingleThreadExecutor().submit(this::writeJoinedBoardsToFile);
    }

    public void writeJoinedBoardsToFile() {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(filename));
            writeJoinedBoards(printWriter);
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes joined boards on a string
     * @param printWriter The printWriter
     */
    public void writeJoinedBoards(PrintWriter printWriter) {
        for (JoinedBoard board : joinedBoards) {
            printWriter.println(board);
        }
    }

    /**
     * Retrieves the joined boards
     * @return The list of boards
     */
    public List<Board> getJoinedBoards() {
        // filter out boards that are optional.empty()
        return joinedBoards.stream()
                .map(JoinedBoard::getBoard)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Joins a board
     * @param board The board to join
     */
    public void joinBoard(Board board) {
        for (JoinedBoard joinedBoard : joinedBoards) {
            var optionalBoard = joinedBoard.getBoard();
            if (optionalBoard.isPresent() && optionalBoard.get().equals(board))
                return;
        }

        joinedBoards.add(new JoinedBoard(server.getServer(), board.inviteKey));
    }

    /**
     * Leaves a board
     * @param board The board to leave
     */
    public void leaveBoard(Board board) {
        for (int i = 0; i < joinedBoards.size(); i++) {
            if (joinedBoards.get(i).equalsToBoard(board)) {
                joinedBoards.remove(i);
                return;
            }
        }
    }

    /**
     * Join boards and saves
     * @param board The board to join and save
     */
    public void joinBoardAndSave(Board board) {
        joinBoard(board);
        writeJoinedBoardsToFileInThread();
    }

    /**
     * Leaves board and saves
     * @param board The board to leave and save
     */
    public void leaveBoardAndSave(Board board) {
        leaveBoard(board);
        writeJoinedBoardsToFileInThread();
    }


    class JoinedBoard {

        private final String url;
        private final String key;

        /**
         * The constructor of JoinedBOard
         * @param url The server url
         * @param key The board of the key
         */
        public JoinedBoard(String url, String key) {
            this.url = url;
            this.key = key;
        }

        /**
         * Equals for the board
         * @param board The board to check
         * @return True if equal, False if not
         */
        public boolean equalsToBoard(Board board) {
            return (server.getServer().equals(this.url)
                    && board.inviteKey.equals(key));
        }

        /**
         * Retrieves a board
         * @return An Optional for the Board
         */
        public Optional<Board> getBoard() {
            if (!server.isServerOk() || !server.getServer().equals(this.url))
                return Optional.empty();

            Board board = server.getBoardByInviteKey(key);
            if (board == null)
                return Optional.empty();

            return Optional.of(board);
        }

        @Override
        public String toString() {
            return url + " " + key;
        }
    }
}
