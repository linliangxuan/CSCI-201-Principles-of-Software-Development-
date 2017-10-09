package comparator;

import java.util.*;
import parser.*;

public class AssignmentsGradeComparator implements Comparator<Assignment> {

	@Override
	public int compare(Assignment o1, Assignment o2) {
		 
		int gradePercentage1Length = o1.getGradePercentage().length();
		String gradePercentage1 = o1.getGradePercentage().substring(0, gradePercentage1Length - 1);
		
		int gradePercentage2Length = o2.getGradePercentage().length();
		String gradePercentage2 = o2.getGradePercentage().substring(0, gradePercentage2Length - 1);
		
		float gradePercentage1Float = Float.parseFloat(gradePercentage1);
		float gradePercentage2Float = Float.parseFloat(gradePercentage2);
		int answer = (int) (gradePercentage1Float - gradePercentage2Float);
		
		return answer;
		
		

			
	}

	
	
}
