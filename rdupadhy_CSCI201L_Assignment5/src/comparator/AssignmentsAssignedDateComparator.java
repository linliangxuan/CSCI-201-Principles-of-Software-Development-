package comparator;

import java.util.*;
import parser.*;

public class AssignmentsAssignedDateComparator implements Comparator<Assignment> {

	@Override
	public int compare(Assignment o1, Assignment o2) {
		
		String date1 = o1.getAssignedDate();
		String[] parts1 = date1.split("-");
		int month1 = Integer.parseInt(parts1[0]);
		int day1 = Integer.parseInt(parts1[1]);
		int year1 = Integer.parseInt(parts1[2]);
		
		String date2 = o2.getAssignedDate();
		String[] parts2 = date2.split("-");
		int month2 = Integer.parseInt(parts2[0]);
		int day2 = Integer.parseInt(parts2[1]);
		int year2 = Integer.parseInt(parts2[2]);
		
		if(year1 != year2) {
			return year1 - year2;
		}
		else if(month1 != month2) {
			return month1 - month2;
		}
		else {
			return day1 - day2;
		}
		

			
	}

	
	
}
