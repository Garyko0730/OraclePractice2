package org.easybooks.xscj.jdbc;

import java.sql.*;
import org.easybooks.xscj.vo.Student;

public class StudentJdbc {
    private PreparedStatement psmt = null;
    private ResultSet rs = null;


    

    /**
     * 检查并创建所有表
     */
    public static void checkAndCreateTables() {
        try (Connection conn = OrclConn.conns) {
            createTableXS(conn);
            createTableKC(conn);
            createTableCJ(conn);
        } catch (SQLException e) {
            System.out.println("数据库连接失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTableXS(Connection conn) {
        String createXS = 
            "CREATE TABLE XS (" +
            "    XM VARCHAR2(50) PRIMARY KEY,  " +  // 姓名
            "    XB VARCHAR2(2),               " +  // 性别
            "    CSSJ DATE,                    " +  // 出生日期
            "    KCS NUMBER,                   " +  // 课程数
            "    BZ VARCHAR2(255),             " +  // 备注
            "    ZP BLOB                       " +  // 照片
            ")";
        createTable(conn, "XS", createXS);
    }

    private static void createTableKC(Connection conn) {
        String createKC = 
            "CREATE TABLE KC (" +
            "    KCM VARCHAR2(50) PRIMARY KEY, " +  // 课程名
            "    XS NUMBER,                    " +  // 学时
            "    XF NUMBER                     " +  // 学分
            ")";
        createTable(conn, "KC", createKC);
    }

    private static void createTableCJ(Connection conn) {
        String createCJ = 
            "CREATE TABLE CJ (" +
            "    XM VARCHAR2(50) NOT NULL,     " +  // 姓名
            "    KCM VARCHAR2(50) NOT NULL,    " +  // 课程名
            "    CJ NUMBER,                    " +  // 成绩
            "    CONSTRAINT PK_CJ PRIMARY KEY (XM, KCM),  " +  // 联合主键
            "    CONSTRAINT FK_CJ_STUDENT FOREIGN KEY (XM) REFERENCES XS(XM), " +
            "    CONSTRAINT FK_CJ_COURSE FOREIGN KEY (KCM) REFERENCES KC(KCM) " +
            ")";
        createTable(conn, "CJ", createCJ);
    }


    private static void createTable(Connection conn, String tableName, String createSql) {
        String checkTableSql = "SELECT COUNT(*) FROM user_tables WHERE UPPER(table_name) = ?";
        try (PreparedStatement psmt = conn.prepareStatement(checkTableSql)) {
            psmt.setString(1, tableName.toUpperCase());
            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement createStmt = conn.prepareStatement(createSql)) {
                        createStmt.executeUpdate();
                        System.out.println("表 " + tableName + " 已创建。");
                    }
                } else {
                    System.out.println("表 " + tableName + " 已存在，无需创建。");
                }
            }
        } catch (SQLException e) {
            System.out.println("创建表 " + tableName + " 时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }



    // 录入学生
    public Student addStudent(Student student) {
        String sql = "INSERT INTO XS(XM, XB, CSSJ, KCS, BZ, ZP) VALUES(?,?,?,?,?,?)";
        try {
            psmt = OrclConn.conns.prepareStatement(sql);

            // 设置学生姓名
            psmt.setString(1, student.getXm()); // 姓名

            // 设置性别
            psmt.setString(2, student.getXb()); // 性别

            // 处理日期字段
            if (student.getCssj() != null) {
                // 解析日期并插入到数据库
                System.out.println("日期值：" + student.getCssj());
                java.util.Date parsedDate = student.getCssj(); // 如果已是 Date 对象，无需解析
                psmt.setDate(3, new java.sql.Date(parsedDate.getTime()));
            } else {
                System.out.println("日期值为空，插入 NULL");
                psmt.setNull(3, Types.DATE);
            }

            // 设置已修课程数
            psmt.setInt(4, student.getKcs()); // 已修课程数

            // 设置备注
            psmt.setString(5, student.getBz()); // 备注

            // 设置照片
            if (student.getZp() != null) {
                psmt.setBytes(6, student.getZp()); // 照片
            } else {
                psmt.setNull(6, Types.BLOB); // 如果照片为空，设置为 NULL
            }

            // 执行插入
            psmt.execute();
            System.out.println("学生信息插入成功：" + student.getXm());
        } catch (Exception e) {
            System.err.println("插入学生信息时发生错误！");
            e.printStackTrace();
        } finally {
            // 关闭 PreparedStatement
            try {
                if (psmt != null) {
                    psmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return student;
    }



//    // 获取某个学生的照片
//    public byte[] getStudentZp(Student student) {
//        String sql = "select ZP from XS where XM ='" + student.getXm() + "'";
//        try {
//            psmt = OrclConn.conns.prepareStatement(sql);
//            rs = psmt.executeQuery();
//            if (rs.next()) {
//                student.setZp(rs.getBytes("ZP"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return student.getZp();
//    }
    
    // 获取某个学生的照片
    public byte[] getStudentZp(Student student) {
        String sql = "SELECT ZP FROM XS WHERE XM = ?";
        byte[] photo = null;
        try {
            psmt = OrclConn.conns.prepareStatement(sql); // 使用 PreparedStatement 防止 SQL 注入
            psmt.setString(1, student.getXm()); // 设置参数
            rs = psmt.executeQuery();
            if (rs.next()) {
                photo = rs.getBytes("ZP"); // 从结果集中获取照片
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 确保资源关闭，避免内存泄漏
            try {
                if (rs != null) rs.close();
                if (psmt != null) psmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return photo; // 返回照片数据
    }


    // 删除学生
    public Student delStudent(Student student) {
        String sql = "delete from XS where XM ='" + student.getXm() + "'";
        try {
            psmt = OrclConn.conns.prepareStatement(sql);
            psmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return student;
    }

    // 查询学生
    public Student showStudent(Student student) {
        String sql = "select * from XS where XM ='" + student.getXm() + "'";
        try {
            psmt = OrclConn.conns.prepareStatement(sql);
            rs = psmt.executeQuery();
            if (rs.next()) {
                student.setXb(rs.getString("XB"));
                student.setCssj(rs.getDate("CSSJ"));
                student.setKcs(rs.getInt("KCS"));
                student.setZp(rs.getBytes("ZP"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return student;
    }

    // 更新学生信息
    public Student updateStudent(Student student) {
        String sql = "update XS set XM=?, XB=?, CSSJ=?, KCS=?, BZ=?, ZP=? where XM = ?";
        try {
            psmt = OrclConn.conns.prepareStatement(sql);
            psmt.setString(1, student.getXm());
            psmt.setString(2, student.getXb());
            psmt.setTimestamp(3, new Timestamp(student.getCssj().getTime()));
            psmt.setInt(4, student.getKcs());
            psmt.setString(5, student.getBz());
            psmt.setBytes(6, student.getZp());
            psmt.setString(7, student.getXm()); // 使用学生的姓名作为条件
            psmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return student;
    }
}
