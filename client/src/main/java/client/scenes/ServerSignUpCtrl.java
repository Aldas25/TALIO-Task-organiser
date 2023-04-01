package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerSignUpCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;


    private final CardListOverviewCtrl cardListOverviewCtrl;
    private final ListTemplateCtrl listTemplateCtrl;
    private final BoardOverviewCtrl boardOverviewCtrl;

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
    @FXML
    private Button registerButton;
    @FXML
    private Button closeButton;

    @Inject
    public ServerSignUpCtrl(ServerUtils server, MainCtrl mainCtrl,
                            CardListOverviewCtrl cardListOverviewCtrl,
                            ListTemplateCtrl listTemplateCtrl,
                            BoardOverviewCtrl boardOverviewCtrl) {

        this.server = server;
        this.mainCtrl = mainCtrl;

        this.cardListOverviewCtrl = cardListOverviewCtrl;
        this.listTemplateCtrl = listTemplateCtrl;
        this.boardOverviewCtrl = boardOverviewCtrl;
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

            server.setSession();
            cardListOverviewCtrl.start();
            boardOverviewCtrl.start();

            signUpLabelMessage.setText(null);
            mainCtrl.showBoardOverview();
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

    public void registerButtonOnMouseEnter (MouseEvent event) {
        registerButton.setStyle("-fx-background-color: #b0bfd4");
    }

    public void registerButtonOnMouseExit (MouseEvent event) {
        registerButton.setStyle("-fx-background-color: #d1dae6");
    }

    public void closeButtonOnMouseEnter (MouseEvent event) {
        closeButton.setStyle("-fx-background-color: #b0bfd4");
    }

    public void closeButtonOnMouseExit (MouseEvent event) {
        closeButton.setStyle("-fx-background-color: #d1dae6");
    }

}