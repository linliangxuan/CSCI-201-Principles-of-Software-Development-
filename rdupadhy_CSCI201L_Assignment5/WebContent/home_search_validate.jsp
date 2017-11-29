<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="parser.*" %>    
<%@ page import="servlet.*" %>    
<%@ page import="java.util.ArrayList" %>       
<%
	
	System.out.println("Hit!");

	String parameter = request.getParameter("parameter");
	String parameterLowerCase = parameter.toLowerCase();

	Education education = (Education)session.getAttribute("education");
	School[] schools = education.getSchools();
	School school = schools[0];
	Department[] departments = schools[0].getDepartments();
	Department department = departments[0];
	Course courses[] = departments[0].getCourses();
	Course course = courses[0];
	StaffMember[] staffMembers = course.getStaffMembers();
	
	String result = "";
	ArrayList<String> results = new ArrayList<String>();
	
	for(int i = 0; i < staffMembers.length; i++) {
		
		StaffMember staffMember = staffMembers[i];
		
		String fname = staffMember.getName().getFname();
		String lname = staffMember.getName().getLname();
		String name = fname + " " + lname; 
		
		String fnameLowerCase = staffMember.getName().getFname().toLowerCase();
		String lnameLowerCase = staffMember.getName().getLname().toLowerCase();
		String nameLowerCase = fnameLowerCase + " " + lnameLowerCase;
		
		if(parameterLowerCase.equals(fnameLowerCase) || parameterLowerCase.equals(lnameLowerCase) || parameterLowerCase.equals(nameLowerCase)) {
			//result = name;
			results.add(name);
		}
		
	}
	
	for(int i = 0; i < results.size(); i++) {
		if(i != results.size() - 1) {
			result += results.get(i) + ", ";
		}
		else {
			result += results.get(i);
		}
	}
	
	System.out.println("Result: " + result);

%>

<%= result %>


