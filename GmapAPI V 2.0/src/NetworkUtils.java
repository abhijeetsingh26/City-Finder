import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class NetworkUtils {
	 public static String getJSONDataFromUrl(String requestUrl) {
	        URL url = createUrl(requestUrl);
	        String jsonResponse = null;
	        try {
	            jsonResponse = makehttpRequest(url);
	        } catch (IOException e) {
	        }
	        return jsonResponse;
	    }

	    private static URL createUrl(String stringUrl) {
	        URL url = null;
	        try {
	            url = new URL(stringUrl);
	        } catch (MalformedURLException e) {
	        }
	        return url;
	    }

	    private static String makehttpRequest(URL url) throws IOException {
	        String jsonResponse = "";
	        if (url == null) {
	            return jsonResponse;
	        }

	        HttpURLConnection urlConnection = null;
	        InputStream inputStream = null;
	        try {
	            urlConnection = (HttpURLConnection) url.openConnection();
	            urlConnection.setReadTimeout(10000 /* milliseconds */);
	            urlConnection.setConnectTimeout(15000 /* milliseconds */);
	            urlConnection.setRequestMethod("GET");
	            urlConnection.connect();

	            if (urlConnection.getResponseCode() == 200) {
	                inputStream = urlConnection.getInputStream();
	                jsonResponse = readFromStream(inputStream);
	            } else {
	            }

	        } catch (IOException e) {
	        } finally {
	            if (urlConnection != null) {
	                urlConnection.disconnect();
	            }
	            if (inputStream != null) {
	                inputStream.close();
	            }
	        }

	        return jsonResponse;

	    }

	    private static String readFromStream(InputStream inputStream) throws IOException {
	        StringBuilder output = new StringBuilder();
	        if (inputStream != null) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
	            BufferedReader reader = new BufferedReader(inputStreamReader);
	            String line = reader.readLine();
	            while (line != null) {
	                output.append(line);
	                line = reader.readLine();
	            }
	        }
	        return output.toString();
	    }
	    
}
