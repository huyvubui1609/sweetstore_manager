package com.example.banhkeo.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.banhkeo.R;
import com.example.banhkeo.ultil.CheckConnection;
import com.example.banhkeo.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ThongTinKhachHang extends AppCompatActivity {
    EditText edttenkhachhang, edtsodienthoai, edtemail, edtdiachi;
    Button btnxacnhan, btnhuy;
    CheckBox checkBox;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_khach_hang);

        edttenkhachhang = (EditText) findViewById(R.id.editTexttenkhachhang);
        edtsodienthoai = (EditText) findViewById(R.id.editTextsdtkhachhang);
        edtemail = (EditText) findViewById(R.id.editTextemailkhachhang);
        edtdiachi = (EditText) findViewById(R.id.editTextdiachikhachhang);
        btnxacnhan = (Button) findViewById(R.id.btnxacnhan);
        btnhuy = (Button) findViewById(R.id.btntrove);
        checkBox = findViewById(R.id.checkboxthanhtoan);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Đang xử lý...");
        pDialog.setCanceledOnTouchOutside(false);

        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            btnxacnhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String ten = edttenkhachhang.getText().toString().trim();
                    final String sdt = edtsodienthoai.getText().toString().trim();
                    final String email = edtemail.getText().toString().trim();
                    final String diachi = edtdiachi.getText().toString().trim();
                    pDialog.show();
                    if (ten.length() > 0 && sdt.length() > 0 && email.length() > 0 && isValidEmail(email) == true && diachi.length() > 0 && checkBox.isChecked() && validatePhoneNumber(sdt)) {
                        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdanthongtinkhachhang, new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String iddonhang) {
                                if (Integer.parseInt(iddonhang) > 0) {
                                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                    StringRequest request = new StringRequest(Request.Method.POST, Server.Duongdanchitietdonhang, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (response.equals("1")) {
                                                MainActivity.manggiohang.clear();
                                                CheckConnection.ShowToast_Short(getApplicationContext(), "Bạn Đã Xác Nhận Thông Tin Đơn Hàng Thành Công");
                                                finish();
                                                CheckConnection.ShowToast_Short(getApplicationContext(), "Mời bạn Tiếp Tục Mua Hàng");
                                            } else {
                                                CheckConnection.ShowToast_Short(getApplicationContext(), "Chức Năng Này Đang Trong Thời Gian Bảo Trì");
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            JSONArray jsonArray = new JSONArray();
                                            for (int i = 0; i < MainActivity.manggiohang.size(); i++) {
                                                JSONObject jsonObject = new JSONObject();
                                                try {
                                                    jsonObject.put("iddonhang", iddonhang);
                                                    jsonObject.put("idsanpham", MainActivity.manggiohang.get(i).getIdsp());
                                                    jsonObject.put("tensanpham", MainActivity.manggiohang.get(i).getTensp());
                                                    jsonObject.put("giasanpham", MainActivity.manggiohang.get(i).getGiasp());
                                                    jsonObject.put("soluongsanpham", MainActivity.manggiohang.get(i).getSoluongsp());
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                jsonArray.put(jsonObject);
                                            }
                                            HashMap<String, String> hashMap = new HashMap<String, String>();
                                            hashMap.put("json", jsonArray.toString());
                                            return hashMap;
                                        }
                                    };
                                    queue.add(request);
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
                                hashMap.put("tenkhachhang", ten);
                                hashMap.put("sodienthoai", sdt);
                                hashMap.put("email", email);
                                hashMap.put("diachi", diachi);
                                return hashMap;
                            }
                        };
                        requestQueue.add(stringRequest);
                    } else {
                        CheckConnection.ShowToast_Short(getApplicationContext(), "Hãy Nhập Đầy Đủ Thông Tin Và Xác Nhận Tính Xác Thực ");
                        pDialog.dismiss();
                    }

                }
            });
        } else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Check your Internet connection");
            pDialog.dismiss();
        }

    }

    private boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) {
            return true;
        }
        //return false if nothing matches the input
        else {
            Toast.makeText(ThongTinKhachHang.this, "Phone number must be 10 digits", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public boolean isValidEmail(CharSequence target) {
        if (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            return true;
        }
        CheckConnection.ShowToast_Short(getApplicationContext(), "Please enter valid email address");
        return false;
    }
}
