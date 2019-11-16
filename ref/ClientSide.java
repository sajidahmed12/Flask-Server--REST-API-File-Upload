
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import retrofit2.converter.gson.GsonConverterFactory;


class ClientSide{

	public static void main(String[] args) {
		
		System.out.println("hello client");
		connectServer();
	}


	public static void connectServer(){

	    String baseUrl= "http://192.168.0.102:8080";

	    Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();

	    JsonObject jsonObject = new JsonObject();
	    jsonObject.addProperty("title", "Hello");
	    jsonObject.addProperty("body", "Retrofit");
	    jsonObject.addProperty("userId", "2");

	    // ApiService service = retrofit.create(ApiService.class);
	    // Call<PostResponse> call = service.postData(jsonObject);

	    // call.enqueue(new Callback<PostResponse>() {
	    //     @Override
	    //     public void onResponse(Call<PostResponse> call, 
	    //     	Response<PostResponse> response) {
	    //         //hiding progress dialog
	    //         if(response.isSuccessful()){

	    //         	System.out.println("Post Title: "+response.body().getTitle()+"Body: "+response.body().getBody()+" PostId: "+response.body().getId());
	    //         }
	    //     }

	    //     @Override
	    //     public void onFailure(Call<PostResponse> call, Throwable t) {
	    //         call.cancel();
	    //         System.out.println("error");
	    //     }
	    // });
    }

    // private interface ApiService {
    //     @POST("post")
    //     Call<PostResponse> postData(
    //             @Body JsonObject body);
    // }
    // private class PostResponse{
    //     @SerializedName("title")
    //     private String title;
    //     @SerializedName("body")
    //     private String body;
    //     @SerializedName("userId")
    //     private String userId;
    //     @SerializedName("id")
    //     private Integer id;

    //     public void setTitle(String title){
    //         this.title = title;
    //     }
    //     public String getTitle(){
    //         return title;
    //     }
    //     public void setBody(String body){
    //         this.body = body;
    //     }
    //     public String getBody(){
    //         return body;
    //     }
    //     public void setUserId(String userId){
    //         this.userId = userId;
    //     }
    //     public String getUserId(){
    //         return userId;
    //     }
    //     public void setId(Integer id){
    //         this.id = id;
    //     }
    //     public Integer getId(){
    //         return id;
    //     }
    // }
}