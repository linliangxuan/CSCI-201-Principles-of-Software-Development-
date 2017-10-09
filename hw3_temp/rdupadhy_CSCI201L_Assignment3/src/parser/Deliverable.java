package parser;

public class Deliverable {
	private String number;
	private String dueDate;
	private String title;
	private String gradePercentage;
	private File[] files;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGradePercentage() {
		return gradePercentage;
	}
	public void setGradePercentage(String gradePercentage) {
		this.gradePercentage = gradePercentage;
	}
	public File[] getFiles() {
		return files;
	}
	public void setFiles(File[] files) {
		this.files = files;
	}
}
