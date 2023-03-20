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

import java.util.List;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private String server = null;

    public void setServer(String server) {
        this.server = server;
        System.out.println("Server set to: " + server);
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

    public List<CardList> getCardLists() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/lists")
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
    public Card addCard(Card card, CardList list) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/lists/" + list.id + "/cards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
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

}