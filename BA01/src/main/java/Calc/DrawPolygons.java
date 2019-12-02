package Calc;

import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;

import static Calc.DrawPolygons.Drawing.*;


public class DrawPolygons {

    public static String RoomNameByUserStr;
    public static String[] RoomNameByUserStrArray;
    public static String[] FloorNameByUserStrArray;
    public static String FloorSelectionByUserStr;
    public static String[] BuildingByUserStrArray;
    public static String BuildingByUserStr;
    public static URL urlfloorplan;

    public static Boolean NewPolyBool = true;
    public static Boolean KeyAllowedSavingPolygon = false;
    public static Boolean Fillpolygon = false;

    public static Boolean ellipsenbool = false;
    public static int ellipsecnt = 0;
    public static int dellipsecnt = 0;
    public static Boolean dellipsebool = false;

    public static Scanner scanner = new Scanner(System.in);

    public static JFrame frame = new JFrame();

    static List<Integer> IntPolyPoints = new ArrayList<Integer>();//list of coord points of current polygon

    public static int Polyboolcnt = 0; //cnt for whether a polygon is drew completely or canceled


    private static final int Window_width = 1210;
    private static final int Window_height = 1200;

    public static class Drawing extends JPanel {

        static Ellipse2D dellipse = new Ellipse2D.Double();
        static Ellipse2D ellipse = new Ellipse2D.Double();
        static ArrayList<Ellipse2D> ellipse2DArrayList = new ArrayList<>();

        private static final Font FONT = new Font("Arial", Font.BOLD, 18);

        private static List<Polygon> polygons = new ArrayList<Polygon>();

        protected static Polygon currentPolygon = new Polygon();


        public static void PolygonFinished() throws IOException {
            NewPolyBool = true;
            int helpintx = currentPolygon.xpoints[0];
            int helpinty = currentPolygon.ypoints[0];
            currentPolygon.addPoint(helpintx, helpinty);
            IntPolyPoints.add(helpintx);
            IntPolyPoints.add(helpinty);
            createPolygon();
        }

        private MouseAdapter mouseListener = new MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                //LeftMouseButton handling => draw
                if (e.getY() < 800 && NewPolyBool == true && SwingUtilities.isLeftMouseButton(e)) {
                    if (e.getClickCount() == 1) {
                        addPoint(e.getX(), e.getY());
                        KeyAllowedSavingPolygon = true;
                    }

                    //RightMouseButton handling => cancel drawing
                } else if (NewPolyBool == true && SwingUtilities.isRightMouseButton(e)) {
                    clearCurrentPolygon();
                    System.out.println("Polygon cleared!");
                    ellipse2DArrayList.removeAll(ellipse2DArrayList);
                    ellipse = null;
                    dellipse = null;
                    ellipsecnt = 0;
                    dellipsecnt = 0;

                    //loop for removing already inserted coord points of canceled polygon
                    for (int i = Polyboolcnt; i > 0; i--) {
                        IntPolyPoints.remove(i - 1);
                    }

                    Polyboolcnt = 0; //new polygon
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Mouse Pressed");
                FrameRepaint();
            }
        };


