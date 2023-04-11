package client.scenes;

import client.services.JoinedBoardsService;
import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class BoardJoinCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
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


    /**
     * The constructor of this object
     * @param mainCtrl Reference to MainCtrl
     * @param server Reference to ServerUtils
     * @param joinedBoardsService Reference to JoinedBoardsService
     */
    @Inject
    public BoardJoinCtrl(MainCtrl mainCtrl, ServerUtils server,
                         JoinedBoardsService joinedBoardsService) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.joinedBoardsService = joinedBoardsService;
    }

    /**
     * Tries to join a board
     */
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

        joinedBoardsService.joinBoardAndSave(boardToJoin);
        closeAndClear();
        mainCtrl.showBoardOverview();
    }

    /**
     * Function called by button in JavaFX
     */
    public void cancelButtonOnAction () {
        closeAndClear();
    }

    /**
     * Function called by button in JavaFX
     */
    public void joinButtonOnAction(){
        setEnteredKey();
        tryJoin();
    }

    /**
     * Function called by button in JavaFX
     */
    public void setEnteredKey(){
        this.enteredKey = enterInviteKeyField.getText();
    }

    /**
     * Function called by event in JavaFX
     */
    public void cancelButtonOnMouseEntered () {
        cancelButton.setStyle("-fx-background-color: #B05656");
    }

    /**
     * Function called by event in JavaFX
     */
    public void cancelButtonOnMouseExited () {
        cancelButton.setStyle("-fx-background-color: #DD6C6C");
    }

    /**
     * Function called by event in JavaFX
     */
    public void joinButtonOnMouseEntered () {
        joinButton.setStyle("-fx-background-color: #90A07B");
    }

    /**
     * Function called by event in JavaFX
     */
    public void joinButtonOnMouseExited () {
        joinButton.setStyle("-fx-background-color: #B5C99A");
    }

    /**
     * Closes a board and clears text
     */
    public void closeAndClear(){
        mainCtrl.closeJoinBoard();
        joinError.setText("");
        enterInviteKeyField.clear();
    }
}
