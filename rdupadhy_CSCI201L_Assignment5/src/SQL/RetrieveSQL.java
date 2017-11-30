package SQL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import parser.Assignment;
import parser.Assistant;
import parser.Course;
import parser.Deliverable;
import parser.Department;
import parser.Education;
import parser.Exam;
import parser.File;
import parser.Lab;
import parser.Lecture;
import parser.Meeting;
import parser.MeetingPeriod;
import parser.Name;
import parser.OfficeHour;
import parser.Program;
import parser.Schedule;
import parser.School;
import parser.StaffMember;
import parser.Syllabus;
import parser.Test;
import parser.Textbook;
import parser.Time;
import parser.Topic;
import parser.Week;

public class RetrieveSQL {

	private static String connectionQuery = "";
	private static Connection conn = null;
	
	public static void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager.getConnection("jdbc:mysql://localhost/rdupadhy_201_site?user=root&password=apple&useSSL=false");
			conn = DriverManager.getConnection(connectionQuery);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs, PreparedStatement ps) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if(conn != null) {
				conn.close();
				conn = null;
			}
			if(ps != null ) {
				ps = null;
			}
		}catch(SQLException sqle) {
			System.out.println("connection close error");
			sqle.printStackTrace();
		}
	}
	
	public static ArrayList<Assignment> assignmentsComparator(String sort, String connectionQueryArgument) {
		connectionQuery = connectionQueryArgument;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		connect();
		try {
			if(sort.equals("dueDate")) {
				ps = conn.prepareStatement("SELECT * FROM ASSIGNMENT WHERE ASSIGNMENT.number <> 'Final Project' ORDER BY ASSIGNMENT.formattedDueDate");
			}
			else if(sort.equals("assignedDate")) {
				ps = conn.prepareStatement("SELECT * FROM ASSIGNMENT WHERE ASSIGNMENT.number <> 'Final Project' ORDER BY ASSIGNMENT.formattedAssignedDate");
			}
			else if(sort.equals("grade")) {
				ps = conn.prepareStatement("SELECT * FROM ASSIGNMENT WHERE ASSIGNMENT.number <> 'Final Project' ORDER BY ASSIGNMENT.formattedGradePercentage");
			}
			rs = ps.executeQuery();
			if(rs != null) {
				while (rs != null && rs.next()) {
					Assignment assignment = new Assignment();
		
					String number = rs.getString("number");
					String assignedDate = rs.getString("assignedDate");
					String dueDate = rs.getString("dueDate");
					String title = rs.getString("title");
					String url = rs.getString("url");
					String gradePercentage = rs.getString("gradePercentage");
					int assignmentID = rs.getInt(1);
					File[] files = retrieveFileAssignments(assignmentID);
					File[] gradingCriteriaFiles = retrieveGradingCriteriaFileAssignments(assignmentID);
					File[] solutionFiles = retrieveSolutionFileAssignments(assignmentID);
					Deliverable[] deliverables = retrieveDeliverables(assignmentID);
					
					assignment.setNumber(number);
					assignment.setAssignedDate(assignedDate);
					assignment.setDueDate(dueDate);
					assignment.setTitle(title);
					assignment.setUrl(url);
					assignment.setGradePercentage(gradePercentage);
					assignment.setFiles(files);
					assignment.setGradingCriteriaFiles(gradingCriteriaFiles);
					assignment.setSolutionFiles(solutionFiles);
					assignment.setDeliverables(deliverables);
				}
			}
		} catch(SQLException sqle) {
			System.out.println("Schools");
			sqle.printStackTrace();
		}
		finally {
			close(rs, ps);
		}
		return assignments;	
	}
	
	public static ArrayList<Deliverable> finalProjectComparator(String sort,  String connectionQueryArgument) {
		connectionQuery = connectionQueryArgument;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ArrayList<Deliverable> deliverables = new ArrayList<Deliverable>();
		connect();
		try {
			if(sort.equals("date")) {
				ps = conn.prepareStatement("SELECT * FROM DELIVERABLE ORDER BY DELIVERABLE.formattedDueDate;");
			}
			else if(sort.equals("title")) {
				ps = conn.prepareStatement("SELECT * FROM DELIVERABLE ORDER BY DELIVERABLE.title;");
			}
			else if(sort.equals("grade")) {
				ps = conn.prepareStatement("SELECT * FROM DELIVERABLE ORDER BY DELIVERABLE.formattedGradePercentage;");
			}
			rs = ps.executeQuery();
			if(rs != null) {
				while (rs != null && rs.next()) {
					Deliverable deliverable = new Deliverable();
					
					String number = rs.getString("number");
					String dueDate = rs.getString("dueDate");
					String title = rs.getString("title");
					String gradePercentage = rs.getString("gradePercentage");
					int deliverableID = rs.getInt(1);
					File[] files = retrieveFileDeliverables(deliverableID);
					
					deliverable.setNumber(number);
					deliverable.setDueDate(dueDate);
					deliverable.setTitle(title);
					deliverable.setGradePercentage(gradePercentage);
					deliverable.setFiles(files);
				}
			}
		} catch(SQLException sqle) {
			System.out.println("Schools");
			sqle.printStackTrace();
		}
		finally {
			close(rs, ps);
		}
		return deliverables;	
	}

	
	public static Education retrieveEducation(String connectionQueryArgument) {
		connectionQuery = connectionQueryArgument;
		Education education = new Education();
		School[] schools = retrieveSchools();
		education.setSchools(schools);
		return education;
	}
	
	public static School[] retrieveSchools() {
		ResultSet rs = null;
		PreparedStatement ps = null;
		School[] schools = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM SCHOOL");
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			schools = new School[count];
			ps = conn.prepareStatement("SELECT * FROM SCHOOL");
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					School school = new School();
					
					String name = rs.getString("name");
					String image = rs.getString("image");
					int schoolID = rs.getInt(1);
					Department[] departments = retrieveDepartments(schoolID);
					
					school.setName(name);
					school.setImage(image);
					school.setDepartments(departments);
					
					schools[index] = school;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Schools");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return schools;		
	}
	
	public static Department[] retrieveDepartments(int schoolID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Department[] departments = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM DEPARTMENT");
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			departments = new Department[count];
			ps = conn.prepareStatement("SELECT * FROM DEPARTMENT INNER JOIN SCHOOL ON DEPARTMENT.schoolID=SCHOOL.schoolID AND DEPARTMENT.schoolID=" + schoolID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					Department department = new Department();
					
					String longname = rs.getString("longname");
					String prefix = rs.getString("prefix");
					int departmentID = rs.getInt(1);
					Course[] courses = retrieveCourses(departmentID);
					
					department.setLongName(longname);
					department.setPrefix(prefix);
					department.setCourses(courses);
					
					departments[index] = department;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Departments");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return departments;	
	}
	
	public static Course[] retrieveCourses(int departmentID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Course[] courses = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM COURSE");
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			courses = new Course[count];
			ps = conn.prepareStatement("SELECT * FROM COURSE INNER JOIN DEPARTMENT ON COURSE.departmentID=DEPARTMENT.departmentID AND COURSE.departmentID=" + departmentID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					Course course = new Course();
					
					String number = rs.getString("number");
					String term = rs.getString("term");
					String year = rs.getString("year");
					String title = rs.getString("title");
					String units = rs.getString("units");
					int courseID = rs.getInt(1);
					StaffMember[] staffMembers = retrieveStaffMembers(courseID);
					Meeting[] meetings = retrieveMeetings(courseID);
					Syllabus syllabus = retrieveSyllabus(courseID);
					Schedule schedule = retrieveSchedule(courseID);
					Assignment[] assignments = retrieveAssignments(courseID);
					Exam[] exams = retrieveExams(courseID);
					
					course.setNumber(number);
					course.setTerm(term);
					course.setYear(year);
					course.setTitle(title);
					course.setUnits(units);
					course.setStaffMembers(staffMembers);
					course.setMeetings(meetings);
					course.setSyllabus(syllabus);
					course.setSchedule(schedule);
					course.setAssignments(assignments);
					course.setExams(exams);

					courses[index] = course;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Courses");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return courses;
	}
	
	public static StaffMember[] retrieveStaffMembers(int courseID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		StaffMember[] staffMembers = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM STAFFMEMBER");
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			staffMembers = new StaffMember[count];
			ps = conn.prepareStatement("SELECT * FROM STAFFMEMBER INNER JOIN COURSE ON STAFFMEMBER.courseID=COURSE.courseID AND STAFFMEMBER.courseID=" + courseID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					StaffMember staffMember = new StaffMember();
					
					String type = rs.getString("type"); 
					String id = rs.getString("id"); 
					String email = rs.getString("email"); 
					String image = rs.getString("image"); 
					String phone = rs.getString("phone"); 
					String office = rs.getString("office");
					int staffMemberID = rs.getInt(1);
					Name name = retrieveName(staffMemberID);
					OfficeHour[] officeHours = retrieveOfficeHours(staffMemberID);

					staffMember.setType(type);
					staffMember.setId(id);
					staffMember.setEmail(email);
					staffMember.setImage(image);
					staffMember.setPhone(phone);
					staffMember.setOffice(office);
					staffMember.setName(name);
					staffMember.setOfficeHours(officeHours);
					staffMembers[index] = staffMember;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Staff Members");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return staffMembers;
	}
	
	public static Name retrieveName(int staffMemberID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Name name = new Name();
		connect();
		try {
			ps = conn.prepareStatement("SELECT * FROM NAME INNER JOIN STAFFMEMBER ON NAME.staffMemberID=STAFFMEMBER.staffMemberID AND NAME.staffMemberID=" + staffMemberID);
			rs = ps.executeQuery();
			while (rs.next()) {
				String fname = rs.getString("fname"); 
				String lname = rs.getString("lname"); 
				
				name.setFname(fname);
				name.setLname(lname);
			}
			
		} catch(SQLException sqle){
			System.out.println("Name");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return name;
	}
	
	public static OfficeHour[] retrieveOfficeHours(int staffMemberID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		OfficeHour[] officeHours = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM OFFICEHOUR WHERE OFFICEHOUR.staffMemberID=" + staffMemberID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			officeHours = new OfficeHour[count];
			ps = conn.prepareStatement("SELECT * FROM OFFICEHOUR INNER JOIN STAFFMEMBER ON OFFICEHOUR.staffMemberID=STAFFMEMBER.staffMemberID AND OFFICEHOUR.staffMemberID=" + staffMemberID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					OfficeHour officeHour = new OfficeHour();
					
					String day = rs.getString("day"); 
					int officehourID = rs.getInt(1);
					Time time = retrieveTimeOfficeHour(officehourID);

					officeHour.setDay(day);
					officeHour.setTime(time);
					officeHours[index] = officeHour;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Office Hours");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return officeHours;
	}
	
	public static Time retrieveTimeOfficeHour(int officehourID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Time time = new Time();
		connect();
		try {
			ps = conn.prepareStatement("SELECT * FROM TIME INNER JOIN OFFICEHOUR ON TIME.officehourID=OFFICEHOUR.officehourID AND TIME.officehourID=" + officehourID);
			rs = ps.executeQuery();
			while (rs.next()) {
				String start = rs.getString("start"); 
				String end = rs.getString("end"); 
				
				time.setStart(start);
				time.setEnd(end);
			}
			
		} catch(SQLException sqle){
			System.out.println("Time Office Hours");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return time;
	}
	
	public static Meeting[] retrieveMeetings(int courseID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Meeting[] meetings = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM MEETING");
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			meetings = new Meeting[count];
			ps = conn.prepareStatement("SELECT * FROM MEETING INNER JOIN COURSE ON MEETING.courseID=COURSE.courseID AND MEETING.courseID=" + courseID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					Meeting meeting = new Meeting();
					
					String type = rs.getString("type"); 
					String section = rs.getString("section"); 
					String room = rs.getString("room"); 
					int meetingID = rs.getInt(1);
					MeetingPeriod[] meetingPeriods = retrieveMeetingPeriods(meetingID);
					Assistant[] assistants = retrieveAssistants(meetingID);

					meeting.setType(type);
					meeting.setSection(section);
					meeting.setRoom(room);
					meeting.setMeetingPeriods(meetingPeriods);
					meeting.setAssistants(assistants);
					meetings[index] = meeting;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Meetings");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return meetings;
	}
	
	public static MeetingPeriod[] retrieveMeetingPeriods(int meetingID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		MeetingPeriod[] meetingPeriods = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM MEETINGPERIOD WHERE MEETINGPERIOD.meetingID=" + meetingID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			meetingPeriods = new MeetingPeriod[count];
			ps = conn.prepareStatement("SELECT * FROM MEETINGPERIOD INNER JOIN MEETING ON MEETINGPERIOD.meetingID=MEETING.meetingID AND MEETINGPERIOD.meetingID=" + meetingID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					MeetingPeriod meetingPeriod = new MeetingPeriod();
					
					String day = rs.getString("day"); 
					int meetingPeriodID = rs.getInt(1);
					Time time = retrieveTimeMeetingPeriod(meetingPeriodID);

					meetingPeriod.setDay(day);
					meetingPeriod.setTime(time);
					meetingPeriods[index] = meetingPeriod;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Meeting Periods");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return meetingPeriods;
	}
	
	public static Time retrieveTimeMeetingPeriod(int meetingPeriodID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Time time = new Time();
		connect();
		try {
			ps = conn.prepareStatement("SELECT * FROM TIME INNER JOIN MEETINGPERIOD ON TIME.meetingPeriodID=MEETINGPERIOD.meetingPeriodID AND TIME.meetingPeriodID=" + meetingPeriodID);
			rs = ps.executeQuery();
			while (rs.next()) {
				String start = rs.getString("start"); 
				String end = rs.getString("end"); 
				
				time.setStart(start);
				time.setEnd(end);
			}
			
		} catch(SQLException sqle){
			System.out.println("Time Meeting Periods");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return time;
	}
	
	public static Assistant[] retrieveAssistants(int meetingID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Assistant[] assistants = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM ASSISTANT WHERE ASSISTANT.meetingID=" + meetingID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			assistants = new Assistant[count];
			ps = conn.prepareStatement("SELECT * FROM ASSISTANT INNER JOIN MEETING ON ASSISTANT.meetingID=MEETING.meetingID AND ASSISTANT.meetingID=" + meetingID);
			rs = ps.executeQuery();
			int index = 0;
			while (rs.next()) {
				Assistant assistant = new Assistant();
				
				String staffMemberID = rs.getString("staffMemberID"); 

				assistant.setStaffMemberID(staffMemberID);
				assistants[index] = assistant;
				index++;
			}
			
		} catch(SQLException sqle){
			System.out.println("Assistants");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return assistants;
	}
	
	public static Syllabus retrieveSyllabus(int courseID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Syllabus syllabus = new Syllabus();
		connect();
		try {
			ps = conn.prepareStatement("SELECT * FROM SYLLABUS INNER JOIN COURSE ON SYLLABUS.courseID=COURSE.courseID AND SYLLABUS.courseID=" + courseID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				String url = rs.getString("url"); 
				
				syllabus.setUrl(url);
			}
			
		} catch(SQLException sqle){
			System.out.println("Syllabus");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return syllabus;
	}
	
	public static Schedule retrieveSchedule(int courseID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Schedule schedule = new Schedule();
		connect();
		try {
			ps = conn.prepareStatement("SELECT * FROM SYLLABUS INNER JOIN COURSE ON SYLLABUS.courseID=COURSE.courseID AND SYLLABUS.courseID=" + courseID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				int scheduleID = rs.getInt(1);
				Textbook[] textbooks = retrieveTextbooks(scheduleID);
				Week[] weeks = retrieveWeeks(scheduleID);
				
				schedule.setTextbooks(textbooks);
				schedule.setWeeks(weeks);
			}
			
		} catch(SQLException sqle){
			System.out.println("Schedule");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return schedule;
	}
	
	public static Textbook[] retrieveTextbooks(int scheduleID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Textbook[] textbooks = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM TEXTBOOK WHERE TEXTBOOK.scheduleID=" + scheduleID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			textbooks = new Textbook[count];
			ps = conn.prepareStatement("SELECT * FROM TEXTBOOK INNER JOIN SCHEDULE ON TEXTBOOK.scheduleID=SCHEDULE.scheduleID AND TEXTBOOK.scheduleID=" + scheduleID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) { 
					Textbook textbook = new Textbook();
					
					String number = rs.getString("number");
					String author = rs.getString("author");
					String title = rs.getString("title");
					String publisher = rs.getString("publisher");
					String year = rs.getString("year");
					String isbn = rs.getString("isbn");

					textbook.setNumber(number);
					textbook.setAuthor(author);
					textbook.setTitle(title);
					textbook.setPublisher(publisher);
					textbook.setYear(year);
					textbook.setIsbn(isbn);
					textbooks[index] = textbook;
					index++;
				}
			}				
			
		} catch(SQLException sqle){
			System.out.println("Textbooks");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return textbooks;
	}
	
	public static Week[] retrieveWeeks(int scheduleID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Week[] weeks = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM WEEK WHERE WEEK.scheduleID=" + scheduleID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			weeks = new Week[count];
			ps = conn.prepareStatement("SELECT * FROM WEEK INNER JOIN SCHEDULE ON WEEK.scheduleID=SCHEDULE.scheduleID AND WEEK.scheduleID=" + scheduleID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					Week week = new Week();
					
					String weekString = rs.getString("week");
					int weekID = rs.getInt(1);
					Lab[] labs = retrieveLabs(weekID);
					Lecture[] lectures = retrieveLectures(weekID);
					
					week.setWeek(weekString);
					week.setLabs(labs);
					week.setLectures(lectures);
					weeks[index] = week;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Weeks");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return weeks;
	}
	
	public static Lab[] retrieveLabs(int weekID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Lab[] labs = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM LAB WHERE LAB.weekID=" + weekID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			labs = new Lab[count];
			ps = conn.prepareStatement("SELECT * FROM LAB INNER JOIN WEEK ON LAB.weekID=WEEK.weekID AND LAB.weekID=" + weekID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					Lab lab = new Lab();
					
					String number = rs.getString("number");
					String title = rs.getString("title");
					String url = rs.getString("url");
					int labID = rs.getInt(1);
					File[] files = retrieveFileLab(labID);
					
					lab.setNumber(number);
					lab.setTitle(title);
					lab.setUrl(url);
					lab.setFiles(files);
					labs[index] = lab;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Labs");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return labs;
	}
	
	public static File[] retrieveFileLab(int labID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		File[] files = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM FILE WHERE FILE.labID IS NOT NULL AND FILE.labID=" + labID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			files = new File[count];
			ps = conn.prepareStatement("SELECT * FROM FILE INNER JOIN LAB ON FILE.labID=LAB.labID AND FILE.labID=" + labID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					File file = new File();
					
					String number = rs.getString("number");
					String title = rs.getString("title");
					String url = rs.getString("url");
					
					file.setNumber(number);
					file.setTitle(title);
					file.setUrl(url);
					files[index] = file;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("File Lab");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return files;
	}	
	
	public static Lecture[] retrieveLectures(int weekID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Lecture[] lectures = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM LECTURE WHERE LECTURE.weekID=" + weekID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			lectures = new Lecture[count];
			ps = conn.prepareStatement("SELECT * FROM LECTURE INNER JOIN WEEK ON LECTURE.weekID=WEEK.weekID AND LECTURE.weekID=" + weekID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					Lecture lecture = new Lecture();
					
					String number = rs.getString("number");
					String date = rs.getString("date");
					String day = rs.getString("day");
					int lectureID = rs.getInt(1);
					Topic[] topics = retrieveTopics(lectureID);
					
					lecture.setNumber(number);
					lecture.setDate(date);
					lecture.setDay(day);
					lecture.setTopics(topics);
					lectures[index] = lecture;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Lectures");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return lectures;
	}
	
	public static Topic[] retrieveTopics(int lectureID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Topic[] topics = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM TOPIC WHERE TOPIC.lectureID=" + lectureID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			topics = new Topic[count];
			ps = conn.prepareStatement("SELECT * FROM TOPIC INNER JOIN LECTURE ON TOPIC.lectureID=LECTURE.lectureID AND TOPIC.lectureID=" + lectureID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					Topic topic = new Topic();
					
					String number = rs.getString("number");
					String title = rs.getString("title");
					String url = rs.getString("url");
					String chapter = rs.getString("chapter");
					int topicID = rs.getInt(1);
					Program[] programs = retrievePrograms(topicID);
					
					topic.setNumber(number);
					topic.setTitle(title);
					topic.setUrl(url);
					topic.setChapter(chapter);
					topic.setPrograms(programs);
					topics[index] = topic;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Topics");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return topics;
	}
	
	public static Program[] retrievePrograms(int topicID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Program[] programs = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM PROGRAM WHERE PROGRAM.topicID=" + topicID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			programs = new Program[count];
			ps = conn.prepareStatement("SELECT * FROM PROGRAM INNER JOIN TOPIC ON PROGRAM.topicID=TOPIC.topicID AND PROGRAM.topicID=" + topicID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					Program program = new Program();
					
					int programID = rs.getInt(1);
					File[] files = retrieveFilePrograms(programID);
					
					program.setFiles(files);
					programs[index] = program;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Programs");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return programs;
	}
	
	public static File[] retrieveFilePrograms(int programID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		File[] files = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM FILE WHERE FILE.programID IS NOT NULL AND FILE.programID=" + programID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			files = new File[count];
			ps = conn.prepareStatement("SELECT * FROM FILE INNER JOIN PROGRAM ON FILE.programID=PROGRAM.programID AND FILE.programID=" + programID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					File file = new File();
					
					String number = rs.getString("number");
					String title = rs.getString("title");
					String url = rs.getString("url");
					
					file.setNumber(number);
					file.setTitle(title);
					file.setUrl(url);
					files[index] = file;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("File Programs");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return files;
	}
	
	public static Assignment[] retrieveAssignments(int courseID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Assignment[] assignments = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM ASSIGNMENT");
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			assignments = new Assignment[count];
			ps = conn.prepareStatement("SELECT * FROM ASSIGNMENT INNER JOIN COURSE ON ASSIGNMENT.courseID=COURSE.courseID AND ASSIGNMENT.courseID=" + courseID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					Assignment assignment = new Assignment();
					
					String number = rs.getString("number");
					String assignedDate = rs.getString("assignedDate");
					String dueDate = rs.getString("dueDate");
					String title = rs.getString("title");
					String url = rs.getString("url");
					String gradePercentage = rs.getString("gradePercentage");
					int assignmentID = rs.getInt(1);
					File[] files = retrieveFileAssignments(assignmentID);
					File[] gradingCriteriaFiles = retrieveGradingCriteriaFileAssignments(assignmentID);
					File[] solutionFiles = retrieveSolutionFileAssignments(assignmentID);
					Deliverable[] deliverables = retrieveDeliverables(assignmentID);
					
					assignment.setNumber(number);
					assignment.setAssignedDate(assignedDate);
					assignment.setDueDate(dueDate);
					assignment.setTitle(title);
					assignment.setUrl(url);
					assignment.setGradePercentage(gradePercentage);
					assignment.setFiles(files);
					assignment.setGradingCriteriaFiles(gradingCriteriaFiles);
					assignment.setSolutionFiles(solutionFiles);
					assignment.setDeliverables(deliverables);
					
					assignments[index] = assignment;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Assignments");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return assignments;
	}
	
	public static File[] retrieveFileAssignments(int assignmentID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		File[] files = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM FILE WHERE FILE.assignmentID IS NOT NULL AND FILE.assignmentID=" + assignmentID + " AND FILE.type='assignment'");
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			files = new File[count];
			ps = conn.prepareStatement("SELECT * FROM FILE INNER JOIN ASSIGNMENT ON FILE.assignmentID=ASSIGNMENT.assignmentID AND FILE.assignmentID=" + assignmentID + " AND FILE.type='assignment'");
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					File file = new File();
					
					String number = rs.getString("number");
					String title = rs.getString("title");
					String url = rs.getString("url");
					
					file.setNumber(number);
					file.setTitle(title);
					file.setUrl(url);
					files[index] = file;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("File Assignments");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return files;
	}
	
	public static File[] retrieveGradingCriteriaFileAssignments(int assignmentID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		File[] files = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM FILE WHERE FILE.assignmentID IS NOT NULL AND FILE.assignmentID=" + assignmentID + " AND FILE.type='grading criteria assignment'");
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			files = new File[count];
			ps = conn.prepareStatement("SELECT * FROM FILE INNER JOIN ASSIGNMENT ON FILE.assignmentID=ASSIGNMENT.assignmentID AND FILE.assignmentID=" + assignmentID + " AND FILE.type='grading criteria assignment'");
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					File file = new File();
					
					String number = rs.getString("number");
					String title = rs.getString("title");
					String url = rs.getString("url");
					
					file.setNumber(number);
					file.setTitle(title);
					file.setUrl(url);
					files[index] = file;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Grading Criteria File Assignments");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return files;
	}
	
	public static File[] retrieveSolutionFileAssignments(int assignmentID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		File[] files = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM FILE WHERE FILE.assignmentID IS NOT NULL AND FILE.assignmentID=" + assignmentID + " AND FILE.type='solution assignment'");
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			files = new File[count];
			ps = conn.prepareStatement("SELECT * FROM FILE INNER JOIN ASSIGNMENT ON FILE.assignmentID=ASSIGNMENT.assignmentID AND FILE.assignmentID=" + assignmentID + " AND FILE.type='solution assignment'");
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					File file = new File();
					
					String number = rs.getString("number");
					String title = rs.getString("title");
					String url = rs.getString("url");
					
					file.setNumber(number);
					file.setTitle(title);
					file.setUrl(url);
					files[index] = file;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Solution File Assignments");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return files;
	}	
	
	public static Deliverable[] retrieveDeliverables(int assignmentID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Deliverable[] deliverables = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM DELIVERABLE WHERE DELIVERABLE.assignmentID=" + assignmentID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			deliverables = new Deliverable[count];
			ps = conn.prepareStatement("SELECT * FROM DELIVERABLE INNER JOIN ASSIGNMENT ON DELIVERABLE.assignmentID=ASSIGNMENT.assignmentID AND DELIVERABLE.assignmentID=" + assignmentID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					Deliverable deliverable = new Deliverable();
					
					String number = rs.getString("number");
					String dueDate = rs.getString("dueDate");
					String title = rs.getString("title");
					String gradePercentage = rs.getString("gradePercentage");
					int deliverableID = rs.getInt(1);
					File[] files = retrieveFileDeliverables(deliverableID);
					
					deliverable.setNumber(number);
					deliverable.setDueDate(dueDate);
					deliverable.setTitle(title);
					deliverable.setGradePercentage(gradePercentage);
					deliverable.setFiles(files);
					
					deliverables[index] = deliverable;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Deliverables");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return deliverables;
	}
	
	public static File[] retrieveFileDeliverables(int deliverableID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		File[] files = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM FILE WHERE FILE.deliverableID IS NOT NULL AND FILE.deliverableID=" + deliverableID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			files = new File[count];
			ps = conn.prepareStatement("SELECT * FROM FILE INNER JOIN DELIVERABLE ON FILE.deliverableID=DELIVERABLE.deliverableID AND FILE.deliverableID=" + deliverableID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					File file = new File();
					
					String number = rs.getString("number");
					String title = rs.getString("title");
					String url = rs.getString("url");
					
					file.setNumber(number);
					file.setTitle(title);
					file.setUrl(url);
					files[index] = file;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("File Deliverables");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return files;
	} 	
	
	public static Exam[] retrieveExams(int courseID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Exam[] exams = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM EXAM");
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			exams = new Exam[count];
			ps = conn.prepareStatement("SELECT * FROM EXAM INNER JOIN COURSE ON EXAM.courseID=COURSE.courseID AND EXAM.courseID=" + courseID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					Exam exam = new Exam();
					
					String year = rs.getString("year"); 
					String semester = rs.getString("semester"); 
					int examID = rs.getInt(1);
					Test[] tests = retrieveTests(examID);

					exam.setYear(year);
					exam.setSemester(semester);
					exam.setTests(tests);
					exams[index] = exam;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Exams");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return exams;
	}
	
	public static Test[] retrieveTests(int examID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Test[] tests = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM TEST WHERE TEST.examID=" + examID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			tests = new Test[count];
			ps = conn.prepareStatement("SELECT * FROM TEST INNER JOIN EXAM ON TEST.examID=EXAM.examID AND TEST.examID=" + examID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					Test test = new Test();
					
					String title = rs.getString("title"); 
					int testID = rs.getInt(1);
					File[] files = retrieveFileTests(testID);

					test.setTitle(title);
					test.setFiles(files);
					tests[index] = test;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("Tests");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return tests;
	}
	
	public static File[] retrieveFileTests(int testID) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		File[] files = null;
		int count = 0;
		connect();
		try {
			ps = conn.prepareStatement("SELECT COUNT(*) FROM FILE WHERE FILE.testID IS NOT NULL AND FILE.testID=" + testID);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			files = new File[count];
			ps = conn.prepareStatement("SELECT * FROM FILE INNER JOIN TEST ON FILE.testID=TEST.testID AND FILE.testID=" + testID);
			rs = ps.executeQuery();
			int index = 0;
			if(rs != null) {
				while (rs != null && rs.next()) {
					File file = new File();
					
					String number = rs.getString("number");
					String title = rs.getString("title");
					String url = rs.getString("url");
					
					file.setNumber(number);
					file.setTitle(title);
					file.setUrl(url);
					files[index] = file;
					index++;
				}
			}
			
		} catch(SQLException sqle){
			System.out.println("File Tests");
			sqle.printStackTrace();
		}
		finally{
			close(rs, ps);
		}
		return files;
	}
	
	
}