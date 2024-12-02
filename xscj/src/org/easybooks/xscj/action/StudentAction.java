package org.easybooks.xscj.action;
//**导入所需的类和包**
import java.sql.*;
import java.util.*;
import java.io.*;
import org.apache.struts2.ServletActionContext;
import org.easybooks.xscj.jdbc.*;
import org.easybooks.xscj.vo.*;
import com.opensymphony.xwork2.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class StudentAction extends ActionSupport {
    //**StudentAction的属性声明**
    private String xm;
    //姓名
    private String msg;
    //页面操作的消息提示文字
    private Student student;
    //学生对象
    private Score score;
    //成绩对象
    private File photo;
    //照片

    //**addStu()方法实现录入学生信息**
    public String addStu() throws Exception {
        //先检查XS表中是否存在该学生的记录
        String sql = "select * from XS where XM ='" + getXm() + "'";
        Statement stmt = OrclConn.conns.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            //如果结果集不为空表示该学生记录已经存在
            setMsg("该学生已经存在！");
            return "result";
        }
        StudentJdbc studentJ = new StudentJdbc();
        Student stu = new Student();
        /*通过“学生”值对象收集表单数据*/
        stu.setXm(getXm());
        stu.setXb(student.getXb());
        stu.setCssj(student.getCssj());
        stu.setKcs(student.getKcs());
        stu.setBz(student.getBz());
        
        if(this.getPhoto()!= null) {
            //有照片上传的情况
            FileInputStream fis = new FileInputStream(this.getPhoto());
            //创建文件输入流，用于读取图片内容
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            stu.setZp(buffer);
        }

        if(studentJ.addStudent(stu)!= null) {
            setMsg("添加成功！");
            Map request = (Map)ActionContext.getContext().get("request");
            //获取上下文请求对象
            request.put("student", stu);
        } else {
            setMsg("添加失败，请检查输入信息！");
        }
        return "result";
     }
    
    /** getImage()方法实现获取和显示当前学生的照片*/
    public String getImage() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        //创建Servlet响应对象
        StudentJdbc studentJ = new StudentJdbc();
        student = new Student();
        student.setXm(getXm());
        byte[] img = studentJ.getStudentZp(student);
        response.setContentType("image/jpeg");
        ServletOutputStream os = response.getOutputStream();
        if (img!= null && img.length!= 0) {
            for (int i = 0; i < img.length; i++) {
                os.write(img[i]);
            }
        }
        os.flush();
        return NONE;
    }
    /** delStu()方法实现删除学生信息*/
    public String delStu() throws Exception {
        //先检查XS表中是否存在该学生的记录
        boolean exist = false;
        String sql = "select * from XS where XM ='" + getXm() + "'";
        Statement stmt = OrclConn.conns.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            exist = true;
        }
        if (exist) {
            //如果存在则可进行删除操作
            StudentJdbc studentJ = new StudentJdbc(); //创建JBDC业务逻辑对象
            Student stu = new Student(); //创建“学生”值对象
            stu.setXm(getXm());
            if(studentJ.delStudent(stu)!= null) {
                setMsg("删除成功！");
            } else 
                setMsg("删除失败，请检查操作权限！");
            } else {
                setMsg("该学生不存在！");
            }
            return "result";
        }
    
    /** queStu()方法实现查询学生信息*/
    public String queStu() throws Exception {
        //先检查XS表中是否存在该学生的记录
        boolean exist = false;
        String sql = "select * from XS where XM ='" + getXm() + "'";
        Statement stmt = OrclConn.conns.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            exist = true;
        }
        if (exist) {
            //如果存在则进行查询操作
            StudentJdbc studentJ = new StudentJdbc();
            Student stu = new Student();
            stu.setXm(getXm());
            if (studentJ.showStudent(stu)!= null) {
                setMsg("查询成功！");
                Map request = (Map) ActionContext.getContext().get("request");
                request.put("student", stu);
                //以下为进一步查询该生的成绩，页面生成成绩单
                ScoreJdbc scoreJ = new ScoreJdbc();
                Score sco = new Score();
                sco.setXm(getXm());
                List<Score> scoList = scoreJ.showScore(sco);
                request.put("scoreList", scoList);
            } else {
                setMsg("查询失败，请检查操作权限！");
            }
        } else {
            setMsg("该学生不存在！");
        }
        return "result";
    }
    /** updStu()方法实现更新学生信息*/
    public String updStu() throws Exception {
        StudentJdbc studentJ = new StudentJdbc();
        Student stu = new Student();
        //通过“学生”值对象收集表单数据
        stu.setXm(getXm());
        stu.setXb(student.getXb());
        stu.setCssj(student.getCssj());
        
        stu.setKcs(student.getKcs());
        stu.setBz(student.getBz());
        if (this.getPhoto()!= null) {
            FileInputStream fis = new FileInputStream(this.getPhoto());
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            stu.setZp(buffer);
        }

        if (studentJ.updateStudent(stu)!= null) {
            setMsg("更新成功！");
            Map request = (Map) ActionContext.getContext().get("request");
            request.put("student", stu);
        } else {
            setMsg("更新失败，请检查输入信息！");
        }
        return "result";
    }
    //**getter和setter方法**
    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public File getPhoto() {
        return photo;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }
}