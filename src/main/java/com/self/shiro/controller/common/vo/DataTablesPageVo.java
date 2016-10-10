package main.java.com.self.shiro.controller.common.vo;

import java.util.List;

/**
 * Create Date: 2016/6/22 9:46
 * Description: 返回给DataTables插件的对象
 */
public class DataTablesPageVo<T> {
    private  String sEcho;
    private  long iTotalRecords;
    private long iTotalDisplayRecords;
    private List<T> aaData;

    public DataTablesPageVo() {}

    public DataTablesPageVo(String sEcho, long iTotalRecords, long iTotalDisplayRecords, List<T> aaData) {
        this.sEcho = sEcho;
        this.iTotalRecords = iTotalRecords;
        this.iTotalDisplayRecords = iTotalDisplayRecords;
        this.aaData = aaData;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public long getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(long iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public long getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public List<T> getAaData() {
        return aaData;
    }

    public void setAaData(List<T> aaData) {
        this.aaData = aaData;
    }
}
