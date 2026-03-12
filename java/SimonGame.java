import java.util.*;

import swiftbot.*;

public class SimonGame {
	
	//A nested class to help store all information about assigning the LED more efficiently
	static class assignedLED {
		public Underlight underlight;
		public int[] colour;
		public String cName;
		
		public assignedLED(Underlight underlight, int[] colour, String cName) {
	        this.underlight = underlight;
	        this.colour = colour;
	        this.cName = cName;
	    }
	}
	
	static class CelebrationDive {
		private static final double K_CM_PER_S_PER_PERCENT = 0.35;

		private static final int[][] GAME_COLOURS = {
				{255, 0, 0},	
				{0, 255, 0},	
				{0, 0, 255},	
				{255, 255, 0}
		};

		private SwiftBotAPI swiftbot;
		
		public CelebrationDive(SwiftBotAPI swiftbot) {
			this.swiftbot = swiftbot;
		}
		
		public void doCelebrationDive(int score) throws InterruptedException {
			if(score < 5) {
				System.out.println("Score too low for celebration");
				return;
			}
			
			int speed = calculateSpeed(score);
			int armTimeMs = calculateArmTimeMs(speed);
			
			blinkRandomGameColours();
			moveVShape(speed, armTimeMs);
			blinkRandomGameColours();
		}
			
			private int calculateSpeed(int score) {
				if (score < 5) {
					return 40;
				} else if (score >= 10) {
					return 100;
				} else {
					return score * 10;
				}
			}
			
			private int calculateArmTimeMs(int speed) {
				double vActual = K_CM_PER_S_PER_PERCENT * speed;
				double timeSeconds = 30.0 / vActual;
				return (int) Math.round(timeSeconds * 1000);
			}
			
			private void moveVShape(int speed, int armTimeMs) throws InterruptedException {
				int offset = 25;
				
				int left1 = speed;
				int right1 = speed;
				
				int left2 = speed;
				int right2 = speed - offset;
				
				swiftbot.move(left1, right1, armTimeMs);
				Thread.sleep(200);
				
				swiftbot.move(left2, right2, 1000);
				Thread.sleep(150);
				
				swiftbot.move(left1, right1, armTimeMs);
				Thread.sleep(200);
			}
			
			private void blinkRandomGameColours() throws InterruptedException {
				int[] order = {0, 1, 2, 3};
				Random rand = new Random();
				
				for (int i = order.length - 1; i > 0; i--) {
					int j = rand.nextInt(i + 1);
					int tmp = order[i];
					order[i] = order[j];
					order[j] = tmp;
				}

			for (int idx : order) {
				int[] colour = GAME_COLOURS[idx];
				
				setAllUnderLights(colour[0], colour[1], colour[2]);
				Thread.sleep(250);
				
				setAllUnderLights(0, 0, 0);
				Thread.sleep(150);
				}
			}
			
			private void setAllUnderLights(int r, int g, int b) {
				int[] rgb = {r, g, b};
				for (Underlight u : Underlight.values()) {
					swiftbot.setUnderlight(u, rgb);
				}
			}

	}

	static SwiftBotAPI swiftbot;
	
	static volatile Button lastInput = null;
	
	static CelebrationDive celebration;
	
	public static void main(String[] args) throws InterruptedException {
		
		swiftbot = SwiftBotAPI.INSTANCE;
		
		celebration = new CelebrationDive(swiftbot);

		//Scanner will be used to take user input
		Scanner input = new Scanner(System.in);
		
		//Defining the main menu
		boolean runGame = true;
		
		while (runGame) {
			
			System.out.println("\n--------SIMON GAME--------\n");
			System.out.println("Welcome to the Simon memory game! Please choose an option:\n");
			System.out.println("[1] Start game");
			System.out.println("[2] Exit game");
			
			String choice = input.next();
			
			if (choice.equals("1")) {
				startGame(input);
				return;
			}
			else if (choice.equals("2")) {
				input.close();
				return;
			}
			else {
				System.out.println("Error, your input is invalid. Please try again.");
			}
			
		}
		
		
	}
	
