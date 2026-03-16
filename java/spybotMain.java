import swiftbot.*;

public class spybotMain {
	static SwiftBotAPI swiftbot;
	static volatile Button lastInput = null;

	public static void main(String[] args) {
		swiftbot = SwiftBotAPI.INSTANCE;
		
		//main menu that is displayed upon starting the program
		System.out.println(allColours.CYAN + "\n==============================");
        System.out.println("    Morse Code Communication");
        System.out.println("==============================" + allColours.RESET);
        System.out.println("\nWelcome to the secure morse code communication program!");
        System.out.println("\nBefore continuing, please authenticate yourself by scanning a QR code in front of the SwiftBot's camera.");

        
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
