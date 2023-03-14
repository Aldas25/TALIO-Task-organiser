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

import java.util.List;

import client.Main;
import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CardListOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private HBox listContainer;
    @FXML
    private Button addListButton;

    @Inject
    public CardListOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void refresh() {
        listContainer.getChildren().clear();
        listContainer.setSpacing(5);            //spacing between lists

        List<CardList> allLists = server.getCardLists();

        for (CardList cardList : allLists) {

            var listTemplate =
                    Main.load(ListTemplateCtrl.class, "client", "scenes", "ListTemplate.fxml");

            ListTemplateCtrl listTemplateCtrl = listTemplate.getKey();
            AnchorPane listNode = (AnchorPane) listTemplate.getValue();
            listTemplateCtrl.setList(cardList);

            // retrieving text from a copy of the file ListTemplate
            Text text = (Text) listNode.getChildren().get(0);
            text.setText(cardList.title);                  // setting title to new node

            // each list contains a Vertical Box with all its Cards
            VBox listBox = (VBox) listNode.getChildren().get(1);
            listBox.getChildren().clear();
            listBox.setSpacing(10); // set spacing between the cards within a list

            for (Card card : server.getCardsForList(cardList)) {

                var cardTemplate =
                        Main.load(CardTemplateCtrl.class, "client", "scenes", "CardTemplate.fxml");

                CardTemplateCtrl cardTemplateCtrl = cardTemplate.getKey();
                AnchorPane cardNode = (AnchorPane) cardTemplate.getValue();
                cardTemplateCtrl.setCard(card);
                cardTemplateCtrl.setCurrentListCtrl(listTemplateCtrl);

                // retrieve name of the Card from the Text Box
                Text cardText = (Text) cardNode.getChildren().get(0);
                cardText.setText(card.title); // set the name of the Card

                listBox.getChildren().add(cardNode); // add this card to the children of the VBox
            }

            // adding "new list" button
            listBox.getChildren().add(listTemplateCtrl.getAddCardButton());

            // adding node to children of listContainer
            listContainer.getChildren().add(listNode);
        }

        listContainer.getChildren().add(addListButton);
    }

    public void addNewList() {
        CardList list = new CardList("New list");
        server.addCardList(list);
        refresh();
    }

    public void disconnectFromServer() {
        mainCtrl.disconnectFromServer();
    }
}