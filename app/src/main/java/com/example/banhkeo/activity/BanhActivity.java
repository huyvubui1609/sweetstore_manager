package com.example.banhkeo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

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
import com.example.banhkeo.adapter.BanhAdapter;
import com.example.banhkeo.model.Sanpham;
import com.example.banhkeo.ultil.CheckConnection;
import com.example.banhkeo.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BanhActivity extends AppCompatActivity {
    ListView listView;
    BanhAdapter banhAdapter;
    ArrayList<Sanpham> mangbanh;
    ImageButton btnBack, btnCart;
    int idbanh = 0;
    int page = 1;
    ProgressBar progressBar;
    View footerview;
    boolean isloading = false;
    mHandler handler;
    boolean limitdata = false;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banh);
        listView = (ListView) findViewById(R.id.listviewbanh);
        mangbanh = new ArrayList<>();
        banhAdapter = new BanhAdapter(getApplicationContext(), mangbanh);
        listView.setAdapter(banhAdapter);
        handler = new mHandler();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Đang xử lý..");
        pDialog.setCanceledOnTouchOutside(false);
        btnBack = (ImageButton) findViewById(R.id.btnBackbanh);
        btnCart = (ImageButton) findViewById(R.id.btnCartbanh);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar, null);
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
        } else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Check your Internet connection!!");
            finish();
        }

    }

    private void LoadMoreData() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BanhActivity.this, ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham", mangbanh.get(position));
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

    private void GetData(int Page) {
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdanbanh + Page;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int ID = 0;
                String Tensanpham = "";
                Integer Giasanpham = 0;
                String Hinhanhsanpham = "";
                String Motasanpham = "";
                int IDsanpham = 0;
                if (response != null && response.length() != 2) {
                    listView.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ID = jsonObject.getInt("id");
                            Tensanpham = jsonObject.getString("tensp");
                            Giasanpham = jsonObject.getInt("giasp");
                            Hinhanhsanpham = jsonObject.getString("hinhanhsp");
                            Motasanpham = jsonObject.getString("motasp");
                            IDsanpham = jsonObject.getInt("idsanpham");
                            mangbanh.add(new Sanpham(ID, Tensanpham, Giasanpham, Hinhanhsanpham, Motasanpham, IDsanpham));
                            banhAdapter.notifyDataSetChanged();
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
                param.put("idsanpham", String.valueOf(idbanh));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void GetIDLoaiSP() {
        idbanh = getIntent().getIntExtra("idloaisanpham", -1);
        Log.d("giatriloaisanpham", idbanh + "");
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
