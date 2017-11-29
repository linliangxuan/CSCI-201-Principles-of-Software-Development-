package parser;

public class Topic {
	private String number;
	private String title;
	private String url;
	private String chapter;
	private Program[] programs;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
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
	public String getChapter() {
		return chapter;
	}
	public void setChapter(String chapter) {
		this.chapter = chapter;
	}
	public Program[] getPrograms() {
		return programs;
	}
	public void setPrograms(Program[] programs) {
		this.programs = programs;
	}
}
