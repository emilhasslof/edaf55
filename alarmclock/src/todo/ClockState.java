package todo;

import se.lth.cs.realtime.semaphore.MutexSem;
import se.lth.cs.realtime.semaphore.Semaphore;

public class ClockState {
	
	int clockTime;
	int alarmTime;
	private Semaphore mutex;
	
	public ClockState() {
		mutex = new MutexSem();
	}
	
	public void setClockTime(int clockTime) {
		mutex.take();
		this.clockTime = clockTime;
		mutex.give();
	}
	public void setAlarmTime(int alarmTime) {
		this.alarmTime = alarmTime;
	}
	public int getClockTime() {
		return clockTime;
	}
	public int getAlarmTime() {
		return alarmTime;
	}

	public void increment() {
		mutex.take();
		clockTime++;
		mutex.give();
	}
	public boolean alarmTriggered() {
		return alarmTime == clockTime;
	}
}
