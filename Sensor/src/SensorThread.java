import java.io.IOException;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class SensorThread implements Runnable{

	private String _url;

	public SensorThread(String iQuery){
		_url=iQuery;
	}

	@Override
	public void run() {
		sensorHttpGetRequest();
		//		StringBuilder theStringBuilder=sensorHttpGetRequest();
		//		System.out.println(theStringBuilder.toString());
	}

	/**
	 * Creates http GET request and saves time taken to execute in a list
	 * @return StringBuilder - textual response
	 */
	private StringBuilder sensorHttpGetRequest(){
		// Create a new HttpClient
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(_url);
		StringBuilder theStringBuilder=null;
		try {
			// Execute HTTP Get Request
			StopWatch sw=new StopWatch();
			sw.start();
			httpclient.execute(httpget);
			//			HttpResponse response = httpclient.execute(httpget);
			sw.stop();
			Long time= sw.getTime();
			// saves in the static list in synchronized manner
			synchronized (HttpClientSensorTester.TIME_LIST) {
				HttpClientSensorTester.TIME_LIST.add(time);
			}
			//			System.out.println("Time taken (ms)="+sw.getTime());
			//			theStringBuilder=HttpUtil.inputStreamToString(response.getEntity().getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return theStringBuilder;
	}


}
