package donQuizote_v2;

import javax.swing.SwingWorker;

import donQuizote_v2.FiniteStateMachine.State;

public class AutomaticWorker extends SwingWorker {

	private DonQuizote dq;
	private QuizController controller;
	private FiniteStateMachine fsm;
	private int winner;
	
	public AutomaticWorker(DonQuizote dq){
		this.dq = dq;
		this.controller = dq.controller;
		this.fsm = dq.fsm;
	}
	
	@Override
	protected Object doInBackground() throws Exception {
		
		while(!(fsm.killSwitch == true)){	
			doAction();
		}
		
		return null;
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
		
		
		fsm.checkCaps();
		
		State state = fsm.state;
		System.out.println("#DQ: Arrived at " + state);
		dq.updateState('S' + state.toString());
		
		switch(state){

			case CHOICEPAGE:
				controller.click("playForFreeButton");
				snooze(2000);
				fsm.doTransaction(40);
				break;
			// This is the splash page where we pick PLAY or REAL money 
			case PLAYPAGE:
				fsm.questionNumber = 0;
				controller.click("playButton");	snooze(1000);
				controller.click("skipButton");	snooze(1900);
				controller.click("playForFreeButton"); snooze(2000);
				fsm.doTransaction(4000);
				break;
			case FFF:
				snooze(200);
				controller.click("playButton");
				controller.click("answerA"); snooze(100);
				controller.click("answerB"); snooze(100);
				controller.click("answerC"); snooze(100);
				controller.click("answerD"); 
				snooze(9000);
				controller.click("playButton");
				snooze(1000);
				fsm.doTransaction(200);
				break;
			case ISCOLLECT:
				snooze(100);
				fsm.doTransaction(200);
				break;
			case SCOREBOARD:
				snooze(1000);
				fsm.doTransaction(400);
				break;
			case QUESTION:
				//OH THIS IS THE BIG ONE
				// Get the mouse out of the way
				fsm.questionNumber += 1;
				controller.click("playButton");
				snooze(4000);
				winner = dq.answerQuestion();
				if (winner == 0) controller.click("answerA");
				if (winner == 1) controller.click("answerB");
				if (winner == 2) controller.click("answerC");
				if (winner == 3) controller.click("answerD");
				fsm.doTransaction(400);
				break;
			case CORRECT:
				// TODO: Do something to update the question status in the DB, and then bump up the Q number.
				snooze(3000);
				dq.q.answerCorrect[winner] = 1;
				dq.updateQuestion();
				fsm.doTransaction(40);
				break;
			case WRONG:
				// TODO:We might have a try again, so turn straight back around and try to reanswer the question.
				//		Obvs don't OCR it again though.
				//		For now we're lazy and just click A then B again to hurry things up and assume we've lost for good
				dq.q.answerCorrect[winner] = 2;
				snooze(1000); controller.click("answerA"); 
				winner = 0;
				fsm.doTransaction(400);
				snooze(100); controller.click("answerB");
				winner = 1;
				fsm.doTransaction(4000);
				break;
					/*
					snooze(1000); controller.click("playButton");
					snooze(1000); controller.click("playButton");
					snooze(8000);
					fsm.doTransaction(4000);
					break;
					*/
			case LOSER:
				// dq.q.answerCorrect[winner] = 2; 
				snooze(3000); 
				fsm.state = State.PLAYPAGE;
			default: 
				snooze(100); // Chill out a little
		}
		
	}
	
	public void snooze(int i){
		fsm.snooze(i);
	}
	
	
	

}
