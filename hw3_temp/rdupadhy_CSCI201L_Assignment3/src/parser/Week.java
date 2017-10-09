package parser;

public class Week {
	private String week;
	private Lab[] labs;
	private Lecture[] lectures;
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public Lab[] getLabs() {
		return labs;
	}
	public void setLabs(Lab[] labs) {
		this.labs = labs;
	}
	public Lecture[] getLectures() {
		return lectures;
	}
	public void setLectures(Lecture[] lectures) {
		this.lectures = lectures;
	}
 }
