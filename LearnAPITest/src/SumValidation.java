import org.testng.Assert;
import org.testng.annotations.Test;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class SumValidation {

	
	@Test
	public void sumofCourses() {
		
		JsonPath js = new JsonPath(Payload.CoursePrice());
		
		//Print No of courses returned by API
		
		int count = js.getInt("courses.size()");
		System.out.println(count);
		
		//Print Purchase Amount
		int totalAmount  = js.getInt("dashboard.purchaseAmount");
		
		System.out.println(totalAmount);
		
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
		
	}//End of SumValidation
	
}//End of Class
