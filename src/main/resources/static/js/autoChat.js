function getParam(paramName) { 
    paramValue = "", isFound = !1; 
    if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) { 
        arrSource = unescape(this.location.search).substring(1, this.location.search.length).split("&"), i = 0; 
        while (i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++ 
    } 
    return paramValue == "" && (paramValue = null), paramValue 
}

showFaceName();

function showLoginName(){
	
	var loginname = getParam("loginname");
	
	//$("#loginname").val(loginname);
	
	//var element = document.getElementById("loginname");
	
	//element.innerHTML = "welcome : " + loginname;
	
	var loginname = document.getElementById("loginname");
	
	loginname.innerHTML = loginname;
}

function showFaceName(){
	var faceName = getParam("faceName");
	
//	var faceName = document.getElementById("faceName");
//	
//	faceName.innerHTML = faceName;
	
	$("#faceName").html(faceName);
}