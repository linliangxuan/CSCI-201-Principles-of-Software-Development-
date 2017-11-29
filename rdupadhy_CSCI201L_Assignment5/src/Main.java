import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import parser.Education;


public class Main {

	public static void main(String []args) {	
//		try {
//			String fileName = "Assignment5.json";
//			InputStream fileContent = new FileInputStream(fileName);
//			Gson gson = new Gson();
//			JsonReader jsonReader = new JsonReader(new InputStreamReader(fileContent));
//	    	Education education = gson.fromJson(jsonReader, Education.class);
//	    	System.out.println(education.getSchools()[0].getName());
//	    	for(int i = 0; i < education.getSchools().length; i++) {
//	    		InsertSQL.insertSchool(education.getSchools()[i]);
//	    	}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		
		Gson gson = new Gson();
		try {
			String fileName = "Assignment5.json";
			InputStream fileContent = new FileInputStream(fileName);;
			JsonReader jsonReader = new JsonReader(new InputStreamReader(fileContent));
	    	Education education1 = gson.fromJson(jsonReader, Education.class);
	    	System.out.println(gson.toJson(education1));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		System.out.println("Start");
		Education education2 = RetrieveSQL.retrieveEducation();
		System.out.println(gson.toJson(education2));
		//System.out.println(gson.toJson(education.getSchools()[0].getDepartments()[0].getCourses()[0]));
		System.out.println("End");
		
	}
	
}
