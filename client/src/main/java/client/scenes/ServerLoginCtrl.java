package client.scenes;

import client.services.JoinedBoardsService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerLoginCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final JoinedBoardsService joinedBoardsService;

    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView logoImageView;
    @FXML
    private ImageView lockImageView;

    @FXML
    private PasswordField adminTextField;
    @FXML
    private TextField serverURLTextField;
    @FXML
    private TextField filenameTextField;
    @FXML
    private Button loginButton;

    @FXML Button adminButton;

    /**
     * The constructor of this object
     * @param mainCtrl Reference to MainCtrl
     * @param server Reference to ServerUtils
     * @param joinedBoardsService Reference to JoinedBoardsService
     */
    @Inject
    public ServerLoginCtrl(ServerUtils server, MainCtrl mainCtrl,
                           JoinedBoardsService joinedBoardsService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.joinedBoardsService = joinedBoardsService;
    }

    /**
     * Make it so the two images (talio_logo, lock) are shown on the screen in the
     * corresponding Image View
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
        File logoFile = new File ("client/src/main/resources/client/images/log-in/talio_logo.png");
        Image logoImage = new Image (logoFile.toURI().toString());
        logoImageView.setImage(logoImage);

        File lockFile = new File ("client/src/main/resources/client/images/log-in/lock.png");
        Image lockImage = new Image (lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }

    public void openAdminScreen(){
        mainCtrl.showAdminBoardOverview();
    }

    /**
     * Allow access to the CardListOverview
     */
    public void connectToServer() {
        String serverURL = serverURLTextField.getText();
        server.setServer(serverURL);

        if (!server.isServerOk()) {
            loginMessageLabel.setText("Please type in a valid server address.");
            return;
        }

        loginMessageLabel.setText(null);
        String filename = filenameTextField.getText();

        if (filename.isBlank()) {
            loginMessageLabel.setText("Please type in a name for the file.");
            return;
        }

        joinedBoardsService.setFilename(filename);
        mainCtrl.startClient();
        mainCtrl.showBoardOverview();
    }


    /**
     * Check if all fields have been completed
     *      If yes, connect to the server
     *      If no, give a warning to the user
     *
     */
    public void loginButtonOnAction () {
        connectToServer();
    }

    /**
     * Button to enter admin screen
     */
    public void adminButtonOnAction(){
        String serverURL = serverURLTextField.getText();
        server.setServer(serverURL);

        if(adminTextField.getText().isBlank()){
            loginMessageLabel.setText("Admin password missing");
        }
        else if(!server.isServerOk()){
            loginMessageLabel.setText("Please type in a valid server address.");
        }
        else if(!server.checkAdminPassword(adminTextField.getText())){
            loginMessageLabel.setText("Wrong admin password");
        }
        else{
            loginMessageLabel.setText(null);
            mainCtrl.startAdmin();
            openAdminScreen();
        }
    }


    /**
     * Function called by event in JavaFX
     */
    public void loginButtonOnMouseEntered () {
        loginButton.setStyle("-fx-background-color: #b0bfd4");
    }

    /**
     * Function called by event in JavaFX
     */
    public void loginButtonOnMouseExited () {
        loginButton.setStyle("-fx-background-color: #d1dae6");
    }

    /**
     * Function called by event in JavaFX
     */
    public void adminButtonOnMouseEntered () {
        adminButton.setStyle("-fx-background-color: #b0bfd4");
    }

    /**
     * Function called by event in JavaFX
     */
    public void adminButtonOnMouseExited () {
        adminButton.setStyle("-fx-background-color: #d1dae6");
    }
}
