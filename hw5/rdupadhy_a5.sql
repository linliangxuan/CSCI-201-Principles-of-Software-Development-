DROP DATABASE if exists rdupadhy_201_site;
CREATE DATABASE rdupadhy_201_site;
USE rdupadhy_201_site;

CREATE TABLE SCHOOL (
	schoolID int(11) primary key not null auto_increment,
	name VARCHAR(50),
	image VARCHAR(200)
);

CREATE TABLE DEPARTMENT (
	departmentID int(11) primary key not null auto_increment,
	longName VARCHAR(50),
	prefix VARCHAR(50),
    
    schoolID int(11),
	FOREIGN KEY fk1(schoolID) REFERENCES school(schoolID)
);

CREATE TABLE COURSE (
	courseID int(11) primary key not null auto_increment,
	number VARCHAR(50),
	term VARCHAR(50),
	year VARCHAR(50),
    title VARCHAR(50),
    units VARCHAR(50),
	
    departmentID int(11),
	FOREIGN KEY fk1(departmentID) REFERENCES department(departmentID)
);

CREATE TABLE STAFFMEMBER (
	staffmemberID int(11) primary key not null auto_increment,
    type VARCHAR(50),
    id VARCHAR(50),
    email VARCHAR(50),
    image VARCHAR(200),
    phone VARCHAR(50),
    office VARCHAR(50),
    
    courseID int(11),
	FOREIGN KEY fk1(courseID) REFERENCES course(courseID)
);

CREATE TABLE NAME (
	nameID int(11) primary key not null auto_increment,
	fname VARCHAR(50),
	lname VARCHAR(50),
    
	staffmemberID int(11),
	FOREIGN KEY fk1(staffmemberID) REFERENCES staffmember(staffmemberID)
);

CREATE TABLE OFFICEHOUR (
	officehourID int(11) primary key not null auto_increment,
	day VARCHAR(50),

	staffmemberID int(11),
	FOREIGN KEY fk1(staffmemberID) REFERENCES staffmember(staffmemberID)
);

CREATE TABLE MEETING (
	meetingID int(11) primary key not null auto_increment,
	type VARCHAR(50),
	section VARCHAR(50),
	room VARCHAR(50),

	courseID int(11),
	FOREIGN KEY fk1(courseID) REFERENCES course(courseID)
);

CREATE TABLE MEETINGPERIOD (
	meetingperiodID int(11) primary key not null auto_increment,
	day VARCHAR(50),
	
    meetingID int(11),
	FOREIGN KEY fk1(meetingID) REFERENCES meeting(meetingID)
);

CREATE TABLE TIME (
	timeID int(11) primary key not null auto_increment,
	start VARCHAR(50),
	end VARCHAR(50),

	officehourID int(11),
	FOREIGN KEY fk1(officehourID) REFERENCES officehour(officehourID),
	meetingperiodID int(11),
	FOREIGN KEY fk2(meetingperiodID) REFERENCES meetingperiod(meetingperiodID)
);

CREATE TABLE ASSISTANT (
	assistantID int(11) primary key not null auto_increment,
    staffMemberID VARCHAR(50),
    
	meetingID int(11),
	FOREIGN KEY fk1(meetingID) REFERENCES meeting(meetingID)
);

CREATE TABLE SYLLABUS (
	syllabusID int(11) primary key not null auto_increment,
	url VARCHAR(50),
    
	courseID int(11),
	FOREIGN KEY fk1(courseID) REFERENCES course(courseID)
);

CREATE TABLE SCHEDULE (
	scheduleID int(11) primary key not null auto_increment,

	courseID int(11),
	FOREIGN KEY fk1(courseID) REFERENCES course(courseID)
);

CREATE TABLE TEXTBOOK (
	textbookID int(11) primary key not null auto_increment,
	number VARCHAR(50),
	author VARCHAR(50),
	title VARCHAR(100),
	publisher VARCHAR(50),
	year VARCHAR(50),
	isbn VARCHAR(50),
    
	scheduleID int(11),
	FOREIGN KEY fk1(scheduleID) REFERENCES schedule(scheduleID)
);

CREATE TABLE WEEK (
	weekID int(11) primary key not null auto_increment,
	week VARCHAR(50),
    
	scheduleID int(11),
	FOREIGN KEY fk1(scheduleID) REFERENCES schedule(scheduleID)
);

CREATE TABLE LAB (
	labID int(11) primary key not null auto_increment,
	title VARCHAR(50),
	number VARCHAR(50),
	url VARCHAR(200),

	weekID int(11),
	FOREIGN KEY fk1(weekID) REFERENCES week(weekID)
);

CREATE TABLE LECTURE (
	lectureID int(11) primary key not null auto_increment,
	number VARCHAR(50),
	date VARCHAR(50),
	day VARCHAR(50),
	
    weekID int(11),
	FOREIGN KEY fk1(weekID) REFERENCES week(weekID)
);

CREATE TABLE TOPIC (
	topicID int(11) primary key not null auto_increment,
	number VARCHAR(50),
	title VARCHAR(50),
	url VARCHAR(200),
    chapter VARCHAR(50),
	
    lectureID int(11),
	FOREIGN KEY fk1(lectureID) REFERENCES lecture(lectureID)
);

CREATE TABLE PROGRAM (
	programID int(11) primary key not null auto_increment,
	
    topicID int(11),
	FOREIGN KEY fk1(topicID) REFERENCES topic(topicID)
);

CREATE TABLE ASSIGNMENT (
	assignmentID int(11) primary key not null auto_increment,
	number VARCHAR(50),
	assignedDate VARCHAR(50),
	dueDate VARCHAR(50),
	title VARCHAR(50),
	url VARCHAR(200),
	gradePercentage VARCHAR(50),
    
    formattedAssignedDate DATE,
    formattedDueDate DATE,
    formattedGradePercentage DOUBLE(10, 2),
	
    courseID int(11),
	FOREIGN KEY fk1(courseID) REFERENCES course(courseID)
);

CREATE TABLE DELIVERABLE (
	deliverableID int(11) primary key not null auto_increment,
    number VARCHAR(50),
	dueDate VARCHAR(50),
	title VARCHAR(50),
	gradePercentage VARCHAR(50),
    
    formattedDueDate DATE,
    formattedGradePercentage DOUBLE(10, 2),
    
    assignmentID int(11),
	FOREIGN KEY fk1(assignmentID) REFERENCES assignment(assignmentID)
);

CREATE TABLE EXAM (
	examID int(11) primary key not null auto_increment,
	semester VARCHAR(50),
	year VARCHAR(50),

	courseID int(11),
	FOREIGN KEY fk1(courseID) REFERENCES course(courseID)
);

CREATE TABLE TEST (
	testID int(11) primary key not null auto_increment,
	title VARCHAR(50),
    
	examID int(11),
	FOREIGN KEY fk1(examID) REFERENCES exam(examID)
);

CREATE TABLE FILE (
	fileID int(11) primary key not null auto_increment,
	number VARCHAR(50),
	title VARCHAR(50),
	url VARCHAR(200),

	type VARCHAR(50) NOT NULL,

	labID int(11),
	FOREIGN KEY fk1(labID) REFERENCES lab(labID),
    programID int(11),
    FOREIGN KEY fk2(programID) REFERENCES program(programID),
    assignmentID int(11),
    FOREIGN KEY fk3(assignmentID) REFERENCES assignment(assignmentID),
    deliverableID int(11),
    FOREIGN KEY fk4(deliverableID) REFERENCES deliverable(deliverableID),
    testID int(11),
    FOREIGN KEY fk5(testID) REFERENCES test(testID)
);


