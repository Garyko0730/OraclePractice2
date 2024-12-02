package org.easybooks.xscj.jdbc;
//**业务逻辑类所在的包**
//**导入所需的类和包**
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.easybooks.xscj.vo.*;

public class ScoreJdbc {
    private PreparedStatement psmt = null;
    private ResultSet rs = null;

    //**查询某学生的成绩**
    public List showScore(Score score) {
        CallableStatement stmt = null;
        try {
            stmt = OrclConn.conns.prepareCall("{call CJ_PROC(?)}");
            stmt.setString(1, score.getXm());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql = "select * from XMC_VIEW";
        
        List scoreList = new ArrayList();
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
    //**查询所有课程**/
    public List showCourse() {
        String sql = "select * from KC";
        List courseList = new ArrayList<>();
        try {
            psmt = OrclConn.conns.prepareStatement(sql);
            rs = psmt.executeQuery();
            // 读出所有课程名放入courseList中
            while (rs.next()) {
                Course course = new Course();
                course.setKcm(rs.getString("KCM"));
                courseList.add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }
    
    //**查询某门课的成绩**
    public List queScore(Score score) {
        String sql = "select * from CJ where KCM='" + score.getKcm() + "'";
        List kcscoreList = new ArrayList<>();
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
    
  //**录入成绩**
    public Score addScore(Score score) {
        String sql = "insert into CJ(XM, KCM, CJ) values(?,?,?)";
        try {
            psmt = OrclConn.conns.prepareStatement(sql);
            psmt.setString(1, score.getXm());
            psmt.setString(2, score.getKcm());
            psmt.setInt(3, score.getCj());
            psmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return score;
    }
    
  //**删除成绩**
    public Score delScore(Score score) {
        String sql = "delete from CJ where XM='" + score.getXm() + "' and KCM='" + score.getKcm() + "'";
        try {
            psmt = OrclConn.conns.prepareStatement(sql);
            psmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return score;
    }
}