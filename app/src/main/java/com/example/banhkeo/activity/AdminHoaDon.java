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
import android.widget.Toast;

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
import com.example.banhkeo.adapter.AdminDonHangAdapter;
import com.example.banhkeo.model.DonHang;
import com.example.banhkeo.ultil.CheckConnection;
import com.example.banhkeo.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class AdminHoaDon extends AppCompatActivity {
    ImageButton btnBack;
    ListView listView;
    AdminDonHangAdapter donHangAdapter;
    ArrayList<DonHang> mangdonhang;
    int page = 1;
    View footerview;
    boolean isLoading = false;
    mHandler handler;
    boolean limitdata = false;
    DonHang donHang;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_hoa_don);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Đang xử lý..");
        pDialog.setCanceledOnTouchOutside(false);
        btnBack = (ImageButton) findViewById(R.id.btnBackhoadon);
        listView = (ListView) findViewById(R.id.listviewhoadon);
        mangdonhang = new ArrayList<>();
        donHangAdapter = new AdminDonHangAdapter(getApplicationContext(), mangdonhang);
        listView.setAdapter(donHangAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar, null);
        handler = new mHandler();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GetData(page);
        LoadMoreData();
        ListViewEvent();
    }

    private void ListViewEvent() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final PrettyDialog builder = new PrettyDialog(AdminHoaDon.this);
                builder
                        .setIcon(R.drawable.icons8paidbill)
                        .setTitle("Xác nhận xóa ")
                        .setMessage("Bạn có chắc muốn xóa đơn hàng này")
                        .addButton("OK",
                                R.color.pdlg_color_white,
                                R.color.pdlg_color_red,
                                new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        donHang = mangdonhang.get(position);
                                        XoaDonHang(donHang.getID());
                                        mangdonhang.remove(position);
                                        donHangAdapter.notifyDataSetChanged();
                                        builder.dismiss();
                                    }
                                }
                        )
                        .addButton("Cancel",
                                R.color.pdlg_color_white,
                                R.color.pdlg_color_green,
                                new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        donHangAdapter.notifyDataSetChanged();
                                        builder.dismiss();
                                    }
                                }).show();
                return true;
            }
        });
    }

    private void XoaDonHang(final int iddh) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdanxoadonhang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_LONG).show();
                    donHangAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Xóa thất bại", Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Xảy ra lỗi", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(iddh));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void LoadMoreData() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChiTietHoaDon.class);
                intent.putExtra("chitiethoadon", mangdonhang.get(position).getID());
                startActivity(intent);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && isLoading == false && limitdata == false) {
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void GetData(int Page) {
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdanhoadon + Page;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String Tenkhachhang = "";
                int Sodienthoai = 0;
                String Email = "";
                String Diachi = "";
                if (response != null && response.length() != 2) {
                    listView.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tenkhachhang = jsonObject.getString("tenkh");
                            Sodienthoai = jsonObject.getInt("sdt");
                            Email = jsonObject.getString("email");
                            Diachi = jsonObject.getString("diachi");
                            mangdonhang.add(new DonHang(id, Tenkhachhang, Sodienthoai, Email, Diachi));
                            donHangAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    limitdata = true;
                    listView.removeFooterView(footerview);
                    CheckConnection.ShowToast_Short(getApplicationContext(), "That was the last one");
                }
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }
        });
        requestQueue.add(stringRequest);
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
                    isLoading = false;
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
