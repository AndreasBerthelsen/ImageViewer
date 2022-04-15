package dk.easv;

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
        while (!blockingQueue.isEmpty()) {
            try {
                activeSlideshow = blockingQueue.take();
                Future<?> future = activeSlideshow.Start();
                future.get(); //while loop will stall here untill slideshow has hit its lifespan
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
