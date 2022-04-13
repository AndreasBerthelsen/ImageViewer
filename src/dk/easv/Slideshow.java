package dk.easv;

import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.util.List;


public class Slideshow extends Task<String> {
    List<Image> images;
    Label label;
    ImageView imageView;
    int currentImageIndex = 0;
    int delay;
    int count;
    boolean exit = false;

    public Slideshow(List<Image> images, Label label, ImageView imageView,int delay) {
        this.images = images;
        this.label = label;
        this.imageView = imageView;
        this.delay = delay;
        count = delay;
    }

    private void nextImage() {
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
            System.out.println("next img");
        }
    }

    private void displayImage() {
        if (!images.isEmpty()) {
            System.out.println("display img");
            imageView.setImage(images.get(currentImageIndex));
        }
    }

    public void stop(){
        exit = true;
    }

    @Override
    protected String call() throws Exception {
        while(!exit){
            if (count < 0){
                nextImage();
                String[] arr = images.get(currentImageIndex).getUrl().split("/");
                int length = arr.length;
                String imgURL = arr[length - 1];
                updateMessage(imgURL);
                count = delay;
            }
            else{
                count--;
            }
        }
        return null;
    }
}
