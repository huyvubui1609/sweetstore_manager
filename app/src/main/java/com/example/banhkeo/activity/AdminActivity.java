package com.example.banhkeo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.banhkeo.R;

public class AdminActivity extends AppCompatActivity {

    ImageButton btnhanghoa, btnhoadon, btnBack, btnthemuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnBack = (ImageButton) findViewById(R.id.btnBackAdmin);
        btnhoadon = (ImageButton) findViewById(R.id.btnhoadon);
        btnhanghoa = (ImageButton) findViewById(R.id.btnhanghoa);
        btnthemuser = (ImageButton) findViewById(R.id.btntaikhoan);
        btnhanghoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminSanPham.class);
                startActivity(intent);
            }
        });
        btnhoadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminHoaDon.class);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnthemuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}
