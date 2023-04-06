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
import java.util.List;
import java.util.ResourceBundle;

import client.Main;
import client.services.BoardService;
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
import org.springframework.http.HttpStatus;

public class CardListOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final BoardService boardService;

    @FXML
    private TextField inviteKeyField;
    @FXML
    private HBox listContainer;
    @FXML
    private ImageView disconnectImageView;
    @FXML
    private ImageView addImageView;

    @Inject
    public CardListOverviewCtrl(ServerUtils server, MainCtrl mainCtrl,
                                BoardService boardService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
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

    /**
     * start() method is invoked when client connects to a valid server.
     */
    public void start(){
        server.registerForMessages("/topic/lists/update", HttpStatus.class, httpStatus -> {
            if (httpStatus.equals(HttpStatus.OK)){
                Platform.runLater(() -> refresh());
            }
        });
        server.registerForMessages("/topic/cards/move", CardList.class, cardList -> {
            Platform.runLater(() -> refresh());
        });
    }

    /**
     * Refreshes all the lists from server
     */
    public void refresh() {
        listContainer.getChildren().clear();

        List<CardList> allLists = boardService.getListsForCurrentBoard();
        for (CardList cardList : allLists) {
            AnchorPane listNode = loadCardListNode(cardList);

            // adding node to children of listContainer
            listContainer.getChildren().add(listNode);
        }
        showInviteKey();
    }

    /**
     * Loads the selected Card List Node
     * @param cardList The chosen Card List
     * @return The Node from that card list
     */
    public AnchorPane loadCardListNode(CardList cardList) {
        var listTemplate =
                Main.load(ListTemplateCtrl.class, "client", "scenes", "ListTemplate.fxml");

        ListTemplateCtrl listTemplateCtrl = listTemplate.getKey();
        AnchorPane listNode = (AnchorPane) listTemplate.getValue();
        listTemplateCtrl.start(cardList);

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

    /**
     * Loads the anchor pane of the card
     * @param card The selected card
     * @param listTemplateCtrl The list controller
     * @return The anchor pane of the card
     */
    public AnchorPane loadCardNode(Card card, ListTemplateCtrl listTemplateCtrl) {
        var cardTemplate =
                Main.load(CardTemplateCtrl.class, "client", "scenes", "CardTemplate.fxml");

        CardTemplateCtrl cardTemplateCtrl = cardTemplate.getKey();
        AnchorPane cardNode = (AnchorPane) cardTemplate.getValue();
        cardTemplateCtrl.start(card, listTemplateCtrl);

        // retrieve name of the Card from the Text Box
        Text cardText = (Text) cardNode.getChildren().get(0);
        cardText.setText(card.title); // set the name of the Card
        return cardNode;
    }

    public void addNewList() {
        boardService.addListToCurrentBoard("New List");
    }

    public void disconnectFromBoard() {
        mainCtrl.showBoardOverview();
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
        String inviteKey = boardService.getBoardInviteKey();
        inviteKeyField.setText(inviteKey);
    }

}