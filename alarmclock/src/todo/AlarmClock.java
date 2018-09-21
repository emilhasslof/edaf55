package todo;
import done.*;
import se.lth.cs.realtime.semaphore.Semaphore;
import se.lth.cs.realtime.semaphore.MutexSem;

public class AlarmClock extends Thread {

	private static ClockInput	input;
	private static ClockOutput	output;
	private static Semaphore	sem;
	
	private TimeState state;
	private TimeCounterThread timeCounter;
	
	private int lastChoice;
	private int currentChoice;

	public AlarmClock(ClockInput i, ClockOutput o) {
		input = i;
		output = o;
		sem = input.getSemaphoreInstance();
		lastChoice = 0;
		state = new TimeState();
		timeCounter = new TimeCounterThread(input, output, state);
	}

	// The AlarmClock thread is started by the simulator. No
	// need to start it by yourself, if you do you will get
	// an IllegalThreadStateException. The implementation
	// below is a simple alarmclock thread that beeps upon 
	// each keypress. To be modified in the lab.
	public void run() {
		timeCounter.start();
		while (true) {
			sem.take();
			lastChoice = currentChoice;
			currentChoice = input.getChoice();
			if(currentChoice == 0) {
				if(lastChoice == 1) {		//Set Alarm
					state.setAlarmTime(input.getValue());
				}
				if(lastChoice == 2) {		//Set Time
					state.setClockTime(input.getValue());
				}
			}
			timeCounter.killAlarm();
		}
	}
}
