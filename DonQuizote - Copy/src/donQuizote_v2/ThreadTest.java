package donQuizote_v2;

public class ThreadTest {

	SimpleThread t1 = new SimpleThread("Spain");
	SimpleThread t2 = new SimpleThread("Alaska");
	
	public void go(){
		
	t1.start();
	try {
		t1.join();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("Hey");
	t2.start();
	
	}
	
	
	
	
	class SimpleThread extends Thread {
	    public SimpleThread(String str) {
	        super(str);
	    }
	    public void run() {
	        for (int i = 0; i < 10; i++) {
	            System.out.println(i + " " + getName());
	            try {
	                sleep((int)(Math.random() * 1000));
	            } catch (InterruptedException e) {}
	        }
	        System.out.println("DONE! " + getName());
	    }
	}
	
	
}
