package dk.easv;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Slideshow extends Task<Void> {
    private final List<Image> images;
    private final ImageView imageView;
    private final int delay;
    private final Label label;
    private final int lifeSpanInMills;
    private int currentImageIndex = 0;
    private boolean exit = false;
    private ExecutorService executorService;
    private Label blueLabel;
    private Label redLabel;
    private Label greenLabel;

    public Slideshow(List<Image> images, Label label, ImageView imageView, int delay, int lifeSpanInMills, Label blueLabel, Label redLabel, Label greenLabel) {
        this.images = images;
        this.imageView = imageView;
        this.delay = delay;
        this.label = label;
        this.lifeSpanInMills = lifeSpanInMills;
        this.blueLabel = blueLabel;
        this.redLabel = redLabel;
        this.greenLabel = greenLabel;
    }

    public Slideshow(Slideshow slideshow) {
        this.images = slideshow.getImages();
        this.imageView = slideshow.getImageView();
        this.delay = slideshow.getDelay();
        this.label = slideshow.getLabel();
        this.lifeSpanInMills = slideshow.getLifeSpanInMills();
        this.blueLabel = slideshow.getBlueLabel();
        this.redLabel = slideshow.getRedLabel();
        this.greenLabel = slideshow.getGreenLabel();
    }

    public Label getBlueLabel() {
        return blueLabel;
    }

    public Label getRedLabel() {
        return redLabel;
    }

    public Label getGreenLabel() {
        return greenLabel;
    }

    public Label getLabel() {
        return label;
    }

    public int getLifeSpanInMills() {
        return lifeSpanInMills;
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

    public Future<?> Start() {
        executorService = Executors.newCachedThreadPool();

        return executorService.submit(this);
    }

    public void Stop() {
        exit = true;
    }

    private boolean checkLifeSpan(long startTime, int lifeSpanInMills) {
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis - startTime <= lifeSpanInMills;
    }

    private void setColorLabels() {
        RGBCounter rgbCounter = new RGBCounter(images.get(currentImageIndex), redLabel, greenLabel, blueLabel);
        executorService.submit(rgbCounter);
    }

    @Override
    protected Void call() throws Exception {
        Platform.runLater(() -> label.textProperty().bind(messageProperty()));
        long startTime = System.currentTimeMillis();
        while (!exit && checkLifeSpan(startTime, lifeSpanInMills)) {
            nextImage();
            setColorLabels();
            String[] arr = images.get(currentImageIndex).getUrl().split("/");
            int length = arr.length;
            String imgURL = arr[length - 1];
            updateMessage(imgURL);
            Thread.sleep(delay);
        }
        return null;
    }
}
