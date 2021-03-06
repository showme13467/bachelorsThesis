package Calc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import static Calc.DrawPolygons.Drawing.*;
import static java.lang.Math.*;


public class DrawPolygons {

    private static String RoomNameByUserStr = ""; // name of the current polygon the user created
    private static Boolean firstdrawing = true; // bool for checking whether the program is started for loading gui
    private static Boolean containsbool = false; // bool for checking whether a selected point is inside a polygon

    private static String[] FloorNameByUserStrArray; // contains all floor names
    private static String FloorNameByUserStr; // floor name selected in the Selectionbox
    private static String[] FloorIDArray; // contains all IDs of floors
    private static String FloorID; // current floor ID

    private static String[] BuildingNameByUserStrArray; // contains all building names
    private static String BuildingNameByUserStr; // building name selected in the Selectionbox
    private static String[] BuildingIDArray; // contains all IDs of buildings
    private static String BuildingID; // current building ID

    private static String PolygonID = ""; // current Polygon ID
    private static List<String> PolygonIDArray = new ArrayList<String>(); // contains all IDs of polygons
    private static int polygonIDcnt; // cnt for ID while removing ID when deleting a polygon
    private static ArrayList<String> PolygonnameArray = new ArrayList<String>(); // contains all names of polygons

    private static Boolean updatebool = false; // bool for checking, whether polygon is in updatemode
    private static Boolean updateboolM = false; // bool for checking, whether updatemode is already selected
    private static File urlfloorplan; // file where the image of the current floorplan is saved to

    private static int xpos = 0; // x position of mouse while checking whether a point selected by user is inside a polygon
    private static int ypos = 0; // y position of mouse while checking whether a point selected by user is inside a polygon

    private static Boolean NewPolyBool = true; // bool whether there is a new poylgon after saving one
    private static Boolean KeyAllowedSavingPolygon = false; // bool whether KeyBoardInput is Allowed
    private static Boolean labelpoints = false; // bool to check whether user wants points to be labeled or not

    private static double refgeoaX = 0; // refpoint geo coord a(x)
    private static double refgeoaY = 0; // refpoint geo coord a(y)
    private static double refgeobX = 0; // refpoint geo coord b(x)
    private static double refgeobY = 0; // refpoint geo coord b(y)

    private static double newgeoX = 0; // new point c(x)
    private static double newgeoY = 0; // new point c(y)

    private static float refpixelaX = 0; // refpoint pixel a(x)
    private static float refpixelaY = 0; // refpoint pixel a(y)
    private static float refpixelbX = 0; // refpoint pixel b(x)
    private static float refpixelbY = 0; // refpoint pixel b(y)

    private static String newname = ""; // String for giving polygon in update mode a new name
    private static int txtcnt = 1; // cnt for advices
    private static JFrame frame = new JFrame();
    private static List<Integer> IntPolyPoints = new ArrayList<Integer>(); // list of coord points of current polygon
    private static int Polyboolcnt = 0; // cnt for number of points of current polygon, x,y => 2 points
    private static final int Window_width = 1210; // width of window
    private static final int Window_height = 1200; // height of window

    public static class Drawing extends JPanel {

        static final Font FONT = new Font("Arial", Font.BOLD, 18); // basic font
        static List<Polygon> polygons = new ArrayList<Polygon>(); // list of all polygons
        static Polygon currentPolygon = new Polygon(); // polygon user is creating at the moment
        static Polygon updatepolygon = new Polygon(); // polygon which is changed in updatemode
        static Polygon fillpolygon = new Polygon(); // polygon which is filled in updatemode

