
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class ClientSide{

	public static String OkHttpGet(OkHttpClient client, String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String OkHttpPostJson(OkHttpClient client, String url, String json) throws IOException {

    	MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

	public static void main(String[] args) {
		
		OkHttpClient client = new OkHttpClient();
		
		String baseUrl="http://127.0.0.1:5000/upload/";

		try
		{	
			//get
			String response = OkHttpGet(client, baseUrl);
        	System.out.println(response);

        	//post
        	String json = "{\"username\":\"admin\",\"id\": 42}";
	        response = OkHttpPostJson(client, baseUrl, json);
	        System.out.println(response);
		}
		catch(Exception e)
		{
			System.out.println("ee");
		}
		
	}
}