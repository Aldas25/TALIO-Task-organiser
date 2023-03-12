package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddCardCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private CardList list;

    @FXML
    private TextField cardTitleTextField;

    @Inject
    public AddCardCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setList(CardList list) {
        this.list = list;
    }

    public void addCard() {
        Card card = new Card(list.id, cardTitleTextField.getText());
        server.addCard(card);
        mainCtrl.showListOverview();
    }

}
