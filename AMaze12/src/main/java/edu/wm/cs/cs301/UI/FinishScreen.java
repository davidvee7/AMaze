package edu.wm.cs.cs301.UI;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class FinishScreen extends Activity {
	final String MYPREFS = "MyPreferences001";
	SharedPreferences prefs;
	SharedPreferences.Editor myEditor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_screen);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);//this.getSharedPreferences("edu.wm.cs.cs301.UI.FinishScreen", Context.MODE_PRIVATE);
        //read prefs
        int[] levels = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};//all the way through f=16
        String levelString = "";
        for (int x: levels){
        	if (PlayMaze.level== x){
        		StringBuilder sb = new StringBuilder();
        		sb.append("");
        		sb.append(x);
        		levelString = sb.toString();
        	}
        }
        
        String timesCompletedKey = "edu.wm.cs.cs301.UI.FinishScreen.timesCompleted"+ levelString; //= "edu.wm.cs.cs301.UI.FinishScreen.timesCompleted";
        String shortestPathKey ="edu.wm.cs.cs301.UI.FinishScreen.shortestPath"+ levelString;
        String leastEnergyKey ="edu.wm.cs.cs301.UI.FinishScreen.leastEnergy"+ levelString;
        int shortestPath = prefs.getInt(shortestPathKey, Integer.MAX_VALUE);
        int leastEnergy = prefs.getInt(leastEnergyKey, Integer.MAX_VALUE);
        int timesCompleted = prefs.getInt(timesCompletedKey, 0);
        //edit prefs
        
        
        
        timesCompleted++;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(timesCompletedKey, timesCompleted);
        if(PlayMaze.maze.totalTravel<shortestPath){
        	shortestPath = PlayMaze.maze.totalTravel;
        	editor.putInt(shortestPathKey, shortestPath);
        }
        if(PlayMaze.maze.energyUsed<leastEnergy){
        	leastEnergy = PlayMaze.maze.energyUsed;
        	editor.putInt(leastEnergyKey, leastEnergy);
        }
        editor.commit();
        
        //prefs.edit().putInt(timesCompletedKey, timesCompleted);
//        myEditor = timesCompleted.edit();
//        if (timesCompleted != null){
//        	applySavedPreferences();
//        }
//        else{
//        	
//        }
        Toast.makeText(FinishScreen.this, timesCompleted +" people have finished this game.", Toast.LENGTH_LONG).show();
        Toast.makeText(FinishScreen.this, shortestPath +" is the shortest distance travelled to the finish.", Toast.LENGTH_LONG).show();
        Toast.makeText(FinishScreen.this, leastEnergy +" is the least amount of energy used to get to the finish.", Toast.LENGTH_LONG).show();

        Toast.makeText(FinishScreen.this, "Energy Used is "+ PlayMaze.maze.energyUsed, Toast.LENGTH_LONG).show();
    	Toast.makeText(FinishScreen.this, "Total Travel is "+ PlayMaze.maze.totalTravel, Toast.LENGTH_LONG).show();

        Toast.makeText(FinishScreen.this, "Energy Used is "+ PlayMaze.maze.energyUsed, Toast.LENGTH_LONG).show();
        Toast.makeText(FinishScreen.this, "Total Travel is "+ PlayMaze.maze.totalTravel, Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_finish_screen, menu);
        return true;
    }
   
    public void switchToTitle(View view){
        Intent titleIntent = new Intent(this, TitleScreen.class);
        Log.v("FinishScreen", "User switched to title screen");
        Toast.makeText(FinishScreen.this, "Switch to title screen", Toast.LENGTH_SHORT).show();
        startActivityForResult(titleIntent, 10);
    }
}