package org.easybooks.xscj.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.easybooks.xscj.vo.*;

public class ScoreJdbc {
    private PreparedStatement psmt = null;
    private ResultSet rs = null;
 // 初始化课程数据
    public void initializeCourses() {
        String checkDataSql = "SELECT COUNT(*) FROM KC";
        String insertDataSql = "INSERT INTO KC (KCM, XS, XF) VALUES (?, ?, ?)";
        try (PreparedStatement checkStmt = OrclConn.conns.prepareStatement(checkDataSql);
             PreparedStatement insertStmt = OrclConn.conns.prepareStatement(insertDataSql)) {
            
            // 检查是否已存在课程数据
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("课程数据已存在，无需初始化。");
                    return;
                }
            }

            // 课程初始化数据
            Object[][] courses = {
                {"高等数学 I", 90, 5},
                {"线性代数", 54, 3},
                {"数理逻辑与集合论", 36, 2},
                {"高等数学 II", 90, 5},
                {"大学物理", 54, 3},
                {"高级语言程序设计实验", 36, 1},
                {"高级语言程序设计", 54, 3},
                {"数据结构", 72, 4},
                {"大学物理实验", 36, 1},
                {"数字电子技术实验", 36, 1},
                {"代数结构与图论", 54, 3},
                {"微机原理", 54, 3},
                {"数字电子技术", 54, 3},
                {"概率论与数理统计", 54, 3},
                {"算法与设计", 54, 3},
                {"操作系统原理", 54, 3},
                {"操作系统原理实验", 36, 1}
            };

            // 插入课程数据
            for (Object[] course : courses) {
                insertStmt.setString(1, (String) course[0]);
                insertStmt.setInt(2, (int) course[1]);
                insertStmt.setInt(3, (int) course[2]);
                insertStmt.executeUpdate();
            }

