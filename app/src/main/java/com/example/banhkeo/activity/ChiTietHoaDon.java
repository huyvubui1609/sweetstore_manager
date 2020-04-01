package com.example.banhkeo.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.banhkeo.R;
import com.example.banhkeo.adapter.ChiTietHoaDonAdapter;
import com.example.banhkeo.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChiTietHoaDon extends AppCompatActivity {
    ImageButton btnBack;
    ListView listView;
    ChiTietHoaDonAdapter adapter;
    ArrayList<com.example.banhkeo.model.ChiTietHoaDon> mangchitiethoadon;
    int idhd = 0;
    int page = 1;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hoa_don);
        listView = findViewById(R.id.listviewchitiethoadon);
        mangchitiethoadon = new ArrayList<>();
        adapter = new ChiTietHoaDonAdapter(getApplicationContext(), mangchitiethoadon);
        listView.setAdapter(adapter);
        btnBack = (ImageButton) findViewById(R.id.btnBackchitietdonhang);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Đang xử lý..");
        pDialog.setCanceledOnTouchOutside(false);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GetIDHoaDon();
        GetData(page);
    }

    private void GetData(int Page) {
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Server.Duongdanchitiethoadon + Page, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                int iddonhang = 0;
                int idsanpham = 0;
                String tensanpham = "";
                int giasanpham = 0;
                int soluong = 0;
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            iddonhang = jsonObject.getInt("iddonhang");
                            idsanpham = jsonObject.getInt("idsanpham");
                            tensanpham = jsonObject.getString("tensanpham");
                            giasanpham = jsonObject.getInt("giasanpham");
                            soluong = jsonObject.getInt("soluong");
                            mangchitiethoadon.add(new com.example.banhkeo.model.ChiTietHoaDon(id, iddonhang, idsanpham, tensanpham, giasanpham, soluong));
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("iddonhang", String.valueOf(idhd));
                return hashMap;
            }
        };
        queue.add(request);

    }

    private void GetIDHoaDon() {
        idhd = getIntent().getIntExtra("chitiethoadon", -1);
        Log.d("GiaTriHoaDon", idhd + "");
    }
}
