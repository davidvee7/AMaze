package edu.wm.cs.cs301.UI;

import java.util.Random;

import android.util.Log;

public class Gambler implements RobotDriver {

    BasicRobot robot;
   
    public void setRobot(BasicRobot r) throws UnsuitableRobotException {
    //	r = new BasicRobot(PlayMaze.maze);

        this.robot = r;
        System.out.println("x pos " + robot.maze.px);
        System.out.println("y pos " + robot.maze.py);
    }

    /**
     * Necessary default constructor sets a basicrobot into global variable robot. 
     * @throws UnsuitableRobotException
     */
    public void setRobot() throws UnsuitableRobotException {
    	BasicRobot r = new BasicRobot(PlayMaze.maze);
        robot = r;
        System.out.println("x pos " + robot.maze.px);
        System.out.println("y pos " + robot.maze.py);
    }
    @Override
    public boolean drive2Exit() throws Exception {
       Log.v("gambler", "inside drive2Exit");
      // Thread.currentThread().sleep(25);
        Random randNo = new Random();
        //System.out.println(robot.maze.WIDTH+ " is width"+ "and height is " + robot.maze.HEIGHT);

 //       while (true){
             // have a list of possible directions to randomly choose once they are established to be available
            boolean[] possDir = new boolean[2];
            possDir[0] = false;
            possDir[1] = false;
            if (robot.distanceToObstacleAhead() != 0){
               
                possDir[0] = true;  //can go forward
            }
            if (robot.distanceToObstacleOnLeft() != 0){
               
                possDir[1] = true;  //can go left
            }
           
            // find out if there exists a "true" direction in the array possDir
            int dirCount = 0;
            for (boolean item: possDir){
                if (item){
                    dirCount ++;
                }
            }
           
            // This random number 0 or 1 will determine which direction to go if free and also which
            // way to rotate if not free wall was found
            int dirIndex = Math.abs(randNo.nextInt()) % 2;
            int sign;
           
            if (dirCount > 0){
               
                // If available ahead, move there.
                if (possDir[0] && !possDir[1]){                  // WHEN TO MOVE BACKWARDS ??
                    robot.move(1, true);
                   // PlayMaze.gw.invalidate();
                    
                }
                else if(!possDir[0] && possDir[1]){
                    robot.rotate(90);
                   // PlayMaze.gw.invalidate();

                    robot.move(1, true);
                  //  PlayMaze.gw.invalidate();

                }
               
                // if you can go both left and forward
                else if (possDir[0] && possDir[1]){
                   
                    // if random dirIndex chose 0 - then it is free to go forward
                    if (dirIndex == 0){
                        robot.move(1, true);
                      //  PlayMaze.gw.invalidate();

                    }
                    else {
                        robot.rotate(90);
                 //       PlayMaze.gw.invalidate();

                        robot.move(1, true);
                 //       PlayMaze.gw.invalidate();

                    }

                }
            }
            // if there is not a free way ahead or to the left. choose randomly right or left rotate
            // SHOULDN'T YOU ALSO BE ABLE TO ROTATE 180 DEGREES?
            else{
                if (dirIndex == 1){
                    sign = 1;
                }
                else{
                    sign = -1;
                }
                robot.rotate(sign*90);
            //    PlayMaze.gw.invalidate();

            }
           
            // IF escape has been pressed it goes back to title so stop the robot.
//            if (robot.maze.state == 1){
//                break;
//            }
            if (robot.maze.isEndPosition(robot.maze.px, robot.maze.py)){
            //	PlayMaze.gw.invalidate();
                return false;
            }
            if (robot.battery <= 0){
            	//PlayMaze.gw.invalidate();
                return false;
            }
  //      } // end of while
        
   // PlayMaze.gw.invalidate(); 
            Thread.currentThread().sleep(500);
    return true;
       
    }

    /**
     * Returns the amount of energy consumed by the robot.
     */
    @Override
    public float getEnergyConsumption() {
    	return robot.energyUsed();
    }

    /**
     * returns the amount of distance traveled by the robot.
     */
    @Override
    public int getPathLength() {
    	return robot.maze.totalTravel;
    }

    @Override
    public void setRobot(Robot r) throws UnsuitableRobotException {
        // TODO Auto-generated method stub
       
    }

}