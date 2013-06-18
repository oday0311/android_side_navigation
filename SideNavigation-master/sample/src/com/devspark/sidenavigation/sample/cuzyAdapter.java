package com.devspark.sidenavigation.sample;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-16
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.theindex.CuzyAdSDK.CuzyTBKItem;


public class cuzyAdapter extends BaseAdapter {

    private ArrayList<CuzyTBKItem> items = null;
    private Context             context       = null;
    private AsyncImageLoader  imageLoader = null;
    /**
     * 构造函数,初始化Adapter,将数据传入
     * @param items
     * @param context
     */
    public cuzyAdapter(ArrayList<CuzyTBKItem> items, Context context, AsyncImageLoader imageLoader) {
        this.items = items;
        this.context = context;
        this.imageLoader = imageLoader;
        //for test

    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        //装载view
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.simplelistview, null);
        view.setBackgroundColor(Color.parseColor("#00000000"));

        //获取控件
        final ImageView itemImageView = (ImageView) view.findViewById(R.id.itemImageView);
        TextView itemName = (TextView) view.findViewById(R.id.itemName);
        TextView itemPrice = (TextView) view.findViewById(R.id.itemPrice);
        TextView itemSellCount = (TextView) view.findViewById(R.id.itemSellCount);

        //对控件赋值
        CuzyTBKItem cuzyItem = (CuzyTBKItem) getItem(position);
        if (cuzyItem != null) {
            final Bitmap temp = getRes("");
            //bookImageView.setImageBitmap(Utils.getHttpBitmap(bookData.getItemImageURLString()));

            //下载图片，第二个参数是否缓存至内存中
            imageLoader.downloadImage(cuzyItem.getItemImageURLString(), false, new AsyncImageLoader.ImageCallback() {
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
        if (cuzyItem != null) {
            final Bitmap temp = getRes("");
            //bookImageView.setImageBitmap(Utils.getHttpBitmap(bookData.getItemImageURLString()));
            //下载图片，第二个参数是否缓存至内存中
            imageLoader.downloadImage(cuzyItem.getItemImageURLString(), false, new AsyncImageLoader.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap, String imageUrl) {
                    if(bitmap != null){
                        itemImageView_right.setImageBitmap(bitmap);
                    }else{
                        //下载失败，设置默认图片
                        itemImageView_right.setImageBitmap(temp);
                    }
                }
            });

            itemImageView_right.setImageBitmap(temp);

            itemName_right.setText(cuzyItem.getItemName());
            itemPrice_right.setText("" + cuzyItem.getItemPromotionPrice()+"元");
            itemSellCount_right.setText("购买量"+cuzyItem.getTradingVolumeInThirtyDays());
        }





        return view;
    }

    public Bitmap getRes(String name) {

        Bitmap bMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        return bMap;
    }
}