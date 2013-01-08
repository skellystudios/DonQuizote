package donQuizote_v2;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class FiniteStateMachine {
	
	/* 
	 * Things we need
	 */
	
	public enum State {FFF, QUESTION, CHOICEPAGE, PLAYPAGE, SCOREBOARD, 
						CORRECT, WRONG, TRYAGAIN, COLLECTCONTINUE, 
						ISCOLLECT, CONFUSED};	
	public State state;
	public int level;
	public boolean killSwitch;
	public boolean confused = false;
	private QuizController controller;
	private DonQuizote dq;
	

	public FiniteStateMachine(DonQuizote d, QuizController c) {
		controller = c;
		state = State.CHOICEPAGE;
		confused = false;
		dq = d;//DonQuizote.getInstance();
	}
	
 
	public void doTransaction(int timeout){
		
		while (!tryToUpdateState() && timeout > 0 && !killSwitch)
		{
			snooze(100); timeout--;
			System.out.println("Timout " + timeout);
			checkCaps();
		}
		if (timeout < 1) killSwitch = true;
	
	}
	
	public Boolean tryToUpdateState(){
			
		
		switch(state){
			// This is where we choose play or real money
			case CHOICEPAGE:
				// Once the green goes, we've moved. Should be instant, pretty much
				if (!isModalColour("checkStartScreenGreen", -16747628, 80)) {
					state = State.PLAYPAGE; return true; 
				}
				break;
			case PLAYPAGE:
				//  Wait until the green bit appears
				if (isMeanColour("fffTimer", -7946692, 80)) {
					state = State.FFF; return true; 
				}
				break;
			case FFF:
				// Wait until the green bit disappears
				if (!isMeanColour("fffTimer", -7946692, 80)) {
					state = State.ISCOLLECT; return true; 
				}
				break; 
			case ISCOLLECT:
				if (!isModalColour("splitBlackContinueCollect", 0, 110)) {
					state = State.COLLECTCONTINUE; return true; 
				} else {
					state = State.SCOREBOARD; return true; 
				}
			case SCOREBOARD:
				// Wait until the purple disappears, and then check for a bonus round
				if (!isMeanColour("splitPurple", -8434794, 80)) {
					if (isMeanColour("fffTimer", -7946692, 80)) {
						state = State.FFF; return true; 
					} else {
						state = State.QUESTION; return true;
					}
				} else {
					return false;
				}
			case QUESTION:
				// See if we got one right first, cos we're optimistic like
				int correctColour = -7551175;
				int wrongColour = -16732441;
				if (isModalColour("answerA", correctColour, 80)
						||isModalColour("answerB", correctColour, 80)
						||isModalColour("answerC", correctColour, 80)
						||isModalColour("answerD", correctColour, 80)) 
					{ state = State.CORRECT; return true; }
				// Now see if we're definitely wrong (pending Try-Again style reprieve)		
				if (isModalColour("answerA", wrongColour, 80)
						||isModalColour("answerB", wrongColour, 80)
						||isModalColour("answerC", wrongColour, 80)
						||isModalColour("answerD", wrongColour, 80)) 
					{ state = State.CORRECT; return true; }
				return false;
			
			default:
				return false;
				
		}
		
		return false;
	
	}
	
	
	/*
	 * 
	 ******************** UTILITIES **************************
	 * 
	 */

	public void snooze(int i){
		try { 
			Thread.sleep(i);
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	public Boolean isModalColour(String area, int colour, int tolerance){  
		BufferedImage image = controller.getImage(controller.getArea(area));
		int areaColour = controller.getModalColour(image);
		int result = (int) ColourTester.compare(areaColour, colour);
		System.out.println("comparing " + areaColour + " against reference " + colour + " result was " + result);
		Boolean check = result < tolerance; 
		return check;
		}
	
	public Boolean isMeanColour(String area, int colour,  int tolerance){  
		BufferedImage image = controller.getImage(controller.getArea(area));
		int areaColour = controller.getMeanColour(image);
		int result = (int) ColourTester.compare(areaColour, colour);
		System.out.println("comparing " + areaColour + " against reference " + colour + " result was " + result);
		Boolean check = result < tolerance;
		return check;
		}
	
	// Check for caps and then stop if necessary
	public void checkCaps(){
	boolean isCaps = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
			if (isCaps) { 
				System.out.println("#FSM: CAPSLOCK'd"); 				
				killSwitch = true;
				state = State.CONFUSED;
			}
	}
}