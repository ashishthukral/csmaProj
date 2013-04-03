package threadtype;

import java.util.Random;

import util.LogUtil;
import exec.CSMAP;

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
					// waits random time max 3 secs
					long time = (long) (random.nextFloat() * 3000);
					LogUtil.printLogXYWait("Channel busy, Going to wait for random time(ms)=" + time);
					Thread.sleep(time);
					isGoingToWait = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			LogUtil.printLog("Checking for channel");
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
					Thread.sleep(CSMAP.CHANNEL_USE_PERIOD);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (isBusy) {
					isBusy = false;
				}
				LogUtil.printLogXYRun("Releasing channel");
			}
			break;
		}
	}

}