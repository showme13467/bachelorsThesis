package Calc;

import static java.lang.Math.*;

public class test {
    private static double refgeoaX = -73.96095;//40.809840;
    private static double refgeoaY = 40.80983;//-73.960924;
    private static double refgeobX = -73.96071;//40.809445;
    private static double refgeobY = 40.80947;//-73.960636;

    private static double newgeoX = 0; // c(x)
    private static double newgeoY = 0; // c(y)


    private static float refpixelaX = 144;//171; // a(x)
    private static float refpixelaY = 130;//113; // a(y)
    private static float refpixelbX = 1068;//1169; // b(x)
    private static float refpixelbY = 686;//690; // b(y)

    private static double zaX = 0; // conversionfactor for ax
    private static double zaY = 0; // conversionfactor for ay


    public static void convertCoords(float refpixelcX, float refpixelcY) {

        double p = 0;
        double q = 0;
        double alpha = 0;
        double lambda = 0;
        double delta = 0;

        lambda = Math.abs((refpixelaX - refpixelbX) / (refgeoaY - refgeobY));
        delta = Math.abs((refpixelaY - refpixelbY) / (refgeoaX - refgeobX));

        System.out.println("lambda: "+lambda+"\ndelta: "+delta);

        double help1 = lambda * (refgeoaY - refgeobY) * (refpixelaX - refpixelbX);
        double help2 = delta * (refgeoaX - refgeobX) * (refpixelaY - refpixelbY);
        double help3 = Math.pow((refpixelaX-refpixelbX),2);
        double help4 = Math.pow((refpixelaY-refpixelbY),2);

        alpha = asin(-1* ((help1 + help2) / (help3 + help4)) );
        System.out.println("alpha: "+alpha);
        p = ((refpixelaX * sin(alpha) + refpixelaY * cos(alpha)) / lambda) + refgeoaY;

        q = ((- refpixelaX * cos(alpha) + refpixelaY * sin(alpha)) / delta) + refgeoaX;

        System.out.println("p: "+p+"\nq: "+q);

        newgeoY = ((- refpixelcX * sin(alpha) - refpixelcY * cos(alpha)) / lambda) + p;
        newgeoX = ((refpixelcX * cos(alpha) - refpixelcY * sin(alpha)) / delta) + q;
    }


    public static void main(String[] args){
    convertCoords(118*2,252*2);
    //convertCoords(208,528); //IRT-Lab, bottom left
    //convertCoords(600,400); //center point of schapiro building
    //convertCoords(940, 300); //room upper right
    System.out.println("new coords: " + newgeoY + ", " + newgeoX);
  }
}


/*
// convert pixel coords into geo coords
        public static void convertCoords(float refpixelcX, float refpixelcY) {
            double p = 0;
            double q = 0;
            double alpha = 0;
            double lambda = 0;
            double delta = 0;

            lambda = Math.abs((refpixelaX - refpixelbX) / (refgeoaX - refgeobX));
            delta = Math.abs((refpixelaY - refpixelbY) / (refgeoaY - refgeobY));

            double help1 = lambda * (refgeoaX - refgeobX) * (refpixelaX - refpixelbX);
            double help2 = delta * (refgeoaY - refgeobY) * (refpixelaY - refpixelbY);
            double help3 = Math.pow((refpixelaX - refpixelbX), 2);
            double help4 = Math.pow((refpixelaY - refpixelbY), 2);

            alpha = asin(-1 * ((help1 + help2) / (help3 + help4)));
            p = ((refpixelaX * sin(alpha) + refpixelaY * cos(alpha)) / lambda) + refgeoaX;

            q = ((-refpixelaX * cos(alpha) + refpixelaY * sin(alpha)) / delta) + refgeoaY;

            newgeoX = ((-refpixelcX * sin(alpha) - refpixelcY * cos(alpha)) / lambda) + p;
            newgeoY = ((refpixelcX * cos(alpha) - refpixelcY * sin(alpha)) / delta) + q;
        }
    }
 */



