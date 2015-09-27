package com.example.zassmin.imagesearch.utils;

import com.example.zassmin.imagesearch.models.ImageFilter;
import com.loopj.android.http.RequestParams;

/**
 * Created by zassmin on 9/26/15.
 */
public class GoogleImageClient {
    // TODO: turn filter options into list, can I render these in the views?
    // face, photo, clipart, lineart
    private static final String FILTER_IMAGE_TYPE = "imgtype";
    // black, blue, blue, gray, green, orange, pink, purple, red, teal, white, yellow
    private static final String FILTER_COLOR = "imgcolor";
    // small|medium|large|xlarge
    private static final String FILTER_IMAGE_SIZE = "imgsz";
    // specified domain
    private static final String FILTER_SITE = "as_sitesearch";
    private static final String SEARCH_QUERY = "q";
    private static final String SEARCH_IMAGE_COUNT = "rsz";
    private static final String API_VERSION = "1.0";
    private static final String API_VERSION_KEY = "v";
    // TODO: aim to make private to keep google image client calls inside of this class
    public static final String SEARCH_URL =  "https://ajax.googleapis.com/ajax/services/search/images";

    public static RequestParams searchParams(ImageFilter imageFilter, String query) {
        RequestParams params = new RequestParams();
        params.put(API_VERSION_KEY, API_VERSION);
        params.put(SEARCH_QUERY, query);
        params.put(SEARCH_IMAGE_COUNT, 8); // FIXME: pass this in as an arg or something
        if (imageFilter != null) {
            // FIXME: handle cases when all the filters aren't available
            params.put(FILTER_COLOR, imageFilter.color);
            params.put(FILTER_IMAGE_SIZE, imageFilter.size);
            params.put(FILTER_SITE, imageFilter.site);
            params.put(FILTER_IMAGE_TYPE, imageFilter.type);

        }
        return params;
    }

    // FIXME: potentially move client call in this class!
    // client.get(SEARCH_URL, params, handler); // how do I get access to client?
}
