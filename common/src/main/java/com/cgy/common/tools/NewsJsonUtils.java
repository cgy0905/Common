package com.cgy.common.tools;

import com.cgy.common.module.news.model.NewsDetailEntity;
import com.cgy.common.module.news.model.NewsEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.llf.basemodel.utils.JsonUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class NewsJsonUtils {

    private final static String TAG = "NewsJsonUtils";


    public static List<NewsEntity> readJsonDataBeans(String res, String value) {
        List<NewsEntity> beans = new ArrayList<>();
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(res).getAsJsonObject();
            JsonElement jsonElement = jsonObj.get(value);
            if (jsonElement == null){
                return null;
            }
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (jo.has("skipType") && "special".equals(jo.get("skipType").getAsString())) {
                    continue;
                }
                if (jo.has("TAGS") && !jo.has("TAG")) {
                    continue;
                }

                NewsEntity news = JsonUtils.deserialize(jo, NewsEntity.class);
                beans.add(news);
            }
        } catch (Exception e) {

        }
        return beans;
    }

    public static NewsDetailEntity readJsonNewsDetailBeans(String res, String docId) {
        NewsDetailEntity newsDetailBean = null;
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(res).getAsJsonObject();
            JsonElement jsonElement = jsonObj.get(docId);
            if (jsonElement == null) {
                return null;
            }
            newsDetailBean = JsonUtils.deserialize(jsonElement.getAsJsonObject(), NewsDetailEntity.class);
        } catch (Exception e) {

        }
        return newsDetailBean;
    }
}
