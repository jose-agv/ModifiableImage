package com.iteso.image_editing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
        try {
            Thread.sleep(50);
        }catch (InterruptedException e){}
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
    public void toGrayscale(){
        for (int i=0; i<this.getHeight(); i++)
            for (int j=0; j<this.getWidth(); j++){
                int gray_int = grayInt(this.getRGB(j,i));
                int gray_pixel = 0xFF000000+(gray_int<<16)+(gray_int<<8)+gray_int;
                this.setRGB(j, i, gray_pixel);
            }
    }

    /**Creates and returns a copy of this object after applying the grayscale transformation.
     * This object remains unchanged.*/
    public ModifiableImage getGrayscale(){
        ModifiableImage temp = this.clone();
        temp.toGrayscale();
        return temp;
    }

    /**Turns this object into its negative image.*/
    public void toNegative(){
        for (int i=0; i<this.getHeight(); i++)
            for (int j=0; j<this.getWidth(); j++){
                int neg_red   = 255-((this.getRGB(j,i)&16711680)>>16);
                int neg_green = 255-((this.getRGB(j,i)&65280)>>8);
                int neg_blue  = 255-((this.getRGB(j,i)&255));
                int neg_pixel = 0xFF000000+(neg_red<<16)+(neg_green<<8)+neg_blue;
                this.setRGB(j, i, neg_pixel);
            }
    }

    /**Creates and returns the negative image of this object.
     * This object remains unchanged.*/
    public ModifiableImage getNegative(){
        ModifiableImage temp = this.clone();
        temp.toNegative();
        return temp;
    }

    /**Filters this object with a mean low-pass filter.*/
    public void meanFilter(){
        ModifiableImage temp = this.clone();
        int redTotal, greenTotal, blueTotal;
        for (int i=1; i<this.getHeight()-1; i++)
            for (int j=1; j<this.getWidth()-1; j++){
                redTotal=0;
                greenTotal=0;
                blueTotal=0;
                for (int k=i-1; k<=i+1; k++)
                    for (int l=j-1; l<=j+1; l++){
                        redTotal += (temp.getRGB(l,k)&16711680)>>16;
                        greenTotal += (temp.getRGB(l,k)&65280)>>8;
                        blueTotal += (temp.getRGB(l,k)&255);
                    }
                redTotal /= 9;
                greenTotal /= 9;
                blueTotal /= 9;
                int pixel = 0xFF000000+(redTotal<<16)+(greenTotal<<8)+blueTotal;
                this.setRGB(j, i, pixel);
            }
    }

    /**Creates and returns the filtered form of this object, through a mean low-pass filter.
     * This object remains unchanged.*/
    public ModifiableImage getMeanFiltered(){
        ModifiableImage temp = this.clone();
        temp.meanFilter();
        return temp;
    }

    /**Filters this object with a median low-pass filter.*/
    public void medianFilter(){
        ModifiableImage temp = this.clone();
        int[] array = new int[9];
        int index;
        for (int i=1; i<this.getHeight()-1; i++)
            for (int j=1; j<this.getWidth()-1; j++){
                index = 0;
                for (int k=i-1; k<=i+1; k++)
                    for (int l=j-1; l<=j+1; l++){
                        array[index] = (temp.getRGB(l,k))&0x00FFFFFF;
                        index++;
                    }
                Arrays.sort(array);
                this.setRGB(j, i, array[4]);
            }
    }

    /**Creates and returns the filtered form of this object, through a median low-pass filter.
     * This object remains unchanged.*/
    public ModifiableImage getMedianFiltered(){
        ModifiableImage temp = this.clone();
        temp.meanFilter();
        return temp;
    }

    /**Turns this object into a black-and-white version of its own image.*/
    public void toBlackAndWhite(){
        for (int i=0; i<this.getHeight(); i++)
            for (int j=0; j<this.getWidth(); j++){
                int pixel = (grayInt(this.getRGB(j,i))<128)?0xFF000000:0xFFFFFFFF;
                this.setRGB(j,i,pixel);
            }
    }

    /**Creates and returns a copy of this object after turning it into a black-and-white image.
     * This object remains unchanged.*/
    public ModifiableImage getBlackAndWhite(){
        ModifiableImage temp = this.clone();
        temp.toBlackAndWhite();
        return temp;
    }

    /**Turns this object into a halftone version of its own image.*/
    public void toHalftone(){
        for (int i=0; i<this.getHeight()-1; i+=2)
            for (int j=0; j<this.getWidth()-1; j+=2){
                int a = grayInt(this.getRGB(j,i));
                int b = grayInt(this.getRGB(j+1,i));
                int c = grayInt(this.getRGB(j,i+1));
                int d = grayInt(this.getRGB(j+1,i+1));
                double avg = (a+b+c+d)/4.0;
                if (avg>255*0.875) this.mask100(j,i);
                else if (avg>255*0.625) this.mask75(j,i);
                else if (avg>255*0.375) this.mask50(j,i);
                else if (avg>255*0.125) this.mask25(j,i);
                else this.mask0(j,i);
            }
    }

    /**Creates and returns a copy of this object after turning it into a black-and-white image.
     * This object remains unchanged.*/
    public ModifiableImage getHalftoned(){
        ModifiableImage temp = this.clone();
        temp.toHalftone();
        return temp;
    }

    /**Turns this object into a dithering approximation of its own image.*/
    public void toDithering(){
        for (int i=0; i<this.getHeight()-1; i+=2)
            for (int j=0; j<this.getWidth()-1; j+=2){
                RGBPixel a = new RGBPixel(this.getRGB(j,i));
                RGBPixel b = new RGBPixel(this.getRGB(j+1,i));
                RGBPixel c = new RGBPixel(this.getRGB(j,i+1));
                RGBPixel d = new RGBPixel(this.getRGB(j+1,i+1));
                double red_avg = (a.getRed()+b.getRed()+c.getRed()+d.getRed())/4.0;
                double green_avg = (a.getGreen()+b.getGreen()+c.getGreen()+d.getGreen())/4.0;
                double blue_avg = (a.getBlue()+b.getBlue()+c.getBlue()+d.getBlue())/4.0;
                RGBPixel p11 = new RGBPixel(0,0,0);
                RGBPixel p12 = new RGBPixel(0,0,0);
                RGBPixel p21 = new RGBPixel(0,0,0);
                RGBPixel p22 = new RGBPixel(0,0,0);
                dSetRed(p11, p12, p21, p22, red_avg);
                dSetGreen(p11, p12, p21, p22, green_avg);
                dSetBlue(p11, p12, p21, p22, blue_avg);
                this.setRGB(j,i, p11.toInteger());
                this.setRGB(j+1,i, p12.toInteger());
                this.setRGB(j,i+1, p21.toInteger());
                this.setRGB(j+1,i+1, p22.toInteger());
            }
    }

    /**Creates and returns a copy of this object after approximating it via dithering.
     * This object remains unchanged.*/
    public ModifiableImage getDithering(){
        ModifiableImage temp = this.clone();
        temp.toDithering();
        return temp;
    }

    //This method is only used internally to get the grayscale value of an RGB pixel:
    private int grayInt(int pixel){
        int red   = (pixel&0xFF0000)>>16;
        int green = (pixel&0xFF00)>>8;
        int blue  = (pixel&0xFF);
        return (int)(red*0.3+green*0.59+blue*0.11);
    }

    //The next 4 methods are only used to set the individual pixels of an image to black or white during halftoning:
    private void mask100(int x, int y){
        this.setRGB(x,y,0xFFFFFFFF);
        this.setRGB(x+1,y,0xFFFFFFFF);
        this.setRGB(x,y+1,0xFFFFFFFF);
        this.setRGB(x+1,y+1,0xFFFFFFFF);
    }
    private void mask75(int x, int y){
        this.setRGB(x,y,0xFFFFFFFF);
        this.setRGB(x+1,y,0xFF000000);
        this.setRGB(x,y+1,0xFFFFFFFF);
        this.setRGB(x+1,y+1,0xFFFFFFFF);
    }
    private void mask50(int x, int y){
        this.setRGB(x,y,0xFFFFFFFF);
        this.setRGB(x+1,y,0xFF000000);
        this.setRGB(x,y+1,0xFF000000);
        this.setRGB(x+1,y+1,0xFFFFFFFF);
    }
    private void mask25(int x, int y){
        this.setRGB(x,y,0xFF000000);
        this.setRGB(x+1,y,0xFFFFFFFF);
        this.setRGB(x,y+1,0xFF000000);
        this.setRGB(x+1,y+1,0xFF000000);
    }
    private void mask0(int x, int y){
        this.setRGB(x,y,0xFF000000);
        this.setRGB(x+1,y,0xFF000000);
        this.setRGB(x,y+1,0xFF000000);
        this.setRGB(x+1,y+1,0xFF000000);
    }

    //The next 3 methods are only used internally to set the RGB values of the pixels during dithering:
    private void dSetRed(RGBPixel p11, RGBPixel p12, RGBPixel p21, RGBPixel p22, double red_avg){
        if (red_avg>0.375*255){
            p11.setRed(255);
            p22.setRed(255);
            if (red_avg>0.625*255) p21.setRed(255);
            if (red_avg>0.875*255) p12.setRed(255);
        }
        else if (red_avg>0.125*255) p12.setRed(255);
    }
    private void dSetGreen(RGBPixel p11, RGBPixel p12, RGBPixel p21, RGBPixel p22, double green_avg){
        if (green_avg>0.375*255){
            p11.setGreen(255);
            p22.setGreen(255);
            if (green_avg>0.625*255) p21.setGreen(255);
            if (green_avg>0.875*255) p12.setGreen(255);
        }
        else if (green_avg>0.125*255) p12.setGreen(255);
    }
    private void dSetBlue(RGBPixel p11, RGBPixel p12, RGBPixel p21, RGBPixel p22, double blue_avg){
        if (blue_avg>0.375*255){
            p11.setBlue(255);
            p22.setBlue(255);
            if (blue_avg>0.625*255) p21.setBlue(255);
            if (blue_avg>0.875*255) p12.setBlue(255);
        }
        else if (blue_avg>0.125*255) p12.setBlue(255);
    }

}
