package parser;

public class School {
	
	private String name;
	private Department[] departments;
	private String image;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Department[] getDepartments() {
		return departments;
	}

	public void setDepartments(Department[] departments) {
		this.departments = departments;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
}

