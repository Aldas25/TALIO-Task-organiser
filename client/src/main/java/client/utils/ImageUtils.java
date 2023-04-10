package client.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class ImageUtils {

    public void loadImage(ImageView imageView, String filename) {
        File file = new File(constructFilename(filename));
        Image image = new Image (file.toURI().toString());
        imageView.setImage(image);
    }

    public String constructFilename(String original) {
        return "client/src/main/resources/client/images/" + original;
    }

}
