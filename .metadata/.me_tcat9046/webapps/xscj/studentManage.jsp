<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>学生管理</title>
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"13645",secure:"13646"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>
<body bgcolor="BBDAF2" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-1" data-genuitec-path="/xscj/WebRoot/studentManage.jsp">
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
                            <td>姓名：</td>
                            <td><input type="text" name="xm" value="<s:property value="#student.xm"/>"/></td>
                        </tr>
                        <tr>
                           
                            <td>
                                <s:radio list="{'男','女'}" label="性别" name="student.xb" value="#student.xb"/>
                            </td>
                        </tr>
                        <tr>
                            <td>出生年月：</td>
                            <td>
                            	<input type="text" name="student.cssj" value="<s:date name='#student.cssj' format='yyyy-MM-dd'/>"/>
                            </td>
                        </tr>
                        <tr>
                            
                            <s:file name="photo" accept="image/*" label="照片" onchange="document.getElementById('image').src=this.value;"/>
                        </tr>
                        <tr>
                            <td><img id="image" src="getImage.action?xm=<s:property value="#student.xm"/>" width="90" height="120" /></td>
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
                            <td>已修课程<input type="text" name="student.kcs" value="<s:property value="#student.kcs"/>" disabled/></td>
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

<script type="text/javascript">
function add() {
    document.frm.action = "addStu.action";
    document.frm.submit();
}

function del() {
    document.frm.action = "delStu.action";
    document.frm.submit();
}

function upd() {
    document.frm.action = "updStu.action";
    document.frm.submit();
}

function que() {
    document.frm.action = "queStu.action";
    document.frm.submit();
}
</script>
