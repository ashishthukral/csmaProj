package threadtype;

import java.util.Random;

import util.LogUtil;
import exec.CSMAP;

/*
 * Used for CSMA-Non Persistent demo
 */
public class CSMAThreadNP implements Runnable {

	private Boolean isBusy = false;

	// init xyseries to plot points on it

	@Override
	public void run() {
		boolean isGoingToWait = false;
		Random random = new Random();
		while (true) {
			if (isGoingToWait) {
				try {
					// waits random time max 3 secs
					long time = (long) (random.nextFloat() * 3000);
					LogUtil.printLog("going to wait for random time(ms)=" + time);
					Thread.sleep(time);
					isGoingToWait = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			LogUtil.printLog("checking for channel");
			synchronized (isBusy) {
				if (isBusy) {
					isGoingToWait = true;
					continue;
				}
				isBusy = true;
			}
			LogUtil.printLog("******************");
			synchronized (this) {
				LogUtil.printLogXY("started using channel");
				try {
					Thread.sleep(CSMAP.CHANNEL_USE_PERIOD);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (isBusy) {
					isBusy = false;
				}
				LogUtil.printLogXY("releasing channel");
			}
			break;
		}
	}

}