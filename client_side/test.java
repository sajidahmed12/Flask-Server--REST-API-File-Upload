
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.MultipartBody;


import java.io.File;
import java.nio.file.Files;


class test{

	public static void main(String[] args) {

		String img_path = "data/a3.jpeg";
		MediaType MEDIA_TYPE_ALL = MediaType.parse("image/*jpeg");

		try
		{	
			File file = new File(img_path);
			byte[] fileContent = Files.readAllBytes(file.toPath());

			RequestBody body2 = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("image", img_path, RequestBody.create(MEDIA_TYPE_ALL, fileContent)).build();
			RequestBody body = RequestBody.create(MEDIA_TYPE_ALL, fileContent);

			System.out.println(body2);
		}
		catch(Exception e){
			System.out.println("error");
		}
		
	}
}