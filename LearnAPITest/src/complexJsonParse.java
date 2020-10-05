import org.testng.Assert;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class complexJsonParse {
	
	
	public static void main (String args[]){
	JsonPath js = new JsonPath(Payload.CoursePrice());
	
	//Print No of courses returned by API
	
	int count = js.getInt("courses.size()");
	System.out.println(count);
	
	
	//Print Purchase Amount
	int totalAmount  = js.getInt("dashboard.purchaseAmount");
	
	System.out.println(totalAmount);
	
	//Print Title of the first course
	String firstTitle = js.getString("courses[0].title");
	
	System.out.println(firstTitle);
	
	
	//Print All course titles and their respective Prices
	
	for (int i = 0; i < count; i++) {
		
		String courseTitles = js.getString("courses[" + i + "].title");
		
		
		System.out.println(courseTitles);
		System.out.println(js.get("courses["+ i +"].price").toString());
		
		
		
	}//End of for loop
	
	
	
	//Print no of copies sold by RPA Course
	System.out.println("Print no of copies sold by RPA Course");
for (int i = 0; i < count; i++) {
		
		String courseTitles = js.getString("courses[" + i + "].title");
		
		if(courseTitles.equalsIgnoreCase("RPA")) {
			int copies = js.get("courses[" + i + "].copies");
			System.out.println(copies);
			break;
		}
		
		
		
	}//End of for loop
	
	
	
//Verify if Sum of all Course prices matches with Purchase Amount
System.out.println("Verify if Sum of all Course prices matches with Purchase Amount");

int actualTotal = 0;

for (int i = 0; i < count; i++) {
	
	int price = js.get("courses[" + i + "].price");
	int copies = js.get("courses["+ i + "].copies");
	
	actualTotal = actualTotal  + (price*copies);
	
	
	
}//End of for loop


System.out.println(actualTotal);
System.out.println(totalAmount);
Assert.assertEquals(actualTotal, totalAmount);

	
	}//End of psvm
	
}//End of Class
