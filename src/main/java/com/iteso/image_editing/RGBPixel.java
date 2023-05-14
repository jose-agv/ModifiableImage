package com.iteso.image_editing;

public class RGBPixel {
    private int red, green, blue;

    //Constructors:
    /**Creates a generic RGBPixel, with a gray color.*/
    public RGBPixel(){
        this(127,127,127);
    }
    /**Creates an RGBPixel based on the given colors.*/
    public RGBPixel(int red, int green, int blue) throws IllegalArgumentException {
        this.setRed(red);
        this.setGreen(green);
        this.setBlue(blue);
    }
    /**Creates an RGBPixel based on a single int with format 0xXXRRGGBB*/
    public RGBPixel(int rgb){
        this.setRed((rgb&0x00FF0000)>>16);
        this.setGreen((rgb&0x0000FF00)>>8);
        this.setBlue((rgb&0x000000FF));
    }

    //Setters:
    /**Sets this objects red attribute to the given value. If the value is outside the [0,255] range, throws an Illegal Argument Exception*/
    public void setRed(int red) throws IllegalArgumentException{
        if (red<0 || red>255) throw new IllegalArgumentException();
        this.red = red;
    }
    /**Sets this objects green attribute to the given value. If the value is outside the [0,255] range, throws an Illegal Argument Exception*/
    public void setGreen(int green) throws IllegalArgumentException{
        if (green<0 || green>255) throw new IllegalArgumentException();
        this.green = green;
    }
    /**Sets this objects blue attribute to the given value. If the value is outside the [0,255] range, throws an Illegal Argument Exception*/
    public void setBlue(int blue) throws IllegalArgumentException{
        if (blue<0 || blue>255) throw new IllegalArgumentException();
        this.blue = blue;
    }

    //Getters:
    /** Returns this object's red value.*/
    public int getRed() {
        return red;
    }
    /** Returns this object's green value.*/
    public int getGreen() {
        return green;
    }
    /** Returns this object's blue value.*/
    public int getBlue() {
        return blue;
    }

    //Class methods:
    public int toInteger(){
        return 0xFF000000+(this.red<<16)+(this.green<<8)+this.blue;
    }
}
