/*
import java.awt.*;
import java.awt.event.*;
public class sys_windows extends Frame implements MouseListener{
    sys_windows(){
        addMouseListener(this);
        gui pc =  new gui();
        pc.setSize(1600,1200);
        pc.setLayout(null);
        pc.setVisible(true);
    }
    public void mouseClicked(MouseEvent e) {
        Graphics g=getGraphics();
        g.setColor(Color.BLUE);
        g.fillOval(e.getX(),e.getY(),30,30);

    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

   // public static void main(String[] args) {
   //     new sys_windows();
   // }
}


*/












/*

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class sys_windows extends JPanel
        implements ActionListener, MouseMotionListener, MouseListener
{
    private JPanel titlebar, titlepane;
    public  JPanel content;
    private JLabel title;
    private int xPos, yPos, prevX, prevY = 0;
    private int xSize, ySize = 0;
    public JButton[] btn = new JButton[3];


    public Container container; public Component comp;

    public sys_windows()
    {
        titlebar = new JPanel();
        titlebar.setLayout(null);
        titlebar.setBackground(new Color(255,255,255, 10));
        titlebar.addMouseListener(this);
        titlebar.addMouseMotionListener(this);
        titlebar.setDoubleBuffered(true);
        titlebar.setOpaque(true);
        titlebar.setVisible(true);

        titlepane = new JPanel();
        titlepane.setLayout(null);
        titlepane.setBackground(new Color(255,255,255, 0));
        titlepane.setDoubleBuffered(true);
        titlepane.setOpaque(true);
        titlepane.setVisible(true);

        content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEtchedBorder());
        content.addMouseListener(this);
        content.addMouseMotionListener(this);
        content.setDoubleBuffered(true);
        content.setOpaque(true);
        content.setVisible(true);

        for (int i=0; i<btn.length; i++)
        {
            btn[i] = new JButton();
            btn[i].setDoubleBuffered(true);
            btn[i].addActionListener(this);
            btn[i].setOpaque(false);
            btn[i].setBackground(new Color(0,80,130, 40));
            btn[i].setForeground(Color.WHITE);
            btn[i].setFont(new Font("SansSerif", Font.BOLD, 12));
            btn[i].setBorderPainted(true);
            btn[i].setContentAreaFilled(true);
            btn[i].setVisible(true);
        }

        title = new JLabel("", JLabel.LEFT);
        title.setForeground(Color.BLACK);
        title.setDoubleBuffered(true);

        btn[0].setText("x");
        btn[1].setText("+");
        btn[2].setText("--");

        setLayout(null);
        setBackground(new Color(255,255,255, 50));
        setBorder(BorderFactory.createEtchedBorder());
        addMouseListener(this);
        addMouseMotionListener(this);
        setSize(200,200);
        setOpaque(true);
        setVisible(true);

        update();
    }

    public void addComponent(Container container,
                             Component c, int x, int y, int width, int height)
    {
        c.setBounds(x,y,width,height);
        container.add(c);
    }

    public void setContent(Container container)
    {
        this.container.add(container);
        repaint();
        update();
    }

    public void update()
    {
        addComponent(this, content,  5,30, getWidth()-10, getHeight()-35);
        addComponent(titlepane, title, 5, 1, getWidth(), 30);
        addComponent(titlebar, titlepane, 0, 0, getWidth()-120, 30);
        addComponent(this, titlebar, 0,0, getWidth(), 30);
        addComponent(titlebar, btn[0], titlebar.getWidth()-btn[0].getWidth(), 2,45,20);
        addComponent(titlebar, btn[1], titlebar.getWidth()-btn[1].getWidth()-btn[0].getWidth(),2,38,20);
        addComponent(titlebar, btn[2], titlebar.getWidth()-btn[2].getWidth()-btn[1].getWidth()-btn[0].getWidth(), 2, 38, 20);
        repaint();
    }

    public void setTitle(String title)
    {
        this.title.setText(title);
    }


    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btn[0])
        {
            setVisible(false);
            setEnabled(false);
            Runtime.getRuntime().gc();
        }

        if (e.getSource() == btn[1])
        {

        }

        if (e.getSource() == btn[2])
        {
            setVisible(true);
        }
    }

    public void mouseDragged(MouseEvent e)
    {
        if (e.getSource() == this)
        {
            xSize = e.getX();
            ySize = e.getY();
            if (xSize < 120)
            {
                xSize = 120;
            }

            if (ySize < 40)
            {
                ySize = 40;
            }
            setSize(xSize, ySize);
            repaint();
            update();
        }

        if (e.getSource() == titlebar)
        {
            xPos = this.getX() + e.getXOnScreen() - prevX;
            yPos = this.getY() + e.getYOnScreen() - prevY;

            setLocation(xPos, yPos);
            repaint();

            prevX = e.getXOnScreen();
            prevY = e.getYOnScreen();
        }
    }

    public void mousePressed(MouseEvent e)
    {
        prevX = e.getXOnScreen();
        prevY = e.getYOnScreen();
        repaint();
    }

    public void mouseClicked(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    public void mouseMoved(MouseEvent e)
    {

    }

    public void mouseEntered(MouseEvent e)
    {

    }

    public void mouseExited(MouseEvent e)
    {

    }
}


*/