package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.event.ActionEvent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class ServerLoginCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView logoImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField enterPasswordField;
    @FXML
    private TextField serverURLTextField;

    @Inject
    public ServerLoginCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
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
        File logoFile = new File ("client/src/main/java/client/images/log-in/talio_logo.png");
        Image logoImage = new Image (logoFile.toURI().toString());
        logoImageView.setImage(logoImage);

        File lockFile = new File ("client/src/main/java/client/images/log-in/lock.png");
        Image lockImage = new Image (lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }

    /**
     * Allow access to the CardListOverview
     */
    public void connectToServer() {
        String serverURL = serverURLTextField.getText();
        server.setServer(serverURL);
        if (server.isServerOk()) {
            mainCtrl.showListOverview();
        } else {
            loginMessageLabel.setText("Please type in a valid server address.");
        }
    }

    public void openAdminScreen(){
        mainCtrl.showBoardOverview();
    }

    /**
     * Check if all fields have been completed
     *      If yes, connect to the server
     *      If no, give a warning to the user
     *
     * @param event On button click
     */
    public void loginButtonOnAction (ActionEvent event) {
        if (!usernameTextField.getText().isBlank() && !enterPasswordField.getText().isBlank()) {
            if(usernameTextField.getText().equals("admin") &&
                    enterPasswordField.getText().equals("admin")){
                openAdminScreen();
            }
            else{
                connectToServer();
            }

        } else {
            loginMessageLabel.setText("Please enter a username and a password.");
        }
    }

    /**
     * Go to the Sign-Up Scene
     *
     * @param event On button click
     */
    public void signUpButtonOnAction (ActionEvent event) {
        mainCtrl.showServerSignUp();
    }


}
