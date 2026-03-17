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
				{"A", ".-"},   {"G", "--."},  {"M", "--"},   {"S", "..."},  {"Y", "-.--"},
	            {"B", "-..."}, {"H", "...."}, {"N", "-."},   {"T", "-"},    {"Z", "--.."},
	            {"C", "-.-."}, {"I", ".."},   {"O", "---"},  {"U", "..-"},  {"0", "-----"},
	            {"D", "-.."},  {"J", ".---"}, {"P", ".--."},  {"V", "...-"},
	            {"E", "."},    {"K", "-.-"},  {"Q", "--.-"}, {"W", ".--"},
	            {"F", "..-."}, {"L", ".-.."}, {"R", ".-."},  {"X", "-..-"}
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


	//whenever this is called, it will print out the dictionary on the console
	public void showDict() {
		System.out.println("\nHere is the morse code dictionary to help with writing messages:\n");
        int count = 0;
        
        //goes through every entry, getting both values in the entry to then print them like this
        //A: .-      | G: -.   
        //B: -...    | H: ....
        for (Map.Entry<String, String> entry : char2morse.entrySet()) {
            System.out.printf("%-2s: %-7s", entry.getKey(), entry.getValue());
            count++;
            //makes sure each column has no more than 6 rows
            if (count % 5 == 0) {
                System.out.println();
            } else {
                System.out.print(" | ");
            }
        }
        System.out.println();
	}
	
}
