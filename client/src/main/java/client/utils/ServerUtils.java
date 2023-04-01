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

    public void setServer(String server) {
        this.server = server;
        System.out.println("Server set to: " + server);
    }
    /*
        Connects session to the same port as the server.
     */
    public void setSession(){
        session = connect("ws://localhost:8080/websocket");
    }

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

    public List<CardList> getCardListForBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + board.id + "/lists")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<CardList>>() {});
    }

    public CardList addCardList(CardList list) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/lists")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(list, APPLICATION_JSON), CardList.class);
    }

    public CardList updateCardListTitle(CardList list) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/lists/" + list.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(list, APPLICATION_JSON), CardList.class);
    }

    public Response removeCardList(CardList list) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/lists/" + list.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    public void addCard(Card card, CardList list) {
        // session can only send one object, therefore we pair them up
        send("/app/cards/add", new CustomPair<Long, Card>(list.id, card));
    }

    public Tag addTag(Tag tag){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/tags/" + tag.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }
    public Tag updateTag(Tag tag){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/tags/" + tag.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }
    public Card updateCardTitle(Card card) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("/api/cards/" + card.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public Response removeCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("/api/cards/" + card.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    public List<Card> getCardsForList(CardList list) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/lists/" + list.id + "/cards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Card>>() {});
    }

    public Card moveCardToList(Card card, CardList list, int newPos) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/cards/" + card.id + "/list/" + list.id + "/" + newPos)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public Response removeBoard(Board board){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + board.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    public Board updateBoardTitle(Board board){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/" + board.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    public List<Board> getBoards () {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Board>>() {});
    }
}