package com.example.zassmin.imagesearch.adapters;

import android.widget.AbsListView;

/**
 * Created by zassmin on 9/27/15.
 */
public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
    // the minimum amount of items to have below your current scroll position
    // before loading more
    private int visibleThreshold = 5;
    // the current offset index of data you have loaded
    private int currentPage = 0;
    // the total number of items in the database after the last load
    private int previousTotalItemCount = 0; // this increase by 8 when ready to load
    // true if we are still waiting for the last set of data to load
    private boolean loading = true;
    // sets the starting page index
    private int startingPageIndex = 0; // of the app or api?

    public EndlessScrollListener() {
    }

    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public EndlessScrollListener(int visibleThreshold, int startPage) {
        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = startPage;
        this.currentPage = startPage;
    }

    // this happens many times a second during a scroll, so be wary of the code you place here.
    // we are given on a few useful parameters to help us figure out if we need to load more data
    // first we check if we are waiting on the previous load to finish

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // if the total item count is zero and the previous isn't, assume the list is
        // invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) { this.loading = true; }
        }
        // if it's still loading, we check to see if the dataset count has changed, if so
        // we conclude it has finished loading and update the current page
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        // if it isn't currently loading, we check to see if we have breached the visibleThreshold
        // and need to reload more data. if we do need to reload some more data, we execute
        // onLoadMore to fetch data
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            loading = onLoadMore(currentPage, totalItemCount);
        }
    }

    // defines the process for actually loading more data based on the page
    // returns if more data is being loaded; returns false if there is no more data to load
    public abstract boolean onLoadMore(int page, int totalItemsCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // don't do anything on state changed
    }
}
