package dk.easv;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ImageViewerWindowController implements Initializable {

    @FXML
    private TextField txtInput;

    @FXML
    Parent root;

    @FXML
    private ImageView imageView;
    public Label imageFileLabel;

    ExecutorService executor;
    Scheduler scheduler;
    int slideshowLifespan = 10000;

    public ImageViewerWindowController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private List<Image> loadImages() {
        List<Image> images = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (!files.isEmpty()) {
            files.forEach((File f) ->
            {
                images.add(new Image(f.toURI().toString()));
            });
        }
        System.out.println(images);
        return images;
    }


    public void handleBtnStart(ActionEvent actionEvent) throws Exception {
        int delay = Integer.parseInt(txtInput.getText()) * 1000; //*1000 for mills
        List<Image> images = loadImages();

        if (delay > 0 && !images.isEmpty()) {
            if (scheduler == null) {
                scheduler = new Scheduler();
            }

            Slideshow slideshow = new Slideshow(images, imageFileLabel, imageView, delay, slideshowLifespan);
            scheduler.addSlideshow(slideshow);

            if (executor == null) {
                executor = Executors.newSingleThreadExecutor();
                executor.submit(scheduler);
            }
        }
    }

    public void handleBtnStop(ActionEvent actionEvent) {
        //removes the current slideshow from the cycle
        if (scheduler != null) {
            scheduler.removeActiveSlideshow();
        }


    }


}