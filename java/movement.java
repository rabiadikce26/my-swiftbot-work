import swiftbot.SwiftBotAPI;

public class movement {

	private SwiftBotAPI swiftbot;
    private String currentLocation;
    private String currentOrientation;
    private String senderLocation;
    private String destination;

    //movement constants 
    private static final int MOVE_SPEED      = 60;  
    private static final int TURN_TIME_MS    = 550;  
    private static final int TRAVEL_TIME_MS  = 3000;
    
    
    public void goToDestination() throws InterruptedException {
    	
    	System.out.println(allColours.PURPLE + "\n====================");
        System.out.println("   MESSAGE STATUS");
        System.out.println("====================" + allColours.RESET);
        System.out.println("Destination: " + destination);
        System.out.println("Moving to recipient's location...");

        turnTowards(currentLocation, destination);
        moveForward();
        
        //blink red upon arriving at destination
        int[] red = {255, 0, 0};
        for (int i = 0; i < 3; i++) {
            swiftbot.fillUnderlights(red);
            Thread.sleep(300);
            swiftbot.disableUnderlights();
            Thread.sleep(200);
        }
        
        //update current location and orientation
        currentLocation = destination;
        currentOrientation = currentLocation + "facing" + senderLocation;
    }
    
    private void turnTowards(String from, String to) throws InterruptedException {
    	boolean turnRight = isClockwise(from, to); //checks if swiftbot is gonna go clockwise 
    	//around the triangle
    	
    	if (turnRight) {
            //turns right
            swiftbot.startMove(MOVE_SPEED, -MOVE_SPEED);
        } else {
            //turns left
            swiftbot.startMove(-MOVE_SPEED, MOVE_SPEED);
        }
    	
    	Thread.sleep(TURN_TIME_MS); //how long swiftbot should continue turning for before stopping
        swiftbot.stopMove();
        Thread.sleep(300);

        currentOrientation = from + "facing" + to;
    }
    
    //will return value of true if swiftbot will have to travel clockwise
    //triangle looks like this
    //   B
    //  / \
   //  /   \
  //  A-----C
    private boolean isClockwise(String from, String to) {
        boolean aToB = from.equals("A") && to.equals("B");
        boolean bToC = from.equals("B") && to.equals("C");
        boolean cToA = from.equals("C") && to.equals("A");
        return aToB || bToC || cToA;
    }
    
    //swiftbot moves along a side of the triangle
    private void moveForward() throws InterruptedException {
        swiftbot.startMove(MOVE_SPEED, MOVE_SPEED);
        Thread.sleep(TRAVEL_TIME_MS);
        swiftbot.stopMove();
        Thread.sleep(300);
    }
    
    //goes back to sender
    public void returnToSender() throws InterruptedException {
        System.out.println("Current location: " + currentLocation);
        System.out.println("Travelling to: " + senderLocation);

        // The robot arrived facing forward into the destination corner.
        // It needs to turn 180 degrees to face back towards the sender.
        turnAround();
        moveForward();

        currentLocation = senderLocation;
        currentOrientation = currentLocation + "facing" + destination;
    }
    
    //turn 180 degrees to go back to sender's location
    private void turnAround() throws InterruptedException {
        //do a 180 turn by doing two 90 degree turns in same direction
        swiftbot.startMove(MOVE_SPEED, -MOVE_SPEED);
        Thread.sleep(TURN_TIME_MS * 2);
        swiftbot.stopMove();
        Thread.sleep(300);
    }
}
