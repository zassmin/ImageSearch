package com.example.zassmin.imagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zassmin on 9/24/15.
 */
public class ImageResult implements Serializable {
    public String fullUrl;
    public String title;
    public String thumbUrl;

    public ImageResult(JSONObject json) {
        try {
           this.fullUrl = json.getString("url");
           this.thumbUrl = json.getString("tbUrl");
           this.title = json.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<ImageResult> fromJsonArray(JSONArray array) {
        ArrayList<ImageResult> results = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
