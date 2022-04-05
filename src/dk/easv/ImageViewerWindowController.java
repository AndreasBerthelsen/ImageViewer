package dk.easv;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
    private final List<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;
    @FXML
    private TextField txtInput;


    @FXML
    Parent root;

    @FXML
    private ImageView imageView;
    public Label imageFileLabel;

    private boolean isSliding = false;
    ScheduledExecutorService executor;
    Slideshow slideshow;

    public ImageViewerWindowController() {
        slideshow = new Slideshow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageFileLabel.textProperty().bind(slideshow.messageProperty());
    }

    @FXML
    private void handleBtnLoadAction() {
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
            displayImage();
        }
    }

    @FXML
    private void handleBtnPreviousAction() {
        if (!images.isEmpty()) {
            currentImageIndex =
                    (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction() throws Exception {
        nextImage();
        slideshow.call();
    }

    private void nextImage(){
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
            System.out.println("next img");
        }
    }
    private void displayImage() {
        if (!images.isEmpty()) {
            imageView.setImage(images.get(currentImageIndex));
        }
    }

    public void handleBtnStart(ActionEvent actionEvent) throws Exception {
        if (!isSliding) {
            executor = Executors.newScheduledThreadPool(1);

            int delay = Integer.parseInt(txtInput.getText());
            executor.scheduleAtFixedRate(slideshow,delay,delay,TimeUnit.SECONDS);

            isSliding = true;
            System.out.println("start");
        }
    }


    public void handleBtnStop(ActionEvent actionEvent) {
        if (isSliding) {
            executor.shutdown();
            System.out.println("stop");
            isSliding = false;
        }
    }


    public class Slideshow extends Task<String> {

        @Override
        protected String call() throws Exception {
            System.out.println("call");
            nextImage();
            File file = new File(images.get(currentImageIndex).getUrl());
            updateMessage(file.getName());
            return null;
        }
    }
}