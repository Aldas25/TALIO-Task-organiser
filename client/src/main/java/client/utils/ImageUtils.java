package client.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class ImageUtils {

    /**
     * Loads an image
     * @param imageView The ImageView
     * @param filename The filename to load
     */
    public void loadImage(ImageView imageView, String filename) {
        File file = new File(constructFilename(filename));
        Image image = new Image (file.toURI().toString());
        imageView.setImage(image);
    }

    /**
     * Constructs a filename from String
     * @param original The original file name
     * @return The filename
     */
    public String constructFilename(String original) {
        return "client/src/main/resources/client/images/" + original;
    }

}
