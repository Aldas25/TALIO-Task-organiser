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
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import commons.*;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ServerUtils {

    private final String httpPrefix = "http://";
    private final String webSocketPrefix = "ws://";
    private final String webSocketSuffix = "websocket";
    private String server = null;


    public <T> void registerForMessages(String dest,Class<T> type, Consumer<T> consumer){
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    private StompSession session;

    /**
     * Creates a new StompSession for WebSockets
     * @param url The connection URL
     * @return a StompSession or a response for failure
     */
    private StompSession connect(String url){
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        catch(ExecutionException e){
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    public void send(String dest, Object o){
        session.send(dest, o);
    }

    public String getHttpServer() {
        return httpPrefix + server;
    }

    public String getWebSocketServer() {
        String url = webSocketPrefix + server;
        if (url.charAt(url.length()-1) != '/') url += "/";
        url += webSocketSuffix;
        return url;
    }

    /**
     * Sets server to correct URL, and prints response
     * @param server the Server URL
     */
    public void setServer(String server) {
        this.server = server;
    }

    public String getServer() {
        return this.server;
    }

    /**
     * Connects session to the same port as the server.
     */
    public void setSession(){
        session = connect(getWebSocketServer());
    }

    /**
     * Checks if server URL is correct
     * @return True if URL is correct, false if not
     */
    public boolean isServerOk() {
        try {
            return ClientBuilder.newClient(new ClientConfig())
                    .target(getHttpServer()).path("status")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(Boolean.class);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if password is correct
     * @param enteredPassword the password used by user
     * @return true if the password is correct, false otherwise
     */
    public boolean checkAdminPassword(String enteredPassword) {
        return ClientBuilder.newClient(new ClientConfig())
                    .target(getHttpServer()).path("/api/admin/checkPassword")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.entity(enteredPassword, APPLICATION_JSON), boolean.class);

    }

    /**
     * Gets all cardlists connected to board
     * @param board The board chosen
     * @return The list of CardList connected to that board
     */
    public List<CardList> getCardListForBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(getHttpServer()).path("api/boards/" + board.id + "/lists")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<CardList>>() {});
    }

    /**
     * Updates the title of the list
     * @param list the list to be updated
     */
    public void updateListTitle(CardList list){
        send("/app/lists/update", new CustomPair<Long, CardList>(list.id, list));
    }

    /**
     * Adds a new list to current board
     * @param board the board
     * @param cardList the card list to add
     */
    public void addListToCurrentBoard(Board board, CardList cardList){
        send("/app/lists/add",
                new CustomPair<>(board.id, cardList));
    }

    /**
     * Removes a CardList from server
     * @param list The CardList that needs to be removed
     */
    public void removeCardList(CardList list) {
        send("/app/lists/delete", list.id);
    }

    /**
     * Adds a card to a cardlist in server
     * @param card The card to add
     * @param list The list to add
     */
    public void addCard(Card card, CardList list) {
        // session can only send one object, therefore we pair them up
        send("/app/cards/add", new CustomPair<Long, Card>(list.id, card));
    }

    /**
     * Updates the title of a card in server
     * @param card The card that is changing
     */


    public void updateCardTitle(Card card) {
        send("/app/cards/update", new CustomPair<Long, Card>(card.id, card));
    }


    /**
     * Remove card from the server
     * @param card The card that is removed
     */
    public void removeCard(Card card) {
        send("/app/cards/delete", card.id);
    }

    /**
     * Returns the cards from a CardList
     * @param list The CardList we want to retrieve from
     * @return A list of cards connected to the CardList
     */
    public List<Card> getCardsForList(CardList list) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(getHttpServer()).path("api/lists/" + list.id + "/cards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Card>>() {});
    }

    /**
     * Moves a card to a new list
     * @param card The card to be moved
     * @param list The list where it goes
     * @param newPos Its position in the list
     */
    public void moveCardToList(Card card, CardList list, int newPos) {
        send(
                "/app/cards/move",
                new CustomPair(card, new CustomPair(list, newPos))
        );
//        return ClientBuilder.newClient(new ClientConfig())
//                .target(getHttpServer())
//                .path("api/cards/" + card.id + "/list/" + list.id + "/" + newPos)
//                .request(APPLICATION_JSON)
//                .accept(APPLICATION_JSON)
//                .put(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    private final ExecutorService exec = Executors.newSingleThreadExecutor();

    /**
     * @param consumer functional interface that is used to consume data
     */
    public void registerForUpdates(Consumer<Board> consumer) {
        // create a new thread, so it can run in the background
        // (otherwise the application would just wait for updates and block everything else)
        exec.submit(() ->
        {
            // everything here runs in a different thread
            // as long as the thread is uninterrupted we are polling it constantly
            while(!Thread.interrupted()){
                var res =  ClientBuilder.newClient(new ClientConfig())
                        .target(getHttpServer())
                        .path("api/boards/admin/update")
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);

                // if there is NO CONTENT (status code 204) we continue
                if(res.getStatus() == 204){
                    continue;
                }
                var board = res.readEntity(Board.class);
                consumer.accept(board);
            }
        });
    }

    public void stop(){
        exec.shutdownNow();
    }
    /**
     * Adds board to server
     *
     * @param board the board to be added
     * @return board that has been added
     */
    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(getHttpServer()).path("api/boards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }


    /**
     * Remove board from server
     * @param board to be removed
     */
    public void removeBoard(Board board){send("/app/boards/delete", board.id);}


    /**
     * Update board title in server
     * @param board to be updated
     */
    public void updateBoardTitle(Board board){
        send("/app/boards/update", new CustomPair<Long, Board>(board.id, board));}

    /**
     * Retrieves all boards
     * @return The list of all boards
     */
    public List<Board> getBoards () {
        return ClientBuilder.newClient(new ClientConfig())
                .target(getHttpServer()).path("api/boards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Board>>() {});
    }

    /**
     * Get board by entered key
     * @param enteredKey the entered key
     * @return The board with that key
     */
    public Board getBoardByInviteKey(String enteredKey){
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(getHttpServer()).path("api/boards/byKey/" + enteredKey)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();
        Board board = null; // will return this, if not found will be null
        try {
            if (response.getStatus() == 200) {
                board = response.readEntity(Board.class);
            }
        } finally {
            response.close();
        }

        return board;
    }
}