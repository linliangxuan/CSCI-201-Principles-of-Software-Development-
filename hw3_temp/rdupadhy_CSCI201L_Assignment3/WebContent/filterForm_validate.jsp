<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="parser.*"%>
<%@ page import="servlet.*"%>
<%@ page import="comparator.*"%>
<%@ page import="java.util.*"%>
<%
	String sort = request.getParameter("sort");

	Education education = (Education)session.getAttribute("education");
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

	if(sort.equals("lectures")) {
%>
<tr>
	<th align="center">Week #</th>
	<th align="center">Lab</th>
	<th align="center">Lecture #</th>
	<th align="center">Day</th>
	<th align="center" width="40">Date</th>
	<th align="center">Lecture Topic</th>
	<th align="center">Chapter</th>
	<th>Program</th>
</tr>
<%
            	int lectureTracker = 0;
            	for(int i = 0; i < weeks.length; i++) {
            		
            		Week week = weeks[i];
            		Lab[] labs = week.getLabs();
            		Lecture[] lectures = week.getLectures();
            		int rowSpan = lectures.length;
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
              				%> <a href="<%= lab.getUrl() %>"><%= lab.getTitle() %></a>
		<hr /> <%	
              				}
              				else {
              				%> <a href="<%= lab.getUrl() %>"><%= lab.getTitle() %></a>
		<%		
              				}
              				%> <%
              				}
              			}
              			else {
              				%> <a>No Labs Available</a> <%
              			}
              				%>
	</td>
	<%
              		for(int j = 0; j < lectures.length; j++) {
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
              						%> <a href="<%= topics[k].getUrl() %>"><%= topics[k].getTitle() %></a>
		<hr /> <%
              						}
              						else {
              						%> <a href="<%= topics[k].getUrl() %>"><%= topics[k].getTitle() %></a>
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
              						%> <a href="<%= allFiles.get(k).getUrl() %>"><%= allFiles.get(k).getTitle() %></a><br />
		<% 		
              						}
              						else {
              						%> <a href="<%= allFiles.get(k).getUrl() %>"><%= allFiles.get(k).getTitle() %></a>
		<%	
              						}
              					}
              					%>
	</td>
</tr>
<%         
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
              						%> <a href="<%= topics[k].getUrl() %>"><%= topics[k].getTitle() %></a>
		<hr /> <%
              						}
              						else {
              						%> <a href="<%= topics[k].getUrl() %>"><%= topics[k].getTitle() %></a>
		<hr /> <%
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
              						%> <a href="<%= allFiles.get(k).getUrl() %>"><%= allFiles.get(k).getTitle() %></a><br />
		<% 		
              						}
              						else {
              						%> <a href="<%= allFiles.get(k).getUrl() %>"><%= allFiles.get(k).getTitle() %></a>
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
              		}
            	}
            	%>
