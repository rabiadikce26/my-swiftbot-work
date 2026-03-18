import swiftbot.SwiftBotAPI;

public class msgRelay {
	
	private SwiftBotAPI swiftbot;
    private morseDict dict;
    
    //all values that are set initially and should never be changed
    private static final int[] WHITE  = {255, 255, 255};
    private static final int[] BLUE   = {0,   0,   255};
    private static final int[] AMBER  = {255, 191, 0  };
    private static final int[] RED    = {255, 0,   0  };
    private static final int[] GREEN  = {0,   255, 0  };

    private static final int BLINK_ON_MS  = 400;
    private static final int BLINK_OFF_MS = 200;
    private static final int GAP_MS       = 300;

    public msgRelay(SwiftBotAPI swiftbot, morseDict dict) {
        this.swiftbot = swiftbot;
        this.dict = dict;
    }
    
    
    private void oneBlink(int[] colour) throws InterruptedException {
    	swiftbot.fillUnderlights(colour); //turn lights on in said colour
        Thread.sleep(BLINK_ON_MS); //leave on for 400ms
        swiftbot.disableUnderlights(); //turn lights off
        Thread.sleep(BLINK_OFF_MS); //wait 200ms
        Thread.sleep(GAP_MS); //a short gap before the next blink
    }
    
    
    public void relayMsg(String senderCallSign, String senderLocation, String msgContent) throws InterruptedException {
    	
    	System.out.println(allColours.PURPLE + "\n==============================");
        System.out.println("    Morse Code Delivering Menu");
        System.out.println("==============================" + allColours.RESET);
        System.out.println("\nWelcome to the delivering message menu.");
        System.out.println("\nHere is a guide to help with decoding how the message is being transmitted");
        System.out.println("in morse code using the SwiftBot's underlights:\n");
        System.out.println("Dot (.)          ->  LEDs flash " + allColours.WHITE  + "white"  + allColours.RESET);
        System.out.println("Dash (-)         ->  LEDs flash " + allColours.BLUE   + "blue"   + allColours.RESET);
        System.out.println("End of character ->  LEDs flash " + allColours.YELLOW + "amber"  + allColours.RESET);
        System.out.println("End of word      ->  LEDs flash " + allColours.RED    + "red"    + allColours.RESET);
        System.out.println("End of message   ->  LEDs flash " + allColours.GREEN  + "green"  + allColours.RESET);

        System.out.println("\nThe sender of your message is: " + allColours.PURPLE + senderCallSign + allColours.RESET);
        System.out.println("Delivering message now...");
        
        //put whole message together to make looping through it easier
        String fullMessage = senderLocation + " " + msgContent;
        
        //breaks down whole message into array individual words
        String[] words = fullMessage.split(" ");
        
        //loops through every word
        for (int i = 0; i < words.length; i++) {
        	String word = words[i];
        	
        	//loops through every character in the word
        	//.toCharArray breaks down every word into its characters
        	for (char c : word.toCharArray()) {
        		String morseCode = dict.encode(String.valueOf(c)); //convert character to string
        														  //and look up in dictionary
        		
        		//loops through every morse code symbol
        		//.toCharArray breaks down every morse code sequence into its individual symbols
        		for (char symbol : morseCode.toCharArray()) {
        			if (symbol == '.') {
                        oneBlink(WHITE);
                    } 
        			else if (symbol == '-') {
                        oneBlink(BLUE);
                    }
				}
        		
        		//end of character
        		oneBlink(AMBER);
			}
        	
        	//end of word
        	oneBlink(RED);
		}
        
        //end of message
        oneBlink(GREEN);
    }

}
