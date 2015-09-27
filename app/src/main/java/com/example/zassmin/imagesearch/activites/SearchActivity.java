package com.example.zassmin.imagesearch.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.example.zassmin.imagesearch.R;
import com.example.zassmin.imagesearch.adapters.ImageResultsAdapter;
import com.example.zassmin.imagesearch.models.ImageFilter;
import com.example.zassmin.imagesearch.models.ImageResult;
import com.example.zassmin.imagesearch.utils.GoogleImageClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private static final int FILTER_REQUEST_CODE = 22;
    private ImageFilter imageFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpViews();
        // creates the image source
        imageResults = new ArrayList<>();
        // attach data source to an adapter
        aImageResults = new ImageResultsAdapter(this, imageResults);
        // link the adapter to the view (gridview)
        gvResults.setAdapter(aImageResults);
    }

    private void setUpViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() { // we are inside of an anonymous class like javascript
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // launch the image display activity
                // create an intent
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class); // where you want to put the intent
                // get the image result
                ImageResult result = imageResults.get(position);
                // pass image result into the intent
                i.putExtra("result", result); // needs to be packageable into the extra
                // ^^ to pass in the object, this is why we implement serializable,
                // it makes the object capable of being encoded - we want to make the ImageResult
                // object serializable
                // launch the new activity
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onImageSearch(View view) {
        RequestParams requestParams = GoogleImageClient.searchParams(imageFilter, etQuery.getText().toString());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(GoogleImageClient.SEARCH_URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageResultsJson = null; // TODO: better null handling
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear(); // NOTE: when we paginate, avoid calling clear, except on the initial result
                    // when you make changes to the adapter, it modifies the underlying data for you
                    aImageResults.addAll(ImageResult.fromJsonArray(imageResultsJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onSearchFilter(MenuItem item) {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivityForResult(intent, FILTER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == FILTER_REQUEST_CODE) {
            // set object on data
            imageFilter = ImageFilter.fromIntent(data);
            // TODO: will data persist when I leave searchActivity?
        }
    }
}
