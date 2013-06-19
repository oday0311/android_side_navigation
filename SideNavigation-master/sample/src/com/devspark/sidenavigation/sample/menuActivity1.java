package com.devspark.sidenavigation.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.theindex.CuzyAdSDK.*;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;


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
    public static final String EXTRA_WEBURL = "com.devspark.sidenavigation.sample.extra.weburl";


    private ImageView icon;
    private SideNavigationView sideNavigationView;

    private ArrayList<ArrayList<CuzyTBKItem>> dataSourceForAdapter;
    private ListView listView;
    public ArrayList<CuzyTBKItem> rawData = new ArrayList<CuzyTBKItem>();



    private AsyncImageLoader loader = null;
    private  cuzyAdapter adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        CuzyAdSDK.getInstance().setContext(this);
        CuzyAdSDK.getInstance().registerApp("200056","051a9e4652fc5b881dfc6ba74d3cd633");


        setContentView(R.layout.menuactivity1);
        listView = (ListView)findViewById(R.id.listView);
        listView.setDividerHeight(0);
        int layoutID = com.theindex.CuzyAdSDK.R.layout.cuzy_list_cell_2;

          testSimpleListView();
        //dataSourceForAdapter = generateDataSource();
       // adapter  = new ListAdapter(this,layoutID,dataSourceForAdapter);
        //listView.setAdapter(adapter);

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

        testCuzySDKfunction();
    }

    public void testSimpleListView()
    {


        //ListView listview= new ListView(this);
        loader = new AsyncImageLoader(getApplicationContext());

        //将图片缓存至外部文件中
        loader.setCache2File(true); //false
        //设置外部缓存文件夹
        loader.setCachedDir(this.getCacheDir().getAbsolutePath());

        adapter = new cuzyAdapter(rawData, this,loader,this);

        listView.setAdapter(adapter);

        //setContentView(listview);

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
            reloadListView();
        }

        @Override
        protected void onPreExecute(){
        }

        @Override
        protected void onProgressUpdate(Void... values){
        }
    }


    public void reloadListView(){
        //ListView listview= new ListView(this);
        adapter = new cuzyAdapter(rawData, this,loader,this);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
        //listView.deferNotifyDataSetChanged();
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

    private Bitmap getBitmap(CuzyTBKItem item){
        Bitmap bitmap = null;
        File file = new File(Utils.appExternalDirPath(),filename(item));
        if (file.exists()){
            try{
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            }catch (Exception e){
                Log.d("CuzyAdSDK", e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        return bitmap;
    }
    private String filename(CuzyTBKItem item){
        String result = null;
        if (item != null){
            int slash =  item.getItemImageURLString().lastIndexOf("/");
            return item.getItemImageURLString().substring(slash + 1);
        }
        return result;
    }

    private int indexForItem(CuzyTBKItem item)
    {

        for(int i = 0; i < rawData.size(); i ++){
            CuzyTBKItem it = rawData.get(i);
            if (it.getItemID() == item.getItemID()){
                return i;
            }
        }
        return -1;
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




    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
