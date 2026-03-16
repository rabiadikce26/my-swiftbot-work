import swiftbot.*;

public class morseInput {
	private SwiftBotAPI swiftbot;
    private morseDict dict;

    public morseInput(SwiftBotAPI swiftbot, morseDict dict) {
        this.swiftbot = swiftbot;
        this.dict = dict;
    }
    
    
    public String msgInput(String senderLoc) throws InterruptedException {
    	
    	
    	spybotMain.enableButtons(swiftbot);
    	
    	String message = "";
        String morseChar = "";
        
        System.out.print("\nInput: ");
        
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
			}
		}
    }

}
