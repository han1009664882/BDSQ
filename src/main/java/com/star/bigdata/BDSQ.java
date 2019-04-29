package com.star.bigdata;

/**
 * Created by sh on 2017/5/16.
 */
public class BDSQ implements Cloneable{

    private int number; //编号
    private String custFlag; //客服：server，访客：customer
    private  String logTime; //日志时间（开始访问时间）
    private String custService;//当次接待客服名称
    private String visitPage; //访问来源
    private String visitUrl; //初始访问网址
    private String keyword; // 关键词
    private String searchWord; //搜索词
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

    public String getCustFlag() {
        return custFlag;
    }

    public void setCustFlag(String custFlag) {
        this.custFlag = custFlag;
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

    public String getVisitUrl() {
        return visitUrl;
    }

    public void setVisitUrl(String visitUrl) {
        this.visitUrl = visitUrl;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
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
                custFlag + '\001' +
                logTime + '\001' +
                custService + '\001' +
                visitPage + '\001' +
                visitUrl + '\001' +
                keyword + '\001' +
                searchWord + '\001' +
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
