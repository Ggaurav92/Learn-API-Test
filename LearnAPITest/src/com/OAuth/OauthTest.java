package com.OAuth;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import com.pojo.Api;
import com.pojo.GetCourse;
import com.pojo.WebAutomation;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class OauthTest {

	public static void main(String[] args) throws InterruptedException {
		
		//Expected List of Course Titles
		String[] courseTitles = {"Selenium Webdriver Java","Cypress","Protractor"};
		
		//To  getCode we need to send it the Url to browser and then get code from it
		//There we also need to enter email and password
		//This can only be done through Selenium
		
		/*
		System.setProperty("webbdriver.chrome.driver", "D:\\Automation\\eclipse-workspace\\LearnAPITest\\Drivers\\chromedriver.exe");
		ChromeDriver driver = new ChromeDriver();
		
		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=verifyfdjss");
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys("g.gaurav2812");
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys("garje@2812$");
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		
		String url = driver.getCurrentUrl();
		*/
		
		String url = "https://rahulshettyacademy.com/getCourse.php?state=verifyfdjss&code=4%2F3QFxh352J7X4Fj9Y1ocuUvxkTw1_zMnDb7KTfTrXpfqL7Rn-dbvQSKw98FqjKM1BDS2BHtQgDu9GTIXPtMWL28M&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none#";
		
		String partialUrl = url.split("code=")[1];
		String code = partialUrl.split("&scope")[0];
		
		
		System.out.println(code);
		
		
		//Exchange code
		String AccessTokenResponse = given().log().all().urlEncodingEnabled(false)
				.queryParams("code", code)
		.queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type","authorization_code")
		.when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		//Extracting Access token from Exchange code
		JsonPath js = new JsonPath(AccessTokenResponse);
		String accessToken = js.getString("access_token");
		System.out.println(accessToken);
		
		
		//actual response
		GetCourse gc = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
		.when()
		.get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
		
		
		//to get any value of a key using POJO
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());
		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());	
		
		List<Api> apiCourses = gc.getCourses().getApi();
		
		for (int i = 0; i < apiCourses.size(); i++) {
			
			System.out.println(apiCourses.get(i).getCourseTitle());
			
			if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
				System.out.println(apiCourses.get(i).getPrice());
			}
			
		}
		
		
		//Get course names of WebAutomation
		ArrayList<String> a = new ArrayList<String>();
		List<WebAutomation> webAutomationCourses = gc.getCourses().getWebAutomation(); 
		
		for (int i = 0; i < webAutomationCourses.size(); i++) {
			
			System.out.println(webAutomationCourses.get(i).getCourseTitle());
			a.add(webAutomationCourses.get(i).getCourseTitle());
			
		}
		
		List<String> expectedList = Arrays.asList(courseTitles);
		
		Assert.assertTrue(a.equals(expectedList));
		
		//System.out.println(response);

	}

}
