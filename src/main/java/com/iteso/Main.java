package com.iteso;

import com.iteso.image_editing.Format;
import com.iteso.image_editing.ModifiableImage;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ModifiableImage img;
        try {
            img = new ModifiableImage("C:\\Users\\josea\\Desktop\\uni 6\\poo\\images\\paisaje.png");
        }catch(IOException ex){
            System.out.println("Image could not be opened");
            return;
        }
        img.display();
        ModifiableImage img2 = img.getDithering();
        img2.display();
        ModifiableImage img3 = img.clone();
        img3.toHalftone();
        img3.display();
        try{
            img2.save("C:\\Users\\josea\\Desktop\\uni 6\\poo\\images\\paisaje_dith.png", Format.PNG);
        }catch (IOException e){
            System.out.println("Image could not be saved.");
        }
    }

}
