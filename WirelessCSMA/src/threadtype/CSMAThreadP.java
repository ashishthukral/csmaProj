package threadtype;

import java.util.Random;

import util.LogUtil;
import exec.CSMADemo;

/*
 * Used for CSMA- 1,P Persistent demo
 */
public class CSMAThreadP implements Runnable {

	private boolean _is1P;

	public CSMAThreadP(boolean iIs1P) {
		_is1P = iIs1P;
	}

	@Override
	public void run() {
		Random random = new Random();
		boolean isGoingToWait = false;
		while (true) {
			if (!_is1P) {
				if (isGoingToWait) {
					try {
						LogUtil.printLogXYWait("Client decided not to transmit, wait for fixed time (ms)=" + CSMADemo.PP_WAIT_UNIT_TIME);
						Thread.sleep(CSMADemo.PP_WAIT_UNIT_TIME);
						isGoingToWait = false;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			LogUtil.printLogXYWait("Client waiting for channel to get free");
			synchronized (this) {
				LogUtil.printLogXYWait("Channel free, Going to take control");
				if (!_is1P) {
					int theStaticProb = (int) (CSMADemo.PP_PROB * 100);
					// generates 0 to 99
					int theDynamicProb = random.nextInt(100);
					LogUtil.printLog(Thread.currentThread().getName() + " theStaticProb=" + theStaticProb + ",,, theDynamicProb=" + theDynamicProb);
					if (theDynamicProb > theStaticProb) {
						isGoingToWait = true;
						continue;
					}
				}
				LogUtil.printLogXYRun("Started using channel");
				try {
					Thread.sleep(CSMADemo.CHANNEL_USE_PERIOD);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LogUtil.printLogXYRun("Releasing channel");
			}
			break;
		}
	}
}