       /*         private MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
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
            //  this.addMouseMotionListener(mouseMotionListener);
        }

        public void addNotify() {
            super.addNotify();
            requestFocus();
        }

        protected void addPoint(int x, int y) {
            // ellipsenbool = true;

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
            FrameRepaint();
        }

        //polygon must consist out of at least 3 points minimum
        public static void createPolygon() throws IOException { //protected
            if (currentPolygon.npoints > 2) {
                polygons.add(currentPolygon);
                System.out.println("Polygon finished! \n \n");
                Polyboolcnt = 0; //polygon finished, ready for new one
                sendPolygon();
                IntPolyPoints = new ArrayList<>();
            }
            clearCurrentPolygon();
            FrameRepaint();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(Window_width, Window_height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            // super.paintComponent(g);

            //setting the floorplan as the background

             /*   try {
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

            g.setColor(Color.RED);
            g.setFont(FONT);


            for (Polygon polygon : polygons) {
                drawPolygon(g, polygon);
            }
            g.setColor(Color.GREEN);

            int[] testPPP = new int[]{138, 449, 284, 450, 284, 585, 136, 584, 138, 449};
            //  drawGivenPolygons(g);

            drawPolygon(g, currentPolygon);
        }

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

                for (int i = 0; i < polygon.npoints - 1; i++)
                    g2.drawLine(x[i], y[i], x[i + 1], y[i + 1]);
            }

            //calling the function for the letters
            for (int i = 0; i < polygon.npoints; i++) {
                drawNthPoint(g2, polygon, i);
            }

            for (int i = 0; i < polygons.size(); i++) {
                g2.setColor(Color.RED);
                g2.fill(polygons.get(i));
            }
            g2.setColor(Color.GREEN);


            //when point added to current polygon, calling function to set up ellipse
       /*         if(ellipsenbool) {
                    setDellipse(g2, currentPolygon, ellipsecnt);
                }

                //drawing all ellipses
                for(int i=0;i<ellipsecnt;i++){
                    g2.draw(ellipse2DArrayList.get(i));
                }
                ellipsenbool = false; */
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

