package com.example.banhkeo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.banhkeo.R;
import com.example.banhkeo.model.Account;
import com.example.banhkeo.ultil.CheckConnection;
import com.example.banhkeo.ultil.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    ImageButton btnBack;
    Button btnlogin, btnreset;
    private EditText edtUserName;
    private EditText edtPassWord;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            addControl();
            addEvent();
        } else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Check Your Internet Connection");
            finish();
        }

    }

    private void addEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get value input
                String username = edtUserName.getText().toString().trim();
                String password = edtPassWord.getText().toString().trim();
                // Call method
                loginAccount(username, password);

            }
        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPass.class);
                startActivity(intent);
            }
        });
    }

    private void addControl() {
        edtUserName = (EditText) findViewById(R.id.editUsername);
        edtPassWord = (EditText) findViewById(R.id.editPassword);
        btnlogin = (Button) findViewById(R.id.btnLogin);
        btnreset = (Button) findViewById(R.id.btnResetPass);
        btnBack = findViewById(R.id.btnBackLogin);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Đang đăng nhập...");
        pDialog.setCanceledOnTouchOutside(false);
    }

    public boolean checkEditText(EditText editText) {
        if (editText.getText().toString().trim().length() > 0)
            return true;
        else {
            editText.setError("Vui lòng nhập dữ liệu!");
        }
        return false;
    }

    public void loginAccount(final String username, final String password) {
        if (checkEditText(edtUserName) && checkEditText(edtPassWord)) {
            pDialog.show();
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest requestLogin = new StringRequest(Request.Method.POST, Server.Duongdanlogin,
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
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                    intent.putExtra("login", account);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    message = jsonObject.getString("message");
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                    return params;
                }
            };
            queue.add(requestLogin);
        }
    }
}
