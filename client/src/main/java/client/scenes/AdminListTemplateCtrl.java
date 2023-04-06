package client.scenes;

import client.services.BoardService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.CardList;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminListTemplateCtrl implements Initializable {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final BoardService boardService;

    private CardList list;
    @FXML
    private TextField updateListNameField;
    @FXML
    private AnchorPane listAnchorPane;

    @Inject
    public AdminListTemplateCtrl(MainCtrl mainCtrl, ServerUtils server,
                            BoardService boardService) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardService = boardService;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {}


    public void start(CardList list){
        setList(list);
    }

    public void setList(CardList list) {
        this.list = list;
    }

    public CardList getList(){
        return list;
    }


}