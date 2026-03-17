import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class msgLog {

	//these are constants and as such will never change
	private static final String LOG_FILE = "messageLog.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    
    //when called, will log the message including details of the msg, the sender
    //and time it was recorded
    public void logMsg(String senderCallSign, String senderLocation, String destination, String msgContent) {
        String recordedTime = LocalDateTime.now().format(FORMATTER);

        try {
            FileWriter writer = new FileWriter(LOG_FILE, true);
            writer.write("--- NEW MESSAGE ---\n");
            writer.write("Sender:           " + senderCallSign + " at location " + senderLocation + "\n");
            writer.write("Destination:      " + destination + "\n");
            writer.write("Message:          " + msgContent + "\n");
            writer.write("Time recorded:    " + recordedTime + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Warning: Couldn't write to log file.");
        }
    }
    
    
    //logs the remaining information after the message is delivered
    //includes receiving agent's details, and time received
    public void logDelivery(String senderCallSign, String senderLocation, String receiverCallSign, String receiverLocation) {
        String deliveredTime = LocalDateTime.now().format(FORMATTER);

        try {
            FileWriter writer = new FileWriter(LOG_FILE, true);
            writer.write("Receiver:         " + receiverCallSign + " at location " + receiverLocation + "\n");
            writer.write("Time delivered:   " + deliveredTime + "\n");
            writer.write("------------------\n\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Warning: Couldn't write to log file.");
        }
    }
}
