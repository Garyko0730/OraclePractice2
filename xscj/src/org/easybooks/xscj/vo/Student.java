package org.easybooks.xscj.vo;
import java.util.*;
public class Student implements java.io.Serializable {
    private String xm;        // 姓名
    private String xb;        // 性别
    private Date cssj;        // 出生日期
    private int kcs;          // 课程数
    private String bz;        // 备注
    private byte[] zp;        // 照片（字节数组）

    public Student() {}      // 构造方法

    // xm（姓名）属性的getter/setter方法
    public String getXm() {
        return this.xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    // xb（性别）属性的getter/setter方法
    public String getXb() {
        return this.xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    // cssj（出生日期）属性的getter/setter方法
    public Date getCssj() {
        return this.cssj;
    }

    public void setCssj(Date cssj) {
        this.cssj = cssj;
    }

    // kcs（课程数）属性的getter/setter方法
    public int getKcs() {
        return this.kcs;
    }

    public void setKcs(int kcs) {
        this.kcs = kcs;
    }

    // bz（备注）属性的getter/setter方法
    public String getBz() {
        return this.bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    // zp（照片）属性的getter/setter方法
    public byte[] getZp() {
        return this.zp;
    }

    public void setZp(byte[] zp) {
        this.zp = zp;
    }
}