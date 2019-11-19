
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MultipartBody;

import java.io.File;
import java.nio.file.Files;

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


    public static String OkHttpPostImage(OkHttpClient client, String url, String img_path) throws IOException {
        
        // System.out.println(file.exists());


    	MediaType MEDIA_TYPE_ALL = MediaType.parse("image/*");

		File file = new File(img_path);
		byte[] fileContent = Files.readAllBytes(file.toPath());

    	// RequestBody imageBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        // RequestBody body = MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("data", img_path, RequestBody.create(MEDIA_TYPE_PNG, file)).build();
        // RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("image", img_path, RequestBody.create(MEDIA_TYPE_ALL, fileContent)).build();
        // RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("name","hello").addFormDataPart("image", img_path, RequestBody.create(MEDIA_TYPE_ALL, fileContent)).build();
       	
       	MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
       	multipartBodyBuilder.addFormDataPart("image", img_path, RequestBody.create(MEDIA_TYPE_ALL, fileContent)).build();

       	RequestBody body = multipartBodyBuilder.build();
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
        	//post
	        String img_path = "data/a3.jpeg";
	        String response = OkHttpPostJson(client, baseUrl, img_path);
	        System.out.println(response);
		}
		catch(Exception e)
		{
			System.out.println("ee");
		}
	}
}