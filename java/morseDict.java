import java.util.LinkedHashMap;
import java.util.Map;

public class morseDict {
	
	//Goes from morse code (".-") to a plain text character ("A")
	//Linked hash map used instead of normal one because it maintains the order of the dictionary
    private Map<String, String> morse2char = new LinkedHashMap<>();

    //Goes from plain text character to morse code
    //Another different linked hash map is used because lookups will become much easier than
    //having to reverse search on the same map
    private Map<String, String> char2morse = new LinkedHashMap<>();
	
	public morseDict() {
		loadDict();
	}
	
	private void loadDict() {
		String[][] dictEntries = {
	            {"A", ".-"},   {"B", "-..."}, {"C", "-.-."}, {"D", "-.."},
	            {"E", "."},    {"F", "..-."}, {"G", "--."},  {"H", "...."},
	            {"I", ".."},   {"J", ".---"}, {"K", "-.-"},  {"L", ".-.."},
	            {"M", "--"},   {"N", "-."},   {"O", "---"},  {"P", ".--."},
	            {"Q", "--.-"}, {"R", ".-."},  {"S", "..."},  {"T", "-"},
	            {"U", "..-"},  {"V", "...-"}, {"W", ".--"},  {"X", "-..-"},
	            {"Y", "-.--"}, {"Z", "--.."},
	            {"0", "-----"}
	        };
		
		//iterates through each entry and places them in the linked hash maps
		//with morse2char being in reverse order (second value becomes key instead)
		for (String[] strings : dictEntries) {
			char2morse.put(strings[0], strings[1]);
			morse2char.put(strings[1], strings[0]);
		}
	}
	
	//checks if inputted morse code is in the dictionary
	public boolean validMorse(String code) {
		return morse2char.containsKey(code);
	}
	
	public String decode(String code) {
		return morse2char.get(code);
	}
	
	public String encode(String character) {
		return char2morse.get(character);
	}

}
