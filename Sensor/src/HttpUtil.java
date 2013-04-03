

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpUtil {

	// Fast Implementation
	public static StringBuilder inputStreamToString(InputStream iInputStream) throws IOException {
		String line = "";
		StringBuilder theResponse = new StringBuilder();
		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(iInputStream));
		// Read response until the end
		while ((line = rd.readLine()) != null) { 
			theResponse.append(line); 
		}
		// Return full string
		return theResponse;
	}

}
