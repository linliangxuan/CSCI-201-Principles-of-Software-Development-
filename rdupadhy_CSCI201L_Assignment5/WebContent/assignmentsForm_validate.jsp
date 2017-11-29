<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="parser.*"%>
<%@ page import="servlet.*"%>
<%@ page import="comparator.*"%>
<%@ page import="java.util.*"%>
<%
	String sort = request.getParameter("sort");
	String order = request.getParameter("order");
		
	Education education = (Education)session.getAttribute("education");
	School[] schools = education.getSchools();
	School school = schools[0];
	Department[] departments = schools[0].getDepartments();
	Department department = departments[0];
	Course courses[] = departments[0].getCourses();
	Course course = courses[0];
	Assignment[] assignments = course.getAssignments();
	
	ArrayList<Assignment> assignmentsList = new ArrayList<Assignment>();
	for(int i = 0; i < assignments.length - 1; i++) {
		assignmentsList.add(assignments[i]);
	}
	
	if(sort.equals("dueDate")) {
		Collections.sort(assignmentsList, new AssignmentsDueDateComparator());		
	}
	else if(sort.equals("assignedDate")) {
		Collections.sort(assignmentsList, new AssignmentsAssignedDateComparator());	
	}
	else if(sort.equals("grade")) {
		Collections.sort(assignmentsList, new AssignmentsGradeComparator());
	}
	
	if(order.equals("descending")) {
		Collections.reverse(assignmentsList);
	}
	
%>


<tr>
	<th align="center">Homework #</th>
	<th align="center">Assigned</th>
	<th align="center" width="40">Due</th>
	<th>Assignment</th>
	<th>% Grade</th>
	<th>Grading Criteria</th>
	<th>Solution</th>
</tr>
<%
          				for(int i = 0; i < assignmentsList.size(); i++) {
          					Assignment assignment = assignmentsList.get(i);
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
              						%> <a href="<%= assignment.getUrl() %>"><%= assignment.getTitle() %></a>
	<hr /> <%
              						}
                					if(files != null) {
                						for(int j = 0; j < files.length; j++) {
                							File file = files[j];
                					%> <a href="<%= file.getUrl() %>"><%= file.getTitle() %></a>
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
              					%> <a href="<%= gradingCriteriaFile.getUrl() %>"><%= gradingCriteriaFile.getTitle() %></a><br />
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
              					%> <a href="<%= solutionFile.getUrl() %>"><%= solutionFile.getTitle() %></a>
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
				<td colspan="3" align="center"><a
					href="<%= finalProject.getUrl() %>"><%= finalProject.getTitle() %></a>
				<hr /> <%
                							if(files != null) {
                								for(int i = 0; i < files.length; i++) {
                									File file = files[i];
                							%> <a href="<%= file.getUrl() %>"><%= file.getTitle() %></a>
					<%
                								}
                							}	
                							%></td>
			</tr>
			<%
                  					if(deliverables != null) {
                  						for(int i = 0; i < deliverables.length; i++) {
                  							Deliverable deliverable = deliverables[i];
                  							File[] deliverableFiles = deliverable.getFiles();
                  					%>
			<tr>
				<td><%= deliverable.getDueDate() %></td>
				<td><%= deliverable.getTitle() %> <%
                    						if(deliverableFiles == null) {
                    				%></td>
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
				<a href="<%= deliverableFile.getUrl() %>"><%= deliverableFile.getTitle() %></a>
				<hr />
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