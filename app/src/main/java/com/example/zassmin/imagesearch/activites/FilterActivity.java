package com.example.zassmin.imagesearch.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zassmin.imagesearch.R;
import com.example.zassmin.imagesearch.models.ImageFilter;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onFilterSet(View view) {
        Toast.makeText(this, this.toString(), Toast.LENGTH_SHORT).show();
        // grab stuff from the form
        EditText etColor = (EditText) findViewById(R.id.etColor);
        EditText etImageSize = (EditText) findViewById(R.id.etImageSize);
        EditText etImageType = (EditText) findViewById(R.id.etImageType);
        EditText etSite = (EditText) findViewById(R.id.etSite);

        // load intent
        Intent data = new Intent();
        // FIXME: can I grab all the data at once and just build the object here? Instead of doing fromIntent?
        // TODO: if so, make the image filter serializable
        // FIXME: this only works if there are no null values - fix that
        data.putExtra(ImageFilter.COLOR, etColor.getText().toString());
        data.putExtra(ImageFilter.SIZE, etImageSize.getText().toString());
        data.putExtra(ImageFilter.TYPE, etImageType.getText().toString());
        data.putExtra(ImageFilter.SITE, etSite.getText().toString( ));

        setResult(RESULT_OK, data);

        this.finish();
    }
}
