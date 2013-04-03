package util;

import exec.CSMADemo;

public class LogUtil {

	public static float printLog(String iMessage) {
		float timeTaken = (float) (System.currentTimeMillis() - CSMADemo.START_TIME) / 1000;
		String theMessage = timeTaken + " sec - " + Thread.currentThread().getName() + "-" + ThreadUtil.getThreadCustomId() + " - " + iMessage;
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
