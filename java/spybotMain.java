import swiftbot.*;

public class spybotMain {
	static SwiftBotAPI swiftbot;

	public static void main(String[] args) {
		swiftbot = SwiftBotAPI.INSTANCE;
		
		//main menu that is displayed upon starting the program
		System.out.println(allColours.CYAN + "\n==============================");
        System.out.println("    Morse Code Communication");
        System.out.println("==============================" + allColours.RESET);
        System.out.println("\nWelcome to the secure morse code communication program!");
        System.out.println("\nBefore continuing, please authenticate yourself by scanning a QR code in front of the SwiftBot's camera.");

        
	}

}
