package com.example.banhkeo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.banhkeo.R;
import com.example.banhkeo.model.DonHang;

import java.util.ArrayList;

public class AdminDonHangAdapter extends BaseAdapter {
    Context context;
    ArrayList<DonHang> arraydonhang;

    public AdminDonHangAdapter(Context context, ArrayList<DonHang> arraydonhang) {
        this.context = context;
        this.arraydonhang = arraydonhang;
    }

    @Override
    public int getCount() {
        return arraydonhang.size();
    }

    @Override
    public Object getItem(int position) {
        return arraydonhang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_donhang, null);
            viewHolder.txtid = convertView.findViewById(R.id.textViewiddonhang);
            viewHolder.txtten = convertView.findViewById(R.id.textViewtenkhachhang);
            viewHolder.txtsdt = convertView.findViewById(R.id.textViewsodienthoai);
            viewHolder.txtemail = convertView.findViewById(R.id.textViewemail);
            viewHolder.txtdiachi = convertView.findViewById(R.id.textViewdiachi);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DonHang donHang = (DonHang) getItem(position);
        viewHolder.txtid.setText("Mã đơn hàng: " + donHang.getID());
        viewHolder.txtten.setText("Khách hàng: " + donHang.getTenkhachhang());
        viewHolder.txtsdt.setText("Số điện thoại: " + donHang.getSodienthoai());
        viewHolder.txtemail.setText("Email: " + donHang.getEmail());
        viewHolder.txtdiachi.setMaxLines(2);
        viewHolder.txtdiachi.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtdiachi.setText("Địa chỉ: " + donHang.getDiachi());
        return convertView;
    }

    public class ViewHolder {
        TextView txtid, txtten, txtsdt, txtemail, txtdiachi;
    }
}
