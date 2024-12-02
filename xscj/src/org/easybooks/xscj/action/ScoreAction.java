package org.easybooks.xscj.action;
//**Action所在的包**
//**导入所需的类和包**
import java.util.*;
import java.sql.*;
import org.easybooks.xscj.jdbc.*;
import org.easybooks.xscj.vo.*;
import com.opensymphony.xwork2.*;

public class ScoreAction extends ActionSupport {
    //**ScoreAction的属性声明**
    private String xm;
    //姓名
    private int cj;
    //成绩
    private String msg;
    //页面操作的消息提示文字
    private Score score;
    //成绩对象

    //**showAll()方法实现预加载信息（课程名）**
    public String showAll() {
        Map request = (Map) ActionContext.getContext().get("request");
        request.put("courseList", allCou());
        //将查到的课程名放到请求中，以便在页面上加载
        return "result";
    }

    //**queSco()方法实现查询某门课的成绩**
    public String queSco() {
        Map request = (Map) ActionContext.getContext().get("request");
        request.put("kcscoreList", curSco());
        //将查到的成绩记录放到Map‘容器中
        return "result";
    }
    //**addSco()方法实现录入成绩**
    public String addSco() throws Exception {
        //先检查CJ表中是否存在该学生该门课成绩的记录
        String sql = "select * from CJ where XM ='" + getXm() + "' and KCM ='" + score.getKcm() + "'";
        Statement stmt = OrclConn.conns.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            //如果结果集不为空表示该记录已经存在
            setMsg("该记录已经存在！");
            return "reject";
        }
        ScoreJdbc scoreJ = new ScoreJdbc();
        Score sco = new Score();
        //用“成绩”值对象存储和传递录入的内容
        sco.setXm(getXm());
        sco.setKcm(score.getKcm());
        sco.setCj(getCj());
        if (scoreJ.addScore(sco)!= null) {
            //传给业务逻辑类以执行录入操作
            setMsg("录入成功！");
        } else {
            setMsg("录入失败，请确保有此学生！");
        }
        //实时加载显示操作结果
        Map request = (Map) ActionContext.getContext().get("request");
        request.put("courseList", allCou());
        request.put("kcscoreList", curSco());
        return "result";
    }
  //**delSco()方法实现删除成绩**
    public String delSco() throws Exception {
        //先检查CJ表中是否存在该学生该门课成绩的记录
        String sql = "select * from CJ where XM ='" + getXm() + "' and KCM ='" + score.getKcm() + "'";
        Statement stmt = OrclConn.conns.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (!rs.next()) {
            //如果结果集为空表示该记录不存在，无法删除
            setMsg("该记录不存在！");
            return "reject";
        }
        ScoreJdbc scoreJ = new ScoreJdbc();
        Score sco = new Score();
        //创建“成绩”值对象
        sco.setXm(getXm());
        sco.setKcm(score.getKcm());
        if (scoreJ.delScore(sco)!= null) {
            //传给业务逻辑类以执行删除操作
            setMsg("删除成功！");
        } else {
            setMsg("删除失败，请检查操作权限！");
        }
        //实时加载显示操作结果
        Map request = (Map) ActionContext.getContext().get("request");
        request.put("courseList", allCou());
        request.put("kcscoreList", curSco());
        return "result";
    }
  //**加载课程名列表（用于刷新页面）**
    public List<Course> allCou() {
        ScoreJdbc scoreJ = new ScoreJdbc();
        List<Course> couList = scoreJ.showCourse();
        return couList;
    }

    //**加载当前课的成绩表（用于刷新页面）**
    public List<Score> curSco() {
        ScoreJdbc scoreJ = new ScoreJdbc();
        Score kcsco = new Score();
        kcsco.setKcm(score.getKcm());
        List<Score> kcscoList = scoreJ.queScore(kcsco);
        return kcscoList;
    }
  //**getter和setter方法**
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