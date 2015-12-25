package edu.wm.cs.cs301.UI;

/**
 * Implements the Wizard algorithm to drive a robot through a maze.  
 * @author guest
 *
 */
public class Wizard implements RobotDriver {

    BasicRobot robot;
  
    /**
     * Configures a robot with predefined BasicRobot settings.
     * @param r
     * @throws UnsuitableRobotException
     */
    public void setRobot(BasicRobot r) throws UnsuitableRobotException {
        robot = r;
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

    /**
     * Implements the Wizard algorithm.  The wizard algorithm uses the distance matrix from Maze.java through
     * the Maze.java's solvestep method.  This moves the robot one step closer to the exit every time.
     */
    @Override
    public boolean drive2Exit() throws Exception {
       robot.battery =2500;
       robot.batteryLife =2500;
       robot.sensors = 2;
       robot.battery -= robot.sensors;
       robot.maze.totalTravel = 0;
       // while (true){

            int rotations = robot.maze.solveStep(); //moves one step closer to exit and returns the number of times it had to rotate to get to appropriate step
            System.out.println("battery is " + robot.battery );
            robot.maze.totalTravel ++;
            if (rotations == 0){
                robot.battery = (int) (robot.battery - robot.getEnergyForStepForward() - robot.sensors);
               
            }
            else if (rotations ==1){
                robot.battery = (int) (robot.battery - (robot.getEnergyForFullRotation()/4) -robot.sensors);
                robot.battery = (int) (robot.battery - robot.getEnergyForStepForward() - robot.sensors);

            }
            else{ //rotations = 2

                    robot.battery = (int) (robot.battery - (robot.getEnergyForFullRotation()/2) -(robot.sensors*2));
                    robot.battery = (int) (robot.battery - robot.getEnergyForStepForward() - robot.sensors);

                }
           
            if (robot.maze.state == 1){
                //break;
            }
            if (robot.maze.isEndPosition(robot.maze.px, robot.maze.py)){
                return false;
            }
            if (robot.battery <= 0){
                return false;
            }
        //} // end of while
        robot.maze.energyUsed = robot.batteryLife - robot.battery;
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

    /**
     * Sets the robot with preconfigured Robot settings.
     */
    @Override
    public void setRobot(Robot r) throws UnsuitableRobotException {
        // TODO Auto-generated method stub
      
    }

}