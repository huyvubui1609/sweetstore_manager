package com.example.banhkeo.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.banhkeo.adapter.AdminSanPhamAdapter;
import com.example.banhkeo.model.Sanpham;
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

public class AdminSanPham extends AppCompatActivity {
    public static ListView listView;
    public static ArrayList<Sanpham> manghanghoa;
    public static EditText edttensanpham, edtgiasanpham, edthinhanhsanpham, edtmotasanpham, edtloaisanpham;
    public static Button btnthem, btnsua;
    ImageButton btnBack;
    AdminSanPhamAdapter adapter;
    int idhanghoa = 0;
    int page = 1;
    View footerview;
    boolean isLoading = false;
    mHandler handler;
    boolean limitdata = false;
    int id = 0;
    String Tenhanghoa = "";
    int Giahanghoa = 0;
    String Hinhanhhanghoa = "";
    String Motahanghoa = "";
    int Idsp = 0;
    Sanpham sanpham1;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_san_pham);
        btnBack = (ImageButton) findViewById(R.id.btnBacksanpham);
        listView = (ListView) findViewById(R.id.listviewhanghoa);
        manghanghoa = new ArrayList<>();
        adapter = new AdminSanPhamAdapter(getApplicationContext(), manghanghoa);
        listView.setAdapter(adapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar, null);
        handler = new mHandler();
        edttensanpham = (EditText) findViewById(R.id.editTexttenhanghoa);
        edtgiasanpham = (EditText) findViewById(R.id.editTextgiahanghoa);
        edthinhanhsanpham = (EditText) findViewById(R.id.editTexthinhhanghoa);
        edtmotasanpham = (EditText) findViewById(R.id.editTextmotahanghoa);
        edtloaisanpham = (EditText) findViewById(R.id.editTextidhanghoa);
        btnthem = (Button) findViewById(R.id.btnthem);
        btnsua = (Button) findViewById(R.id.btnsua);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Đang xử lý..");
        pDialog.setCanceledOnTouchOutside(false);
