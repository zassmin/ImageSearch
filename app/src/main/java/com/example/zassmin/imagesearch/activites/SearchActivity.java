package com.example.zassmin.imagesearch.activites;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.zassmin.imagesearch.R;
import com.example.zassmin.imagesearch.adapters.EndlessScrollListener;
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
    private int page;

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
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // triggered only when new data needs to be appended to the list
                loadDataFromGoogleSearchApi(totalItemsCount);
                return true;
            }
        });
    }

    public void loadDataFromGoogleSearchApi(final int offset) {
        // check for internet connectivity
        if (!isNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.no_network_connectivity_notice), Toast.LENGTH_LONG).show();
            return;
        }

        page = offset;
        // TODO: stretch - use moreResultsUrl from response instead of reinitializing the searchParams
        RequestParams requestParams = GoogleImageClient.searchParams(imageFilter, etQuery.getText().toString(), offset);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(GoogleImageClient.SEARCH_URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    // when you make changes to the adapter, it modifies the underlying data for you
                    aImageResults.addAll(ImageResult.fromJsonArray(imageResultsJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (statusCode == 400) { // only doing 400 since other http error are unlikely for this request
                    Toast.makeText(getBaseContext(), getString(R.string.bad_request_message), Toast.LENGTH_LONG).show();
                    return;
                }
                // logging in case other errors occur
                Log.i("ERROR", "response: " + responseString + ", status: " + statusCode);
                super.onFailure(statusCode, headers, responseString, throwable);
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
        page = 0; // for subsequent searches
        aImageResults.clear(); // clear out old results
        loadDataFromGoogleSearchApi(page);
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

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
