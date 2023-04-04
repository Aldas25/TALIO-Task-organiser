/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.services;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class JoinedBoardsServiceTest {

    private JoinedBoardsService sut;
    private TestServerUtils server;

    @BeforeEach
    public void setup() {
        server = new TestServerUtils();
        sut = new JoinedBoardsService(server);
    }

    @Test
    public void testJoinedBoardToString() {
        JoinedBoardsService.JoinedBoard joinedBoard;
        joinedBoard = sut.new JoinedBoard("url", "key");
        assertEquals("url key", joinedBoard.toString());
    }

    @Test
    public void testGetBoardWrongServer() {
        JoinedBoardsService.JoinedBoard joinedBoard;
        joinedBoard = sut.new JoinedBoard("url_wrong", "key");
        Optional<Board> optionalBoard = joinedBoard.getBoard();

        assertTrue(optionalBoard.isEmpty());
        assertTrue(server.log.contains("isServerOk"));
        assertTrue(server.log.contains("getServer"));
    }

    @Test
    public void testGetBoardNonExistingKey() {
        JoinedBoardsService.JoinedBoard joinedBoard;
        joinedBoard = sut.new JoinedBoard("url", "key_wrong");
        Optional<Board> optionalBoard = joinedBoard.getBoard();

        assertTrue(optionalBoard.isEmpty());
        assertTrue(server.log.contains("isServerOk"));
        assertTrue(server.log.contains("getServer"));
        assertTrue(server.log.contains("getBoardByInviteKey key_wrong"));
    }

    @Test
    public void testGetBoard() {
        JoinedBoardsService.JoinedBoard joinedBoard;
        joinedBoard = sut.new JoinedBoard("url", "key");
        Optional<Board> optionalBoard = joinedBoard.getBoard();

        assertTrue(optionalBoard.isPresent());

        Board board = optionalBoard.get();

        assertEquals("key", board.inviteKey);
        assertEquals(5, board.id);

        assertTrue(server.log.contains("isServerOk"));
        assertTrue(server.log.contains("getServer"));
        assertTrue(server.log.contains("getBoardByInviteKey key"));
    }

    @Test
    public void testJoinBoard() {
        Board boardToJoin = new Board("Board title", new ArrayList<>());
        boardToJoin.id = 5;
        boardToJoin.inviteKey = "key";

        sut.joinBoard(boardToJoin);
        List<Board> boards = sut.getJoinedBoards();

        assertEquals(1, boards.size());
        assertEquals(5, boards.get(0).id);
        assertEquals("key", boards.get(0).inviteKey);
        assertTrue(server.log.contains("getServer"));
    }

    @Test
    public void testJoinBoardAlreadyJoined() {
        Board boardToJoin = new Board("Board title", new ArrayList<>());
        boardToJoin.id = 5;
        boardToJoin.inviteKey = "key";

        sut.joinBoard(boardToJoin);
        sut.joinBoard(boardToJoin); // joining same board, should not join again
        List<Board> boards = sut.getJoinedBoards();

        assertEquals(1, boards.size());
        assertEquals(5, boards.get(0).id);
        assertEquals("key", boards.get(0).inviteKey);
        assertTrue(server.log.contains("getServer"));
    }

    @Test
    public void testJoinBoardAlreadyJoinedAndRead() {
        String str = "url_wrong key\nurl key_wrong\nurl key\n";
        Scanner scanner = new Scanner(str);
        sut.readJoinedBoards(scanner);

        Board boardToJoin = new Board("Board title", new ArrayList<>());
        boardToJoin.id = 5;
        boardToJoin.inviteKey = "key";

        sut.joinBoard(boardToJoin);
        sut.joinBoard(boardToJoin); // joining same board, should not join again
        List<Board> boards = sut.getJoinedBoards();

        assertEquals(1, boards.size());
        assertEquals(5, boards.get(0).id);
        assertEquals("key", boards.get(0).inviteKey);
        assertTrue(server.log.contains("getServer"));
    }

    @Test
    public void testWriteJoinedBoards() {
        Board boardToJoin = new Board("Board title", new ArrayList<>());
        boardToJoin.id = 5;
        boardToJoin.inviteKey = "key";

        sut.joinBoard(boardToJoin);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        sut.writeJoinedBoards(printWriter);
        String expected = "url key" + System.lineSeparator();
        String actual = stringWriter.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void readJoinedBoards() {
        String str = "url key\nurl_wrong key\nurl key_wrong\n";
        Scanner scanner = new Scanner(str);
        sut.readJoinedBoards(scanner);
        List<Board> boards = sut.getJoinedBoards();

        assertEquals(1, boards.size());
        assertEquals(5, boards.get(0).id);
        assertEquals("key", boards.get(0).inviteKey);
    }
}