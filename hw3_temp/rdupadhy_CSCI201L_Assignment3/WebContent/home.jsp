<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="parser.*" %>    
<%@ page import="servlet.*" %>  
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<%
		Education education;
		String design;
		
		if(request.getAttribute("education") != null) {
			education = (Education)request.getAttribute("education"); 
			design = (String)request.getAttribute("design");
			System.out.println("Design: " + design);
			session.setAttribute("education", education);
			session.setAttribute("design", design);
			/* session.setMaxInactiveInterval(60); */
		}
		else {
			education = (Education)session.getAttribute("education"); 
			design = (String)session.getAttribute("design");
		}
		
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
    	
		String number = course.getNumber();
		String term = course.getTerm();
		String year = course.getYear();
		StaffMember[] staffMembers = course.getStaffMembers();
		Meeting[] meetings = course.getMeetings();
		String title = course.getTitle();
		String units = course.getUnits();
		Syllabus syllabus = course.getSyllabus();
		Schedule schedule = course.getSchedule();
		Assignment[] assignments = course.getAssignments();
		Exam[] exams = course.getExams();
		
		ArrayList<StaffMember> professors = new ArrayList<StaffMember>();
		for(int i = 0; i < staffMembers.length; i++) {
			if(staffMembers[i].getType().equals("instructor")) {
				professors.add(staffMembers[i]);
			}
		}
		
		ArrayList<Meeting> lectures = new ArrayList<Meeting>();
		for(int i = 0; i < meetings.length; i++) {
			if(meetings[i].getType().equals("lecture") || meetings[i].getType().equals("quiz")) {
				lectures.add(meetings[i]);
			}
		}
		
		ArrayList<Meeting> quizzes = new ArrayList<Meeting>();
		for(int i = 0; i < meetings.length; i++) {
			if(meetings[i].getType().equals("quiz")) {
				quizzes.add(meetings[i]);
			}
		}
		
		ArrayList<Meeting> labs = new ArrayList<Meeting>();
		for(int i = 0; i < meetings.length; i++) {
			if(meetings[i].getType().equals("lab")) {
				labs.add(meetings[i]);
			}
		}		
	%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><%= school.getName() %>: <%= department.getPrefix() + " " + course.getNumber() + " " + course.getTerm() + " " + course.getYear() %></title>
		<!-- <link rel="stylesheet" type="text/css" href="home1.css" /> -->
		<%
		if(design.equals("design1")) {
		%>
			<link rel="stylesheet" type="text/css" href="home1.css" />
		<%	
		}
		else {
		%>
			<link rel="stylesheet" type="text/css" href="home2.css" /> 
		<%
		}
		%>
		<script>
			function validate() {
				
				var xhttp = new XMLHttpRequest();
				xhttp.open("GET", "home_search_validate.jsp?parameter=" + document.searchForm.parameter.value, false);
				xhttp.send();
				
				console.log("Result: " + xhttp.responseText.trim());
				var result = xhttp.responseText.trim();
				var resultArray = result.split(',');
				for(var i = 0; i < resultArray.length; i++) {
					resultArray[i] = resultArray[i].trim();
				}
				
				var professorRows = document.getElementById("professors").rows;
				var lectureRows = document.getElementById("lectures").rows;
				var labRows = document.getElementById("labs").rows;
				var staffmemberRows = document.getElementById("staffmembers").rows;
				
				for(var i = 0; i < resultArray.length; i++) {
					
					for(var j = 0; j < professorRows.length; j++) {
						var professorRow = professorRows[j];
						for(var k = 0; k < professorRow.children.length; k++) {
							var child = professorRow.children[k];
							if(child.innerText == resultArray[i]) {
								child.style.backgroundColor = "aqua";
								child.parentElement.children[k - 1].style.backgroundColor = "aqua";
							}
						}
					}
					
					for(var j = 0; j < lectureRows.length; j++) {
						var lectureRow = lectureRows[j];
						for(var k = 0; k < lectureRow.children.length; k++) {
							var child = lectureRow.children[k];
							if(child.innerText.indexOf(resultArray[i]) != -1) {
								child.style.backgroundColor = "aqua";
							}
						}
					}
					
					for(var j = 0; j < labRows.length; j++) {
						var labRow = labRows[j];
						for(var k = 0; k < labRow.children.length; k++) {
							var child = labRow.children[k];
							if(child.innerText.indexOf(resultArray[i]) != -1) {
								var childRows = child.children[0].rows[0].children;
								for(var l = 0; l < childRows.length; l++) {
									if(childRows[l].innerText.trim() == resultArray[i]) {
										childRows[l].style.backgroundColor = "aqua";
									}
								}
							}
						}
					}
					
					for(var j = 0; j < staffmemberRows.length; j++) {
						var staffmemberRow = staffmemberRows[j];
						for(var k = 0; k < staffmemberRow.children.length; k++) {
							var child = staffmemberRow.children[k];
							if(child.innerText.indexOf(resultArray[i]) != -1) {
								child.style.backgroundColor = "aqua";
							}
						}
					}
				}
				
				/* for(var i = 0; i < professorRows.length; i++) {
					var professorRow = professorRows[i];
					for(var j = 0; j < professorRow.children.length; j++) {
						var child = professorRow.children[j];
						if(child.innerText == result) {
							child.style.backgroundColor = "aqua";
							child.parentElement.children[j - 1].style.backgroundColor = "aqua";
						}
					}
				}
				
				for(var i = 0; i < lectureRows.length; i++) {
					var lectureRow = lectureRows[i];
					for(var j = 0; j < lectureRow.children.length; j++) {
						var child = lectureRow.children[j];
						if(child.innerText.indexOf(result) != -1) {
							child.style.backgroundColor = "aqua";
						}
					}
				}
				
				for(var i = 0; i < labRows.length; i++) {
					var labRow = labRows[i];
					for(var j = 0; j < labRow.children.length; j++) {
						var child = labRow.children[j];
						if(child.innerText.indexOf(result) != -1) {
							var childRows = child.children[0].rows[0].children;
							for(var k = 0; k < childRows.length; k++) {
								if(childRows[k].innerText.trim() == result) {
									childRows[k].style.backgroundColor = "aqua";
								}
							}
						}
					}
				}
				
				for(var i = 0; i < staffmemberRows.length; i++) {
					var staffmemberRow = staffmemberRows[i];
					for(var j = 0; j < staffmemberRow.children.length; j++) {
						var child = staffmemberRow.children[j];
						if(child.innerText.indexOf(result) != -1) {
							child.style.backgroundColor = "aqua";
						}
					}
				}	 */			
				
				/* console.log(staffmemberRows); */
				
				return false;
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
         		 	<font size="+1"><%= department.getPrefix() + " " + course.getNumber() %> Home</font><br /><br />
          			<font size="+1"><a href="<%= syllabus.getUrl() %>">Syllabus</a></font><br /><br />
          			<font size="+1"><a href="lectures.jsp">Lectures</a></font><br /><br />
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
          			<p ><hr size="4" />
          			<p>
          				<form name="searchForm" method="GET" onsubmit="return validate();">
          					 <input type="text" name="parameter" /> 
          					 <input type="submit" name="submit" value="Search Staff" /> 
          					 <input type="reset" name="reset" value="Clear Search" /> 
          				</form>
          			<p ><hr size="4" />
          			<p><font size="-1">
						<table border="1" id="professors">
							<tr>
                				<th align="center">Picture</th>
                				<th align="center">Professor</th>
                				<th align="center">Office</th>
                				<th align="center">Phone</th>
                				<th align="center">Email</th>
                				<th align="center">Office Hours</th>
              				</tr>
              				<%
              				for(int i = 0; i < professors.size(); i++) {
							%>
							<tr>
                				<td><img width="100" height="100" src="<%= professors.get(i).getImage() %>" /></td>
                				<td><font size="-1"><%= professors.get(i).getName().getFname() + " " + professors.get(i).getName().getLname() %></font></td>
                				<td><font size="-1"><%= professors.get(i).getOffice() %></font></td>
                				<td><font size="-1"><%= professors.get(i).getPhone() %></font></td>
                				<td><font size="-1"><%= professors.get(i).getEmail() %></font></td>
                				<td><font size="-1">
                				<%
                				for(int j = 0; j < professors.get(i).getOfficeHours().length; j++) {
                				%>
                  					<%= professors.get(i).getOfficeHours()[j].getDay() + " " + professors.get(i).getOfficeHours()[j].getTime().getStart() + "-" + professors.get(i).getOfficeHours()[j].getTime().getEnd() %> 
                  					<hr />
                				<%	
                				}
                				%>
                  					Any day by appointment
                				</font></td>
              				</tr>
							<%
							}
							%>
                		</table>
                		<br />	
                	<p><hr size="4" /></p>
                	<b><font size="+1" class="title">Lecture Schedule</font></b>
          			<table border="2" cellpadding="5" width="590" id="lectures">
                		<tr>
              				<th align="center">Sect. No.</th><th align="center">Day &amp; Time</th><th align="center">Room</th><th>Lecture Assistant</th>
            			</tr>
            			<%
            			for(int i = 0; i < lectures.size(); i++) {
            				MeetingPeriod[] meetingPeriods = lectures.get(i).getMeetingPeriods();
            				Assistant[] assistants;
            				if(lectures.get(i).getAssistants() != null) {
            					assistants = lectures.get(i).getAssistants();
            				}
            				else {
            					assistants = new Assistant[0];
            				}
            				Time time = meetingPeriods[0].getTime();
            				String days = "";
            				for(int j = 0; j < meetingPeriods.length; j++) {
            					if(j == meetingPeriods.length - 1) {
            						days += meetingPeriods[j].getDay() + " " + meetingPeriods[j].getTime().getStart() + "-" + meetingPeriods[j].getTime().getEnd();
            					}
            					else {
            						days += meetingPeriods[j].getDay() + " " + meetingPeriods[j].getTime().getStart() + "-" + meetingPeriods[j].getTime().getEnd() + ", "; 

            					}
            				}
            				ArrayList<StaffMember> staffMembersById = new ArrayList<StaffMember>();
            				for(int j = 0; j < assistants.length; j++) {
            					for(int k = 0; k < staffMembers.length; k++) {
            						if(assistants[j].getStaffMemberID().equals(staffMembers[k].getId())) {
            							staffMembersById.add(staffMembers[k]);
            						}
            					}
            				}
            			%>
            			<tr>
              				<td align="center"><font size="-1"><%= lectures.get(i).getSection() %></font></td>
              				<td align="center"><font size="-1"><%= days %></font></td>
              				<td align="center"><font size="-1"><%= lectures.get(i).getRoom() %></font></td>
              				<%
              				for(int j = 0; j < staffMembersById.size(); j++) {
              				%>
              				<td align="center"><font size="-1"><img src="<%= staffMembersById.get(j).getImage() %>"><br /><a href="mailto:<%= staffMembersById.get(j).getEmail() %>"><%= staffMembersById.get(j).getName().getFname() + " " + staffMembersById.get(j).getName().getLname() %></a></font></td>
              				<%	
              				}
              				%>
            			</tr>
            			<%
            			}
            			%>
            		</table>
            		<p><hr size="4" />
          			<b><font size="+1" class="title">Lab Schedule</font></b>
          			<table border="2" cellpadding="5" width="590" id="labs">
            			<tr>
              				<th align="center">Sect. No.</th><th align="center">Day &amp; Time</th><th align="center">Room</th><th align="center">Lab Assistants</th>
            			</tr>	
						<%
						for(int i = 0; i < labs.size(); i++) {
							MeetingPeriod[] meetingPeriods = labs.get(i).getMeetingPeriods();
            				Assistant[] assistants;
            				if(labs.get(i).getAssistants() != null) {
            					assistants = labs.get(i).getAssistants();
            				}
            				else {
            					assistants = new Assistant[0];
            				}
            				Time time = meetingPeriods[0].getTime();
            				String days = "";
            				for(int j = 0; j < meetingPeriods.length; j++) {
            					if(j == meetingPeriods.length - 1) {
            						days += meetingPeriods[j].getDay() + " " + meetingPeriods[j].getTime().getStart() + "-" + meetingPeriods[j].getTime().getEnd();
            					}
            					else {
            						days += meetingPeriods[j].getDay() + " " + meetingPeriods[j].getTime().getStart() + "-" + meetingPeriods[j].getTime().getEnd() + ", ";

            					}
            				}
            				ArrayList<StaffMember> staffMembersById = new ArrayList<StaffMember>();
            				for(int j = 0; j < assistants.length; j++) {
            					for(int k = 0; k < staffMembers.length; k++) {
            						if(assistants[j].getStaffMemberID().equals(staffMembers[k].getId())) {
            							staffMembersById.add(staffMembers[k]);
            						}
            					}
            				}
						%>
						 <tr>
              				<td align="center"><font size="-1"><%= labs.get(i).getSection() %></font></td>
              				<td align="center"><font size="-1"><%= days %></font></td>
              				<td align="center"><font size="-1"><%= labs.get(i).getRoom() %></font></td>
              				<td align="center">
                				<table border="0">
                  					<tr>
                  					<%
                					for(int j = 0; j < staffMembersById.size(); j++) {
                					%>
                					<td align="center"><font size="-1"><img src="<%= staffMembersById.get(j).getImage() %>"><br /><a href="mailto:<%= staffMembersById.get(j).getEmail() %>"><%= staffMembersById.get(j).getName().getFname() + " " + staffMembersById.get(j).getName().getLname() %></a></font></td>
                					<%
                					}
                					%>
                  					</tr>
                				</table>
              				</td>
            			</tr>
						
						<%
						}
						%>
					</table>
					<br /><hr size="4" /><br />
						<b><font size="+1" class="title">Office Hours</font></b> - All staff office hours are held in the SAL Open Lab.  Prof. Miller's office hours are listed above.<br />
						<%
				
						ArrayList<ArrayList<StaffMember>> table = new ArrayList<ArrayList<StaffMember>>();
						
						ArrayList<StaffMember> test1 = new ArrayList<StaffMember>();
						test1.add(null);
						test1.add(null);
						test1.add(null);
						test1.add(null);
						test1.add(null);
						
						ArrayList<StaffMember> test2 = new ArrayList<StaffMember>();
						test2.add(null);
						test2.add(null);
						test2.add(null);
						test2.add(null);
						test2.add(null);
						
						ArrayList<StaffMember> test3 = new ArrayList<StaffMember>();
						test3.add(null);
						test3.add(null);
						test3.add(null);
						test3.add(null);
						test3.add(null);
						
						ArrayList<StaffMember> test4 = new ArrayList<StaffMember>();
						test4.add(null);
						test4.add(null);
						test4.add(null);
						test4.add(null);
						test4.add(null);
						
						ArrayList<StaffMember> test5 = new ArrayList<StaffMember>();
						test5.add(null);
						test5.add(null);
						test5.add(null);
						test5.add(null);
						test5.add(null);
						
						ArrayList<StaffMember> test6 = new ArrayList<StaffMember>();
						test6.add(null);
						test6.add(null);
						test6.add(null);
						test6.add(null);
						test6.add(null);
						
						table.add(test1);
						table.add(test2);
						table.add(test3);
						table.add(test4);
						table.add(test5);
						table.add(test6);
						
						for(int i = 0; i < staffMembers.length; i++) {
							StaffMember staffMember = staffMembers[i];
  							OfficeHour[] officeHours;
  							if(staffMember.getType().equals("instructor")) {
  								continue;
  							}
  							if(staffMember.getOfficeHours() != null) {
  								officeHours = staffMember.getOfficeHours();
  							} 
  							else {
  								officeHours = new OfficeHour[0];
  							}
  							for(int j = 0; j < officeHours.length; j++) {
    							if(officeHours[j].getDay().equals("Monday")) {
      								if(officeHours[j].getTime().getStart().equals("10:00a.m.")) {
      									table.get(0).set(0, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("12:00p.m.")) {
      									table.get(0).set(1, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("2:00p.m.")) {
      									table.get(0).set(2, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("4:00p.m.")) {
      									table.get(0).set(3, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("6:00p.m.")) {
      									table.get(0).set(4, staffMember);
      								}
    							}
    							else if(officeHours[j].getDay().equals("Tuesday")) {
      								if(officeHours[j].getTime().getStart().equals("10:00a.m.")) {
      									table.get(1).set(0, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("12:00p.m.")) {
      									table.get(1).set(1, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("2:00p.m.")) {
      									table.get(1).set(2, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("4:00p.m.")) {
      									table.get(1).set(3, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("6:00p.m.")) {
      									table.get(1).set(4, staffMember);
      								}
    							}
    							else if(officeHours[j].getDay().equals("Wednesday")) {
      								if(officeHours[j].getTime().getStart().equals("10:00a.m.")) {
      									table.get(2).set(0, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("12:00p.m.")) {
      									table.get(2).set(1, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("2:00p.m.")) {
      									table.get(2).set(2, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("4:00p.m.")) {
      									table.get(2).set(3, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("6:00p.m.")) {
      									table.get(2).set(4, staffMember);
      								}
    							}
    							else if(officeHours[j].getDay().equals("Thursday")) {
      								if(officeHours[j].getTime().getStart().equals("10:00a.m.")) {
      									table.get(3).set(0, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("12:00p.m.")) {
      									table.get(3).set(1, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("2:00p.m.")) {
      									table.get(3).set(2, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("4:00p.m.")) {
      									table.get(3).set(3, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("6:00p.m.")) {
      									table.get(3).set(4, staffMember);
      								}
    							}
    							else if(officeHours[j].getDay().equals("Friday")) {
      								if(officeHours[j].getTime().getStart().equals("10:00a.m.")) {
      									table.get(4).set(0, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("12:00p.m.")) {
      									table.get(4).set(1, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("2:00p.m.")) {
      									table.get(4).set(2, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("4:00p.m.")) {
      									table.get(4).set(3, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("6:00p.m.")) {
      									table.get(4).set(4, staffMember);
      								}
    							}
    							else if(officeHours[j].getDay().equals("Saturday")) {
      								if(officeHours[j].getTime().getStart().equals("10:00a.m.")) {
      									table.get(5).set(0, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("12:00p.m.")) {
      									table.get(5).set(1, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("2:00p.m.")) {
      									table.get(5).set(2, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("4:00p.m.")) {
      									table.get(5).set(3, staffMember);
      								}
      								else if(officeHours[j].getTime().getStart().equals("6:00p.m.")) {
      									table.get(5).set(4, staffMember);
      								}
    							}
  							}
						}
						ArrayList<StaffMember> staffMembersArrayList;
						
						%>
            			<table border="1" id="staffmembers">
            				<tr>
                				<th></th>
                				<th>10:00a.m.-12:00p.m.</th>
                				<th>12:00p.m.-2:00p.m.</th>
                				<th>2:00p.m.-4:00p.m.</th>
                				<th>4:00p.m.-6:00p.m.</th>
                				<th>6:00p.m.-8:00p.m.</th>
              				</tr>
              				<tr>
              					<th>Monday</th>
              					<%
              					staffMembersArrayList = table.get(0);
              					for(int i = 0; i < staffMembersArrayList.size(); i++) {
              						StaffMember staffMember = staffMembersArrayList.get(i);
              						if(staffMember != null) {
              					%>
              					<td><table border="0"><tr><td><img src="<%= staffMember.getImage() %>" /></td></tr><tr><td><font size="-1"><a href="mailto:<%= staffMember.getEmail() %>"><%= staffMember.getName().getFname() + " " + staffMember.getName().getLname() %></a><br />&nbsp;</font></td></tr></table></td>
              					
              					<%		
              						}
              						else {
              					%>
              					<td><table border="0"><tr><td></td></tr><tr><td><font size="-1"><br />&nbsp;</font></td></tr></table></td>
              					<%		
              						}
              					}
              					staffMembersArrayList = null;
              					%>	
              				</tr>
              				<tr>
              					<th>Tuesday</th>
              					<%
              					staffMembersArrayList = table.get(1);
              					for(int i = 0; i < staffMembersArrayList.size(); i++) {
              						StaffMember staffMember = staffMembersArrayList.get(i);
              						if(staffMember != null) {
              					%>
              					<td><table border="0"><tr><td><img src="<%= staffMember.getImage() %>" /></td></tr><tr><td><font size="-1"><a href="mailto:<%= staffMember.getEmail() %>"><%= staffMember.getName().getFname() + " " + staffMember.getName().getLname() %></a><br />&nbsp;</font></td></tr></table></td>
              					
              					<%		
              						}
              						else {
              					%>
              					<td><table border="0"><tr><td></td></tr><tr><td><font size="-1"><br />&nbsp;</font></td></tr></table></td>
              					<%		
              						}
              					}
              					staffMembersArrayList = null;
              					%>	
              				</tr>
              				<tr>
              					<th>Wednesday</th>
              					<%
              					staffMembersArrayList = table.get(2);
              					for(int i = 0; i < staffMembersArrayList.size(); i++) {
              						StaffMember staffMember = staffMembersArrayList.get(i);
              						if(staffMember != null) {
              					%>
              					<td><table border="0"><tr><td><img src="<%= staffMember.getImage() %>" /></td></tr><tr><td><font size="-1"><a href="mailto:<%= staffMember.getEmail() %>"><%= staffMember.getName().getFname() + " " + staffMember.getName().getLname() %></a><br />&nbsp;</font></td></tr></table></td>
              					
              					<%		
              						}
              						else {
              					%>
              					<td><table border="0"><tr><td></td></tr><tr><td><font size="-1"><br />&nbsp;</font></td></tr></table></td>
              					<%		
              						}
              					}
              					staffMembersArrayList = null;
              					%>	
              				</tr>
              				<tr>
              					<th>Thursday</th>
              					<%
              					staffMembersArrayList = table.get(3);
              					for(int i = 0; i < staffMembersArrayList.size(); i++) {
              						StaffMember staffMember = staffMembersArrayList.get(i);
              						if(staffMember != null) {
              					%>
              					<td><table border="0"><tr><td><img src="<%= staffMember.getImage() %>" /></td></tr><tr><td><font size="-1"><a href="mailto:<%= staffMember.getEmail() %>"><%= staffMember.getName().getFname() + " " + staffMember.getName().getLname() %></a><br />&nbsp;</font></td></tr></table></td>
              					
              					<%		
              						}
              						else {
              					%>
              					<td><table border="0"><tr><td></td></tr><tr><td><font size="-1"><br />&nbsp;</font></td></tr></table></td>
              					<%		
              						}
              					}
              					staffMembersArrayList = null;
              					%>	
              				</tr>
              				<tr>
              					<th>Friday</th>
              					<%
              					staffMembersArrayList = table.get(4);
              					for(int i = 0; i < staffMembersArrayList.size(); i++) {
              						StaffMember staffMember = staffMembersArrayList.get(i);
              						if(staffMember != null) {
              					%>
              					<td><table border="0"><tr><td><img src="<%= staffMember.getImage() %>" /></td></tr><tr><td><font size="-1"><a href="mailto:<%= staffMember.getEmail() %>"><%= staffMember.getName().getFname() + " " + staffMember.getName().getLname() %></a><br />&nbsp;</font></td></tr></table></td>
              					
              					<%		
              						}
              						else {
              					%>
              					<td><table border="0"><tr><td></td></tr><tr><td><font size="-1"><br />&nbsp;</font></td></tr></table></td>
              					<%		
              						}
              					}
              					staffMembersArrayList = null;
              					%>	
              				</tr> 
              				<tr>
              					<th>Saturday</th>
              					<%
              					staffMembersArrayList = table.get(5);
              					for(int i = 0; i < staffMembersArrayList.size(); i++) {
              						StaffMember staffMember = staffMembersArrayList.get(i);
              						if(staffMember != null) {
              					%>
              					<td><table border="0"><tr><td><img src="<%= staffMember.getImage() %>" /></td></tr><tr><td><font size="-1"><a href="mailto:<%= staffMember.getEmail() %>"><%= staffMember.getName().getFname() + " " + staffMember.getName().getLname() %></a><br />&nbsp;</font></td></tr></table></td>
              					
              					<%		
              						}
              						else {
              					%>
              					<td><table border="0"><tr><td></td></tr><tr><td><font size="-1"><br />&nbsp;</font></td></tr></table></td>
              					<%		
              						}
              					}
              					staffMembersArrayList = null;
              					%>	
              				</tr>        				
						</table>
					</td>
      			</tr>
    		</table>
		</body>
</html>


