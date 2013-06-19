package com.devspark.sidenavigation.sample;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-16
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.theindex.CuzyAdSDK.CuzyTBKItem;


public class cuzyAdapter extends BaseAdapter {

    private ArrayList<CuzyTBKItem> items = null;
    private Context             context       = null;
    private AsyncImageLoader  imageLoader = null;
    private Activity thisActivity = null;

    public static final String EXTRA_WEBURL = "com.devspark.sidenavigation.sample.extra.weburl";
    /**
     * 构造函数,初始化Adapter,将数据传入
     * @param items
     * @param context
     */
    public cuzyAdapter(ArrayList<CuzyTBKItem> items, Context context, AsyncImageLoader imageLoader, Activity adapterActivity) {
        this.items = items;
        this.context = context;
        this.imageLoader = imageLoader;
        this.thisActivity = adapterActivity;

        //for test

    }

    @Override
    public int getCount() {

        //using the 2 items model
        return items == null ? 0 : items.size()/2;
    }

    @Override
    public Object getItem(int position) {
        return items == null ? null : items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //装载view
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        final View view = layoutInflater.inflate(R.layout.simplelistview, null);
        view.setBackgroundColor(Color.parseColor("#00000000"));

        RelativeLayout leftLayout = (RelativeLayout)view.findViewById(R.id.relativeLayoutLeft);

        ///the cellimage is 140 pix , we using 40 pix as the middle empty
        int leftPadding = (Utils.getWindowWith(thisActivity) - 140*2 - 40)/2;

        leftLayout.setPadding(leftPadding,0,0,0);

        RelativeLayout rightLayout = (RelativeLayout)view.findViewById(R.id.relativeLayoutRight);

        ///the cellimage is 140 pix , we using 40 pix as the middle empty
        int rightPadding = leftPadding+140+40;

        rightLayout.setPadding(rightPadding,0,0,0);

        Log.i("cuzySDK", ""+ leftPadding + "   "+ rightPadding);

        //获取控件
        final ImageView itemImageView = (ImageView) view.findViewById(R.id.itemImageView);
        TextView itemName = (TextView) view.findViewById(R.id.itemName);
        TextView itemPrice = (TextView) view.findViewById(R.id.itemPrice);
        TextView itemSellCount = (TextView) view.findViewById(R.id.itemSellCount);

        //对控件赋值
        final  CuzyTBKItem cuzyItem = (CuzyTBKItem) getItem(2*position);
        if (cuzyItem != null) {
            final Bitmap temp = getRes("");
            //bookImageView.setImageBitmap(Utils.getHttpBitmap(bookData.getItemImageURLString()));

            //下载图片，第二个参数是否缓存至内存中
            imageLoader.downloadImage(cuzyItem.getItemImageURLString(), true, new AsyncImageLoader.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap, String imageUrl) {
                    if(bitmap != null){
                        itemImageView.setImageBitmap(bitmap);
                    }else{
                        //下载失败，设置默认图片
                        itemImageView.setImageBitmap(temp);
                    }
                }
            });


            itemImageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.i("Test", "点击");
                    menuActivity1 temp = (menuActivity1)thisActivity;
                    temp.startWebViewActivity("http://"+cuzyItem.getItemClickURLString());
                }
            }
            );
            itemName.setText(cuzyItem.getItemName());
            itemPrice.setText("" + cuzyItem.getItemPromotionPrice()+"元");
            itemSellCount.setText("购买量"+cuzyItem.getTradingVolumeInThirtyDays());
        }
        //获取控件
       final  ImageView itemImageView_right = (ImageView) view.findViewById(R.id.itemImageView_right);
        TextView itemName_right = (TextView) view.findViewById(R.id.itemName_right);
        TextView itemPrice_right = (TextView) view.findViewById(R.id.itemPrice_right);
        TextView itemSellCount_right = (TextView) view.findViewById(R.id.itemSellCount_right);

        //对控件赋值
       final CuzyTBKItem cuzyItem_right = (CuzyTBKItem) getItem(2*position+1);

        if (cuzyItem_right != null) {
            final Bitmap tempRight = getRes("");
            //bookImageView.setImageBitmap(Utils.getHttpBitmap(bookData.getItemImageURLString()));
            //下载图片，第二个参数是否缓存至内存中
            imageLoader.downloadImage(cuzyItem_right.getItemImageURLString(), true, new AsyncImageLoader.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap, String imageUrl) {
                    if(bitmap != null){
                        itemImageView_right.setImageBitmap(bitmap);
                    }else{
                        //下载失败，设置默认图片
                        itemImageView_right.setImageBitmap(tempRight);
                    }
                }
            });
            itemImageView_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    menuActivity1 temp = (menuActivity1)thisActivity;
                    temp.startWebViewActivity("http://"+cuzyItem_right.getItemClickURLString());
                    Log.i("Test", "点击");
                }


            });

            itemImageView_right.setImageBitmap(tempRight);

            itemName_right.setText(cuzyItem_right.getItemName());
            itemPrice_right.setText("" + cuzyItem_right.getItemPromotionPrice()+"元");
            itemSellCount_right.setText("购买量"+cuzyItem_right.getTradingVolumeInThirtyDays());
        }





        return view;
    }

    public Bitmap getRes(String name) {

        Bitmap bMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon);
        return bMap;
    }


}