       /*     private static void setDellipse(Graphics g, Polygon polygon, int nth){
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

        public static void drawGivenPolygons(Graphics g) { //, int[] points) {

            JsonObject AlreadyGivenPolygonsRequestJsonobj = new JsonObject();
            AlreadyGivenPolygonsRequestJsonobj.addProperty("floor", FloorSelectionByUserStr);

            String FloorSelectionByUserStrCopy = "\"" + FloorSelectionByUserStr + "\"";

            String payloadbuilding = "{" +
                    "\"floor\": " + FloorSelectionByUserStrCopy +
                    "}";
            StringEntity entityfloor = new StringEntity(payloadbuilding,
                    ContentType.APPLICATION_JSON);

            StringEntity ttt = new StringEntity(AlreadyGivenPolygonsRequestJsonobj.toString(),
                    ContentType.APPLICATION_JSON);

            HttpClient httpClientroom = HttpClientBuilder.create().build();
            HttpPost requestroom = new HttpPost("http://irt-ap.cs.columbia.edu/api/get_rooms/");
            requestroom.setEntity(entityfloor);

            HttpResponse responseroom = null;
            try {
                responseroom = httpClientroom.execute(requestroom);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            String roomhttpanswer = null;
            try {
                roomhttpanswer = EntityUtils.toString(responseroom.getEntity());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println(roomhttpanswer);


            char letter = '?';
            String str = roomhttpanswer.toLowerCase();
            letter = Character.toLowerCase(letter);
            int roomarraylength = 0;

            for (int i = 0; i < str.length(); i++) {
                char currentLetter = str.charAt(i);
                if (currentLetter == letter)
                    roomarraylength++;
            }


            int[] points = new int[]{};


            for (int k = 0; k < roomarraylength; k++) {

                System.out.println("point array: " + points[k]);

                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3)); //thickness of lines
                g2.setColor(Color.RED);

                int n = points.length;
                int[] x = new int[n / 2];
                int[] y = new int[n / 2];
                int xcnt = 0;
                int ycnt = 0;
                for (int i = 0; i < n; i++) {
                    if (i % 2 == 0) {
                        x[xcnt] = points[i];
                        xcnt += 1;
                    } else {
                        y[ycnt] = points[i];
                        ycnt += 1;
                    }
                }

                Polygon poly = new Polygon(x, y, n / 2);
                g2.drawPolygon(poly);
                g2.setColor(Color.green);
            }
        }


    public static boolean isNumeric(String value) {
        try {
            int number = Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }



        public static void sendPolygon() throws IOException {

                String coordinatesOfPolygon = "";

                for(int i=0;i<currentPolygon.npoints;i++){
                    coordinatesOfPolygon +="["+currentPolygon.xpoints[i]+", "+currentPolygon.ypoints[i]+"],";
                }
                coordinatesOfPolygon = coordinatesOfPolygon.substring(0,coordinatesOfPolygon.length()-1);

                RoomNameByUserStr = "\""+RoomNameByUserStr+"\"";

                //setting up a room through sending a finished polygon to the database
                String payload = "{" +
                        "\"coordinates\": [[" +
                        coordinatesOfPolygon +
                        "]]"+ ","+
                        "\"name\": "+ RoomNameByUserStr+ ", " +
                        "\"floor\": "+ FloorSelectionByUserStr+ ", "+
                        "\"building\": "+ BuildingByUserStr+
                        "}";
                StringEntity entity = new StringEntity(payload,
                        ContentType.APPLICATION_JSON);

                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost request = new HttpPost("http://irt-ap.cs.columbia.edu/api/create_room/");
                request.setEntity(entity);

                HttpResponse response = httpClient.execute(request);
                System.out.println(response.getStatusLine().getStatusCode());
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
    }


    protected static void initUI() {
         frame = new JFrame("DrawPolygons");

            //DropDown Menu
                //selection of buildings
                String[] buildings = BuildingByUserStrArray; //array for buildings from database
                final JComboBox buildingSelectionbox = new JComboBox(buildings);
                buildingSelectionbox.setBounds(100, 830, 200, 40);
                buildingSelectionbox.setFont(new Font("Arial",Font.BOLD,16));

                //selection of floors
                String[] floors = FloorNameByUserStrArray; //array for floors from database depending on building
                final JComboBox floorSelectionbox = new JComboBox(floors);
                floorSelectionbox.setBounds(350,830, 200, 40);
                floorSelectionbox.setFont(new Font("Arial",Font.BOLD,16));


                floorSelectionbox.setVisible(false); //set floorSelection just visible when building is already chosen

        //when selecting a building getting the floorselection via http post request
                buildingSelectionbox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        BuildingByUserStr = "\""+buildingSelectionbox.getItemAt(buildingSelectionbox.getSelectedIndex())+"\"";

                        //getting the floors from the database
                        String payloadbuilding = "{"+
                                "\"building\": "+BuildingByUserStr+
                                "}";
                        StringEntity entityfloor = new StringEntity(payloadbuilding,
                                ContentType.APPLICATION_JSON);

                        HttpClient httpClientfloor = HttpClientBuilder.create().build();
                        HttpPost requestfloor = new HttpPost("http://irt-ap.cs.columbia.edu/api/get_floors/");
                        requestfloor.setEntity(entityfloor);

                        HttpResponse responsefloor = null;
                        try {
                            responsefloor = httpClientfloor.execute(requestfloor);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        String floorhttpanswer = null;
                        try {
                            floorhttpanswer = EntityUtils.toString(responsefloor.getEntity());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println(floorhttpanswer);

                        //splitting the String to get only the floor names shown in the dropdown list
                        int j=0;
                        String[] arrOfStrfloors = floorhttpanswer.split("[{+:]",0);
                        j=0;
                        int floorarraylength = arrOfStrfloors.length-1;
                        FloorNameByUserStrArray = new String[floorarraylength/2];
                        String[] CopyfloorStr = new String[floorarraylength];
                        for(int i=1;i<floorarraylength;i++){
                            arrOfStrfloors[i] = arrOfStrfloors[i].substring(1,arrOfStrfloors[i].length()-1);
                            CopyfloorStr[j] = arrOfStrfloors[i];
                            j+=1;
                            i=i+1;
                        }

                        for(int k=0;k<floorarraylength/2;k++) {
                            FloorNameByUserStrArray[k] = CopyfloorStr[k];
                        }

                        //deleting the old dropdown list and adding the items from new selection of building
                        floorSelectionbox.removeAllItems();
                        for(int i=0;i<FloorNameByUserStrArray.length;i++) {
                            floorSelectionbox.addItem(FloorNameByUserStrArray[i]);
                        }

                        floorSelectionbox.setVisible(true);
                    }
                });



        //when selecting a floor getting the floorplan via http post request
        floorSelectionbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 FloorSelectionByUserStr ="\""+floorSelectionbox.getItemAt(floorSelectionbox.getSelectedIndex())+"\"";

                      /*  String payloadbuilding = "{"+
                                "\"building\": "+BuildingByUserStr+
                                "\"floor\": "+FloorSelectionByUserStr+
                                "}";
                        StringEntity entityfloor = new StringEntity(payloadbuilding,
                                ContentType.APPLICATION_JSON);

                        HttpClient httpClientfloor = HttpClientBuilder.create().build();
                        HttpPost requestfloor = new HttpPost("http://irt-ap.cs.columbia.edu/api/get_floorplan/");
                        requestfloor.setEntity(entityfloor);

                        HttpResponse responsefloor = null;
                        try {
                            responsefloor = httpClientfloor.execute(requestfloor);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        String floorhttpanswer = null;
                        try {
                            floorhttpanswer = EntityUtils.toString(responsefloor.getEntity());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println(floorhttpanswer);

                        //showing floorplan
                        frame.update(frame.getGraphics());
                    */

/*                             try {
                    urlfloorplan = new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Universit%C3%A4tsbibliothek.jpg/375px-Universit%C3%A4tsbibliothek.jpg");
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
                frame.paintComponents(frame.getGraphics());
                System.out.println("WORKSSSSSSSSSSSSSSSSSSS"); */
            }
        });

        frame.add(buildingSelectionbox);
        frame.add(floorSelectionbox);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Drawing Drawinginst = new Drawing();
        InputMap im= Drawinginst.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        //enter handling
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true),"Enter");
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

        //backspace handling
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,0,true),"Backspace");
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
             /*           ellipsecnt -=1;
                        dellipsecnt -=1;
                        ellipse2DArrayList.remove(ellipse2DArrayList.size()-1); */

                    } /*else {
                        Polyboolcnt = 0;
                        IntPolyPoints.clear(); */
          /*              ellipsecnt = 0;
                        dellipsecnt =0;
                        ellipse2DArrayList.remove(ellipse2DArrayList.size()-1); */
                    }
                }
        });


        frame.add(Drawinginst);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void FrameRepaint(){
        frame.repaint();
    }

    private static void createWindow() {
        JFrame wframe = new JFrame("Name of room");
        createUI(wframe);
        wframe.setSize(560, 200);
        wframe.setLocationRelativeTo(null);
        wframe.setVisible(true);
    }

    private static void createUI(final JFrame wframe) {
        JPanel panel = new JPanel();
        LayoutManager layout = new FlowLayout();
        panel.setLayout(layout);

        final JButton CloseBtnW = new JButton("Save this name");
        CloseBtnW.setVisible(false);
        CloseBtnW.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Int list: "+IntPolyPoints);
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

        JButton button = new JButton("Please type in the name of the room.");
        final JLabel label = new JLabel();

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = (String)JOptionPane.showInputDialog(

                        wframe,
                        "Please type in the name of the room.",
                        "Name of room",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "Name of room"
                );
                if(result != null && result.length() > 0){
                    label.setText("You selected: " + result);
                    RoomNameByUserStr = result;
                    CloseBtnW.setVisible(true);
                }else {
                    label.setText("None selected");
                }
            }
        });

        panel.add(CloseBtnW);
        panel.add(button);
        panel.add(label);
        wframe.getContentPane().add(panel, BorderLayout.CENTER);
    }


    public static void main(String[] args) throws IOException {

     //   urlfloorplan = new URL("https://www.study-in-bavaria.de/fileadmin/_migrated/pics/Geb33_a_02.jpg");

        //getting the buildings from the database
        String payloadempty = "{"+
                "}";
        StringEntity entitybuilding = new StringEntity(payloadempty,
                ContentType.APPLICATION_JSON);

        HttpClient httpClientbuilding = HttpClientBuilder.create().build();
        HttpPost requestbuilding = new HttpPost("http://irt-ap.cs.columbia.edu/api/get_buildings/");
        requestbuilding.setEntity(entitybuilding);

        HttpResponse responsebuilding = httpClientbuilding.execute(requestbuilding);
        String buildinghttpanswer = EntityUtils.toString(responsebuilding.getEntity());
        System.out.println(buildinghttpanswer);

        String[] arrOfStrbuilding = buildinghttpanswer.split("[?+?]",0);
        int j=0;

        char letter = '?';
        String str = buildinghttpanswer.toLowerCase();
        letter = Character.toLowerCase(letter);
        int buildingarraylength = 0;

        for (int i = 0; i < str.length(); i++) {
            char currentLetter = str.charAt(i);
            if (currentLetter == letter)
                buildingarraylength++;
        }

        buildingarraylength = buildingarraylength/2;
        BuildingByUserStrArray = new String[buildingarraylength];
        buildingarraylength=buildingarraylength*2;
        for(int i=0;i<buildingarraylength;i++){
            i+=1;
            BuildingByUserStrArray[j] = arrOfStrbuilding[i];
            j+=1;
        }



        JsonObject FloorRequestJsonObj = new JsonObject();
        FloorRequestJsonObj.addProperty("building", "Schapiro");

        //getting the floors from the database
        String payloadbuilding = "{"+
                "\"building\": "+"\"Schapiro\""+
                "}";
        StringEntity entityfloor = new StringEntity(payloadbuilding,
                ContentType.APPLICATION_JSON);

        StringEntity entityJson = new StringEntity(FloorRequestJsonObj.toString(),
                ContentType.APPLICATION_JSON);

        HttpClient httpClientfloor = HttpClientBuilder.create().build();
        HttpPost requestfloor = new HttpPost("http://irt-ap.cs.columbia.edu/api/get_floors/");
        //requestfloor.setEntity(entityfloor);
        requestfloor.setEntity(entityJson);

        HttpResponse responsefloor = null;
        try {
            responsefloor = httpClientfloor.execute(requestfloor);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String floorhttpanswer = null;
        try {
            floorhttpanswer = EntityUtils.toString(responsefloor.getEntity());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println(floorhttpanswer);


        j=0;
        String[] arrOfStrfloors = floorhttpanswer.split("[{+:]",0);
        int floorarraylength = arrOfStrfloors.length-1;
        FloorNameByUserStrArray = new String[floorarraylength/2];
        String[] CopyfloorStr = new String[floorarraylength];
        for(int i=1;i<floorarraylength;i++){
            arrOfStrfloors[i] = arrOfStrfloors[i].substring(1,arrOfStrfloors[i].length()-1);
            CopyfloorStr[j] = arrOfStrfloors[i];
            j+=1;
            i=i+1;
        }

        for(int k=0;k<floorarraylength/2;k++) {
            FloorNameByUserStrArray[k] = CopyfloorStr[k];
        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                initUI();
            }
        });




        //open floor menu
       // new ComboBoxExample();

        /*
//--------------------------------------------------------------------------------



//---------------------------------------------------------------------------------

*/
 //       String emergencytest = "[{\"7th Floor\":\"5ddda3799c5b6a511bd792aa\"},{\"8th Floor\":\"5dddab9264f41a53379cc52d\"},{\"9th Floor\":\"5dddbb609a15f3595a8ce980\"},{\"10th Floor\":\"5dddbb659a15f3595a8ce981\"},{\"11th Floor\":\"5dddbb769a15f3595a8ce982\"}]\n"









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

create json object for each room !!!!!

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