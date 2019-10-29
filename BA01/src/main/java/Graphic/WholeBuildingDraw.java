package Graphic;

import Calc.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

//main class for creating gui

public class WholeBuildingDraw extends JFrame {//implements MouseListener {
    public int xclickS; //xcoord for drawing line in roomIRTLabSetZ
    public int yclickS;

    {
       // addMouseListener(this);   //MouseEvent for point painting ***here disabled at the moment for test reasons***

                JButton IRTLabBtn = new JButton("IRTLabBtn");   //Link to room view
                IRTLabBtn.setBounds(960, 210, 150, 145);
                add(IRTLabBtn);
                IRTLabBtn.setVisible(true);

           /*     JButton R1Btn = new JButton("R1Btn");
                R1Btn.setBounds(960, 360, 145, 540);
                add(R1Btn);
                R1Btn.setVisible(true);
*/

                setSize(1600, 1200);
                setLayout(null);
                setVisible(true);

                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            }

       /*     public void mouseClicked(MouseEvent e) {
                Graphics g = getGraphics();
                g.setColor(Color.BLUE);
                g.fillOval(e.getX(), e.getY(), 15, 15);
                System.out.println("x: " + e.getX() + " y: " + e.getY());
                if(e.getX()>=0 && e.getY() >= 0) {

                }


            }


    public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            */

            //function for painting the geo coordinates in gui
            public void paint(Graphics g) {
                Readimage readinst = new Readimage();
                readinst.Reading();

                GeoCalc geoCalcinst = new GeoCalc();
                g.setColor(Color.black);
                geoCalcinst.pointfinderGO();

                Loading loadinginst = new Loading();
                loadinginst.loaddata("C:\\Users\\alexa\\Desktop\\Prog Proj\\BA01\\WallCoords.txt");

                Pixel pixelinst = new Pixel();
                pixelinst.Pixeldist();
                pixelinst.PixelcalcGO();

                //drawing the wall, store contains the wall coords
                int cs = 0;
                while (cs < loadinginst.store.size() / 2) {
                    g.drawLine(loadinginst.store.get(0), loadinginst.store.get(1),
                            loadinginst.store.get(0), loadinginst.store.get(1));
                    loadinginst.store.remove(0);
                    loadinginst.store.remove(0);
                }



            }


            //main method
            public static void main(String[] args) throws IOException, URISyntaxException {

            //    new WholeBuildingDraw();
               new floorplan();
           //     new roomIRTLabSetXY();

           //    new roomIRTLabSetZ();

            }
        }
