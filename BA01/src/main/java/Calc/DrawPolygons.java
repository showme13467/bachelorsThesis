package Calc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.javafx.iio.ImageStorage;
import javafx.scene.transform.Scale;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.spi.LocaleServiceProvider;
import javax.imageio.ImageIO;
import javax.swing.*;

import static Calc.DrawPolygons.Drawing.*;


public class DrawPolygons {

    public static String RoomNameByUserStr=""; // name of the current polygon the user created
    public static Boolean firstdrawing = true; // bool for checking whether the program is started for loading gui
    public static Boolean containsbool = false; // bool for checking whether a selected point is inside a polygon

    public static String[] FloorNameByUserStrArray; // contains all floor names
    public static String FloorNameByUserStr; // floor name selected in the Selectionbox
    public static String[] FloorIDArray; // contains all IDs of floors
    public static String FloorID; // current floor ID

    public static String[] BuildingNameByUserStrArray; // contains all building names
    public static String BuildingNameByUserStr; // building name selected in the Selectionbox
    public static String[] BuildingIDArray; // contains all IDs of buildings
    public static String BuildingID; // current building ID

    public static String PolygonID = ""; // current Polygon ID
    private static List<String> PolygonIDArray = new ArrayList<String>(); // contains all IDs of polygons
    public static int polygonIDcnt; // cnt for ID while removing ID when deleting a polygon
    public static ArrayList<String> PolygonnameArray = new ArrayList<String>(); // contains all names of polygons
    public static String[][] PolygonGeoCoords = new String[][]{}; // Array of geo Coords of a polygon

    public static Boolean updatebool = false; // bool for checking, whether polygon is in updatemode
    public static Boolean updateboolM = false; // bool for checking, whether updatemode is already selected

    public static File urlfloorplan; // file where the image of the current floorplan is saved to

    protected static int xpos =0; // x position of mouse while checking whether a point selected by user is inside a polygon
    protected static int ypos =0; // y position of mouse while checking whether a point selected by user is inside a polygon

    public static Boolean NewPolyBool = true; // bool whether there is a new poylgon after saving one
    public static Boolean KeyAllowedSavingPolygon = false; // bool whether KeyBoardInput is Allowed
    public static Boolean Fillpolygon = false; // bool whether polygon is selected and should be filled out

    public static Boolean txtcurrentpolygon = false;

/*    public static Boolean ellipsenbool = false;
    public static int ellipsecnt = 0;
    public static int dellipsecnt = 0;
    public static Boolean dellipsebool = false; */

    static int txtcnt = 1; // cnt for advices

    public static JFrame frame = new JFrame();

    static List<Integer> IntPolyPoints = new ArrayList<Integer>(); // list of coord points of current polygon

    public static int Polyboolcnt = 0; // cnt for whether a polygon is drew completely or canceled


    protected static final int Window_width = 1210; // width of window
    protected static final int Window_height = 1200; // height of window

    public static class Drawing extends JPanel {

     /*   static Ellipse2D dellipse = new Ellipse2D.Double();
        static Ellipse2D ellipse = new Ellipse2D.Double();
        static ArrayList<Ellipse2D> ellipse2DArrayList = new ArrayList<>(); */

        protected static final Font FONT = new Font("Arial", Font.BOLD, 18); // basic font

        protected static List<Polygon> polygons = new ArrayList<Polygon>(); // list of all polygons

        protected static Polygon currentPolygon = new Polygon(); // polygon user is creating at the moment
        protected static Polygon updatepolygon = new Polygon(); // polygon which is changed in updatemode
        protected static Polygon fillpolygon = new Polygon(); // polygon which is filled in updatemode


        /*
        Method which is called when Polygon is finished and should to be created and drawn

        @param helpintx - gets x coord of point of beginning of polygon for drawing it completely
        @param helpinty - gets y coord of point of beginning of polygon for drawing it completely
         */
        public static void PolygonFinished() throws IOException {
            NewPolyBool = true;
            int helpintx = currentPolygon.xpoints[0];
            int helpinty = currentPolygon.ypoints[0];
            currentPolygon.addPoint(helpintx, helpinty);
            IntPolyPoints.add(helpintx);
            IntPolyPoints.add(helpinty);
            createPolygon();
        }

        /*
        Mouseclick handling , when user clicks on floor plan
         */
        private MouseAdapter mouseListener = new MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                //LeftMouseButton handling => draw
                if (e.getY() < 800 && NewPolyBool == true && SwingUtilities.isLeftMouseButton(e)) {
                    if(e.getClickCount() ==2){
                    for(int i=0;i<polygons.size();i++) {
                        if (polygons.get(i).contains(e.getX(), e.getY())) {

                            updatebool = true;
                            updatepolygon = polygons.get(i);
                            polygonIDcnt = i;
                            clearCurrentPolygon();
                            Polyboolcnt = 0;
                            IntPolyPoints = new ArrayList<Integer>();
                            if(!updateboolM) {
                                updateboolM = true;
                            }
                        }
                    }
                    //giving currentpolygon the points of the restored polygon
                    if(updateboolM){
                        for(int j=0;j<updatepolygon.npoints;j++){
                            addPoint(updatepolygon.xpoints[j],updatepolygon.ypoints[j]);
                             }
                             updateboolM = false;
                        }
                    }

                    else if (e.getClickCount() == 1) {
                                    containsbool = true;
                                    xpos = e.getX();
                                    ypos = e.getY();

                            addPoint(e.getX(), e.getY());
                            KeyAllowedSavingPolygon = true;
                    }

                    //RightMouseButton handling => cancel drawing
                } else if (NewPolyBool && SwingUtilities.isRightMouseButton(e)) {
                    clearCurrentPolygon();
                    System.out.println("Polygon cleared!");
   /*                 ellipse2DArrayList.clear();
                    ellipse = new Ellipse2D.Double();
                    dellipse = new Ellipse2D.Double();
                    ellipsecnt = 0;
                    dellipsecnt = 0; */

                    //loop for removing already inserted coord points of canceled polygon
                    for (int i = Polyboolcnt; i > 0; i--) {
                        IntPolyPoints.remove(i - 1);
                    }

                    Polyboolcnt = 0; //new polygon
                }
            }

