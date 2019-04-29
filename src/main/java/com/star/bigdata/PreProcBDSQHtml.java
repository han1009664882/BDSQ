package com.star.bigdata;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sh on 2017/5/16.
 */
public class PreProcBDSQHtml {

    private static Logger logger = Logger.getLogger(PreProcBDSQHtml.class);
    /**
     * 获取HTML内容
     */
    private String readHtml(File inputFile) throws IOException {
        String text = null;
        Document document = Jsoup.parse(inputFile, Constants.ENCODING);
        Elements elements = document.getElementsByTag("script");
        if (elements.size() < 2) {
            logger.error("页面无记录");
            return "";
        } else {
            text = elements.get(1).data();
            return text;
        }
    }

    /**
     * 解析html内容
     *
     * @param info
     * @return
     */
    private List<BDSQ> parseHtml(String info) throws CloneNotSupportedException {
        String[] splits = info.trim().split(Constants._N);
        String historyJson = sub(splits[3], "DATA_HISTORY=");
        String preData = sub(splits[4], "DATA=");
        Map<String, BDSQ> historyBDSQMap = parseHistoryData(historyJson);
        Map<String, BDSQ> dataMap = parseData(preData);
        return buildFinalBdsq(historyBDSQMap, dataMap);
    }

    /**
     * 创建最终的输出对象
     *
     */
    private List<BDSQ> buildFinalBdsq(Map<String, BDSQ> historyBDSQMap, Map<String, BDSQ> dataMap) throws CloneNotSupportedException {
        List<BDSQ> list = new ArrayList<>();
        BDSQ bdsq = null;
        for (Map.Entry<String, BDSQ> entry : historyBDSQMap.entrySet()) {
            String bid = entry.getKey();
            bdsq = entry.getValue();
            for (Map.Entry<String, BDSQ> dataEntry : dataMap.entrySet()) {
                String bid2 = dataEntry.getKey();
                BDSQ bdsq2 = dataEntry.getValue();
                if (bid.equals(bid2)) {
                    bdsq.setNumber(bdsq2.getNumber());
                    bdsq.setLogTime(bdsq2.getLogTime());
                    bdsq.setCustService(bdsq2.getCustService());
                    bdsq.setVisitUrl(bdsq2.getVisitUrl());
                    bdsq.setKeyword(bdsq2.getKeyword());
                    bdsq.setSearchWord(bdsq2.getSearchWord());
                    bdsq.setArea(bdsq2.getArea());
                }
            }
            String[] phone = bdsq.getPhone().split(Constants._T);
            if (phone.length > 1) {
                for (String ph : phone) {
                    BDSQ bdsq3 = bdsq.clone();
                    if (StringUtils.isNoneEmpty(ph)) {
                        bdsq3.setPhone(ph);
                    }
                    list.add(bdsq3);
                    //存在 { phone + "\t"+""+"\t"+phone,去除clone的bdsq3
                    if (bdsq3.getPhone().contains(Constants._T)) {
                        list.remove(bdsq3);
                    }
                }
            }
            list.add(bdsq);
            if (phone.length > 1) {
                list.remove(bdsq);
            }
        }
        return list;
    }

