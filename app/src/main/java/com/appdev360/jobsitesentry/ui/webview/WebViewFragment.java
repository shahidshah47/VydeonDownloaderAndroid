package com.appdev360.jobsitesentry.ui.webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by abubaker on 3/22/18.
 */

public class WebViewFragment extends BaseFragment {

    @BindView(R.id.web_view)
    WebView webView;
    Unbinder unbinder;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private String url;
    private String title;
    private SentryLocationActivity activity;

    @Override
    public void initViews(View parentView, Bundle saveInstanceState) {

        unbinder = ButterKnife.bind(this, parent);
        activity = (SentryLocationActivity) getActivity();

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        progressBar.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new myWebClient());

    }

    @Override
    public int getLayoutId() {
        return R.layout.webview_fragment_layout;
    }

    @Override
    public void updateFragmentReference() {

    }

    @Override
    public void updateActionState(boolean action, View v) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressBar != null)
            progressBar.setVisibility(View.GONE);

        }
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setTitle(String title){
        this.title = title;
    }
}
