package com.JiraAPITest;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

public class JiraAPITest {

	
	public static void main(String[] args) {
		
		RestAssured.baseURI = "http://localhost:8090";
		
		//Login and Create SEssipon
		SessionFilter session = new SessionFilter();
		String response = given().relaxedHTTPSValidation().header("Content-Type","application/json")
				.header("User-Agent","PostmanRuntime/7.26.2")
				.header("Accept","*/*")
				.header("Accept-Encoding","gzip, deflate, br")
				.header("Connection","keep-alive").log().all().body("{ \"username\": \"Ggaurav92\", \"password\": \"garje@Jira$\" }")
		.filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();
		
		
		//Add comment
		String ExpectedMessage = "Hi REST API. I am Gaurav. How are you?";
		
		String addCommentResponse  = given().pathParam("key", "10001").log().all().header("Content-Type","application/json")
		.header("User-Agent","PostmanRuntime/7.26.2")
		.header("Accept","*/*")
		.header("Accept-Encoding","gzip, deflate, br")
		.header("Connection","keep-alive")
		.body("{\r\n" + 
				"    \"body\": \""+ExpectedMessage+"\",\r\n" + 
				"    \"visibility\": {\r\n" + 
				"        \"type\": \"role\",\r\n" + 
				"        \"value\": \"Administrators\"\r\n" + 
				"    }\r\n" + 
				"}").filter(session).when().post("/rest/api/2/issue/{key}/comment").then().log().all()
		.assertThat().statusCode(201).extract().response().asString();
		JsonPath js = new JsonPath(addCommentResponse);
		String Commentid = js.getString("id");
		
		
		
		//Adding attachments via REST API
		given().header("X-Atlassian-Token","no-check").filter(session).pathParam("key", "10001")
		.header("Content-Type","multipart/form-data")
		.multiPart("file",new File("jiraAttach.txt"))
		.when().post("/rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);
		
		
		//Get Issue
		String issueDetails = given().filter(session).pathParam("key","10001")
				.queryParam("fields", "comment").log().all()
		.when().get("/rest/api/2/issue/{key}").then().log().all()
		.extract().response().asString();
		
		System.out.println(issueDetails);
		
		JsonPath js1 = new JsonPath(issueDetails);
		
		int commentsCount = js1.getInt("fields.comment.comments.size()");
		
		for (int i = 0; i < commentsCount; i++) {
			
			String commentIdIssue = js1.get("fields.comment.comments["+i+"].id").toString();
			if(commentIdIssue.equals(Commentid)) {
				String Issuemessage = js1.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(Issuemessage);
				
				Assert.assertEquals(Issuemessage, ExpectedMessage);
			}
		}//End of for loop
		
		
		
	}//End of psvm
}//End of Class
