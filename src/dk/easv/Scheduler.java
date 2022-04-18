package dk.easv;

import java.util.concurrent.*;

public class Scheduler implements Runnable {

    BlockingQueue<Slideshow> blockingQueue = new LinkedBlockingQueue<>();
    Slideshow activeSlideshow;
    boolean deleteSlideshow = false;
    boolean exit = false;


    public void addSlideshow(Slideshow slideshow) {
        blockingQueue.add(slideshow);
    }

    public void removeActiveSlideshow() {
        if (activeSlideshow != null) {
            activeSlideshow.Stop();
            deleteSlideshow = true;
        }
    }

    @Override
    public void run() {
        while (!exit) {
            try {
                activeSlideshow = blockingQueue.take();
                Future<?> future = activeSlideshow.Start();
                future.get(); //while loop will stall here until slideshow has hit its lifespan
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (activeSlideshow != null && !deleteSlideshow) {
                addSlideshow(new Slideshow(activeSlideshow));
                activeSlideshow.Stop();
            } else deleteSlideshow = false;
        }
    }
}
