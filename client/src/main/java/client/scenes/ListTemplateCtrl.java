package client.scenes;

import com.google.inject.Inject;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ListTemplateCtrl {

    private final MainCtrl mainCtrl;
    private CardList list;

    @FXML
    private Button addCardButton;

    @Inject
    public ListTemplateCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void setList(CardList list) {
        this.list = list;
    }

    public void addCard() {
        mainCtrl.showAddCard(list);
    }

    public Button getAddCardButton() {
        return addCardButton;
    }

}
