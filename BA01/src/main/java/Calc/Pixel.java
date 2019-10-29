package Calc;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


//class for getting the pixels into an coordinate system
public class Pixel {

    GeoCalc geoCalcinst = new GeoCalc();

    double[][][] arrayPix1 = new double[80][87][3]; //arrays for storing the pixel coords into it
    double[][][] arrayPix2 = new double[20][60][3];
    double[][][] arrayPix3 = new double[20][60][3];
    double[][][] arrayPix4 = new double[20][60][3];
    double[][][] arrayPix5 = new double[20][60][3];
    double[][][] arrayPix6 = new double[20][60][3];
    double[][][] arrayPix7 = new double[20][60][3];

    double calcparam1P = 0;  //value for calculating geo coordinates in meter
    double calcparam2P = 0;
    double calcparam3P = 0;
    double calcparam4P = 0;

    double iv112 = 0;   //intermediate variable for distance
    double iv113 = 0;
    double iv212 = 0;
    double iv213 = 0;
    double iv312 = 0;
    double iv313 = 0;
    double iv412 = 0;
    double iv413 = 0;
    double iv512 = 0;
    double iv513 = 0;
    double iv612 = 0;
    double iv613 = 0;
    double iv712 = 0;
    double iv713 = 0;

    public ArrayList<Integer> store = new ArrayList<Integer>(); //storing coords of pixels into it

    //getting the distance between the geo coords
    public void Pixeldist() {
        iv112 = geoCalcinst.distance(40.809448, -73.960767, 40.809714, -73.960558);
        iv113 = geoCalcinst.distance(40.809448, -73.960767, 40.809584, -73.961093);
        iv212 = geoCalcinst.distance(40.809458, -73.960689, 40.809697, -73.960514);
        iv213 = geoCalcinst.distance(40.809448, -73.960767, 40.809584, -73.961093);
        iv312 = geoCalcinst.distance(40.809612, -73.961073, 40.809840, -73.960927);
        iv313 = geoCalcinst.distance(40.809612, -73.961073, 40.809632, -73.961126);
        iv412 = geoCalcinst.distance(40.809445, -73.960635, 40.809645, -73.960497);
        iv413 = geoCalcinst.distance(40.809445, -73.960635, 40.809462, -73.960683);
        iv512 = geoCalcinst.distance(40.809632, -73.961126, 40.809839, -73.960976);
        iv513 = geoCalcinst.distance(40.809632, -73.961126, 40.809658, -73.961160);
        iv612 = geoCalcinst.distance(40.809491, -73.960582, 40.809616, -73.960491);
        iv613 = geoCalcinst.distance(40.809491, -73.960582, 40.809498, -73.960594);
        iv712 = geoCalcinst.distance(40.809715, -73.961116, 40.809721, -73.961129);
        iv713 = geoCalcinst.distance(40.809715, -73.961116, 40.809721, -73.961129);
    }

    void differenceP(int x1, int y1, int x2, int y2, int x3, int y3, double ivxx1, double ivxx2) {

        //conversion factor calculation
        double help1 = x1 - x2;
        double help2 = y1 - y2;
        double help3 = x1 - x3;
        double help4 = y1 - y3;
        help1 = help1 / ivxx1;
        help2 = help2 / ivxx1;
        help3 = help3 / ivxx2;
        help4 = help4 / ivxx2;
        int mult = 1;//100;
        this.calcparam1P = help1 * mult; //North-South param for calculating geo coordinates in meter
        this.calcparam2P = help2 * mult; //North-South
        this.calcparam3P = help3 * mult; //East-West
        this.calcparam4P = help4 * mult; //East-West
    }

    //function for writing pixel coordinates in array
    void Pixelcalc(int x1, int y1, int x2, int y2, int x3, int y3, double ivxx1, double ivxx2, double[][][] arrayPix) {
        differenceP(x1, y1, x2, y2, x3, y3, ivxx1, ivxx2);

        double xanf1 = x1;
        double yanf1 = y1;
        double x11 = x1;
        double y11 = y1;

        for (int i = 0; i < arrayPix.length; i++) {
            for (int j = 0; j < arrayPix[0].length; j++) {
                arrayPix[i][j][0] = x11;
                arrayPix[i][j][1] = y11;
                x11 = x11 + calcparam1P;   //new x1-value shifted by calcparam1 factor
                y11 = y11 + calcparam2P;
                store.add((int) x11);
                store.add((int) y11);
            }
            x11 = xanf1 + calcparam3P;
            y11 = yanf1 + calcparam4P;
            xanf1 = x11;
            yanf1 = y11;
        }
    }

    //running Pixelcalc with corresponding int coords in coordinate system
    public void PixelcalcGO() {
        PrintWriter writer;

        {
            try {
                writer = new PrintWriter("PixelCoords.txt", "UTF-8");

                Pixelcalc(1081, 812, 1100, 105, 219, 814, iv112, iv113, arrayPix1); //M
                Pixelcalc(1149, 736, 1154, 171, 1089, 744, iv212, iv213, arrayPix2);//ML
                Pixelcalc(210, 744, 217, 148, 153, 744, iv312, iv313, arrayPix3);//MH
                Pixelcalc(1211, 729, 1225, 182, 1156, 729, iv412, iv413, arrayPix4);//LL
                Pixelcalc(153, 737, 155, 157, 86, 738, iv512, iv513, arrayPix5);//LL
                Pixelcalc(1235, 580, 1214, 582, 1237, 333, iv612, iv613, arrayPix6);//HL
                Pixelcalc(88, 578, 67, 321, 63, 576, iv712, iv713, arrayPix7);//HH

                for(int cnt = 0; cnt < store.size(); cnt++){
                    writer.println(store.get(cnt));
                }

                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
