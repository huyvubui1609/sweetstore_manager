package com.example.banhkeo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.banhkeo.R;
import com.example.banhkeo.adapter.GioHangAdapter;

import java.text.DecimalFormat;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class GioHang extends AppCompatActivity {
    static TextView txttongtien;
    ImageButton btnBack;
    Button btnthanhtoan, btntieptucmua;
    ListView listView;
    TextView txtthongbao;
    GioHangAdapter gioHangAdapter;

    public static void EventUltil() {
        long tongtien = 0;
        for (int i = 0; i < MainActivity.manggiohang.size(); i++) {
            tongtien += MainActivity.manggiohang.get(i).getGiasp();

        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txttongtien.setText(decimalFormat.format(tongtien) + " VND");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        btnBack = (ImageButton) findViewById(R.id.btnBackgiohang);
        btnthanhtoan = (Button) findViewById(R.id.btnthanhtoangiohang);
        btntieptucmua = (Button) findViewById(R.id.btntieptucmuahang);
        txtthongbao = (TextView) findViewById(R.id.textViewthongbao);
        txttongtien = (TextView) findViewById(R.id.textViewtongtien);
        listView = (ListView) findViewById(R.id.listViewgiohang);
        gioHangAdapter = new GioHangAdapter(GioHang.this, MainActivity.manggiohang);
        listView.setAdapter(gioHangAdapter);


        CheckData();
        EventUltil();
        CatchOnItemListView();
        EventButton();

    }

    private void EventButton() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btntieptucmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.manggiohang.size() > 0) {
                    Intent intent = new Intent(getApplicationContext(), ThongTinKhachHang.class);
                    startActivity(intent);
                    finish();
                } else if (MainActivity.manggiohang.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "Giỏ hàng của bạn đang trống", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void CatchOnItemListView() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final PrettyDialog builder = new PrettyDialog(GioHang.this);
                builder
                        .setIcon(R.drawable.icons8whitecart)
                        .setTitle("Xác nhận xóa sản phẩm")
                        .setMessage("Bạn có chắc muốn xóa sản phẩm này")
                        .addButton("Có", R.color.pdlg_color_white,
                                R.color.pdlg_color_red,
                                new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        if (MainActivity.manggiohang.size() <= 0) {
                                            txtthongbao.setVisibility(View.VISIBLE);
                                            builder.dismiss();
                                        } else {
                                            MainActivity.manggiohang.remove(position);
                                            gioHangAdapter.notifyDataSetChanged();
                                            EventUltil();
                                            builder.dismiss();
                                            if (MainActivity.manggiohang.size() <= 0) {
                                                txtthongbao.setVisibility(View.VISIBLE);
                                                builder.dismiss();
                                            } else {
                                                txtthongbao.setVisibility(View.INVISIBLE);
                                                gioHangAdapter.notifyDataSetChanged();
                                                EventUltil();
                                                builder.dismiss();
                                            }
                                        }
                                    }
                                }).addButton("Không", R.color.pdlg_color_white,
                        R.color.pdlg_color_green,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {

                                gioHangAdapter.notifyDataSetChanged();
                                EventUltil();
                                builder.dismiss();
                            }
                        }).show();
                return true;
            }
        });
    }

    private void CheckData() {
        if (MainActivity.manggiohang.size() <= 0) {
            gioHangAdapter.notifyDataSetChanged();
            txtthongbao.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            gioHangAdapter.notifyDataSetChanged();
            txtthongbao.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
    }
}
