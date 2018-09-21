package todo;

import done.ClockInput;
import done.ClockOutput;
import se.lth.cs.realtime.semaphore.MutexSem;
import se.lth.cs.realtime.semaphore.Semaphore;

public class TimeCounterThread extends Thread {

	long incrementedTime;
	long now;
	long dt;
	int alarmTicker;
	
	ClockInput i;
	ClockOutput o;
	TimeState state;
	
	private Semaphore mutex;


	public TimeCounterThread(ClockInput i, ClockOutput o, TimeState state) {
		this.i = i;
		this.o = o;
		this.state = state;
		mutex = new MutexSem();
	}

	public void run() {

		incrementedTime = System.currentTimeMillis();

		while (true) {
			incrementedTime += 1000;
			now = System.currentTimeMillis();
			dt = incrementedTime - now;
			try {
				Thread.sleep(dt);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			state.increment();
			o.showTime(state.getClockTime());
			if(state.isAlarmTime() && i.getAlarmFlag()) {
				mutex.take();
				alarmTicker = 20;
				mutex.give();
			}
			if(alarmTicker > 0) {
				o.doAlarm();
				mutex.take();
				alarmTicker--;
				mutex.give();
			}
		}

	}
	
	public void killAlarm() {
		mutex.take();
		alarmTicker = 0;
		mutex.give();
	}
	
}
