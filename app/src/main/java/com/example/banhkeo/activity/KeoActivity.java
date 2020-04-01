package com.example.banhkeo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.banhkeo.R;
import com.example.banhkeo.adapter.KeoAdapter;
import com.example.banhkeo.model.Sanpham;
import com.example.banhkeo.ultil.CheckConnection;
import com.example.banhkeo.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KeoActivity extends AppCompatActivity {
    ListView listView;
    KeoAdapter keoAdapter;
    ArrayList<Sanpham> mangkeo;
    ImageButton btnBack, btnCart;
    int idkeo = 0;
    int page = 1;
    View footerview;
    boolean isloading = false;
    mHandler handler;
    boolean limitdata = false;

    Sanpham keo;
    int ID = 0;
    String Tenkeo = "";
    Integer Giakeo = 0;
    String Hinhanhkeo = "";
    String Motakeo = "";
    int IDkeo = 0;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keo);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Đang xử lý..");
        pDialog.setCanceledOnTouchOutside(false);
        listView = (ListView) findViewById(R.id.listviewkeo);
        mangkeo = new ArrayList<>();
        keoAdapter = new KeoAdapter(getApplicationContext(), mangkeo);
        listView.setAdapter(keoAdapter);
        btnCart = (ImageButton) findViewById(R.id.btnCartkeo);
        btnBack = (ImageButton) findViewById(R.id.btnBackkeo);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar, null);
        handler = new mHandler();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.banhkeo.activity.GioHang.class);
                startActivity(intent);
            }
        });
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            GetIDLoaiSP();
            GetData(page);
            LoadMoreData();
            //CatchOnItemListView();
        } else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Check your Internet connection!!");
            finish();
        }

    }

    //  private void CatchOnItemListView() {
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                final PrettyDialog builder=new PrettyDialog(KeoActivity.this);
//                builder.setTitle("Thêm Vào Giỏ Hàng");
//                builder.setIcon(R.drawable.icons8candy);
//                builder.setMessage("Bạn có muốn thêm vào giỏ hàng?");
//                builder.addButton("Có",
//                        R.color.pdlg_color_white,
//                        R.color.pdlg_color_red,
//                        new PrettyDialogCallback()  {
//                    @Override
//                    public void onClick() {
//                        if (MainActivity.manggiohang.size()>0){
//                            int sl=1;
//                            boolean exists=false;
//                            keo=mangkeo.get(position);
//                            for (int i=0;i<MainActivity.manggiohang.size();i++){
//                                if (MainActivity.manggiohang.get(i).getIdsp()==keo.getID()){
//                                    MainActivity.manggiohang.get(i).setSoluongsp(MainActivity.manggiohang.get(i).getSoluongsp()+sl);
//                                    MainActivity.manggiohang.get(i).setGiasp(Giakeo*MainActivity.manggiohang.get(i).getSoluongsp());
//                                    exists=true;
//                                    builder.dismiss();
//                                }
//                            }
//                            if (exists==false){
//                                int soluong=1;
//                                long Giamoi=soluong * Giakeo;
//                                MainActivity.manggiohang.add(new com.example.banhkeo.model.GioHang(keo.getID(),Tenkeo,Giamoi,Hinhanhkeo,soluong));
//                                builder.dismiss();
//                            }
//                        }else {
//                            int soluong=1;
//                            long Giamoi=soluong * Giakeo;
//                            MainActivity.manggiohang.add(new com.example.banhkeo.model.GioHang(keo.getID(),Tenkeo,Giamoi,Hinhanhkeo,soluong));
//                            builder.dismiss();
//                        }
//                        Toast.makeText(KeoActivity.this,"Đã Thêm Vào Giỏ Hàng",Toast.LENGTH_LONG).show();
//                        builder.dismiss();
//                    }
//                });
//                builder.addButton("Không",
//                        R.color.pdlg_color_white,
//                        R.color.pdlg_color_red,
//                        new PrettyDialogCallback()  {
//                    @Override
//                    public void onClick() {
//                       keoAdapter.notifyDataSetChanged();
//                       builder.dismiss();
//                    }
//                });
//                builder.show();
//                return true;
//            }
//        });
//    }
    private void GetIDLoaiSP() {
        idkeo = getIntent().getIntExtra("idloaisanpham", -1);
    }

    private void GetData(int Page) {
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdankeo + Page;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null && response.length() != 2) {
                    listView.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ID = jsonObject.getInt("id");
                            Tenkeo = jsonObject.getString("tensp");
                            Giakeo = jsonObject.getInt("giasp");
                            Hinhanhkeo = jsonObject.getString("hinhanhsp");
                            Motakeo = jsonObject.getString("motasp");
                            IDkeo = jsonObject.getInt("idsanpham");
                            mangkeo.add(new Sanpham(ID, Tenkeo, Giakeo, Hinhanhkeo, Motakeo, IDkeo));
                            keoAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    limitdata = true;
                    listView.removeFooterView(footerview);
                    CheckConnection.ShowToast_Short(getApplicationContext(), "That was the last product");
                }
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("idsanpham", String.valueOf(idkeo));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void LoadMoreData() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(KeoActivity.this, ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham", mangkeo.get(position));
                startActivity(intent);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && isloading == false && limitdata == false) {
                    isloading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    public class mHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    listView.addFooterView(footerview);
                    break;
                case 1:
                    GetData(++page);
                    isloading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = handler.obtainMessage(1);
            handler.sendMessage(message);
            super.run();
        }
    }
}

