package com.example.zassmin.imagesearch.models;

import android.content.Intent;

/**
 * Created by zassmin on 9/26/15.
 */
public class ImageFilter {
    public String color;
    public String size;
    public String type;
    public String site;

    public static String COLOR = "filter_color";
    public static String SIZE = "filter_image_size";
    public static String TYPE = "filter_image_type";
    public static String SITE = "filter_site" ;

    // FIXME: might be able to remove...
    public static ImageFilter fromIntent(Intent data) {
        ImageFilter imageFilter = new ImageFilter();
        imageFilter.color = data.getStringExtra(COLOR);
        imageFilter.size = data.getStringExtra(SIZE);
        imageFilter.type = data.getStringExtra(TYPE);
        imageFilter.site = data.getStringExtra(SITE);
        return imageFilter;
    }
}
