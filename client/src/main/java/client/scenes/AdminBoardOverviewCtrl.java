package client.scenes;

import client.Main;
import client.utils.ImageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminBoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ImageUtils imageUtils;

    @FXML
    private ImageView disconnectImageView;

    @FXML
    private VBox boardContainer;

    /**
     * The constructor of this object
     * @param mainCtrl Reference to MainCtrl
     * @param server Reference to ServerUtils
     * @param imageUtils Reference to ImageUtils
     */
    @Inject
    public AdminBoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl,
                                  ImageUtils imageUtils) {
        this.server = server;
        this.mainCtrl = mainCtrl;
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

    /**
     * start() method is invoked when client connects to a valid server.
     */
    public void start(){
        server.registerForUpdates(board -> Platform.runLater(this::refresh));
    }

    /**
     * Resets and disconnects ImageView
     */
    public void resetDisconnectImageView () {
        imageUtils.loadImage(disconnectImageView, "admin-board-overview/disconnect1.png");
    }

    /**
     * Function called by event in JavaFX
     */
    public void disconnectFromServer(){
        mainCtrl.disconnectFromServer();
    }

    /**
     * Function called by event in JavaFX
     */
    public void disconnectOnMouseEntered () {
        imageUtils.loadImage(disconnectImageView, "admin-board-overview/disconnect2.png");
    }

    /**
     * Function called by event in JavaFX
     */
    public void disconnectOnMouseExited () {
        resetDisconnectImageView();
    }

    /**
     * Refreshes the BoardOverview
     */
    public void refresh() {
        boardContainer.getChildren().clear();

        List<Board> allBoards = server.getBoards();
        for(Board board: allBoards) {
            var adminBoardTemplate = Main.load (
                    AdminBoardTemplateCtrl.class, "client", "scenes",
                    "AdminBoardTemplate.fxml"
            );

            AdminBoardTemplateCtrl adminBoardTemplateCtrl = adminBoardTemplate.getKey();
            AnchorPane boardNode = (AnchorPane) adminBoardTemplate.getValue();
            adminBoardTemplateCtrl.setBoard(board);

            // retrieving text from a copy of the file BoardTemplate
            TextField textField = (TextField) boardNode.getChildren().get(0);
            textField.setText(board.title);                  // setting title to new node

            // add board to the board container
            boardContainer.getChildren().add(boardNode);
        }
    }

    /**
     * Stops the server
     */
    public void stop(){
        server.stop();
    }

}
