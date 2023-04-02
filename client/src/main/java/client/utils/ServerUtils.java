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

    /**
     * Sets server to correct URL, and prints response
     * @param server the Server URL
     */
    public void setServer(String server) {
        this.server = server;
        System.out.println("Server set to: " + server);
    }

    /**
     * Connects session to the same port as the server.
     */
    public void setSession(){
        session = connect("ws://localhost:8080/websocket");
    }

    /**
     * Checks if server URL is correct
     * @return True if URL is correct, false if not
     */
    public boolean isServerOk() {
        try {
            return ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("status")
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
                    .target(server).path("/api/admin/checkPassword")
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
                .target(server).path("api/boards/" + board.id + "/lists")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<CardList>>() {});
    }

    /**
     * Adds a CardList to api/lists
     * @param list The new CardList
     * @return The CardList added
     */
    public CardList addCardList(CardList list) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/lists")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(list, APPLICATION_JSON), CardList.class);
    }

    /**
     * Updates the title of a CardList in server
     * @param list The CardList that changes
     * @return The new CardList with new title
     */
    public CardList updateCardListTitle(CardList list) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/lists/" + list.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(list, APPLICATION_JSON), CardList.class);
    }

    /**
     * Removes a CardList from server
     * @param list The CardList that needs to be removed
     * @return A server Response
     */
    public Response removeCardList(CardList list) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/lists/" + list.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
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
     * Adds a Tag to server
     * @param tag The tag to add
     * @return The tag created
     */
    public Tag addTag(Tag tag){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/tags/" + tag.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * Updates an existing tag to server
     * @param tag The tag that changes
     * @return The new changed tag
     */
    public Tag updateTag(Tag tag){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/tags/" + tag.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * Updates the title of a card in server
     * @param card The card that is changing
     * @return The changed card
     */
    public Card updateCardTitle(Card card) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("/api/cards/" + card.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * Remove card from the server
     * @param card The card that is removed
     * @return server Response
     */
    public Response removeCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("/api/cards/" + card.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * Returns the cards from a CardList
     * @param list The CardList we want to retrieve from
     * @return A list of cards connected to the CardList
     */
    public List<Card> getCardsForList(CardList list) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/lists/" + list.id + "/cards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Card>>() {});
    }

    /**
     * Moves a card to a new list
     * @param card The card to be moved
     * @param list The list where it goes
     * @param newPos Its position in the list
     * @return The Card that was moved
     */
    public Card moveCardToList(Card card, CardList list, int newPos) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/cards/" + card.id + "/list/" + list.id + "/" + newPos)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * Removes a board from server
     * @param board The board that needs to be removed
     * @return A server Response
     */
    public Response removeBoard(Board board){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + board.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * Updates the title of a Board
     * @param board The board that needs to change
     * @return The changed board
     */
    public Board updateBoardTitle(Board board){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + board.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * Retrieves all boards
     * @return The list of all boards
     */
    public List<Board> getBoards () {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Board>>() {});
    }
}