        /*
        Method which is called when Polygon is finished and should to be created and drawn

        @param helpintx - gets x coord of point of beginning of polygon for drawing it completely
        @param helpinty - gets y coord of point of beginning of polygon for drawing it completely
         */
        static void PolygonFinished() throws IOException {
            NewPolyBool = true;
            int helpintx = currentPolygon.xpoints[0]; //add first point to array in database
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
                if (e.getY() < 800 && NewPolyBool && SwingUtilities.isLeftMouseButton(e)) {
                    if (e.getClickCount() == 2) {
                        for (int i = 0; i < polygons.size(); i++) {
                            if (polygons.get(i).contains(e.getX(), e.getY())) {

                                newname = PolygonnameArray.get(i);
                                updatebool = true;
                                updatepolygon = polygons.get(i);
                                polygonIDcnt = i;
                                clearCurrentPolygon();
                                Polyboolcnt = 0;
                                IntPolyPoints = new ArrayList<Integer>();
                                if (!updateboolM) {
                                    updateboolM = true;
                                }
                            }
                        }

                        //giving currentpolygon the points of the restored polygon
                        if (updateboolM) {
                            for (int j = 0; j < updatepolygon.npoints-1; j++) {
                                addPoint(updatepolygon.xpoints[j], updatepolygon.ypoints[j]);
                            }
                            updateboolM = false;
                        }
                    } else if (e.getClickCount() == 1) {
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

                    //loop for removing already inserted coord points of canceled polygon
                    for (int i = Polyboolcnt; i > 0; i--) {
                        IntPolyPoints.remove(i - 1);
                    }

                    Polyboolcnt = 0; //new polygon
                }
            }
        };


        Drawing() {
            this.addMouseListener(mouseListener);
        }

        public void addNotify() {
            super.addNotify();
            requestFocus();
        }

        //adding a point to the current poylgon
        void addPoint(int x, int y) {
            currentPolygon.addPoint(x, y); //adding the mouse-click point to the current polygon
            IntPolyPoints.add(x); //x-coord of new point of polygon
            Polyboolcnt += 1; //increasing the number of points by one
            IntPolyPoints.add(y); //y-coord of new point of polygon
            Polyboolcnt += 1; //increasing the number of points by one => x,y => 2 points
            FrameRepaint();
        }

        //canceling to draw polygon
        static void clearCurrentPolygon() {
            currentPolygon = new Polygon();
            containsbool = false;
            FrameRepaint();
        }

