package dk.easv;

import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Slideshow extends Task<Image> {

    private List<Image> images;
    private int currentImageIndex = 0;

    public Slideshow(List<Image> images) {
        this.images = images;
    }

    private void nextImage(){
        if (!images.isEmpty()){
            currentImageIndex = (currentImageIndex + 1) % images.size();
        }
    }

    @Override
    protected Image call() throws Exception {
        nextImage();
        return images.get(currentImageIndex);

    }
}
