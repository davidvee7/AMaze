package edu.wm.cs.cs301.UI;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


public class GeneratingScreen extends Activity {
	Intent PlayIntent;
	Intent backToTitleIntent;
	ProgressBar mProgress;
	boolean backToTitle = false;
	MazeBuilder mazebuilder;
	MazeBuilder mazebuilderSetUp;
	Maze maze;
	int complexity;
	int[] params;
	String robotName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_generating_screen);
        //Intent intent = getIntent();     
        
        backToTitleIntent = new Intent(this, TitleScreen.class);
        PlayIntent = new Intent(this, PlayMaze.class);
        
        // Get the parameters from TitleScreen's intent
        Bundle parameters = getIntent().getExtras();
        
        String mazeGenAlgo = parameters.getString("genAlgo");
        robotName = parameters.getString("robotName");
      //  Log.v("GeneratingScreen", "algo is "+ mazeGenAlgo);
        
       final int complexity = parameters.getInt("level");
        Log.v("GeneratingScreen", "level is " + complexity);
        String challenge = parameters.getString("challenge");

     //   mProgress = (ProgressBar) findViewById(R.id.progressBar1); 
      //  generateMaze(mazeGenAlgo, complexity);
        
     //   PlayIntent.putExtra("complexity", complexity);
     //   PlayIntent.putExtra("algo", mazeGenAlgo);
        PlayIntent.putExtra("challenge", challenge);
        PlayIntent.putExtra("robotName", robotName);
        
       // PlayIntent = new Intent(this, PlayMaze.class);
     //   Log.v("genScreen", "intent made");
       
       // mazebuilderSetUp = new MazeBuilder(true, complexity);
        
       // int[] params = mazebuilderSetUp.setUpBuild(mazeGenAlgo, complexity);
    //    Log.v("genScreen", "params set");
        
        maze = new Maze();
     //   Log.v("genScreen", "maze made");
        Log.v("genScreen", "maze init");
        maze.init();
        maze.build(complexity);
        
//        int buildCount = 0;
        while(maze.mazebuilder.buildThread.isAlive()){
//        	mProgress.setProgress(buildCount);
//        	buildCount++;
        	continue;
        }
        
//        Thread thread = new Thread() {
//        	
//        	public void run() {
//        		while (true){
//        			Log.v("genScreen", " percent "+ maze.percentdone);
//        			mProgress.setProgress(maze.percentdone);
//        		}        		
//        	}
//        	
//        };
//        maze.build(complexity);
//        thread.start();
//        Log.v("genScreen", "thread done");
//        try {
//			maze.mazebuilder.buildThread.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
 
//
//          Thread thread = new Thread(new Runnable() {
//        	  public void run(){
//        		  
//        		  Log.v("genscreen", "in run");
//        		  Log.v("genscreen", "complexity "+ complexity);
//        		  maze.build(complexity);
//        	//	  while(maze.mazebuilder.buildThread.isAlive()){
//        		  Log.v("genscreen", "setprogress of bar");
//        			  mProgress.setProgress(maze.percentdone);
//        		//	  continue;
//        		//  }	  
//        		  
//        	  }
//        	  
//        	  
//     	    
//          });
//          
//          Log.v("genscreen", "start thread ");
//          thread.start();
//          
//          
//          try {
//			thread.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
          
        //  Log.v("genscreen", "put in maze and start playintent");
          PlayIntent.putExtra("maze", maze);
        //  startActivity(PlayIntent);
          
    	
          
//        
//        maze.build(0, params);
//        while (maze.mazebuilder.buildThread.isAlive()){
//        	continue;
//        }

//        
//        Log.v("genScreen", "finished building maze");
        
//        if (mazebuilder.cells.hasWallOnRight(2,2)){
//        	Log.v("genScreen", "has wall on Right");
//        }
//        else{
//        	Log.v("genScreen", "not wall on Right");
//        }
   //     Bundle bundle = new Bundle();
        
               
      //  PlayIntent.putExtra("maze", bundle);
        //PlayIntent.putExtra("maze", maze);
     //   Log.v("genScreen", "maze put into intent");
        
      //  startActivity(PlayIntent);
      //  finish();
        ProgBar();
    }

//    	new Thread (nw Runnable)
//    	public void run() {
//    	
//    		
//    		.start();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_generating_screen, menu);
        return true;
    }
    public void backToTitle(View view){
    	//Intent goBackIntent = new Intent();
    	backToTitle = true;
    	Log.v("GeneratingScreen", "User switched to title screen");
    	Toast.makeText(GeneratingScreen.this, "Back to title screen", Toast.LENGTH_SHORT).show();
    	startActivity(backToTitleIntent);
    	finish();
    }
    
    public void generateMaze(String mazeGenAlgo, Integer complexity){
    	Log.v("generateMaze", "generating with "+ mazeGenAlgo);
        Log.v("generateMaze", "generating with " + complexity.toString());
        
             
    }
    
    public void ProgBar(){
    	
    	mProgress = (ProgressBar) findViewById(R.id.progressBar1);
    	
    	Log.v("genScreen", "building maze");
    	
    	new CountDownTimer(5000, 1000) {

    	     public void onTick(long millisUntilFinished) {
    	    	 
    	        mProgress.setProgress(100-((int)(millisUntilFinished)/100));// mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
    	     }

    	     public void onFinish() {
    	    	if (!backToTitle){
    	    		Log.v("PlayMaze", "maze generated, put into intent, move on to PlayMaze");
    	        	
    	    		startActivity(PlayIntent);
    	    	}
    	     }
    	  }.start();
	
    }
}
