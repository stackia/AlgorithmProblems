package com.teamabcd.algorithmproblems.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.teamabcd.algorithmproblems.R;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Project: Algorithm Problems
 * Created by: Stackia <jsq2627@gmail.com>
 * Date: 11/8/14
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    private View footerView;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean initialized = false;
    private boolean isLoading = false;

    public LoadMoreListView(Context context) {
        super(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (onLoadMoreListener == null) {
            return;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (onLoadMoreListener == null) {
            return;
        }
        if (!isLoading && firstVisibleItem + visibleItemCount >= totalItemCount) {
            isLoading = true;
            CircularProgressBar progressBar = (CircularProgressBar) footerView.findViewById(R.id.loadingProgressBar);
            progressBar.setVisibility(VISIBLE);
            onLoadMoreListener.onLoadMore();
        }
    }

    public OnLoadMoreListener getOnLoadMoreListener() {
        return onLoadMoreListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        if (!initialized) {
            initialized = true;
            LayoutInflater inflater = LayoutInflater.from(getContext());
            footerView = inflater.inflate(R.layout.list_load_more_footer_view, this, false);
            addFooterView(footerView);
            setOnScrollListener(this);
        }
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void forceLoadingViewVisible() {
        isLoading = true;
        CircularProgressBar progressBar = (CircularProgressBar) footerView.findViewById(R.id.loadingProgressBar);
        progressBar.setVisibility(VISIBLE);
    }

    public void notifyLoadMoreFinished() {
        isLoading = false;
        CircularProgressBar progressBar = (CircularProgressBar) footerView.findViewById(R.id.loadingProgressBar);
        progressBar.setVisibility(GONE);
    }

    public interface OnLoadMoreListener {
        public void onLoadMore();
    }
}
