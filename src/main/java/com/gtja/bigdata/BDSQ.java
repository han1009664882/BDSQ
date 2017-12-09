package com.gtja.bigdata;

/**
 * Created by sh on 2017/5/16.
 */
public class BDSQ implements Cloneable{

    private int number; //编号
    private String cust_flag; //客服：server，访客：customer
    private  String logTime; //日志时间（开始访问时间）
    private String custService;//当次接待客服名称
    private String visitPage; //访问来源
    private String vistUrl; //初始访问网址
    private String keyword; // 关键词
    private String searchword; //搜索词
    private String area; //地域
    private String ref; //对话内容里的链接
    private String phone; //电话
    private String remark; //包含QQ、微信、渠道码字眼的，完整内容

    private String bid;

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCust_flag() {
        return cust_flag;
    }

    public void setCust_flag(String cust_flag) {
        this.cust_flag = cust_flag;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getCustService() {
        return custService;
    }

    public void setCustService(String custService) {
        this.custService = custService;
    }

    public String getVisitPage() {
        return visitPage;
    }

    public void setVisitPage(String visitPage) {
        this.visitPage = visitPage;
    }

    public String getVistUrl() {
        return vistUrl;
    }

    public void setVistUrl(String vistUrl) {
        this.vistUrl = vistUrl;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSearchword() {
        return searchword;
    }

    public void setSearchword(String searchword) {
        this.searchword = searchword;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return  number + '\001' +
                cust_flag + '\001' +
                logTime + '\001' +
                custService + '\001' +
                visitPage + '\001' +
                vistUrl + '\001' +
                keyword + '\001' +
                searchword + '\001' +
                area + '\001' +
                ref + '\001' +
                phone + '\001' +
                remark + '\001'
                ;
    }

    @Override
    public BDSQ clone() throws CloneNotSupportedException {
        return (BDSQ) super.clone();
    }
}
