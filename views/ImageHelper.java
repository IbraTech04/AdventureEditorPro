package views;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class ImageHelper {
    /**
     * Load an image without retaining a handle on the file after loading.
     */
    public static Image load(Path path) throws IOException {
        try(InputStream fis = Files.newInputStream(path)) {
            return new Image(fis);
        }
        catch (NoSuchFileException e){
            return null;
        }
    }

    /**
     * Load an image without retaining a handle on the file after loading.
     */
    public static Image load(File file) throws IOException {
        return load(file.toPath());
    }
}
