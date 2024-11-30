package org.easybooks.xscj.vo;

import java.io.Serializable;

public class Course implements Serializable {
    private String kcm;  // 课程名
    private int xs;      // 学时
    private int xf;      // 学分

    public Course() {}  // 构造方法

    // kcm（课程名）属性的getter/setter方法
    public String getKcm() {
        return kcm;
    }

    public void setKcm(String kcm) {
        this.kcm = kcm;
    }

    // xs（学时）属性的getter/setter方法
    public int getXs() {
        return xs;
    }

    public void setXs(int xs) {
        this.xs = xs;
    }

    // xf（学分）属性的getter/setter方法
    public int getXf() {
        return xf;
    }

    public void setXf(int xf) {
        this.xf = xf;
    }
}