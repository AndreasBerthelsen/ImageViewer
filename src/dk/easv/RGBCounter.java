package dk.easv;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;


public class RGBCounter extends Task<Void> {
    Image image;
    Label redLabel;
    Label greenLabel;
    Label blueLabel;

    public RGBCounter(Image image, Label redLabel, Label greenLabel, Label blueLabel) {
        this.image = image;
        this.redLabel = redLabel;
        this.greenLabel = greenLabel;
        this.blueLabel = blueLabel;
    }


    /**
     * Adds numbers of two arrays. if arrays are of Different length than only
     * addition only occur for as many elements in the small array.
     *
     * @param first
     * @param second
     * @return
     */
    private int[] add(int[] first, int[] second) {
        int length = Math.min(first.length, second.length);
        int[] result = new int[length];

        for (int i = 0; i < length; i++) {
            result[i] = first[i] + second[i];
        }

        return result;
    }

    private int[] findPixelValue(HashMap<String,Double> colorMap){
        int redCount = 0;
        int greenCount = 0;
        int blueCount = 0;

        double highestValue = 0;

        //find highest value color value for pixel
        for (String key : colorMap.keySet()){
            if (colorMap.get(key) > highestValue){
                highestValue = colorMap.get(key);
            }
        }

        for(String key : colorMap.keySet()){
            if (colorMap.get(key) == highestValue){
                switch (key){
                    case "red" -> redCount++;
                    case "blue" -> blueCount++;
                    case "green" -> greenCount++;
                }
            }
        }
        return new int[]{redCount,greenCount,blueCount};
    }

    @Override
    protected Void call() {
        PixelReader pixelReader = image.getPixelReader();
        int[] imageColorCount = {0,0,0};
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = pixelReader.getColor(x, y);
                double blue = color.getBlue();
                double red = color.getRed();
                double green = color.getGreen();

                HashMap<String,Double> colorMap = new HashMap<>();
                colorMap.put("red",red);
                colorMap.put("green",green);
                colorMap.put("blue",blue);

                int[] pixelColor = findPixelValue(colorMap);
                imageColorCount = add(imageColorCount,pixelColor);
            }
        }
        String redResult = String.valueOf(imageColorCount[0]);
        String greenResult = String.valueOf(imageColorCount[1]);
        String blueResult = String.valueOf(imageColorCount[2]);
        Platform.runLater(() -> redLabel.setText(redResult));
        Platform.runLater(() -> greenLabel.setText(greenResult));
        Platform.runLater(() -> blueLabel.setText(blueResult));
        return null;
    }
}
