package util;

import exec.CSMAP;

public class LogUtil {

	public static float printLog(String iMessage) {
		float timeTaken = (float) (System.currentTimeMillis() - CSMAP.START_TIME) / 1000;
		String theMessage = timeTaken + " sec - " + Thread.currentThread().getName() + " - " + iMessage;
		System.out.println(theMessage);
		return timeTaken;
	}

	public static void printLogXYRun(String iMessage) {
		float timeTaken = printLog(iMessage);
		ThreadUtil.addTimeRunGraph(timeTaken);
	}

	public static void printLogXYWait(String iMessage) {
		float timeTaken = printLog(iMessage);
		ThreadUtil.addTimeWaitGraph(timeTaken);
	}
}
