package Calc;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

//class for converting geo coordinates into coordinates used in a normal coordinate system
public class GeoCalc {
    double calcparam1 = 0;  //value for calculating geo coordinates in meter
    double calcparam2 = 0;
    double calcparam3 = 0;
    double calcparam4 = 0;

    //Arrays for storing geo coords from the building, divided into 7 parts from the inside out:
    //                  HH HL MH M ML LH LL
    //7 parts cause of the structure based on the plan of google maps

    double [][][] geoarrayM = new double[80][87][3];  //Main part(M), Array contains geo coordinates
  //  double [][][] convcoordM = new double [80][87][3];    //Array contains converted coordinates

    double [][][] geoarrayML = new double[5][18][3]; //Main part lower layer(ML)
  //  double [][][] convcoordML = new double[20][60][3];

    double [][][] geoarrayMH = new double[5][18][3]; //Main part higher layer(MH)
  //  double [][][] convcoordMH = new double[20][60][3];

    double [][][] geoarrayLL = new double[2][4][3]; //Lowest part lowest layer(LL)
  //  double [][][] convcoordLL = new double[20][60][3];

    double [][][] geoarrayLH = new double[4][17][3]; //Lowest part higher layer(LH)
  //  double [][][] convcoordLH = new double[20][60][3];

    double [][][] geoarrayHL = new double[4][17][3]; //Highest part lower layer(HL)
  //  double [][][] convcoordHL = new double[20][60][3];

    double [][][] geoarrayHH = new double[4][17][3]; //Highest part highest layer(HH)
  //  double [][][] convcoordHH = new double[20][60][3];



    //for calculating the geo coords into int values used for putting them in an own coordinate system later
    Mercator mercator = new Mercator.EllipticalMercator() {
        @Override
        double yAxisProjection(double input) {
            input = Math.min(Math.max(input, -89.5), 89.5);
            double earthDimensionalRateNormalized = 1.0 - Math.pow(Mercator.RADIUS_MINOR / Mercator.RADIUS_MAJOR, 2);

            double inputOnEarthProj = Math.sqrt(earthDimensionalRateNormalized) *
                    Math.sin(Math.toRadians(input));

            inputOnEarthProj = Math.pow(((1.0 - inputOnEarthProj) / (1.0 + inputOnEarthProj)),
                    0.5 * Math.sqrt(earthDimensionalRateNormalized));

            double inputOnEarthProjNormalized =
                    Math.tan(0.5 * ((Math.PI * 0.5) - Math.toRadians(input))) / inputOnEarthProj;

            return (-1) * Mercator.RADIUS_MAJOR * Math.log(inputOnEarthProjNormalized);
        }

        @Override
        double xAxisProjection(double input) {
            return Mercator.RADIUS_MAJOR * Math.toRadians(input);
        }
    };

