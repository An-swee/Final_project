package com.example.steven.finalproject2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by steven on 2017/12/30.
 */

public class MyAdapter extends BaseAdapter {


    private ArrayList<HashMap<String, Object>> mAppList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] keyString;
    private int[] valueViewID;
    private ItemView itemView;

    private class ItemView {
        TextView item_book_name ;
        TextView item_store_city ;
        TextView item_address ;
        TextView item_phone ;
        TextView item_open ;
        TextView item_goto;
        ImageView item_image ;
        ImageView item_facebook;
        ImageView item_map;
        ImageView item_web;
        ImageView item_return;
    }

    public MyAdapter(Context context, ArrayList<HashMap<String, Object>> appList, int resource, String[] from, int[] to) {
        mAppList = appList;
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return 0;
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //return null;
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        //return 0;
        return position;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //return null;
        if (convertView != null) {
            itemView = (ItemView) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.book_show, null);
            itemView = new ItemView();
            itemView.item_book_name = (TextView) convertView.findViewById(R.id.item_book_name);
            itemView.item_store_city = (TextView)convertView.findViewById(R.id.item_store_city);
            itemView.item_address = (TextView)convertView.findViewById(R.id.item_address);
            itemView.item_phone = (TextView) convertView.findViewById(R.id.item_phone);
            itemView.item_open = (TextView) convertView.findViewById(R.id.item_open);
            itemView.item_goto = (TextView) convertView.findViewById(R.id.item_goto);
            itemView.item_image = (ImageView) convertView.findViewById(R.id.item_image);
            itemView.item_facebook = (ImageView) convertView.findViewById(R.id.item_facbook);
            itemView.item_map = (ImageView) convertView.findViewById(R.id.item_map);
            itemView.item_web = (ImageView) convertView.findViewById(R.id.item_web);
            itemView.item_return = (ImageView) convertView.findViewById(R.id.item_return);
            convertView.setTag(itemView);
        }

        HashMap<String, Object> appInfo = mAppList.get(position);
        if (appInfo != null) {

            if( ! ((String)appInfo.get(keyString[2])).equals("") )
                new DownloadImageTask(itemView.item_image).execute((String)appInfo.get(keyString[2]));
            else
                itemView.item_image.setImageDrawable(itemView.item_image.getResources().getDrawable(R.drawable.no_store_url));

            itemView.item_book_name.setText((String)appInfo.get(keyString[0]));
            itemView.item_store_city.setText((String)appInfo.get(keyString[1]));
            //itemView.item_image.setImageDrawable(itemView.item_image.getResources().getDrawable(R.drawable.no_store_url));
            itemView.item_address.setText((String)appInfo.get(keyString[3]));
            itemView.item_phone.setText((String)appInfo.get(keyString[4]));
            itemView.item_open.setText((String)appInfo.get(keyString[5]));
            itemView.item_goto.setText((String)appInfo.get(keyString[6]));
            final String facebook_url = (String)appInfo.get(keyString[7]);
            final String web_url = (String)appInfo.get(keyString[8]);
            MapsActivity.map_store_name = (String)appInfo.get(keyString[0]);
            MapsActivity.map_store_add = (String)appInfo.get(keyString[3]);
            //Log.d("test",(String) appInfo.get(keyString[9]));
            MapsActivity.map_store_latitude = Double.valueOf((String)appInfo.get(keyString[9]));
            MapsActivity.map_store_longitude = Double.valueOf((String)appInfo.get(keyString[10]));

            itemView.item_facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(facebook_url.indexOf("http") != -1){
                        Uri uri=Uri.parse(facebook_url);
                        Intent go =new Intent(Intent.ACTION_VIEW,uri);

                    }
                    else
                        Toast.makeText(MainActivity.mActivity, "此書店無ＦＡＣＥＢＯＯＫ", Toast.LENGTH_SHORT).show();
                }
            });
            itemView.item_web.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!web_url.equals("")) {
                        Uri uri = Uri.parse(web_url);
                        Intent go = new Intent(Intent.ACTION_VIEW, uri);
                        MainActivity.mActivity.startActivity(go);
                    }
                    else
                        Toast.makeText(MainActivity.mActivity, "此書店無網頁！", Toast.LENGTH_SHORT).show();
                }
            });
            itemView.item_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    MainActivity.mActivity.startActivity(new Intent(MainActivity.mActivity,MapsActivity.class));
                }
            });
            itemView.item_return.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.mActivity.startActivity(new Intent(MainActivity.mActivity,MainActivity.class));
                }
            });
        }
        return convertView;
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}

