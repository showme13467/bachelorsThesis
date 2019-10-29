package Calc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;

//class for directly loading already created image
public class floorplan {

    //***setting this into main cause of drag and drop function***
   /* public floorplan() throws IOException {
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); */

//code according to https://www.codota.com/code/java/classes/java.awt.event.MouseEvent

    //***not working yet, the "*" symbol is created on another layer under the floorplan***
    //***should be above the floorplan to move IoT devices

    public static class MouseDragTest extends JPanel {

            private static final String TITLE = "*";
            private static final int W = 1640;
            private static final int H = 80;
            private Point textPt = new Point(W / 2, H / 2);
            private Point mousePt;


            public MouseDragTest() {
                this.setFont(new Font("Calibri", Font.ITALIC + Font.BOLD, 40));
                this.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mousePressed(MouseEvent e) {
                        mousePt = e.getPoint();
                        repaint();
                    }
                });
                this.addMouseMotionListener(new MouseMotionAdapter() {

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        int dx = e.getX() - mousePt.x;
                        int dy = e.getY() - mousePt.y;
                        textPt.setLocation(textPt.x + dx, textPt.y + dy);
                        mousePt = e.getPoint();
                        repaint();
                    }
                });
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(W, H);
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w2 = g.getFontMetrics().stringWidth(TITLE) / 2;
                g.drawString(TITLE, textPt.x - w2, textPt.y);
            }

        }
    }