            System.out.println("课程数据初始化完成。");

        } catch (SQLException e) {
            System.err.println("初始化课程数据时发生错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 初始化数据库表
    public void checkAndCreateTables() {
        try {
            Connection conn = OrclConn.conns;

            // 创建 XS 表
            createTable(conn, "XS",
                "CREATE TABLE XS (" +
                "    XM VARCHAR2(50) PRIMARY KEY, " + // 学生姓名
                "    XB VARCHAR2(10), " +            // 性别
                "    CSSJ DATE, " +                  // 出生日期
                "    KCS NUMBER, " +                 // 已修课程数
                "    BZ VARCHAR2(255), " +           // 备注
                "    ZP BLOB " +                     // 照片
                ")"
            );

            // 创建 KC 表
            createTable(conn, "KC",
                "CREATE TABLE KC (" +
                "    KCM VARCHAR2(50) PRIMARY KEY, " + // 课程名
                "    XS NUMBER, " +                    // 学时
                "    XF NUMBER " +                     // 学分
                ")"
            );

            // 创建 CJ 表
            createTable(conn, "CJ",
                "CREATE TABLE CJ (" +
                "    XM VARCHAR2(50) NOT NULL, " +      // 学生姓名
                "    KCM VARCHAR2(50) NOT NULL, " +     // 课程名
                "    CJ NUMBER, " +                     // 成绩
                "    CONSTRAINT PK_CJ PRIMARY KEY (XM, KCM), " + // 联合主键
                "    CONSTRAINT FK_CJ_STUDENT FOREIGN KEY (XM) REFERENCES XS(XM), " +
                "    CONSTRAINT FK_CJ_COURSE FOREIGN KEY (KCM) REFERENCES KC(KCM) " +
                ")"
            );

         // 创建存储过程 CJ_PROC
            createProcedure(conn, "CJ_PROC",
                "CREATE OR REPLACE PROCEDURE CJ_PROC (p_xm IN VARCHAR2) AS " +
                "BEGIN " +
                "    -- 尝试删除视图，如果视图不存在，则忽略异常 " +
                "    BEGIN " +
                "        EXECUTE IMMEDIATE 'DROP VIEW XMC_VIEW'; " +
                "    EXCEPTION " +
                "        WHEN OTHERS THEN " +
                "            IF SQLCODE != -942 THEN " + // ORA-00942: 表或视图不存在
                "                RAISE; " +
                "            END IF; " +
                "    END; " +

                "    -- 创建新的视图 " +
                "    EXECUTE IMMEDIATE 'CREATE VIEW XMC_VIEW AS " +
                "                      SELECT KCM, CJ FROM CJ WHERE XM = ''' || p_xm || ''''; " +

                "END;"
            );



            System.out.println("数据库表和存储过程检查完成，必要时已创建。");
        } catch (SQLException e) {
            System.err.println("初始化数据库表时发生错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 创建表的通用方法
    private void createTable(Connection conn, String tableName, String createSql) throws SQLException {
        String checkTableSql = "SELECT COUNT(*) FROM user_tables WHERE UPPER(table_name) = ?";
        try (PreparedStatement psmt = conn.prepareStatement(checkTableSql)) {
            psmt.setString(1, tableName.toUpperCase());
            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(createSql);
                        System.out.println("表 " + tableName + " 已创建。");
                    }
                } else {
                    System.out.println("表 " + tableName + " 已存在，无需创建。");
                }
            }
        }
    }

    // 创建存储过程的通用方法
    private void createProcedure(Connection conn, String procedureName, String procedureSql) throws SQLException {
        String checkProcedureSql = 
            "SELECT COUNT(*) FROM user_objects WHERE OBJECT_TYPE = 'PROCEDURE' AND UPPER(OBJECT_NAME) = ?";
        try (PreparedStatement psmt = conn.prepareStatement(checkProcedureSql)) {
            psmt.setString(1, procedureName.toUpperCase());
            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(procedureSql);
                        System.out.println("存储过程 " + procedureName + " 已创建。");
                    }
                } else {
                    System.out.println("存储过程 " + procedureName + " 已存在，无需创建。");
                }
            }
        }
    }

    // 查询某学生的成绩
    public List<Score> showScore(Score score) {
        CallableStatement stmt = null;
        try {
            stmt = OrclConn.conns.prepareCall("{call CJ_PROC(?)}");
            stmt.setString(1, score.getXm());
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1031) { // ORA-01031: 权限不足
            	System.out.println("权限不足，请检查对视图或存储过程的权限！");
                return new ArrayList<>();
            } else {
                e.printStackTrace();
            }
        }

        String sql = "SELECT * FROM XMC_VIEW";
        List<Score> scoreList = new ArrayList<>();
        try {
            psmt = OrclConn.conns.prepareStatement(sql);
            rs = psmt.executeQuery();
            while (rs.next()) {
                Score kcscore = new Score();
                kcscore.setKcm(rs.getString("KCM"));
                kcscore.setCj(rs.getInt("CJ"));
                scoreList.add(kcscore);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scoreList;
    }

    // 查询所有课程
    public List<Course> showCourse() {
        String sql = "SELECT * FROM KC";
        List<Course> courseList = new ArrayList<>();
        try {
            psmt = OrclConn.conns.prepareStatement(sql);
            rs = psmt.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setKcm(rs.getString("KCM"));
                course.setXs(rs.getInt("XS"));
                course.setXf(rs.getInt("XF"));
                courseList.add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }

    // 查询某门课的成绩
    public List<Score> queScore(Score score) {
        String sql = "SELECT * FROM CJ WHERE KCM='" + score.getKcm() + "'";
        List<Score> kcscoreList = new ArrayList<>();
        try {
            psmt = OrclConn.conns.prepareStatement(sql);
            rs = psmt.executeQuery();
            while (rs.next()) {
                Score kcscore = new Score();
                kcscore.setXm(rs.getString("XM"));
                kcscore.setKcm(rs.getString("KCM"));
                kcscore.setCj(rs.getInt("CJ"));
                kcscoreList.add(kcscore);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kcscoreList;
    }

    // 录入成绩
    public Score addScore(Score score) {
        String sql = "INSERT INTO CJ(XM, KCM, CJ) VALUES(?,?,?)";
        try {
            psmt = OrclConn.conns.prepareStatement(sql);
            psmt.setString(1, score.getXm());
            psmt.setString(2, score.getKcm());
            psmt.setInt(3, score.getCj());
            psmt.execute();
        } catch (SQLException e) {
            // 如果是外键约束失败，抛出友好提示
            if (e.getMessage().contains("ORA-02291")) {
                throw new IllegalArgumentException("录入失败：学生或课程不存在，请先录入相关信息！");
            } else {
                e.printStackTrace();
            }
        }
        return score;
    }


    // 删除成绩
    public Score delScore(Score score) {
        String sql = "DELETE FROM CJ WHERE XM='" + score.getXm() + "' AND KCM='" + score.getKcm() + "'";
        try {
            psmt = OrclConn.conns.prepareStatement(sql);
            psmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return score;
    }
}
