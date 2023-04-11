package client.scenes;

import client.utils.ImageUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpScreenCtrl implements Initializable {

    private final ImageUtils imageUtils;

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

    /**
     * The constructor of this object
     * @param imageUtils Reference to ImageUtils
     */
    @Inject
    public HelpScreenCtrl(ImageUtils imageUtils) {
        this.imageUtils = imageUtils;
    }

    /**
     * Loads the images on initialization.
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
        loadImages();
    }

    /**
     * Insert all the images into the corresponding Image Views.
     */
    private void loadImages() {

        loadImage(keyboardImageView, "keyboard.png");
        loadImage(qImageView, "question_mark.png");
        loadImage(shiftKeyImageView, "shift_key.png");
        loadImage(upKeyImageView, "up_key.png");
        loadImage(downKeyImageView, "down_key.png");
        loadImage(eKeyImageView, "e_key.png");
        loadImage(delKeyImageView, "del_key.png");
        loadImage(backspaceKeyImageView, "backspace_key.png");
        loadImage(enterKeyImageView, "enter_key.png");
        loadImage(escKeyImageView, "esc_key.png");
        loadImage(tKeyImageView, "t_key.png");
        loadImage(cKeyImageView, "c_key.png");
        loadImage(arrowKeysImageView, "arrow_keys.png");
    }

    /**
     * Loads an image
     * @param imageView The ImageView
     * @param file The file to laod
     */
    private void loadImage(ImageView imageView, String file) {
        String prefix = "help-screen/";
        imageUtils.loadImage(imageView, prefix + file);
    }
}
