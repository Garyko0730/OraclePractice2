package org.easybooks.xscj.action;
//**导入所需的类和包**
import java.sql.*;
import java.text.SimpleDateFormat;
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
        if (OrclConn.conns == null) {
            throw new IllegalStateException("数据库连接未正确初始化！");
        }

        if (student == null) {
            setMsg("学生数据未正确提交！");
            return "error";
        }

        // 验证是否已经存在该学生记录
        String sql = "SELECT COUNT(*) FROM XS WHERE XM = ?";
        try (PreparedStatement stmt = OrclConn.conns.prepareStatement(sql)) {
            stmt.setString(1, getXm());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    setMsg("该学生已经存在！");
                    System.out.println("学生已存在：" + getXm());
                    return "success";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            setMsg("检查学生记录时发生错误！");
            return "error";
        }

        // 如果学生不存在，执行录入逻辑
        StudentJdbc studentJ = new StudentJdbc();
        Student stu = new Student();
        stu.setXm(getXm());
        stu.setXb(student.getXb());
        stu.setCssj(student.getCssj());
        stu.setKcs(student.getKcs());
        stu.setBz(student.getBz());
        
     
        // 处理照片上传
        if (this.getPhoto() != null) {
            try (FileInputStream fis = new FileInputStream(this.getPhoto())) {
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                stu.setZp(buffer);
            } catch (Exception e) {
                e.printStackTrace();
                setMsg("处理上传照片时发生错误！");
                return "error";
            }
        } else {
            System.out.println("没有上传照片。");
            stu.setZp(null);
        }

        if (studentJ.addStudent(stu) != null) {
            setMsg("添加成功！");
            // 安全类型转换
            Map<String, Object> request = (Map<String, Object>) ActionContext.getContext().get("request");
            request.put("student", stu);
            // 打印表单数据以便调试
            System.out.println("表单数据：");
            System.out.println("学生姓名：" + stu.getXm());
            System.out.println("学生性别：" + stu.getXb());
            System.out.println("出生日期：" + stu.getCssj());
        } else {
            setMsg("添加失败，请检查输入信息！");
            return "error";
        }

        return "success";
    }

    
    /** getImage()方法实现获取和显示当前学生的照片 */
    public String getImage() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse(); // 获取响应对象
        response.setContentType("image/jpeg"); // 设置响应的内容类型为 JPEG 图片

        StudentJdbc studentJ = new StudentJdbc(); // 数据库操作类
        Student student = new Student(); // 创建学生对象
        student.setXm(getXm()); // 设置学生姓名

        // 从数据库中获取照片
        byte[] img = studentJ.getStudentZp(student);

        try (ServletOutputStream os = response.getOutputStream()) { // 获取输出流
            if (img != null && img.length > 0) {
            	System.out.println("照片长度：" + img.length);
                os.write(img); // 直接写入二进制数据
            } else {
                System.out.println("没有找到照片数据或照片为空。");
                // 可以返回一个默认图片
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("获取照片时发生错误！", e);
        }

        return NONE; // 不需要返回视图
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
        return "success";
        }
    
    /** queStu()方法实现查询学生信息 */
    public String queStu() throws Exception {
        boolean exist = false;

        // 检查学生是否存在于XS表中
        String sql = "SELECT * FROM XS WHERE XM = ?";
        try (PreparedStatement stmt = OrclConn.conns.prepareStatement(sql)) {
            stmt.setString(1, getXm());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                exist = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            setMsg("系统错误，检查学生记录失败！");
            return "error";
        }

        if (exist) {
            // 查询学生基本信息
            StudentJdbc studentJ = new StudentJdbc();
            Student stu = new Student();
            stu.setXm(getXm());

            if (studentJ.showStudent(stu) != null) {
                setMsg("查询成功！");
                Map<String, Object> request = (Map<String, Object>) ActionContext.getContext().get("request");
                request.put("student", stu);

                // 查询学生成绩列表
                try {
                    ScoreJdbc scoreJ = new ScoreJdbc();
                    Score sco = new Score();
                    sco.setXm(getXm());
                    List<Score> scoList = scoreJ.showScore(sco);

                    // 设置已修课程数量
                    if (scoList != null) {
                        stu.setKcs(scoList.size());
                    } else {
                        stu.setKcs(0);
                    }

                    request.put("scoreList", scoList);
                } catch (Exception e) {
                    e.printStackTrace();
                    setMsg("查询成绩失败，请检查操作权限！");
                }
            } else {
                setMsg("查询学生基本信息失败，请检查操作权限！");
            }
        } else {
            setMsg("该学生不存在！");
        }
        return "success";
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
        return "success";
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