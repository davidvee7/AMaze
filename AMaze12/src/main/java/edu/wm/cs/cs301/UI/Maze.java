package edu.wm.cs.cs301.UI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;




import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.renderscript.Font;
import android.util.Log;
import android.util.EventLog.Event;
import android.view.View;



//import java.awt.Color;
//import java.awt.Event;
//import java.awt.Font;
//import java.awt.FontMetrics;
//import java.awt.Graphics;
//import java.awt.Panel;

/**
 * Class handles the user interaction for the maze.
 * It implements a state-dependent behavior that controls the display and reacts to key board input from a user.
 * After refactoring the original code from an applet into a panel, it is wrapped by a MazeApplication to be a java application
 * and a MazeApp to be an applet for a web browser. At this point user keyboard input is first dealt with a key listener
 * and then handed over to a Maze object by way of the keyDown method.
 *
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 */
// MEMO: original code: public class Maze extends Applet {
public class Maze implements Serializable{ //took away Panel

    Font smallBannerFont, largeBannerFont;
 
    public static final int view_width = 490;
    public static final int view_height = 490;
    int zscale = view_height/2;

    static final int map_unit = 128;
    static final int view_offset = map_unit/8;
    static final int step_size = map_unit/4;

    private RangeSet rset;
    //private Graphics gc;

    //private Image buffer_img;
    
    public Color col;
 
    public int state;            // keeps track of the current GUI state, one of STATE_TITLE,...,STATE_FINISH, mainly used in redraw()
    // user can navigate
    // title -> generating -(escape) -> title
    // title -> generation -> play -(escape)-> title
    // title -> generation -> play -> finish -> title
    // STATE_PLAY is the main state where the user can navigate through the maze in a first person view
    static final int STATE_TITLE = 1;
    static final int STATE_GENERATING = 2;
    static final int STATE_PLAY = 3;
    static final int STATE_FINISH = 4;
 
    public int percentdone = 0; // describes progress during generation phase
    public boolean showMaze;             // toggle switch to show overall maze on screen
    public boolean showSolution;        // toggle switch to show solution in overall maze on screen
    public boolean solving;            // toggle switch

    final int viewz = 50;
    int viewx, viewy, ang;
    public int dx, dy;  // current direction
    public int px, py ; // current position on maze grid (x,y)
    int walk_step;
    int view_dx, view_dy; // current view direction


    // debug stuff
    boolean deepdebug = false;
    boolean all_visible = false;
    boolean new_game = false;

    public int mazew; // width of maze
    int mazeh; // height of maze
    // grid for maze
    Cells mazecells ;
    int[][] mazedists;
    Cells seencells ;
    //BSPNode bsp_root;
 
    public boolean map_mode; // true: display map of maze, false: do not display map of maze
    // map_mode is toggled by user keyboard input, causes a call to draw_map during play mode
    //int map_scale; relocated to mapdrawer

    // MapDrawer to perform drawing of maps
    public transient MapDrawer mapdrawer ;
    // Drawer to get the first person perspective
    public transient FirstPersonDrawer firstpersondrawer ;
    // Mazebuilder is used to calculate a new maze together with a solution
    // The maze is computed in a separate thread. It is started in the local Build method.
    // The calculation communicates back by calling the local newMaze() method.
    transient public MazeBuilder mazebuilder;

    public BSPNode bsp_root;
    
    //Robot robot;
    public String robotDriver;
    public int skill;
    public boolean ready = false;
   // public ManualDriver manualDriverM;
  
 
    public int energyUsed = 0;
    public int totalTravel = 0;
 
    // manualDriver.setRobot somewhere
    // pass in the maze into robot(...)
    // make a constructor in BasicRobot to accept a maze object.

 
    

    // fixing a value matching the escape key
    final int ESCAPE = 27;

    // generation method used to compute a maze
    int method = 0 ; // 0 : default method, Falstad's original code

  //  public GraphicsWrapper gw;
    //gw = new GraphicsWrapper();
    // method == 1: Prim's algorithm
 
    //Paint _paint;
    static int skill_x[] =     { 4, 12, 15, 20, 25, 25, 35, 35, 40, 60,
        70, 80, 90, 110, 150, 300 };
    static int skill_y[] =     { 4, 12, 15, 15, 20, 25, 25, 35, 40, 60,
        70, 75, 75,  90, 120, 240 };
    static int skill_rooms[] = { 0,  2,  2,  3,  4,  5, 10, 10, 20, 45,
        45, 50, 50,  60,  80, 160 };
    static int skill_partct[] = { 60,
        600, 900, 1200,	
        2100, 2700, 3300,
        5000, 6000, 13500,
        19800, 25000, 29000,
        45000, 85000, 85000*4 };
    
