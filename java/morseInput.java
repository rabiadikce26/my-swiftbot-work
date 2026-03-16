import swiftbot.*;

public class morseInput {
	private SwiftBotAPI swiftbot;
    private morseDict dict;

    public morseInput(SwiftBotAPI swiftbot, morseDict dict) {
        this.swiftbot = swiftbot;
        this.dict = dict;
    }
    
    
    //Takes morse code input, decoding and saving them to a String message
    public String msgInput(String senderLoc) throws InterruptedException {
    	
    	
    	spybotMain.enableButtons(swiftbot);
    	
    	String message = "";
        String morseChar = "";
        
        System.out.print("\nInput: ");
        
        //Checks each individual button press, and performs its designated action
        while (true) {
			Button pressed = spybotMain.buttonPress();
			
			if (pressed == Button.X) {
				morseChar = morseChar + ".";
				System.out.print(".");
			}
			else if (pressed == Button.Y) {
				morseChar = morseChar + "-";
				System.out.print("-");
			}
			else if (pressed == Button.A) {
				if (morseChar.isEmpty()) {
					continue;
				}

				String decodeChar = dict.decode(morseChar);

				// if the morse char sequence doesn't exist in the dictionary
				if (decodeChar == null) {
					// error signal
					System.out.println(allColours.RED + "\nERROR:" + allColours.RESET
							+ " Invalid Morse code character input detected.");
					System.out.println("Please review the instructions and dictionary once more, and try again.");
					// message menu
					//
					System.out.println("---------------");
					System.out.println(allColours.CYAN + "Current message: " + message + allColours.RESET);
					morseChar = "";
					System.out.print("\nInput: ");
					continue;
				}

				if (morseChar.equals("-----")) {

					// check contents of msg isn't empty
					if (message.isEmpty()) {
						// error signal
						System.out
								.println(allColours.RED + "\nERROR:" + allColours.RESET + " Message cannot be empty.");
						morseChar = "";
						System.out.print("\nInput: ");
						continue;
					}

					// separates message into individual words, and stores first word (destination)
					// as variable
					String[] words = message.split(" ");
					String destination = words[0];

					// check if destination is of one of the 3 valid locations
					if (!destination.equals("A") && !destination.equals("B") && !destination.equals("C")) {
						// error signal
						System.out.println(allColours.RED + "\nERROR:" + allColours.RESET
								+ " The first word of the message must be a valid destination (A, B, or C).");
						morseChar = "";
						System.out.print("\nInput: ");
						continue;
					}

					// ensures that destination isn't the same as sender's location
					// as you can't send the message to yourself
					if (destination.equals(senderLoc)) {
						// error signal
						System.out.println(allColours.RED + "\nERROR:" + allColours.RESET
								+ " The destination cannot be the same as the sender's location.");
						morseChar = "";
						System.out.print("\nInput: ");
						continue;
					}

					System.out.println("\n\nEnd of message detected.");
					System.out.println("Message " + allColours.GREEN + "successfully" + allColours.RESET + " logged!");
					return message;
				}

				// add character to current message, resetting morseChar
				message = message + decodeChar;
				morseChar = "";

				System.out.println();
				System.out.println("---------------");
				System.out.println(allColours.CYAN + "Current message: " + message + allColours.RESET);
				System.out.print("\nInput: ");
			}
			else if (pressed == Button.B) {
				//end of word indicated, so just adding white space to message
				//make sure message isn't empty, and that it isn't ending on a white space
				//to prevent the message from having multiple spaces one after another
				if (!message.isEmpty() && !message.endsWith(" ")) {
                    message += " ";
                    morseChar = "";
                    System.out.println("\nInput: End of word");
                    System.out.println("---------------");
                    System.out.println(allColours.CYAN + "Current message: " + message.trim() + allColours.RESET);
                    System.out.print("\nInput: ");
                }
				
			}
		}
    }

}
