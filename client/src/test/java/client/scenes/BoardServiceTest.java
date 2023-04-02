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
package client.scenes;

import client.services.BoardService;
import client.utils.ServerUtils;
import commons.Board;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardServiceTest {

    private BoardService sut;
    private TestServerUtils server;

    @BeforeEach
    public void setup() {
        server = new TestServerUtils();
        sut = new BoardService(server);
    }

    @Test
    public void testGetCurrentBoardNull() {
        assertNull(sut.getCurrentBoard());
    }

    @Test
    public void testSetCurrentBoard() {
        Board b = new Board("Title", new ArrayList<>());
        b.id = 0;
        sut.setCurrentBoard(b);
        assertEquals(b, sut.getCurrentBoard());
    }

    @Test
    public void testGetCardLists() {
        CardList list1 = new CardList("list1", new ArrayList<>());
        list1.id = 1;
        CardList list2 = new CardList("list2", new ArrayList<>());
        list2.id = 2;
        List<CardList> expected = List.of(list1, list2);

        Board b = new Board("Title", expected);
        b.id = 0;

        sut.setCurrentBoard(b);

        assertEquals(expected, sut.getListsForCurrentBoard());
        assertTrue(server.log.contains("getCardListForBoard 0"));
    }

    class TestServerUtils extends ServerUtils {

        public List<String> log = new ArrayList<>();

        @Override
        public List<CardList> getCardListForBoard(Board board) {
            log.add("getCardListForBoard " + board.id);
            return board.lists;
        }

    }
}