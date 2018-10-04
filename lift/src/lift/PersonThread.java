package lift;

public class PersonThread extends Thread {

	private LiftMonitor lm;
	private int origin;
	private int destination;

	public PersonThread(LiftMonitor lm) {
		this.lm = lm;
		destination = (int) (Math.random() * 7);
	}

	public void run() {
		while (true) {
			try {
				long delay = 1000 * ((int) (Math.random() * 46.0));
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			origin = destination;
			while (destination == origin) {
				destination = (int) (Math.random() * 7);
			}
			lm.travel(origin, destination);
		}
	}

}
