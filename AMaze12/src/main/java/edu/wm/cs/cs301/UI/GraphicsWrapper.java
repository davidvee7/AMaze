package edu.wm.cs.cs301.UI;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.FontMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class GraphicsWrapper extends View {
   
    public Canvas canvas;
    public Color color;
    public Bitmap bitmap;
   // public Maze maze;
    public String stringCol;
    public Paint p;
    public int count = 0;
   public GraphicsWrapper(Context context){
	   
	   super(context);
	   setFocusableInTouchMode(true);
	   setFocusable(true);
	   p = new Paint();

	   
	   //this.canvas = new Canvas();
	   //this.maze = maze;
   }
//    public GraphicsWrapper(Maze maze) {
//    	super(context);
//        this.maze = maze;
//        Log.v("gw", "in GraphicsWrapper");
//       
//    }
//
//   void setMaze(Maze maze){
//	   this.maze = maze;
//   }
//   
   @Override
   protected void onDraw(Canvas canvas){
	   setCanvas(canvas);
	   
	   
	   
	   p.setColor(Color.DKGRAY);

	   canvas.drawRect(0,245,490,490,p);
	   
	   PlayMaze.maze.redrawPlay();
	   


//	   if(maze.firstpersondrawer != null){
//		   Log.v("gw ondraw","fpd is not null");
//	   }
	   
//	   else{
//		   Log.v("gw ondraw","fpd is  null");
//
//		 maze.start();
//		   }
	   
	   
   }
    public int getColor(String color){
       
        return Color.parseColor(color);
      
    }
    public void setColor(String color){
           
            int fieldColor= getColor(color);
//            col = fieldColor;
    	

            //canvas.drawColor(fieldColor);  
            p.setColor(fieldColor);
    }
    
    public void setColor(Integer color){
    	//canvas.drawColor(color);
    	p.setColor(color);
    }
    
  public void setCanvas(Canvas canvas){
	  this.canvas = canvas;
  }
    public void drawLine(float nx1, float ny1, float nx2, float ny2, Paint linePaint){
      //  System.out.println("drawLine is being called");
        canvas.drawLine(nx1, ny1, nx2, ny2, linePaint); //WILL HAVE TO CHANGE THIS FROM NULL?
        }
  
    public void fillOval(RectF oval, Paint ovalPaint){
      //  System.out.println("fillOval is being called");
        //RectF oval= new RectF(x,y,height,width);
       // Paint p = new Paint();
        canvas.drawOval(oval,ovalPaint);
    }
  
    public int createColor(int x, int y, int z){
        int newColor = Color.rgb(x, y, z);
        //int alpha = Color.alpha(color);
       
       
       
        //Color tempColor = Color.getColor(color);
       // gc.setColor(fieldColor);
       
        return newColor;
    }
   
//    public int createColorInt(int x, int y, int z){
//        int color = Color.rgb(x, y, z);
//        return color;
//    }
    public void fillRect(int i, int j, int view_width, int k, int red, int green, int blue) {
        int color = createColor(red,green,blue);
        p.setColor(color);
        canvas.drawRect(i, j, view_width, k, p);      
    }
    
    public void fillRect(int i, int j, int view_width, int k, String string) {
        int color = getColor(string);
        //Paint p = new Paint();
        p.setColor(color);
      //  Log.v("gw", "" + i + " " + j + "k " + k + " " + view_width + " " + p);
        canvas.drawRect(i, j, view_width, k, p);      
    }
    
    
//    public void fillPolygon(int[] xps, int[] yps, int i, int color) {
//        canvas.drawColor(color);
//      
//    }


//    public void setFont(Font largeBannerFont) {
//        gc.setFont(largeBannerFont);
//       
//    }
//
//
    public FontMetrics getFontMetrics() {
        return p.getFontMetrics();
    }


//    public void setColor(Color col2) {
//        g.setColor(col2);
//       
//    }


  
}





//package edu.wm.cs.cs301.UI;
//
////import java.awt.*;
////import java.lang.reflect.Field;
//
//public class GraphicsWrapper {
//	
////	public Graphics gc;
////	public Color col;
////    public Image buffer_img;
//    public Maze maze;
//    public String stringCol;
//
//    
//    public GraphicsWrapper(Maze maze) {
//    	this.maze = maze;
////    	buffer_img = maze.createImage(maze.view_width, maze.view_height);
////    	this.gc = buffer_img.getGraphics();
//    	
//	}
//
//
////	public Color getColor(String color){
////    	
////        return Color.getColor(color);
////       
////    }
////    public void setColor(String color){
////
////	    	Color fieldColor;
////	    	try {
////	    	    Field field = Class.forName("java.awt.Color").getField(color);
////	    	    fieldColor = (Color)field.get(null);
////	    	} catch (Exception e) {
////	    	    fieldColor = null; // Not defined
////	    	}
////	    	//Color tempColor = Color.getColor(color);
////	    	System.out.println("fieldColor is " + fieldColor);
////	        gc.setColor(fieldColor);
////    	
////    }
////   
////    public void drawLine(int nx1, int ny1, int nx2, int ny2){
////    	System.out.println("drawLine is being called");
////        gc.drawLine(nx1, ny1, nx2, ny2);
////    }
////   
////    public void fillOval(int x, int y, int height, int width){
////    	System.out.println("fillOval is being called");
////        gc.fillOval(x,y,height, width);
////    }
////   
////    public Color createColor(int x, int y, int z){
////        col = new Color(x,y,z);
////        stringCol = col.toString();
//////        Color createColor;
//////    	try {
//////    	    Field field = Class.forName("java.awt.Color").getField(createColor);
//////    	    createColor = (Color)field.get(null);
//////    	} catch (Exception e) {
//////    	    fieldColor = null; // Not defined
//////    	}
//////    	//Color tempColor = Color.getColor(color);
//////    	System.out.println("fieldColor is " + fieldColor);
//////       // gc.setColor(fieldColor);
////    	
////        return col;
////    }
////    public void fillRect(int i, int j, int view_width, int k) {
////        gc.fillRect(i, j, view_width, k);
////       
////    }
////    public void fillPolygon(int[] xps, int[] yps, int i) {
////        gc.fillPolygon(xps, yps, i);
////       
////    }
////
////
////	public void setFont(Font largeBannerFont) {
////		gc.setFont(largeBannerFont);
////		
////	}
////
////
////	public FontMetrics getFontMetrics() {
////		return gc.getFontMetrics();
////	}
////
////
////	public void setColor(Color col2) {
////		gc.setColor(col2);
////		
////	}
//
//
//   
//}