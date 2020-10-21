<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${subject} | Airinfo Email</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <style type="text/css">
        #wrapper {
            width: 80vw;
            margin-left: 10vw;
            height: 100vh;
            position: absolute;
        }
        #banner {
            width: 100%;
            position: relative;
        }
    </style>
</head>
<body>
<div id="wrapper">
    <img id="banner" alt="banner" src="http://localhost:8080/images/banner.jpg" />

<p>Dear ${fullname}</p>
<#include contentTemplate>

</div>
</body>
</html>