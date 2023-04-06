package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


import javafx.scene.layout.AnchorPane;


import java.net.URL;
import java.util.ResourceBundle;


public class AdminCardTemplateCtrl implements Initializable {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private Card card;
    private AdminListTemplateCtrl currentListCtrl;
    private Board board;

    @FXML
    private AnchorPane cardAnchorPane;

    @Inject
    public AdminCardTemplateCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {}

    public void start(Card card, AdminListTemplateCtrl currentListCtrl){
        setCard(card);
        setCurrentListCtrl(currentListCtrl);
    }


    public void setCard(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCurrentListCtrl(AdminListTemplateCtrl listCtrl) {
        this.currentListCtrl = listCtrl;
    }

    public AdminListTemplateCtrl getCurrentListCtrl() {
        return currentListCtrl;
    }

    public AnchorPane getCardAnchorPane() {
        return cardAnchorPane;
    }

    public void setBoard (Board board) {
        this.board = board;
    }

}