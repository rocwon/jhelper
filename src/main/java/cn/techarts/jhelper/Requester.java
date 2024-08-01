package cn.techarts.jhelper;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;

/**
 * Based on Java 9 New HTTP Client API.
 */
public final class Requester {
	
	public static final int HTTP_STATUS_OK = 200;
	public static final int TIMEOUT_SECONDS = 300;
	private static final String CONTENT_TYPE_FILE = "application/octet-stream";
	public static final String CONTENT_TYPE_TEXT = "text/plain;charset=UTF-8";
	public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
	public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded;charset=UTF-8";
	
	private static HttpClient httpClient = null; //Shares the object for all requests to improve performance
	
	/**
	 * [GET] Detects the given URL is whether alive
	 * @return The HTTP status code is returned.<br>
	 *         -1 means an unknown error(generally TIMEOUT)
	 * */
	public static int ping(String url) {
		var client = createHttpClient();
		var request = HttpRequest.newBuilder().uri(URI.create(url))
				   .timeout(Duration.ofSeconds(3)).GET() //Short
				   .header("Content-Type", CONTENT_TYPE_FORM).build();
		try {
			var result = client.send(request, BodyHandlers.ofString());
			return result != null ? result.statusCode() : -1;
		}catch(Exception e) {
			return -1; //An unknown error(generally TIMEOUT) has been occurred
		}
	}
	
	/**
	 * Send a redirect request
	 */
	public static String redirect(String url, Map<String, String> parameters) {
		var request = createHttpGetRequest(url, parameters);
		return sendRequestSync(createHttpClient(true), request);
	}
	
	public static String get(String url, Map<String, String> parameters) {
		var request = createHttpGetRequest(url, parameters);
		return sendRequestSync(createHttpClient(), request);
	}
	
	/**
	 * Send plain text content
	 */
	public static String post(String url, String text) {
		var request = createHttpRequest(url, text);
		return sendRequestSync(createHttpClient(), request);
	}
	
	/**
	 * Send plain text content as async-mode
	 */
	public static void post(String url, String text, final HttpCallback callback) {
		var request = createHttpRequest(url, text);
		sendRequestAsync(createHttpClient(), request, callback);
	}
	
	/**
	 * Send form-data
	 */
	public static String post(String url, Map<String, String> data) {
		var request = createHttpRequest(url, data);
		return sendRequestSync(createHttpClient(), request);
	}
	
	/**
	 * Send form-data as async mode
	 */
	public static void post(String url, Map<String, String> data, final HttpCallback callback) {
		var request = createHttpRequest(url, data);
		sendRequestAsync(createHttpClient(), request, callback);
	}
	
	/**
	 * Send form-data with specified HTTP headers
	 */
	public static String post(String url, Map<String, String> data, Map<String, String> header) {
		var request = createHttpRequest(url, data, header);
		return sendRequestSync(createHttpClient(), request);
	}
	
	/**
	 * Send the payload with your specified content type(TEXT | JSON | FORM-DATA)
	 */
	public static String post(String url, String payload, String contentType) {
		var request = createHttpRequest(url, payload, contentType);
		return sendRequestSync(createHttpClient(), request);
	}
	
	/**
	 * Send a file
	 * @param file The full path of the file (e.g. /usr/local/bin/girl.jpg)
	 */
	public static String send(String url, String file) {
		var request = createHttpFileRequest(url, file);
		return sendRequestSync(createHttpClient(), request);
	}
	
	/**
	 * Send a file as async-mode
	 * @param file The full path of the file (e.g. /usr/local/bin/girl.jpg)
	 */
	public static void send(String url, String file, final HttpCallback callback) {
		var request = createHttpFileRequest(url, file);
		sendRequestAsync(createHttpClient(), request, callback);
	}
	
	//---------------------------------------------------------------------------------------------------------------------
	
	private static HttpClient createHttpClient(boolean redirect) {
		var client = HttpClient.newBuilder().version(Version.HTTP_1_1);
		if(redirect) client.followRedirects(Redirect.ALWAYS);
		return client.build();
		//.authenticator(Authenticator.getDefault()).build();
	}
	
	private static HttpClient createHttpClient() {
		if(httpClient == null) {
			httpClient = HttpClient.newBuilder().version(Version.HTTP_1_1).build();
			//.authenticator(Authenticator.getDefault()).build();
		}
		return httpClient;
	}
	
