package edu.wm.cs.cs301.UI;

//import java.awt.event.KeyListener;
import java.math.*;


/**
 *
 * @author dbvinega
 *
 *Instantiate an implemenation of a Robot object.  Declares global variables, such as the battery life.
 */
public class BasicRobot implements Robot{
 //
    Maze maze ;
//    KeyListener kl ;
 
   // int totalTravel = 0;
    int sensors;
    int batteryLife = 2500;
    int battery = 2500;
    Cells cells;
    boolean hitWall = false;
   
 /**
  *
  * @param maze: gives each BasicRobot a particular maze to operate in.
  * Initializes number of sensors and the battery.
  */
    public BasicRobot(Maze maze){
        this.maze = maze;
        this.sensors = 2;
        this.battery = batteryLife - sensors;
    }
/**
 * Obligatory empty constructor.
// */
//    public BasicRobot() {
//        // TODO Auto-generated constructor stub
//    }
 
    // ** Check robot specifications
    /**
     * Responsible for rotating a basicrobot a specified number of degrees.  Also decrements the battery and increments energy used.
     */
    @Override
    public void rotate(int degree) throws UnsupportedArgumentException {
  
        System.out.println("enter BasicRobot.rotate: " + degree);
        System.out.println(maze.dx);
   
        // Find the amount energy used by scaling down energy for full rotation. Subtract from battery
        float energyUsed = Math.abs((degree/360)) * getEnergyForFullRotation()/4;      // FLOAT OR INT?
        battery = (int) (battery - energyUsed);
        System.out.println("battery is " + battery);
        int curdx = maze.dx;
        int curdy = maze.dy;
       
        if (!hasStopped()){
            System.out.println("enter !hasStopped condition");

            int directionChange = degree/90;
            maze.rotate(directionChange);

        }
        else{
            System.out.println("Robot out of battery");
        }
 
        if ( (maze.dx != curdx) || (maze.dy != curdy)){
            battery = (int) (battery - ((getEnergyForFullRotation())/4)-sensors);
            //maze.totalTravel += 3;
            maze.energyUsed = batteryLife-battery;
            System.out.println("Energy used is "+ maze.energyUsed +" and totalTravel is " + maze.totalTravel);
        }
    }

    /**
     * Responsible for moving by a specified distance forward or backward.  Decrements battery, increments energy used, and increments totalTravel.
     */
    @Override
    public void move(int distance, boolean forward) throws HitObstacleException {
  
        System.out.println("enter BasicRobot.move: " + distance + " " + forward);
   
        int direction;
        if (forward){
            direction = 1;
            }
        else{
            direction = -1;
            }
   
        int curpx = maze.px;
        int curpy = maze.py;
        int distanceMoved = 0;
        for (int move = 0; move < distance; move ++){
            if (!hasStopped()){
                  if(maze.walk(direction)){
                      distanceMoved += 1;
                  }
                  else{
                      hitWall = true;
                  }
            }
            // If no more battery left, break for loop. print.
            else{
                System.out.println("Robot has stopped");
                break;
            }
        }
   
        if ( (maze.px != curpx) || (maze.py != curpy)){
            battery = (int) (battery - (distanceMoved * getEnergyForStepForward())-sensors);
            maze.totalTravel += distanceMoved;
            maze.energyUsed = batteryLife -battery;
        }
        System.out.println("Energy used is "+ maze.energyUsed +" and totalTravel is " + maze.totalTravel);

        // do htobstacleexceptiongetEnergyForFullRotation()
        //check for energy
        //check if forward or backward
        //check if there is a wall in certain number of spaces forward/backward

    }
   
    /**
     * Subtracts current battery from intital batterylife.
     */
    public int energyUsed(){
        return batteryLife - battery;
    }
    public int pathLength(){
        return maze.totalTravel;
    }
 
