import swiftbot.*;

public class spybotMain {
	static SwiftBotAPI swiftbot;
	static volatile Button lastInput = null;

	public static void main(String[] args) throws InterruptedException {
		swiftbot = SwiftBotAPI.INSTANCE;
		
		//main menu that is displayed upon starting the program
		System.out.println(allColours.CYAN + "\n==============================");
        System.out.println("    Morse Code Communication");
        System.out.println("==============================" + allColours.RESET);
        System.out.println("\nWelcome to the secure morse code communication program!");
        System.out.println("\nBefore continuing, please authenticate yourself by scanning a QR code in front of the SwiftBot's camera.");

        
        //initialise agent data and morse dictionary
        agentRecords agentRecs = new agentRecords();
        morseDict dict = new morseDict();
        msgLog msgLog = new msgLog();
        
        //authenticate user, and then store user's info individually for later use
        userAuthenticate auth = new userAuthenticate(swiftbot, agentRecs);
        String[] senderInfo = auth.verifyUser();
        
        String senderCallSign = senderInfo[0];
        String senderLocation = senderInfo[1];
        
        //show morse messaging menu and take user input
        morseInput morseInput = new morseInput(swiftbot, dict);
        String message = morseInput.msgInput(senderLocation);
        
        
        //extract destination (first word of message)
        String destination = message.split(" ")[0];
        String messageContent = message.substring(destination.length()).trim();

        System.out.println("\nMessage " + allColours.GREEN + "successfully" + allColours.RESET + " logged!");
        System.out.println("Sending message to recipient...");
        
        
        //log the message with user details
        msgLog.logMsg(senderCallSign, senderLocation, destination, messageContent);

        //go to destination
        movement navigation = new movement(swiftbot, senderLocation, destination);
        navigation.goToDestination();
        
        //verify receiver
        System.out.println(allColours.PURPLE + "\n====================");
        System.out.println("   MESSAGE STATUS");
        System.out.println("====================" + allColours.RESET);
        System.out.println(allColours.GREEN + "Successfully arrived at destination!" + allColours.RESET);
        System.out.println("\nIn order to verify that you are the intended recipient, please scan");
        System.out.println("your QR code in front of the SwiftBot's camera.");

        userAuthenticate receiverAuth = new userAuthenticate(swiftbot, agentRecs);
        String[] receiverInfo = receiverAuth.verifyReceiver(destination);
        String receiverCallSign = receiverInfo[0];
        
        //show the message via underlights
        msgRelay relay = new msgRelay(swiftbot, dict);
        relay.relayMsg(senderCallSign, senderLocation, messageContent);

        //log delivery, including receiving user's details
        msgLog.logDelivery(senderCallSign, senderLocation, receiverCallSign, destination);
        
        
        //swiftbot waits 10 seconds before returning
        System.out.println("\nMessage delivered " + allColours.GREEN + "successfully!" + allColours.RESET);
        System.out.println("The robot will move back to the sender's location in 10 seconds.");
        
        //counts down from 10 and prints each second to the user
        for (int i = 10; i > 0; i--) {
            System.out.println(i);
            Thread.sleep(1000);
        }
        
        System.out.println(allColours.PURPLE + "\n------------------------");
        System.out.println("  Returning to sender...");
        System.out.println("------------------------" + allColours.RESET);

        navigation.returnToSender();

        System.out.println("The SwiftBot has returned to sender " + allColours.GREEN + "successfully." + allColours.RESET);
        System.out.println("Exiting program...");
	}
	
	
	public static void enableButtons(SwiftBotAPI swiftbot) {
		swiftbot.enableButton(Button.A, () -> lastInput = Button.A);
		swiftbot.enableButton(Button.B, () -> lastInput = Button.B);
		swiftbot.enableButton(Button.X, () -> lastInput = Button.X);
		swiftbot.enableButton(Button.Y, () -> lastInput = Button.Y);
	}
	
	public static Button buttonPress() throws InterruptedException {
		while (lastInput == null) {
			Thread.sleep(20);
		}
		Button pressed = lastInput;
		lastInput = null;
		return pressed;
	}

}
