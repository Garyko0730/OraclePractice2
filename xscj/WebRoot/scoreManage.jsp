<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <title>成绩管理</title>
</head>
<body bbgcolor="BBDAF2">
  <s:set name="student" value="#request.student"/>
  <s:form name="frm" method="post" enctype="multipart/form-data">
    <Table>
      <tr>
        <td>
          课程名:
          <!--- 以下JS代码是为了保证在页面刷新后，下拉列表中仍然保持之前的选中项 --->
          <script type="text/javascript">
            function setCookie(name, value) {
              var exp = new Date();
              exp.setTime(exp.getTime() + 24 * 60 * 60 * 1000);
              document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
            }

            function getCookie(name) {
              var regExp = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
              var arr = document.cookie.match(regExp);
              if (arr == null) {
                return null;
              }
              return unescape(arr[2]);
            }
          </script>
          <select name="score.km" id="select_1" onclick="setCookie('select_1', this.selectedIndex)">
            <option selected="selected">请选择</option>
            <s:iterator id="cou" value="#request.courseList">
            	<option value="<s:property value="#cou.km"/>">
            			<s:property value="#cou.km"/>
            		</option>
			</s:iterator>
		</select>
		<script type="text/javascript">
  			var selectedIndex = getCookie("select_1");
  			if (selectedIndex!= null) {
   				document.getElementById("select_1").selectedIndex = selectedIndex;
  			}
		</script>
		<input name="btn1" type="button" value="查询" onclick="que()"/>
	</td>
</tr>
<tr>
  	<td>
    	姓&nbsp;&nbsp;&nbsp;&nbsp;名：
    	<input type="text" name="xm" size="19">
  	</td>
</tr>
	<td>
  		成&nbsp;&nbsp;&nbsp;&nbsp;绩：
  		<input type="text" name="cj" size="19">
  		&nbsp;<input name="btn2" type="button" value="录入" onclick="add()">
  		&nbsp;<input name="btn3" type="button" value="删除" onclick="del()">
	</td>
</tr>
<tr>
	<td align="left" width="400">
  		<table border="1" cellpadding="0" cellspacing="0" width="310">
    		<tr bgcolor="#CCCCCC">
     	 		<td align="center">姓名</td>
      			<td align="center">成绩</td>
    		</tr>
    		<s:iterator value="#request.kcscoreList" id="kcsco">
      		<tr>
        		<td align="center"><s:property value="#kcsco.xm"/></td>
        		<td align="center"><s:property value="#kcsco.cj"/></td>
      		</tr>
    		</s:iterator>
  		</table>
  		</td>
	</tr>
</table>
<s:property value="msg"/>
</s:form>
</body>
</html>
<script type="text/javascript">
  function que() {
    // que()方法查询某门课的成绩
	  document.frm.action = "queSco.action";
	  document.frm.submit();

	  function add() {
	    document.frm.action = "addSco.action";
	    document.frm.submit();
	  }

	  function del() {
	    document.frm.action = "delSco.action";
	    document.frm.submit();
	  }
  }
</script>