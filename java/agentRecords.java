import java.util.HashMap;
import java.util.Map;

public class agentRecords {
	
	//hashmap created to contain agent info, such as their callsign and 
	//corresponding location
	private Map<String, String> agentInfo = new HashMap<String, String>();
	
	//adding each individual agent record via the constructor 
	public agentRecords() {
		agentInfo.put("AG10A", "A");
		agentInfo.put("AG20B", "B");
		agentInfo.put("AG30C", "C");
	}
	
	public boolean isFormatValid(String qrData) {
		
		//checks if data stored in QR is null, or doesn't contain a colon
		if (qrData == null || !qrData.contains(":")) {
            return false;
        }
		
		//Splits up the QR data into an array of substrings every time a colon is found
		//Works like this -> "TEST.A".split(":") -> becomes ["TEST", "A"]
		//If there aren't exactly two different parts, then it is invalid as records
		//only contain two parts
		String[] parts = qrData.split(":");
        if (parts.length != 2) {
            return false;
        }
        
        //Divide parts into separate variables
        String callSign = parts[0];
        String location = parts[1];
        
        //Checks if callSign conforms to length limit and character usage
        //[a-zA-Z0-9]+ -> means one or more characters within the set
        //Special chars and spaces not allowed
        boolean csTooLong = callSign.length() > 5;
        boolean csInvalidChars = !callSign.matches("[a-zA-Z0-9]+");
        if (csTooLong || csInvalidChars) {
            return false;
        }
		
        //location has to match one of the three given locations
        if (!location.equals("A") && !location.equals("B") && !location.equals("C")) {
            return false;
        }
        
		return true;
	}

}
