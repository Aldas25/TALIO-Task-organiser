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
import commons.Tag;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
    @FXML
    private Button disconnectButton;
    @FXML
    private VBox content;

    @Inject
    public CardListOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * start() method is invoked when client connects to a valid server.
     */
    public void start(){
        server.registerForMessages("/topic/lists/add", CardList.class, list -> {
            Platform.runLater(() -> refresh());
        });
    }


    public void refresh() {
        listContainer.getChildren().clear();

        List<CardList> allLists = server.getCardLists();
        for (CardList cardList : allLists) {
            var listTemplate =
                    Main.load(ListTemplateCtrl.class, "client", "scenes", "ListTemplate.fxml");

            ListTemplateCtrl listTemplateCtrl = listTemplate.getKey();
            AnchorPane listNode = (AnchorPane) listTemplate.getValue();
            listTemplateCtrl.setList(cardList);

            // retrieving text from a copy of the file ListTemplate
            TextField textField = (TextField) listNode.getChildren().get(0);
            textField.setText(cardList.title);                  // setting title to new node

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

                cardNode = addTags(cardNode, card);

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
        server.send("/app/lists/add", list);
    }

    public AnchorPane addTags(AnchorPane cardNode, Card card){
        HBox tagBox = (HBox) cardNode.getChildren().get(1);
        for(Tag tag : card.tagList){
            var tagTemplate =
                    Main.load(TagTemplateCtrl.class,
                            "client", "scenes", "TagTemplate.fxml");
            AnchorPane tagNode = (AnchorPane) tagTemplate.getValue();
            tagNode.setStyle("-fx-background-color: " + tag.color + ";" +
                    "-fx-background-radius: 5;");
            Text tagTitle = (Text) tagNode.getChildren().get(0);
            tagTitle.setText(tag.title);
            tagBox.setSpacing(2);
            tagBox.getChildren().add(tagNode);
        }

        return cardNode;
    }

    public void disconnectFromServer() {
        mainCtrl.disconnectFromServer();
    }

    public void disconnectButtonOnMouseEnter() {
        disconnectButton.setStyle("-fx-background-color: #b0bfd4; -fx-border-color: #6D85A8");
    }

    public void disconnectButtonOnMouseExited() {
        disconnectButton.setStyle("-fx-background-color: #d1dae6; -fx-border-color: #6D85A8");
    }

    public void addListButtonOnMouseEntered() {
        addListButton.setStyle("-fx-background-color: #b0bfd4; -fx-border-color: #6D85A8");
    }

    public void addListButtonOnMouseExited() {
        addListButton.setStyle("-fx-background-color: #d1dae6; -fx-border-color: #6D85A8");
    }

}