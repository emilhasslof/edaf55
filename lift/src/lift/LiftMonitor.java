package lift;

public class LiftMonitor {

	private final static int NBR_PASSENGERS = 10;
	private final static boolean IMPROVED_VERSION = true;

	int here; // If here != next , here (floor number) tells from which floor
				// the lift is moving and next to which floor it is moving.

	int next; // If here == next , the lift is standing still on the floor
				// given by here.

	int[] waitEntry; // The number of persons waiting to enter the lift at the
						// various floors.

	int[] waitExit; // The number of persons (inside the lift) waiting to leave
					// the lift at the various floors.

	int load; // The number of people currently occupying the lift.

	private LiftView lv;
	
	private boolean goingUp;

	public LiftMonitor() {
		waitEntry = new int[7];
		waitExit = new int[7];
		lv = new LiftView();
		here = 0;
		next = 0;
		goingUp = true;
	}

	synchronized public void travel(int origin, int destination) {
		try {
			waitEntry[origin]++;
			lv.drawLevel(origin, waitEntry[origin]);
			notifyAll();
			while (here != origin || here != next || load == 4) {
				wait();
			}
			waitEntry[origin]--;
			waitExit[destination]++;
			load++;
			lv.drawLevel(origin, waitEntry[origin]);
			lv.drawLift(origin, load);
			notifyAll();
			while (here != destination || here != next) {
				wait();
			}
			load--;
			waitExit[destination]--;
			lv.drawLift(destination, load);
			notifyAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	synchronized private void sendNotify() {
		notifyAll();
	}
	
	synchronized private void setDestination(int destination){
		next = destination;
	}

	synchronized private void arrive(){
		here = next;
	}
	
	synchronized private boolean noPassengersWaiting(){
		for(int i = 0; i < 7; i++){
			if(waitEntry[i] != 0){
				return false;
			}
		}
		return true;
	}
	
	private int findDestinationOriginal(){
		if(here == 0){
			goingUp = true;
		} else if (here == 6){
			goingUp = false;
		}
		if(goingUp){
			return here + 1;
		} else {
			return here - 1;
		}
	}
	
	synchronized private int findDestination(){
		int destination = 0;
		if(here < 6){
			destination = here + 1;
		}
		while(load == 0 && noPassengersWaiting()){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(load == 0){
			int min = 6;
			int temp = 0;
			for(int i = 0; i < 7; i++){
				if(waitEntry[i] != 0){
					temp = Math.abs(here - i);
					if (temp < min){
						min = temp;
						destination = i;
					}
				}
			}
		} else {
			int min = 6;
			int temp = 0;
			for(int i = 0; i < 7; i++){
				if(waitExit[i] != 0){
					temp = Math.abs(here - i);
					if (temp < min){
						min = temp;
						destination = i;
					}
				}
			}
		}
		return destination;
	}
	public void moveLift() {
		try {
			int destination = 0;
			if(IMPROVED_VERSION){
				destination = findDestination();
			} else {
				destination = findDestinationOriginal();
			}
			setDestination(destination);
			lv.moveLift(here, next);
			arrive();
			sendNotify();
			Thread.sleep(200);		//Elevator standing still
			sendNotify();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] arghs) {
		LiftMonitor lm = new LiftMonitor();

		LiftThread lt = new LiftThread(lm);
		lt.start();

		for (int i = 0; i < NBR_PASSENGERS; i++) {
			PersonThread p = new PersonThread(lm);
//			p.setPriority(10);
			p.start();
		}
	}

}
