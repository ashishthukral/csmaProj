package threadtype;

import java.util.Random;

import exec.CSMAP;

/*
 * Used for CSMA- 1,P Persistent demo
 */
public class CSMAThreadP implements Runnable {

	private float _prob;

	public CSMAThreadP(float iProb) {
		_prob = iProb;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " - checking for channel");
		int theStaticProb = (int) (_prob * 100);
		Random random = new Random();
		boolean isGoingToWait = false;
		while (true) {
			if (isGoingToWait) {
				try {
					// System.out.println(Thread.currentThread().getName() + " - going to wait for fixed time(ms)=" + CSMAP.CHANNEL_WAIT_UNIT_TIME);
					Thread.sleep(CSMAP.CHANNEL_WAIT_UNIT_TIME);
					isGoingToWait = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			synchronized (this) {
				// generates 0 to 99
				int theDynamicProb = random.nextInt(100);
				System.out.println(Thread.currentThread().getName() + " theStaticProb=" + theStaticProb + ",,, theDynamicProb=" + theDynamicProb);
				if (theDynamicProb > theStaticProb) {
					isGoingToWait = true;
					System.out.println(Thread.currentThread().getName() + " - going to wait for fixed time(ms)=" + CSMAP.CHANNEL_WAIT_UNIT_TIME);

					continue;
				}
				System.out.println(Thread.currentThread().getName() + " - started using channel");
				try {
					Thread.sleep(CSMAP.CHANNEL_USE_PERIOD);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + " - releasing channel");
			}
			break;
		}
	}
}
