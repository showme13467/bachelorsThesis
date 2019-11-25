package Calc;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;


public class DrawPolygons {

    public static String RoomNameByUserStr = null;
    public static Boolean NewPolyBool = true;
    public static Boolean RightClickAllowed = false;
    public static Boolean KeyBoardAllowed = false;
    public static Boolean Actbool = false;
    
    
    static List<String> listPolygonPoints = new ArrayList<String>(); //list of coord points of polygons and name of room
    static List<Integer> IntPolyPoints = new ArrayList<Integer>();

    public static int Polyboolcnt = 0; //cnt for whether a polygon is drew completely or canceled
    public static int listcnt = 0; //cnt for list index


    public static void simulatingkeyfct() {
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e1) {
            e1.printStackTrace();
        }

        // Simulate a key press
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    private static final int Window_width = 1210;
        private static final int Window_height = 1200;

        public static Polygon ContinuePolygon = new Polygon();


    public static class Drawing extends JPanel implements KeyListener {

        Ellipse2D ellipse = new Ellipse2D.Double();

        private static final Font FONT = new Font("Arial", Font.BOLD, 18);

            private List<Polygon> polygons = new ArrayList<Polygon>();

            private static Polygon currentPolygon = new Polygon();

    @Override
    public void keyTyped(KeyEvent e){
      }

    @Override
    public void keyPressed(KeyEvent e) {
        if(KeyBoardAllowed == true){
            int key = e.getKeyCode();

        if (currentPolygon.npoints > 2 && key == KeyEvent.VK_ENTER) {
            ContinuePolygon = currentPolygon;
            System.out.println("Pressed Enter");
            int helpintx = currentPolygon.xpoints[0];
            int helpinty = currentPolygon.ypoints[0];
            currentPolygon.addPoint(helpintx,helpinty);
            IntPolyPoints.add(helpintx);
            IntPolyPoints.add(helpinty);
            createPolygon();
        }
        if (key == KeyEvent.VK_BACK_SPACE) {
            System.out.println("Pressed Backspace");
            int oldPtx = 0;
            int oldPty = 0;
            clearCurrentPolygon();
            if(Polyboolcnt > 1) {
                Polyboolcnt = Polyboolcnt - 2;
                for (int i = 0; i < Polyboolcnt; i++) {
                    oldPtx = IntPolyPoints.get(i);
                    i += 1;
                    oldPty = IntPolyPoints.get(i);
                    currentPolygon.addPoint(oldPtx, oldPty);
                }
                IntPolyPoints.remove(IntPolyPoints.size() - 1);
                IntPolyPoints.remove(IntPolyPoints.size() - 1);
        //        listPolygonPoints.remove(listPolygonPoints.size() - 2);
        //        listPolygonPoints.remove(listPolygonPoints.size() - 2);
            }
            else {
                Polyboolcnt = 0;
                IntPolyPoints.clear();
                listPolygonPoints.clear();
            }
        }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }


        public void ContinuetoDraw(){
        clearCurrentPolygon();
        currentPolygon = ContinuePolygon;
        repaint();
    }


            private MouseAdapter mouseListener = new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
/*
                    if (ellipse.contains(e.getX(), e.getY())) {
                        System.out.println("RUUUUUUUUUUUUUUUN");

                        int x = e.getX(); // x-coordinate of point
                        int y = e.getY(); // y-coordinate of point
                        ellipse = null;
                        for (int i = ellipse.size() - 1; i >= 0; i--) { // check shapes from front to back
                            Ellipse2D s = (Ellipse2D) ellipse.get(i);
                            if (s.containsPoint(x, y)) {
                                dragShape = s;
                                prevDragX = x;
                                prevDragY = y;
                                ellipse = s;
                                break;
                            }

                        }

                    } else */ {

                        //LeftMouseButton handling => draw
                        if (NewPolyBool == true && SwingUtilities.isLeftMouseButton(e)) {
                            if (e.getClickCount() == 1) {
                                addPoint(e.getX(), e.getY());
                            } else if (e.getClickCount() == 2) {
                                currentPolygon.addPoint(currentPolygon.xpoints[0],currentPolygon.ypoints[0]);
                                IntPolyPoints.add(e.getX());
                                IntPolyPoints.add(e.getY());
                                createPolygon();

                            }
                            //RightMouseButton handling => cancel drawing
                        } else if (RightClickAllowed == true && NewPolyBool == true &&
                                SwingUtilities.isRightMouseButton(e)) {

                            clearCurrentPolygon();
                            System.out.println("Polygon cleared!");

                            Polyboolcnt = Polyboolcnt + listcnt;
                            //loop for removing already inserted coord points of canceled polygon
                            for (int i = Polyboolcnt; i > 0; i--) {
                                listPolygonPoints.remove(i - 1);
                                IntPolyPoints.remove(i-1);
                            }

                            //setting list index to value before adding points of canceled polygon
                            Polyboolcnt = 0; //new polygon
                            RightClickAllowed = false;
                        }
                    }
                }

            };

