<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>File Upload</title>
		<script>
			function validate() {
				if(document.getElementById('file').value == "") {
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
			Please choose a JSON file <br />
			<input type="file" name="file" id="file" accept=".json" /><br />
			<input type="submit" value="Upload File" /> <br /> 
			<input type="radio" name="design" value="design1" checked> Design 1 <br />
  			<input type="radio" name="design" value="design2"> Design 2 
		</form>
	</body>
</html>