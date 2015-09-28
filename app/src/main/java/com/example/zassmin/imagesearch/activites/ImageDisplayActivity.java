package com.example.zassmin.imagesearch.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.zassmin.imagesearch.R;
import com.example.zassmin.imagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        // remove action bar on image display activity
        // like jquery toggle - could also add as theme see:
        // http://stackoverflow.com/questions/30284627/how-to-show-and-hide-actionbar-with-appcompat-v-7
        // which is better?
        getSupportActionBar().hide();
        // pull out the url from the intent
        ImageResult imageResult = (ImageResult) getIntent().getSerializableExtra("result");
        // find the image view
        ImageView ivImageResult = (ImageView) findViewById(R.id.ivImageResult);
        // load the image url into the imageview using picasso
        Picasso.with(this)
                .load(imageResult.fullUrl)
                .placeholder(R.mipmap.ic_launcher)
                .noFade()
                .resize(800, 800)
                .centerCrop()
                .into(ivImageResult);
        // how do you know when you are passing `this` instead of getContext()
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
