package com.devspark.sidenavigation.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.theindex.CuzyAdSDK.CuzyAdSDK;
import com.theindex.CuzyAdSDK.CuzyTBKItem;
import com.umeng.analytics.MobclickAgent;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-5-12
 * Time: 下午6:24
 * To change this template use File | Settings | File Templates.
 */
public class menuActivity1 extends SherlockActivity implements ISideNavigationCallback{

    public static final String EXTRA_TITLE = "com.devspark.sidenavigation.sample.extra.MTGOBJECT";
    public static final String EXTRA_RESOURCE_ID = "com.devspark.sidenavigation.sample.extra.RESOURCE_ID";
    public static final String EXTRA_MODE = "com.devspark.sidenavigation.sample.extra.MODE";

    private ImageView icon;
    private SideNavigationView sideNavigationView;

    public ArrayList<CuzyTBKItem> rawData = new ArrayList<CuzyTBKItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
                .detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        Log.v("huangzf  ", "this is in the menu activity 1");


        CuzyAdSDK.getInstance().setContext(this);
        CuzyAdSDK.getInstance().registerApp("200056","051a9e4652fc5b881dfc6ba74d3cd633");


        setContentView(R.layout.menuactivity1);
        icon = (ImageView) findViewById(android.R.id.icon);
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);



        if (getIntent().hasExtra(EXTRA_TITLE)) {
            String title = getIntent().getStringExtra(EXTRA_TITLE);
            int resId = getIntent().getIntExtra(EXTRA_RESOURCE_ID, 0);
            setTitle(title);
             //icon.setImageResource(resId);
            sideNavigationView.setMode(getIntent().getIntExtra(EXTRA_MODE, 0) == 0 ? SideNavigationView.Mode.LEFT : SideNavigationView.Mode.RIGHT);
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button = (Button) this.findViewById(R.id.searchButton);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  httpTest();
                testCuzySDKfunction();


            }
        });


        final EditText t = (EditText)findViewById(R.id.editText);
        t.setOnClickListener(new EditText.OnClickListener(){
            @Override
            public void onClick(View v) {

                t.setText("");
            }
        });

        ProgressBar progressHorizontal2 = (ProgressBar) findViewById(R.id.progressBar);
        progressHorizontal2.setVisibility(View.INVISIBLE);

        AdView adView = new AdView(this, AdSize.BANNER, "a15195f21aafd4d");
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.myRelateLayout);
        layout.addView(adView);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        adView.setY(dm.heightPixels-200);
        adView.loadAd(new AdRequest());
    }

    public void testCuzySDKfunction()
    {

        new LongOperation().execute("");

    }

    private class LongOperation extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String...params){

            rawData = CuzyAdSDK.getInstance().fetchRawItems("6", "", 0);
            Log.d("cuzy.com: ", "return of raw data: Thindex:  " + rawData.size());

            return"Executed";
        }

        @Override
        protected void onPostExecute(String result){
            //TextView txt =(TextView) findViewById(R.id.output);
            //txt.setText("Executed");// txt.setText(result);
            //might want to change "executed" for the returned string passed into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute(){
        }

        @Override
        protected void onProgressUpdate(Void... values){
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main_menu, menu);
        if (sideNavigationView.getMode() == SideNavigationView.Mode.RIGHT) {
            menu.findItem(R.id.mode_right).setChecked(true);
        } else {
            menu.findItem(R.id.mode_left).setChecked(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                sideNavigationView.toggleMenu();
                break;
            case R.id.mode_left:
                item.setChecked(true);
                sideNavigationView.setMode(SideNavigationView.Mode.LEFT);
                break;
            case R.id.mode_right:
                item.setChecked(true);
                sideNavigationView.setMode(SideNavigationView.Mode.RIGHT);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onSideNavigationItemClick(int itemId) {
        switch (itemId) {
            case R.id.side_navigation_menu_item1:
                invokeActivity1(getString(R.string.title1), R.drawable.ic_android1);
                break;

            case R.id.side_navigation_menu_item2:
                invokeActivity2(getString(R.string.title2), R.drawable.ic_android2);
                break;

            case R.id.side_navigation_menu_item3:
                invokeActivity3(getString(R.string.title3), R.drawable.ic_android3);
                break;

            case R.id.side_navigation_menu_item4:
                invokeActivity4(getString(R.string.title4), R.drawable.ic_android4);
                break;

//            case R.id.side_navigation_menu_item5:
//                invokeActivity5(getString(R.string.title5), R.drawable.ic_android5);
//                break;

            default:
                return;
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        // hide menu if it shown
        if (sideNavigationView.isShown()) {
            sideNavigationView.hideMenu();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Start activity from SideNavigation.
     *
     * @param title title of Activity
     * @param resId resource if of background image
     */
    private void invokeActivity(String title, int resId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_RESOURCE_ID, resId);
        intent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == SideNavigationView.Mode.LEFT ? 0 : 1);

        // all of the other activities on top of it will be closed and this
        // Intent will be delivered to the (now on top) old activity as a
        // new Intent.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        // no animation of transition
        overridePendingTransition(0, 0);
    }

    public int ProgressBarHidder = 0;
    public String getContentStringFromResponse(String responseString)
    {

        Pattern pattern = Pattern.compile("</code>(.*)</div>");

        Matcher matcher = pattern.matcher(responseString);

       if (matcher.find()){
            Log.v("huangzf  ", matcher.group());
           return matcher.group();
       }
       else
       {
           return "对不起，查询失败，请检查你输入的号码";
       }


    }
    public void httpTest()
    {

        ProgressBar progressHorizontal2 = (ProgressBar) findViewById(R.id.progressBar);
        progressHorizontal2.setVisibility(View.VISIBLE);

        try{

            EditText editView = (EditText) findViewById(R.id.editText);
            String phoneString = editView.getText().toString() ;

            if (phoneString.length()<11)
            {
                Dialog alertDialog = new AlertDialog.Builder(this).
                        setTitle("提示").
                        setMessage("请输入正确的手机号码").
                        setIcon(R.drawable.ic_launcher).
                        create();
                alertDialog.show();
            }

              String responseString = getResultForHttpGet(phoneString);
              String resultString = getContentStringFromResponse(responseString);

              resultString = resultString.replaceFirst("</code>","");
              resultString =resultString.replaceFirst("</div>", "");
              resultString= resultString.replaceAll("&nbsp;", "");
              TextView resultView = (TextView) findViewById(R.id.textView);
              resultView.setText(resultString);

          }
          catch (IOException e)
          {

          }

        progressHorizontal2.setVisibility(View.INVISIBLE);
         Log.v("huangzf ", "in http Test");



    }

    private void invokeActivity1(String title, int resId ) {

        Intent intent = new Intent(this, menuActivity1.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_RESOURCE_ID, resId);
        intent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == SideNavigationView.Mode.LEFT ? 0 : 1);

        // all of the other activities on top of it will be closed and this
        // Intent will be delivered to the (now on top) old activity as a
        // new Intent.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        // no animation of transition
        overridePendingTransition(0, 0);
    }

    private void invokeActivity2(String title, int resId ) {

        Intent intent = new Intent(this, menuActivity2.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_RESOURCE_ID, resId);
        intent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == SideNavigationView.Mode.LEFT ? 0 : 1);

        // all of the other activities on top of it will be closed and this
        // Intent will be delivered to the (now on top) old activity as a
        // new Intent.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        // no animation of transition
        overridePendingTransition(0, 0);
    }
    private void invokeActivity3(String title, int resId ) {

        Intent intent = new Intent(this, menuActivity3.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_RESOURCE_ID, resId);
        intent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == SideNavigationView.Mode.LEFT ? 0 : 1);

        // all of the other activities on top of it will be closed and this
        // Intent will be delivered to the (now on top) old activity as a
        // new Intent.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        // no animation of transition
        overridePendingTransition(0, 0);
    }
    private void invokeActivity4(String title, int resId ) {

        Intent intent = new Intent(this, menuActivity4.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_RESOURCE_ID, resId);
        intent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == SideNavigationView.Mode.LEFT ? 0 : 1);

        // all of the other activities on top of it will be closed and this
        // Intent will be delivered to the (now on top) old activity as a
        // new Intent.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        // no animation of transition
        overridePendingTransition(0, 0);
    }
    private void invokeActivity5(String title, int resId ) {

        Intent intent = new Intent(this, menuActivity5.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_RESOURCE_ID, resId);
        intent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == SideNavigationView.Mode.LEFT ? 0 : 1);

        // all of the other activities on top of it will be closed and this
        // Intent will be delivered to the (now on top) old activity as a
        // new Intent.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        // no animation of transition
        overridePendingTransition(0, 0);
    }



    public String getResultForHttpGet(String phoneNum) throws ClientProtocolException,  IOException {
        //服务器  ：服务器项目  ：servlet名称

        /////////http://www.ip.cn/getm.php?q=13911650018&from=web

        String path="http://www.ip.cn/getm.php?q=";
        String uri=path+phoneNum+"&from=web";
        //name:服务器端的用户名，pwd:服务器端的密码
        //注意字符串连接时不能带空格

        String result="";

        HttpGet httpGet=new HttpGet(uri);//编者按：与HttpPost区别所在，这里是将参数在地址中传递
        HttpResponse response=new DefaultHttpClient().execute(httpGet);
        if(response.getStatusLine().getStatusCode()==200){
            HttpEntity entity=response.getEntity();
            result= EntityUtils.toString(entity, HTTP.UTF_8);
        }

        Log.v("huangzf, " , "this is the return from http"+result);
        return result;
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