            public Drawing() {
                addMouseListener(mouseListener);
                addKeyListener(this);
            }
        public void addNotify() {
            super.addNotify();
            requestFocus();
        }

            protected void addPoint(int x, int y) {

                currentPolygon.addPoint(x, y);
                System.out.println("x + y: " + x + ", " + y);

                listPolygonPoints.add(String.valueOf(x));//x-coord of polygon
                IntPolyPoints.add(x);
                Polyboolcnt += 1;

                listPolygonPoints.add(String.valueOf(y)); //y-coord of polygon
                IntPolyPoints.add(y);
                Polyboolcnt += 1;
                System.out.println("Polyboolcnt: " + Polyboolcnt);
                System.out.println("listcnt: " + listcnt);
                repaint();
                RightClickAllowed = true;
                KeyBoardAllowed = true;
            }

            //canceling to draw polygon
            protected void clearCurrentPolygon() {
                currentPolygon = new Polygon();
                repaint();
            }

            //polygon must consist out of at least 3 points minimum
            public void createPolygon() { //protected
                if (currentPolygon.npoints > 2) {
                    polygons.add(currentPolygon);
                    System.out.println("Polygon finished! \n \n");
                    listcnt = Polyboolcnt + listcnt;
                    Polyboolcnt = 0; //polygon finished, ready for new one
                    listcnt += 1;
                    createWindow();
                    NewPolyBool = false;
                    RightClickAllowed = false;
                    KeyBoardAllowed = false;
                  }
                clearCurrentPolygon();
                repaint();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(Window_width, Window_height);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                //setting the floorplan as the background
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
                drawGivenPolygons(g, testPPP);

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
                    //g.drawPolygon(polygon);

                    for (int i = 0; i < polygon.npoints - 1; i++)
                        g2.drawLine(x[i], y[i], x[i + 1], y[i + 1]);
                }

