package com.nubytouch.crisiscare.ui.web;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.ui.JobActivity;
import com.nubytouch.crisiscare.ui.ToolbarDelegate;
import com.nubytouch.crisiscare.utils.Utils;

import java.nio.charset.Charset;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import timber.log.Timber;

public class BrowserActivity extends JobActivity
{
    private static final String TAG = "BrowserActivity";

    public static final String EXTRA_TITLE = "com.nubytouch.crisiscare.ui.web.title";
    public static final String EXTRA_URI   = "com.nubytouch.crisiscare.ui.web.uri";

    protected String uri;

    private ProgressBar        progress;
    private SwipeRefreshLayout refreshLayout;
    private WebView            webView;

    private boolean refreshable = true;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(getContentView() == 0 ? R.layout.activity_browser : getContentView());

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            new ToolbarDelegate(this).setup(false, getIntent().getStringExtra(EXTRA_TITLE));
            uri = getIntent().getStringExtra(EXTRA_URI);
        }
        else
        {
            new ToolbarDelegate(this).setup(false);
        }

        progress = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.webview);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.browser_swipe_refresh_layout);

        refreshLayout.setEnabled(refreshable);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                loadContent();
            }
        });

        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDefaultTextEncodingName(Charset.forName("UTF-8").displayName());

        webView.setVerticalScrollBarEnabled(true);
        webView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                //Timber.d("onLongClick: ");
                //Prevent selection -> security
                return true;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            try
            {
                // Use the API 11+ calls to disable the controls
                // Use a seperate class to obtain 1.6 compatibility
                new Runnable()
                {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    public void run()
                    {
                        webSettings.setDisplayZoomControls(false);
                    }
                }.run();

            }
            catch (Exception e)
            {
                Timber.e("onCreate: " + e);
            }
        }

        WebViewClient webClient = new WebViewClient()
        {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request)
            {
                //Timber.d("shouldIntercept uri %s", request.getUrl());
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onLoadResource(WebView view, String url)
            {
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                Timber.d("shouldOverrideUrlLoading " + url);
                if (url.startsWith("http:") || url.startsWith("https:"))
                {
                    return false;
                }
                else
                {
                    if (url.startsWith("mailto:"))
                    {
                        startActivity(Utils.newEmailIntent(BrowserActivity.this, MailTo.parse(url).getTo()));
                        view.reload();
                        return true;
                    }
                    else if (url.startsWith("tel:"))
                    {
                        startActivity(Utils.newPhoneIntent(BrowserActivity.this, url));
                    }
                    else
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));

                        try
                        {
                            startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            Timber.d("url not found " + e.getMessage());
                            // There may not be an app installed that match the uri
                        }
                    }

                    return true;
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Timber.d("onReceivedError " + failingUrl + " " + description);
                if (!isFinishing() /*&& !isDestroyed()*/)
                    Toast.makeText(BrowserActivity.this, description, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                Timber.d("onPageStarted " + url);
                if (url.startsWith("http"))
                {
                    refreshLayout.setRefreshing(true);
                }

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                refreshLayout.setRefreshing(false);
            }
        };

        webView.setWebViewClient(webClient);

        loadContent();
    }

    protected void loadContent()
    {
        Timber.d("loadContent " + uri);
        if (uri != null)
        {
            Timber.d("webview load " + uri);
            webView.loadUrl(uri);
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                finish();
                return true;
            }
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    protected int getContentView()
    {
        return 0;
    }
}
