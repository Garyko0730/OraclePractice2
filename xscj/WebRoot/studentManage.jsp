<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>学生管理</title>
    <style>
    td {
        white-space: nowrap;
        vertical-align: middle; /* 垂直居中 */
        text-align: left; /* 水平靠左 */
    }
</style>
</head>
<body bgcolor="BBDAF2">
    <!-- 设置学生对象 -->
    <s:set name="student" value="#request.student"/>
    
    <!-- 表单开始 -->
    <s:form name="frm" method="post" enctype="multipart/form-data">

        <!-- 学生信息部分 -->
        <table>
            <tr>
                <td>
                   <table>
                        <tr>
                            <td style="white-space: nowrap;">姓名：</td>
                           
                            <td>
                            	<input type="text" name="xm" value="<s:property value="#student.xm"/>"/>
                            </td>
                     
                        </tr>
 						<tr style="display: flex; align-items: center;">
						
						    <td>
						        <s:radio name="student.xb" list="{'男','女'}" label="性别" value="%{student.xb}" />
						    </td>
                        <tr>
                        <td style="white-space: nowrap;">出生年月：</td>
                            <td>
                                <input type="date" name="student.cssj" value="<s:date name='#student.cssj' format='yyyy-MM-dd'/>"/>
                            </td>             
                            
                        </tr>
                        <tr>
                                <s:file name="photo" accept="image/*" label="照片" onchange="document.all['image'].src=this.value;"/>
                        </tr>
                        <tr>
                            
							 <td>
							    <img src="<s:url action='getImage'><s:param name='xm'><s:property value='xm'/></s:param></s:url>" 
							    style="max-width: 150%; max-height: 500px; object-fit: contain;" />
							</td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <input name="btn1" type="button" value="录入" onclick="add()">
                                <input name="btn2" type="button" value="删除" onclick="del()">
                                <input name="btn3" type="button" value="更新" onclick="upd()">
                                <input name="btn4" type="button" value="查询" onclick="que()">
                            </td>
                        </tr>
                    </table>
                </td>
                <td>
                
                    <!-- 学生成绩信息部分 -->
                    <table>
                        <tr>
                            <td>已修课程：</td>
                            <td>
                                <input type="text" name="student.kcs" value="<s:property value="#student.kcs"/>"  disabled/>
                            </td>
                        </tr>
                        <tr>
                            <td align="left">
                                <table border="1">
                                    <tr bgcolor="#CCCCC0">
                                        <td>课程名称</td>
                                        <td align="center">成绩</td>
                                    </tr>
                                    <!-- 迭代成绩列表 -->
                                    <s:iterator value="#request.scoreList" id="sco">
                                        <tr>
                                            <td><s:property value="#sco.kcm"/>$nbsp;</td>
                                            <td align="center"><s:property value="#sco.cj"/></td>
                                        </tr>
                                    </s:iterator>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>

        <!-- 显示信息 -->
        <s:property value="msg"/>
    </s:form>
</body>
</html>

 <!-- JavaScript 部分 -->
    <script type="text/javascript">
        function add() {
            document.frm.action = "addStu";  // 设置Action路径
            document.frm.submit();  // 提交表单
        }

        function del() {
            document.frm.action = "delStu.action";  // 设置Action路径
            document.frm.submit();  // 提交表单
        }

        function upd() {
            document.frm.action = "updStu.action";  // 设置Action路径
            document.frm.submit();  // 提交表单
        }

        function que() {
            document.frm.action = "queStu.action";  // 设置Action路径
            document.frm.submit();  // 提交表单
        }
      
    </script>
