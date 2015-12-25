package edu.wm.cs.cs301.UI;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class PlayMaze extends Activity implements View.OnKeyListener{
    //Maze maze;
    public static GraphicsWrapper gw;
    MazeBuilder mazebuilderSetUp;
    public static Maze maze;
    String robotName;
    Intent finishIntent;
    static int level;
	private Object challenge;
    public static BasicRobot robot;
//	RobotDriver driver;
    Gambler gambler;
	static Handler driveHandler;
	private WallFollower wallfollower;
	private Wizard wizard;
	private CuriousGambler cGambler;
	static Runnable driveRunnable;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("playmaze", "inside playmaze");
       // Intent intent = getIntent();
       // intent.getExtras();
        //Bundle extras = intent.getSerializableExtra();
      //  Bundle parameters = getIntent().getExtras();
        //Bundle extras = intent.getExtras();
        Bundle parameters = getIntent().getExtras();

        
        Log.v("playmaze", "defining maze");
        maze = (Maze)getIntent().getSerializableExtra("maze");
         robotName = parameters.getString("robotName");
       // maze.init();
       // this.maze= maze;
        //maze = new Maze(this);
      
     //   level = parameters.getInt("complexity");
      
      //  Intent titleIntent = new Intent(this, TitleScreen.class);
        finishIntent = new Intent(this, FinishScreen.class);
        challenge = parameters.getString("challenge");


//        mazebuilderSetUp = new MazeBuilder(true, level);
//      
//        int[] params = mazebuilderSetUp.setUpBuild("Kruskal", level);
//        Log.v("genScreen", "params set");
//      
//        maze = new Maze();
//        Log.v("genScreen", "maze made");
//        Log.v("genScreen", "maze init");
//        maze.init();
//      
//        maze.build(0, params);
//        while (maze.mazebuilder.buildThread.isAlive()){
//            continue;
//        }
       maze.totalTravel= 0;
        Log.v("genScreen", "finished building maze");
        //gw.setMaze(maze);
        gw = new GraphicsWrapper(this);
      //  gw.onDraw(Canvas canvas);
        Canvas canvas = new Canvas();
        gw.onDraw(canvas);
        if (gw.canvas!=null){
        	Log.v("playmaze", "canvas exists");
        }
        //ArrayList<View> buttons = new ArrayList<View>();
        Button b1 = new Button(this);
        Button b2 = new Button(this);
        Button b3 = new Button(this);
        Button b4 = new Button(this);
        Button b5 = new Button(this);
        
        
        b1.setText("Back");
        b2.setText("Finish");
        b3.setText("View maze");
        b4.setText("Show solution");
        b5.setText("View whole maze");

        
      
        b1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                switchToTitle(v);
            }
        });
        
        b2.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
        		switchToFinish(v);
        	}
        });
        
        b3.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
        		maze.map_mode = !maze.map_mode;
        		maze.redrawPlay();
                gw.invalidate();
        	}
        });
        
        b4.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
        		maze.showSolution = !maze.showSolution;
        		maze.redrawPlay();
                gw.invalidate();
        	}
        });
        
        b5.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
        		maze.showMaze = !maze.showMaze;
        		maze.redrawPlay();
                gw.invalidate();
        	}
        });
      

        setContentView(gw);
        
        // add content view
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      //  b1.setLayoutParams(lp);
      //  this.addView(b1);
      //  addContentView(b1, lp);
        LayoutParams lp2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        
        LayoutParams lp3 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp3.addRule(RelativeLayout.CENTER_HORIZONTAL);
        
        LayoutParams lp4 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //lp4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp4.addRule(RelativeLayout.ALIGN_TOP);
        lp4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
     //   lp4.addRule(RelativeLayout.);
        LayoutParams lp5 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      // lp5.addRule(RelativeLayout
        
        b1.setLayoutParams(lp);
        b2.setLayoutParams(lp2);
        b3.setLayoutParams(lp3);
        b4.setLayoutParams(lp4);
        b5.setLayoutParams(lp5);
        
        RelativeLayout rl = new RelativeLayout(this);
        rl.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        rl.addView(b1);
        rl.addView(b2);
        rl.addView(b3);	
        rl.addView(b4);
        rl.addView(b5);
        addContentView(rl, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
       // when this main thread gets a message: gw.invalidate()

        Log.v("playmaze", "setcontentview");

        robotDriving();
        
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_play_maze, menu);
//        return true;
//    }
//  
    
    public void robotDriving() {
    	
    	driveHandler = new Handler();
    	
        if (robotName.equalsIgnoreCase("Gambler")){
        	// Thread robotThread = new Thread... run(robot =.. driver.. drivetoExit..
        	robot = new BasicRobot(maze);
        	gambler = new Gambler();
        	try {
				gambler.setRobot(robot);
			} catch (UnsuitableRobotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
        	Log.v("Playmaze","gambler setrobot");
        }
        else if(robotName.equalsIgnoreCase("WallFollower")){
        	robot = new BasicRobot(maze);
        	wallfollower = new WallFollower();
        	try {
				wallfollower.setRobot(robot);
			} catch (UnsuitableRobotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	Log.v("Playmaze","wallfollower setrobot");

        }
        else if(robotName.equalsIgnoreCase("Wizard")){
        	robot = new BasicRobot(maze);
        	wizard = new Wizard();
        	try {
				wizard.setRobot(robot);
			} catch (UnsuitableRobotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
        else{// curiousgambler
        	robot = new BasicRobot(maze);
        	cGambler = new CuriousGambler();
        	try {
				cGambler.setRobot(robot);
			} catch (UnsuitableRobotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	Log.v("Playmaze","wallfollower setrobot");
        }
        driveRunnable = new Runnable() {
        	public void run() {
        		gw.invalidate();
        	}
        };

        
        new Thread(new Runnable() {
        	

			public void run() {
        		try {
        			if(robotName.equalsIgnoreCase("Gambler")){
        	        	Log.v("Playmaze","gambler aobut to drive");

        				while(gambler.drive2Exit()){
    					driveHandler.postDelayed(driveRunnable, 500);
    		        	Log.v("Playmaze","gambler driving postdelalyed called once");

        				}
        			}
        			else if(robotName.equalsIgnoreCase("Wizard")){

        				while(wizard.drive2Exit()){
    					driveHandler.postDelayed(driveRunnable, 500);

        			}
        			}
        			else if(robotName.equalsIgnoreCase("Wallfollower")){
        				while(wallfollower.drive2Exit()){
    					

        			}
        			}
        			else if(robotName.equalsIgnoreCase("CuriousGambler")){
        				while(cGambler.drive2Exit()){


        			}
        			}
				} catch (Exception e) {
					e.printStackTrace();
				}
        		
        	}

			
        }).start();
    }
    
    public void switchToFinish(View view){
        Intent finishIntent = new Intent(this, FinishScreen.class);
        Log.v("PlayMaze", "User switched to finish screen");
       // Toast.makeText(PlayMaze.this, "Switch to finish screen", Toast.LENGTH_SHORT).show();
        startActivity(finishIntent);
        finish();
    }
  
    public void switchToTitle(View view){
        Intent titleIntent = new Intent(this, TitleScreen.class);
        Log.v("PlayMaze", "User went back to title screen");
     //   Toast.makeText(PlayMaze.this, "Back to title", Toast.LENGTH_SHORT).show();
        startActivity(titleIntent);
        finish();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }
  public boolean onKeyDown(int keyCode, KeyEvent event) {
     // System.out.println(" enter maze's keyDown");
    //  Log.v("PlayMaze","In playmaze's keydown");
      super.onKeyDown(keyCode, event);
      Log.v("PlayMaze","robotName is "+robotName+"see how many spaces");
      if(keyCode == KeyEvent.KEYCODE_M){
          maze.map_mode = !maze.map_mode;
          maze.redrawPlay();
          gw.invalidate();
          
      }
      if(keyCode == KeyEvent.KEYCODE_PLUS){
    	  maze.mapdrawer.incrementMapScale();
    	  gw.invalidate();
      }
      if(keyCode == KeyEvent.KEYCODE_MINUS){
    	  maze.mapdrawer.decrementMapScale();
    	  gw.invalidate();
      }
      if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT) && (robotName.equalsIgnoreCase("Manual"))){
          maze.rotate(1);
          maze.energyUsed+= 5;

          //maze.redrawPlay();
          gw.invalidate();
          return true;
      }
      if ((keyCode == KeyEvent.KEYCODE_DPAD_UP) && (robotName.equalsIgnoreCase("Manual"))){
          maze.walk(1);
          maze.totalTravel++;
          maze.energyUsed+= 7;

          //maze.redrawPlay();
          gw.invalidate();
          if (maze.state == 4){
              startActivity(finishIntent);
              finish();
          }
          return true;
      }
      if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) && (robotName.equalsIgnoreCase("Manual"))){
          maze.rotate(-1);
          maze.energyUsed+= 5;

          gw.invalidate();
          if (maze.state == 4){
              startActivity(finishIntent);
              finish();
          }
          return true;
      }
      if ((keyCode == KeyEvent.KEYCODE_DPAD_DOWN) && (robotName.equalsIgnoreCase("Manual"))){
          maze.walk(-1);
          maze.totalTravel++;
          maze.energyUsed+= 7;
          gw.invalidate();
          if (maze.state == 4){
              startActivity(finishIntent);
              finish();
          }
          return true;
      }

      if (maze.state == 4){
         
          finishIntent.putExtra("totalTravel", maze.totalTravel);
          startActivity(finishIntent);
          finish();
      }
//      switch (state) {
//      // if screen shows title page, keys describe level of expertise
//      // create a maze according to the user's selected level
//      case STATE_TITLE:
//          if (key >= '0' && key <= '9') {
//              build(key - '0');
//              break;
//          }
//          if (key >= 'a' && key <= 'f') {
//              build(key - 'a' + 10);
//              break;
//          }
//          break;
//      // if we are currently generating a maze, recognize interrupt signal (ESCAPE key)
//      // to stop generation of current maze
//      case STATE_GENERATING:
//          if (key == ESCAPE) {
//              mazebuilder.Interrupt();
//              buildInterrupted();
//          }
//          break;
//       if user explores maze,
//       react to input for directions and interrupt signal (ESCAPE key)
//       react to input for displaying a map of the current path or of the overall maze (on/off toggle switch)
//       react to input to display solution (on/off toggle switch)
//       react to input to increase/reduce map scale
//      case STATE_PLAY:
//          switch (key) {
//          case Event.UP: case 'k': case '8':
//              walk(1);
//              break;
//          case Event.LEFT: case 'h': case '4':
//              rotate(1);
//              break;
//          case Event.RIGHT: case 'l': case '6':
//              rotate(-1);
//              break;
//          case Event.DOWN: case 'j': case '2':
//              walk(-1);
//              break;
//          case ESCAPE: case 65385:
//              if (solving)
//                  solving = false;
//              else
//                  state = STATE_TITLE;
//              redraw();
//              break;
//          case ('w' & 0x1f):
//          {
//              setCurrentPosition(px + dx, py + dy) ;
//              redraw();
//              break;
//          }
//          case '\t': case 'm':
//              map_mode = !map_mode; redraw(); break;
//          case 'z':
//              showMaze = !showMaze; redraw(); break;
//          case 's':
//              showSolution = !showSolution; redraw(); break;
//          case ('s' & 0x1f):
//              if (solving)
//                  solving = false;
//              else {
//                  solving = true;
//                  invalidate();
//                  //repaint(25);
//              }
//          break;
//          case '+': case '=':
//          {
//              if (mapdrawer != null)
//              {
//                  mapdrawer.incrementMapScale() ;
//                  redraw() ;
//              }
//              // else ignore
//              break ;
//          }
//          case '-':
//              if (mapdrawer != null)
//              {
//                  mapdrawer.decrementMapScale() ;
//                  redraw() ;
//              }
//              // else ignore
//              break ;
//          }
//          break;
      // if we are finished, return to initial state with title screen
    //  case STATE_FINISH:
    //      state = STATE_TITLE;
    //      redraw();
    //      break;
    //  }
  return true;
}}