    /**
     * Constructor
     */
    
    
    public Maze() {
    	
    	//super(context);
    	
         Log.v("maze", "inside constructor");
         
         //gw.setColor("WHITE");
        
         
         //this.gw = new GraphicsWrapper(this);
        //rset = new RangeSet();
         
         
    }

    int _height;
    int _width;
     
    Bitmap _bitmap;
    Canvas _canvas;
     

    
    /**
     * Call back method for MazeBuilder to communicate newly generated maze as reaction to a call to build()
     * @param root node for traversals, used for the first person perspective
     * @param cells encodes the maze with its walls and border
     * @param dists encodes the solution by providing distances to the exit for each position in the maze
     * @param startx current position, x coordinate
     * @param starty current position, y coordinate
     */
    public void newMaze(BSPNode root, Cells c, int dists[][], int startx, int starty) {
        if (Cells.deepdebugWall)
        {   // for debugging: dump the sequence of all deleted walls to a log file
            // This reveals how the maze was generated
            c.saveLogFile(Cells.deepedebugWallFileName);
        }
        showMaze = showSolution = solving = false;
        mazecells = c ;
        mazedists = dists;
        seencells = new Cells(mazew+1,mazeh+1) ;
        bsp_root = root; // delegated to firstpersondrawer
        setCurrentDirection(1, 0) ;
        setCurrentPosition(startx,starty) ;
        walk_step = 0;
        view_dx = dx<<16;
        view_dy = dy<<16;
        ang = 0;
        map_mode = false;
        // mazew and mazeh have been set in build() method before mazebuider was called to generate a new maze.
        // reset map_scale in mapdrawer to a value of 10
        mapdrawer = new MapDrawer(view_width,view_height,map_unit,step_size, mazecells, seencells, 12, mazedists, mazew, mazeh) ;
 
        firstpersondrawer = new FirstPersonDrawer(view_width,view_height,map_unit,step_size, mazecells, seencells, 12, mazedists, mazew, mazeh, root) ;
        // set the current state for the state-dependent behavior
        
        //gw = new GraphicsWrapper();
 
        state = STATE_PLAY;
   
        // graphics update
        //redraw();
        //onDraw()
        // the next line is needed because we're in a separate thread;
        // we can't seem to do a real paint in this thread, so tell
        // the main thread to do it.
        //invalidate();
    }

    private void writeObject(ObjectOutputStream out) throws IOException{
    	
    	out.defaultWriteObject();
//    	out.writeObject(rset);
//    	out.writeInt(percentdone);
    	out.writeBoolean(showMaze);
    	out.writeBoolean(showSolution);
    	out.writeBoolean(solving);
    	//out.writeInt(viewz);
    	out.writeInt(viewx);
    	out.writeInt(viewy);
    	out.writeInt(ang);
    	out.writeInt(dx);
    	out.writeInt(dy);
    	out.writeInt(px);
    	out.writeInt(py);
    	out.writeInt(walk_step);
    	out.writeInt(view_dx);
    	out.writeInt(view_dy);
    	out.writeBoolean(deepdebug);
    	out.writeBoolean(all_visible);
    	out.writeBoolean(new_game);
//    	Log.v("Maze", "" + mazew);
    	out.writeInt(mazew);
    	out.writeInt(mazeh);
    	out.writeObject(mazecells);
    	out.writeObject(mazedists);
    	out.writeObject(seencells);
    	out.writeBoolean(map_mode);
    	out.writeObject(mapdrawer);
    	out.writeObject(firstpersondrawer);
//    	//out.writeObject(mazebuilder);
//    	//out.writeInt(skill);
//     //	out.writeBoolean(ready);
    	out.writeInt(energyUsed);
    	out.writeInt(totalTravel);
//    	//out.writeInt(ESCAPE);
    	out.writeInt(method);
    	//out.writeObject(gw);
    	out.writeObject(bsp_root);
    	
    	
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    	
    	in.defaultReadObject();
    	
  //      rset = (RangeSet) in.readObject();
      //  percentdone = in.readInt();
        showMaze = in.readBoolean();
        showSolution = in.readBoolean();
         solving = in.readBoolean();
      
        viewx = in.readInt();
         viewy = in.readInt();
         ang = in.readInt();
         dx = in.readInt();
         dy = in.readInt();
         px = in.readInt();
         py = in.readInt();
         walk_step = in.readInt();
         view_dx = in.readInt();
         view_dy = in.readInt();
      
         deepdebug = in.readBoolean();
         all_visible = in.readBoolean();
         new_game = in.readBoolean();
//         Log.v("Maze", "" + mazew);
         mazew = in.readInt(); 
         mazeh = in.readInt(); 

         mazecells = (Cells)in.readObject();
         mazedists = (int[][])in.readObject();
         seencells = (Cells)in.readObject();
         map_mode = in.readBoolean(); // true: display map of maze, false: do not display map of maze

         mapdrawer = (MapDrawer) in.readObject() ;

         firstpersondrawer = (FirstPersonDrawer)in.readObject();

//     //   MazeBuilder mazebuilder = (MazeBuilder)in.readObject();
//
//       skill = in.readInt();
//       ready = in.readBoolean();
//       // public ManualDriver manualDriverM;
//      
//     
        energyUsed = in.readInt();
        totalTravel = in.readInt();

//        // ESCAPE = in.readInt();

         method = in.readInt(); // 0 : default method, Falstad's original code
//
    //    gw = (GraphicsWrapper) in.readObject();
         bsp_root = (BSPNode)in.readObject();
    	
    	
    }	

 
    private void setCurrentPosition(int x, int y)
    {
        px = x ;
        py = y ;
    }
    private void setCurrentDirection(int x, int y)
    {
        dx = x ;
        dy = y ;
    }
 
 
    void buildInterrupted() {
        state = STATE_TITLE;
        redraw();
        mazebuilder = null;
    }

