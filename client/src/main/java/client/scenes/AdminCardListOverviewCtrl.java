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

public class AdminCardListOverviewCtrl implements Initializable {

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
    public AdminCardListOverviewCtrl(ServerUtils server, MainCtrl mainCtrl,
                                BoardService boardService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        resetDisconnectImageView();
    }

    public void resetDisconnectImageView () {
        File disconnectFile = new
                File ("client/src/main/java/client/images/card-list-overview/disconnect1.png");
        Image disconnectImage = new Image (disconnectFile.toURI().toString());
        disconnectImageView.setImage(disconnectImage);
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
        var adminListTemplate =
                Main.load(AdminListTemplateCtrl.class, "client", "scenes",
                        "AdminListTemplate.fxml");

        AdminListTemplateCtrl adminListTemplateCtrl = adminListTemplate.getKey();
        AnchorPane listNode = (AnchorPane) adminListTemplate.getValue();
        adminListTemplateCtrl.start(cardList);

        // retrieving text from a copy of the file ListTemplate
        TextField textField = (TextField) listNode.getChildren().get(0);
        textField.setText(cardList.title); // setting title to new node

        // each list contains a Vertical Box with all its Cards
        VBox listBox = (VBox) listNode.getChildren().get(1);
        listBox.getChildren().clear();
        listBox.setSpacing(10); // set spacing between the cards within a list

        for (Card card : server.getCardsForList(cardList)) {
            AnchorPane cardNode = loadCardNode(card, adminListTemplateCtrl);
            listBox.getChildren().add(cardNode); // add this card to the children of the VBox
        }

        // adding "new list" button

        return listNode;
    }

    /**
     * Loads the anchor pane of the card
     * @param card The selected card
     * @param adminListTemplateCtrl The list controller
     * @return The anchor pane of the card
     */
    public AnchorPane loadCardNode(Card card, AdminListTemplateCtrl adminListTemplateCtrl) {
        var adminCardTemplate =
                Main.load(AdminCardTemplateCtrl.class, "client", "scenes",
                        "AdminCardTemplate.fxml");

        AdminCardTemplateCtrl adminCardTemplateCtrl = adminCardTemplate.getKey();
        AnchorPane cardNode = (AnchorPane) adminCardTemplate.getValue();
        adminCardTemplateCtrl.start(card, adminListTemplateCtrl);

        // retrieve name of the Card from the Text Box
        Text cardText = (Text) cardNode.getChildren().get(0);
        cardText.setText(card.title); // set the name of the Card

//        cardNode = addTags(cardNode, card);
        return cardNode;
    }


    public void disconnectFromBoard() {
        mainCtrl.showAdminBoardOverview();
    }

//    /**
//     * Adds the tags of the card to the HBOX
//     * @param cardNode The card node
//     * @param card The actual card
//     * @return The anchor pane of the card
//     */
//    public AnchorPane addTags(AnchorPane cardNode, Card card){
//        HBox tagBox = (HBox) cardNode.getChildren().get(1);
//        for(Tag tag : card.tagList){
//            var tagTemplate =
//                    Main.load(TagTemplateCtrl.class,
//                            "client", "scenes", "TagTemplate.fxml");
//            AnchorPane tagNode = (AnchorPane) tagTemplate.getValue();
//            tagNode.setStyle("-fx-background-color: " + tag.color + ";" +
//                    "-fx-background-radius: 5;");
//            Text tagTitle = (Text) tagNode.getChildren().get(0);
//            tagTitle.setText(tag.title);
//            tagBox.setSpacing(5);
//            tagBox.getChildren().add(tagNode);
//        }
//
//        return cardNode;
//    }

    public void disconnectOnMouseEntered() {
        File disconnectFile = new
                File ("client/src/main/java/client/images/card-list-overview/disconnect2.png");
        Image disconnectImage = new Image (disconnectFile.toURI().toString());
        disconnectImageView.setImage(disconnectImage);
    }

    public void disconnectOnMouseExited() {
        resetDisconnectImageView();
    }



    public void showInviteKey(){
        String inviteKey = boardService.getBoardInviteKey();
        inviteKeyField.setText(inviteKey);
    }

}