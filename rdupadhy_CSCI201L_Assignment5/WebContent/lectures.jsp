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
	Schedule schedule = course.getSchedule();
	Textbook[] textbooks = schedule.getTextbooks();
	Textbook textbook = textbooks[0];
	Week[] weeks = schedule.getWeeks();
	Assignment[] assignments = course.getAssignments();
	
	%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><%= school.getName() %>: <%= department.getPrefix() + " " + course.getNumber() + " " + course.getTerm() + " " + course.getYear() %></title>
		<!-- <link rel="stylesheet" type="text/css" href="lectures1.css" /> -->
		<%
		if(design.equals("design1")) {
		%>
			<link rel="stylesheet" type="text/css" href="lectures1.css" />
		<%	
		}
		else {
		%>
			<link rel="stylesheet" type="text/css" href="lectures2.css" /> 
		<%
		}
		%>
		<script>
			function validate() {
				
				if(document.searchForm.parameter.value == "") {
					resetHandler();
					return false;
				}
				
				var xhttp = new XMLHttpRequest();
				xhttp.open("GET", "lecture_search_validate.jsp?parameter=" + document.searchForm.parameter.value, false);
				xhttp.send();
			
				console.log("Result: " + xhttp.responseText.trim());
				var result = xhttp.responseText.trim();
				
				if(!result) {
					resetHandler();
					return false;
				}
				
				var resultArray = result.split(',');
				for(var i = 0; i < resultArray.length; i++) {
					resultArray[i] = resultArray[i].trim();
				}
				
				var lectureRows = document.getElementById("lectures").rows;
				//console.log(lectureRows);
				for(var i = 0; i < resultArray.length; i++) {
					
					for(var j = 0; j < lectureRows.length; j++) {
						var lectureRow = lectureRows[j];
						for(var k = 0; k < lectureRow.children.length; k++) {
							var child = lectureRow.children[k];
							if(k != 1) {
								if(child.children != null) {
									var childRows = child.children;
									for(var l = 0; l < childRows.length; l++) {
										if(childRows[l].innerHTML) {
											if(childRows[l].innerHTML == resultArray[i]) {
												childRows[l].style.backgroundColor = "aqua";
											}
										}
									}
								}
							}							
						}
					}
					
				}
				
				return false;
				
			}
			
			function resetHandler() {
					
				console.log("Reset clicked!");
				
				var design = "${design}";
				var color = "";
				
				console.log(design);
				
				if(design == "design1") {
					color = "#FFFFFF";		
				} 
				else {
					color = "#EEEEEE";
				}
				
				var lectureRows = document.getElementById("lectures").rows;
				
				for(var i = 0; i < lectureRows.length; i++) {
					var lectureRow = lectureRows[i];
					for(var j = 0; j < lectureRow.children.length; j++) {
						var child = lectureRow.children[j];
						if(child.children != null) {
							var childRows = child.children;
							for(var k = 0; k < childRows.length; k++) {
								if(childRows[k].innerHTML) {
									childRows[k].style.backgroundColor = color;
								}
							}
						}
						
					}
				}
				
				
			}
			
			function filterFormChange() {
				var filterForm = document.getElementById("filterForm");
				var sort = filterForm.sort.value;
				
				var xhttp = new XMLHttpRequest();
				xhttp.open("GET", "filterForm_validate.jsp?sort=" + sort, false);
				xhttp.send();
				
				if(xhttp.responseText.trim().length > 0) {
					document.getElementById("lectures").innerHTML = xhttp.responseText;
				}
				
				if(sort == "dueDates") {
					document.getElementById("parameter").disabled = true;
					document.getElementById("submit").disabled = true;
					document.getElementById("reset").disabled = true;
				}
				else {
					document.getElementById("parameter").disabled = false;
					document.getElementById("submit").disabled = false;
					document.getElementById("reset").disabled = false;
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
          			<font size="+1">Lectures</font><br /><br />
          			<font size="+1"><a href="assignments.jsp">Assignments</a></font><br /><br />
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
            		<b id="textbook">Chapter references are from <%= textbook.getAuthor() %> <u><%= textbook.getTitle() %></u>, <%= textbook.getPublisher() %>, <%= textbook.getYear() %>. ISBN <%= textbook.getIsbn() %></b>
          		<p><hr size="4" />
          		<p>
        			<form name="searchForm" method="GET" onsubmit="return validate();">
          				<input type="text" name="parameter" id="parameter" /> 
          				<input type="submit" name="submit" id="submit" value="Search Topics" /> 
          				<input type="reset" name="reset" onclick="resetHandler();" id="reset" value="Clear Search" /> 
          			</form>
        		<p><hr size="4" />
        			<form name="filterForm" id="filterForm" method="GET" onchange="return filterFormChange()" >
          				<input type="radio" name="sort" value="lectures" /> Show Lectures
          				<input type="radio" name="sort" value="dueDates" /> Show Due Dates 
          				<input type="radio" name="sort" value="all" checked /> Show All
          			</form>
        		<p><hr size="4" />
        		<p>
        		<b class="title">Lectures</b>	
        		<table border="2" cellpadding="5" width="590" id="lectures">
        		<tr>
              		<th align="center">Week #</th><th align="center">Lab</th><th align="center">Lecture #</th><th align="center">Day</th><th align="center" width="40">Date</th><th align="center">Lecture Topic</th><th align="center">Chapter</th><th>Program</th>
            	</tr>
            	<%
            	int lectureTracker = 0;
            	for(int i = 0; i < weeks.length; i++) {
            		Week week = weeks[i];
            		Lab[] labs = week.getLabs();
            		Lecture[] lectures = week.getLectures();
            		
            		int numAssignmentsInWeek = 0;
          			int numDeliverablesInWeek = 0;
            		
            		for(int j = 0; j < lectures.length; j++) {
              			Lecture lecture = lectures[j];              			
              			ArrayList<Assignment> assignmentsOnDate = new ArrayList<Assignment>();
              			ArrayList<Deliverable> deliverablesOnDate = new ArrayList<Deliverable>();
              			for(int k = 0; k < assignments.length; k++) {
              				Assignment assignment = assignments[k];
              				if(assignment.getDueDate() != null && assignment.getDueDate().equals(lecture.getDate())) {
              					numAssignmentsInWeek++;
              				}
              			}
              			for(int k = 0; k < assignments.length; k++) {
              				Assignment assignment = assignments[k];
              				if(assignment.getNumber().equals("Final Project")) {
              					Deliverable[] deliverables = assignment.getDeliverables();
              					for(int l = 0; l < deliverables.length; l++) {
              						Deliverable deliverable = deliverables[l];
              						if(deliverable.getDueDate() != null && deliverable.getDueDate().equals(lecture.getDate())) {
              							numDeliverablesInWeek++;
              						}
              					}
              				}
              			}
            		}
            		
            		System.out.println("Week: " + week.getWeek() + " " + numAssignmentsInWeek + " " + numDeliverablesInWeek);
            		int rowSpan = lectures.length + numAssignmentsInWeek + numDeliverablesInWeek;
            		System.out.println(rowSpan);
            		
            	%>
            	<tr>
            		<%-- <td align="center" rowspan="2"><%= week.getWeek() %></td>
              		<td align="center" rowspan="2"> --%>
              		<td align="center" rowspan="<%= rowSpan %>"><%= week.getWeek() %></td>
              		<td align="center" rowspan="<%= rowSpan %>">
              			<%
              			if(labs != null) {
              			for(int j = 0; j < labs.length; j++) {
              				Lab lab = labs[j];	
              				if(j != labs.length - 1) {
              				%>
              				<a href="<%= lab.getUrl() %>"><%= lab.getTitle() %></a><hr />
              				<%	
              				}
              				else {
              				%>
              				<a href="<%= lab.getUrl() %>"><%= lab.getTitle() %></a>
              				<%		
              				}
              				%>
              				<%
              				}
              			}
              			else {
              				%>
              				<a>No Labs Available</a>
              				<%
              			}
              				%>	
              		</td>
              		<%
              		for(int j = 0; j < lectures.length; j++) {
              			//System.out.println("Lecture " + lectureTracker);
              			lectureTracker++;
              			Lecture lecture = lectures[j];
              			Topic[] topics = lecture.getTopics();
              			ArrayList<File> allFiles = new ArrayList<File>();
              			ArrayList<String> stringChapters = new ArrayList<String>();
              			ArrayList<Integer> intChapters = new ArrayList<Integer>();
              			for(int k = 0; k < topics.length; k++) {
              				Topic topic = topics[k];
              				stringChapters.add(topic.getChapter());
              				Program[] programs = topic.getPrograms();
              				if(programs != null) {
              					for(int l = 0; l < programs.length; l++) {
                  					Program program = programs[l];
                  					File[] files = program.getFiles();
                  					for(int m = 0; m < files.length; m++) {
                  						allFiles.add(files[m]);
                  					}
                  				}
              				}   				
              			}
              			for(int k = 0; k < stringChapters.size(); k++) {
              				String chapter = stringChapters.get(k);
              				//System.out.println("1 " + chapter);
              				if(chapter != null && chapter.indexOf('-') == -1 && chapter.indexOf(',') == -1) {
              					intChapters.add(Integer.parseInt(chapter));
              				}
              				else if(chapter != null) {
              					int index = chapter.indexOf('-');
              					if(index == -1) {
              						index = chapter.indexOf(',');
              					}
              					if(chapter.substring(0, index) != "") {
              						intChapters.add(Integer.parseInt(chapter.substring(0, index)));
              					}
              					if(chapter.substring(index + 1) != "") {
              						intChapters.add(Integer.parseInt(chapter.substring(index + 1)));
              					}
              				}
              			}
              			int minIndex = 0;
              			int maxIndex = 0;
              			for(int k = 0; k < intChapters.size(); k++) {
              				if(intChapters.get(k) < intChapters.get(minIndex)) {
              					minIndex = k;
              				}
              				if(intChapters.get(k) > intChapters.get(maxIndex)) {
              					maxIndex = k;
              				}
              			}
              			
              			ArrayList<Assignment> assignmentsOnDate = new ArrayList<Assignment>();
              			ArrayList<Deliverable> deliverablesOnDate = new ArrayList<Deliverable>();
              			for(int k = 0; k < assignments.length; k++) {
              				Assignment assignment = assignments[k];
              				if(assignment.getDueDate() != null && assignment.getDueDate().equals(lecture.getDate())) {
              					assignmentsOnDate.add(assignment);
              				}
              			}
              			for(int k = 0; k < assignments.length; k++) {
              				Assignment assignment = assignments[k];
              				if(assignment.getNumber().equals("Final Project")) {
              					Deliverable[] deliverables = assignment.getDeliverables();
              					for(int l = 0; l < deliverables.length; l++) {
              						Deliverable deliverable = deliverables[l];
              						if(deliverable.getDueDate() != null && deliverable.getDueDate().equals(lecture.getDate())) {
              							deliverablesOnDate.add(deliverable);
              						}
              					}
              				}
              			}
              			
              			System.out.println("Lecutre: " + lecture.getDate() + " " + assignmentsOnDate.size() + " " + deliverablesOnDate.size());
              			
              			if(j == 0) {
              			%>
              				<td align="center"><%= lecture.getNumber() %></td>
              				<%-- <td align="center"><%= lectureTracker %></td> --%>
              				<td align="center"><%= lecture.getDay() %></td>
              				<td align="center"><%= lecture.getDate() %></td>
              				<td align="center">
              					<%
              					for(int k = 0; k < topics.length; k++) {
              						if(k != topics.length - 1) {
              						%>
              							<a href="<%= topics[k].getUrl() %>"><%= topics[k].getTitle() %></a><hr />
              						<%
              						}
              						else {
              						%>
              							<a href="<%= topics[k].getUrl() %>"><%= topics[k].getTitle() %></a>
              						<%
              						}
              					}
              					%>
              				</td>
              				<%
              				if(intChapters.get(minIndex) == intChapters.get(maxIndex)) {
              				%>	
              				<td align="center"><%= intChapters.get(minIndex) %></td>
              				<%	
              				}
              				else {
              				%>	
              				<td align="center"><%= intChapters.get(minIndex) + "-" + intChapters.get(maxIndex) %></td>
              				<%
              				}
              				%>
              				<td align="center">
              					<%
              					for(int k = 0; k < allFiles.size(); k++) {
              						if(k != allFiles.size() - 1) {
              						%>
              							<a href="<%= allFiles.get(k).getUrl() %>"><%= allFiles.get(k).getTitle() %></a><br />
              						<% 		
              						}
              						else {
              						%>
              							<a href="<%= allFiles.get(k).getUrl() %>"><%= allFiles.get(k).getTitle() %></a>
              						<%	
              						}
              					}
              					%>
              				</td>
            			</tr>
            			<%
            				System.out.println("Before loops: " + assignmentsOnDate.size() + " " + deliverablesOnDate.size());
	           				for(int k = 0; k < assignmentsOnDate.size(); k++) {
            					Assignment assignment = assignmentsOnDate.get(k);
            			%>
            			<tr>
            				<td align="center"> </td>
              				<td align="center"><%= lecture.getDay() %></td>
              				<td align="center"><%= lecture.getDate() %></td>
							<td align="center" colspan="3"><font size="+1" color="red"><b>ASSIGNMENT <%= assignment.getNumber() %> DUE</b></font></td>
            			</tr>              				
              			<%
            				}
            			
        					for(int k = 0; k < deliverablesOnDate.size(); k++) {
        						Deliverable deliverable = deliverablesOnDate.get(k);
        			%>
        				<tr>
        					<td align="center"> </td>
          					<td align="center"><%= lecture.getDay() %></td>
          					<td align="center"><%= lecture.getDate() %></td>
							<td align="center" colspan="3"><font size="+1" color="red"><b>FP - <%= deliverable.getTitle() %> DUE</b></font></td>
        				</tr>              				
          			<%
        					}
              			}
              			else {
              			%>
              			<tr>
              				<td align="center"><%= lecture.getNumber() %></td>
              				<%-- <td align="center"><%= lectureTracker %></td> --%>
              				<td align="center"><%= lecture.getDay() %></td>
              				<td align="center"><%= lecture.getDate() %></td>
              				<td align="center">
              					<%
              					for(int k = 0; k < topics.length; k++) {
              						if(k != topics.length - 1) {
              						%>
              							<a href="<%= topics[k].getUrl() %>"><%= topics[k].getTitle() %></a><hr />
              						<%
              						}
              						else {
              						%>
              							<a href="<%= topics[k].getUrl() %>"><%= topics[k].getTitle() %></a><hr />
              						<%
              						}
              					}
              					%>
              				</td>
              				<%
              				if(intChapters.get(minIndex) == intChapters.get(maxIndex)) {
              				%>	
              				<td align="center"><%= intChapters.get(minIndex) %></td>
              				<%	
              				}
              				else {
              				%>	
              				<td align="center"><%= intChapters.get(minIndex) + "-" + intChapters.get(maxIndex) %></td>
              				<%
              				}
              				%>
              				<td align="center">
              					<%
              					for(int k = 0; k < allFiles.size(); k++) {
              						if(k != allFiles.size() - 1) {
              						%>
              							<a href="<%= allFiles.get(k).getUrl() %>"><%= allFiles.get(k).getTitle() %></a><br />
              						<% 		
              						}
              						else {
              						%>
              							<a href="<%= allFiles.get(k).getUrl() %>"><%= allFiles.get(k).getTitle() %></a>
              						<%	
              						}
              					}
              					%>
              				</td>
            			</tr>	
            			<%
            				System.out.println("Before loops: " + assignmentsOnDate.size() + " " + deliverablesOnDate.size());
	           				for(int k = 0; k < assignmentsOnDate.size(); k++) {
            					Assignment assignment = assignmentsOnDate.get(k);
            			%>
            			<tr>
            				<td align="center"> </td>
              				<td align="center"><%= lecture.getDay() %></td>
              				<td align="center"><%= lecture.getDate() %></td>
							<td align="center" colspan="3"><font size="+1" color="red"><b>ASSIGNMENT <%= assignment.getNumber() %> DUE</b></font></td>
            			</tr>              				
              			<%
            				}
            			
        					for(int k = 0; k < deliverablesOnDate.size(); k++) {
        						Deliverable deliverable = deliverablesOnDate.get(k);
        			%>
        				<tr>
        					<td align="center"> </td>
          					<td align="center"><%= lecture.getDay() %></td>
          					<td align="center"><%= lecture.getDate() %></td>
							<td align="center" colspan="3"><font size="+1" color="red"><b>FP - <%= deliverable.getTitle() %> DUE</b></font></td>
        				</tr>              				
          			<%
        					}
        					
              			}
              		%>
              
              		<%	
              		}
            	}
            	%>           	
        		</table>
			</tr>
		</table>	
	</body>
</html>