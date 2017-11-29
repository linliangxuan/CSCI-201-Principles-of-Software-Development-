package comparator;

import java.util.Comparator;

import parser.Deliverable;

public class FinalProjectDueDateComparator implements Comparator<Deliverable> {

	@Override
	public int compare(Deliverable o1, Deliverable o2) {
		
		String date1 = o1.getDueDate();
		String[] parts1 = date1.split("-");
		int month1 = Integer.parseInt(parts1[0]);
		int day1 = Integer.parseInt(parts1[1]);
		int year1 = Integer.parseInt(parts1[2]);
		
		String date2 = o2.getDueDate();
		String[] parts2 = date2.split("-");
		int month2 = Integer.parseInt(parts2[0]);
		int day2 = Integer.parseInt(parts2[1]);
		int year2 = Integer.parseInt(parts2[2]);
		
		System.out.println(date1 + " " + date2);

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
