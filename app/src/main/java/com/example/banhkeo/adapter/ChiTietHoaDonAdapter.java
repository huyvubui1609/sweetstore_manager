package com.example.banhkeo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.banhkeo.R;
import com.example.banhkeo.model.ChiTietHoaDon;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ChiTietHoaDonAdapter extends BaseAdapter {
    Context context;
    ArrayList<ChiTietHoaDon> arraychitiethoadon;

    public ChiTietHoaDonAdapter(Context context, ArrayList<ChiTietHoaDon> arraychitiethoadon) {
        this.context = context;
        this.arraychitiethoadon = arraychitiethoadon;
    }

    @Override
    public int getCount() {
        return arraychitiethoadon.size();
    }

    @Override
    public Object getItem(int position) {
        return arraychitiethoadon.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_chitiethoadon, null);
            viewHolder.txtchithietiddonhang = convertView.findViewById(R.id.textViewIDHoaDon);
            viewHolder.txtchitiettensp = convertView.findViewById(R.id.textViewTenSanPham);
            viewHolder.txtchitietgiasp = convertView.findViewById(R.id.textViewGiaSanPham);
            viewHolder.txtchitietsoluong = convertView.findViewById(R.id.textViewSoLuong);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ChiTietHoaDon chiTietHoaDon = (ChiTietHoaDon) getItem(position);
        viewHolder.txtchithietiddonhang.setText(String.valueOf(chiTietHoaDon.getIDdonhang()));
        viewHolder.txtchitiettensp.setText(chiTietHoaDon.getTenspham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtchitietgiasp.setText("Đơn Giá: " + decimalFormat.format(chiTietHoaDon.getGiasanpham()) + " VND");
        viewHolder.txtchitietsoluong.setText("Số Lượng: " + chiTietHoaDon.getSoluongsanpham());
        return convertView;
    }

    public class ViewHolder {
        public TextView txtchitiettensp, txtchitietgiasp, txtchithietiddonhang, txtchitietsoluong;
    }
}