	public static void enableButtons() {
		swiftbot.enableButton(Button.A, () -> lastInput = Button.A);
		swiftbot.enableButton(Button.B, () -> lastInput = Button.B);
		swiftbot.enableButton(Button.X, () -> lastInput = Button.X);
		swiftbot.enableButton(Button.Y, () -> lastInput = Button.Y);
	}
	
	private static Button buttonPress() throws InterruptedException
	{
		while (lastInput == null) {
			Thread.sleep(20);
		}
		Button pressed = lastInput;
		lastInput = null;
		return pressed;
	}

	private static void startGame(Scanner input) throws InterruptedException {
		
		//The 4 colours that will be used
		int[][] colours = { 
				{ 255, 0, 0 },
				{ 0, 0, 255 },
				{ 0, 255, 0 },
				{ 255, 255, 0 }
		};
		
		String[] colourName = {
				"Red",
				"Blue",
				"Green",
				"Yellow"
		};
		
		//Setting the 4 LEDs that will be used
		Underlight[] underlights = {
				Underlight.FRONT_LEFT, 
				Underlight.FRONT_RIGHT, 
				Underlight.BACK_LEFT, 
				Underlight.BACK_RIGHT	
		};

		
		//Then assigning each button a specific LED
		Button buttons[] = { Button.A, Button.X, Button.B, Button.Y };
		
		Map<Button, assignedLED> buttonAssign = new HashMap<>();
		for (int i = 0; i < buttons.length; i++) {
			buttonAssign.put(buttons[i], new assignedLED(underlights[i], colours[i], colourName[i]));
		}
	
		//Show this information to the player
		System.out.println("\nGAMEPLAY INFO:\n");
		for (Button b : buttons) {
			assignedLED aLED = buttonAssign.get(b);
			System.out.println("Button "+ b + " = " + aLED.underlight + " [ " + aLED.cName + " ] ");
		}

		
		//This will store the sequence of colours the player needs to repeat 
		ArrayList<Button> seq = new ArrayList<>();
		Random random = new Random();
		
		int score = 0;
		int round = 1;
		
		enableButtons();
		
		while (true) {
		
			//Next button added to sequence
			Button nextButton = buttons[random.nextInt(4)];
			seq.add(nextButton);
			
			System.out.println("\nRound no: "+round+" | Score no: "+score);
			Thread.sleep(1000);
			
			//Show sequence of colours - for every button in the sequence, it looks up it's corresponding
			//LED and colour, and shows the sequence of colours to the user on the swiftbot
			for (Button button : seq) {
				assignedLED aLED = buttonAssign.get(button);
				swiftbot.setUnderlight(aLED.underlight, aLED.colour);
				Thread.sleep(500);
				swiftbot.disableUnderlight(aLED.underlight);
				Thread.sleep(300);
			}
			
			System.out.println("Repeat the sequence on the SwiftBot via the buttons: ");
			boolean correct = true;
			
			for (Button rightButton : seq) {
				Button pressed = buttonPress();
				
				assignedLED aLED = buttonAssign.get(pressed);
				
				//Flash appropriate LED for every button pressed
				swiftbot.setUnderlight(aLED.underlight, aLED.colour);
				Thread.sleep(500);
				swiftbot.disableUnderlight(aLED.underlight);
				
				
				if (pressed != rightButton) {
					correct = false;
					break;
				}
			}
			
			if (!correct) {
				
				System.out.println("\nGame Over!");
				System.out.println("Score: "+ score+" | Round: "+round);
				//celebration dive method called
				return;
				
			}
			
			score++;
			
			//Check if player wants to quit or continue every 5th round
			if (round % 5 == 0) {
				Scanner input2 = new Scanner(System.in);
				System.out.println("Would you like to continue? [y/n]: ");
				String answer = input2.next();
				
				if (answer.equals("n")) {
					System.out.println("See you again champ!");
					System.out.println("Score: "+ score+" | Round: "+round);
					celebration.doCelebrationDive(score);
					return;
				}
				
				
			}
			round++;
			
			
		}
		
	}
	
}
