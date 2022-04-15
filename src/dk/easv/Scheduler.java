package dk.easv;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.concurrent.*;

public class Scheduler implements Runnable {



    ExecutorService executor;
    BlockingQueue<Slideshow> blockingQueue = new LinkedBlockingQueue<>();
    Slideshow activeSlideshow;
    boolean removeActiveSlideshow = false;

    public Scheduler() {

        executor = Executors.newSingleThreadExecutor();

    }

    public void addSlideshow(Slideshow slideshow) {
        blockingQueue.add(slideshow);
    }

    @Override
    public void run() {
        System.out.println("scheduler run");
        if (activeSlideshow != null) {
            addSlideshow(new Slideshow(activeSlideshow));
            activeSlideshow.Stop();
        }

        try {
            activeSlideshow = blockingQueue.take();
            activeSlideshow.Start();
            System.out.println("take");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
