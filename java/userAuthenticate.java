import java.awt.image.BufferedImage;

import swiftbot.*;

public class userAuthenticate {
	private SwiftBotAPI swiftbot;
	private agentRecords agentRecs;
	
	public userAuthenticate(SwiftBotAPI swiftbot, agentRecords agentRecs) {
        this.swiftbot = swiftbot;
        this.agentRecs = agentRecs;
    }

	private String scanQRCode() throws InterruptedException {
        try {
            String decoded = "";

            //keeps scanning until something is detected
            while (decoded.isEmpty()) {
                BufferedImage image = swiftbot.getQRImage();
                decoded = swiftbot.decodeQRImage(image);

                if (decoded.isEmpty()) {
                    Thread.sleep(1000);
                }
            }

            return decoded;

        } catch (Exception e) {
            System.out.println("ERROR: Unable to scan for QR code.");
            e.printStackTrace();
            return null;
        }
    }
	
	public String verifyUser() throws InterruptedException {
		
		//If any of the if conditions are found to be true, it goes back to the top of the loop
		//via continue; to scan the QR code again
		while(true) {
			String qrInfo = scanQRCode();
			
			//checks if scanned info is in the valid format
			if (qrInfo == null || qrInfo.isEmpty() || !agentRecs.isFormatValid(qrInfo)) {
				//error signal here
				System.out.println(allColours.RED + "ERROR:" + allColours.RESET + " Invalid QR code detected.");
                System.out.println("The content stored on the QR code does not match any stored agent data.");
                System.out.println("Please scan your QR code again.");
                continue;
			}
			
			//splits up the two record values into individual variables
			String[] parts = qrInfo.split(":");
            String callSign = parts[0];
            String location = parts[1];
            
            //checks whether scanned info matches with an existing agent
            if (!agentRecs.doesAgentExist(callSign, location)) {
            	//error signal here
            	System.out.println(allColours.RED + "ERROR:" + allColours.RESET + " Invalid QR code detected.");
                System.out.println("The content stored on the QR code does not match any stored agent data.");
                System.out.println("Please scan your QR code again.");
                continue;
			}
            
            System.out.println("\nAuthentication " + allColours.GREEN + "successful!" + allColours.RESET);
            System.out.println("Message menu loading...");
		}
	}
}
