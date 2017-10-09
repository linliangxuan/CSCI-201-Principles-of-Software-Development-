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
	
	
	School[] schools = education.getSchools();
	School school = schools[0];
	Department[] departments = schools[0].getDepartments();
	Department department = departments[0];
	Course courses[] = departments[0].getCourses();
	Course course = courses[0];
	
	Syllabus syllabus = course.getSyllabus();
	Assignment[] assignments = course.getAssignments();
		
	%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><%= school.getName() %>: <%= department.getPrefix() + " " + course.getNumber() + " " + course.getTerm() + " " + course.getYear() %></title>
		<!-- <link rel="stylesheet" type="text/css" href="assignments1.css" /> -->
		<%
		if(design.equals("design1")) {
		%>
			<link rel="stylesheet" type="text/css" href="assignments1.css" />
		<%	
		}
		else {
		%>
			<link rel="stylesheet" type="text/css" href="assignments2.css" /> 
		<%
		}
		%>
		<script>
			function finalProjectFormChange() {
				
				var sort = document.getElementById("finalProjectForm").sort.value;
				var order = document.getElementById("finalProjectForm").order.value;
				
				console.log("Sort By: " + sort);
				console.log("Order: " + order);
				var query = "?sort=" + sort + "&order=" + order;
				console.log("Query: " + query);
				
				var xhttp = new XMLHttpRequest();
				xhttp.open("GET", "finalProjectForm_validate.jsp" + query, false);
				xhttp.send();
				//console.log("Response", xhttp.responseText);
				if(xhttp.responseText.trim().length > 0) {
					document.getElementById("assignments").innerHTML = xhttp.responseText.trim();
				}
				
			}
			
			function assignmentsFormChange() {
				
				var sort = document.getElementById("assignmentsForm").sort.value;
				var order = document.getElementById("assignmentsForm").order.value;
				
				console.log("Sort By: " + sort);
				console.log("Order: " + order);
				var query = "?sort=" + sort + "&order=" + order;
				console.log("Query: " + query);
				
				var xhttp = new XMLHttpRequest();
				xhttp.open("GET", "assignmentsForm_validate.jsp" + query, false);
				xhttp.send();
				//console.log("Response", xhttp.responseText);
				if(xhttp.responseText.trim().length > 0) {
					document.getElementById("assignments").innerHTML = xhttp.responseText;
				}
				
			}
		</script>
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
          			<font size="+1">Assignments</font><br /><br />
          			<font size="+1"><a href="exams.jsp">Previous Exams</a></font><br /><br />
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
        			<p>
        				<form name="finalProjectForm" id="finalProjectForm" method="GET" onchange="return finalProjectFormChange()" >
        					Final Project Deliverables -- Sort By:
          					 <input type="radio" name="sort" value="date" checked /> Due Date 
          					 <input type="radio" name="sort" value="title" /> Title 
          					 <input type="radio" name="sort" value="grade" /> Grade Percentage
          					  <br />
          					 <input type="radio" name="order" value="ascending" checked /> Ascending Order
          					 <input type="radio" name="order" value="descending" /> Descending Order
          				</form>
        			<p><hr size="4" />
        			<p>
        				<form name="assignmentsForm" id="assignmentsForm"  method="GET" onchange="return assignmentsFormChange()">
        					Assignments -- Sort By:
          					 <input type="radio" name="sort" value="dueDate" checked /> Due Date 
          					 <input type="radio" name="sort" value="assignedDate" /> Assigned Date 
          					 <input type="radio" name="sort" value="grade" /> Grade Percentage
          					  <br />
          					 <input type="radio" name="order" value="ascending" checked /> Ascending Order
          					 <input type="radio" name="order" value="descending" /> Descending Order
          				</form>
        			<p><hr size="4" />
        			<b class="title">Assignments</b>
          			<table border="2" cellpadding="5" width="590" id="assignments">
          				<tr>
              				<th align="center">Homework #</th><th align="center">Assigned</th><th align="center" width="40">Due</th><th>Assignment</th><th>% Grade</th><th>Grading Criteria</th><th>Solution</th>
            			</tr>
          				<%
          				for(int i = 0; i < assignments.length; i++) {
          					Assignment assignment = assignments[i];
          					if(assignment.getNumber().equals("Final Project")) {
          						continue;
          					}
          					File[] files = assignment.getFiles();
          					File[] gradingCriteriaFiles = assignment.getGradingCriteriaFiles();
          					File[] solutionFiles = assignment.getSolutionFiles();
          				%>
          					<tr>
              					<td align="center"><%= assignment.getNumber() %></td>
              					<td align="center"><%= assignment.getAssignedDate() %></td>
              					<td align="center"><%= assignment.getDueDate() %></td>
              					<td align="center">
              						<%
              						if(assignment.getTitle() != null) {
              						%>
                					<a href="<%= assignment.getUrl() %>"><%= assignment.getTitle() %></a><hr />	
                					<%
              						}
                					if(files != null) {
                						for(int j = 0; j < files.length; j++) {
                							File file = files[j];
                					%>
                						<a href="<%= file.getUrl() %>"><%= file.getTitle() %></a>
                					<%
                						}
                					}	
                					%>
              					</td>
              					<td align="center"><%= assignment.getGradePercentage() %></td>
              					<td align="center">
              					<%
              						if(gradingCriteriaFiles != null) {	
              							for(int j = 0; j < gradingCriteriaFiles.length; j++) {
              								File gradingCriteriaFile = gradingCriteriaFiles[j];
              					%>
              								<a href="<%= gradingCriteriaFile.getUrl() %>"><%= gradingCriteriaFile.getTitle() %></a><br />
              					<%		
              							}
              						}	
              					%>
              					</td>
              					<td align="center">
              					<%
              						if(solutionFiles != null) {	
              							for(int j = 0; j < solutionFiles.length; j++) {
              								File solutionFile = solutionFiles[j];
              					%>
              								<a href="<%= solutionFile.getUrl() %>"><%= solutionFile.getTitle() %></a>
              					<%		
              							}
              						}	
              					%>	
              					</td>
            				</tr>
          				<%	
          				}
          				%>
          				<%
          				int indexFinalProject = 0;
          				for(int i = 0; i < assignments.length; i++) {
          					if(assignments[i].getNumber().equals("Final Project")) {
          						indexFinalProject = i;
          						break;
          					}
          				}
          				Assignment finalProject = assignments[indexFinalProject];
          				File[] files = finalProject.getFiles();
          				Deliverable[] deliverables = finalProject.getDeliverables(); 
          				%>
          				<tr>
							<td align="center"><%= finalProject.getNumber() %></td>
              				<td align="center"><%= finalProject.getAssignedDate() %></td>
              				<td align="center" colspan="3">
                				<table border="1" cellpadding="5">
                  					<tr>
                    					<td colspan="3" align="center">
                      						<a href="<%= finalProject.getUrl() %>"><%= finalProject.getTitle() %></a><hr />
                      						<%
                							if(files != null) {
                								for(int i = 0; i < files.length; i++) {
                									File file = files[i];
                							%>
                									<a href="<%= file.getUrl() %>"><%= file.getTitle() %></a>
                							<%
                								}
                							}	
                							%>
                    					</td>
                  					</tr>
                  					<%
                  					if(deliverables != null) {
                  						for(int i = 0; i < deliverables.length; i++) {
                  							Deliverable deliverable = deliverables[i];
                  							File[] deliverableFiles = deliverable.getFiles();
                  					%>
                  					<tr>
                    					<td><%= deliverable.getDueDate() %></td>
                    					<td><%= deliverable.getTitle() %>
                    				<%
                    						if(deliverableFiles == null) {
                    				%>
                    					</td>	
                    				<%
                    						}
                    						else {
                    				%>
                    					<br />	
                    				<%			
                    							for(int j = 0; j < deliverableFiles.length; j++) {
                    								File deliverableFile = deliverableFiles[j];
                    								if(j != deliverableFiles.length - 1) {
                    				%>
                    						<a href="<%= deliverableFile.getUrl() %>"><%= deliverableFile.getTitle() %></a><hr />
                    				<%
                    								}
                    								else {
                    				%>
                    						<a href="<%= deliverableFile.getUrl() %>"><%= deliverableFile.getTitle() %></a>
                    				<%			
                    								}
                    							}
                    				%>
                    					</td>
                    				<%	
                  							}
                    				%>
                    					<td><%= deliverable.getGradePercentage() %></td>
                  					</tr>
                  					<%
                  						}
                  					}
                  					%>						
                  					<!-- <tr>
                   	 					<td>11-21<br />11-28<br />11-30</td>
                    					<td>
                      						Demonstration<br />
                      						<a href="assignments/FinalProjectPresentationGradingRubric.pdf">Grading Rubric</a><hr />
                      						<a href="assignments/FinalProjectPresentationEvaluationForm-Professor.pdf">Professor Evaluation Form</a><hr />
                      						<a href="assignments/FinalProjectPresentationEvaluationForm-Student.pdf">Student Evaluation Form</a>
                    					</td>
                    					<td>10.0%</td>
                  					</tr> -->
                				</table>
              				</td>        					
          				</tr>
        			</table>
				</td>
			</tr>			
		</table>
	</body>
</html>