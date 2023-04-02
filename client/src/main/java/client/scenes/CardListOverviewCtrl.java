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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.Main;
import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CardListOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField inviteKeyField;
    @FXML
    private HBox listContainer;
    @FXML
    private ImageView disconnectImageView;
    @FXML
    private ImageView addImageView;

    private Board board;

    @Inject
    public CardListOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        resetDisconnectImageView();
        resetAddImageView();
    }

    public void resetDisconnectImageView () {
        File disconnectFile = new
                File ("client/src/main/java/client/images/card-list-overview/disconnect1.png");
        Image disconnectImage = new Image (disconnectFile.toURI().toString());
        disconnectImageView.setImage(disconnectImage);
    }

    public void resetAddImageView () {
        File addFile = new File ("client/src/main/java/client/images/card-list-overview/add1.png");
        Image addImage = new Image (addFile.toURI().toString());
        addImageView.setImage(addImage);
    }

    public void setBoard (Board board) {
        this.board = board;
    }

    /**
     * start() method is invoked when client connects to a valid server.
     */
    public void start(){
        server.registerForMessages("/topic/lists/add", CardList.class, list -> {
            Platform.runLater(() -> refresh());
        });
        server.registerForMessages("/topic/lists/delete", long.class, id -> {
            Platform.runLater(() -> refresh());
        });
        server.registerForMessages("/topic/lists/update", CardList.class, cardList -> {
            Platform.runLater(() -> refresh());
        });
        server.registerForMessages("/topic/cards/move", CardList.class, cardList -> {
            Platform.runLater(() -> refresh());
        });
        server.registerForMessages("/topic/cards/delete", long.class, id -> {
            Platform.runLater(() -> refresh());
        });
        server.registerForMessages("/topic/cards/add", Card.class, card -> {
            Platform.runLater(() -> mainCtrl.showListOverview());
        });
        server.registerForMessages("/topic/cards/update", Card.class, card -> {
            Platform.runLater(() -> mainCtrl.showListOverview());
        });
    }

    public void refresh() {
        listContainer.getChildren().clear();

        List<CardList> allLists = server.getCardListForBoard(board);
        for (CardList cardList : allLists) {
            AnchorPane listNode = loadCardListNode(cardList);

            // adding node to children of listContainer
            listContainer.getChildren().add(listNode);
        }
        showInviteKey();
    }

    public AnchorPane loadCardListNode(CardList cardList) {
        var listTemplate =
                Main.load(ListTemplateCtrl.class, "client", "scenes", "ListTemplate.fxml");

        ListTemplateCtrl listTemplateCtrl = listTemplate.getKey();
        AnchorPane listNode = (AnchorPane) listTemplate.getValue();
        listTemplateCtrl.start(cardList, board);

        // retrieving text from a copy of the file ListTemplate
        TextField textField = (TextField) listNode.getChildren().get(0);
        textField.setText(cardList.title); // setting title to new node

        // each list contains a Vertical Box with all its Cards
        VBox listBox = (VBox) listNode.getChildren().get(1);
        listBox.getChildren().clear();
        listBox.setSpacing(10); // set spacing between the cards within a list

        for (Card card : server.getCardsForList(cardList)) {
            AnchorPane cardNode = loadCardNode(card, listTemplateCtrl);
            listBox.getChildren().add(cardNode); // add this card to the children of the VBox
        }

        // adding "new list" button
        listBox.getChildren().add(listTemplateCtrl.getAddCardButton());

        return listNode;
    }

    public AnchorPane loadCardNode(Card card, ListTemplateCtrl listTemplateCtrl) {
        var cardTemplate =
                Main.load(CardTemplateCtrl.class, "client", "scenes", "CardTemplate.fxml");

        CardTemplateCtrl cardTemplateCtrl = cardTemplate.getKey();
        AnchorPane cardNode = (AnchorPane) cardTemplate.getValue();
        cardTemplateCtrl.start(card, listTemplateCtrl);

        // retrieve name of the Card from the Text Box
        Text cardText = (Text) cardNode.getChildren().get(0);
        cardText.setText(card.title); // set the name of the Card

        cardNode = addTags(cardNode, card);
        return cardNode;
    }

    public void addNewList() {
        CardList list = new CardList("New list", new ArrayList<>());
        server.send("/app/lists/add", new CustomPair<>(board.id, list));
    }

    public void disconnectFromBoard() {
        mainCtrl.showBoardOverview();
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
            tagBox.setSpacing(5);
            tagBox.getChildren().add(tagNode);
        }

        return cardNode;
    }

    public void disconnectOnMouseEntered() {
        File disconnectFile = new
                File ("client/src/main/java/client/images/card-list-overview/disconnect2.png");
        Image disconnectImage = new Image (disconnectFile.toURI().toString());
        disconnectImageView.setImage(disconnectImage);
    }

    public void disconnectOnMouseExited() {
        resetDisconnectImageView();
    }

    public void addOnMouseEntered() {
        File addFile = new File ("client/src/main/java/client/images/card-list-overview/add2.png");
        Image addImage = new Image (addFile.toURI().toString());
        addImageView.setImage(addImage);
    }

    public void addOnMouseExited() {
        resetAddImageView();
    }

    public void showInviteKey(){
        inviteKeyField.setText(String.valueOf(board.inviteKey));
    }

}