    /**
     * Returns the robots current position in the maze.
     */
    @Override
    public int[] getCurrentPosition() {
        int[] currPos = new int[2];
        currPos[0] = maze.px;
        currPos[1] = maze.py;
        return currPos;
    }
    /**
     * Returns true if the robot is at the exit.
     */
    @Override
    public boolean isAtGoal() {
        if (maze.isEndPosition(maze.px, maze.py)){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Returns the direction the robot is facing as a coordinate.
     */
    @Override
    public int[] getCurrentDirection() {
        int[] currDir = new int[2];
        currDir[0] = maze.dx;
        currDir[1] = maze.dy;
        return currDir;
    }

    /**
     * Returns the current battery life.
     */
    @Override
    public float getCurrentBatteryLevel() {
        return battery;
    }

    /**
     * Returns the amount of energy it takes to rotate 360 degrees.
     */
    @Override
    public float getEnergyForFullRotation() {
        return 12;
    }

    /**
     * returns teh amount of energy it takes to move one step forward.
     */
    @Override
    public float getEnergyForStepForward() {
        return 5;
    }
    /**
     * Returns true if the robot has stopped due to running out of energy or hitting a wall.
     * Hitting a wall causes battery to be set to 0.
     */
    @Override
    public boolean hasStopped() {
        if(battery<=0)

            return true;
        
        else if (this.hitWall){
        	return true;
        }
        return false;
    }

    /**
     * Returns true if the robot can see the goal ahead.
     */
    @Override
    public boolean canSeeGoalAhead() throws UnsupportedMethodException {
        int infinity =Integer.MAX_VALUE;
        if (this.distanceToObstacleAhead() == infinity){
            return true;
        }
        return false;
        
    }
    /**
     * Returns true if the goal is behind the robot in its back view.
     */
    @Override
    public boolean canSeeGoalBehind() throws UnsupportedMethodException {
        int infinity = Integer.MAX_VALUE;

        if (this.distanceToObstacleBehind() == infinity){
            return true;
        }
        return false;
    }

    /**
     * Returns true if the goal is in sight of the robot's left sensor.
     */
    @Override
    public boolean canSeeGoalOnLeft() throws UnsupportedMethodException {
        int infinity =Integer.MAX_VALUE;

        if (this.distanceToObstacleOnLeft() == infinity){
            return true;
        }   
        return false; 
    }

    /**
     * Returns true if the goal is in sight of the robot's right sensor.
     */
    @Override
    public boolean canSeeGoalOnRight() throws UnsupportedMethodException {
        int infinity = Integer.MAX_VALUE;

        if (this.distanceToObstacleOnRight() == infinity){
            return true;
        }   
        return false;
    }

 
    /**
     * Returns the distance to an obstacle ahead.
     */
    @Override
    public int distanceToObstacleAhead() throws UnsupportedMethodException {
        int[] masks = Cells.getMasks() ;
        int distance = 0;
        int x = maze.px;
        int y = maze.py;
      
        if(maze.dx == -1){ // and dy =0. aka moving west.
            for(x=maze.px; maze.mazecells.hasMaskedBitsFalse(x, y, masks[2]); x--){ // wall on left

                if (x==0){
                    distance = Integer.MAX_VALUE;
                    break;

                }
                distance +=1;         
              
                }
      //  System.out.println("The distnace to obstacle ahead is "+distance);
     
        return distance;
        }
   
        else if(maze.dx == 1){ // and dy =0. aka moving east.
            System.out.println("enter maze.dx is 1 = EAST");
            while(maze.mazecells.hasMaskedBitsFalse(x, y, masks[0])){ // WALL ON right
                System.out.println("entering east for loop");

//                if (breakOut){
//                    break;
//                }
                distance +=1;
                if (x==maze.mazecells.width-1){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                x += 1;
                System.out.println("distance is incremented to " + distance);  
//                if (maze.mazecells.cells[x+1][y]== maze.mazecells.width-1 || maze.mazecells.cells[x+1][y]< 0){
//                 
//                    breakOut = true;
//                }

           // System.out.println("The distnace to obstacle ahead is "+distance);
            }
        return distance;
       
      
        }
        else if(maze.dy == 1){ // and dy =0. aka moving north.
            x = maze.px;
            y = maze.py;
            while(maze.mazecells.hasMaskedBitsFalse(x, y, masks[1])){ //wall on top
                System.out.println("enter the north while loop " + y);

//                if (breakOut){
//                    break;
//                }
                distance +=1;
                if (y==maze.mazecells.height-1){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                y++;
                if (y > maze.mazecells.height - 1){
                    break;
                }
//                    if (maze.mazecells.cells[x][y+1]== maze.mazecells.height-1 || maze.mazecells.cells[x][y+1]< 0){
//                        breakOut = true;
//                        }

                }

       
         //   System.out.println("The distnace to obstacle ahead is "+distance);

        return distance;
        }
   
        else if(maze.dy == -1){ //moving south
            while(maze.mazecells.hasMaskedBitsFalse(x, y, masks[3])){ //wall on bottom
              
//                if (breakOut){
//                    break;
//                }
                distance +=1;
                if (y==0){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                y--;
                if (y < 0){
                    break;
                }
//                if (maze.mazecells.cells[x][y-1]== maze.mazecells.height || maze.mazecells.cells[x][y-1]< 0){
//
//                        breakOut = true;
//                    }

            }
        //    System.out.println("The distnace to obstacle ahead is "+distance);

       // return distance;
       
      
        return distance;
        }
      
        return distance;
    }

    /**
     * Returns distance to an obstacle on left.
     */
    @Override
    public int distanceToObstacleOnLeft() throws UnsupportedMethodException {
        int[] masks = Cells.getMasks() ;
        int distance = 0;
        int x = maze.px;
        int y = maze.py;
        boolean breakOut = false;
      
        if(maze.dx == -1){ //moving west
            while(maze.mazecells.hasMaskedBitsFalse(x, y, masks[3])){ //wall on bottom //may need to be reversed
              

                distance +=1;
                if (y==0){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                y--;
                if (y ==0){
                    break;
                }
//              

            }
     
       
      
        return distance;
        }
   
        else if(maze.dx == 1){ // and dy =0. aka moving east.
            x = maze.px;
            y = maze.py;
            while(maze.mazecells.hasMaskedBitsFalse(x, y, masks[1])){ //wall on top
                System.out.println("enter the north while loop " + y);

//                if (breakOut){
//                    break;
//                }
                distance +=1;
                if (y==maze.mazecells.height-1){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                y++;
                if (y >maze.mazecells.height-1){
                    break;
                }
//                    if (maze.mazecells.cells[x][y+1]== maze.mazecells.height-1 || maze.mazecells.cells[x][y+1]< 0){
//                        breakOut = true;
//                        }

                }

       
         //   System.out.println("The distnace to obstacle ahead is "+distance);

        return distance;
        }
        else if(maze.dy == 1){ //  aka moving north.
            for(x=maze.px; maze.mazecells.hasMaskedBitsFalse(x, y, masks[2]); x--){ // wall on east
              

                if (breakOut){
                    break;
                }
                if (x==0){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                distance +=1;
//                if (maze.mazecells.cells[x-1][y]== maze.mazecells.width-1 || maze.mazecells.cells[x-1][y]< 0){
//                    breakOut = true;
//                }                 
              
                }
      //  System.out.println("The distnace to obstacle ahead is "+distance);
     
        return distance;
        }
   
        else if(maze.dy == -1){ // aka moving south.
            System.out.println("enter maze.dx is 1 = EAST");
            while(maze.mazecells.hasMaskedBitsFalse(x, y, masks[0])){ // WALL ON west
                System.out.println("entering east for loop");

//                if (breakOut){
//                    break;
//                }
                distance +=1;
                if (x==maze.mazecells.width-1){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                x += 1;
                System.out.println("distance is incremented to " + distance);  
//                if (maze.mazecells.cells[x+1][y]== maze.mazecells.width-1 || maze.mazecells.cells[x+1][y]< 0){
//                 
//                    breakOut = true;
//                }

           // System.out.println("The distnace to obstacle ahead is "+distance);
            }
        return distance;
       
      
        }
      
        return distance;
    }
   
    /**
     * Returns the distance to an obstacle on the robots right sensor.
     */
    @Override
    public int distanceToObstacleOnRight() throws UnsupportedMethodException {
        int[] masks = Cells.getMasks() ;
        int distance = 0;
        int x = maze.px;
        int y = maze.py;
        boolean breakOut = false;
      
        if(maze.dx == -1){ //moving west
            while(maze.mazecells.hasMaskedBitsFalse(x, y, masks[1])){ //wall on bottom //may need to be reversed
              

                distance +=1;
                if (y==maze.mazecells.height-1){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                y++;
                if (y ==maze.mazecells.height-1){
                    break;
                }
//              

            }
     
       
      
        return distance;
        }
   
        else if(maze.dx == 1){ // and dy =0. aka moving east.
            x = maze.px;
            y = maze.py;
            while(maze.mazecells.hasMaskedBitsFalse(x, y, masks[3])){ //wall on top
                System.out.println("enter the north while loop " + y);

//                if (breakOut){
//                    break;
//                }
                distance +=1;
                if (y==0){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                y--;
                if (y <0){
                    break;
                }
//                    if (maze.mazecells.cells[x][y+1]== maze.mazecells.height-1 || maze.mazecells.cells[x][y+1]< 0){
//                        breakOut = true;
//                        }

                }

       
         //   System.out.println("The distnace to obstacle ahead is "+distance);

        return distance;
        }
        else if(maze.dy == 1){ //  aka moving north.
            for(x=maze.px; maze.mazecells.hasMaskedBitsFalse(x, y, masks[0]); x++){ // wall on east
              

                if (breakOut){
                    break;
                }
                if (x==maze.mazecells.width-1){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                distance +=1;
//                if (maze.mazecells.cells[x-1][y]== maze.mazecells.width-1 || maze.mazecells.cells[x-1][y]< 0){
//                    breakOut = true;
//                }                 
              
                }
      //  System.out.println("The distnace to obstacle ahead is "+distance);
     
        return distance;
        }
   
        else if(maze.dy == -1){ // aka moving south.
            System.out.println("enter maze.dx is 1 = EAST");
            while(maze.mazecells.hasMaskedBitsFalse(x, y, masks[2])){ // WALL ON west
                System.out.println("entering east for loop");

//                if (breakOut){
//                    break;
//                }
                distance +=1;
                if (x==0){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                x -= 1;
                System.out.println("distance is incremented to " + distance);  
//                if (maze.mazecells.cells[x+1][y]== maze.mazecells.width-1 || maze.mazecells.cells[x+1][y]< 0){
//                 
//                    breakOut = true;
//                }

           // System.out.println("The distnace to obstacle ahead is "+distance);
            }
        return distance;
       
      
        }
      
        return distance;
    }

    /**
     * Returns the distance to the nearest obstacle behind the robot.
     */
    @Override
    public int distanceToObstacleBehind() throws UnsupportedMethodException {
        int[] masks = Cells.getMasks() ;
        int distance = 0;
        int x = maze.px;
        int y = maze.py;
        boolean breakOut = false;
      
        if(maze.dx == -1){ // and dy =0. aka moving west.
            for(x=maze.px; maze.mazecells.hasMaskedBitsFalse(x, y, masks[0]); x++){ // wall on east
              

                if (breakOut){
                    break;
                }
                if (x==maze.mazecells.width-1){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                distance +=1;
//                if (maze.mazecells.cells[x-1][y]== maze.mazecells.width-1 || maze.mazecells.cells[x-1][y]< 0){
//                    breakOut = true;
//                }                 
              
                }
      //  System.out.println("The distnace to obstacle ahead is "+distance);
     
        return distance;
        }
   
        else if(maze.dx == 1){ // and dy =0. aka moving east.
            System.out.println("enter maze.dx is 1 = EAST");
            while(maze.mazecells.hasMaskedBitsFalse(x, y, masks[2])){ // WALL ON west
                System.out.println("entering east for loop");

//                if (breakOut){
//                    break;
//                }
                distance +=1;
                if (x==0){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                x -= 1;
                System.out.println("distance is incremented to " + distance);  
//                if (maze.mazecells.cells[x+1][y]== maze.mazecells.width-1 || maze.mazecells.cells[x+1][y]< 0){
//                 
//                    breakOut = true;
//                }

           // System.out.println("The distnace to obstacle ahead is "+distance);
            }
        return distance;
       
      
        }
        else if(maze.dy == 1){ // and dy =0. aka moving north.
            x = maze.px;
            y = maze.py;
            while(maze.mazecells.hasMaskedBitsFalse(x, y, masks[3])){ //wall on top
                System.out.println("enter the north while loop " + y);

//                if (breakOut){
//                    break;
//                }
                distance +=1;
                if (y==0){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                y--;
                if (y <0){
                    break;
                }
//                    if (maze.mazecells.cells[x][y+1]== maze.mazecells.height-1 || maze.mazecells.cells[x][y+1]< 0){
//                        breakOut = true;
//                        }

                }

       
         //   System.out.println("The distnace to obstacle ahead is "+distance);

        return distance;
        }
   
        else if(maze.dy == -1){ //moving south
            while(maze.mazecells.hasMaskedBitsFalse(x, y, masks[1])){ //wall on bottom //may need to be reversed
              

                distance +=1;
                if (y==maze.mazecells.height-1){
                    distance = Integer.MAX_VALUE;
                    break;
                }
                y++;
                if (y ==maze.mazecells.height-1){
                    break;
                }
//              

            }
     
       
      
        return distance;
        }
      
        return distance;
    }
}