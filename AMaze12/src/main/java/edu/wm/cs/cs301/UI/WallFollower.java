package edu.wm.cs.cs301.UI;

//ENERGYUSED MAY BE OFF BY A BIT
//DISTANCE TRAVELED IS PERFECT

/**
 * The WallFollower class implements an algorithm that guides a robot to the end of a maze by following the
 * wall on the robot's left hand side.  
 * @author guest
 *
 */
public class WallFollower implements RobotDriver {
	boolean isFollowingWall = true;
    BasicRobot robot;
    int distanceCounter = 0;
    /**
     * Sets the robot to be a robot with preconfigured BasicRobot configurations.
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
     * Implements the wallfollower algorithm.
     */
    @Override
    public boolean drive2Exit() throws Exception {
       robot.battery =2500;
       robot.batteryLife =2500;
       robot.sensors = 2;
       robot.battery -= robot.sensors;
       robot.maze.totalTravel = 0;
       int notFollowingWall = 0;
       

       // keep track of number of rotatations since last moved forward.
       int turns=0;
        while (true){
        	if (robot.maze.isEndPosition(robot.maze.px, robot.maze.py)){
                break;
            }
        	System.out.println("in while loop");
        	if (robot.distanceToObstacleOnLeft() !=0  && robot.distanceToObstacleAhead()!=0 && turns==1){
        		
        		robot.move(1,true);
        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

        		distanceCounter++;
        		// only increment notFollowingWall when obstacleOnleft != 0 AND  robot is moving forward.
        		notFollowingWall ++;
        		if (notFollowingWall >=3){
        			isFollowingWall = false;
        		}
        		turns = 0;
                robot.battery = (int) (robot.battery - robot.getEnergyForStepForward() - robot.sensors);

        	}
        	if (robot.distanceToObstacleOnLeft() == 0  && robot.distanceToObstacleAhead()!=0){
        		notFollowingWall = 0;
            	robot.move(1, true);
        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

        		distanceCounter++;

            	turns =0;
                robot.battery = (int) (robot.battery - robot.getEnergyForStepForward() - robot.sensors);

        	}else if(robot.distanceToObstacleOnLeft() != 0  && robot.distanceToObstacleAhead()==0){
        		robot.rotate(90);
        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

        		turns ++;
        		robot.battery = (int) (robot.battery - (robot.getEnergyForFullRotation()/4) -robot.sensors);  

        	}
        		else if(robot.distanceToObstacleOnLeft() == 0  && robot.distanceToObstacleAhead()==0){
        			
        			robot.rotate(90);
            		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

        			turns ++;
        			notFollowingWall = 0;
            		robot.battery = (int) (robot.battery - (robot.getEnergyForFullRotation()/4) -robot.sensors);  

        		}
        		else if(robot.distanceToObstacleOnLeft() != 0  && robot.distanceToObstacleAhead()!=0){
        			robot.rotate(90);
            		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

        			turns ++;
            		robot.battery = (int) (robot.battery - (robot.getEnergyForFullRotation()/4) -robot.sensors);  

        			//robot.move(1, true);
        		}
        	if (turns ==3){
        		robot.move(1, true);
        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);
        		distanceCounter++;

        		turns =0;
                robot.battery = (int) (robot.battery - robot.getEnergyForStepForward() - robot.sensors);

        		
        	}
            
           
            if (robot.maze.state == 1){
                break;
            }
            if (robot.maze.isEndPosition(robot.maze.px, robot.maze.py)){
                break;
            }
            if (robot.battery <= 0){
                return false;
            }
        } // end of while
        robot.maze.energyUsed = robot.batteryLife - robot.battery;
        
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
     * Sets robot with preconfigured Robot settings.
     */
    @Override
    public void setRobot(Robot r) throws UnsuitableRobotException {
        // TODO Auto-generated method stub
      
    }

}