//        btnclear=findViewById(R.id.btnclear);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GetIdLoaiSanPham();
        GetData(page);
        //LoadMoreData();
        EventButton();
        adapter.notifyDataSetChanged();

    }

    private void XoaSanPham(final int idsp) {
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdanxoasanpham, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Xóa thất bại", Toast.LENGTH_LONG).show();

                }
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Xảy ra lỗi", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(idsp));
                manghanghoa.clear();
                GetData(page);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void CapNhatSanPham() {
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdansuasanpham, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(getApplicationContext(), "Update thành công", Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Update thất bại", Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                }
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Xảy ra lỗi", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(sanpham1.getID()));
                map.put("tensanpham", edttensanpham.getText().toString().trim());
                map.put("giasanpham", edtgiasanpham.getText().toString().trim());
                map.put("hinhanhsanpham", edthinhanhsanpham.getText().toString().trim());
                map.put("motasanpham", edtmotasanpham.getText().toString().trim());
                map.put("idsanpham", edtloaisanpham.getText().toString().trim());
                manghanghoa.clear();
                GetData(page);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ThemSanPham() {
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Server.Duongdanthemsanpham, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                adapter.notifyDataSetChanged();
                CheckConnection.ShowToast_Short(getApplicationContext(), "Thêm sản phẩm thành công");
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                adapter.notifyDataSetChanged();
                pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("tensanpham", edttensanpham.getText().toString().trim());
                hashMap.put("giasanpham", edtgiasanpham.getText().toString().trim());
                hashMap.put("hinhanhsanpham", edthinhanhsanpham.getText().toString().trim());
                hashMap.put("motasanpham", edtmotasanpham.getText().toString().trim());
                hashMap.put("idsanpham", edtloaisanpham.getText().toString().trim());
                manghanghoa.clear();
                GetData(page);
                return hashMap;

            }
        };
        queue.add(request);
        adapter.notifyDataSetChanged();
    }

    private void EventButton() {
        AdminSanPham.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sanpham1 = manghanghoa.get(position);
                AdminSanPham.edttensanpham.setText(sanpham1.getTensanpham());
                AdminSanPham.edtgiasanpham.setText(String.valueOf(sanpham1.getGiasanpham()));
                AdminSanPham.edthinhanhsanpham.setText(sanpham1.getHinhanhsanpham());
                AdminSanPham.edtmotasanpham.setText(sanpham1.getMotasanpham());
                AdminSanPham.edtloaisanpham.setText(String.valueOf(sanpham1.getIDsanpham()));

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final PrettyDialog builder = new PrettyDialog(AdminSanPham.this);
                builder.setIcon(R.drawable.icons8cookie)
                        .setTitle("Xác nhận xóa ")
                        .setMessage("Bạn có chắc muốn xóa đơn hàng này")
                        .addButton("OK",
                                R.color.pdlg_color_white,
                                R.color.pdlg_color_red,
                                new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        sanpham1 = manghanghoa.get(position);
                                        if (manghanghoa.size() > 10) {
                                            XoaSanPham(sanpham1.getID());
                                            manghanghoa.remove(position);
                                            adapter.notifyDataSetChanged();
                                            builder.dismiss();

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Số lượng sản phẩm không thể bé hơn 10", Toast.LENGTH_LONG).show();
                                            builder.dismiss();
                                        }
                                    }
                                }
                        )
                        .addButton("Cancel",
                                R.color.pdlg_color_white,
                                R.color.pdlg_color_green,
                                new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {

                                        adapter.notifyDataSetChanged();
                                        builder.dismiss();
                                    }
                                }).show();
                return true;
            }
        });

        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ten = edttensanpham.getText().toString().trim();
                final String gia = edtgiasanpham.getText().toString().trim();
                final String anh = edthinhanhsanpham.getText().toString().trim();
                final String mota = edtmotasanpham.getText().toString().trim();
                final String idloai = edtloaisanpham.getText().toString().trim();
                if (ten.length() > 0 && gia.length() > 0 && anh.length() > 0 && mota.length() > 0 && idloai.length() > 0) {
                    CapNhatSanPham();
                    adapter.notifyDataSetChanged();

                } else {
                    CheckConnection.ShowToast_Short(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin");

                }

            }
        });
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ten = edttensanpham.getText().toString().trim();
                final String gia = edtgiasanpham.getText().toString().trim();
                final String anh = edthinhanhsanpham.getText().toString().trim();
                final String mota = edtmotasanpham.getText().toString().trim();
                final String idloai = edtloaisanpham.getText().toString().trim();
                if (ten.length() > 0 && gia.length() > 0 && anh.length() > 0 && mota.length() > 0 && idloai.length() > 0) {
                    ThemSanPham();
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.notifyDataSetChanged();
                    CheckConnection.ShowToast_Short(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin");
                }
            }
        });


    }

    private void LoadMoreData() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (limitdata == false && isLoading == false && totalItemCount != 0 && firstVisibleItem + visibleItemCount == totalItemCount) {
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
        String duongdan = Server.Duongdansanpham + Page;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null && response.length() != 2) {
                    listView.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tenhanghoa = jsonObject.getString("tensp");
                            Giahanghoa = jsonObject.getInt("giasp");
                            Hinhanhhanghoa = jsonObject.getString("hinhanhsp");
                            Motahanghoa = jsonObject.getString("motasp");
                            Idsp = jsonObject.getInt("idsanpham");
                            manghanghoa.add(new Sanpham(id, Tenhanghoa, Giahanghoa, Hinhanhhanghoa, Motahanghoa, Idsp));
                            adapter.notifyDataSetChanged();
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
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("idsanpham", String.valueOf(idhanghoa));
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void GetIdLoaiSanPham() {
        idhanghoa = getIntent().getIntExtra("idloaisanpham", -1);
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
