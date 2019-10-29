package Calc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

//code inspired from https://www.youtube.com/watch?v=cq80Itgs5Lw


//class for reading the given image of a floor plan and setting up a new image just in black and white
public class Readimage {

    public static void Reading() {

        File originalimage = new File("C:\\Users\\alexa\\Desktop\\BA\\floor plans\\7th\\7th floor Schapiro Center Columbia University final1.jpg");

        BufferedImage img = null;
        try {
            img = ImageIO.read(originalimage);

            BufferedImage blackwhiteImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

            //writing the wall coords in .txt-file
            PrintWriter writer = new PrintWriter("WallCoords.txt", "UTF-8");

            for(int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    Color c = new Color(img.getRGB(i,j));
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    int a = c.getAlpha();
                  //  System.out.println(r+"."+g+ "."+b+" ");
                    Color myWhite = new Color(255, 255, 255);
                    Color myBlack = new Color(0, 0, 0);

                    //if the pixel is black(wall), set as black in new image
                    if(c.getRed() < 135 && c.getGreen() < 135 && c.getBlue() < 135){
                        blackwhiteImage.setRGB(i,j,myBlack.getRGB());
                        writer.println(i+50);
                        writer.println(j+50);
                    }
                    else {
                        blackwhiteImage.setRGB(i,j, myWhite.getRGB());
                    }
                 }
            }

            writer.close();
        //saving the created image on computer
        //    ImageIO.write(blackwhiteImage, "png", new File((       //saving the picture
        //            "C:\\Users\\alexa\\Desktop\\BA\\floor plans\\7th\\coordpainting.png")));

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}

