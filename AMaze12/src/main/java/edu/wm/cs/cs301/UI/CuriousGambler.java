package edu.wm.cs.cs301.UI;

import java.util.Random;

/**
 * Implements the CuriousGambler algorithm.  The algorithm creates a bias for a robot to go to places it 
 * has not visited before.  
 * @author guest
 *
 */
public class CuriousGambler implements RobotDriver {
	int leftBias = 0;
	int forwardBias = 0;
	int noBias = 0;
	int distanceCounter=0;
	
    BasicRobot robot;
   
    /**
     * Sets a robot to be configured with predefined BasicRobot attributes.
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
     * Drives the robot to the exit using the curiousgambler algorithm.  It has a bias to move to an 
     * adjacent cell that has been visited the fewest number of times. 
     */
    @Override
    public boolean drive2Exit() throws Exception {
        Random randNo = new Random();
        
        
        System.out.println(robot.maze.mazew+ " is width"+ "and height is " + robot.maze.mazeh);
       
        // This 2d array will keep track of number of visits to each cell.
        int[][] numVisits = new int[robot.maze.mazew+1][robot.maze.mazeh+1];
       for (int x=0; x< robot.maze.mazew; x++){
    	   for(int y=0; y< robot.maze.mazeh; y++){
    		   System.out.println("x is "+x + " y is " + y);
    		   numVisits[x][y]= 0;
    	   }
       }

       numVisits[robot.maze.px][robot.maze.py]=1;
       while (true){
    	   int visitForward = 0;
    	   int visitLeft = 0;

    	   System.out.println("made it into while");

    	   // will determine which way to go if forward and left have been visited equally.  also determiens which way to rotate if neither are free.
           int dirIndex = Math.abs(randNo.nextInt()) % 2;

              //  In order to determine the number of visits to each position on the map you must know the 
           	  // the direction the robot is currently facing.  Once direction is known, get numVisits for 
              // forward and left.  Make sure you do not index anything out of bounds.
    		   if (robot.maze.dx ==1 && robot.maze.dy ==0){ // facing east
    			   if(robot.maze.px+1 <= robot.maze.mazew){ //make sure no index out of bounds occurs
    			   visitForward = numVisits[robot.maze.px+1][robot.maze.py]; //east
    			   }
    			   else{
    				   visitForward = Integer.MAX_VALUE;
    			   }
    			   if(robot.maze.py+1 <= robot.maze.mazeh){
    			   visitLeft = numVisits[robot.maze.px][robot.maze.py+1]; //left faces north
    			   }
    			   else{
    				   visitLeft = Integer.MAX_VALUE;
    			   }
    			   
    			   //  This if else block determines which way the robot will move.
    			   if (visitLeft == visitForward){
    				   moveRobotEqualPri(dirIndex);
    			   }
    			   else if(visitLeft <visitForward){
    				   moveRobotLeftPri(dirIndex);
    			   }
    			   else{
    				   moveRobotForwardPri(dirIndex);
    			   }
    		   }
    		//  In order to determine the number of visits to each position on the map you must know the 
            // the direction the robot is currently facing.  Once direction is known, get numVisits for 
            // forward and left.  Make sure you do not index anything out of bounds.
    		   else if(robot.maze.dx ==0 && robot.maze.dy ==1){ // facing north
    			   if(robot.maze.py+1 <= robot.maze.mazeh){ //make sure no index out of bounds occurs
        			   visitForward = numVisits[robot.maze.px][robot.maze.py+1]; //east
        			   }
        			   else{
        				   visitForward = Integer.MAX_VALUE;
        			   }  
    			   if (robot.maze.px-1 >= 0){
    			   visitLeft =numVisits[robot.maze.px-1][robot.maze.py]; //faces west
    			   }
    			   else {
    				   visitLeft = Integer.MAX_VALUE;
    			   }
    			   
    			   //This if else block determines which direction the robot will go.
    			   if (visitLeft == visitForward){
    				   moveRobotEqualPri(dirIndex);
    			   }
    			   else if(visitLeft <visitForward){
    				   moveRobotLeftPri(dirIndex);  
    			   }
    			   else{
    				   moveRobotForwardPri(dirIndex);
    			   }
    		   }
    		   else if(robot.maze.dx ==-1 && robot.maze.dy ==0){ // facing west 
    			   if (robot.maze.px-1 >=0){
    			   visitForward = numVisits[robot.maze.px-1][robot.maze.py]; //west
    			   }
    			   else{
    				   visitForward = Integer.MAX_VALUE;
    			   }
    			   if (robot.maze.py-1>=0){
    			   visitLeft =numVisits[robot.maze.px][robot.maze.py-1]; //faces south
    			   }
    			   else{
    				   visitLeft = Integer.MAX_VALUE;
    			   }
    			   if (visitForward<visitLeft){ //if ahead has been visited less than on left
    				   moveRobotForwardPri(dirIndex);
    				 
    			   }
    			   else if(visitLeft<visitForward){
    				   moveRobotLeftPri(dirIndex);
    			   }
    			   else{ //forward and left have been visited equally, so pick a random direction and go there.
    				   moveRobotEqualPri(dirIndex);
    			   }
    		   }
    		   else if(robot.maze.dx ==0 && robot.maze.dy ==-1){ // facing south i think 
    			   if (robot.maze.py-1 >=0){
    			   visitForward = numVisits[robot.maze.px][robot.maze.py-1]; //south
    			   }
    			   else{
    				   visitForward = Integer.MAX_VALUE;
    			   }
    			   if(robot.maze.px+1 <=robot.maze.mazew){
    			   visitLeft =numVisits[robot.maze.px+1][robot.maze.py]; // east
    			   }
    			   else{
    				   visitLeft = Integer.MAX_VALUE;
    			   }
    			   if (visitLeft == visitForward){
    				   moveRobotEqualPri(dirIndex);
    			   }
    			   else if(visitLeft < visitForward){
    				   moveRobotLeftPri(dirIndex);
    				   
    			   }
    			   else{
    				   moveRobotForwardPri(dirIndex);

    			   }
    		   }
    	   

           
            // IF escape has been pressed it goes back to title so stop the robot.
            if (robot.maze.state == 1){
                break;
            }
            if (robot.maze.isEndPosition(robot.maze.px, robot.maze.py)){
                break;
            }
            if (robot.battery <= 0){
                return false;
            }
            numVisits[robot.maze.px][robot.maze.py] ++;
			visitLeft = 0;
			visitForward = 0;
        } // end of while
   
    return true;
       
    }
    
