<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>File Upload</title>
		<script>
			function validate() {
				if(document.getElementById('username').value == "" || document.getElementById('password').value == "" || document.getElementById('ipaddress').value == "") {
					return false;
				}
				else {
					return true;
				}
			}
		</script>
	</head>
	<body>
		<form method="post" action="Submit" enctype="multipart/form-data" onsubmit="return validate();">
			<br />
			Enter MySQL username <input type="text" name="username" id="username" /> <br />
			Enter MySQL password (can be blank) <input type="text" name="password" id="password" /> <br />
			Enter MySQL ipaddress <input type="text" name="ipaddress" id="ipaddress" /> <br />
			Choose whether to use SSL <input type="checkbox" name="ssl" value="ssl"> <br />
			<br />
			If you would like, you can upload a json file containing data you wish to insert into the database <br />
			<input type="file" name="file" id="file" accept=".json" /><br />
			<br />
			<input type="radio" name="design" value="design1" checked> Design 1 <br />
  			<input type="radio" name="design" value="design2"> Design 2 <br />
  			<br />
  			<input type="submit" value="Connect" /> <br /> 
		</form>
	</body>
</html>