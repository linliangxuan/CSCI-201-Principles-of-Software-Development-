Name: Ronak UpadhyayaUSC ID: 2287766736Email: rdupadhy@usc.eduLecture Session Number: 29909RThe servlet package contains a single servlet class used to catch the contents of the uploaded Json file’s contents into a stream, parsing the data using Gson, storing it in an Education object, populating the database tables, and  dispatching the request to home.jsp  if a file is uploaded. However, if a file is not uploaded, data is retrieved from the database tables.
The SQL package contains the following classes:
i. InsertSQL.java - This class contains functions for populating database tables.
ii. RetrieveSQL.java - This class contains functions for retrieving data from the database tables, which are later converted to an Education (container) object and stored on the session. The class also contains functions for returning sorted arraylists of Assignment and Deliverable objects. The functions for sorting assignments and deliverables are called in assignmentsForm_validate.jsp and finalProjectForm_validate.jsp respectively.

Note: The Main class in the SQL package is redundant. It was used for testing purposes only.

The sql file rdupadhy_a5.sql is included both in the hw5 directory and the zip file. The database is named rdupadhy_201_site, as directed.
The program must be started by running index.jsp on the Tomcat server.