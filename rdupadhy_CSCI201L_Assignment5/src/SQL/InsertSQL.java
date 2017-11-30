package SQL;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

import parser.*;
import parser.Time;

public class InsertSQL {
	
	private static String connectionQuery = "";
	private static Connection conn = null;
	private static ResultSet rs = null;
	private static PreparedStatement ps = null;
	
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
	
	public static void close() {
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
	
	
	public static void insertSchool(School school, String connectionQueryArgument) {
		connectionQuery = connectionQueryArgument;
		String name = school.getName();
		String image = school.getImage();
		Department[] departments = school.getDepartments();
		connect();
		try {
			String query = "INSERT INTO SCHOOL (name, image) VALUES (?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, name);
			ps.setString(2, image);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int schoolID = rs.getInt(1);
				if(departments != null) {
					for(int i = 0; i < departments.length; i++) {
						Department department = departments[i];
						insertDepartment(department, schoolID);
					}
				}
            }
			
		} catch(SQLException sqle){
			System.out.println("School");
			sqle.printStackTrace();
		}
		finally{
			System.out.println("Done");
			close();
		}
	}
	
	public static void insertDepartment(Department department, int schoolID) {
		String longname = department.getLongName();
		String prefix = department.getPrefix();
		Course[] courses = department.getCourses();
		connect();
		try {
			String query = "INSERT INTO DEPARTMENT (longname, prefix, schoolID) VALUES (?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, longname);
			ps.setString(2, prefix);
			ps.setInt(3, schoolID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int departmentID = rs.getInt(1);
				if(courses != null) {
					for(int i = 0; i < courses.length; i++) {
						Course course = courses[i];
						insertCourse(course, departmentID);
					}
				}				
            }
			
		} catch(SQLException sqle){
			System.out.println("Department");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertCourse(Course course, int departmentID) {
		String number = course.getNumber();
		String term = course.getTerm();
		String year = course.getYear();
		String title = course.getTitle();
		String units = course.getUnits();
		StaffMember[] staffMembers = course.getStaffMembers();
		Meeting[] meetings = course.getMeetings();
		Syllabus syllabus = course.getSyllabus();
		Schedule schedule = course.getSchedule();
		Assignment[] assignments = course.getAssignments();
		Exam[] exams = course.getExams();
		connect();
		try {
			String query = "INSERT INTO COURSE (number, term, year, title, units, departmentID) VALUES (?,?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, term);
			ps.setString(3, year);
			ps.setString(4, title);
			ps.setString(5, units);
			ps.setInt(6, departmentID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int courseID = rs.getInt(1);
				if(staffMembers != null) {
					for(int i = 0; i < staffMembers.length; i++) {
						StaffMember staffMember = staffMembers[i];
						insertStaffMember(staffMember, courseID);
					}
				}
				if(meetings != null) {
					for(int i = 0; i < meetings.length; i++) {
						Meeting meeting = meetings[i];
						insertMeeting(meeting, courseID);
					}
				}
				insertSyllabus(syllabus, courseID);
				insertSchedule(schedule, courseID);
				if(assignments != null) {
					for(int i = 0; i < assignments.length; i++) {
						Assignment assignment = assignments[i];
						insertAssignment(assignment, courseID);
					}
				}
				if(exams != null) {
					for(int i = 0; i < exams.length; i++) {
						Exam exam = exams[i];
						insertExam(exam, courseID);
					}
				}
				
            }
			
		} catch(SQLException sqle){
			System.out.println("Course");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertStaffMember(StaffMember staffMember, int courseID) {
		String type = staffMember.getType();
		String id = staffMember.getId();
		String email = staffMember.getEmail();
		String image = staffMember.getImage();
		String phone = staffMember.getPhone();
		String office = staffMember.getOffice();
		Name name = staffMember.getName();
		OfficeHour[] officeHours = staffMember.getOfficeHours();
		connect();
		try {
			String query = "INSERT INTO STAFFMEMBER (type, id, email, image, phone, office, courseID) VALUES (?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, type);
			ps.setString(2, id);
			ps.setString(3, email);
			ps.setString(4, image);
			ps.setString(5, phone);
			ps.setString(6, office);
			ps.setInt(7, courseID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int staffMemberID = rs.getInt(1);
				insertName(name, staffMemberID);
				if(officeHours != null) {
					for(int i = 0; i < officeHours.length; i++) {
						OfficeHour officeHour = officeHours[i];
						insertOfficeHour(officeHour, staffMemberID);
					}
				}
            }
			
		} catch(SQLException sqle){
			System.out.println("Staff Member");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertName(Name name, int staffMemberID) {
		String fname = name.getFname();
		String lname = name.getLname();
		connect();
		try {
			String query = "INSERT INTO NAME (fname, lname, staffmemberID) VALUES (?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, fname);
			ps.setString(2, lname);
			ps.setInt(3, staffMemberID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
		} catch(SQLException sqle){
			System.out.println("Name");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertOfficeHour(OfficeHour officeHour, int staffMemberID) {
		String day = officeHour.getDay();
		Time time = officeHour.getTime();
		connect();
		try {
			String query = "INSERT INTO OFFICEHOUR (day, staffmemberID) VALUES (?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, day);
			ps.setInt(2, staffMemberID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int officeHourID = rs.getInt(1);
				insertTimeOfficeHour(time, officeHourID);
            }
			
		} catch(SQLException sqle){
			System.out.println("Office Hour");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertTimeOfficeHour(Time time, int officeHourID) {
		String start = time.getStart();
		String end = time.getEnd();
		connect();
		try {
			String query = "INSERT INTO TIME (start, end, officehourID) VALUES (?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, start);
			ps.setString(2, end);
			ps.setInt(3, officeHourID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
		} catch(SQLException sqle){
			System.out.println("Time Office Hour");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertMeeting(Meeting meeting, int courseID) {
		String type = meeting.getType();
		String section = meeting.getSection();
		String room = meeting.getRoom();
		MeetingPeriod[] meetingPeriods = meeting.getMeetingPeriods();
		Assistant[] assistants = meeting.getAssistants();
		connect();
		try {
			String query = "INSERT INTO MEETING (type, section, room, courseID) VALUES (?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, type);
			ps.setString(2, section);
			ps.setString(3, room);
			ps.setInt(4, courseID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int meetingID = rs.getInt(1);
				if(meetingPeriods != null) {
					for(int i = 0; i < meetingPeriods.length; i++) {
						MeetingPeriod meetingPeriod = meetingPeriods[i];
						insertMeetingPeriod(meetingPeriod, meetingID);
					}
				}
				if(assistants != null) {
					for(int i = 0; i < assistants.length; i++) {
						Assistant assistant = assistants[i];
						insertAssistant(assistant, meetingID);
					}
				}
            }
			
		} catch(SQLException sqle){
			System.out.println("Meeting");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertMeetingPeriod(MeetingPeriod meetingPeriod, int meetingID) {
		String day = meetingPeriod.getDay();
		Time time = meetingPeriod.getTime();
		connect();
		try {
			String query = "INSERT INTO MEETINGPERIOD (day, meetingID) VALUES (?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, day);
			ps.setInt(2, meetingID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int meetingPeriodID = rs.getInt(1);
				insertTimeMeetingPeriod(time, meetingPeriodID);
            }
			
		} catch(SQLException sqle){
			System.out.println("Meeting Period");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
		
	}
	
	public static void insertTimeMeetingPeriod(Time time, int meetingPeriodID) {
		String start = time.getStart();
		String end = time.getEnd();
		connect();
		try {
			String query = "INSERT INTO TIME (start, end, meetingperiodID) VALUES (?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, start);
			ps.setString(2, end);
			ps.setInt(3, meetingPeriodID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
		} catch(SQLException sqle){
			System.out.println("Time Meeting Period");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}

	public static void insertAssistant(Assistant assistant, int meetingID) {
		String staffMemberID = assistant.getStaffMemberID();
		connect();
		try {
			String query = "INSERT INTO ASSISTANT (staffMemberID, meetingID) VALUES (?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, staffMemberID);
			ps.setInt(2, meetingID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
		} catch(SQLException sqle){
			System.out.println("Assistant");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertSyllabus(Syllabus syllabus, int courseID) {
		String url = syllabus.getUrl();
		connect();
		try {
			String query = "INSERT INTO SYLLABUS (url, courseID) VALUES (?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, url);
			ps.setInt(2, courseID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
		} catch(SQLException sqle){
			System.out.println("Syllabus");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertSchedule(Schedule schedule, int courseID) {
		Textbook[] textbooks = schedule.getTextbooks();
		Week[] weeks = schedule.getWeeks();
		connect();
		try {
			String query = "INSERT INTO SCHEDULE (courseID) VALUES (?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, courseID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int scheduleID = rs.getInt(1);
				if(textbooks != null) {
					for(int i = 0; i < textbooks.length; i++) {
						Textbook textbook = textbooks[i];
						insertTextbook(textbook, scheduleID);
					}
				}
				if(weeks != null) {
					for(int i = 0; i < weeks.length; i++) {
						Week week = weeks[i];
						insertWeek(week, scheduleID);
					}
				}
            }
			
		} catch(SQLException sqle){
			System.out.println("Schedule");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertTextbook(Textbook textbook, int scheduleID) {
		String number = textbook.getNumber();
		String author = textbook.getAuthor();
		String title = textbook.getTitle();
		String publisher = textbook.getPublisher();
		String year = textbook.getYear();
		String isbn = textbook.getIsbn();
		connect();
		try {
			String query = "INSERT INTO TEXTBOOK (number, author, title, publisher, year, isbn, scheduleID) VALUES (?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, author);
			ps.setString(3, title);
			ps.setString(4, publisher);
			ps.setString(5, year);
			ps.setString(6, isbn);
			ps.setInt(7, scheduleID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
		} catch(SQLException sqle){
			System.out.println("Textbook");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertWeek(Week week, int scheduleID) {
		String weekString = week.getWeek();
		Lab[] labs = week.getLabs();
		Lecture[] lectures = week.getLectures();
		connect();
		try {
			String query = "INSERT INTO WEEK (week, scheduleID) VALUES (?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, weekString);
			ps.setInt(2, scheduleID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int weekID = rs.getInt(1);
				if(labs != null) {
					for(int i = 0; i < labs.length; i++) {
						Lab lab = labs[i];
						insertLab(lab, weekID);
					}
				}
				if(lectures != null) {
					for(int i = 0; i < lectures.length; i++) {
						Lecture lecture = lectures[i];
						insertLecture(lecture, weekID);
					}
				}
            }
			
		} catch(SQLException sqle){
			System.out.println("Week");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertLab(Lab lab, int weekID) {
		String number = lab.getNumber();
		String title = lab.getTitle();
		String url = lab.getUrl();
		File[] files = lab.getFiles();
		connect();
		try {
			String query = "INSERT INTO LAB (number, title, url, weekID) VALUES (?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, title);
			ps.setString(3, url);
			ps.setInt(4, weekID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int labID = rs.getInt(1);
				if(files != null) {
					for(int i = 0; i < files.length; i++) {
						File file = files[i];
						insertFileLab(file, labID);
					}
				}
            }
			
		} catch(SQLException sqle){
			System.out.println("Lab");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertFileLab(File file, int labID) {
		String number = file.getNumber();
		String title = file.getTitle();
		String url = file.getUrl();
		String type = "lab";
		connect();
		try {
			String query = "INSERT INTO FILE (number, title, url, type, labID) VALUES (?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, title);
			ps.setString(3, url);
			ps.setString(4, type);
			ps.setInt(5, labID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
		} catch(SQLException sqle){
			System.out.println("File Lab");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertLecture(Lecture lecture, int weekID) {
		String number = lecture.getNumber();
		String date = lecture.getDate();
		String day = lecture.getDay();
		Topic[] topics = lecture.getTopics();
		connect();
		try {
			String query = "INSERT INTO LECTURE (number, date, day, weekID) VALUES (?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, date);
			ps.setString(3, day);
			ps.setInt(4, weekID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int lectureID = rs.getInt(1);
				if(topics != null) {
					for(int i = 0; i < topics.length; i++) {
						Topic topic = topics[i];
						insertTopic(topic, lectureID);
					}
				}
            }
			
		} catch(SQLException sqle){
			System.out.println("Lecture");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertTopic(Topic topic, int lectureID) {
		String number = topic.getNumber();
		String title = topic.getTitle();
		String url = topic.getUrl();
		String chapter = topic.getChapter();
		Program[] programs = topic.getPrograms();
		connect();
		try {
			String query = "INSERT INTO TOPIC (number, title, url, chapter, lectureID) VALUES (?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, title);
			ps.setString(3, url);
			ps.setString(4, chapter);
			ps.setInt(5, lectureID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int topicID = rs.getInt(1);
				if(programs != null) {
					for(int i = 0; i < programs.length; i++) {
						Program program = programs[i];
						insertProgram(program, topicID);
					}
				}
            }
			
		} catch(SQLException sqle){
			System.out.println("Topic");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertProgram(Program program, int topicID) {
		File[] files = program.getFiles();
		connect();
		try {
			String query = "INSERT INTO PROGRAM (topicID) VALUES (?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, topicID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int programID = rs.getInt(1);
				if(files != null) {
					for(int i = 0; i < files.length; i++) {
						File file = files[i];
						insertFileProgram(file, programID);
					}
				}
            }
			
		} catch(SQLException sqle){
			System.out.println("Program");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertFileProgram(File file, int programID) {
		String number = file.getNumber();
		String title = file.getTitle();
		String url = file.getUrl();
		String type = "program";
		connect();
		try {
			String query = "INSERT INTO FILE (number, title, url, type, programID) VALUES (?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, title);
			ps.setString(3, url);
			ps.setString(4, type);
			ps.setInt(5, programID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
		} catch(SQLException sqle){
			System.out.println("File Program");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertAssignment(Assignment assignment, int courseID) {
		String number = assignment.getNumber();
		if(number.equals("Final Project")) {
			insertAssignmentFinal(assignment, courseID);
		}
		else {
			insertAssignmentNormal(assignment, courseID);
		}
	}
	
	public static void insertAssignmentNormal(Assignment assignment, int courseID) {
		String number = assignment.getNumber();
		String assignedDate = assignment.getAssignedDate();
		String dueDate = assignment.getDueDate();
		String title = assignment.getTitle();
		String url = assignment.getUrl();
		String gradePercentage = assignment.getGradePercentage();
		File[] files = assignment.getFiles();
		File[] gradingCriteriaFiles = assignment.getGradingCriteriaFiles();
		File[] solutionFiles = assignment.getSolutionFiles();
		Deliverable[] deliverables = assignment.getDeliverables();
		
		SimpleDateFormat format1 = new SimpleDateFormat("m-d-yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-m-d");
		Date tempAssignedDate;
		Date formattedAssignedDate = null;
		Date tempDueDate;
		Date formattedDueDate = null;
		try {
			tempAssignedDate = new Date(format1.parse(assignedDate).getTime());
			String formattedAssignedDateString = format2.format(tempAssignedDate);
			formattedAssignedDate = Date.valueOf(formattedAssignedDateString);
			
			tempDueDate = new Date(format1.parse(dueDate).getTime());
			String formattedDueDateString = format2.format(tempDueDate);
			formattedDueDate = Date.valueOf(formattedDueDateString);
			
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		int index = gradePercentage.indexOf("%");
		String formattedGradePercentageString = gradePercentage.substring(0, index);
		double formattedgradePercentage = Double.parseDouble(formattedGradePercentageString);
		
		connect();
		try {
			String query = "INSERT INTO ASSIGNMENT (number, assignedDate, dueDate, title, url, gradePercentage, courseID, formattedAssignedDate, formattedDueDate, formattedGradePercentage) VALUES (?,?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, assignedDate);
			ps.setString(3, dueDate);
			ps.setString(4, title);
			ps.setString(5, url);
			ps.setString(6, gradePercentage);
			ps.setInt(7, courseID);
			ps.setDate(8, formattedAssignedDate);
			ps.setDate(9, formattedDueDate);
			ps.setDouble(10, formattedgradePercentage);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int assignmentID = rs.getInt(1);
				if(files != null) {
					for(int i = 0; i < files.length; i++) {
						File file = files[i];
						insertFileAssignment(file, assignmentID);
					}
				}
				if(gradingCriteriaFiles != null) {
					for(int i = 0; i < gradingCriteriaFiles.length; i++) {
						File gradingCriteriaFile = gradingCriteriaFiles[i];
						insertGradingCriteriaFileAssignment(gradingCriteriaFile, assignmentID);
					}
				}
				if(solutionFiles != null) {
					for(int i = 0; i < solutionFiles.length; i++) {
						File solutionFile = solutionFiles[i];
						insertSolutionFileAssignment(solutionFile, assignmentID);
					}
				}
				if(deliverables != null) {
					for(int i = 0; i < deliverables.length; i++) {
						Deliverable deliverable = deliverables[i];
						insertDeliverable(deliverable, assignmentID);
					}
				}
            }
			
		} catch(SQLException sqle){
			System.out.println("Assignment");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
		
	}
	
	public static void insertAssignmentFinal(Assignment assignment, int courseID) {
		String number = assignment.getNumber();
		String assignedDate = assignment.getAssignedDate();
		String dueDate = assignment.getDueDate();
		String title = assignment.getTitle();
		String url = assignment.getUrl();
		String gradePercentage = assignment.getGradePercentage();
		File[] files = assignment.getFiles();
		File[] gradingCriteriaFiles = assignment.getGradingCriteriaFiles();
		File[] solutionFiles = assignment.getSolutionFiles();
		Deliverable[] deliverables = assignment.getDeliverables();
		connect();
		try {
			String query = "INSERT INTO ASSIGNMENT (number, assignedDate, dueDate, title, url, gradePercentage, courseID) VALUES (?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, assignedDate);
			ps.setString(3, dueDate);
			ps.setString(4, title);
			ps.setString(5, url);
			ps.setString(6, gradePercentage);
			ps.setInt(7, courseID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int assignmentID = rs.getInt(1);
				if(files != null) {
					for(int i = 0; i < files.length; i++) {
						File file = files[i];
						insertFileAssignment(file, assignmentID);
					}
				}
				if(gradingCriteriaFiles != null) {
					for(int i = 0; i < gradingCriteriaFiles.length; i++) {
						File gradingCriteriaFile = gradingCriteriaFiles[i];
						insertGradingCriteriaFileAssignment(gradingCriteriaFile, assignmentID);
					}
				}
				if(solutionFiles != null) {
					for(int i = 0; i < solutionFiles.length; i++) {
						File solutionFile = solutionFiles[i];
						insertSolutionFileAssignment(solutionFile, assignmentID);
					}
				}
				if(deliverables != null) {
					for(int i = 0; i < deliverables.length; i++) {
						Deliverable deliverable = deliverables[i];
						insertDeliverable(deliverable, assignmentID);
					}
				}
            }
			
		} catch(SQLException sqle){
			System.out.println("Assignment");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
		
	}
	
	public static void insertFileAssignment(File file, int assignmentID) {
		String number = file.getNumber();
		String title = file.getTitle();
		String url = file.getUrl();
		String type = "assignment";
		connect();
		try {
			String query = "INSERT INTO FILE (number, title, url, type, assignmentID) VALUES (?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, title);
			ps.setString(3, url);
			ps.setString(4, type);
			ps.setInt(5, assignmentID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
		} catch(SQLException sqle){
			System.out.println("File Assignment");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertGradingCriteriaFileAssignment(File gradingCriteriaFile, int assignmentID) {
		String number = gradingCriteriaFile.getNumber();
		String title = gradingCriteriaFile.getTitle();
		String url = gradingCriteriaFile.getUrl();
		String type = "grading criteria assignment";
		connect();
		try {
			String query = "INSERT INTO FILE (number, title, url, type, assignmentID) VALUES (?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, title);
			ps.setString(3, url);
			ps.setString(4, type);
			ps.setInt(5, assignmentID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
		} catch(SQLException sqle){
			System.out.println("Grading Criteria File Assignment");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertSolutionFileAssignment(File solutionFile, int assignmentID) {
		String number = solutionFile.getNumber();
		String title = solutionFile.getTitle();
		String url = solutionFile.getUrl();
		String type = "solution assignment";
		connect();
		try {
			String query = "INSERT INTO FILE (number, title, url, type, assignmentID) VALUES (?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, title);
			ps.setString(3, url);
			ps.setString(4, type);
			ps.setInt(5, assignmentID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
		
			
		} catch(SQLException sqle){
			System.out.println("Solution File Assignment");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}

	public static void insertDeliverable(Deliverable deliverable, int assignmentID) {
		String number = deliverable.getNumber();
		String dueDate = deliverable.getDueDate();
		String title = deliverable.getTitle();
		String gradePercentage = deliverable.getGradePercentage();
		File[] files = deliverable.getFiles();
		
		SimpleDateFormat format1 = new SimpleDateFormat("m-d-yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-m-d");
		Date tempDueDate;
		Date formattedDueDate = null;
		try {
			tempDueDate = new Date(format1.parse(dueDate).getTime());
			String formattedDueDateString = format2.format(tempDueDate);
			formattedDueDate = Date.valueOf(formattedDueDateString);
			
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		int index = gradePercentage.indexOf("%");
		String formattedGradePercentageString = gradePercentage.substring(0, index);
		double formattedgradePercentage = Double.parseDouble(formattedGradePercentageString);
		
		connect();
		try {
			String query = "INSERT INTO DELIVERABLE (number, dueDate, title, gradePercentage, assignmentID, formattedDueDate, formattedGradePercentage) VALUES (?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, dueDate);
			ps.setString(3, title);
			ps.setString(4, gradePercentage);
			ps.setInt(5, assignmentID);
			ps.setDate(6, formattedDueDate);
			ps.setDouble(7, formattedgradePercentage);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int deliverableID = rs.getInt(1);
				if(files != null) {
					for(int i = 0; i < files.length; i++) {
						File file = files[i];
						insertFileDeliverable(file, deliverableID);
					}
				} 
            }
			
		} catch(SQLException sqle){
			System.out.println("Deliverable");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertFileDeliverable(File file, int deliverableID) {
		String number = file.getNumber();
		String title = file.getTitle();
		String url = file.getUrl();
		String type = "deliverable";
		connect();
		try {
			String query = "INSERT INTO FILE (number, title, url, type, deliverableID) VALUES (?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, title);
			ps.setString(3, url);
			ps.setString(4, type);
			ps.setInt(5, deliverableID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
		} catch(SQLException sqle){
			System.out.println("File Deliverable");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertExam(Exam exam, int courseID) {
		String semester = exam.getSemester();
		String year = exam.getYear();
		Test[] tests = exam.getTests();
		connect();
		try {
			String query = "INSERT INTO EXAM (semester, year, courseID) VALUES (?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, semester);
			ps.setString(2, year);
			ps.setInt(3, courseID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int examID = rs.getInt(1);
				if(tests != null) {
					for(int i = 0; i < tests.length; i++) {
						Test test = tests[i];
						insertTest(test, examID);
					}
				}
            }
			
		} catch(SQLException sqle){
			System.out.println("Exam");
			sqle.printStackTrace();
		}
		finally{
			close();
		}		
	}
	
	public static void insertTest(Test test, int examID) {
		String title = test.getTitle();
		File[] files = test.getFiles();
		connect();
		try {
			String query = "INSERT INTO TEST (title, examID) VALUES (?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, title);
			ps.setInt(2, examID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs != null && rs.next()) {
				int testID = rs.getInt(1);
				if(files != null) {
					for(int i = 0; i < files.length; i++) {
						File file = files[i];
						insertFileTest(file, testID);
					}
				}
            }
			
		} catch(SQLException sqle){
			System.out.println("Test");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	public static void insertFileTest(File file, int testID) {
		String number = file.getNumber();
		String title = file.getTitle();
		String url = file.getUrl();
		String type = "test";
		connect();
		try {
			String query = "INSERT INTO FILE (number, title, url, type, testID) VALUES (?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, number);
			ps.setString(2, title);
			ps.setString(3, url);
			ps.setString(4, type);
			ps.setInt(5, testID);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
		} catch(SQLException sqle){
			System.out.println("File Test");
			sqle.printStackTrace();
		}
		finally{
			close();
		}
	}

}
