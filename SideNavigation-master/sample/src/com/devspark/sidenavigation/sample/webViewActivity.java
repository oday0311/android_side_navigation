package com.devspark.sidenavigation.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.WebView;
import com.devspark.sidenavigation.SideNavigationView;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-5-19
 * Time: 上午11:03
 * To change this template use File | Settings | File Templates.
 */
public class webViewActivity extends Activity {


    public static final String EXTRA_WEBURL = "com.devspark.sidenavigation.sample.extra.weburl";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.webview);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
                .detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        if (getIntent().hasExtra(EXTRA_WEBURL)) {
            String title = getIntent().getStringExtra(EXTRA_WEBURL);
            //setTitle(title);
            WebView uiwebview = (WebView)findViewById(R.id.webView);
            uiwebview.getSettings().setJavaScriptEnabled(true);
            uiwebview.loadUrl(title);

            //uiwebview.getUrl();
            Log.v("huangzf", "the url is "+ title);
        }

    }
}