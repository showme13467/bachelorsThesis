package Graphic;

import Calc.GeoCalc;
import Calc.Loading;
import Calc.Pixel;
import Calc.Readimage;

import java.awt.*;
import javax.swing.*;


public class gui extends JFrame {


    //   int x1 = 0;
    //   int y1 = 0;

    public gui() {
        super();
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Readimage Neu = new Readimage();
        Neu.Reading();


    }

    //function for painting the geo coordinates in gui
    public void paint(Graphics g) {
        GeoCalc test2 = new GeoCalc();
        g.setColor(Color.black);
        test2.pointfinderGO();

        Loading kkk = new Loading();
        kkk.loaddata("C:\\Users\\alexa\\Desktop\\Prog Proj\\BA01\\GeoCoords.txt");
        System.out.println(kkk.store.get(0));
        int cs = 0;
        while (cs < kkk.store.size() / 2) {
            g.drawLine(kkk.store.get(0), kkk.store.get(1), kkk.store.get(0), kkk.store.get(1));
            kkk.store.remove(0);
            kkk.store.remove(0);
        }

        Pixel Ptest = new Pixel();


    }

             /*  test2.gradTocoordGO();
                for (int i = 0; i < test2.convcoordM.length; i++) {
                    for (int j = 0; j < test2.convcoordM[0].length; j++) {
                        x1 = (int) test2.convcoordM[i][j][0] - 300; //setting all points near centre of gui
                        y1 = (int) test2.convcoordM[i][j][1] + 7;
                        switch ((int) test2.convcoordM[i][j][2]) {
                            case 0:
                                break;
                            case 1:
                                g.drawLine(x1 + 1, y1 + 1, x1, y1);
                                break;
                            case 2:
                                g.setColor(Color.blue);
                                g.drawLine(x1 + 1, y1 + 1, x1, y1);
                                g.setColor(Color.black);
                                break;
                            case 3:
                                g.setColor(Color.blue);
                                g.drawLine(x1 + 1, y1 + 1, x1, y1);
                                g.setColor(Color.black);
                                break;

                        }
                    }

            }
            for (int i = 0; i < test2.convcoordML.length; i++) {
                for (int j = 0; j < test2.convcoordML[0].length; j++) {
                    x1 = (int) test2.convcoordML[i][j][0] -300; //setting all points near centre of gui
                    y1 = (int) test2.convcoordML[i][j][1] +7;
                    switch ((int) test2.convcoordML[i][j][2]) {
                        case 0:
                            break;
                        case 1:
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            break;
                        case 2:
                            g.setColor(Color.blue);
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            g.setColor(Color.black);
                            break;
                        case 3:
                            g.setColor(Color.blue);
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            g.setColor(Color.black);
                            break;
                    }
                }
            }
            for (int i = 0; i < test2.convcoordMH.length; i++) {
                for (int j = 0; j < test2.convcoordMH[0].length; j++) {
                    x1 = (int) test2.convcoordMH[i][j][0] -300; //setting all points near centre of gui
                    y1 = (int) test2.convcoordMH[i][j][1] +7;
                    switch ((int) test2.convcoordMH[i][j][2]) {
                        case 0:
                            break;
                        case 1:
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            break;
                        case 2:
                            g.setColor(Color.blue);
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            g.setColor(Color.black);
                            break;
                        case 3:
                            g.setColor(Color.blue);
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            g.setColor(Color.black);
                            break;
                    }
                }
            }
            for (int i = 0; i < test2.convcoordLH.length; i++) {
                for (int j = 0; j < test2.convcoordLH[0].length; j++) {
                    x1 = (int) test2.convcoordLH[i][j][0] -300; //setting all points near centre of gui
                    y1 = (int) test2.convcoordLH[i][j][1] +7;
                    switch ((int) test2.convcoordLH[i][j][2]) {
                        case 0:
                            break;
                        case 1:
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            break;
                        case 2:
                            g.setColor(Color.blue);
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            g.setColor(Color.black);
                            break;
                        case 3:
                            g.setColor(Color.blue);
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            g.setColor(Color.black);
                            break;
                    }
                }
            }
            for (int i = 0; i < test2.convcoordHL.length; i++) {
                for (int j = 0; j < test2.convcoordHL[0].length; j++) {
                    x1 = (int) test2.convcoordHL[i][j][0] -300; //setting all points near centre of gui
                    y1 = (int) test2.convcoordHL[i][j][1] +7;
                    switch ((int) test2.convcoordHL[i][j][2]) {
                        case 0:
                            break;
                        case 1:
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            break;
                        case 2:
                            g.setColor(Color.blue);
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            g.setColor(Color.black);
                            break;
                        case 3:
                            g.setColor(Color.blue);
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            g.setColor(Color.black);
                            break;
                    }
                }
            }
            for (int i = 0; i < test2.convcoordLL.length; i++) {
                for (int j = 0; j < test2.convcoordLL[0].length; j++) {
                    x1 = (int) test2.convcoordLL[i][j][0] -300; //setting all points near centre of gui
                    y1 = (int) test2.convcoordLL[i][j][1] +7;
                    switch ((int) test2.convcoordLL[i][j][2]) {
                        case 0:
                            break;
                        case 1:
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            break;
                        case 2:
                            g.setColor(Color.blue);
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            g.setColor(Color.black);
                            break;
                        case 3:
                            g.setColor(Color.blue);
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            g.setColor(Color.black);
                            break;
                    }
                }
            }
            for (int i = 0; i < test2.convcoordHH.length; i++) {
                for (int j = 0; j < test2.convcoordHH[0].length; j++) {
                    x1 = (int) test2.convcoordHH[i][j][0] -300; //setting all points near centre of gui
                    y1 = (int) test2.convcoordHH[i][j][1] +7;
                    switch ((int) test2.convcoordHH[i][j][2]) {
                        case 0:
                            break;
                        case 1:
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            break;
                        case 2:
                            g.setColor(Color.blue);
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            g.setColor(Color.black);
                            break;
                        case 3:
                            g.setColor(Color.blue);
                            g.drawLine(x1 + 1, y1 + 1, x1, y1);
                            g.setColor(Color.black);
                            break;
                    }
                }
            }

            */


    //main method
  /*  public static void main(String[] args) {


        new sys_windows();

         /*   gui pc =  new gui();
            pc.setSize(1600, 1200);
            pc.setVisible(true);

*/
    }