        //polygon must consist out of at least 3 points minimum
        static void createPolygon() throws IOException {
            if (currentPolygon.npoints > 2) {
                if (updatebool) {
                    updatepolygonrequest();
                    polygons.add(polygonIDcnt, currentPolygon);
                    PolygonnameArray.add(polygonIDcnt, RoomNameByUserStr);
                    IntPolyPoints = new ArrayList<Integer>();
                    Polyboolcnt = 0;
                    polygons.remove(polygonIDcnt + 1);
                    PolygonnameArray.remove(polygonIDcnt + 1);
                    GetGivenPolygons();
                } else {
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

        //saving all the images from the database
        protected static void loadimages(String name) {

            name = name + ".jpg";

            String filename = "http://irt-beagle.cs.columbia.edu/images/" + name;

            BufferedImage image = null;
            try {

                URL url = new URL(filename);
                image = ImageIO.read(url);

                BufferedImage blackwhiteImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

                for (int i = 0; i < image.getWidth(); i++) {
                    for (int j = 0; j < image.getHeight(); j++) {
                        int p = image.getRGB(i,j);
                        int r = (p>>16)&0xff;
                        int g = (p>>8)&0xff;
                        int b = p&0xff;

                        double rr = Math.pow(r / 255.0 , 2.2);
                        double gg = Math.pow(g / 255.0 , 2.2);
                        double bb = Math.pow(b / 255.0 , 2.2);
                        double lum = 0.2126 * rr + 0.7152 * gg + 0.0722 * bb;

                        int graylevel = (int) (255.0*Math.pow(lum,1.0 / 2.2));
                        int gray = (graylevel<<16) + (graylevel<<8) + graylevel;

                        blackwhiteImage.setRGB(i,j,gray);
                    }
                }
                File imagefile = new File(name);
                ImageIO.write(blackwhiteImage, "jpg", imagefile);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //getting the image of the current floor plan
        private static void getimage(String name) {
            name = name + ".jpg";
            urlfloorplan = new File(name);
        }

        //method runs all the time, handling of drawing and setting up the drawing area
        @Override
        protected void paintComponent(Graphics g) {
            getimage(FloorID);
            try {
                BufferedImage image = ImageIO.read(urlfloorplan);
                g.drawImage(image, 0, 0,1211,800, null);
            } catch (IOException e) {
                System.out.println("ERROR 404, FLOOR PLAN NOT FOUND");

                JFrame wframe = new JFrame("ERROR 404, FLOOR PLAN NOT FOUND");
                JButton CloseBtn = new JButton("OK");
                CloseBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        wframe.dispose();
                        frame.dispose();
                    }
                });
                JTextField Errormsg1 = new JTextField("Please upload the corresponding floor plan");
                JTextField Errormsg2 = new JTextField("to the selected floor to create polygons here!");

                Errormsg1.setEditable(false);
                Errormsg2.setEditable(false);
                CloseBtn.setVisible(true);
                Errormsg1.setVisible(true);
                Errormsg2.setVisible(true);
                CloseBtn.setFont(new Font("Serif",Font.BOLD,16));
                Errormsg1.setFont(new Font("Serif",Font.BOLD,18));
                Errormsg2.setFont(new Font("Serif",Font.BOLD,18));
                CloseBtn.setBounds(350,20,100,40);
                Errormsg1.setBounds(150,80,450,60);
                Errormsg2.setBounds(150,135,450,60);


                wframe.setDefaultCloseOperation(0);
                wframe.add(Errormsg2);
                wframe.add(Errormsg1);
                wframe.add(CloseBtn);
                wframe.setLayout(new BorderLayout());
                wframe.setSize(800, 300);
                wframe.setLocationRelativeTo(null);
                wframe.setVisible(true);
            }

            if (firstdrawing) {
                try {
                    GetGivenPolygons();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    for (int j = 0; j < FloorIDArray.length; j++) {
                        File deleteoldimagefile = new File(FloorIDArray[j]);
                        deleteoldimagefile.delete();
                    }
                } catch (Exception e1) {}

                try {
                    for (int i = 0; i < FloorIDArray.length; i++) {
                        loadimages(FloorIDArray[i]);
                    }
                } catch (Exception e1) {}
            }

            g.setFont(FONT);

            // placing the name of the polygon next to the polygon
            int lowestx; // lowest x coord of polygon
            int lowesty; // lowest y coord of polygon
            for (int i = 0; i < polygons.size(); i++) {
                g.setColor(Color.RED);
                drawPolygon(g, polygons.get(i));

                lowestx = 0;
                lowesty = 0;

                for (int k = 0; k < polygons.get(i).xpoints.length; k++) {
                    if (polygons.get(i).xpoints[k] != 0) {
                        lowestx = polygons.get(i).xpoints[k];
                    }
                    if (polygons.get(i).ypoints[k] != 0) {
                        lowesty = polygons.get(i).ypoints[k];
                    }
                }

                for (int j = 0; j < polygons.get(i).xpoints.length; j++) {
                    if (lowestx > polygons.get(i).xpoints[j] && polygons.get(i).xpoints[j] != 0) {
                        lowestx = polygons.get(i).xpoints[j];
                    }
                    if (lowesty > polygons.get(i).ypoints[j] && polygons.get(i).ypoints[j] != 0) {
                        lowesty = polygons.get(i).ypoints[j];
                    }
                }

                g.setColor(Color.blue);
                g.drawString(PolygonnameArray.get(i), lowestx + 40, lowesty + 40);
            }
            g.setColor(Color.GREEN);

            drawPolygon(g, currentPolygon);
        }

        //drawing current polygon and lines
        private void drawPolygon(Graphics g, Polygon polygon) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3)); //thickness of lines
            if (polygon.npoints < 3) {
                if (polygon.npoints == 1) {
                    g2.fillOval(polygon.xpoints[0] - 2, polygon.ypoints[0] - 2, 4, 4);
                    if(labelpoints) {drawNthPoint(g2, polygon, 0);}
                } else if (polygon.npoints == 2) {
                    g2.drawLine(polygon.xpoints[0], polygon.ypoints[0], polygon.xpoints[1], polygon.ypoints[1]);
                    if(labelpoints) {
                        drawNthPoint(g2, polygon, 0);
                        drawNthPoint(g2, polygon, 1);
                    }
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
                if(labelpoints){drawNthPoint(g2, polygon, i);}
            }
            g2.setColor(Color.GREEN);
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

        // convert pixel coords into geo coords
        public static void convertCoords(float refpixelcX, float refpixelcY) {
            double p;
            double q;
            double alpha;
            double lambda;
            double delta;

            lambda = Math.abs((refpixelaX - refpixelbX) / (refgeoaY - refgeobY));
            delta = Math.abs((refpixelaY - refpixelbY) / (refgeoaX - refgeobX));

            double help1 = lambda * (refgeoaY - refgeobY) * (refpixelaX - refpixelbX);
            double help2 = delta * (refgeoaX - refgeobX) * (refpixelaY - refpixelbY);
            double help3 = Math.pow((refpixelaX-refpixelbX),2);
            double help4 = Math.pow((refpixelaY-refpixelbY),2);

            alpha = asin(-1* ((help1 + help2) / (help3 + help4)) );

            p = ((refpixelaX * sin(alpha) + refpixelaY * cos(alpha)) / lambda) + refgeoaY;

            q = ((- refpixelaX * cos(alpha) + refpixelaY * sin(alpha)) / delta) + refgeoaX;

            newgeoY = ((- refpixelcX * sin(alpha) - refpixelcY * cos(alpha)) / lambda) + p;
            newgeoX = ((refpixelcX * cos(alpha) - refpixelcY * sin(alpha)) / delta) + q;
        }
    }

