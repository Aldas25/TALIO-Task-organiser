package client.services;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class JoinedBoardsService {

    private final ServerUtils server;
    private final String filename = "client/joinedBoards.txt";
    private List<JoinedBoard> joinedBoards = new ArrayList<>();

    @Inject
    public JoinedBoardsService(ServerUtils server) {
        this.server = server;
    }

    public void readJoinedBoardsFromFile() {
        try {
            Scanner scanner = new Scanner(new File(filename));
            readJoinedBoards(scanner);
            scanner.close();
        } catch (FileNotFoundException e) {
            this.joinedBoards = new ArrayList<>();
            writeJoinedBoardsToFile();
        }
    }

    public void readJoinedBoards(Scanner scanner) {
        joinedBoards = new ArrayList<>();
        while (scanner.hasNext()) {
            String url = scanner.next();
            String key = scanner.next();
            joinedBoards.add(new JoinedBoard(url, key));
        }
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

    public void writeJoinedBoards(PrintWriter printWriter) {
        for (JoinedBoard board : joinedBoards) {
            printWriter.println(board);
        }
    }

    public List<Board> getJoinedBoards() {
        // filter out boards that are optional.empty()
        return joinedBoards.stream()
                .map(JoinedBoard::getBoard)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void joinBoard(Board board) {
        for (JoinedBoard joinedBoard : joinedBoards) {
            var optionalBoard = joinedBoard.getBoard();
            if (optionalBoard.isPresent() && optionalBoard.get().equals(board))
                return;
        }

        joinedBoards.add(new JoinedBoard(server.getServer(), board.inviteKey));
    }

    public void leaveBoard(Board board) {
        for (int i = 0; i < joinedBoards.size(); i++) {
            if (joinedBoards.get(i).equalsToBoard(board)) {
                joinedBoards.remove(i);
                return;
            }
        }
    }

    public void joinBoardAndSave(Board board) {
        joinBoard(board);
        writeJoinedBoardsToFile();
    }

    public void leaveBoardAndSave(Board board) {
        leaveBoard(board);
        writeJoinedBoardsToFile();
    }

    class JoinedBoard {

        private final String url;
        private final String key;

        public JoinedBoard(String url, String key) {
            this.url = url;
            this.key = key;
        }

        public boolean equalsToBoard(Board board) {
            return (server.getServer().equals(this.url)
                    && board.inviteKey.equals(key));
        }

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
