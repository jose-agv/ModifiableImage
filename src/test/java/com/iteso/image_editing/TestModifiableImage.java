package com.iteso.image_editing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TestModifiableImage {
    public static void main(String[] args) {
        ModifiableImage img;
        try {
            img = new ModifiableImage("C:\\Users\\josea\\Desktop\\uni 6\\poo\\images\\honkai.jpeg");
        }catch(IOException ex){
            System.out.println("Image could not be opened");
            return;
        }
        //img.display();
    }
}