    // when Polygon contains point selected by mouse click, polygon is filled out
    private static void PolygonContains(Graphics g) throws InterruptedException {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GREEN);
        containspointrequest();
        g2.fill(fillpolygon);
        g2.setColor(Color.GREEN);
        fillpolygon = new Polygon();
    }

    // setting up the already in the database stored polygons
    private static void GetGivenPolygons() throws IOException {
        polygons = new ArrayList<>();
        PolygonnameArray = new ArrayList<>();
        PolygonIDArray = new ArrayList<>();

        PostRequest x = new PostRequest();

        //drawing the polygons which are already in the database
        String Response =
                x.executePost(
                        "http://irt-beagle.cs.columbia.edu/api/roomsByFloor",
                        "{\n"
                                + " \"floor\" :" + "\"" + FloorID + "\"" + "\n"
                                + "}", "POST");

        if(Response.equals("false")) {
            System.out.println("No Polygons");
        } else {
            JsonObject jsonObjectgivenpolygonsreq = new JsonParser().parse(Response).getAsJsonObject();
            System.out.println("GETGIVENPOLYGONSREQ: " + jsonObjectgivenpolygonsreq.getAsJsonObject().get("success"));
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
                for (int h = 0; h < pixeljsonarray.size(); h++) {
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
        }
        firstdrawing = false;
    }

    // request for updating a polygon
    private static void updatepolygonrequest() throws IOException {
        PostRequest x = new PostRequest();

        String pixelcoordsOfPolygon = "";
        for (int i = 0; i < currentPolygon.npoints; i++) {
            pixelcoordsOfPolygon += currentPolygon.xpoints[i] + ", " + currentPolygon.ypoints[i] + ",";
        }
        pixelcoordsOfPolygon = pixelcoordsOfPolygon.substring(0, pixelcoordsOfPolygon.length() - 1);

        String geocoordsOfPolygon = "";
        for (int i = 0; i < currentPolygon.npoints; i++) {
            convertCoords(currentPolygon.xpoints[i], currentPolygon.ypoints[i]);
            geocoordsOfPolygon += "[" + newgeoX + "," + newgeoY + "],";
        }
        geocoordsOfPolygon = geocoordsOfPolygon.substring(0, geocoordsOfPolygon.length() - 1);

        PolygonID = PolygonIDArray.get(polygonIDcnt);

        String Response =
                x.executePost(
                        "http://irt-beagle.cs.columbia.edu/api/room/" + PolygonID,
                        "{\n"
                                + "\"geometry\": {" + "\n"
                                + "\"type\": " + "\"Polygon\"" + ",\n"
                                + "\"coordinates\":" + "[[" + geocoordsOfPolygon + "]]" + "\n},"
                                + "\"pixel\":" + "[" + pixelcoordsOfPolygon + "]" + ",\n"
                                + "\"name\" :" + "\"" + RoomNameByUserStr + "\"" + "\n,"
                                + "\"floor\" :" + "\"" + FloorID + "\"" + "\n"
                                + "}", "PUT");

        JsonObject jsonObjectdeletereq = new JsonParser().parse(Response).getAsJsonObject();
        System.out.println("UPDATEPOLYGONREQ: " + jsonObjectdeletereq.getAsJsonObject().get("success"));
    }

    // request for sending the current polygon to the database
    private static void sendPolygon() {
        String pixelcoordsOfPolygon = "";
        for (int i = 0; i < currentPolygon.npoints; i++) {
            pixelcoordsOfPolygon += currentPolygon.xpoints[i] + "," + currentPolygon.ypoints[i] + ",";
        }
        pixelcoordsOfPolygon = pixelcoordsOfPolygon.substring(0, pixelcoordsOfPolygon.length() - 1);

        String geocoordsOfPolygon = "";
        for (int i = 0; i < currentPolygon.npoints; i++) {
            convertCoords(currentPolygon.xpoints[i], currentPolygon.ypoints[i]);
            geocoordsOfPolygon += "[" + newgeoX + "," + newgeoY + "],";
        }
        geocoordsOfPolygon = geocoordsOfPolygon.substring(0, geocoordsOfPolygon.length() - 1);


        PostRequest x = new PostRequest();

        //getting the buildings from the database
        String Response =
                x.executePost(
                        "http://irt-beagle.cs.columbia.edu/api/room",
                        "{\n"
                                + "\"geometry\": {" + "\n"
                                + "\"type\": " + "\"Polygon\"" + ",\n"
                                + "\"coordinates\":" + "[[" + geocoordsOfPolygon + "]]" + "\n},"
                                + "\"pixel\":" + "[" + pixelcoordsOfPolygon + "]" + "\n,"
                                + "\"name\" :" + "\"" + RoomNameByUserStr + "\"" + "\n,"
                                + "\"floor\" :" + "\"" + FloorID + "\"" + "\n"
                                + "}", "POST");

        JsonObject jsonObjectsendpolygonreq = new JsonParser().parse(Response).getAsJsonObject();
        System.out.println("DELETEREQ: " + jsonObjectsendpolygonreq.getAsJsonObject().get("success"));

        PolygonID = jsonObjectsendpolygonreq.getAsJsonObject().get("id").toString();
        PolygonIDArray.add(PolygonID.substring(1, PolygonID.length() - 1));
    }

    // request for checking whether Polygon contains point selected by mouse click
    private static void containspointrequest() {
        PostRequest x = new PostRequest();
        convertCoords(xpos,ypos);
        String coordinatesMousePos = newgeoX+","+newgeoY;

        String Response =
                x.executePost(
                        "http://irt-beagle.cs.columbia.edu/api/roomsByMouse",
                        "{\n"
                                + "\"location\": {" + "\n"
                                + "\"type\": " + "\"Point\"" + ",\n"
                                + "\"coordinates\": [" + coordinatesMousePos + "]\n}"
                                + "}", "POST");

        JsonObject jsonObjectcontainsreq = new JsonParser().parse(Response).getAsJsonObject();
        System.out.println("CONTAINSPOINTREQ: " + jsonObjectcontainsreq.getAsJsonObject().get("success"));

        JsonObject jsonarrayIDreq = jsonObjectcontainsreq.getAsJsonObject("data");
        String IDresponse = jsonarrayIDreq.getAsJsonObject().get("_id").toString();
        IDresponse = IDresponse.substring(1, IDresponse.length() - 1);

        int IDcnt = 0;
        for (int i = 0; i < PolygonIDArray.size(); i++) {
            if (PolygonIDArray.get(i).equals(IDresponse)) {
                IDcnt = i;
            }
        }
        fillpolygon = polygons.get(IDcnt);
    }

    // request for getting all the buildings from the database
    protected static void buildingrequest() throws IOException {
        GetRequest x = new GetRequest();
        //getting the buildings from the database
        String Response =
                x.sendGET(
                        "http://irt-beagle.cs.columbia.edu/api/buildings",
                        "{"
                                + "}", "GET");

        JsonObject jsonObjectbuildingreq = new JsonParser().parse(Response).getAsJsonObject();
        System.out.println("BUILDINGREQ: " + jsonObjectbuildingreq.getAsJsonObject().get("success"));
        JsonArray jsonarraybuildingreq = jsonObjectbuildingreq.getAsJsonArray("data");

        BuildingNameByUserStrArray = new String[jsonarraybuildingreq.size()];
        BuildingIDArray = new String[jsonarraybuildingreq.size()];

        for (int i = 0; i < jsonarraybuildingreq.size(); i++) {
            String resultname = jsonarraybuildingreq.get(i).getAsJsonObject().get("name").toString();
            BuildingNameByUserStrArray[i] = resultname.substring(1, resultname.length() - 1);
        }

        for (int j = 0; j < jsonarraybuildingreq.size(); j++) {
            String resultid = jsonarraybuildingreq.get(j).getAsJsonObject().get("_id").toString();
            BuildingIDArray[j] = resultid.substring(1, resultid.length() - 1);
        }
    }

    // request for getting all the floor of the selected building from the database
    protected static void floorrequest() throws IOException {
        //getting the floors from the database
        PostRequest x = new PostRequest();
        String Response =
                x.executePost(
                        "http://irt-beagle.cs.columbia.edu/api/floorsByBuilding",
                        "{\n"
                                + " \"building\" :" + "\"" + BuildingID + "\"" + "\n"
                                + "}", "POST");

        JsonObject jsonObjectfloorreq = new JsonParser().parse(Response).getAsJsonObject();
        JsonArray jsonarrayfloorreq = jsonObjectfloorreq.getAsJsonArray("data");

        System.out.println("FLOORREQ: " + jsonObjectfloorreq.getAsJsonObject().get("success"));

        FloorNameByUserStrArray = new String[jsonarrayfloorreq.size()];
        FloorIDArray = new String[jsonarrayfloorreq.size()];
        polygons = new ArrayList<>(jsonarrayfloorreq.size());

        for (int i = 0; i < jsonarrayfloorreq.size(); i++) {
            String resultname = jsonarrayfloorreq.get(i).getAsJsonObject().get("name").toString();
            FloorNameByUserStrArray[i] = resultname.substring(1, resultname.length() - 1);
        }

        for (int i = 0; i < jsonarrayfloorreq.size(); i++) {
            String resultid = jsonarrayfloorreq.get(i).getAsJsonObject().get("_id").toString();
            FloorIDArray[i] = resultid.substring(1, resultid.length() - 1);
        }

        int[] refpixel = new int[]{};
        double[] refgeo = new double[]{};
        int cnt = 0;
        for (int y = 0; y < FloorIDArray.length; y++) {
            if (FloorIDArray[y] == FloorID) {
                cnt = y;
            }
        }

        JsonObject jsonfloorobj = jsonarrayfloorreq.get(cnt).getAsJsonObject();
        JsonObject jsonrefobj = jsonfloorobj.get("refpoints").getAsJsonObject();
        JsonArray jsonpixelarray = jsonrefobj.get("pixel").getAsJsonArray();
        JsonArray jsongeoarray = jsonrefobj.get("geocoord").getAsJsonArray();

        refpixel = new int[jsonpixelarray.size()];
        for (int h = 0; h < jsonpixelarray.size(); h++) {
            refpixel[h] = jsonpixelarray.get(h).getAsInt();
        }

        refgeo = new double[jsongeoarray.size()];
        for (int h = 0; h < jsongeoarray.size(); h++) {
            JsonElement geoelem = jsongeoarray.get(h);
            String help = geoelem.getAsJsonObject().get("$numberDecimal").toString();
            help = help.substring(1, help.length() - 1);
            refgeo[h] = Double.parseDouble(help);
        }

        //determining which coords belong to which points
if(refpixel.length != 0) {
    if (refpixel[0] < refpixel[2]) {
        refpixelaX = refpixel[0];
        refpixelaY = refpixel[1];
        refpixelbX = refpixel[2];
        refpixelbY = refpixel[3];
        refgeoaX = refgeo[0];
        refgeoaY = refgeo[1];
        refgeobX = refgeo[2];
        refgeobY = refgeo[3];
    } else {
        refpixelaX = refpixel[2];
        refpixelaY = refpixel[3];
        refpixelbX = refpixel[0];
        refpixelbY = refpixel[1];
        refgeoaX = refgeo[2];
        refgeoaY = refgeo[3];
        refgeobX = refgeo[0];
        refgeobY = refgeo[1];
    }
}
        try {
            File deleteoldimagefile = new File(FloorID);
            deleteoldimagefile.delete();
        } catch (Exception e1) {}
        loadimages(FloorID);
    }

    // request for deleting a polygon from the database
    protected static void deletepolygonrequest() throws IOException {
        GetRequest x = new GetRequest();
        String Response =
                x.sendGET(
                        "http://irt-beagle.cs.columbia.edu/api/room/" + PolygonID,
                        "{"
                                + "}", "DELETE");

        JsonObject jsonObjectdeletereq = new JsonParser().parse(Response).getAsJsonObject();

        System.out.println("DELETEREQ: " + jsonObjectdeletereq.getAsJsonObject().get("success"));
    }

    // handling the main frame
    protected static void initUI() {
        Drawing Drawinginst = new Drawing();

        frame = new JFrame("DrawPolygons");

        //DropDown Menus
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
        advicefield2.setBounds(100,920,1050,40);
        advicefield2.setVisible(true);

        advicefield1.setBackground(Color.LIGHT_GRAY);
        advicefield1.setBounds(100,880,1050,40);
        advicefield1.setVisible(true);

        // advice button, shows next advice
        JButton nextAdviceBtn = new JButton("Next advice");
        nextAdviceBtn.setBounds(810,830, 140,40);
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
                        "Then you are in the update mode and you are able to undo the already drew lines by the \"Backspace\"-Key " +
                        "or delete it by pressing the \"Delete\"-Key.\n\n"+"If you accidentially got in the update mode, just press " +
                                "the \"Escape\"-Key to leave the update mode without saving anything.\n\n"+
                        "If you want to see, whether a point is inside a polygon, click on the floorplan and press the \"Control\"-Key.\n\n" +
                                "When you want to get short advices, press the \"Advice\"-Button.\n\n" +
                                "You can label the points by clicking on the \"Label Points\"-Button.");
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
        Helpbtn.setBounds(610,830,140,40);
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
            } catch (Exception e1){}
                Newfloorimage();
                FrameRepaint();
            }
        });

        // Labeling of the points button
        JButton labelpointsbtn = new JButton("Label Points");
        labelpointsbtn.setBounds(1010,830,140,40);
        labelpointsbtn.setVisible(true);
        labelpointsbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(labelpoints){labelpoints=false;}
                else{labelpoints=true;}
                Drawinginst.paintComponent(Drawinginst.getGraphics());
            }
        });


        frame.add(labelpointsbtn);
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
                    } else {
                        Polyboolcnt = 0;
                        IntPolyPoints.clear();
                    }
                }
            }
        });

        // delete handling
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, true), "Delete");
        ap.put("Delete", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
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
                if (containsbool) {
                    try {
                        PolygonContains(Drawinginst.getGraphics());
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
    private static void FrameRepaint() {
        frame.repaint();
    }

    // when a new floor is selected, the current polygon has to be deleted and the already
    // stored ones have to be drawn
    private static void Newfloorimage(){
        clearCurrentPolygon();
        IntPolyPoints = new ArrayList<Integer>();
        Polyboolcnt = 0;
        loadimages(FloorID);
        Drawing.getimage(FloorID);
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
        String initialname = "Name of room";
        if(updatebool){
            initialname = newname;
        }
        String finalInitialname = initialname;
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
                        finalInitialname
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
        //initial settings
        BuildingID = "5de68a3f756b75438fbec5f6"; //Schapiro Building

        FloorID = "5de68ab0756b75438fbec5f8"; //7th Floor

        buildingrequest();
        floorrequest();

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {initUI();}});

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            //method for deleting every image after closing the application
            @Override
            public void run()
            {
                try{
                    GetRequest x = new GetRequest();
                        String Response =
                                x.sendGET(
                                        "http://irt-beagle.cs.columbia.edu/api/floors",
                                        "{"
                                                + "}", "GET");

                    JsonObject jsonObjectdeleteallreq = new JsonParser().parse(Response).getAsJsonObject();
                    System.out.println("ALLFLOORDELETEREQ: " + jsonObjectdeleteallreq.getAsJsonObject().get("success"));

                        JsonObject jsonObjectfloorreq = new JsonParser().parse(Response).getAsJsonObject();
                        JsonArray jsonarrayfloorreq = jsonObjectfloorreq.getAsJsonArray("data");

                        FloorIDArray = new String[jsonarrayfloorreq.size()];

                        for (int i = 0; i < jsonarrayfloorreq.size(); i++) {
                            String resultid = jsonarrayfloorreq.get(i).getAsJsonObject().get("_id").toString();
                            FloorIDArray[i] = resultid.substring(1, resultid.length() - 1);
                        }

                        for (int i = 0; i < FloorIDArray.length; i++) {
                            String deleteimage = FloorIDArray[i] + ".jpg";
                            File deleteimagefile = new File(deleteimage);
                            deleteimagefile.delete();

                    }
                } catch (Exception e1){}
            }
        });

    }
}