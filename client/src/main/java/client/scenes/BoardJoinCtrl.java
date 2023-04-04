package client.scenes;

import client.services.BoardService;
import client.services.JoinedBoardsService;
import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class BoardJoinCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final BoardService boardService;
    private final JoinedBoardsService joinedBoardsService;

    private String enteredKey;

    @FXML
    private Text joinError;
    @FXML
    private TextField enterInviteKeyField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button joinButton;


    @Inject
    public BoardJoinCtrl(MainCtrl mainCtrl, ServerUtils server,
                         BoardService boardService,
                         JoinedBoardsService joinedBoardsService) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardService = boardService;
        this.joinedBoardsService = joinedBoardsService;
    }

    public void tryJoin(){
        if(this.enteredKey.length() != 4){
            joinError.setText("Invalid Invite Key");
            return;
        }

        Board boardToJoin = server.getBoardByInviteKey(enteredKey);

        if(boardToJoin == null){
            joinError.setText("Board Not Found");
            return;
        }

//        boardService.setCurrentBoard(boardToJoin);
//        mainCtrl.showListOverview();
        joinedBoardsService.joinBoardAndSave(boardToJoin);
        closeAndClear();
        mainCtrl.showBoardOverview();
    }

    public void cancelButtonOnAction () {
        closeAndClear();
    }

    public void joinButtonOnAction(){
        setEnteredKey();
        tryJoin();
    }

    public void setEnteredKey(){
        this.enteredKey = enterInviteKeyField.getText();
    }

    public void cancelButtonOnMouseEntered (MouseEvent event) {
        cancelButton.setStyle("-fx-background-color: #B05656");
    }

    public void cancelButtonOnMouseExited (MouseEvent event) {
        cancelButton.setStyle("-fx-background-color: #DD6C6C");
    }

    public void joinButtonOnMouseEntered (MouseEvent event) {
        joinButton.setStyle("-fx-background-color: #90A07B");
    }

    public void joinButtonOnMouseExited (MouseEvent event) {
        joinButton.setStyle("-fx-background-color: #B5C99A");
    }

    public void closeAndClear(){
        mainCtrl.closeJoinBoard();
        joinError.setText("");
        enterInviteKeyField.clear();
    }



}
