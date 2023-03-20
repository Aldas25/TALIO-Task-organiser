package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerSignUpCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField serverURLTextField;
    @FXML
    private Label signUpLabelMessage;
    @FXML
    private ImageView userIconImageView;

    @Inject
    public ServerSignUpCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Make it so the user icon image is shown in the corresponding Image View
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
        File userIconFile = new File ("client/src/main/java/client/images/sign-up/user_icon.png");
        Image userIcon = new Image (userIconFile.toURI().toString());
        userIconImageView.setImage(userIcon);
    }

    /**
     * If all the fields are completed, allow access to the CardListOverview
     * Else, give a warning
     *
     * @param event When the button is clicked
     */
    public void registerButtonOnAction (ActionEvent event) {
        String serverURL = serverURLTextField.getText();
        server.setServer(serverURL);
        if (!firstnameTextField.getText().isBlank() &&
            !lastnameTextField.getText().isBlank() &&
            !setPasswordField.getText().isBlank() &&
            !confirmPasswordField.getText().isBlank()) {

            mainCtrl.showListOverview();
        } else {
            signUpLabelMessage.setText("All fields must be completed.");
        }
    }

    /**
     * Go back to the Log In Screen
     *
     * @param event When the button is clicked
     */
    public void closeButtonOnAction (ActionEvent event) {
        mainCtrl.showServerLogin();
    }

}