package dk.easv;

import javafx.scene.image.Image;

import java.util.concurrent.*;

public class Scheduler implements Runnable {

    boolean exit = false;
    int lifeSpan = 20000;

    BlockingQueue<Slideshow> blockingQueue = new LinkedBlockingQueue<>();


    public Scheduler() {

    }
    public void addSlideshow(Slideshow slideshow){
        blockingQueue.add(slideshow);
    }

    @Override
    public void run() {
        while (!blockingQueue.isEmpty()) {
            try {
                blockingQueue.take().call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