<%	
	}
	
	else if(sort.equals("dueDates")) {
%>
<tr>
	<th align="center">Week #</th>
	<th align="center">Lab</th>
	<th align="center">Lecture #</th>
	<th align="center">Day</th>
	<th align="center" width="40">Date</th>
	<th align="center">Lecture Topic</th>
	<th align="center">Chapter</th>
	<th>Program</th>
</tr>
<%
            	int lectureTracker = 0;
            	for(int i = 0; i < weeks.length; i++) {
            		Week week = weeks[i];
            		Lab[] labs = week.getLabs();
            		Lecture[] lectures = week.getLectures();            		

              		for(int j = 0; j < lectures.length; j++) {
              			Lecture lecture = lectures[j];
              			
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
              				%>
<%
	           				for(int k = 0; k < assignmentsOnDate.size(); k++) {
            					Assignment assignment = assignmentsOnDate.get(k);
            			%>
<tr>
	<td align="center" colspan="3"><%= week.getWeek() %></td>            			
	<td align="center"><%= lecture.getDay() %></td>
	<td align="center"><%= lecture.getDate() %></td>
	<td align="center" colspan="3"><font size="+1" color="red"><b>ASSIGNMENT
				<%= assignment.getNumber() %> DUE
		</b></font></td>
</tr>
<%
            				}
            			
        					for(int k = 0; k < deliverablesOnDate.size(); k++) {
        						Deliverable deliverable = deliverablesOnDate.get(k);
        			%>
<tr>
	<td align="center" colspan="3"><%= week.getWeek() %></td>
	<td align="center"><%= lecture.getDay() %></td>
	<td align="center"><%= lecture.getDate() %></td>
	<td align="center" colspan="3"><font size="+1" color="red"><b>FP
				- <%= deliverable.getTitle() %> DUE
		</b></font></td>
</tr>
<%
        					}
              		%>

<%	
              		}
            	}
            	%>
<%			
	}
	
	else if(sort.equals("all")) {
%>< tr>
<th align="center">Week #</th>
<th align="center">Lab</th>
<th align="center">Lecture #</th>
<th align="center">Day</th>
<th align="center" width="40">Date</th>
<th align="center">Lecture Topic</th>
<th align="center">Chapter</th>
<th>Program</th>
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
              				%> <a href="<%= lab.getUrl() %>"><%= lab.getTitle() %></a>
		<hr /> <%	
              				}
              				else {
              				%> <a href="<%= lab.getUrl() %>"><%= lab.getTitle() %></a>
		<%		
              				}
              				%> <%
              				}
              			}
              			else {
              				%> <a>No Labs Available</a> <%
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
              						%> <a href="<%= topics[k].getUrl() %>"><%= topics[k].getTitle() %></a>
		<hr /> <%
              						}
              						else {
              						%> <a href="<%= topics[k].getUrl() %>"><%= topics[k].getTitle() %></a>
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
              						%> <a href="<%= allFiles.get(k).getUrl() %>"><%= allFiles.get(k).getTitle() %></a><br />
		<% 		
              						}
              						else {
              						%> <a href="<%= allFiles.get(k).getUrl() %>"><%= allFiles.get(k).getTitle() %></a>
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
	<td align="center"></td>
	<td align="center"><%= lecture.getDay() %></td>
	<td align="center"><%= lecture.getDate() %></td>
	<td align="center" colspan="3"><font size="+1" color="red"><b>ASSIGNMENT
				<%= assignment.getNumber() %> DUE
		</b></font></td>
</tr>
<%
            				}
            			
        					for(int k = 0; k < deliverablesOnDate.size(); k++) {
        						Deliverable deliverable = deliverablesOnDate.get(k);
        			%>
<tr>
	<td align="center"></td>
	<td align="center"><%= lecture.getDay() %></td>
	<td align="center"><%= lecture.getDate() %></td>
	<td align="center" colspan="3"><font size="+1" color="red"><b>FP
				- <%= deliverable.getTitle() %> DUE
		</b></font></td>
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
              						%> <a href="<%= topics[k].getUrl() %>"><%= topics[k].getTitle() %></a>
		<hr /> <%
              						}
              						else {
              						%> <a href="<%= topics[k].getUrl() %>"><%= topics[k].getTitle() %></a>
		<hr /> <%
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
              						%> <a href="<%= allFiles.get(k).getUrl() %>"><%= allFiles.get(k).getTitle() %></a><br />
		<% 		
              						}
              						else {
              						%> <a href="<%= allFiles.get(k).getUrl() %>"><%= allFiles.get(k).getTitle() %></a>
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
	<td align="center"></td>
	<td align="center"><%= lecture.getDay() %></td>
	<td align="center"><%= lecture.getDate() %></td>
	<td align="center" colspan="3"><font size="+1" color="red"><b>ASSIGNMENT
				<%= assignment.getNumber() %> DUE
		</b></font></td>
</tr>
<%
            				}
            			
        					for(int k = 0; k < deliverablesOnDate.size(); k++) {
        						Deliverable deliverable = deliverablesOnDate.get(k);
        			%>
<tr>
	<td align="center"></td>
	<td align="center"><%= lecture.getDay() %></td>
	<td align="center"><%= lecture.getDate() %></td>
	<td align="center" colspan="3"><font size="+1" color="red"><b>FP
				- <%= deliverable.getTitle() %> DUE
		</b></font></td>
</tr>
<%
        					}
        					
              			}
              		%>

<%	
              		}
            	}
            	%>
<%		
	}

%>
