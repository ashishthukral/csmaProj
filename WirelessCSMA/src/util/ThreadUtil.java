package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.xy.XYSeries;

public class ThreadUtil {

	public static Map<String, Integer> THREAD_NAME_ID_MAP = new HashMap<String, Integer>();
	public static Map<String, List<XYSeries>> THREAD_NAME_XY_MAP = new HashMap<String, List<XYSeries>>();

	public static List<Thread> threadStart(int iCount, Runnable iRunnable, long iStartInterval) {
		List<Thread> theThreads = new ArrayList<Thread>();
		for (int i = 0; i < iCount; i++) {
			String name = "Thread-" + (char) ('A' + i);
			Thread thread = new Thread(iRunnable, name);
			THREAD_NAME_ID_MAP.put(name, i + 1);
			List<XYSeries> list = new ArrayList<XYSeries>();
			list.add(new XYSeries(name));
			list.add(new XYSeries(name + "wait"));
			THREAD_NAME_XY_MAP.put(name, list);
			theThreads.add(thread);
		}
		for (Thread thread : theThreads) {
			thread.start();
			try {
				Thread.sleep(iStartInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return theThreads;
	}

	public static int getThreadCustomId() {
		return THREAD_NAME_ID_MAP.get(Thread.currentThread().getName());
	}

	public static void addTimeRunGraph(Number iX) {
		THREAD_NAME_XY_MAP.get(Thread.currentThread().getName()).get(0).add(iX, getThreadCustomId());
	}

	public static void addTimeWaitGraph(Number iX) {
		THREAD_NAME_XY_MAP.get(Thread.currentThread().getName()).get(1).add(iX, getThreadCustomId());
	}

}
