

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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


    public static String OkHttpPostImage(OkHttpClient client, String url, String file_path) throws IOException {

    	MediaType MEDIA_TYPE_ALL = MediaType.parse("*/*");

		File file = new File(file_path);
		byte[] fileContent = Files.readAllBytes(file.toPath());

    	// RequestBody imageBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        // RequestBody body = MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("data", file_path, RequestBody.create(MEDIA_TYPE_PNG, file)).build();
        // RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("image", file_path, RequestBody.create(MEDIA_TYPE_ALL, fileContent)).build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file_type","video").addFormDataPart("file", file_path, RequestBody.create(MEDIA_TYPE_ALL, fileContent)).build();
       	
       	// MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
       	// multipartBodyBuilder.addFormDataPart("file", file_path, RequestBody.create(MEDIA_TYPE_ALL, fileContent)).build();
       	// RequestBody body = multipartBodyBuilder.build();

        Request request = new Request.Builder().url(url).header("Accept", "application/json").header("Content-Type", "multipart/form-data").post(body).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

	public static void main(String[] args) {
		
		OkHttpClient client = new OkHttpClient();
		String baseUrl="http://127.0.0.1:8080/upload/";

		try
		{	
	        //String file_path = "data/a1.png";
            //String file_path = "data/a3.jpeg";
            String file_path = "data/a4.mp4";
	        String response = OkHttpPostImage(client, baseUrl, img_path);
	        System.out.println(response);
		}
		catch(Exception e)
		{
			System.out.println("ee");
		}
	}
}