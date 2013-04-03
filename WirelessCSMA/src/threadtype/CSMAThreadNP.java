package threadtype;

import java.util.Random;

import util.LogUtil;
import exec.CSMADemo;

/*
 * Used for CSMA-Non Persistent demo
 */
public class CSMAThreadNP implements Runnable {

	private Boolean isBusy = false;

	@Override
	public void run() {
		boolean isGoingToWait = false;
		Random random = new Random();
		while (true) {
			if (isGoingToWait) {
				try {
					// waits random time max MAX_RANDOM_WAIT ms
					long time = (long) (random.nextFloat() * CSMADemo.NP_MAX_RANDOM_WAIT);
					LogUtil.printLogXYWait("Channel busy, Going to wait for random time(ms)=" + time);
					Thread.sleep(time);
					isGoingToWait = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			LogUtil.printLog("Checking if channel busy or not");
			synchronized (isBusy) {
				if (isBusy) {
					isGoingToWait = true;
					continue;
				}
				isBusy = true;
			}
			LogUtil.printLogXYWait("Channel free, Going to take control");
			synchronized (this) {
				LogUtil.printLogXYRun("Started using channel");
				try {
					Thread.sleep(CSMADemo.CHANNEL_USE_PERIOD);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LogUtil.printLogXYRun("Releasing channel");
				synchronized (isBusy) {
					isBusy = false;
				}
			}
			break;
		}
	}

}