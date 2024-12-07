<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head data-genuitec="wc1-8-35">
    <title data-genuitec="wc1-8-36">学生管理</title>
    <style data-genuitec="wc1-8-37">
    td {
        white-space: nowrap;
        vertical-align: middle; /* 垂直居中 */
        text-align: left; /* 水平靠左 */
    }
</style>
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"13645",secure:"13646"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>
<body bgcolor="BBDAF2" data-genuitec="wc1-8-34" data-genuitec-lp-enabled="true" data-genuitec-file-id="wc1-8" data-genuitec-path="/xscj/WebRoot/studentManage.jsp">
    
    <s:set name="student" value="#request.student"/>
    
    
    <s:form name="frm" method="post" enctype="multipart/form-data">

        
        <table data-genuitec="wc1-8-39">
            <tr data-genuitec="wc1-8-40">
                <td data-genuitec="wc1-8-41">
                   <table data-genuitec="wc1-8-42">
                        <tr data-genuitec="wc1-8-43">
                            <td style="white-space: nowrap;" data-genuitec="wc1-8-44">姓名：</td>
                           
                            <td data-genuitec="wc1-8-45">
                            	<input type="text" name="xm" value="<s:property value="#student.xm"/>" data-genuitec="wc1-8-46"/>
                            </td>
                     
                        </tr>
 						<tr style="display: flex; align-items: center;" data-genuitec="wc1-8-47">
						
						    <td data-genuitec="wc1-8-48">
						        <s:radio name="student.xb" list="{'男','女'}" label="性别" value="%{student.xb}" />
						    </td>
                        <tr data-genuitec="wc1-8-49">
                        <td style="white-space: nowrap;" data-genuitec="wc1-8-50">出生年月：</td>
                            <td data-genuitec="wc1-8-51">
                                <input type="date" name="student.cssj" value="<s:date name='#student.cssj' format='yyyy-MM-dd'/>" data-genuitec="wc1-8-52"/>
                            </td>             
                            
                        </tr>
                        <tr data-genuitec="wc1-8-53">
                                <s:file name="photo" accept="image/*" label="照片" onchange="document.all['image'].src=this.value;"/>
                        </tr>
                        <tr data-genuitec="wc1-8-54">
                            
							 <td data-genuitec="wc1-8-55">
							    <img src="<s:url action='getImage'><s:param name='xm'><s:property value='xm'/></s:param></s:url>" 
							    style="max-width: 150%; max-height: 500px; object-fit: contain;" data-genuitec="wc1-8-56"/>
							</td>
                        </tr>
                        <tr data-genuitec="wc1-8-57">
                            <td data-genuitec="wc1-8-58"></td>
                            <td data-genuitec="wc1-8-59">
                                <input name="btn1" type="button" value="录入" onclick="add()" data-genuitec="wc1-8-60">
                                <input name="btn2" type="button" value="删除" onclick="del()" data-genuitec="wc1-8-61">
                                <input name="btn3" type="button" value="更新" onclick="upd()" data-genuitec="wc1-8-62">
                                <input name="btn4" type="button" value="查询" onclick="que()" data-genuitec="wc1-8-63">
                            </td>
                        </tr>
                    </table>
                </td>
                <td data-genuitec="wc1-8-64">
                
                    
                    <table data-genuitec="wc1-8-65">
                        <tr data-genuitec="wc1-8-66">
                            <td data-genuitec="wc1-8-67">已修课程：</td>
                            <td data-genuitec="wc1-8-68">
                                <input type="text" name="student.kcs" value="<s:property value="#student.kcs"/>"  disabled data-genuitec="wc1-8-69"/>
                            </td>
                        </tr>
                        <tr data-genuitec="wc1-8-70">
                            <td align="left" data-genuitec="wc1-8-71">
                                <table border="1" data-genuitec="wc1-8-72">
                                    <tr bgcolor="#CCCCC0" data-genuitec="wc1-8-73">
                                        <td data-genuitec="wc1-8-74">课程名称</td>
                                        <td align="center" data-genuitec="wc1-8-75">成绩</td>
                                    </tr>
                                    
                                    <s:iterator value="#request.scoreList" id="sco">
                                        <tr data-genuitec="wc1-8-77">
                                            <td data-genuitec="wc1-8-78"><s:property value="#sco.kcm"/>$nbsp;</td>
                                            <td align="center" data-genuitec="wc1-8-79"><s:property value="#sco.cj"/></td>
                                        </tr>
                                    </s:iterator>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>

        
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
