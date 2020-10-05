package com.APILibrary;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DynamicJson {

	@Test(dataProvider="BooksData")
	public void addBook(String isbn,String aisle) {
		
		RestAssured.baseURI = "http://216.10.245.166";

		String resp = given().log().all().header("Content-Type","application/json")
		.header("User-Agent","PostmanRuntime/7.26.2")
		.header("Accept","*/*")
		.header("Accept-Encoding","gzip, deflate, br")
		.header("Connection","keep-alive")
		.body(Payload.Addbook(isbn,aisle))
		.when()
		.post("Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		
		JsonPath js = ReUsableMethods.rawToJson(resp);
		String id = js.get("ID");
		System.out.println(id);
		
	}//End of addBook
	
	
	
	@DataProvider(name="BooksData")
	public Object[][] getdata() {
		
		
		return new Object[][] {{"book1","127"},{"book2","128"},{"book3","129"}};
	}//End of getdata
	
	
	public static String GenerateStringFromResource(String path) throws IOException {
		
		return new String(Files.readAllBytes(Paths.get(path)));
		
	}//End of Generate String
	
}//End of Class
