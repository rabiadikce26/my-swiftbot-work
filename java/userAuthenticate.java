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
	
	public String[] verifyUser() throws InterruptedException {
		
		//If any of the if conditions are found to be true, it goes back to the top of the loop
		//via continue; to scan the QR code again
		while(true) {
			String qrInfo = scanQRCode();
			
			//checks if scanned info is in the valid format
			if (qrInfo == null || qrInfo.isEmpty() || !agentRecs.isFormatValid(qrInfo)) {
				errSignal();
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
            	errSignal();
            	System.out.println(allColours.RED + "ERROR:" + allColours.RESET + " Invalid QR code detected.");
                System.out.println("The content stored on the QR code does not match any stored agent data.");
                System.out.println("Please scan your QR code again.");
                continue;
			}
            
            System.out.println("\nAuthentication " + allColours.GREEN + "successful!" + allColours.RESET);
            System.out.println("Message menu loading...");
            
            return new String[]{callSign, location};
		}
	}
	
	
	public String[] verifyReceiver(String expectedLocation) throws InterruptedException {

        while (true) {
            String qrInfo = scanQRCode();

          //checks if scanned info is in the valid format
            if (qrInfo == null || qrInfo.isEmpty() || !agentRecs.isFormatValid(qrInfo)) {
                errSignal();
                System.out.println(allColours.RED + "ERROR:" + allColours.RESET + " Invalid QR code detected.");
                System.out.println("The content stored on the QR code does not match any stored agent data.");
                System.out.println("Please scan your QR code again.");
                continue;
            }

            String[] parts = qrInfo.split(":");
            String callSign = parts[0].toUpperCase();
            String location = parts[1].toUpperCase();

            //location on QR must match the destination
            boolean locationMatchesDestination = location.equals(expectedLocation);
            if (!locationMatchesDestination) {
                errSignal();
                System.out.println(allColours.RED + "ERROR:" + allColours.RESET + " Invalid QR code detected.");
                System.out.println("The content stored on the QR code either does not match the destination,");
                System.out.println("or the recipient's stored data.");
                System.out.println("Please scan your QR code again.");
                continue;
            }

            if (!agentRecs.doesAgentExist(callSign, location)) {
                errSignal();
                System.out.println(allColours.RED + "ERROR:" + allColours.RESET + " Invalid QR code detected.");
                System.out.println("The content stored on the QR code does not match any stored agent data.");
                System.out.println("Please scan your QR code again.");
                continue;
            }

            System.out.println("\nVerification " + allColours.GREEN + "successful!" + allColours.RESET);
        }
    }
	
	//method that displays an error signal via blinking pink lights twice
    private void errSignal() throws InterruptedException {
        int[] pink = {255, 20, 147};

        for (int i = 0; i < 2; i++) {
            swiftbot.fillUnderlights(pink);
            Thread.sleep(400);
            swiftbot.disableUnderlights();
            Thread.sleep(1000);
        }
    }
}
