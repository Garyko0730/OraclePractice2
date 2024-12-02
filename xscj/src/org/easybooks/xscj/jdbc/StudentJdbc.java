package org.easybooks.xscj.jdbc;
//业务逻辑类所在的包
//**导入所需的类和包**
import java.sql.*;
import org.easybooks.xscj.vo.*;

public class StudentJdbc {
    private PreparedStatement psmt = null;
    //预处理SQL语句对象
    private ResultSet rs = null;
    //结果集对象
    //**录入学生**
    public Student addStudent(Student student) {
        String sql = "insert into XS(XM, XB, CSSJ, KCS, BZ, ZP) values(?,?,?,?,?,?)";
        //录入操作的SQL语句
        try {
            psmt = OrclConn.conns.prepareStatement(sql);
            //预编译语句
            //下面开始收集数据参数*
            psmt.setString(1, student.getXm());
            //姓名
            psmt.setString(2, student.getXb());
            //性别
            psmt.setTimestamp(3, new Timestamp(student.getCssj().getTime()));
            //出生时间
            psmt.setInt(4, student.getKcs());
            //已修课程数
            psmt.setString(5, student.getBz());
            //备注
            psmt.setBytes(6, student.getZp());
            //照片
            psmt.execute();
            //执行语句
        } catch (Exception e) {
            e.printStackTrace();
        }
        return student;
        //返回“学生”值对象给Action（即StudentAction）
    }

/**获取某个学生的照片*/
public byte[] getStudentZp(Student student) {
    String sql = "select ZP from XS where XM ='" + student.getXm() + "'";
    try {
        psmt = OrclConn.conns.prepareStatement(sql);
        //获取静态连接，预编译语句
        rs = psmt.executeQuery();
        //执行语句，返回所获得的学生照片
        if (rs.next()) {
            student.setZp(rs.getBytes("ZP"));
            //值对象获取照片数据
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return student.getZp();
    //通过值对象返回照片数据
}

/**删除学生
 * @return */
public Student delStudent(Student student) {
    String sql = "delete from XS where XM ='" + student.getXm() + "'";
    try {
        psmt = OrclConn.conns.prepareStatement(sql);
        //预编译语句
        psmt.execute();
        //执行删除操作
    } catch (Exception e) {
        e.printStackTrace();
    }
    return student;
}

/**查询学生*/
public Student showStudent(Student student) {
    String sql = "select * from XS where XM ='" + student.getXm() + "'";
    try {
        psmt = OrclConn.conns.prepareStatement(sql);
        //预编译语句
        rs = psmt.executeQuery();
        //执行语句，返回所查询的学生信息
        if (rs.next()) {
            //返回结果集不为空
            //用“学生”值对象保存查到的学生各项信息
            student.setXb(rs.getString("XB"));
            //性别
            student.setCssj(rs.getDate("CSSJ"));
            //出生时间
            student.setKcs(rs.getInt("KCS"));
            //已修课程数
            student.setZp(rs.getBytes("ZP"));
            //照片
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return student;
    //返回“学生”值对象给Action（即StudentAction）
}
/**更新学生信息*/
public Student updateStudent(Student student) {
    String sql = "update XS set XM=?, XB=?, CSSJ=?, KCS=?, BZ=?, ZP=? where XM = " + student.getXm() + "";
    // 更新操作的SQL语句
    try {
        psmt = OrclConn.conns.prepareStatement(sql);
        // 预编译语句
        // 下面开始收集数据参数
        psmt.setString(1, student.getXm());
        // 姓名
        psmt.setString(2, student.getXb());
        // 性别
        psmt.setTimestamp(3, new Timestamp(student.getCssj().getTime()));
        // 出生时间
        psmt.setInt(4, student.getKcs());
        // 已修课程数
        psmt.setString(5, student.getBz());
        // 备注
        psmt.setBytes(6, student.getZp());
        // 照片
        psmt.execute();
        // 执行语句
    } catch (Exception e) {
        e.printStackTrace();
    }
    return student;
    // 返回值对象给 Action
}
}