    final double radify(int x) {
        return x*Math.PI/180;
    }



    /**
     * Updates graphical output on screen, called from MazeBuilder
     */
    public void redraw() {

    	

        redrawPlay();


        
       // update(gw);
        
    }

//    private void centerString(Graphics g, FontMetrics fm, String str, int ypos) {
//        g.drawString(str, (view_width-fm.stringWidth(str))/2, ypos);
//    }
//
    /**
     * Allows external increase to percentage in generating mode with subsequence graphics update
     * @param pc gives the new percentage on a range [0,100]
     * @return true if percentage was updated, false otherwise
     */
    public boolean increasePercentage(int pc) {
        if (percentdone < pc && pc < 100) {
            percentdone = pc;
//            if (state == STATE_GENERATING)
//            {
//                redraw();
//                repaint();
//            }
//            else
//                dbg("Warning: Receiving update request for increasePercentage while not in generating state, skip redraw.") ;
            return true ;
        }
        
        return false ;
    }
    /**
     * Helper method for redraw to draw screen during the game. If map_mode is true, i.e. the user wants to see the overall map,
     * the map is drawn only on a small rectangle inside the maze area. The current position is located is centered such that
     * it may happen that only a part of the map fits the display and is thus visible.
     * @param gc graphics handler to manipulate screen
     */
    public void redrawPlay() {
        firstpersondrawer.redrawPlay(px, py, view_dx, view_dy, walk_step, view_offset, rset, ang) ;
 
        if (map_mode) {
            mapdrawer.draw_map(px, py, walk_step, view_dx, view_dy, showMaze, showSolution) ;
            mapdrawer.draw_currentlocation(view_dx, view_dy) ;
        }
        // send message through handler to UI thread.
    }
 

    /**
     * overwrites Applet method, called in redraw()
     */
//    public void update(GraphicsWrapper gw) {
//        paint(gw) ;
//        if (solving)
//            solveStep();
//    }
 
    /**
     * overwrites Applet method
     */
//    public void paint(GraphicsWrapper gw) {
//        gw.canvas.drawBitmap(gw.bitmap, 0, 0, _paint);
//    }

    /////////////////////// Methods for debugging ////////////////////////////////
    private void dbg(String str) {
        System.out.println(str);
    }

    private void logPosition() {
        if (!deepdebug)
            return;
        dbg("x="+viewx/map_unit+" ("+
                viewx+") y="+viewy/map_unit+" ("+viewy+") ang="+
                ang+" dx="+dx+" dy="+dy+" "+view_dx+" "+view_dy);
    }
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * Helper method for walk()
     * @param dir
     * @return true if there is no wall in this direction
     */
    public boolean checkMove(int dir) {
        // obtain appropriate index for direction (CW_BOT, CW_TOP ...)
        // for given direction parameter
        int a = ang/90;
        if (dir == -1)
            a = (a+2) & 3; // TODO: check why this works
        // check if cell has walls in this direction
        // returns true if there are no walls in this direction
        int[] masks = Cells.getMasks() ;
        return mazecells.hasMaskedBitsFalse(px, py, masks[a]) ;
    }



