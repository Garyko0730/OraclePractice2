This is a final project for an Oracle lab course. The project is a student grade management system based on Jakarta EE, utilizing the Tomcat 9.0.96 web server to access an Oracle 12c database.这是一个Oracle实验课程结课项目，本项目是基于Jakarta EE实现学生成绩管理系统，使用Web服务器Tomcat 9.0.96来访问Oracle12c数据库。

确认用户权限授予权限，否则成绩管理页面的查询页面将不能查询成绩信息
确保以下权限已经授予给 SYSTEM：
GRANT CREATE VIEW TO SYSTEM; 
GRANT CREATE PROCEDURE TO SYSTEM; 
GRANT EXECUTE ON CJ_PROC TO SYSTEM; 
GRANT SELECT ON CJ TO SYSTEM; 
