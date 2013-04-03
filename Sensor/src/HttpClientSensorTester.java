import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.jfree.data.xy.XYSeries;

//Project 22 How do you obtain average processing time per client in a typical
// internet environment? Ref: slide 10 of chapter 12.
public class HttpClientSensorTester {

	private static final String Y_AXIS_TITLE = "Avg processing Time for Each client (ms)";
	private static final String X_AXIS_TITLE = "Number of Clients";
	private static final String GRAPH_TITLE = "Processing time per client";
	private static final Integer MAX_THREADS=500;
	private static final Integer SENSOR_GROUP_INCREMENT=20;

	private static final String URL="http://localhost:22001/gsn?";
	public static List<Long> TIME_LIST;
	private List<XYSeries> _xySeriesList=new ArrayList<XYSeries>();;



	public static void main(String[] args) throws InterruptedException {
		HttpClientSensorTester aHttpClientTester=new HttpClientSensorTester();
		aHttpClientTester.readSensors("1");
		aHttpClientTester.readSensors("60");
		aHttpClientTester.readSensors("300");
		// Generate graph for executed sensor reads
		new GraphMaker(GRAPH_TITLE,X_AXIS_TITLE,Y_AXIS_TITLE,aHttpClientTester.getXySeriesList());
	}

	/**
	 * Reads sensors by creating http client threads n saving info to generate graphs
	 * @param iWindow - no of sensor readings to get
	 */
	private void readSensors(String iWindow){
		List<NameValuePair> iNameValuePairs=new ArrayList<NameValuePair>();
		// "REQUEST_ONE_SHOT_QUERY","114"
		iNameValuePairs.add(new BasicNameValuePair("REQUEST", "114"));
		iNameValuePairs.add(new BasicNameValuePair("name", "multiformattemperaturehandler"));
		iNameValuePairs.add(new BasicNameValuePair("window", iWindow));
		iNameValuePairs.add(new BasicNameValuePair("fields", "LIGHT,TEMPERATURE"));
		// create query to append for GET query
		String theQuery = URLEncodedUtils.format(iNameValuePairs, "utf-8");
		theQuery=URL+theQuery;
		System.out.println("\nWindow (no of sensors) size="+iWindow);
		System.out.println(theQuery);
		// create sensorThread Http Client instance
		SensorThread aSensorThread=new SensorThread(theQuery);
		// init xyseries to plot points on it
		XYSeries aXYSeries = new XYSeries("Window="+iWindow);
		// spawn http client threads to read sensors
		for(int sensorIncrease=SENSOR_GROUP_INCREMENT;sensorIncrease<=MAX_THREADS;sensorIncrease+=SENSOR_GROUP_INCREMENT){
			spawnThreads(aSensorThread, sensorIncrease,aXYSeries);
		}
		// add xypoints to list
		_xySeriesList.add(aXYSeries);
	}

	/**
	 * Spawn http client threads to read sensors
	 * @param aSensorThread - instance
	 * @param sensorIncrease - bunches of sensors to read
	 * @param iXYSeries - graph points
	 */
	private void spawnThreads(SensorThread aSensorThread,int sensorIncrease,XYSeries iXYSeries) {
		TIME_LIST=new ArrayList<Long>(sensorIncrease);
		Thread[] threadArr=new Thread[sensorIncrease];
		for(int i=0;i<sensorIncrease;i++){
			threadArr[i]=new Thread(aSensorThread);
			threadArr[i].start();
		}
		// check until all threads finished
		OUTER: while(true){
			for(int i=0;i<sensorIncrease;i++){
				if(threadArr[i].isAlive())
					continue OUTER;
			}
			break;
		}
		Integer size=HttpClientSensorTester.TIME_LIST.size();
		Double avg=calcAvg();
		System.out.println(size+" clients=Avg Time taken (ms)="+avg);
		// add clients/avg time info to xy points
		iXYSeries.add(size,avg);
	}

	/**
	 * Calculates average
	 * @return Double - avg time of all times contained in list
	 */
	private Double calcAvg(){
		Double avg=0.0;
		Long total=0L;
		for(Long time:HttpClientSensorTester.TIME_LIST){
			total+=time;
		}
		avg=(double) (total/HttpClientSensorTester.TIME_LIST.size());
		return avg;
	}

	public List<XYSeries> getXySeriesList() {
		return _xySeriesList;
	}

}
