package Calc;

import com.sun.jndi.toolkit.url.Uri;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URISyntaxException;


//class for choosing the position (just z) in a room, from side view
public class roomIRTLabSetZ extends JFrame implements MouseListener {


        {
            addMouseListener(this); //for mouseevents

            //button for going to next website
            //***isn´t working yet***
            JButton Save = new JButton("Save");
            Save.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        new openWebpage(new Uri("http://www.google.de"));
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            Save.setBounds(960, 810, 100, 100);
            add(Save);
            Save.setVisible(true);

            JButton Back = new JButton("Back");
            Back.setBounds(520, 810, 100, 100);
            add(Back);
            Back.setVisible(true);

        }

        //setting up a new window with the room
        public roomIRTLabSetZ() {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            add(new JLabel(new ImageIcon("C:\\Users\\alexa\\Desktop\\BA\\floor plans\\7th\\IRTLab.png")));
            pack();
            setVisible(true);
            setSize(1600, 1200);


        }

     //   WholeBuildingDraw wholebuildingdrawinst = new WholeBuildingDraw();


    //drawing the line through the coords of the mouseclick to set up the height position
    //***so x coord is unchanged, just a line with enlarging the y coord***
    //Mouseclick handling and setting a blue point on position of mouseclick
        @Override
        public void mouseClicked(MouseEvent e){
            System.out.println("Mouse clicked");
            Graphics g = getGraphics();
            g.setColor(Color.BLUE);
            g.fillOval(e.getX()-7, e.getY()-7, 15, 15);
            System.out.println("x: " + e.getX() + " y: " + e.getY());
            if (e.getX() >= 0 && e.getY() >= 0) {
            //    g.drawLine(e.getX(),e.getY()-wholebuildingdrawinst.getHeight(),e.getX(),
                // e.getY()+wholebuildingdrawinst.getHeight());
                g.drawLine(e.getX(),e.getY()-200,e.getX(),e.getY()+200); //***just for presenting, hardcode***
            }
            removeMouseListener(this); //removing mouselistener to make sure, that just one click is possible
        }



        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

}
