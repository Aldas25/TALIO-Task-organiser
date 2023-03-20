package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpScreenCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ImageView keyboardImageView;
    @FXML
    private ImageView qImageView;
    @FXML
    private ImageView shiftKeyImageView;
    @FXML
    private ImageView upKeyImageView;
    @FXML
    private ImageView downKeyImageView;
    @FXML
    private ImageView eKeyImageView;
    @FXML
    private ImageView delKeyImageView;
    @FXML
    private ImageView backspaceKeyImageView;
    @FXML
    private ImageView enterKeyImageView;
    @FXML
    private ImageView escKeyImageView;
    @FXML
    private ImageView tKeyImageView;
    @FXML
    private ImageView cKeyImageView;
    @FXML
    private ImageView arrowKeysImageView;


    @Inject
    public HelpScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Insert all the images into the corresponding Image Views
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
        File kFile = new File ("client/src/main/java/client/images/help-screen/keyboard.png");
        Image keyboardImage = new Image (kFile.toURI().toString());
        keyboardImageView.setImage(keyboardImage);

        File qFile = new File ("client/src/main/java/client/images/help-screen/question_mark.png");
        Image qImage = new Image(qFile.toURI().toString());
        qImageView.setImage(qImage);

        File shiftFile = new File ("client/src/main/java/client/images/help-screen/shift_key.png");
        Image shiftImage = new Image (shiftFile.toURI().toString());
        shiftKeyImageView.setImage(shiftImage);

        File upFile = new File ("client/src/main/java/client/images/help-screen/up_key.png");
        Image upImage = new Image (upFile.toURI().toString());
        upKeyImageView.setImage(upImage);

        File downFile = new File ("client/src/main/java/client/images/help-screen/down_key.png");
        Image downImage = new Image (downFile.toURI().toString());
        downKeyImageView.setImage(downImage);

        File eFile = new File ("client/src/main/java/client/images/help-screen/e_key.png");
        Image eImage = new Image (eFile.toURI().toString());
        eKeyImageView.setImage(eImage);

        File delFile = new File ("client/src/main/java/client/images/help-screen/del_key.png");
        Image delImage = new Image (delFile.toURI().toString());
        delKeyImageView.setImage(delImage);

        File bFile = new File("client/src/main/java/client/images/help-screen/backspace_key.png");
        Image backspaceImage = new Image (bFile.toURI().toString());
        backspaceKeyImageView.setImage(backspaceImage);

        File enterFile = new File ("client/src/main/java/client/images/help-screen/enter_key.png");
        Image enterImage = new Image (enterFile.toURI().toString());
        enterKeyImageView.setImage(enterImage);

        finishInitialize();
    }

    public void finishInitialize() {
        File escFile = new File ("client/src/main/java/client/images/help-screen/esc_key.png");
        Image escImage = new Image (escFile.toURI().toString());
        escKeyImageView.setImage(escImage);

        File tFile = new File ("client/src/main/java/client/images/help-screen/t_key.png");
        Image tImage = new Image (tFile.toURI().toString());
        tKeyImageView.setImage(tImage);

        File cFile = new File ("client/src/main/java/client/images/help-screen/c_key.png");
        Image cImage = new Image (cFile.toURI().toString());
        cKeyImageView.setImage(cImage);

        File arrowFile = new File ("client/src/main/java/client/images/help-screen/arrow_keys.png");
        Image arrowImage = new Image (arrowFile.toURI().toString());
        arrowKeysImageView.setImage(arrowImage);
    }
}