                for (int i = 0; i < polygon.npoints; i++) {
                        drawNthPoint(g2, polygon, i);
                    }

            }

            //Designation of the corner points of a polygon
            private void drawNthPoint(Graphics g, Polygon polygon, int nth) {
                Graphics2D g2 = (Graphics2D) g;
                // Only works 26 times!
                String name = Character.toString((char) ('A' + nth));
                int x = polygon.xpoints[nth];
                int height = g.getFontMetrics().getHeight();
                int y = polygon.ypoints[nth] < height ? polygon.ypoints[nth] + height : polygon.ypoints[nth];
                g.drawString(name, x, y);
                ellipse = new Ellipse2D.Double(x,y,20,20);
                g2.draw(ellipse);
            }


            public void drawGivenPolygons(Graphics g, int[] points) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3)); //thickness of lines
                g2.setColor(Color.RED);

                int n = points.length;
                int[] x = new int[n/2];
                int[] y = new int[n/2];
                int xcnt = 0;
                int ycnt = 0;
                for(int i=0;i< n;i++){
                    if(i%2 == 0){
                        x[xcnt] = points[i];
                        xcnt +=1;
                    }
                    else{
                        y[ycnt] = points[i];
                        ycnt +=1;
                    }
                }

                Polygon poly = new Polygon(x,y,n/2);
                g2.drawPolygon(poly);
                g2.setColor(Color.green);
            }

    }





    final static Font Btnfont = new Font("Serif", Font.BOLD, 20);

    protected static void initUI() {
        final JFrame frame = new JFrame("DrawPolygons");

        final JButton UndoBtn = new JButton("Undo");
        UndoBtn.setFont(Btnfont);
        UndoBtn.setBackground(Color.LIGHT_GRAY);
        UndoBtn.setForeground(Color.black);
        UndoBtn.setBounds(800,810, 200,100);
        UndoBtn.setVisible(true);
        UndoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });

        final JButton CloseBtn = new JButton("Close");
        CloseBtn.setFont(Btnfont);
        CloseBtn.setBackground(Color.LIGHT_GRAY);
        CloseBtn.setForeground(Color.black);
        CloseBtn.setBounds(500,810, 200,100);
        CloseBtn.setVisible(true);
        CloseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        final JButton SavePolygonBtn = new JButton("Save Polygon");
        SavePolygonBtn.setFont(Btnfont);
        SavePolygonBtn.setBackground(Color.LIGHT_GRAY);
        SavePolygonBtn.setForeground(Color.black);
        SavePolygonBtn.setBounds(200,810, 200,100);
        SavePolygonBtn.setVisible(true);
        SavePolygonBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DrawPolygons.simulatingkeyfct();

            }
        });

        frame.add(UndoBtn);
        frame.add(SavePolygonBtn);
        frame.add(CloseBtn);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Drawing());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }

    private static void createWindow() {
        JFrame frame = new JFrame("Name of room");
        createUI(frame);
        frame.setSize(560, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void createUI(final JFrame frame) {
        JPanel panel = new JPanel();
        LayoutManager layout = new FlowLayout();
        panel.setLayout(layout);

        final JButton ContinueDrawingBtn = new JButton("Continue Drawing");
        ContinueDrawingBtn.setVisible(true);
        ContinueDrawingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int oldPtx = 0;
                int oldPty = 0;
                    Polyboolcnt = Polyboolcnt - 2;
                    for (int i = 0; i < Polyboolcnt; i++) {
                        oldPtx = IntPolyPoints.get(i);
                        i += 1;
                        oldPty = IntPolyPoints.get(i);
                        ContinuePolygon.addPoint(oldPtx, oldPty);
                    }
                    IntPolyPoints.remove(IntPolyPoints.size() - 1);
                    IntPolyPoints.remove(IntPolyPoints.size() - 1);
           //     Drawing.ContinuetoDraw();
                frame.dispose();
            }
        });

        final JButton CloseBtnW = new JButton("Save this name");
        CloseBtnW.setVisible(false);
        CloseBtnW.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listPolygonPoints.add(RoomNameByUserStr);
                System.out.println("String list: "+listPolygonPoints);
                System.out.println("Int list: "+IntPolyPoints);
                NewPolyBool = true;
                frame.dispose();
            }
        });

        JButton button = new JButton("Type in the name of the room.");
        final JLabel label = new JLabel();

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = (String)JOptionPane.showInputDialog(

                        frame,
                        "Type in the name of the room.",
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

        panel.add(ContinueDrawingBtn);
        panel.add(CloseBtnW);
        panel.add(button);
        panel.add(label);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
    }


    public static void main(String[] args) {
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
Show already written polygons *[finished]*

keyboard enter and backspace *[finished]*

drag and drop ***[PROGRESS, stuck]*** maybe JavaFx

buttons disappear when mouse is moving

undo function *[finished]* delete current and paint new with 2 points less

buttons function  ***[PROGRESS, stuck]*** simulate pressing enter key

line to line not first point and last point connect to new point *[finished]*

multiple polygons for one name => hallway contains different rooms

just one name in Array *[finished]*

split Array into int Arrays for correct data *[finished]*

 */