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
	

}
