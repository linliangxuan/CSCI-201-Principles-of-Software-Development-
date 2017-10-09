package parser;

import parser.Deliverable;
import parser.File;

public class Assignment {
	private String number;
	private String assignedDate;
	private String dueDate;
	private String title;
	private String url;
	private File[] files;
	private File[] gradingCriteriaFiles;
	private File[] solutionFiles;
	private String gradePercentage;
	private Deliverable[] deliverables;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getAssignedDate() {
		return assignedDate;
	}
	public void setAssignedDate(String assignedDate) {
		this.assignedDate = assignedDate;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public File[] getFiles() {
		return files;
	}
	public void setFiles(File[] files) {
		this.files = files;
	}
	public String getGradePercentage() {
		return gradePercentage;
	}
	public void setGradePercentage(String gradePercentage) {
		this.gradePercentage = gradePercentage;
	}
	public Deliverable[] getDeliverables() {
		return deliverables;
	}
	public void setDeliverables(Deliverable[] deliverables) {
		this.deliverables = deliverables;
	}
	public File[] getGradingCriteriaFiles() {
		return gradingCriteriaFiles;
	}
	public void setGradingCriteriaFiles(File[] gradingCriteriaFiles) {
		this.gradingCriteriaFiles = gradingCriteriaFiles;
	}
	public File[] getSolutionFiles() {
		return solutionFiles;
	}
	public void setSolutionFiles(File[] solutionFiles) {
		this.solutionFiles = solutionFiles;
	}
}
