package client.scenes;

import client.Main;
import client.services.BoardService;
import client.utils.ImageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.http.HttpStatus;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminCardListOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final BoardService boardService;
    private final ImageUtils imageUtils;

    @FXML
    private TextField inviteKeyField;
    @FXML
    private HBox listContainer;
    @FXML
    private ImageView disconnectImageView;

    /**
     * The constructor of this object
     * @param mainCtrl Reference to MainCtrl
     * @param server Reference to ServerUtils
     * @param imageUtils Reference to ImageUtils
     * @param boardService Reference to BoardService
     */
    @Inject
    public AdminCardListOverviewCtrl(ServerUtils server, MainCtrl mainCtrl,
                                BoardService boardService, ImageUtils imageUtils) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
        this.imageUtils = imageUtils;
    }

    /**
     *
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resourceBundle
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        resetDisconnectImageView();
    }

    public void resetDisconnectImageView () {
        imageUtils.loadImage(disconnectImageView, "card-list-overview/disconnect1.png");
    }


    /**
     * start() method is invoked when client connects to a valid server.
     */
    public void start(){
        server.registerForMessages("/topic/lists/update", HttpStatus.class, httpStatus -> {
            if (httpStatus.equals(HttpStatus.OK)){
                Platform.runLater(this::refresh);
            }
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
            AnchorPane cardNode = loadCardNode(card);
            listBox.getChildren().add(cardNode); // add this card to the children of the VBox
        }

        // adding "new list" button

        return listNode;
    }

    /**
     * Loads the anchor pane of the card
     * @param card The selected card
     * @return The anchor pane of the card
     */
    public AnchorPane loadCardNode(Card card) {
        var adminCardTemplate =
                Main.load(AdminCardTemplateCtrl.class, "client", "scenes",
                        "AdminCardTemplate.fxml");

        AnchorPane cardNode = (AnchorPane) adminCardTemplate.getValue();
        //adminCardTemplateCtrl.start(card, adminListTemplateCtrl);

        // retrieve name of the Card from the Text Box
        TextField cardText = (TextField) cardNode.getChildren().get(0);
        cardText.setText(card.title); // set the name of the Card

//        cardNode = addTags(cardNode, card);
        return cardNode;
    }


    /**
     * Function called by button in JavaFX
     */
    public void disconnectFromBoard() {
        mainCtrl.showAdminBoardOverview();
    }

    /**
     * Function called by event in JavaFX
     */
    public void disconnectOnMouseEntered() {
        imageUtils.loadImage(disconnectImageView, "card-list-overview/disconnect2.png");
    }

    /**
     * Function called by event in JavaFX
     */
    public void disconnectOnMouseExited() {
        resetDisconnectImageView();
    }

    /**
     * Function called by button in JavaFX
     */
    public void showInviteKey(){
        String inviteKey = boardService.getBoardInviteKey();
        inviteKeyField.setText(inviteKey);
    }

}