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

    @FXML
    private TextField txtInput;

    @FXML
    Parent root;

    @FXML
    private ImageView imageView;
    public Label imageFileLabel;

    private boolean isSliding = false;
    ScheduledExecutorService executor;
    Slideshow activeSlideshow;
    Scheduler scheduler;

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
            {
                images.add(new Image(f.toURI().toString()));
            });
        }
        System.out.println(images);
        return images;
    }


    public void handleBtnStart(ActionEvent actionEvent) throws Exception {
        int delay = Integer.parseInt(txtInput.getText());
        Slideshow slideshow = new Slideshow(loadImages(),imageFileLabel,imageView,delay);
        imageFileLabel.textProperty().bind(slideshow.messageProperty());
        scheduler.addSlideshow(slideshow);
        scheduler.run();

    }

    public void handleBtnStop(ActionEvent actionEvent) {
        if (isSliding) {
            executor.shutdown();
            System.out.println("stop");
            isSliding = false;
        }
    }


}