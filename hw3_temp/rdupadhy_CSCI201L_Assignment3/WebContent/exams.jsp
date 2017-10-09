<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="parser.*" %>    
<%@ page import="servlet.*" %>     
<%@ page import="java.util.ArrayList" %>      
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<%
	Education education = (Education)session.getAttribute("education");
	String design = (String)session.getAttribute("design");
	
	if(education == null) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	/* Course course = (Course)session.getAttribute("course"); */
	
	School[] schools = education.getSchools();
	School school = schools[0];
	Department[] departments = schools[0].getDepartments();
	Department department = departments[0];
	Course courses[] = departments[0].getCourses();
	Course course = courses[0];
	
	
	Syllabus syllabus = course.getSyllabus();
	Exam[] exams = course.getExams();
	%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><%= school.getName() %>: <%= department.getPrefix() + " " + course.getNumber() + " " + course.getTerm() + " " + course.getYear() %></title>
		<!-- <link rel="stylesheet" type="text/css" href="exams1.css" /> -->
		<%
		if(design.equals("design1")) {
		%>
			<link rel="stylesheet" type="text/css" href="exams1.css" />
		<%	
		}
		else {
		%>
			<link rel="stylesheet" type="text/css" href="exams2.css" /> 
		<%
		}
		%>
	</head>
	<body text="#333333" bgcolor="#EEEEEE" link="#0000EE" vlink="#551A8B" alink="#336633">
		<table cellpadding="10" width="800" id="main">
			<tr>
        		<!-- column for left side of page -->
				<td width="180" valign="top" align="right" id="sidebar" class="main_td">
					<a href="http://www.usc.edu"><img src="http://www-scf.usc.edu/~csci201/images/USC_seal.gif" border="0"/></a><br /><br />
          			<font size="+1"><a href="http://cs.usc.edu"><%= department.getPrefix() %> Department</a></font><br /><br />
         		 	<font size="+1"><a href="home.jsp"><%= department.getPrefix() + " " + course.getNumber() %> Home</a></font><br /><br />
          			<font size="+1"><a href="<%= syllabus.getUrl() %>">Syllabus</a></font><br /><br />
         			<font size="+1"><a href="lectures.jsp">Lectures</a></font><br /><br />
          			<font size="+1"><a href="assignments.jsp">Assignments</a></font><br /><br />
          			<font size="+1">Previous Exams</font><br /><br />
        		</td>
        		<!-- center column to separate other two columns -->
        		<td width="5" class="main_td" id="seperation"><br /></td>
        		<!-- large column in center of page with dominant content -->
        		<td align="baseline" width="615" class="main_td" id="dominant">
        			<br />
          			<p id="header">
            			<b><font size="+3"><%= department.getPrefix() + " " + course.getNumber() %></font></b><br />
            			<b><i><font size="+1"><%= course.getTitle() %> - <%= course.getUnits() %> units</font></i></b><br />
            			<b><i><font size="+1"><%= course.getTerm() + " " + course.getYear() %></font></i></b><br />
          			<p><hr size="4" />
          			
          			<table border="2" cellpadding="5" width="590" id="exams">
            			<tr>
              				<th align="center">Semester</th><th align="center">Written Exam #1</th><th align="center">Programming Exam #1</th><th>Written Exam #2</th><th>Programming Exam #2</th>
            			</tr>
            			<%
            			for(int i = 0; i < exams.length; i++) {
            				Exam exam = exams[i];
            				Test[] tests = exam.getTests();
            				Test writtenExam1 = null;
            				Test writtenExam2 = null;
            				Test programmingExam1 = null;
            				Test programmingExam2 = null;
            				for(int j = 0; j < tests.length; j++) {
            					Test test = tests[j];
            					if(test.getTitle() != null) {
	            					if(test.getTitle().equals("Written Exam #1")) {
	            						writtenExam1 = test;
	            					}
	            					else if(test.getTitle().equals("Written Exam #2")) {
	            						writtenExam2 = test;
	            					}
	            					else if(test.getTitle().equals("Programming Exam #1")) {
	            						programmingExam1 = test;
	            					}
	            					else if(test.getTitle().equals("Programming Exam #2")) {
	            						programmingExam2 = test;
	            					}
            					}
            				}
            			%>
            			<tr>
            				<td align="center"><%= exam.getSemester() + " " + exam.getYear() %></td>
            				<%
            				if(writtenExam1 != null) {
            				%>
            				<td align="center">
            				<%
            					File[] files = writtenExam1.getFiles();
            					if(files != null) {
            					for(int j = 0; j < files.length; j++) {
            						File file = files[j];
            						if(files.length == 1) {
            				%>
            				<a href="<%= file.getUrl() %>"><%= file.getTitle() %></a>
            				<%			
            						}
            						else {
            							if(j != files.length - 1) {
            				%>
            				<a href="<%= file.getUrl() %>"><%= file.getTitle() %></a><hr />
            				<%				
            							}
            							else {
            				%>
            				<a href="<%= file.getUrl() %>"><%= file.getTitle() %></a>
            				<%				
            							}
            						}
            					}
            					}	
            				%>
            				</td>
            				<%
            				}
            				else {
            				%>
            				<td align="center"></td>
            				<%
            				}
            				if(programmingExam1 != null) {
                				%>
                				<td align="center">
                				<%
                					File[] files = programmingExam1.getFiles();
                					if(files != null) {
                					for(int j = 0; j < files.length; j++) {
                						File file = files[j];
                						if(files.length == 1) {
                				%>
                				<a href="<%= file.getUrl() %>"><%= file.getTitle() %></a>
                				<%			
                						}
                						else {
                							if(j != files.length - 1) {
                				%>
                				<a href="<%= file.getUrl() %>"><%= file.getTitle() %></a><hr />
                				<%				
                							}
                							else {
                				%>
                				<a href="<%= file.getUrl() %>"><%= file.getTitle() %></a>
                				<%				
                							}
                						}
                					}
                					}
                				%>
                				</td>
                				<%
                			}
            				else {
                				%>
                				<td align="center"></td>
                				<%
                				}
            				if(writtenExam2 != null) {
                				%>
                				<td align="center">
                				<%
                					File[] files = writtenExam2.getFiles();
                					if(files != null) {
                					for(int j = 0; j < files.length; j++) {
                						File file = files[j];
                						if(files.length == 1) {
                				%>
                				<a href="<%= file.getUrl() %>"><%= file.getTitle() %></a>
                				<%			
                						}
                						else {
                							if(j != files.length - 1) {
                				%>
                				<a href="<%= file.getUrl() %>"><%= file.getTitle() %></a><hr />
                				<%				
                							}
                							else {
                				%>
                				<a href="<%= file.getUrl() %>"><%= file.getTitle() %></a>
                				<%				
                							}
                						}
                					}
                					}
                				%>
                				</td>
                				<%
                			}
            				else {
                				%>
                				<td align="center"></td>
                				<%
                				}
            				if(programmingExam2 != null) {
                				%>
                				<td align="center">
                				<%
                					File[] files = programmingExam2.getFiles();
                					if(files != null) {
                					for(int j = 0; j < files.length; j++) {
                						File file = files[j];
                						if(files.length == 1) {
                				%>
                				<a href="<%= file.getUrl() %>"><%= file.getTitle() %></a>
                				<%			
                						}
                						else {
                							if(j != files.length - 1) {
                				%>
                				<a href="<%= file.getUrl() %>"><%= file.getTitle() %></a><hr />
                				<%				
                							}
                							else {
                				%>
                				<a href="<%= file.getUrl() %>"><%= file.getTitle() %></a>
                				<%				
                							}
                						}
                					}
                					}	
                				%>
                				</td>
                				<%
                			}
            				else {
                				%>
                				<td align="center"></td>
                				<%
                				}
            			}	
            			%>
            			</tr>
          			</table>
				</td>
			</tr>
		</table>
	</body>
</html>