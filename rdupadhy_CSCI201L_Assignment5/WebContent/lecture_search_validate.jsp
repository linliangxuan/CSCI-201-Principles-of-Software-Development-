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
	Syllabus syllabus = course.getSyllabus();
	Schedule schedule = course.getSchedule();
	Week[] weeks = schedule.getWeeks();
	
	String result = "";	
	ArrayList<String> results = new ArrayList<String>();
	
	for(int i = 0; i < weeks.length; i++) {
		Week week = weeks[i];
		Lecture[] lectures = week.getLectures();
		for(int j = 0; j < lectures.length; j++) {
			Lecture lecture = lectures[j];
			Topic[] topics = lecture.getTopics();
			for(int k = 0; k < topics.length; k++) {
				Topic topic = topics[k];
				String title = topic.getTitle();
				String titleLowerCase = title.toLowerCase();
				if(titleLowerCase.contains(parameterLowerCase)) {
					results.add(title);
				}
			}
		}
	}
	
	for(int i = 0; i < results.size(); i++) {
		System.out.println("Results: " + results.get(i));
		if(i != results.size() - 1) {
			result += results.get(i) + ", ";
		}
		else {
			result += results.get(i);
		}
	}
	
	
	System.out.println(result);

	if(!parameterLowerCase.equals("")) {
	
%>

<%= result %>

<%
	}
%>
	

