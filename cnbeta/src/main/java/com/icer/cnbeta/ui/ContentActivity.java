package com.icer.cnbeta.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.icer.cnbeta.R;
import com.icer.cnbeta.app.AppConstants;
import com.icer.cnbeta.app.BaseActivity;
import com.icer.cnbeta.db.DBHelper;
import com.icer.cnbeta.manager.RequestManager;
import com.icer.cnbeta.util.TextViewUtil;
import com.icer.cnbeta.volley.NewsContentBean;
import com.icer.cnbeta.volley.entity.NewsContent;

/**
 * Created by icer on 2015-09-28.
 */
public class ContentActivity extends BaseActivity {

    public static final String TAG = ContentActivity.class.getCanonicalName();
    public static final int ID = TAG.hashCode();

    private Toolbar mToolbar;
    private TextView mTitleTv;
    private TextView mSourceTv;
    private TextView mPubtimeTv;
    private TextView mSummaryTv;
    private View mDividerV;
    private LinearLayout mContentLl;

    private String mSid;
    private String mTitle;
    private String mPubtime;
    private int mPosition;
    private boolean mIsCollected;
    private boolean mIsLoadFinish;

    private DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initData();
        initView();
        initActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsLoadFinish)
            LoadContentFromDB();
    }

    @Override
    protected void onStop() {
        super.onStop();
        RequestManager.getInstance().cancelRequest(TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content, menu);
        initCollectState();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_comments:
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtra(AppConstants.SID, mSid);
                goToActivity(intent);
                return true;

            case R.id.action_collect:
                updateCollectState();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        Intent intent = getIntent();
        mSid = intent.getStringExtra(AppConstants.SID);
        mTitle = intent.getStringExtra(AppConstants.TITLE);
        mPubtime = intent.getStringExtra(AppConstants.PUBTIME);
        mPosition = intent.getIntExtra(AppConstants.POSITION, -1);
        mDBHelper = new DBHelper(this);
        mIsCollected = mDBHelper.isNewsCollected(mSid);
    }

    private void initView() {
        mTitleTv = (TextView) findViewById(R.id.content_title_tv);
        mTitleTv.setText(mTitle);
        mSourceTv = (TextView) findViewById(R.id.content_source_tv);
        mPubtimeTv = (TextView) findViewById(R.id.content_pubtime_tv);
        mPubtimeTv.setText(mPubtime);
        mSummaryTv = (TextView) findViewById(R.id.content_summary_tv);
        mDividerV = findViewById(R.id.content_divider);
        mContentLl = (LinearLayout) findViewById(R.id.content_ll);
    }

    private void initActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.subtitle_content);
    }

    private void LoadContentFromDB() {
        final NewsContent newsContent = mDBHelper.getLocalNewsContent(mSid);
        if (newsContent == null)
            requestContent();
        else
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fillContentData(newsContent);
                }
            }, 300);
    }

    private void requestContent() {
        RequestManager.getInstance().requestContent(mSid, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                logI(TAG, s);
                NewsContentBean newsContentBean = JSON.parseObject(s, NewsContentBean.class);
                mDBHelper.saveNewsContent(newsContentBean.result);
                fillContentData(newsContentBean.result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showToast(AppConstants.HINT_LOADING_FAILED);
            }
        }, TAG);
    }

    private void fillContentData(NewsContent newsContent) {
        logI(TAG, newsContent.toString());
        TextViewUtil.setTextAfterColon(ContentActivity.this, mSourceTv, newsContent.source, true);
        mDividerV.setVisibility(View.VISIBLE);
        updateSummary(newsContent.hometext);
        addWebView2ContentLayout(new String(newsContent.bodytext));
    }

    private void initCollectState() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((ActionMenuItemView) mToolbar.findViewById(R.id.action_collect)).setIcon(getResources().getDrawable(mIsCollected ? R.drawable.ic_uncollect : R.drawable.ic_collect));
            }
        }, 100);
    }

    private void updateCollectState() {
        mDBHelper.updateNewsIsCollected(mSid, !mIsCollected);
        if (mIsCollected) {
            ((ActionMenuItemView) mToolbar.findViewById(R.id.action_collect)).setIcon(getResources().getDrawable(R.drawable.ic_collect));
            if (mPosition != -1)
                setResult(AppConstants.RESUET_CODE_UNCOLLECT + mPosition);
        } else {
            ((ActionMenuItemView) mToolbar.findViewById(R.id.action_collect)).setIcon(getResources().getDrawable(R.drawable.ic_uncollect));
            if (mPosition != -1)
                setResult(AppConstants.RESUET_CODE_COLLECT);
        }
        mIsCollected = !mIsCollected;
    }

    private void updateSummary(String text) {
        mSummaryTv.setText(Html.fromHtml(text));
    }

    private void addWebView2ContentLayout(String data) {
        String html = data;
        WebView wv = new WebView(this);
        wv.setWebChromeClient(new WebChromeClient());
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wv.loadDataWithBaseURL(null, fixWebViewImage(fixWebViewWH(fixWebViewHW(html))), "text/html", "utf-8", null);
        mContentLl.addView(wv);
        mIsLoadFinish = true;
    }

    private String fixWebViewHW(String htmlText) {
        String res = htmlText;
        String[] segments = res.split("height=\"" + "[1-9][0-9]{2}\"" + " width=\"" + "[1-9][0-9]{2}\"");
        if (segments.length > 1) {
            String fixedHtmlText = segments[0];
            int width = (int) (mTitleTv.getMeasuredWidth() - dp2px(13.34f));
            for (int i = 0; i < segments.length - 1; i++) {
                String param = res.substring(fixedHtmlText.length() - 1, fixedHtmlText.length() + 25);
                String[] hw = param.split("=\"");
                float tHeight = Float.parseFloat(hw[1].substring(0, hw[1].indexOf("\"")));
                float tWidth = Float.parseFloat(hw[2].substring(0, hw[2].indexOf("\"")));
                logI(TAG + " fixWebViewHW()", "//" + param + "\\\\");
                logI(TAG + " fixWebViewHW()", "height:" + tHeight + " width:" + tWidth);
                int fixedH = (int) (tHeight / (tWidth / (width / dp2px(1f))));
                logI(TAG + " fixWebViewHW()", "fixed height:" + fixedH + " fixed width:" + ((int) (width / dp2px(1f))));
                fixedHtmlText += "height=\"" + fixedH + "\"" + " width=\"" + ((int) (width / dp2px(1f))) + "\"" + segments[i + 1];
            }
            res = fixedHtmlText;
        }
        return res;
    }

    private String fixWebViewWH(String htmlText) {
        String res = htmlText;
        String[] segments = res.split("width=\"" + "[1-9][0-9]{2}\"" + " height=\"" + "[1-9][0-9]{2}\"");
        if (segments.length > 1) {
            String fixedHtmlText = segments[0];
            int width = (int) (mTitleTv.getMeasuredWidth() - dp2px(13.34f));
            for (int i = 0; i < segments.length - 1; i++) {
                String param = res.substring(fixedHtmlText.length() - 1, fixedHtmlText.length() + 25);
                String[] hw = param.split("=\"");
                float tWidth = Float.parseFloat(hw[1].substring(0, hw[1].indexOf("\"")));
                float tHeight = Float.parseFloat(hw[2].substring(0, hw[2].indexOf("\"")));
                logI(TAG + " fixWebViewWH()", "//" + param + "\\\\");
                logI(TAG + " fixWebViewWH()", "height:" + tHeight + " width:" + tWidth);
                int fixedH = (int) (tHeight / (tWidth / (width / dp2px(1f))));
                logI(TAG + " fixWebViewWH()", "fixed height:" + fixedH + " fixed width:" + ((int) (width / dp2px(1f))));
                fixedHtmlText += "height=\"" + fixedH + "\"" + " width=\"" + ((int) (width / dp2px(1f))) + "\"" + segments[i + 1];
            }
            res = fixedHtmlText;
        }
        return res;
    }

    private String fixWebViewImage(String htmlText) {
        String res = htmlText;
        int width = (int) (mTitleTv.getMeasuredWidth() - dp2px(13.34f));
        res = res.replace("<img", "<img width=\"" + ((int) (width / dp2px(1f))) + "\"");
        logI(TAG, res);
        return res;
    }
}