    private void rotateStep() {
        ang = (ang+1800) % 360;
        view_dx = (int) (Math.cos(radify(ang))*(1<<16));
        view_dy = (int) (Math.sin(radify(ang))*(1<<16));
        moveStep();
    }

    private void moveStep() {
        redrawPlay();
    	//PlayMaze.gw.invalidate();
        try {
            Thread.currentThread().sleep(25);
        } catch (Exception e) { }
    }

    private void rotateFinish() {
        setCurrentDirection((int) Math.cos(radify(ang)), (int) Math.sin(radify(ang))) ;
        logPosition();
    }

    private void walkFinish(int dir) {
        setCurrentPosition(px + dir*dx, py + dir*dy) ;
 
        if (isEndPosition(px,py)) {
            state = STATE_FINISH;
            redrawPlay();
            //PlayMaze.gw.invalidate();
        }
        walk_step = 0;
        logPosition();
    }

    /**
     * checks if the given position is outside the maze
     * @param x
     * @param y
     * @return true if position is outside, false otherwise
     */
    public boolean isEndPosition(int x, int y) {
        return x < 0 || y < 0 || x >= mazew || y >= mazeh;
    }



    synchronized public boolean walk(int dir) {
        if (!checkMove(dir))
            return false;
        for (int step = 0; step != 4; step++) {
            walk_step += dir;
            moveStep();
        }
        walkFinish(dir);
        return true;
    }

    synchronized public void rotate(int dir) {
        int origang = ang;
        int steps = 4;

        for (int step = 0; step != steps; step++) {
            ang = origang + dir*(90*(step+1))/steps;
            rotateStep();
        }
        rotateFinish();
    }

    /**
     * Helper method for solveStep
     * @param n
     * @return
     */
    private int rotateTo(int n) {
        int numRotations = 0;
        int a = ang/90;
        if (n == a)
            ;
    
        else if (n == ((a+2) & 3)) {
            rotate(1);
            rotate(1);
            numRotations =2;
        } else if (n == ((a+1) & 3)) {
            rotate(1);
            numRotations =1;
        } else{
            rotate(-1);}
            numRotations =1;
        return numRotations;
    }

    /**
     * Method is only called in update() method (which serves redraw())
     * @return
     */
    synchronized int solveStep() {
        solving = false;
        int d = mazedists[px][py];
       // gw.setColor("YELLOW");
        // case 1: we are not directly next to the final position
        if (d > 1) {
            int n = getDirectionIndexTowardsSolution(px,py,d);
            if (n == 4)
                dbg("HELP!");
            int rotations = rotateTo(n);
            walk(1);
           // invalidate();
            //repaint(25);
            
            solving = true;
            return rotations;
          
        }
        // case 2: we are one step close to the final position
        int n;
        int[] masks = Cells.getMasks() ;
        for (n = 0; n < 4; n++) {
            // skip this direction if there is a wall or border
            if (mazecells.hasMaskedBitsGTZero(px, py, masks[n]))
                continue;
            // stop if position in this direction is end position
            if (isEndPosition(px+mapdrawer.dirsx[n], py+mapdrawer.dirsy[n]))
                break ;
        }
        int rotations = rotateTo(n);
        walk(1);
        return rotations;
    }


    private int getDirectionIndexTowardsSolution(int x, int y, int d) {
        int[] masks = Cells.getMasks() ;
        for (int n = 0; n < 4; n++) {
            if (mazecells.hasMaskedBitsTrue(x,y,masks[n]))
                continue;
                int dx = mapdrawer.dirsx[n];
                int dy = mapdrawer.dirsy[n];
                int dn = mazedists[x+dx][y+dy];
                if (dn < d)
                    return n ;
        }
        return 4 ;
    }

