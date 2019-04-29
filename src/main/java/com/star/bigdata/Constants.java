package com.star.bigdata;

/**
 * Created by sh on 2017/5/22.
 */
public interface Constants {

    /**
     * 字符相关常量
     */
    String ENCODING = "UTF-8";
    String _T = "\t";
    String _N = "\n";

    /**
     * DATA相关常量
     */
    String BID = "bid";
    String LOGTIME = "enterTime"; //访问时间
    String CUST_SERVICE = "waiterDispose";//客服名称
    String VISIT_URL = "landingUri";//初始访问地址
    String KEY_WORD = "keyword";//关键词
    String SEARCH_WORD = "searchWord";
    String AREA = "visitorIPArea";

    /**
     * DATA_HISTORY常量
     */
    String SENDER_NAME = "senderName";//访客或客服
    String VISIT_PAGE = "fromUrl"; //沟通发起页

    String HISTORY_LOG = "historyLog";
    String MSG = "msg";

    String CUST_FLAG_CUSTOMER = "customer";
    String CUST_FLAG_SERVER = "server";

    String CUSTOMER = "访客";

}