    //function for distance between two geo coordinates according to the code from:
    // https://dzone.com/articles/distance-calculation-using-3
  public double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2; //
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.609344 * 1.1515 * 1000;
        return (dist);
    }
    //function converts decimal degrees to radians
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //function converts radians to decimal degrees
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    //function calculates difference between geo coordinates
    void difference(double x1, double y1, double x2, double y2,double x3, double y3) {
        double erg1 = distance(x1, y1, x2, y2); //distance for North-South distance
        double erg2 = distance(x1, y1, x3, y3);    //distance for East-West distance
        erg1 = erg1*10; //distance in 10cm steps
        erg2 = erg2*10;

        //conversion factor calculation
        double help1 = x2 - x1;
        double help2 = y2 - y1;
        double help3 = x3 - x1;
        double help4 = y3 - y1;
        help1 = help1 / erg1;
        help2 = help2 / erg1;
        help3 = help3 / erg2;
        help4 = help4 / erg2;
        int mult = 10;//or 100;
            this.calcparam1 = help1*mult; //North-South param for calculating geo coordinates in meter
            this.calcparam2 = help2*mult; //North-South
            this.calcparam3 = help3*mult; //East-West
            this.calcparam4 = help4*mult; //East-West
    }




    //function for writing geo coordinates in array
    void pointfinder(double x1, double y1, double x2, double y2, double x3, double y3, double[][][] arrayGeo) {

        //writing geo coords into .txt-file
        PrintWriter writer;

        {
            try {
                writer = new PrintWriter("GeoCoords.txt", "UTF-8");

                difference(x1, y1, x2, y2, x3, y3);
                double xanf1 = x1;
                double yanf1 = y1;
                for (int i = 0; i < arrayGeo.length; i++) {
                    for (int j = 0; j < arrayGeo[0].length; j++) {
                        arrayGeo[i][j][0] = x1; //lat-value
                        arrayGeo[i][j][1] = y1; //lon-value
                        writer.println(x1);
                        writer.println(y1 + "\n");
                        arrayGeo[i][j][2] = 0;  //boolean-value, indicates whether the point is already used for an IoT device
                        x1 = x1 + calcparam1;   //new x1-value shifted by calcparam1 factor
                        y1 = y1 + calcparam2;
                    }
                 //   System.out.println(x1+"\n"+y1+"\n");    //debug, shows last entries of geo coords arrays
                    x1 = xanf1 + calcparam3;
                    y1 = yanf1 + calcparam4;
                    xanf1 = x1;
                    yanf1 = y1;
                }
             //   System.out.println("Array end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! \n \n"); //debug
                writer.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    //function for getting geo coordinates in coordinate system by converting them with methods written above
    public void gradTocoord (double x1, double y1, double x2, double y2, double x3, double y3, double[][][] arrayGeo, double[][][] arrayconv){

      //writing geo coords of IoT devices in .txt-file
        PrintWriter writerIoTdevices;

        {
        try {
        writerIoTdevices = new PrintWriter("IoT devices.txt", "UTF-8");

    pointfinder(x1,y1,x2,y2,x3,y3,arrayGeo);
            for (int i = 0; i < arrayGeo.length; i++) {
                for (int j = 0; j < arrayGeo[0].length; j++) {
                    switch ((int) arrayGeo[i][j][2]) {
                        case 0: //no wall
                            break;
                        case 1: //wall
                                arrayconv[i][j][0] = mercator.xAxisProjection(arrayGeo[i][j][0]) - 4542500;  //converting geo coordinates
                                arrayconv[i][j][1] = (((mercator.yAxisProjection(arrayGeo[i][j][1])) * (-0.00001)) - 124) * 10000 - 5400;
                                arrayconv[i][j][2] = 1; //boolean-value remains unaffected

                            //first try, way more simple, but not that accurate. But it works ^^
                 /*           convcoord[i][j][0] = ((geoarray[i][j][0] - 40.809) * 100000);  //converting geo coordinates
                            convcoord[i][j][1] = ((geoarray[i][j][1] + 73.960) * (100000));
                            convcoord[i][j][2] = 1; //boolean-value remains unaffected  */
                            break;
                        case 2: //wall with device
                            arrayconv[i][j][0] = mercator.xAxisProjection(arrayGeo[i][j][0]) - 4542500;
                            arrayconv[i][j][1] = (((mercator.yAxisProjection(arrayGeo[i][j][1])) * (-0.00001)) - 124) * 10000 - 5400;
                            arrayconv[i][j][2] = 2;
                            writerIoTdevices.println("lat: "+arrayconv[i][j][0]+", long: "+arrayconv[i][j][1]);
                            break;
                        case 3: //no wall but device
                            arrayconv[i][j][0] = mercator.xAxisProjection(arrayGeo[i][j][0]) - 4542500;
                            arrayconv[i][j][1] = (((mercator.yAxisProjection(arrayGeo[i][j][1])) * (-0.00001)) - 124) * 10000 - 5400;
                            arrayconv[i][j][2] = 3;
                            writerIoTdevices.println("lat: "+arrayconv[i][j][0]+", long: "+arrayconv[i][j][1]+"\n");
                            break;
                    }
                }
            }
            writerIoTdevices.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        }

        }

    //method to start gradTocoord-function with given geo coords and arrays with already converted coords
    public void pointfinderGO(){
        pointfinder(40.809448, -73.960767, 40.809714, -73.960558, 40.809584, -73.961093, geoarrayM);
        pointfinder(40.809458, -73.960689, 40.809697, -73.960514, 40.809477, -73.960741, geoarrayML);
        pointfinder(40.809612, -73.961073, 40.809840, -73.960927, 40.809632, -73.961126, geoarrayMH);
        pointfinder(40.809445, -73.960635, 40.809645, -73.960497, 40.809462, -73.960683, geoarrayLH);
        pointfinder(40.809632, -73.961126, 40.809839, -73.960976, 40.809658, -73.961160, geoarrayHL);
        pointfinder(40.809491, -73.960582, 40.809616, -73.960491, 40.809498, -73.960594, geoarrayLL);
        pointfinder(40.809715, -73.961116, 40.809836, -73.961034, 40.809721, -73.961129, geoarrayHH);
    }



}

    

