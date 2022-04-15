package dk.easv;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Slideshow extends Task<Void> {
    private final List<Image> images;
    private final ImageView imageView;
    private int currentImageIndex = 0;
    private final int delay;
    private boolean exit = false;
    private final Label label;

    public Slideshow(List<Image> images, Label label, ImageView imageView, int delay) {
        this.images = images;
        this.imageView = imageView;
        this.delay = delay;
        this.label = label;
    }

    public Slideshow(Slideshow slideshow){
        this.images = slideshow.getImages();
        this.imageView = slideshow.getImageView();
        this.delay = slideshow.getDelay();
        this.label = slideshow.getLabel();
    }

    public Label getLabel() {
        return label;
    }

    public List<Image> getImages() {
        return images;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getDelay() {
        return delay;
    }

    private void nextImage() {
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        }
    }

    private void displayImage() {
        if (!images.isEmpty()) {
            imageView.setImage(images.get(currentImageIndex));
        }
    }

    public void Start(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(this);
    }

    public void Stop(){
        exit = true;
    }

    @Override
    protected Void call() throws Exception {
        Platform.runLater(() -> label.textProperty().bind(this.messageProperty()));
        while (!exit) {
            nextImage();
            String[] arr = images.get(currentImageIndex).getUrl().split("/");
            int length = arr.length;
            String imgURL = arr[length - 1];
            updateMessage(imgURL);
            Thread.sleep(delay);
        }
        return null;
    }


}
