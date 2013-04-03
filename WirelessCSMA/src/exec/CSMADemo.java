package exec;

import graph.GraphMaker;

import java.util.HashMap;
import java.util.List;

import org.jfree.data.xy.XYSeries;

import threadtype.CSMAThreadNP;
import threadtype.CSMAThreadP;
import util.LogUtil;
import util.ThreadUtil;

public class CSMADemo {

	private static long THREAD_START_INTERVAL;
	public static int THREAD_COUNT;
	public static long CHANNEL_USE_PERIOD;
	public static long CHANNEL_WAIT_UNIT_TIME;

	public static final Float PROB_P = 0.39F;
	public static final Float PROB_1 = 1.00F;
	public static Long START_TIME;

	// static {
	// int randomLimit = 100 / THREAD_COUNT;
	// Random random = new Random();
	// // 0 to randomLimit-1
	// // np<1
	// // PROB = (float) random.nextInt(randomLimit) / 100;
	// // PROB = 0.19F;
	// // PROB_P = 0.39F;
	//
	// // PROB = 1.00F;
	// }

	public static void main(String[] args) {
		CSMADemo obj = new CSMADemo();
		// obj.testPP();
		obj.csma1PTester();
		// obj.csmaNPTester();
		LogUtil.printLog("*** main END ***");
	}

	private void cleanUp() {
		ThreadUtil.THREAD_NAME_ID_MAP = new HashMap<String, Integer>();
		ThreadUtil.THREAD_NAME_XY_MAP = new HashMap<String, List<XYSeries>>();
	}

	/*
	 * p-Persistent CSMA Protocol:
	 * 
	 * 1. If the medium is idle, transmit with probability p, and delay for one time unit with probability (1 - p) (time unit = length of propagation delay)
	 * 
	 * 2. If the medium is busy, continue to listen until medium becomes idle, then go to Step 1
	 * 
	 * 3. If transmission is delayed by one time unit, continue with Step 1
	 */
	private void testPP() {
		System.out.println("*** testPP ***");
		long start = System.currentTimeMillis();
		CSMAThreadP threadP = new CSMAThreadP(PROB_P, false);
		List<Thread> theThreads = ThreadUtil.threadStart(THREAD_COUNT, threadP, THREAD_START_INTERVAL);
		OUTER: while (true) {
			try {
				Thread.sleep(CHANNEL_USE_PERIOD);
				System.out.println("testPP Checking if all threads dead or not");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Thread aThread : theThreads) {
				if (aThread.isAlive())
					continue OUTER;
			}
			break;
		}
		long end = System.currentTimeMillis();
		System.out.println("total time (secs)=" + (float) (end - start) / 1000);
	}

	/*
	 * 1. If the medium is idle, transmit immediately
	 * 
	 * 2. If the medium is busy, continue to listen until medium becomes idle, and then transmit immediately
	 */
	private void csma1PTester() {
		THREAD_START_INTERVAL = 500L;
		THREAD_COUNT = 10;
		CHANNEL_USE_PERIOD = 1500L;
		CHANNEL_WAIT_UNIT_TIME = 150L;
		String graphTitle = "1-Persistent Channel Use Graph";
		String xAxisTitle = "Time (sec)";
		String yAxisTitle = "Client Number";
		START_TIME = System.currentTimeMillis();
		LogUtil.printLog("*** csma1PTester ***");
		CSMAThreadP thread1P = new CSMAThreadP(PROB_1, true);
		List<Thread> theThreads = ThreadUtil.threadStart(THREAD_COUNT, thread1P, THREAD_START_INTERVAL);
		for (Thread aThread : theThreads) {
			try {
				aThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		LogUtil.printLog("*** csma1PTester END ***");
		int timeTaken = (int) (System.currentTimeMillis() - START_TIME) / 1000;
		timeTaken += 5;
		new GraphMaker(graphTitle, xAxisTitle, yAxisTitle, ThreadUtil.THREAD_NAME_XY_MAP.values(), timeTaken);
		cleanUp();
	}

	/*
	 * 1. If the medium is idle, transmit immediately
	 * 
	 * 2. If the medium is busy, wait a random amount of time and repeat Step 1
	 */
	private void csmaNPTester() {
		THREAD_START_INTERVAL = 500L;
		THREAD_COUNT = 10;
		CHANNEL_USE_PERIOD = 1500L;
		CHANNEL_WAIT_UNIT_TIME = 150L;
		String graphTitle = "N-Persistent Channel Use Graph";
		String xAxisTitle = "Time (sec)";
		String yAxisTitle = "Client Number";
		START_TIME = System.currentTimeMillis();
		LogUtil.printLog("*** csmaNPTester ***");
		CSMAThreadNP threadNP = new CSMAThreadNP();
		List<Thread> theThreads = ThreadUtil.threadStart(THREAD_COUNT, threadNP, THREAD_START_INTERVAL);
		for (Thread aThread : theThreads) {
			try {
				aThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		LogUtil.printLog("*** csmaNPTester END ***");
		int timeTaken = (int) (System.currentTimeMillis() - START_TIME) / 1000;
		timeTaken += 5;
		new GraphMaker(graphTitle, xAxisTitle, yAxisTitle, ThreadUtil.THREAD_NAME_XY_MAP.values(), timeTaken);
		cleanUp();
	}

}
