package com.iteso.image_editing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ModifiableImage extends BufferedImage {

    private String name = "Modifiable Image";

    //Constructors:
    /**Constructs a new ModifiableImage based on an existing image file in the specified location.
     * This constructor throws an IOException if the specified image file is not found or can't be opened.*/
    public ModifiableImage(String imagePath) throws IOException{
        this(ImageIO.read(new File(imagePath.trim())));
        this.name = this.findName(imagePath);
    }
    /**Constructs a ModifiableImage based on an already opened BufferedImage object.*/
    public ModifiableImage(BufferedImage image){
        super(image.getColorModel(), image.copyData(null), image.isAlphaPremultiplied(), null);
    }

    //The next method is only used internally to find the name of the opened image file:
    private String findName(String path){
        int slashIndex = path.length();
        do slashIndex--;
        while(path.charAt(slashIndex)!='\\' && path.charAt(slashIndex)!='/' && slashIndex>0);
        return path.substring(slashIndex+1);
    }

    //Getters:
    /**Returns the name of the image, which is automatically determined if the path name constructor was used.*/
    public String getName() {
        return this.name;
    }

    //Setters:
    /**Sets this object's name to any non-null String.*/
    public void setName(String name) throws IllegalArgumentException{
        if (name==null) throw new IllegalArgumentException();
        this.name = name;
    }

    //Class methods:
    /**Opens a JFrame window displaying the ModifiableImage.*/
    public void display(){
        JFrame frame = new JFrame(this.name);
        frame.setSize(this.getWidth(), this.getHeight());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        JLabel imgLabel = new JLabel(new ImageIcon(this));
        Container c = frame.getContentPane();
        c.add(imgLabel);
        frame.setVisible(true);
    }

    /**Saves this object in its current state as a file in the specified path location with the specified image format.*/
    public void save(String path, Format format) throws IOException{
        ImageIO.write(this, format.toString(), new File(path));
    }

    /**Creates and returns a ModifiableImage object with the same characteristics as this object.*/
    public ModifiableImage clone(){
        BufferedImage temp1 = new BufferedImage(this.getColorModel(), this.copyData(null), this.isAlphaPremultiplied(), null);
        ModifiableImage temp2 = new ModifiableImage(temp1);
        temp2.name = this.name;
        return temp2;
    }

    /**Applies the grayscale transformation to this object, which means it will only appear in shades of gray.*/
    public void toGrayScale(){
        for (int i=0; i<this.getHeight(); i++)
            for (int j=0; j<this.getWidth(); j++){
                int red   = (this.getRGB(j,i)&16711680)>>16;
                int green = (this.getRGB(j,i)&65280)>>8;
                int blue  = (this.getRGB(j,i)&255);
                int gray_int = (int)(red*0.3+green*0.59+blue*0.11);
                int gray_pixel = 0xFF000000+(gray_int<<16)+(gray_int<<8)+gray_int;
                this.setRGB(j, i, gray_pixel);
            }
    }

    /**Creates and returns a copy of this object after applying the grayscale transformation.
     * This object remains unchanged.*/
    public ModifiableImage getGrayScale(){
        ModifiableImage temp = this.clone();
        temp.toGrayScale();
        return this;
    }

}
