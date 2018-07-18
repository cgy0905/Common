package com.cgy.common.net;

/**
 * Created by cgy
 * 2018/7/18  17:28
 */
public class Apis {

    //http://c.m.163.com/nc/article/headline/T1348647909107/0-5.html  头条

    public static final int PAGE_SIZE = 10;

    public static final String HOST = "http://c.m.163.com/";
    public static final String END_URL = "-" + PAGE_SIZE + ".html";
    public static final String END_DETAIL_URL = "/full.html";
    // 头条
    public static final String TOP_URL = HOST + "nc/article/headline/";
    public static final String TOP_ID = "T1348647909107";
    // 新闻详情
    public static final String NEW_DETAIL = HOST + "nc/article/";

    public static final String COMMON_URL = HOST + "nc/article/list/";

    // 汽车
    public static final String CAR_ID = "T1348654060988";
    // 笑话
    public static final String JOKE_ID = "T1350383429665";
    // nba
    public static final String NBA_ID = "T1348649145984";

    // 图片
    public static final String IMAGES_URL = "http://api.laifudao.com/open/tupian.json";
    /**
     * http://c.3g.163.com/nc/video/list/V9LG4CHOR/n/10-10.html 视频
     */
    public static final String Video = "nc/video/list/";
    // 热点视频
    public static final String VIDEO_HOT_ID = "V9LG4B3A0";
    // 娱乐视频
    public static final String VIDEO_ENTERTAINMENT_ID = "V9LG4CHOR";
    // 搞笑视频
    public static final String VIDEO_FUN_ID = "V9LG4E6VR";
    // 精品视频
    public static final String VIDEO_CHOICE_ID = "00850FRB";
}
