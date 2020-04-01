package com.example.banhkeo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.banhkeo.R;
import com.example.banhkeo.adapter.TaiKhoanAdapter;
import com.example.banhkeo.model.Account;
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

public class RegisterActivity extends AppCompatActivity {
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String TAG = RegisterActivity.class.getSimpleName();
    ImageButton btnBack;
    EditText edtUserName, edtPassWord, edtEmail;
    Button btnthem;
    ArrayList<Account> mangtaikhoan;
    TaiKhoanAdapter adapter;
    ListView listView;
    int page = 1;
    int id = 0;
    String Tendangnhap = "";
    String Password = "";
    String Email = "";
    Account account;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnthem = (Button) findViewById(R.id.btnthemtaikhoan);
        btnBack = (ImageButton) findViewById(R.id.btnBacktaikhoan);
        listView = findViewById(R.id.listviewtaikhoan);
        mangtaikhoan = new ArrayList<>();
        adapter = new TaiKhoanAdapter(getApplicationContext(), mangtaikhoan);
        listView.setAdapter(adapter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addControls();
        addEvents();
        GetData(page);


    }

    private void XoaTaiKhoan(final int idtk) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdanxoataikhoan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
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
                map.put("id", String.valueOf(idtk));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void GetData(int Page) {
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdantaikhoan;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tendangnhap = jsonObject.getString("tendangnhap");
                            Password = jsonObject.getString("matkhau");
                            Email = jsonObject.getString("email");
                            mangtaikhoan.add(new Account(id, Tendangnhap, Password, Email));
                            adapter.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        pDialog.dismiss();
                    }
                } else {
                    CheckConnection.ShowToast_Short(getApplicationContext(), "That was the last ");
                    pDialog.dismiss();
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
                hashMap.put("id", String.valueOf(id));

                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void addControls() {
        edtUserName = (EditText) findViewById(R.id.editTexttentaikhoan);
        edtPassWord = (EditText) findViewById(R.id.editTextmatkhau);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Đang xử lý...");
        pDialog.setCanceledOnTouchOutside(false);

    }

    private void addEvents() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final PrettyDialog builder = new PrettyDialog(RegisterActivity.this);
                builder.setTitle("Xác nhận xóa ")
                        .setIcon(R.drawable.icons8landlord);
                builder.setMessage("Bạn có chắc muốn xóa tài khoản này");
                builder.addButton("Có",
                        R.color.pdlg_color_white,
                        R.color.pdlg_color_red,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                account = mangtaikhoan.get(position);
                                if (account.getId() != 1) {
                                    XoaTaiKhoan(account.getId());
                                    mangtaikhoan.remove(position);
                                    adapter.notifyDataSetChanged();
                                    builder.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Không thể xóa tài khoản gốc!!!", Toast.LENGTH_LONG).show();
                                    builder.dismiss();
                                }


                            }
                        }
                );
                builder.addButton("Không",
                        R.color.pdlg_color_white,
                        R.color.pdlg_color_green,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                adapter.notifyDataSetChanged();
                                builder.dismiss();
                            }
                        });
                builder.show();
                return true;
            }
        });


        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get data input
                String username = edtUserName.getText().toString().trim();
                String password = edtPassWord.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();

                //Call method register
                registerUser(username, password, email);
            }
        });

    }

    private void registerUser(final String username, final String password, final String email) {

        if (checkEditText(edtUserName) && checkEditText(edtPassWord) && checkEditText(edtEmail) && isValidEmail(email)) {
            pDialog.show();
            StringRequest registerRequest = new StringRequest(Request.Method.POST, Server.Duongdanregister,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            String message = "";
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getInt("success") == 1) {
                                    Account account = new Account();
                                    account.setUserName(jsonObject.getString("user_name"));
                                    account.setEmail(jsonObject.getString("email"));
                                    message = jsonObject.getString("message");
                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                                    //Start LoginActivity
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    message = jsonObject.getString("message");
                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                            }
                            pDialog.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            pDialog.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(KEY_USERNAME, username);
                    params.put(KEY_PASSWORD, password);
                    params.put(KEY_EMAIL, email);
                    mangtaikhoan.clear();
                    GetData(page);
                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(registerRequest);
        }
    }

    /**
     * Check Input
     */
    private boolean checkEditText(EditText editText) {
        if (editText.getText().toString().trim().length() > 0)
            return true;
        else {
            editText.setError("Vui lòng nhập dữ liệu!");
        }
        return false;
    }

    /**
     * Check Email
     */
    private boolean isValidEmail(String target) {
        if (target.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))
            return true;
        else {
            edtEmail.setError("Email sai định dạng!");
        }
        return false;
    }
}
