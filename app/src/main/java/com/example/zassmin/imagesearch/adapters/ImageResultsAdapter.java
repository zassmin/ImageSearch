package com.example.zassmin.imagesearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.zassmin.imagesearch.R;
import com.example.zassmin.imagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zassmin on 9/24/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {
    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context, android.R.layout.simple_list_item_1, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageInfo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);

        // clear out the image
        ivImage.setImageResource(0);

        // populate
        Picasso.with(getContext())
                .load(imageInfo.thumbUrl)
                .placeholder(R.mipmap.ic_launcher)
                .resize(150, 150)
                .centerCrop()
                .into(ivImage);
        return convertView;
    }
}
