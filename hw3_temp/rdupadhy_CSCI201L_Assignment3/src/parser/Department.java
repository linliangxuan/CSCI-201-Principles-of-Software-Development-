package parser;

public class Department {
	
	private String longName;
	private String prefix;
	private Course[] courses;
	
	public String getLongName() {
		return longName;
	}
	
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public Course[] getCourses() {
		return courses;
	}
	
	public void setCourses(Course[] courses) {
		this.courses = courses;
	}
	
}