	private static HttpRequest createHttpGetRequest(String url, Map<String, String> data) {
		var uri = (Empty.is(data)) ? url : url.concat("?").concat(stringify(data));
		return HttpRequest.newBuilder().uri(URI.create(uri))
			   .timeout(Duration.ofSeconds(TIMEOUT_SECONDS)).GET()
			   .header("Content-Type", CONTENT_TYPE_FORM).build();
	}
	
	/**
	 * Make a request to send a set of encoded form parameters
	 * */
	private static HttpRequest createHttpRequest(String url, Map<String, String> data) {
		return HttpRequest.newBuilder().uri(URI.create(url))
			   .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
			   .POST(BodyPublishers.ofString(stringify(data)))
			   .header("Content-Type", CONTENT_TYPE_FORM).build();
	}
	
	/**
	 * Make a request to send the text data
	 * */
	private static HttpRequest createHttpRequest(String url, String data) {
		return HttpRequest.newBuilder().uri(URI.create(url))
			   .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
			   .POST(BodyPublishers.ofString(data))
			   .header("Content-Type", CONTENT_TYPE_TEXT).build();
	}
	
	private static HttpRequest createHttpRequest(String url, String data, String contentType) {
		return HttpRequest.newBuilder().uri(URI.create(url))
			   .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
			   .POST(BodyPublishers.ofString(data))
			   .header("Content-Type", contentType).build();
	}
	
	/**
	 * Make a request to send a java object as JSON
	 * */
	private static HttpRequest createHttpRequest(String url, Map<String, String> data, Map<String, String> headers) {
		var result = HttpRequest.newBuilder().uri(URI.create(url))
							    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
							    .POST(BodyPublishers.ofString(stringify(data)))
							    .header("Content-Type", CONTENT_TYPE_JSON);
		if(!Empty.is(headers)) headers.forEach((k,v)->result.header(k, v));
		return result.build();
	}
	
	private static HttpRequest createHttpFileRequest(String url, String file) {
		try{
			return HttpRequest.newBuilder().uri(URI.create(url))
			   .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
			   .POST(BodyPublishers.ofFile(Paths.get(file)))
			   .header("Content-Type", CONTENT_TYPE_FILE).build();
		}catch(FileNotFoundException e) {
			throw new RuntimeException("Failed to find the file [" + file + "].");
		}
	}
	
	private static String sendRequestSyncInternal(HttpClient client, HttpRequest request) throws Exception{
		var response = client.send(request, BodyHandlers.ofString());
		int statusCode = response.statusCode();
		if(statusCode != HTTP_STATUS_OK) {
			throw new RuntimeException("Failed with a code: " + response.statusCode());
		}
		return response.body();	//Returns the raw response body as text while success.
	}
	
	private static String sendRequestSync(HttpClient client, HttpRequest request) {
		try {
			return sendRequestSyncInternal(client, request);
		}catch(Exception e) {
			throw new RuntimeException("Failed to complete the request.", e);
		}
	}
	
	/**
	 * A common approach to handle an ASYNC HTTP request
	 * */
	private static void sendRequestAsync(HttpClient client, HttpRequest request, HttpCallback callback) {
		try {
			client.sendAsync(request, BodyHandlers.ofString()).thenApply(response->{
				if(response.statusCode() == HTTP_STATUS_OK) return response;
				throw new RuntimeException("Request failed with code [" + response.statusCode() + "]");
			}).thenApply(HttpResponse::body).thenAccept(body->callback.handle(body));
		}catch(Exception e) {
			throw new RuntimeException("Failed to complete the request.", e);
		}
	}
	
	/**
	 * Concatenates K-V parameters to an HTTP query-strings
	 * */
	private static String stringify(Map<String, String> params) {
		int index = 0;
		var result = new StringBuilder(256);
		if(Empty.is(params)) return "";
		for(var param : params.entrySet()) {
			if(index++ > 0) result.append("&");
			var key = param.getKey();
			if(Empty.is(key)) continue;
			var val = param.getValue();
			if(Empty.is(val)) continue;
			result.append(key).append("=").append(val);
		}
		return result.toString();
	}
	
	public static abstract class HttpCallback{
		public static HttpCallback empty() {
			return new HttpCallback() {
				@Override
				public void handle(String response) {
				}
			};
		}
		public abstract void handle(String response);
	}
}