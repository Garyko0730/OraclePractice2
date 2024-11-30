package org.easybooks.xscj.vo;

import java.io.Serializable;

public class Score implements Serializable {
    private String xm;    // 姓名
    private String kcm;   // 课程名
    private int cj;       // 成绩

    public Score() {}   // 构造方法

    // xm（姓名）属性的getter/setter方法
    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    // kcm（课程名）属性的getter/setter方法
    public String getKcm() {
        return kcm;
    }

    public void setKcm(String kcm) {
        this.kcm = kcm;
    }

    // cj（成绩）属性的getter/setter方法
    public int getCj() {
        return cj;
    }

    public void setCj(int cj) {
        this.cj = cj;
    }
}