package lift;

public class LiftThread extends Thread {

	private LiftMonitor lm;
	private int floor;

	public LiftThread(LiftMonitor lm) {
		this.lm = lm;
		floor = 0;
	}

	public void run() {
		while (true) {
//			//moving up
//			for (int i = 0; i < 6; i++) {
//				lm.moveLift(i + 1);
//			}
//			//moving down
//			for (int i = 6; i > 0; i--) {
//				lm.moveLift(i - 1);
//			}
			lm.moveLift();
		}
	}

}
