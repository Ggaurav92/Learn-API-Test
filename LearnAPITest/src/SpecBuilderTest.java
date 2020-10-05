import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

import com.pojo.AddPlace;
import com.pojo.Location;


public class SpecBuilderTest {

	public static void main(String[] args) {
		
		//RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		//Initializing the POJO class
		AddPlace p = new AddPlace();
		//adding the values by using POJO class
		p.setAccuracy(50);
		p.setName("Frontline house");
		p.setPhone_number("(+91) 983 893 3937");
		p.setAddress("29, side layout, cohen 09");
		p.setWebsite("http://google.com");
		p.setLanguage("French-IN");
		List<String> myList = new ArrayList<String>();
		myList.add("shoe park");
		myList.add("shop");
		p.setTypes(myList);

		
		//To add values to Location first we have to create the object of Location
		Location l = new Location();
		l.setLat(-38.383494);
		l.setLng(33.427362);
		p.setLocation(l);
		
		 RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123")
		.setContentType(ContentType.JSON).build();
		
		 ResponseSpecification resspec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		 
		RequestSpecification res = given().spec(req)
		.body(p);
		
		Response response = res.when().post("/maps/api/place/add/json")
		.then().spec(resspec).extract().response();

		
		String responseString  = response.asString();
		System.out.println(responseString);
		
	}//End of psvm

}//End of Class
