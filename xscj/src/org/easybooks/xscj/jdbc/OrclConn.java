package org.easybooks.xscj.jdbc;
import java.sql.*;
public class OrclConn {
    public static Connection conns;
    static {
        try {
        	// 练习2 Java EE 7/Oracle 12c学生成绩管理系统
        	//**加载并注册Oracle 12c的JDBC驱动**/
        	Class.forName("oracle.jdbc.OracleDriver");
        	//**创建到Oracle 12c的连接**/
        	conns = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XSCJ",
        "SYSTEM", "Manager123");
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}