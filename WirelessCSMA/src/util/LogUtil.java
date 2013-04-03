package util;

import exec.CSMAP;

public class LogUtil {

	public static void printLog(String iMessage) {
		float timeTaken = (float) (System.currentTimeMillis() - CSMAP.START_TIME) / 1000;
		String theMessage = timeTaken + " sec - " + Thread.currentThread().getName() + " - " + iMessage;
		System.out.println(theMessage);
	}

	public static void printLogXY(String iMessage) {
		float timeTaken = (float) (System.currentTimeMillis() - CSMAP.START_TIME) / 1000;
		String theMessage = timeTaken + " sec - " + Thread.currentThread().getName() + "=" + ThreadUtil.getThreadCustomId() + " - " + iMessage;
		System.out.println(theMessage);
		ThreadUtil.addTimeGraph(timeTaken);
	}
}
