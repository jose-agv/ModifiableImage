package com.iteso.image_editing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.iteso.image_editing.Utils.*;

public class TestModifiableImage {
    @Test
    public void testGrayscale(){
        System.out.println("testGrayscale():");
        ModifiableImage img = randomImage();
        System.out.println("Random image generated.");
        ModifiableImage gray = img.getGrayscale();
        System.out.println("Grayscale version of random image generated.");
        for (int i=0; i<IMG_HEIGHT; i++)
            for (int j=0; j<IMG_WIDTH; j++){
                RGBPixel pixel_a = new RGBPixel(img.getRGB(j, i));
                RGBPixel pixel_b = new RGBPixel(gray.getRGB(j, i));
                assertEquals((int)(pixel_a.getRed()*0.3+pixel_a.getGreen()*0.59+pixel_a.getBlue()*0.11), pixel_b.getBlue());
            }
        System.out.println();
    }



}
