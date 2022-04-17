package dk.easv;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageViewerWindowController implements Initializable {

    public Label imageFileLabel;
    public Label redLabel;
    public Label greenLabel;
    public Label blueLabel;
    @FXML
    Parent root;
    ExecutorService executor;
    Scheduler scheduler;
    int slideshowLifespan = 10000;
    @FXML
    private TextField txtInput;
    @FXML
    private ImageView imageView;


    public ImageViewerWindowController() {
        scheduler = new Scheduler();
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
                    images.add(new Image(f.toURI().toString())));
        }
        System.out.println(images);
        return images;
    }


    public void handleBtnStart(ActionEvent actionEvent) throws Exception {
        int delay = Integer.parseInt(txtInput.getText()) * 1000; //*1000 for mills
        List<Image> images = loadImages();

        if (delay > 0 && !images.isEmpty()) {

            Slideshow slideshow = new Slideshow(images, imageFileLabel, imageView, delay, slideshowLifespan, blueLabel, redLabel, greenLabel);
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