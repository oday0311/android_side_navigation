package com.devspark.sidenavigation.sample;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-16
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */

import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    /**
     * 构造函数,初始化Adapter,将数据传入
     * @param items
     * @param context
     */
    public cuzyAdapter(ArrayList<CuzyTBKItem> items, Context context) {
        this.items = items;
        this.context = context;
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

        //获取控件
        ImageView bookImageView = (ImageView) view.findViewById(R.id.book_image);
        TextView bookNameTextView = (TextView) view.findViewById(R.id.book_name);
        TextView bookNoReadNumTextView = (TextView) view.findViewById(R.id.book_no_read_num);
        TextView bookLastTitleView = (TextView) view.findViewById(R.id.book_lasttitle);
        ImageView bookHasUpdateImageView = (ImageView) view.findViewById(R.id.book_has_update);
        //对控件赋值
        CuzyTBKItem bookData = (CuzyTBKItem) getItem(position);
        if (bookData != null) {


            Bitmap temp = getRes("");
            //bookImageView.setImageBitmap(Utils.getHttpBitmap(bookData.getItemImageURLString()));
            bookImageView.setImageBitmap(temp);

            bookNameTextView.setText(bookData.getItemName());
            bookLastTitleView.setText("更新至:" + bookData.getItemDescription());
        }

        return view;
    }

    public Bitmap getRes(String name) {

        Bitmap bMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        return bMap;
    }
}