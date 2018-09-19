package todo;

import done.ClockOutput;

public class TimeCounterThread extends Thread {

	long incrementedTime;
	long now;
	long dt;
	ClockOutput o;
	ClockState state;
	int alarmTicker;

	public TimeCounterThread(ClockOutput o, ClockState state) {
		this.o = o;
		this.state = state;
		alarmTicker = 0;
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
			if(state.alarmTriggered()) {
				alarmTicker = 20;
			}
			if(alarmTicker > 0) {
				o.doAlarm();
				alarmTicker--;
			}
		}

	}
	
}