    /** If robot has moved to space on left fewer times than the space ahead, left has priority.  So the robot
     * will try to go left first.  Then it will try to go forward, and then it will choose a random direction
     * to rotate.
     * 
     * @param dirIndex
     * @throws UnsupportedMethodException
     * @throws UnsupportedArgumentException
     * @throws HitObstacleException
     */
    public void moveRobotLeftPri(int dirIndex) throws UnsupportedMethodException, UnsupportedArgumentException, HitObstacleException{
		   if(robot.distanceToObstacleOnLeft()!=0){ //if you can go left, go there
			   robot.rotate(90);
       		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

			   robot.move(1, true);
       		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

			   distanceCounter++;

		   }
		   else if(robot.distanceToObstacleAhead()!=0){ // if you can't go left
			   //if you can go ahead, go there
				   robot.move(1, true);
	        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

				   distanceCounter++;

			   }
		   else{
			   if (dirIndex == 0){
				   robot.rotate(90);
	        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

			   }
			   else{
				   robot.rotate(-90);
	        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

			   }
		   }
		   leftBias ++;

    }
    
    /** If robot has moved to space on ahead fewer times than the space on left, forward has priority.  So the robot
     * will try to go left first.  Then it will try to go left, and then it will choose a random direction
     * to rotate.
     * 
     * @param dirIndex
     * @throws UnsupportedMethodException
     * @throws UnsupportedArgumentException
     * @throws HitObstacleException
     */
    public void moveRobotForwardPri(int dirIndex) throws UnsupportedMethodException, HitObstacleException, UnsupportedArgumentException{
    	if(robot.distanceToObstacleAhead()!=0){ //if you can go ahead, go there
			   robot.move(1, true);
       		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

			   distanceCounter++;

		   }
		   else if (robot.distanceToObstacleOnLeft()!=0){ // if you can't go ahead but you can go left
				   robot.rotate(90); 
	        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

				   robot.move(1, true);
	        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

				   distanceCounter++;

			   }
		   else{
			   if (dirIndex ==0){
				   robot.rotate(90);
	        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

			   }
			   else{
				   robot.rotate(-90);
	        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

			   }
		   }
		   forwardBias ++;

    }
    
    /** If robot has moved to space on left equal times as space ahead, there is equal priority.  So the robot
     * will randomly choose which way to gofirst.  Then it will try to go the other direction, and then it will 
     * choose a random direction to rotate.
     * 
     * @param dirIndex
     * @throws UnsupportedMethodException
     * @throws UnsupportedArgumentException
     * @throws HitObstacleException
     */
    public void moveRobotEqualPri(int dirIndex) throws UnsupportedMethodException, HitObstacleException, UnsupportedArgumentException{
    	if (dirIndex == 0){
			   if(robot.distanceToObstacleAhead()!=0){// if you can go ahead
				   robot.move(1, true);
	        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

				   distanceCounter++;
			   }
			   else if(robot.distanceToObstacleOnLeft()!= 0){ //if you cant go ahead, go left
					   robot.rotate(90);
		        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

					   robot.move(1, true);
		        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

					   distanceCounter++;

				   }
			   else{ // if you can't go ahead or left, rotate left b/c dirIndex is 0
				   robot.rotate(90); 
	        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

			   }
		   }
		   else{
			   if(robot.distanceToObstacleOnLeft()!=0){ // if you can go left
				   robot.rotate(90);
	        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

				   robot.move(1, true);
	        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

				   distanceCounter++;

			   }
			   else if(robot.distanceToObstacleAhead()!=0){// if you can go ahead
				   robot.move(1, true);
	        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

				   distanceCounter++;

			   }
			   else{ // if you can't go ahead or left, rotate right b/c dirInde is -90
				   robot.rotate(-90);
	        		PlayMaze.driveHandler.postDelayed(PlayMaze.driveRunnable, 500);

			   }
		   }
    	noBias++;

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
     * Configures the robot with predefined Robot settings.  
     */
    @Override
    public void setRobot(Robot r) throws UnsuitableRobotException {
        // TODO Auto-generated method stub
       
    }
}