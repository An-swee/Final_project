package com.example.steven.finalproject2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mActivity;
    static String get_date;
    ListView list;
    List my_list;
    List my_info;
    SearchView searchView;
    MyAdapter adapter;
    CustomAdapter mCustomAdapter;
    ArrayList<HashMap<String, Object>> Item = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, Object>> serach = new ArrayList<HashMap<String, Object>>();
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity=this;
        list = (ListView) findViewById(R.id.list);

        searchView = (SearchView) findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);// 關閉icon切換
        searchView.setFocusable(false); // 不要進畫面就跳出輸入鍵盤
        setSearch_function();
        my_list = new ArrayList<>();
        my_info = new ArrayList<>();
        // 給List增加測試數據
        //for(int i=0;i<1000;i++){
        //    my_list.add("no: "+i);
        //}
        mCustomAdapter = new CustomAdapter(this,my_list,my_info);
        list.setAdapter(mCustomAdapter);
        // 給listview 設置過濾器
        list.setTextFilterEnabled(true);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String a =mCustomAdapter.getStore_name(position);
                //Log.d("tewq",a);
                for (int i = 0 ; i < 55 ; i++) {
                    //Log.d("test",""+Item.get(0));
                    if ( (""+Item.get(i)).indexOf(a) != -1 )
                    {
                        //Log.d("test",""+i);
                        Toast.makeText(MainActivity.this,"查詢"+ a + "詳細資料",Toast.LENGTH_LONG).show();
                        serach.add(Item.get(i));
                        adapter = new MyAdapter(MainActivity.this,
                                serach,
                                R.layout.book_show,
                                new String[]{"name" , "cityName" , "representImage" , "address" , "phone" , "openTime" , "arriveWay" , "facebook" , "website" , "latitude" , "longitude"},
                                new int[]{R.id.item_book_name , R.id.item_store_city , R.id.item_image , R.id.item_address , R.id.item_phone , R.id.item_open , R.id.item_goto});
                        list.setAdapter(adapter);
                        i = 56;
                    }
                }
            }
        });
        //String abc = "{\"result\":{\"offset\":0,\"limit\":10000,\"count\":28153,\"sort\":\"\",\"results\":[{\"_id\":\"1\",\"vector\":\"999\",\"showLat\":\"25.063588\",\"showLon\":\"121.501942\",\"stopLocationId\":";
        //Log.d("asd",abc);
        //String[] c = abc.split("\\[");
        //Log.d("asd",c[0]);
        //abc = abc.replace(c[0],"");
        //Log.d("asd",abc);
        //String a = "{address=汀州路3段130號, arriveWay=捷運公館站1號出口, longitude=121.533744, website=, phone=886-2-23682260, cityName=臺北市  中正區, mainTypeName=獨立書店, openTime=週一到週日9:30-21:30, representImage=http://cloud.culture.tw/h_upload_ccacloud/festival/image/A0/B0/C0/D0/E0/F2/6429c0f0-ceea-4c53-94d1-d559f2c5d14a.jpg, facebook=, latitude=25.013919, name=公館舊書城, intro=}\n";
        //Log.d("123123123123",""+a.indexOf("pib"));

        new GoodTask().execute("https://cloud.culture.tw/frontsite/trans/" +
                "emapOpenDataAction.do?method=exportEmapJson&typeId=M");
    }
    class GoodTask extends AsyncTask<String, Integer, String> {
        // <傳入參數, 處理中更新介面參數, 處理後傳出參數>
        private static final int TIME_OUT = 1000;

        String jsonString1 = "";

        @Override
        protected String doInBackground(String... countTo) {
            // TODO Auto-generated method stub
            // 再背景中處理的耗時工作
            try {
                HttpURLConnection conn = null;
                URL url = new URL(countTo[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.connect();
                // 讀取資料
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "UTF-8"));
                jsonString1 = reader.readLine();
                reader.close();

                if (Thread.interrupted()) {
                    throw new InterruptedException();

                }
                if (jsonString1.equals("")) {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "網路中斷" + e;
            }

            return jsonString1;
        }

        //宣告要取出資料名稱
        String[] name;
        String[] representImage;
        String[] intro;
        String[] address;
        double[] longitude;
        double[] latitude;
        String[] openTime;
        String[] phone;
        String[] facebook;
        String[] website;
        String[] arriveWay;
        String[] mainTypeName;
        String[] cityName;
        String[] get_http = new String[13];
        String[] list_item ;
        public void onPostExecute(String result) {
            super.onPreExecute();
            // 背景工作處理完"後"需作的事

            // JSONObject obj;
            try {
                //  obj = new JSONObject(result);
                //如果傳來的json是物件，且有給名字比如:data:{}才會使用obj.getString(“data”)
                // String data = obj.getString("data");
                //這裡傳來的是JSON陣列，因此要把剛剛撈到的字串轉換為JSON陣列
                JSONArray dataArray = new JSONArray(result);
                //先宣告tital跟id的字串陣列來存放等等要拆解的資料
                name = new String[dataArray.length()];
                representImage = new String[dataArray.length()];
                intro = new String[dataArray.length()];
                address = new String[dataArray.length()];
                longitude = new double[dataArray.length()];
                latitude = new double[dataArray.length()];
                openTime = new String[dataArray.length()];
                phone = new String[dataArray.length()];
                facebook = new String[dataArray.length()];
                website = new String[dataArray.length()];
                arriveWay = new String[dataArray.length()];
                mainTypeName = new String[dataArray.length()];
                cityName = new String[dataArray.length()];
                list_item = new String[dataArray.length()];
                get_http[0] = "name";
                get_http[1] = "representImage";
                get_http[2] = "intro";
                get_http[3] = "address";
                get_http[4] = "longitude";
                get_http[5] = "latitude";
                get_http[6] = "openTime";
                get_http[7] = "phone";
                get_http[8] = "facebook";
                get_http[9] = "website";
                get_http[10] = "arriveWay";
                get_http[11] = "mainTypeName";
                get_http[12] = "cityName";

                //db = openOrCreateDatabase("expense.db", MODE_PRIVATE, null);
                //Cursor c = db.rawQuery("SELECT * FROM main.exp ",null);
                //c.moveToFirst();

                //因為data陣列裡面有好多個JSON物件，因此利用for迴圈來將資料抓取出來
                //因為不知道data陣列裡有多少物件，因此我們用.length()這個方法來取得物件的數量
                for (int i = 0; i < 55; i++) {
                    //接下來這兩行在做同一件事情，就是把剛剛JSON陣列裡的物件抓取出來
                    //並取得裡面的字串資料
                    //dataArray.getJSONObject(i)這段是在講抓取data陣列裡的第i個JSON物件
                    //抓取到JSON物件之後再利用.getString(“欄位名稱”)來取得該項value
                    HashMap<String, Object> map = new HashMap<String, Object>();

                    for (int j = 0 ; j <= 12 ; j++)
                    {
                        get_date = dataArray.getJSONObject(i).getString(get_http[j]);
                        if( j == 0)
                        {
                            my_list.add(get_date);
                        }
                        if( j == 2)
                        {
                            my_info.add(get_date);
                        }

                        map.put(get_http[j], get_date);
                    }
                    Item.add(map);
                    //Log.d("item",""+map);
                }
                adapter = new MyAdapter(MainActivity.this,
                        Item,
                        R.layout.book_show,
                        new String[]{"name" , "cityName" , "representImage" , "address" , "phone" , "openTime" , "arriveWay" , "facebook" , "website" , "latitude" , "longitude"},
                        new int[]{R.id.item_book_name , R.id.item_store_city , R.id.item_image , R.id.item_address , R.id.item_phone , R.id.item_open , R.id.item_goto});
                //list.setAdapter(adapter);
                list.setAdapter(mCustomAdapter);
                //list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //    @Override
                //    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //        Toast.makeText(MainActivity.this,"點擊了第"+(i+1)+"個List",Toast.LENGTH_LONG).show();
                //    }
                //});

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            // 背景工作處理"中"更新的事

        }
    }
    // 設定searchView的文字輸入監聽
    private void setSearch_function() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCustomAdapter.getFilter().filter(newText);

                return true;
            }
        });
    }
}
