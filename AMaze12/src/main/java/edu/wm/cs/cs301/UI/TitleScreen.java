package edu.wm.cs.cs301.UI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class TitleScreen extends Activity {

	Spinner robotSpinner;
	Spinner levelSpinner;
	Spinner genAlgoSpinner;
	RadioButton loadFileButton;
	String level;
	String challenge;
	String genAlgo;
	String robotName;
	
	int complexity;
	Spinner challengeSpinner;
	private static int REQUEST_CODE = 10;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);
        Button PlayButton = (Button) findViewById(R.id.button1);
        robotSpinner = (Spinner) findViewById(R.id.spinner1);
        levelSpinner = (Spinner) findViewById(R.id.spinner2);
        genAlgoSpinner = (Spinner) findViewById(R.id.spinner3);
        loadFileButton = (RadioButton)findViewById(R.id.radioButton1);
        challengeSpinner =(Spinner) findViewById(R.id.spinner4);

        ArrayAdapter<CharSequence> driverAdapter = ArrayAdapter.createFromResource(this,
             R.array.RobotDrivers, android.R.layout.simple_spinner_item);
        
        
        ArrayAdapter<CharSequence> challengeAdapter = ArrayAdapter.createFromResource(this, 
              	 R.array.Challenges, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(this, 
        	 R.array.Complexity, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> algoAdapter = ArrayAdapter.createFromResource(this, 
           	 R.array.Algorithms, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        driverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        algoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        challengeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        robotSpinner.setAdapter(driverAdapter);
        levelSpinner.setAdapter(levelAdapter);
        genAlgoSpinner.setAdapter(algoAdapter);
        challengeSpinner.setAdapter(challengeAdapter);

      
        // Create toasts used in the title screen
//        Context titleContext = getApplicationContext();
//        CharSequence driverText = "Hello toast!";
//        int duration = Toast.LENGTH_SHORT;
//
//        Toast driverToast = Toast.makeText(titleContext, driverText, duration);
//        toast.show();
    	
        
        // taken from http://stackoverflow.com/questions/4923310/android-spinner-onitemselected-setonitemselectedlistener-not-triggering
    		robotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
             public void onItemSelected(AdapterView<?> parent, View view, 
                        int pos, long id) {
                    // An spinnerItem was selected. You can retrieve the selected item using
                    // parent.getItemAtPosition(pos)
            	 	 String robot = (String) parent.getItemAtPosition(pos);
            	 	 robotName = robot;
            	 	
            	 	Log.v("TitleScreen", "User selected " + robot);
                    //Toast.makeText(TitleScreen.this, robot,Toast.LENGTH_SHORT).show();

                }

                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing, just another required interface callback
                }

        });
    		challengeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				/**
                 * Called when a new item is selected (in the Spinner)
                 */
                 public void onItemSelected(AdapterView<?> parent, View view, 
                            int pos, long id) {
                        // An spinnerItem was selected. You can retrieve the selected item using
                        // parent.getItemAtPosition(pos)
                	 	challenge = (String) parent.getItemAtPosition(pos);
                	 	
                	 	Log.v("TitleScreen", "User selected Challenge" + challenge);
                      //  Toast.makeText(TitleScreen.this, genAlgo,Toast.LENGTH_SHORT).show();

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // Do nothing, just another required interface callback
                    }

            });
    		levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                /**
                 * Called when a new item is selected (in the Spinner)
                 */
                 public void onItemSelected(AdapterView<?> parent, View view, 
                            int pos, long id) {
                        // An spinnerItem was selected. You can retrieve the selected item using
                        // parent.getItemAtPosition(pos)
                	 	level = (String) parent.getItemAtPosition(pos);
                	 	complexity = Integer.parseInt(level);
                	 	
                	 	Log.v("TitleScreen", "User selected " + complexity);
                       // Toast.makeText(TitleScreen.this, level,Toast.LENGTH_SHORT).show();
                        

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // Do nothing, just another required interface callback
                    }

            });
    		
    		genAlgoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                /**
                 * Called when a new item is selected (in the Spinner)
                 */
                 public void onItemSelected(AdapterView<?> parent, View view, 
                            int pos, long id) {
                        // An spinnerItem was selected. You can retrieve the selected item using
                        // parent.getItemAtPosition(pos)
                	 	genAlgo = (String) parent.getItemAtPosition(pos);
                	 	
                	 	Log.v("TitleScreen", "User selected " + genAlgo);
                      //  Toast.makeText(TitleScreen.this, genAlgo,Toast.LENGTH_SHORT).show();

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // Do nothing, just another required interface callback
                    }

            });
    		
    		Maze challengeMaze1 = new Maze();
        	challengeMaze1.init();
        	//MazeBuilder mazebuilderSetUp= new MazeBuilder(false, 0);
        	//int[] params = mazebuilderSetUp.setUpBuild("Kruskal", complexity);
        	challengeMaze1.build(0);
        	SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        	SharedPreferences.Editor ed = mPrefs.edit();
        	ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(); 
        	ObjectOutputStream out;
        	try {
        	    out = new ObjectOutputStream(arrayOutputStream);
        	    out.writeObject(challengeMaze1);
        	    out.close();
        	    arrayOutputStream.close();
        	} catch (IOException e) {
        	    e.printStackTrace();
        	}
        	ed.putString("challengeMaze1", arrayOutputStream.toString());
        	ed.commit();
    }
    
    public void switchToGenerate(View view){
    	Log.v("TitleScreen", "User generates maze");
    	Log.v("TitleScreen", "passing in "+ complexity);
    	//Toast.makeText(TitleScreen.this, "Generating maze", Toast.LENGTH_SHORT).show();
    	
    	Intent generatingIntent = new Intent(getApplicationContext(), GeneratingScreen.class);
    	generatingIntent.putExtra("level", complexity);
    	generatingIntent.putExtra("genAlgo", genAlgo);
    	generatingIntent.putExtra("robotName", robotName);
    	startActivity(generatingIntent);
    	finish();
    }
    
    public void resetConfig(View view){
    	robotSpinner.setSelection(0);
    	levelSpinner.setSelection(0);
    	genAlgoSpinner.setSelection(0);
    	
    	CheckBox checkbox1 = (CheckBox)findViewById(R.id.checkBox1);
    	checkbox1.setChecked(false);
    	CheckBox checkbox2 = (CheckBox)findViewById(R.id.checkBox2);
    	checkbox2.setChecked(false);
    	CheckBox checkbox3 = (CheckBox)findViewById(R.id.checkBox3);
    	checkbox3.setChecked(false);
    	
    	loadFileButton.setChecked(false);
    	
    	Log.v("TitleScreen", "User resets default configuarations, set spinners, checkboxes to default, unchecked");
    	//Toast.makeText(TitleScreen.this, "Reset default configurations", Toast.LENGTH_SHORT).show();

    }
    
    public void onRadioClicked(View view){
    	
    	Log.v("title", "load existing maze");
    	Toast.makeText(TitleScreen.this, "Load existing maze from file", Toast.LENGTH_SHORT).show();
    }
    
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox1:
                if (checked)
                	Log.v("TitleScreen", "User checked 'Show whole maze from above'");
                	//Toast.makeText(TitleScreen.this, "Show whole maze from above", Toast.LENGTH_SHORT).show();
                	
                break;
            case R.id.checkBox2:
                if (checked)
                	Log.v("TitleScreen", "User check 'Show solution in maze'");
                	//Toast.makeText(TitleScreen.this, "Show solution in maze", Toast.LENGTH_SHORT).show();
                      	
                break;
            case R.id.checkBox3:
            	if (checked)
            		Log.v("TitleScreen", "User check 'Show visible walls'");
                	//Toast.makeText(TitleScreen.this, "Show visible walls", Toast.LENGTH_SHORT).show();
            		
                break;
           
        }
    }
    
}
