package com.iteso.image_editing;

import java.awt.image.BufferedImage;

public class Utils {
    public static final int IMG_WIDTH = 500, IMG_HEIGHT=250;
    public static ModifiableImage randomImage(){
        BufferedImage tmp = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        for (int i=0; i<IMG_HEIGHT; i++)
            for (int j=0; j<IMG_WIDTH; j++){
                RGBPixel rand = new RGBPixel((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256));
                tmp.setRGB(j, i, rand.toInteger());
            }
        return new ModifiableImage(tmp);
    }
}
