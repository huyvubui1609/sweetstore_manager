package com.example.banhkeo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.banhkeo.R;
import com.example.banhkeo.model.GioHang;
import com.example.banhkeo.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ChiTietSanPham extends AppCompatActivity {
    ImageButton btnBack, btnCart;
    ImageView imgChiTiet;
    TextView txtten, txtgia, txtmota;
    Spinner spinner;
    Button btndatmua, btnmuangay;
    int id = 0;
    String TenChiTiet = "";
    int GiaChiTiet = 0;
    String Hinhanhchitiet = "";
    String Motachitiet = "";
    int Idsanpham = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        btnBack = (ImageButton) findViewById(R.id.btnBackchitietsanpham);
        btnCart = (ImageButton) findViewById(R.id.btnCartchitietsanpham);
        txtgia = (TextView) findViewById(R.id.textViewgiachitietsanpham);
        txtmota = (TextView) findViewById(R.id.textViewmotachitietsanpham);
        txtten = (TextView) findViewById(R.id.textViewtenchitietsanpham);
        spinner = (Spinner) findViewById(R.id.spinner);
        btndatmua = (Button) findViewById(R.id.btnchitietsanpham);
        imgChiTiet = (ImageView) findViewById(R.id.imageViewchitietsanpham);
        btnmuangay = findViewById(R.id.btnmuangay);

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

        GetInformation();
        CatchEventSpinner();
        EventButton();
    }

    private void EventButton() {
        btndatmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.manggiohang.size() > 0) {
                    int sl = Integer.parseInt(spinner.getSelectedItem().toString());
                    boolean exists = false;
                    for (int i = 0; i < MainActivity.manggiohang.size(); i++) {
                        if (MainActivity.manggiohang.get(i).getIdsp() == id) {
                            MainActivity.manggiohang.get(i).setSoluongsp(MainActivity.manggiohang.get(i).getSoluongsp() + sl);
                            MainActivity.manggiohang.get(i).setGiasp(GiaChiTiet * MainActivity.manggiohang.get(i).getSoluongsp());
                            exists = true;
                        }
                    }
                    if (exists == false) {
                        int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                        long Giamoi = soluong * GiaChiTiet;
                        MainActivity.manggiohang.add(new GioHang(id, TenChiTiet, Giamoi, Hinhanhchitiet, soluong));
                    }
                } else {
                    int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                    long Giamoi = soluong * GiaChiTiet;
                    MainActivity.manggiohang.add(new GioHang(id, TenChiTiet, Giamoi, Hinhanhchitiet, soluong));
                }
                Toast.makeText(getApplicationContext(), "Đã Thêm Vào Giỏ Hàng", Toast.LENGTH_LONG).show();
            }
        });
        btnmuangay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.manggiohang.size() > 0) {
                    int sl = Integer.parseInt(spinner.getSelectedItem().toString());
                    boolean exists = false;
                    for (int i = 0; i < MainActivity.manggiohang.size(); i++) {
                        if (MainActivity.manggiohang.get(i).getIdsp() == id) {
                            MainActivity.manggiohang.get(i).setSoluongsp(MainActivity.manggiohang.get(i).getSoluongsp() + sl);
                            MainActivity.manggiohang.get(i).setGiasp(GiaChiTiet * MainActivity.manggiohang.get(i).getSoluongsp());
                            exists = true;
                        }
                    }
                    if (exists == false) {
                        int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                        long Giamoi = soluong * GiaChiTiet;
                        MainActivity.manggiohang.add(new GioHang(id, TenChiTiet, Giamoi, Hinhanhchitiet, soluong));
                    }
                } else {
                    int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                    long Giamoi = soluong * GiaChiTiet;
                    MainActivity.manggiohang.add(new GioHang(id, TenChiTiet, Giamoi, Hinhanhchitiet, soluong));
                }
                Intent intent = new Intent(getApplicationContext(), com.example.banhkeo.activity.GioHang.class);
                startActivity(intent);
            }
        });
    }

    private void CatchEventSpinner() {
        Integer[] soluong = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, R.layout.support_simple_spinner_dropdown_item, soluong);
        spinner.setAdapter(arrayAdapter);
    }

    private void GetInformation() {
        Sanpham sanpham = (Sanpham) getIntent().getSerializableExtra("thongtinsanpham");
        id = sanpham.getID();
        TenChiTiet = sanpham.getTensanpham();
        GiaChiTiet = sanpham.getGiasanpham();
        Hinhanhchitiet = sanpham.getHinhanhsanpham();
        Motachitiet = sanpham.getMotasanpham();
        Idsanpham = sanpham.getIDsanpham();
        txtten.setText(TenChiTiet);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtgia.setText("Price: " + decimalFormat.format(GiaChiTiet) + " VND");
        txtmota.setText(Motachitiet);
        Picasso.get().load(Hinhanhchitiet)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imgChiTiet);
    }
}
