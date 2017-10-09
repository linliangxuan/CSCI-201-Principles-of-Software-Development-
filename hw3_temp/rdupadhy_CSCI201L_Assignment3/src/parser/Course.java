package parser;

public class Course {
	
	private String number;
	private String term;
	private String year;
	private StaffMember[] staffMembers;
	private Meeting[] meetings;
	private String title;
	private String units;
	private Syllabus syllabus;
	private Schedule schedule;
	private Assignment[] assignments;
	private Exam[] exams;
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getTerm() {
		return term;
	}
	
	public void setTerm(String term) {
		this.term = term;
	}
	
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public StaffMember[] getStaffMembers() {
		return staffMembers;
	}
	
	public void setStaffMembers(StaffMember[] staffMembers) {
		this.staffMembers = staffMembers;
	}
	
	public Meeting[] getMeetings() {
		return meetings;
	}
	
	public void setMeetings(Meeting[] meetings) {
		this.meetings = meetings;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public Syllabus getSyllabus() {
		return syllabus;
	}

	public void setSyllabus(Syllabus syllabus) {
		this.syllabus = syllabus;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public Assignment[] getAssignments() {
		return assignments;
	}

	public void setAssignments(Assignment[] assignments) {
		this.assignments = assignments;
	}

	public Exam[] getExams() {
		return exams;
	}

	public void setExams(Exam[] exams) {
		this.exams = exams;
	}
	
}