    /**
     * 解析Data
     */
    private Map<String, BDSQ> parseData(String dataJson) {
        JSONArray jsonArray = new JSONArray(dataJson);
        Map<String, BDSQ> dataMap = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            BDSQ bdsq = buidBDSQ(json);
            bdsq.setNumber(i + 1);
            dataMap.put(bdsq.getBid(), bdsq);
        }
        return dataMap;
    }

    /**
     * 通过DATA建立BDSQ
     */
    private BDSQ buidBDSQ(JSONObject json) {
        String logTime = "";
        String custService = "";
        String visitUrl = "";
        String keyword = "";
        String searchword = "";
        String area = "";
        String bid = "";
        if (json.has(Constants.BID))
            bid = json.getString(Constants.BID);
        if (json.has(Constants.LOGTIME))
            logTime = json.getString(Constants.LOGTIME);
        if (json.has(Constants.CUST_SERVICE))
            custService = json.getString(Constants.CUST_SERVICE);
        if (json.has(Constants.VISIT_URL))
            visitUrl = json.getString(Constants.VISIT_URL);
        if (json.has(Constants.KEY_WORD))
            keyword = json.getString(Constants.KEY_WORD);
        if (json.has(Constants.SEARCH_WORD))
            searchword = json.getString(Constants.SEARCH_WORD);
        if (json.has(Constants.AREA))
            area = json.getString(Constants.AREA);
        BDSQ bdsq = new BDSQ();
        bdsq.setBid(bid);
        bdsq.setLogTime(logTime);
        bdsq.setCustService(custService);
        bdsq.setVisitUrl(visitUrl);
        bdsq.setKeyword(keyword);
        bdsq.setSearchWord(searchword);
        bdsq.setArea(area);

        return bdsq;
    }

    /**
     * 解析历史数据
     */
    private Map<String, BDSQ> parseHistoryData(String str) {
        //[{"historyLog":[{msg,independCommunication...}],"bid":136},....]
        JSONArray jsonArray = new JSONArray(str);
        String bid = "";
        Map<String, BDSQ> map = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            //"historyLog":[{msg,independCommunication...}],"bid":136}
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONArray jsonArray2 = jsonObject.getJSONArray(Constants.HISTORY_LOG);
            if (jsonArray2.length() <= 12) {
                continue;
            }
            if (jsonObject.has(Constants.BID)) {
                bid = jsonObject.getString(Constants.BID);
            }
            BDSQ bdsq = extractFromHistory(jsonArray2);
            if (bdsq != null) {
                map.put(bid, bdsq);
            }
        }
        return map;
    }

    /**
     * 从历史记录提取BDSQ信息
     */
    private BDSQ extractFromHistory(JSONArray jsonArray) {
        String custSerever = "";
        String visitPage = "";
        String cust_flag = "";
        String ref = "";
        String phone = "";
        String remark = "";

        BDSQ bdsq = null;
//        StringBuilder msg = new StringBuilder();
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject jsonObject = jsonArray.getJSONObject(j);
            if (jsonObject.has(Constants.MSG)) {
                String replace = jsonObject.getString(Constants.MSG).replace("\\", "");
                if (replace.startsWith("{")) {
                    JSONObject js = new JSONObject(replace);
                    visitPage = js.getString(Constants.MSG);
                } else {
                    String tmpMsg = jsonObject.getString(Constants.MSG);
                    String number = matchNumber(tmpMsg);
                    String tmpPhone = matchPhone(tmpMsg);
                    if (StringUtils.isNotEmpty(tmpPhone)) {
                        phone += tmpPhone + Constants._T;
                    }
                    Document document = Jsoup.parse(tmpMsg);
                    String url = document.select("a").text();
                    if (StringUtils.isNotEmpty(url)) {
                        ref = url + Constants._T;
                    }
//                    tmpMsg = document.text();
//                    String msgResult = msg.toString();
                    String msgResult= document.text();
                    if (msgResult.contains("qq") || msgResult.contains("QQ") || msgResult.contains("微信") ||
                            msgResult.contains("手机") || msgResult.contains("电话") || msgResult.contains("联系方式") ||
                            msgResult.contains("渠道码") || StringUtils.isNotEmpty(number) || StringUtils.isNotEmpty(phone)) {
                        remark = msgResult;
                    }
                    if (StringUtils.isNotEmpty(remark) || StringUtils.isNotEmpty(ref) || StringUtils.isNotEmpty(phone)) {

                        if (jsonObject.has(Constants.SENDER_NAME) && jsonObject.get(Constants.SENDER_NAME) != null) {
                            String senderNameTmp = jsonObject.getString(Constants.SENDER_NAME);
                            if (senderNameTmp.equals(Constants.CUSTOMER)) {
                                cust_flag = Constants.CUST_FLAG_CUSTOMER;
                            } else {
                                cust_flag = Constants.CUST_FLAG_SERVER;
                                if (StringUtils.isNotEmpty(senderNameTmp)) {
                                    custSerever = senderNameTmp;
                                }
                            }
                        }
                        bdsq = new BDSQ();
                        bdsq.setCustService(custSerever);
                        bdsq.setVisitPage(visitPage);
                        bdsq.setCustFlag(cust_flag);
                        bdsq.setRef(ref);
                        bdsq.setPhone(phone);
                        bdsq.setRemark(remark);
                    }
                }
            }
        }
        return bdsq;
    }

    /**
     * 匹配手机号
     */
    private String matchPhone(String str) {
        StringBuilder phone_tmp = new StringBuilder();
        String regExp = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        for (int i = 0; i < str.length(); i++) {
            if (str.substring(i, i + 1).contains("1")) {
                String tmp = str.substring(i, i + 11);
                Matcher m = p.matcher(tmp);
                if (m.find() && StringUtils.isNotEmpty(tmp)) {
                    phone_tmp.append(tmp).append(Constants._T);
                }
            }
        }
        return phone_tmp.toString();
    }

    /**
     * 匹配6位渠道码
     */
    private String matchNumber(String str) {
        StringBuilder tmp = new StringBuilder();
        String regix = "\\d{6}";
        Pattern pattern = Pattern.compile(regix);
        for (int i = 0; i < str.length(); i++) {
            String s = str.substring(i, i + 6);
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                tmp.append(s).append(Constants._T);
            }
        }
        return tmp.toString();
    }

    /**
     * 截取字符串
     *
     * @param source 原始字符串
     * @param str    确定开始未知的内容
     * @return 截取结果
     */
    private String sub(String source, String str) {
        int start = source.indexOf(str) + str.length();
        return source.substring(start, source.length() - 1);
    }

    /**
     * 递归获取文件夹下的所有文件名
     *
     * @param path     文件夹路径
     * @param dirName  目录路径
     * @param fileName 文件路径
     */
    private void getFileList(String path, List<String> dirName, List<File> fileName) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            String[] names = file.list();
            if (names != null) {
                dirName.addAll(Arrays.asList(names));
            }
            for (File f : files) {
                if (f.isDirectory()) {
                    getFileList(f.getAbsolutePath(), dirName, fileName);
                } else if (f.isFile() && fileName != null) {
                    fileName.add(f);
                }
            }
        }
    }

    private void output(String inputFile, String outputFile) {
        File html_out = new File(outputFile);
        try (OutputStream os = new FileOutputStream(html_out.getAbsolutePath(), true);
             Writer osWriter = new OutputStreamWriter(os, Constants.ENCODING);
             BufferedWriter writer = new BufferedWriter(osWriter)) {
            List<File> list = new ArrayList<>();
            getFileList(inputFile, new ArrayList<>(), list);
            for (File f : list) {
                String htmlContent = readHtml(f);
                List<BDSQ> bdsqs = parseHtml(htmlContent);
                for (BDSQ bdsq : bdsqs) {
                    writer.write(bdsq.toString(), 0, bdsq.toString().length());
                    writer.flush();
                }
            }
        } catch (IOException | CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Error! Need two args : input_dir_path and output_file_path");
        } else {
            PreProcBDSQHtml preProcBDSQHtml = new PreProcBDSQHtml();
            preProcBDSQHtml.output(args[0], args[1]);
        }

    }
}