    /**
     * Method incorporates all reactions to keyboard input in original code,
     * after refactoring, Java Applet and Java Application wrapper call this method to communicate input.
     */
//    public boolean keyDown(Event evt, int key) {
//        System.out.println(" enter maze's keyDown");
//        switch (state) {
//        // if screen shows title page, keys describe level of expertise
//        // create a maze according to the user's selected level
//        case STATE_TITLE:
//            if (key >= '0' && key <= '9') {
//                build(key - '0');
//                break;
//            }
//            if (key >= 'a' && key <= 'f') {
//                build(key - 'a' + 10);
//                break;
//            }
//            break;
//        // if we are currently generating a maze, recognize interrupt signal (ESCAPE key)
//        // to stop generation of current maze
//        case STATE_GENERATING:
//            if (key == ESCAPE) {
//                mazebuilder.Interrupt();
//                buildInterrupted();
//            }
//            break;
        // if user explores maze,
        // react to input for directions and interrupt signal (ESCAPE key)
        // react to input for displaying a map of the current path or of the overall maze (on/off toggle switch)
        // react to input to display solution (on/off toggle switch)
        // react to input to increase/reduce map scale
//        case STATE_PLAY:
//            switch (key) {
//            case Event.UP: case 'k': case '8':
//                walk(1);
//                break;
//            case Event.LEFT: case 'h': case '4':
//                rotate(1);
//                break;
//            case Event.RIGHT: case 'l': case '6':
//                rotate(-1);
//                break;
//            case Event.DOWN: case 'j': case '2':
//                walk(-1);
//                break;
//            case ESCAPE: case 65385:
//                if (solving)
//                    solving = false;
//                else
//                    state = STATE_TITLE;
//                redraw();
//                break;
//            case ('w' & 0x1f):
//            {
//                setCurrentPosition(px + dx, py + dy) ;
//                redraw();
//                break;
//            }
//            case '\t': case 'm':
//                map_mode = !map_mode; redraw(); break;
//            case 'z':
//                showMaze = !showMaze; redraw(); break;
//            case 's':
//                showSolution = !showSolution; redraw(); break;
//            case ('s' & 0x1f):
//                if (solving)
//                    solving = false;
//                else {
//                    solving = true;
//                    invalidate();
//                    //repaint(25);
//                }
//            break;
//            case '+': case '=':
//            {
//                if (mapdrawer != null)
//                {
//                    mapdrawer.incrementMapScale() ;
//                    redraw() ;
//                }
//                // else ignore
//                break ;
//            }
//            case '-':
//                if (mapdrawer != null)
//                {
//                    mapdrawer.decrementMapScale() ;
//                    redraw() ;
//                }
//                // else ignore
//                break ;
//            }
//            break;
//        // if we are finished, return to initial state with title screen
////        case STATE_FINISH:
////            state = STATE_TITLE;
////            redraw();
////            break;
////        }
//        return true;
//    }

 
    public void init() {
    	
    	//this.gw = new GraphicsWrapper(this);
    	 rset = new RangeSet();
        //state = STATE_TITLE;
        //buffer_img = createImage(view_width, view_height);
        //gc = buffer_img.getGraphics(); 
        //this.gw = new GraphicsWrapper(this);
        //this.gc = gw.gc;
        //gw.gc = buffer_img.getGraphics();
        
//        if (null == gw.buffer_img)
//        {
//            System.out.println("Error: creation of buffered image failed, presumedly container not displayable");
//        }
//        rset = new RangeSet();
        
        
        //largeBannerFont = new Font("TimesRoman", Font.BOLD, 48);
        //smallBannerFont = new Font("TimesRoman", Font.BOLD, 16);
        // force MazeBuilder to load; if we load it later, it takes
        // FOREVER, using Netscape 2.0b5 on Windows NT
 
//        MazeBuilder mbjunk = new MazeBuilder(); //The original one.
//        
//        //MazeBuilder mbjunk = new MazeBuilderPrim();
//        FloatPair fpjunk = new FloatPair(0.0, 0.0);
//        RangePair rpjunk = new RangePair(0, 0, 0, 0);
//        Seg sgjunk = new Seg(0, 0, 0, 0, 0, 0, gw);
//        RangeSetElement rsejunk = new RangeSetElement(0, 0);
        
    }
    
    

    public void start() {
        redraw();
    }
    
    /**
     * Method obtains a new Mazebuilder and has it compute new maze,
     * it is only used in keyDown()
     * @param skill level determines the width, height and number of rooms for the new maze
     */

    
    public void build(int skill) {
    	
        mazebuilder = new MazeBuilder(true, skill);
        //int roomcount = skill_rooms[skill];
        this.mazew = skill_x[skill];
        this.mazeh = skill_y[skill];
        int roomcount = skill_rooms[skill];
        
        mazebuilder.build(this, mazew, mazeh, roomcount, skill_partct[skill]);
        // mazebuilder calls back by calling newMaze() to return newly generated maze

    }

}
//
//
