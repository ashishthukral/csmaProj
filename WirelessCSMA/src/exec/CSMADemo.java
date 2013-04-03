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
	public static final String X_AXIS_GRAPH_TITLE = "Time (sec)";
	public static final String Y_AXIS_GRAPH_TITLE = "Client Number";

	public static long THREAD_START_INTERVAL = 500L;
	public static int THREAD_COUNT = 10;
	public static long CHANNEL_USE_PERIOD = 1500L;
	public static Long START_TIME;
	public static long PP_WAIT_UNIT_TIME = 150L;
	public static final Float PP_PROB = 0.39F;

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
		obj.csmaPPTester();
		obj.csma1PTester();
		obj.csmaNPTester();
		LogUtil.printLog("*** main END ***");
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
	private void csmaPPTester() {
		System.out.println("\n");
		START_TIME = System.currentTimeMillis();
		String graphTitle = "P-Persistent Channel Use Graph";
		LogUtil.printLog("*** csmaPPTester ***");
		commonHelper(graphTitle, new CSMAThreadP(false));
		LogUtil.printLog("*** csmaPPTester END ***");
	}

	/*
	 * 1. If the medium is idle, transmit immediately
	 * 
	 * 2. If the medium is busy, continue to listen until medium becomes idle, and then transmit immediately
	 */
	private void csma1PTester() {
		System.out.println("\n");
		START_TIME = System.currentTimeMillis();
		String graphTitle = "1-Persistent Channel Use Graph";
		LogUtil.printLog("*** csma1PTester ***");
		commonHelper(graphTitle, new CSMAThreadP(true));
		LogUtil.printLog("*** csma1PTester END ***");
	}

	/*
	 * 1. If the medium is idle, transmit immediately
	 * 
	 * 2. If the medium is busy, wait a random amount of time and repeat Step 1
	 */
	private void csmaNPTester() {
		System.out.println("\n");
		START_TIME = System.currentTimeMillis();
		String graphTitle = "N-Persistent Channel Use Graph";
		LogUtil.printLog("*** csmaNPTester ***");
		commonHelper(graphTitle, new CSMAThreadNP());
		LogUtil.printLog("*** csmaNPTester END ***");
	}

	private void commonHelper(String iGraphTitle, Runnable iRunnable) {
		List<Thread> theThreads = ThreadUtil.threadStart(THREAD_COUNT, iRunnable, THREAD_START_INTERVAL);
		for (Thread aThread : theThreads) {
			try {
				aThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int timeTaken = (int) (System.currentTimeMillis() - START_TIME) / 1000;
		timeTaken += 5;
		new GraphMaker(iGraphTitle, X_AXIS_GRAPH_TITLE, Y_AXIS_GRAPH_TITLE, ThreadUtil.THREAD_NAME_XY_MAP.values(), timeTaken);
		cleanUp();
	}

	private void cleanUp() {
		ThreadUtil.THREAD_NAME_ID_MAP = new HashMap<String, Integer>();
		ThreadUtil.THREAD_NAME_XY_MAP = new HashMap<String, List<XYSeries>>();
	}

}
