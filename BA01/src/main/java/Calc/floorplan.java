package Calc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

//class for directly loading already created image
public class floorplan {

    public floorplan() throws IOException
    {
        Image img= ImageIO.read(new File("C:\\Users\\alexa\\Desktop\\BA\\floor plans\\7th\\coordpainting2.png"));
        img = img.getScaledInstance(1500, 900, Image.SCALE_DEFAULT);
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(1600,1200);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}