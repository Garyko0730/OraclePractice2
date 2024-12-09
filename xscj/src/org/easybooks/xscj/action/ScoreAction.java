package org.easybooks.xscj.action;
//**Action所在的包**
//**导入所需的类和包**
import java.util.*;
import java.sql.*;
import org.easybooks.xscj.jdbc.*;
import org.easybooks.xscj.vo.*;
import com.opensymphony.xwork2.*;

public class ScoreAction extends ActionSupport {
    private String xm; // 姓名
    private int cj; // 成绩
    private String msg; // 页面操作的消息提示文字
    private Score score; // 成绩对象

    // **showAll()方法实现预加载信息（课程名）**
    public String showAll() {
    	ScoreJdbc scoreJdbc = new ScoreJdbc();
        scoreJdbc.checkAndCreateTables(); // 检查并创建表
        scoreJdbc.initializeCourses();    // 初始化课程数据
        
        Map<String, Object> request = (Map<String, Object>) ActionContext.getContext().get("request");
        List<Course> courses = allCou();
        if (courses != null) {
            System.out.println("Loaded courses: " + courses.size());
        }
        request.put("courseList", courses);
        return "success"; // 与 struts.xml 对应
    }


    // **queSco()方法实现查询某门课的成绩**
    public String queSco() {
        Map<String, Object> request = (Map<String, Object>) ActionContext.getContext().get("request");
        request.put("kcscoreList", curSco());
        return "success"; // 与 struts.xml 对应
    }

 // **addSco()方法实现录入成绩**
    public String addSco() {
        try {
            // 检查成绩记录是否已经存在
            String checkScoreSql = "SELECT * FROM CJ WHERE XM = ? AND KCM = ?";
            try (PreparedStatement stmt = OrclConn.conns.prepareStatement(checkScoreSql)) {
                stmt.setString(1, getXm());
                stmt.setString(2, score.getKcm());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    setMsg("该成绩记录已经存在！");
                    refreshData();
                    return "input"; // 返回当前页面
                }
            }

            // 检查学生是否存在
            String checkStudentSql = "SELECT COUNT(*) FROM XS WHERE XM = ?";
            try (PreparedStatement stmt = OrclConn.conns.prepareStatement(checkStudentSql)) {
                stmt.setString(1, getXm());
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    setMsg("学生 " + getXm() + " 不存在，请先录入学生信息！");
                    refreshData();
                    return "input"; // 返回当前页面
                }
            }

            // 插入成绩记录
            ScoreJdbc scoreJ = new ScoreJdbc();
            Score sco = new Score();
            sco.setXm(getXm());
            sco.setKcm(score.getKcm());
            sco.setCj(getCj());
            if (scoreJ.addScore(sco) != null) {
                setMsg("录入成功！");
            } else {
                setMsg("录入失败，请确保输入正确的信息！");
            }

            // 刷新数据
            refreshData();
            return "success"; // 返回成功页面
        } catch (Exception e) {
            e.printStackTrace();
            setMsg("系统发生错误，请稍后再试！");
            refreshData();
            return "input"; // 返回当前页面，并提示错误
        }
    }

    // 用于刷新课程和成绩数据的方法
    private void refreshData() {
        Map<String, Object> request = (Map<String, Object>) ActionContext.getContext().get("request");
        request.put("courseList", allCou());
        request.put("kcscoreList", curSco());
    }





    // **delSco()方法实现删除成绩**
    public String delSco() throws Exception {
        String sql = "select * from CJ where XM ='" + getXm() + "' and KCM ='" + score.getKcm() + "'";
        Statement stmt = OrclConn.conns.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (!rs.next()) {
            setMsg("该记录不存在！");
            return "reject"; // 与 struts.xml 对应
        }
        ScoreJdbc scoreJ = new ScoreJdbc();
        Score sco = new Score();
        sco.setXm(getXm());
        sco.setKcm(score.getKcm());
        if (scoreJ.delScore(sco) != null) {
            setMsg("删除成功！");
        } else {
            setMsg("删除失败，请检查操作权限！");
        }
        Map<String, Object> request = (Map<String, Object>) ActionContext.getContext().get("request");
        request.put("courseList", allCou());
        request.put("kcscoreList", curSco());
        return "success"; // 与 struts.xml 对应
    }

    // **加载课程名列表（用于刷新页面）**
    public List<Course> allCou() {
        ScoreJdbc scoreJ = new ScoreJdbc();
        return scoreJ.showCourse();
    }

    // **加载当前课的成绩表（用于刷新页面）**
    public List<Score> curSco() {
        ScoreJdbc scoreJ = new ScoreJdbc();
        Score kcsco = new Score();
        kcsco.setKcm(score.getKcm());
        return scoreJ.queScore(kcsco);
    }

    // **getter和setter方法**
    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public int getCj() {
        return cj;
    }

    public void setCj(int cj) {
        this.cj = cj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}