package com.devspark.sidenavigation.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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
import com.umeng.analytics.MobclickAgent;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-5-12
 * Time: 下午6:24
 * To change this template use File | Settings | File Templates.
 */
public class menuActivity4 extends SherlockActivity implements ISideNavigationCallback{

    public static final String EXTRA_TITLE = "com.devspark.sidenavigation.sample.extra.MTGOBJECT";
    public static final String EXTRA_RESOURCE_ID = "com.devspark.sidenavigation.sample.extra.RESOURCE_ID";
    public static final String EXTRA_MODE = "com.devspark.sidenavigation.sample.extra.MODE";

    public static final String EXTRA_WEBURL = "com.devspark.sidenavigation.sample.extra.weburl";

    private ImageView icon;
    private SideNavigationView sideNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.menuactivity4);
        icon = (ImageView) findViewById(android.R.id.icon);
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);



        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
                .detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        Log.v("huangzf  ", "this is in the menu activity 1");
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

                //httpTest();
                // TestAdmob();
                //

                uploadFile();
            }
        });

        Button button2 = (Button) this.findViewById(R.id.button2);

        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

               ///todo
                click_xuanqutupian(v);
            }
        });




        AdView adView = new AdView(this, AdSize.BANNER, "a15195f21aafd4d");
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.myRelateLayout);
        layout.addView(adView);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        adView.setY(dm.heightPixels-200);
        adView.loadAd(new AdRequest());
    }

    public void TestAdmob()
    {
        AdView adView = new AdView(this, AdSize.BANNER, "a15195f21aafd4d");
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.myRelateLayout);
        layout.addView(adView);
        adView.loadAd(new AdRequest());

    }
    public void click_xuanqutupian(View source) {
        Intent intent = new Intent();
  /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
        //intent.setType("audio/*"); //选择音频
        //intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType("video/*;image/*");//同时选择视频和图片


  /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
  /* 取得相片后返回本画面 */
        startActivityForResult(intent, 1);
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




    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 选取图片的返回值

        if (requestCode == 1) {

            //

            if (resultCode == RESULT_OK) {

                Uri uri = data.getData();

                Cursor cursor = getContentResolver().query(uri, null, null,

                        null, null);

                cursor.moveToFirst();

                // String imgNo = cursor.getString(0); // 图片编号

                String imgPath = cursor.getString(1); // 图片文件路径

                String imgSize = cursor.getString(2); // 图片大小

                String imgName = cursor.getString(3); // 图片文件名


                picPathString = imgPath;
                //fileName = imgName;

                //fileSize = imgSize;

                // Log.e("uri", uri.toString());

                ContentResolver cr = this.getContentResolver();

                //try
                {

                    //Bitmap bitmap = BitmapFactory.decodeStream(cr
                    //        .openInputStream(uri));
                    Bitmap bitmap = this.getLocalBitmap(imgPath);

                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageBitmap(bitmap);
                  }
                //catch (IOException e)
                //{
                //}
                cursor.close();
            }

        }

        // 拍照的返回值

        if (requestCode == 2) {

            if (resultCode == RESULT_OK) {

                //


               String imgPath = data.getStringExtra("filePath");

                String fileName = data.getStringExtra("fileName");

               String fileSize = data.getStringExtra("fileSize");

                // 读取拍照所得的文件

                try {

                    Bitmap bitmap = this.getLocalBitmap(imgPath);


                    ImageView imageView = (ImageView) findViewById(R.id.imageView);

                    imageView.setImageBitmap(bitmap);

                } catch (Exception e) {

                    // TODO: handle exception

                }

                //

            }

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private Bitmap getLocalBitmap(String url) {
        try {
                FileInputStream fis = new FileInputStream(url);
                Bitmap returnBitmap =   BitmapFactory.decodeStream(fis);
                fis.close();

            return returnBitmap;
        } catch (IOException e ) {
            e.printStackTrace();
            return null;
        }
    }

    public String picPathString = "";
    private void uploadFile()
    {
        ////http://easynote.sinaapp.com/saestorage.php

        String actionUrl = "http://easynote.sinaapp.com/saestorage.php";
        String end ="\r\n";
        String twoHyphens ="--";
        String boundary ="*****";
        try
        {
            URL url =new URL(actionUrl);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
          /* 允许Input、Output，不使用Cache */          con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
          /* 设置传送的method=POST */
            con.setRequestMethod("POST");
          /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary="+boundary);
          /* 设置DataOutputStream */

            DataOutputStream ds =            new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "+ "name=\"uploaded\";filename=\""+"file.jpg" +"\""+ end);
            ds.writeBytes(end);
          /* 取得文件的FileInputStream */
            ImageView imageView = (ImageView) findViewById(R.id.imageView);

            imageView.setDrawingCacheEnabled(true);

            Bitmap bitmap;
            bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
            imageView.setDrawingCacheEnabled(false);

            String filepath1;
            if (picPathString.length() > 3)
            {
                   filepath1 = picPathString;
                ContentResolver cr = this.getContentResolver();


            }
            else
            {
                filepath1 = Environment.getExternalStorageDirectory() +File.separator + "tempPhoto.jpg";
                FileOutputStream fStream =new FileOutputStream(filepath1);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fStream);

                fStream.flush();
                fStream.close();
            }



            FileInputStream inputSteam = new FileInputStream(filepath1);


          /* 设置每次写入1024bytes */
            int bufferSize =1024;
            byte[] buffer =new byte[bufferSize];
            int length =-1;
          /* 从文件读取数据至缓冲区 */
            while((length = inputSteam.read(buffer)) !=-1)
            {
            /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
          /* close streams */
            inputSteam.close();
            ds.flush();
          /* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) !=-1 )
            {
                b.append( (char)ch );
            }
            Log.v("huangzf...",b.toString());
          /* 将Response显示于Dialog */
          //  showDialog("上传成功"+b.toString().trim());
          /* 关闭DataOutputStream */
            ds.close();
            is.close();
            String urlString = "http://shitu.baidu.com/i?objurl=" +b.toString()+"&rt=0&rn=10&ftn=searchstu&ct=1&stt=0&tn=baiduimage";
            startWebViewActivity(urlString);
        }
        catch(Exception e)
        {
            //showDialog("上传失败"+e);
        }
    }


    public void startWebViewActivity(String urlString)
    {
        Intent intent = new Intent(this, webViewActivity.class);
        intent.putExtra(EXTRA_WEBURL, urlString);

        // all of the other activities on top of it will be closed and this
        // Intent will be delivered to the (now on top) old activity as a
        // new Intent.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        // no animation of transition
        overridePendingTransition(0, 0);
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
