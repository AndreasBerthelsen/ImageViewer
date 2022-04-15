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
    boolean deleteSlideshow = false;

    public Scheduler() {
        executor = Executors.newSingleThreadExecutor();
    }

    public void addSlideshow(Slideshow slideshow) {
        blockingQueue.add(slideshow);
    }

    public void removeActiveSlideshow() {
        if (activeSlideshow != null){
            activeSlideshow.Stop();
            deleteSlideshow = true;
        }
    }



    @Override
    public void run() {
        System.out.println("scheduler run");
        while (!blockingQueue.isEmpty()) {
            try {
                activeSlideshow = blockingQueue.take();
                Future<?> future = activeSlideshow.Start();
                future.get(); //while loop will stall here untill slideshow has hit its lifespan

                System.out.println("take");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (activeSlideshow != null && !deleteSlideshow) {
                addSlideshow(new Slideshow(activeSlideshow));
                activeSlideshow.Stop();
            }else deleteSlideshow = false;


        }
    }
}
