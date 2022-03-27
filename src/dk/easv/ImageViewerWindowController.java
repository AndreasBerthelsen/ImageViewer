package dk.easv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ImageViewerWindowController {
    private final List<Image> images = new ArrayList<>();
    @FXML
    private Button startStopBtn;
    @FXML
    private TextField txtInput;
    private int currentImageIndex = 0;

    @FXML
    Parent root;

    @FXML
    private ImageView imageView;

    private boolean isSliding = false;

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
    private void handleBtnNextAction() {
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        }
    }

    private void displayImage() {
        if (!images.isEmpty()) {
            imageView.setImage(images.get(currentImageIndex));
        }
    }

    public void handleBtnStart(ActionEvent actionEvent)  {

        if (!isSliding){
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            Runnable slideshow = this::handleBtnNextAction;
            int delay = Integer.parseInt(txtInput.getText());
            Future<?> future = executor.scheduleAtFixedRate(slideshow, delay, delay, TimeUnit.SECONDS);
            future.cancel(true);
            isSliding = true;
            startStopBtn.setText("Stop");

        }
        if (!executor.isShutdown() && isSliding) {
            executor.shutdown();
            isSliding = false;
            startStopBtn.setText("Start");
        }



    }


}