         /*   @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Mouse Pressed");
                FrameRepaint();
            } */
        };


             /*   private MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        for (int ecnt = 0; ecnt < ellipse2DArrayList.size(); ecnt++) {
                            if (ellipse2DArrayList.get(ecnt).contains(e.getX(), e.getY())) {
                                System.out.println("Ellipse " + ecnt + " clicked");
                                dellipsebool = true;
                                dellipsecnt = ecnt;
                            }
                        }
                        if(dellipsebool) {
                            Point p = e.getPoint();
                            System.out.println("Mouse dragged to: " + p.x + ", " + p.y);
                            IntPolyPoints.set(dellipsecnt, p.x);
                            IntPolyPoints.set(dellipsecnt+1, p.y);
                            clearCurrentPolygon();
                            for (int i = 0; i < IntPolyPoints.size(); i++) {
                                currentPolygon.addPoint(IntPolyPoints.get(i), IntPolyPoints.get(i + 1));
                                Polyboolcnt +=2;
                                i++;
                            }
                            dellipse = new Ellipse2D.Double(p.x-10,p.y-10,20,20);
                            ellipse2DArrayList.set(dellipsecnt, dellipse);

                            drawPolygon(getGraphics(), currentPolygon);
                            FrameRepaint();
                        }
                    }

                    @Override
                    public void mouseMoved(MouseEvent e) {
                    }
                };
*/


        public Drawing() {
            this.addMouseListener(mouseListener);
        //      this.addMouseMotionListener(mouseMotionListener);
        }

        public void addNotify() {
            super.addNotify();
            requestFocus();
        }

        //adding a point to the current poylgon
        protected void addPoint(int x, int y) {
          //   ellipsenbool = true;

            currentPolygon.addPoint(x, y);
            System.out.println("x + y: " + x + ", " + y);

            IntPolyPoints.add(x); //x-coord of new point of polygon
            Polyboolcnt += 1;

            IntPolyPoints.add(y); //y-coord of new point of polygon
            Polyboolcnt += 1;
            System.out.println("Polyboolcnt: " + Polyboolcnt);

            FrameRepaint();
        }

        //canceling to draw polygon
        protected static void clearCurrentPolygon() {
            currentPolygon = new Polygon();
            containsbool = false;
            FrameRepaint();
        }

        //polygon must consist out of at least 3 points minimum
        public static void createPolygon() throws IOException { //protected
            if (currentPolygon.npoints > 2) {
                if(updatebool){
                    txtcurrentpolygon = true;

                    System.out.println("CURRENTPOLYGON POINTS: "+currentPolygon.npoints);
                    updaterequest();
                    polygons.add(polygonIDcnt,currentPolygon);
                    PolygonnameArray.add(polygonIDcnt,RoomNameByUserStr);
                    IntPolyPoints = new ArrayList<Integer>();
                    Polyboolcnt = 0;
                    polygons.remove(polygonIDcnt+1);
                    PolygonnameArray.remove(polygonIDcnt+1);
                    GetGivenPolygons();
                }
                else {
                    polygons.add(currentPolygon);
                    PolygonnameArray.add(RoomNameByUserStr);
                    System.out.println("Polygon finished! \n \n");
                    Polyboolcnt = 0; //polygon finished, ready for new one
                    sendPolygon();
                    IntPolyPoints = new ArrayList<>();
                }
                updatebool = false;
            }
            clearCurrentPolygon();
            FrameRepaint();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(Window_width, Window_height);
        }

        // getting the image of the current floor plan
        private void getimage(String name) {


            String filename = "https://www.study-in-bavaria.de/fileadmin/_migrated/pics/Geb33_a_02";//+"/"+name;

            name = name + ".jpg";

            BufferedImage image = null;
            try {

                URL url = new URL(filename + ".jpg");
                image = ImageIO.read(url);

                BufferedImage blackwhiteImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

                for (int i = 0; i < image.getWidth(); i++) {
                    for (int j = 0; j < image.getHeight(); j++) {
                        Color c = new Color(image.getRGB(i, j));
                        Color myWhite = new Color(255, 255, 255);
                        Color myBlack = new Color(0, 0, 0);

                        //if the pixel is black(wall), set as black in new image
                        if (c.getRed() < 135 && c.getGreen() < 135 && c.getBlue() < 135) {
                            blackwhiteImage.setRGB(i, j, myBlack.getRGB());

                        } else {
                            blackwhiteImage.setRGB(i, j, myWhite.getRGB());
                        }
                    }
                }

                BufferedImage scaledImage = new BufferedImage(1211,800, BufferedImage.TYPE_INT_ARGB);
                final AffineTransform at = AffineTransform.getScaleInstance(2.0, 2.0);
                final AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
                scaledImage = ato.filter(blackwhiteImage, scaledImage);

                BufferedImage newimage= scaledImage;



                File imagefile = new File(name);
                if(imagefile.exists() && !imagefile.isDirectory()) {}
                else {
                    ImageIO.write(newimage, "jpg", imagefile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

         urlfloorplan = new File(name);


        }

        //method runs all the time, handling of drawing and setting up the drawing area
        @Override
        protected void paintComponent(Graphics g) {
            // super.paintComponent(g);

//convertCoords();

            //setting the floorplan as the background

     /*       getimage(FloorID);

                try {
                    BufferedImage image = ImageIO.read(urlfloorplan);
                    g.drawImage(image,0,0,null);
                } catch (IOException e) {
                    e.printStackTrace();
                } */


            File originalimage = new File("C:\\Users\\alexa\\Desktop\\BA\\floor plans\\7th\\coordpainting3.png");

            BufferedImage img = null;
            try {
                img = ImageIO.read(originalimage);
                g.drawImage(img, 0, 0, null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(firstdrawing){
                try {
                    GetGivenPolygons();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

           g.setFont(FONT);

            // placing the name of the polygon next to the polygon
            int lowestx; // lowest x coord of polygon
            int lowesty; // lowest y coord of polygon
            for (int i=0;i<polygons.size();i++){
                g.setColor(Color.RED);
                drawPolygon(g,polygons.get(i));

                lowestx = 0;
                lowesty = 0;

                for(int k=0;k<polygons.get(i).xpoints.length;k++) {
                    if(polygons.get(i).xpoints[k] != 0) {
                        lowestx = polygons.get(i).xpoints[k];
                    }
                    if(polygons.get(i).ypoints[k] != 0){
                        lowesty = polygons.get(i).ypoints[k];
                    }
                }

                for(int j=0;j<polygons.get(i).xpoints.length;j++){
                    if(lowestx > polygons.get(i).xpoints[j] && polygons.get(i).xpoints[j] != 0){
                        lowestx = polygons.get(i).xpoints[j];
                    }
                    if(lowesty > polygons.get(i).ypoints[j] && polygons.get(i).ypoints [j]!= 0){
                        lowesty = polygons.get(i).ypoints[j];
                    }
                }

                g.setColor(Color.DARK_GRAY);
                g.drawString(PolygonnameArray.get(i),lowestx+40,lowesty+40);
                }

            g.setColor(Color.GREEN);

            drawPolygon(g, currentPolygon);
          /*  if(txtcurrentpolygon) {
                int lowestxCP = currentPolygon.xpoints[0];
                int lowestyCP = currentPolygon.ypoints[0];
                for (int j = 0; j < currentPolygon.xpoints.length; j++) {
                    if (lowestxCP > currentPolygon.xpoints[j]) {
                        lowestxCP = currentPolygon.xpoints[j];
                    }
                    if (lowestyCP > currentPolygon.ypoints[j]) {
                        lowestyCP = currentPolygon.ypoints[j];
                    }
                }
                g.setColor(Color.DARK_GRAY);
                if (lowestxCP == 0 && lowestyCP == 0) {
                    lowestxCP = polygons.get(polygons.lastIndexOf(polygons) + 1).xpoints[0];
                    lowestyCP = polygons.get(polygons.lastIndexOf(polygons) + 1).ypoints[0];
                }
                g.drawString(RoomNameByUserStr, lowestxCP + 40, lowestyCP + 40);
            }
            txtcurrentpolygon = false; */
        }

        //drawing current polygon and lines
        private void drawPolygon(Graphics g, Polygon polygon) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3)); //thickness of lines
            if (polygon.npoints < 3) {
                if (polygon.npoints == 1) {
                    g2.fillOval(polygon.xpoints[0] - 2, polygon.ypoints[0] - 2, 4, 4);
                    drawNthPoint(g2, polygon, 0);
                } else if (polygon.npoints == 2) {
                    g2.drawLine(polygon.xpoints[0], polygon.ypoints[0], polygon.xpoints[1], polygon.ypoints[1]);
                    drawNthPoint(g2, polygon, 0);
                    drawNthPoint(g2, polygon, 1);
                }
            } else {
                int[] x = polygon.xpoints;
                int[] y = polygon.ypoints;


                for (int i = 0; i < polygon.npoints - 1; i++) {
                    g2.drawLine(x[i], y[i], x[i + 1], y[i + 1]);
                }
            }

            //calling the function for the letters
            for (int i = 0; i < polygon.npoints; i++) {
                drawNthPoint(g2, polygon, i);
            }

        /*    for (int i = 0; i < polygons.size(); i++) {
                g2.setColor(Color.RED);
                g2.fill(polygons.get(i));
            } */
            g2.setColor(Color.GREEN);


            //when point added to current polygon, calling function to set up ellipse
    /*            if(ellipsenbool) {
                    setDellipse(g2, currentPolygon, ellipsecnt);
                }

                //drawing all ellipses
                for(int i=0;i<ellipsecnt;i++){
                    g2.draw(ellipse2DArrayList.get(i));
                }
                ellipsenbool = false;  */
        }

        //Designation of the corner points of a polygon
        private static void drawNthPoint(Graphics g, Polygon polygon, int nth) {
            Graphics2D g2 = (Graphics2D) g;
            // Only works 26 times!
            String name = Character.toString((char) ('A' + nth));
            int x = polygon.xpoints[nth];
            int height = g.getFontMetrics().getHeight();
            int y = polygon.ypoints[nth] < height ? polygon.ypoints[nth] + height : polygon.ypoints[nth];
            g2.drawString(name, x + 10, y);
        }

    /*        private static void setDellipse(Graphics g, Polygon polygon, int nth){
                Graphics2D g2 = (Graphics2D) g;
                int x = polygon.xpoints[nth];
                int height = g2.getFontMetrics().getHeight();
                int y = polygon.ypoints[nth] < height ? polygon.ypoints[nth] + height : polygon.ypoints[nth];
                ellipse = new Ellipse2D.Double(x-10,y-10,20,20);
                g2.draw(ellipse);
                ellipse2DArrayList.add(ellipse);
                System.out.println("Ellipse added! Size of ellipsearray: "+ellipse2DArrayList.size());
                ellipsecnt +=1;
                }
*/
        // convert pixel coords into geo coords
        public void convertCoords(){
            Point2D.Double ref1 = new Point2D.Double(40.809855, -73.961014); //45,113
            Point2D.Double ref2 = new Point2D.Double(40.809445, -73.960636); //1174,690
            Double stretchx1 = 0.0;
            Double stretchy = 0.0;
            Double rotation = 0.0;
        }

        // when Polygon contains point selected by mouse click, polygon is filled out
        public void PolygonContains(Graphics g) throws InterruptedException {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GREEN);

            containspointrequest();
            g2.fill(fillpolygon);
            g2.setColor(Color.GREEN);
            fillpolygon = new Polygon();
        }

        // setting up the already in the database stored polygons
        public static void GetGivenPolygons() throws IOException{
            polygons.clear();
            PolygonnameArray.clear();
            PolygonIDArray.clear();

            PostRequest x = new PostRequest();

                //drawing the polygons which are already in the database
                String Response =
                        x.executePost(
                              "http://irt-ap.cs.columbia.edu/api/roomsByFloor",
                              "{\n"
                                      + " \"floor\" :" + "\"" + FloorID + "\"" + "\n"
                                      + "}", "POST");


            System.out.println("GETGIVENPOLYGONSREQ: "+Response);
            JsonObject jsonObjectgivenpolygonsreq = new JsonParser().parse(Response).getAsJsonObject();
            JsonArray jsonarraygivenpolygonsreq = jsonObjectgivenpolygonsreq.getAsJsonArray("data");


    for (int i = 0; i < jsonarraygivenpolygonsreq.size(); i++) {
        String response = jsonarraygivenpolygonsreq.get(i).getAsJsonObject().get("name").toString();
        PolygonnameArray.add(response.substring(1, response.length() - 1));
    }
    for (int i = 0; i < jsonarraygivenpolygonsreq.size(); i++) {
        String response = jsonarraygivenpolygonsreq.get(i).getAsJsonObject().get("_id").toString();
        PolygonIDArray.add(response.substring(1, response.length() - 1));
    }

    for (int i = 0; i < jsonarraygivenpolygonsreq.size(); i++) {
        JsonArray pixeljsonarray = jsonarraygivenpolygonsreq.get(i).getAsJsonObject().get("pixel").getAsJsonArray();
        int[] points = new int[pixeljsonarray.size()];
        for(int h=0;h<pixeljsonarray.size();h++){
            points[h] = pixeljsonarray.get(h).getAsInt();
        }
                    int n = points.length;
                    int[] xp = new int[n / 2];
                    int[] yp = new int[n / 2];
                    int xcnt = 0;
                    int ycnt = 0;
                    for (int z = 0; z < n; z++) {
                        if (z % 2 == 0) {
                            xp[xcnt] = points[z];
                            xcnt += 1;
                        } else {
                            yp[ycnt] = points[z];
                            ycnt += 1;
                        }
                    }

                    Polygon poly = new Polygon(xp, yp, n / 2);
                    polygons.add(poly);
            }
            firstdrawing = false;
        }

        // request for updating a polygon
        public static void updaterequest() throws IOException {

            PostRequest x = new PostRequest();

            System.out.println("CURRENTPOLYGONS POINTS: "+currentPolygon.npoints);

            String pixelcoordsOfPolygon = "";
            for (int i = 0; i < currentPolygon.npoints; i++) {
                pixelcoordsOfPolygon += currentPolygon.xpoints[i] + ", " + currentPolygon.ypoints[i] + ",";
            }
            pixelcoordsOfPolygon = pixelcoordsOfPolygon.substring(0, pixelcoordsOfPolygon.length() - 1);

            String geocoordsOfPolygon ="";
            for (int i = 0; i < currentPolygon.npoints; i++) {
                geocoordsOfPolygon += "["+currentPolygon.xpoints[i] + ", " + currentPolygon.ypoints[i] + "],";
            }
            geocoordsOfPolygon = geocoordsOfPolygon.substring(0, geocoordsOfPolygon.length() - 1);

            PolygonID = PolygonIDArray.get(polygonIDcnt);

            String Response =
                    x.executePost(
                            "http://irt-ap.cs.columbia.edu/api/room/"+PolygonID,
                            "{\n"
                                    + "\"geometry\": {"+"\n"
                                    + "\"type\": "+"\"Polygon\""+",\n"
                                    + "\"coordinates\":"+"[["+geocoordsOfPolygon+"]]" +"\n},"
                                    + "\"pixel\":"+"["+pixelcoordsOfPolygon+"]"+",\n"
                                    + "\"name\" :"+"\""+RoomNameByUserStr+"\"" +"\n,"
                                    + "\"floor\" :"+"\""+FloorID+"\""+"\n"
                                    + "}", "PUT");

            JsonObject jsonObjectdeletereq = new JsonParser().parse(Response).getAsJsonObject();
            System.out.println("UPDATEREQ: "+jsonObjectdeletereq.getAsJsonObject().get("success"));

        }

        // request for sending the current polygon to the database
        public static void sendPolygon(){

            LocalTime Timebefore = LocalTime.now();


            String pixelcoordsOfPolygon = "";
            for (int i = 0; i < currentPolygon.npoints; i++) {
                pixelcoordsOfPolygon += currentPolygon.xpoints[i] + "," + currentPolygon.ypoints[i] + ",";
            }
            pixelcoordsOfPolygon = pixelcoordsOfPolygon.substring(0, pixelcoordsOfPolygon.length() - 1);

            String geocoordsOfPolygon = "";
            for (int i = 0; i < currentPolygon.npoints; i++) {
                geocoordsOfPolygon += "["+currentPolygon.xpoints[i]/10 + "," + currentPolygon.ypoints[i]/10 + "],";
            }
            geocoordsOfPolygon = geocoordsOfPolygon.substring(0,geocoordsOfPolygon.length()-1);

            PostRequest x = new PostRequest();

            //getting the buildings from the database
            String Response =
                    x.executePost(
                            "http://irt-ap.cs.columbia.edu/api/room",
                            "{\n"
                                    + "\"geometry\": {"+"\n"
                                    + "\"type\": "+"\"Polygon\""+",\n"
                                    + "\"coordinates\":"+"[["+geocoordsOfPolygon+"]]" +"\n},"
                                    + "\"pixel\":"+"["+pixelcoordsOfPolygon+"]"+"\n,"
                                    + "\"name\" :"+"\""+RoomNameByUserStr+"\"" +"\n,"
                                    + "\"floor\" :"+"\""+FloorID+"\""+"\n"
                                    + "}", "POST");

            System.out.println("SENDPOLYGONREQ"+Response);

            LocalTime TimeAfter = LocalTime.now();

            JsonObject jsonObjectsendpolygonreq = new JsonParser().parse(Response).getAsJsonObject();

            PolygonID = jsonObjectsendpolygonreq.getAsJsonObject().get("id").toString();
            PolygonIDArray.add(PolygonID.substring(1,PolygonID.length()-1));


            long ns = Duration.between(Timebefore, TimeAfter).toMillis();

            System.out.println("Duration: "+ns);

            /*
            1:  4 points:  ms
            2: 10 points:  ms
            3: 20 points:  ms

             */

        }

    }

    public static void containsroomrequest(){
        PostRequest x = new PostRequest();

        String geocoordsOfPolygon ="";
        for (int i = 0; i < fillpolygon.npoints; i++) {
            geocoordsOfPolygon += "["+fillpolygon.xpoints[i]/10 + ", " + fillpolygon.ypoints[i]/10 + "],";
        }
        geocoordsOfPolygon = geocoordsOfPolygon.substring(0, geocoordsOfPolygon.length() - 1);

        String Response =
                x.executePost(
                        "http://irt-ap.cs.columbia.edu/api/roomsByPolygon",
                        "{\n"
                                + "\"geometry\": {"+"\n"
                                + "\"type\": "+"\"Polygon\""+",\n"
                                + "\"coordinates\":"+"[["+geocoordsOfPolygon+"]]" +"\n}"
                                + "}", "POST");

        JsonObject jsonObjectdeletereq = new JsonParser().parse(Response).getAsJsonObject();
        System.out.println("CONTAINSROOMREQ: "+jsonObjectdeletereq.getAsJsonObject().get("success"));

    }

    // request for checking whether Polygon contains point selected by mouse click
    public static void containspointrequest(){
        PostRequest x = new PostRequest();

        String coordinatesMousePos = xpos/10 + "," + ypos/10;

        String Response =
                x.executePost(
                        "http://irt-ap.cs.columbia.edu/api/roomsByMouse",
                        "{\n"
                                + "\"location\": {"+"\n"
                                + "\"type\": "+"\"Point\""+",\n"
                                + "\"coordinates\": ["+coordinatesMousePos+"]\n}"
                                + "}", "POST");

        JsonObject jsonObjectcontainsreq = new JsonParser().parse(Response).getAsJsonObject();
        System.out.println("CONTAINSPOINTREQ: "+jsonObjectcontainsreq.getAsJsonObject().get("success"));

        JsonObject jsonarrayIDreq = jsonObjectcontainsreq.getAsJsonObject("data");
        String IDresponse = jsonarrayIDreq.getAsJsonObject().get("_id").toString();
        IDresponse = IDresponse.substring(1,IDresponse.length()-1);

        int IDcnt = 0;
        for(int i=0;i<PolygonIDArray.size();i++){
            if(PolygonIDArray.get(i).equals(IDresponse)){
                IDcnt = i;
               }
        }
        fillpolygon = polygons.get(IDcnt);
    }

    // request for getting all the buildings from the database
    public static void buildingrequest() throws IOException {

        GetRequest x = new GetRequest();
        //getting the buildings from the database
        String Response =
                x.sendGET(
                        "http://irt-ap.cs.columbia.edu/api/buildings",
                        "{"
                                + "}","GET");

        System.out.println("BUILDINGREQ: "+Response);
        JsonObject jsonObjectbuildingreq = new JsonParser().parse(Response).getAsJsonObject();
        JsonArray jsonarraybuildingreq = jsonObjectbuildingreq.getAsJsonArray("data");

        BuildingNameByUserStrArray = new String[jsonarraybuildingreq.size()];
        BuildingIDArray = new String[jsonarraybuildingreq.size()];

        for(int i=0;i<jsonarraybuildingreq.size();i++){
            String resultname = jsonarraybuildingreq.get(i).getAsJsonObject().get("name").toString();
            BuildingNameByUserStrArray[i] = resultname.substring(1,resultname.length()-1);
        }

        for(int j=0;j<jsonarraybuildingreq.size();j++){
            String resultid = jsonarraybuildingreq.get(j).getAsJsonObject().get("_id").toString();
            BuildingIDArray[j] = resultid.substring(1,resultid.length()-1);
        }
    }

    // request for getting all the floor of the selected building from the database
    public static void floorrequest() throws IOException {
        //getting the floors from the database
        PostRequest x = new PostRequest();
        String Response =
                x.executePost(
                        "http://irt-ap.cs.columbia.edu/api/floorsByBuilding",
                        "{\n"
                                + " \"building\" :"+"\""+BuildingID+"\"" +"\n"
                                + "}","POST");

        System.out.println("FLOORREQ: "+Response);
        JsonObject jsonObjectfloorreq = new JsonParser().parse(Response).getAsJsonObject();
        JsonArray jsonarrayfloorreq = jsonObjectfloorreq.getAsJsonArray("data");

        FloorNameByUserStrArray = new String[jsonarrayfloorreq.size()];
        FloorIDArray = new String[jsonarrayfloorreq.size()];

        for(int i=0;i<jsonarrayfloorreq.size();i++){
            String resultname = jsonarrayfloorreq.get(i).getAsJsonObject().get("name").toString();
            FloorNameByUserStrArray[i] = resultname.substring(1,resultname.length()-1);
        }

        for(int i=0;i<jsonarrayfloorreq.size();i++){
            String resultid = jsonarrayfloorreq.get(i).getAsJsonObject().get("_id").toString();
            FloorIDArray[i] = resultid.substring(1,resultid.length()-1);
        }
    }

    // request for deleting a polygon
    public static void deletepolygonrequest() throws IOException {
        // deleting polygons from the database
        GetRequest x = new GetRequest();
        String Response =
                x.sendGET(
                        "http://irt-ap.cs.columbia.edu/api/room/"+PolygonID,
                        "{"
                                + "}","DELETE");

        JsonObject jsonObjectdeletereq = new JsonParser().parse(Response).getAsJsonObject();

        System.out.println("DELETEREQ: "+jsonObjectdeletereq.getAsJsonObject().get("success"));
    }

    // handling the main frame
    protected static void initUI() {
        Drawing Drawinginst = new Drawing();

        frame = new JFrame("DrawPolygons");

        //DropDown Menu
        //selection of buildings
        String[] buildings = BuildingNameByUserStrArray; //array for buildings from database
        final JComboBox buildingSelectionbox = new JComboBox(buildings);
        buildingSelectionbox.setBounds(100, 830, 200, 40);
        buildingSelectionbox.setFont(new Font("Arial", Font.BOLD, 16));

        //selection of floors
        String[] floors = FloorNameByUserStrArray; //array for floors from database depending on building
        final JComboBox floorSelectionbox = new JComboBox(floors);
        floorSelectionbox.setBounds(350, 830, 200, 40);
        floorSelectionbox.setFont(new Font("Arial", Font.BOLD, 16));

        //when selecting a building getting the floorselection via http post request
        buildingSelectionbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object BuildingItembefore = buildingSelectionbox.getItemAt(buildingSelectionbox.getSelectedIndex());
                buildingSelectionbox.setSelectedItem(BuildingItembefore);

                try {
                    buildingrequest();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                //deleting the old dropdown list and adding the items from new selection of building
                buildingSelectionbox.removeAllItems();
                for (int i = 0; i < BuildingNameByUserStrArray.length; i++) {
                    buildingSelectionbox.addItem(BuildingNameByUserStrArray[i]);
                }

                buildingSelectionbox.setSelectedItem(BuildingItembefore);

                BuildingID = BuildingIDArray[buildingSelectionbox.getSelectedIndex()];
                BuildingNameByUserStr = BuildingNameByUserStrArray[buildingSelectionbox.getSelectedIndex()];

                floorSelectionbox.removeAllItems();

                //Drawinginst.getimage(FloorID);


                // new floorplanimage when changing building
                // deleting of the currentpolygon and the already drawn polygons as well as loading the
                // already drawn already drawn polygons of the new selected floor
            }
        });

        // advice fields
        JTextField advicefield1 = new JTextField();
        JTextField advicefield2 = new JTextField();

        Font adviceFont = new Font("Serif",Font.BOLD,20);
        advicefield1.setFont(adviceFont);
        advicefield2.setFont(adviceFont);

        String LeftClickT1 = "Left Mouse Click - setting Point of Polygon";
        String EnterT2 = "Enter - save Polygon";
        String BackspaceT1 = "Backspace - undo last Point";
        String RightClickT2 = "Right Mouse Click - clear current polygon";
        String DoubleClickT1 = "Double Left Click in Polygon - update mode and normal functions";
        String EscT2 = "Esc - back without saving (only in update mode)";
        String DeleteT1 = "Delete - delete Polygon (only in update mode)";
        String SpaceT2 = "Control - see wheter point is inside a polygon";

        String[] T1list = {LeftClickT1,BackspaceT1,DoubleClickT1,DeleteT1};
        String[] T2list = {EnterT2,RightClickT2,EscT2,SpaceT2};

        advicefield1.setText(LeftClickT1);
        advicefield2.setText(EnterT2);

        advicefield1.setEditable(false);
        advicefield2.setEditable(false);

        advicefield2.setBackground(Color.LIGHT_GRAY);
        advicefield2.setBounds(100,920,700,40);
        advicefield2.setVisible(true);

        advicefield1.setBackground(Color.LIGHT_GRAY);
        advicefield1.setBounds(100,880,700,40);
        advicefield1.setVisible(true);

        // advice button, shows next advice
        JButton nextAdviceBtn = new JButton("Next advice");
        nextAdviceBtn.setBounds(850,830, 200,40);
        nextAdviceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                advicefield1.setText(T1list[txtcnt%4]);
                advicefield2.setText(T2list[txtcnt%4]);
                txtcnt+=1;
            }
        });


        // help text with detailed explanation
        class NewFrame extends JFrame implements ActionListener {

            public NewFrame() {
                JTextPane helptext = new JTextPane();

                Font helptextFont = new Font("Serif",Font.BOLD,22);
                helptext.setFont(helptextFont);
                        helptext.setText(
                        "This is the help menu.\n\n"+
                        "You can create polygons here, which are directly stored in the data base.\n\n"+
                        "When clicking on the floorplan with the left mouse click, " +
                        "you create a point. A polygon must have at least 3 points.\n\n"+
                        "If you want to save a polygon, press the \"Enter\"-Key. \n\n"+
                        "When you want to undo your last point, press the \"Backspace\"-Key.\n\n"+
                        "While creating the polygon, you can click the right mouse button, " +
                        "which will delete your whole polygon before being saved. \n\n" +
                        "You can edit or delete a polygon any time when making a double click into it. \n\n" +
                        "Then you are in the update mode and you are able to undo the already drew lines by the \"Backspace\"-Key" +
                        "or delete it by pressing the \"Delete\"-Key.\n\n"+"If you accidentially got in the update mode, just press" +
                                "the \"Escape\"-Key to leave the update mode without saving anything.\n\n"+
                        "If you want to see, whether a point is inside a polygon, click on the floorplan and press the \"Control\"-Key.");
                helptext.setBackground(Color.LIGHT_GRAY);
                helptext.setEditable(false);
                add(helptext);
                setBounds(500,100,900,900);
                setVisible(true);
            }

            public void actionPerformed(ActionEvent event) {
                new NewFrame();
            }
        }

        // building label
        JLabel buildinglabel = new JLabel("Buildings");
        buildinglabel.setFont(FONT);
        buildinglabel.setBounds(160,795,200,40);
        buildinglabel.setVisible(true);

        // floor label
        JLabel floorlabel = new JLabel("Floors");
        floorlabel.setFont(FONT);
        floorlabel.setBounds(420,795,200,40);
        floorlabel.setVisible(true);

        // Help button to help text
        JButton Helpbtn = new JButton("Help");
        Helpbtn.setBounds(600,830,200,40);
        Helpbtn.setVisible(true);
        Helpbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewFrame();
            }
        });

        //when selecting a floor getting the floorplan via http post request
        floorSelectionbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object FloorItembefore = floorSelectionbox.getItemAt(floorSelectionbox.getSelectedIndex());
                floorSelectionbox.setSelectedItem(FloorItembefore);

                try {
                    floorrequest();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                //deleting the old dropdown list and adding the items from new selection of building
                floorSelectionbox.removeAllItems();
                for (int i = 0; i < FloorNameByUserStrArray.length; i++) {
                    floorSelectionbox.addItem(FloorNameByUserStrArray[i]);}


                floorSelectionbox.setSelectedItem(FloorItembefore);
            try {
                FloorID = FloorIDArray[floorSelectionbox.getSelectedIndex()];
                FloorNameByUserStr = FloorNameByUserStrArray[floorSelectionbox.getSelectedIndex()];
            } catch (Exception e1){
            }

            //    Drawing Drawinginstfloor = new Drawing();
            //    Drawinginstfloor.Newfloorimage();
                Newfloorimage();
                FrameRepaint();

                // deleting of the currentpolygon and the already drawn polygons as well as loading the
                // already drawn already drawn polygons of the new selected floor


/*                             try {
                    urlfloorplan = new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Universit%C3%A4tsbibliothek.jpg/375px-Universit%C3%A4tsbibliothek.jpg");
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
                frame.paintComponents(frame.getGraphics());
                System.out.println("WORKSSSSSSSSSSSSSSSSSSS"); */
            }
        });

        frame.add(buildinglabel);
        frame.add(floorlabel);
        frame.add(nextAdviceBtn);
        frame.add(advicefield2);
        frame.add(advicefield1);
        frame.add(Helpbtn);
        frame.add(buildingSelectionbox);
        frame.add(floorSelectionbox);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        InputMap im = Drawinginst.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        // enter handling
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "Enter");
        ActionMap ap = Drawinginst.getActionMap();
        ap.put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Drawinginst.currentPolygon.npoints > 2 && KeyAllowedSavingPolygon) {
                    System.out.println("Pressed Enter");
                    createWindow(); //new path, but don´t just open window and go to polygon finished
                }
            }
        });

        // backspace handling
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0, true), "Backspace");
        ap.put("Backspace", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Drawinginst.currentPolygon.npoints >= 1 && KeyAllowedSavingPolygon) {
                    System.out.println("Pressed Backspace");
                    int oldPtx = 0;
                    int oldPty = 0;
                    Drawinginst.clearCurrentPolygon();
                    if (Polyboolcnt > 1) {
                        Polyboolcnt = Polyboolcnt - 2;
                        for (int i = 0; i < Polyboolcnt; i++) {
                            oldPtx = IntPolyPoints.get(i);
                            i += 1;
                            oldPty = IntPolyPoints.get(i);
                            Drawinginst.currentPolygon.addPoint(oldPtx, oldPty);
                        }
                        IntPolyPoints.remove(IntPolyPoints.size() - 1);
                        IntPolyPoints.remove(IntPolyPoints.size() - 1);
      /*                  ellipsecnt -= 1;
                        dellipsecnt -= 1;
                        ellipse2DArrayList.remove(ellipse2DArrayList.size() - 1); */

                    } else {
                        Polyboolcnt = 0;
                        IntPolyPoints.clear();
    /*                    ellipsecnt = 0;
                        dellipsecnt = 0;
                        ellipse2DArrayList.remove(ellipse2DArrayList.size() - 1); */
                    }
                }
            }
        });

        // delete handling
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, true), "Delete");
        ap.put("Delete", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Pressed Delete");
                        if(updatebool){
                            PolygonID = PolygonIDArray.get(polygonIDcnt);
                            try {
                                deletepolygonrequest();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            clearCurrentPolygon();

                            PolygonnameArray.remove(polygonIDcnt);
                            PolygonIDArray.remove(polygonIDcnt);
                            polygons.remove(polygonIDcnt);

                            IntPolyPoints = new ArrayList<Integer>();
                            Polyboolcnt=0;
                            updatepolygon = new Polygon();
                            updatebool = false;
                        }
                    }
            }
        );

        // control handling
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, 0, true), "Control");
        ap.put("Control", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed Control");
                if (containsbool) {
                    try {
                        Drawinginst.PolygonContains(Drawinginst.getGraphics());
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    containsbool = false;
                }
            }
        });

        // escape handling
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "ESCAPE");
        ap.put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed Escape");
                if(updatebool){
                    updatebool = false;
                    clearCurrentPolygon();
                    IntPolyPoints = new ArrayList<Integer>();
                    Polyboolcnt = 0;
                }
            }
        });

        frame.add(Drawinginst);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // repaint function
    static void FrameRepaint() {
        frame.repaint();
    }

    // when a new floor is selected, the current polygon has to be deleted and the already
    // stored ones have to be drawn
    public static void Newfloorimage(){
        clearCurrentPolygon();
        IntPolyPoints = new ArrayList<Integer>();
        try {
            GetGivenPolygons();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // creating window after pressing enter to save the polygon
    private static void createWindow() {
        JFrame wframe = new JFrame("Name of room");
        createUI(wframe);
        wframe.setSize(560, 200);
        wframe.setLocationRelativeTo(null);
        wframe.setVisible(true);
    }

    // handling the different parts of the window
    private static void createUI(final JFrame wframe) {
        JPanel panel = new JPanel();
        LayoutManager layout = new FlowLayout();
        panel.setLayout(layout);

        // Save button
        final JButton SaveBtn = new JButton("Save this name");
        SaveBtn.setVisible(false);
        SaveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewPolyBool = false;
                KeyAllowedSavingPolygon = false;
                Fillpolygon = true;
         /*       ellipse2DArrayList.removeAll(ellipse2DArrayList);
                ellipsecnt = 0;
                dellipsecnt = 0; */

                try {
                    PolygonFinished();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                wframe.dispose();
            }
        });

        // Enter name button
        JButton button = new JButton("Please type in the name of the room.");
        final JLabel labeladvice = new JLabel("Last line will be drawn automatically after saving name.");
        final JLabel label = new JLabel();

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = (String) JOptionPane.showInputDialog(

                        wframe,
                        "Please type in the name of the room.",
                        "Name of room",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "Name of room"
                );
                if (result != null && result.length() > 0) {
                    label.setText("You selected: " + result);
                    RoomNameByUserStr = result;
                    SaveBtn.setVisible(true);
                } else {
                    label.setText("None selected");
                }
            }
        });
        panel.add(SaveBtn);
        panel.add(button);
        panel.add(label);
        panel.add(labeladvice);
        wframe.getContentPane().add(panel, BorderLayout.CENTER);
    }

    // main method
    public static void main(String[] args) throws IOException {


        //   urlfloorplan = new URL("https://www.study-in-bavaria.de/fileadmin/_migrated/pics/Geb33_a_02.jpg");

        //initial settings
        BuildingID = "5de68a3f756b75438fbec5f6"; //Schapiro Building

        FloorID = "5de68ab0756b75438fbec5f8"; //7th Floor
        //FloorID = "5de6c47ec2569658e7a0a132"; //8th Floor

        buildingrequest();
        floorrequest();

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                initUI();
            }
        });


    }
    }




    //TODO
/*
Show already written polygons                                       *[finished]*

keyboard enter and backspace                                        *[finished]*

drag and drop                                                       **********[PROGRESS, stuck]**********

undo function, delete current and paint new with 2 points less      *[finished]*

line to line not first point and last point connect to new point    *[finished]*

just one name in Array                                              *[finished]*

split Array into int Arrays for correct data                        *[finished]*

no buttons! let the "x" lead to continue                            *[finished]*

send array to server and delete local one                           *[finished]*

create json object for each room !!!!!                              *[finished]*

dropdown list with floor, building                                  *[finished]*

geocalculation through two ref points

 */

// different layout => buttons (less buttons)

// java application and website hosted separate cause adding floorplans on the java application
// might going to be uploaded through e.g. janitorial staff and adding IoT devices on the website
// by people working in the rooms like in the IRT-Lab





/*
node --experimental-modules main.js
 */

/*

*/