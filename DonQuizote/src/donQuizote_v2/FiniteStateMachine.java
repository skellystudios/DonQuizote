package donQuizote_v2;

public class FiniteStateMachine {
	
	/* 
	 * Things we need
	 */
	
	public enum State {SPLASHMAIN, SPLASHDEMO, SPLASHPLAY, TARRENT1,  
		FASTESTFINGER, RESULTS, QUESTION};	
	public State state;
	public int level;
	public boolean killSwitch = false;
	public boolean confused = false;
	private QuizController controller;
	
	/*
	 * Pretty much the whole application flow
	 */
	
	public FiniteStateMachine(QuizController c) {
		controller = c;
		state = State.SPLASHMAIN;
	}
	
	public void doAction(){
		/*
		 *  The general idea is that at each point we should check 
		 *  we are where we think, and then perform discrete actions
		 *  until we have made a state transition, and mark what we think that transition is
		 *  
		 *  If the check fails, we should either go into a panic mode and refresh the 
		 *  browser and start again, or we should attempt to guess the most likely state
		 */
		
		// if (confused) System.out.println("FSM: Entered fsm confused"); tryToUpdateState(); 

		switch(state){
			// This is the splash page before 
			case SPLASHMAIN:
				// Check that we are where we think we are
				if (!controller.isStartPage()) { confused = true; break; }
				// Press the playMoney button
				controller.pressDemoButton();
				
				break;
			case SPLASHDEMO:
				
				// 
				break;
			case SPLASHPLAY:
				break;
			
		}
		
		if (confused)System.out.println("FSM: Man, I thought we were somewhere else. FSM is confused.");
	}
}
