package Calc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;
import javax.swing.JPanel;


//class for choosing the position (just x and y) in a room, view from above
public class roomIRTLabSetXY extends JFrame implements MouseListener {

    //value should be saved after mouseclick and go to roomIRTLabSetZ for drawing a line,
    //to make sure, just to choose a point on the line
    //***isn´t working yet, no access from roomIRTLabSetZ, just through WholeBuildingDraw,
    //but then it´s the null value and not the coords from mouseclick***
public int xclick;  //xcoord for drawing line in roomIRTLabSetZ
public int yclick;
    {
        addMouseListener(this); //for mouseevents

        JButton Save = new JButton("Save");
        Save.setBounds(960, 810, 100, 100);
        add(Save);
        Save.setVisible(true);

        JButton New = new JButton("New");
        New.setBounds(520, 810, 100, 100);
        add(New);
        New.setVisible(true);

    }

    //setting up a new window with the room
    public roomIRTLabSetXY() {
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    add(new JLabel(new ImageIcon("C:\\Users\\alexa\\Desktop\\BA\\floor plans\\7th\\IRTLab.png")));
    pack();
    setVisible(true);
    setSize(1600, 1200);


    }

    //Mouseclick handling and setting a blue point on position of mouseclick
    @Override
    public void mouseClicked(MouseEvent e){
                System.out.println("Mouse clicked");
                Graphics g = getGraphics();
                g.setColor(Color.BLUE);
                g.fillOval(e.getX(), e.getY(), 15, 15);
                System.out.println("x: " + e.getX() + " y: " + e.getY());
                xclick = e.getX();
                yclick = e.getY();
                if (e.getX() >= 0 && e.getY() >= 0) {
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