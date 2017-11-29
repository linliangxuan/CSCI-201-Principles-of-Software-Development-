package parser;

public class Exam {
	private String semester;
	private String year;
	private Test[] tests;
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Test[] getTests() {
		return tests;
	}
	public void setTests(Test[] tests) {
		this.tests = tests;
	}
	
}
