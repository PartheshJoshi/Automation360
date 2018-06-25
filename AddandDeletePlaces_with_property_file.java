import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import Files.resources;
import Files.Payload;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AddandDeletePlaces_with_property_file {
	
	Properties prop = new Properties();
	
	@BeforeTest
	
	public void GetDatafromproperties() throws IOException
	{
		//FileInputStream fi = new FileInputStream("D:\\Executables\\FundTech_TAF_Soap_Ui\\RestAPIAutomation_simple\\src\\Files\\environment.properties");
		FileInputStream fi = new FileInputStream("D:\\Executables\\FundTech_TAF_Soap_Ui\\RestAPIAutomation_simple\\src\\Files\\environment.properties");
	    prop.load(fi);	
	}
	
	@Test
	
	public void AddData()
	{
		
        RestAssured.baseURI=prop.getProperty("HOST");

			
		//Task - 1 : Add new Place Id
		
		Response res= given().
				
		                      queryParam("key",prop.getProperty("KEY")).
		                      body(Payload.getPostData()).
		              when().
		              
		                       post(resources.placepostdata()).
		              then().
		              
		                       assertThat().statusCode(200).and().contentType(ContentType.JSON).
		                       extract().response();
		
		String response =  res.asString();
		
		System.out.println("Response is :" + response);
		
		//Task - 2 : Grab the Place Id from Response
		
		JsonPath js = new JsonPath(response);
		
		String placeId = js.get("place_id");
		
		System.out.println(placeId);
		
		//Task - 3 : Delete Place Id
		
		String deletebodyrequest = "{"+
				"\"place_id\" : \""+placeId+"\""+
				"}";
		
		Response deleteres = given().
				
                                     queryParam("key",prop.getProperty("KEY")).
                                     body(deletebodyrequest).
                              when().
                              
                                     post("/maps/api/place/delete/json").
                              then().
       
                                     assertThat().statusCode(200).and().contentType(ContentType.JSON).
                                     extract().response();
		
		String deleteresponse = deleteres.asString();
		
		System.out.println(deleteresponse);
		
		JsonPath js1 = new JsonPath(deleteresponse);
		
		String status = js1.get("status");
		
		System.out.println(status);
	}

}
