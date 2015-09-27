package com.example.zassmin.imagesearch.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

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
        // grab data from view
        Spinner spnColor = (Spinner) findViewById(R.id.spnColor);
        Spinner spnImageSize = (Spinner) findViewById(R.id.spnImageSize);
        Spinner spnImageType = (Spinner) findViewById(R.id.spnImageType);
        EditText etSite = (EditText) findViewById(R.id.etSite);

        // load intent
        Intent data = new Intent();

        // FIXME: can I grab all the data at once and just build the object here? Instead of doing fromIntent? If so, make the image filter serializable
        String colorSpinner = spnColor.getSelectedItem().toString();
        String imageSizeSpinner = spnImageSize.getSelectedItem().toString();
        String imageTypeSpinner = spnImageType.getSelectedItem().toString();

        // don't set spinner defaults
        if (hasSpinnerPrompt(colorSpinner)) {
            colorSpinner = null;
        }
        if (hasSpinnerPrompt(imageSizeSpinner)) {
            imageSizeSpinner = null;
        }
        if (hasSpinnerPrompt(imageTypeSpinner)) {
            imageTypeSpinner = null;
        }

        data.putExtra(ImageFilter.COLOR, colorSpinner);
        data.putExtra(ImageFilter.SIZE, imageSizeSpinner);
        data.putExtra(ImageFilter.TYPE, imageTypeSpinner);
        data.putExtra(ImageFilter.SITE, etSite.getText().toString());

        setResult(RESULT_OK, data);

        this.finish();
    }

    // NOTE: this felt like a simpler approach over adapter overrides, suggested:
    // http://stackoverflow.com/questions/867518/how-to-make-an-android-spinner-with-initial-text-select-one/12221309#12221309
    // I'm guessing performance is at stake here. How/where can I get good benchmarks?
    private boolean hasSpinnerPrompt(String value) {
        if (value.contains("select")) {
            return true;
        }
        return false;
    }
}