/*
        double oldgeoaX = refgeoaX;
        double oldgeoaY = refgeoaY;
        double oldgeobX = refgeobX;
        double oldgeobY = refgeobY;

        double oldpixelaX = refpixelaX;
        double oldpixelaY = refpixelaY;
        double oldpixelbX = refpixelbX;
        double oldpixelbY = refpixelbY;

        if(refpixelaX != 0 && refpixelaY != 0) {
            zaX = (refpixelaY - refpixelbY) / (refpixelaY - 0);
            zaY = (refpixelaX - refpixelbX) / (refpixelaX - 0);

            newgeoX = refgeoaX - ((refgeoaX - refgeobX) / zaX);
            newgeoY = refgeoaY - ((refgeoaY - refgeobY) / zaY);

            refgeoaX = newgeoX;
            refgeoaY = newgeoY;
            refpixelaX = 0;
            refpixelaY = 0;
            System.out.println("Ref Geo A: " + refgeoaX + ", " + refgeoaY);
        }
        if(refpixelbX != 1210 && refpixelbY != 800) {
            zaX = (refpixelaY - refpixelbY) / (refpixelaY - 800);
            zaY = (refpixelaX - refpixelbX) / (refpixelaX - 1210);

            newgeoX = refgeoaX - ((refgeoaX - refgeobX) / zaX);
            newgeoY = refgeoaY - ((refgeoaY - refgeobY) / zaY);

            refgeobX = newgeoX;
            refgeobY = newgeoY;
            refpixelbX = 1210;
            refpixelbY = 800;
            System.out.println("Ref Geo B: " + refgeobX + ", " + refgeobY);
        }

        zaX = (oldpixelaY - oldpixelbY) / (oldpixelaY - 800);
        zaY = (oldpixelaX - oldpixelbX) / (oldpixelaX - 0);
        System.out.println("zaX and zaY: "+zaX+", "+zaY);

        newgeoX = oldgeoaX - ((oldgeoaX - oldgeobX) / zaX);
        newgeoY = oldgeoaY - ((oldgeoaY - oldgeobY) / zaY);

        double pixelcX = 0;
        double pixelcY = 800;
        double geocX = newgeoX;
        double geocY = newgeoY;
        System.out.println("geo C: "+geocX+", "+geocY);


        zaX = (refpixelaY-refpixelbY) / (refpixelaY-refpixelcY); //0 for A, 800 for B
        zaY = (refpixelaX-refpixelbX) / (refpixelaX-refpixelcX); //0 for A, 1210 for B

        newgeoX = refgeoaX - ((refgeoaX - refgeobX) / zaX);
        newgeoY = refgeoaY - ((refgeoaY - refgeobY) / zaY);

        System.out.println("before Rotation: "+newgeoX+", "+newgeoY);


        double xc = (refgeoaX + refgeobX) / 2; // Center point x coord
        double yc = (refgeoaY + refgeobY) / 2; // Center point y coord
        double xd = (refgeobX - refgeoaX) / 2; // Half-diagonal x coord
        double yd = (refgeobY - refgeoaY) / 2; // Half-diagonal y coord

        double x3 = xc - xd; // Third corner x coord
        double y3 = yc + yd; // Third corner y coord
        double x4 = xc + xd; // Fourth corner x coord
        double y4 = yc - yd; // Fourth corner y coord


        //calculating the angle
        double refpixelaXd = refpixelaX;
        double refpixelbXd = refpixelbX;
        double refpixelaYd = refpixelaY;
        double refpixelbYd = refpixelbY;

        System.out.println("Pixel Coords Ref Point A: "+refpixelaX+", "+refpixelaY+"\nPixelCoords Ref Point B: "+refpixelbX+", "+refpixelbY);
        System.out.println("Pixel Coords Corner 3: "+x3+", "+y3+"\nPixelCoords Corner 4: "+x4+", "+y4);


        double Rotationfactor = atan((y3 - refgeobY) / (refgeobX - x3));
        Rotationfactor = Rotationfactor*Math.PI / 180;
        System.out.println("new angle: "+Rotationfactor);

        double help1 = (newgeoX - refgeobX);
        double help2 = (newgeoY - refgeobY);
        double distanceCB = Math.pow(help1, 2) + Math.pow(help2, 2);
        distanceCB = sqrt(distanceCB);

        double sigma = atan(Math.abs(refgeobY - newgeoY) / Math.abs(refgeobX - newgeoX));
        sigma = sigma * Math.PI / 180;
        double beta = sigma - Rotationfactor;
        beta = beta * 180 / Math.PI;
        newgeoX = refgeobX - distanceCB * cos(beta);
        newgeoY = refgeobY + distanceCB * sin(beta);
        */




















/*
        // checking whether first ref point is upper left corner
        if (refpixelaX != 0 && refpixelaY != 0) {

            //calculating the conversionfactor z
            zaX = (refpixelaX - refpixelbX) / (refpixelaY - 0);
            zaY = (refpixelbY - refpixelaY) / (0 - refpixelaX);

            //calculating the new geo coord
            newgeoX = refgeoaX - ((refgeobY - refgeoaY) / zaY);
            newgeoY = refgeoaY + ((refgeoaX - refgeobX) / zaX);

            refpixelaX = 0;
            refpixelaY = 0;
            refgeoaX = newgeoX;
            refgeoaY = newgeoY;
            System.out.println("refgeo a: " + refgeoaX + ", " + refgeoaY);
        }

        // checking whether second ref point is lower right corner
        if (refpixelbX != 1210 && refpixelbY != 800) {

            zbX = (refpixelaX - refpixelbX) / (refpixelaY - 800);
            zbY = (refpixelbY - refpixelaY) / (1210 - refpixelaX);

            newgeoX = refgeoaX - ((refgeobY - refgeoaY) / zbY);
            newgeoY = refgeobY + ((refgeoaX - refgeobX) / zbX);

            refpixelbX = 1210;
            refpixelbY = 800;
            refgeobX = newgeoX;
            refgeobY = newgeoY;
            System.out.println("refgeo b: " + refgeobX + ", " + refgeobY);
        }
*/
