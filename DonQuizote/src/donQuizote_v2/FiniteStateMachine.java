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
	private DonQuizote dq;
	
	/*
	 * Pretty much the whole application flow
	 */
	
	public FiniteStateMachine(DonQuizote d, QuizController c) {
		controller = c;
		state = State.SPLASHPLAY;
		confused = true;
		dq = d;//DonQuizote.getInstance();
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
		
		if (confused) System.out.println("#FSM: Entered with fsm confused. Trying to get a grip NOW"); tryToUpdateState(); 
		
		System.out.println("#FSM: Arrived at " + state);
		switch(state){
			// This is the splash page where we pick PLAY or REAL money 
			case SPLASHMAIN:
				// Check that we are where we think we are
				if (!controller.isStartPage()) { confused = true; break; }
				// Press the playMoney button
				controller.pressDemoButton();
				snooze(2000);
				state = State.SPLASHPLAY; 
				
				break;
			case SPLASHDEMO:
				
				// 
				break;
			// This is where we have to click the play button. And maybe make a bet, but we don't do that	
			case SPLASHPLAY:
				
				if (controller.isStartPage()) {state = State.SPLASHMAIN; break;}
				controller.pressPlayButton();
				snooze(1000);
				controller.pressSkipButton();
				snooze(1000);
				controller.skipChris();
				state =  State.FASTESTFINGER;
				break;
			// The stupid 4 options fastest finger
			case FASTESTFINGER:
				if (!controller.isFFF()) {confused=true; break;}
				snooze(2000);
				controller.pressA(); snooze(1000);
				controller.pressB(); snooze(1000);
				controller.pressC(); snooze(1000);
				controller.pressD(); 
				snooze(4000);
				state = State.RESULTS;
				break;
			// Chris Tarrent says some shit 
			case RESULTS:
				// Click somewhere arbitrary to skip it
				controller.pressA();
				snooze(4000);
				state = State.QUESTION;
				break;
			case QUESTION:
				//OH THIS IS THE BIG ONE
				String winner = dq.answerQuestion();
				if (winner == "A") controller.pressA();
				if (winner == "B") controller.pressB();
				if (winner == "C") controller.pressC();
				if (winner == "D") controller.pressD();
				confused = true;
				break;
			default: 
				confused = true;
				snooze(100); // Chill out a little
			
		}
		
		if (confused) System.out.println("#FSM: Man, I thought we were somewhere else. FSM is confused.");
		else System.out.println("#FSM: Exited " + state);
		
		snooze(100);
	}

		public void tryToUpdateState(){
			
			if (controller.isStartPage()){state = State.SPLASHMAIN;}
			else if (controller.is2WayTraffic()){state = State.SPLASHPLAY;}
			else if (controller.isFFF()){state = State.FASTESTFINGER;}
			
			
		}

	public void snooze(int i){
		try { 
			Thread.sleep(i);
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
}