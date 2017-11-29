package comparator;

import java.util.Comparator;

import parser.Deliverable;

public class FinalProjectTitleComparator implements Comparator<Deliverable> {

	@Override
	public int compare(Deliverable o1, Deliverable o2) {
			
		String title1 = o1.getTitle();
		String title2 = o2.getTitle();
		
		return title1.compareTo(title2);
		
	}

}
