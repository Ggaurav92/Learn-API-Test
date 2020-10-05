import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.annotations.Test;

import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class StaticJson {
	
	
	@Test()
	public void addBook() throws IOException {
		
		RestAssured.baseURI = "http://216.10.245.166";

		String resp = given().log().all().header("Content-Type","application/json")
		.header("User-Agent","PostmanRuntime/7.26.2")
		.header("Accept","*/*")
		.header("Accept-Encoding","gzip, deflate, br")
		.header("Connection","keep-alive")
		.body(StaticJson.GenerateStringFromResource("D:\\IT_Work\\Learning\\API Testing\\Addbookdetails.json"))
		.when()
		.post("Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		
		JsonPath js = ReUsableMethods.rawToJson(resp);
		String id = js.get("ID");
		System.out.println(id);
		
	}//End of addBook

	
public static String GenerateStringFromResource(String path) throws IOException {
		
		return new String(Files.readAllBytes(Paths.get(path)));
		
	}//End of Generate String
	
}//End of Class
