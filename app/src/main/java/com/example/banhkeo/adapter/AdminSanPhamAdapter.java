package com.example.banhkeo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.banhkeo.R;
import com.example.banhkeo.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdminSanPhamAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arrayadminsanpham;

    public AdminSanPhamAdapter(Context context, ArrayList<Sanpham> arrayadminsanpham) {
        this.context = context;
        this.arrayadminsanpham = arrayadminsanpham;
    }

    @Override
    public int getCount() {
        return arrayadminsanpham.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayadminsanpham.get(position);
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
            convertView = inflater.inflate(R.layout.dong_tatcasanpham, null);
            viewHolder.txthanghoa = convertView.findViewById(R.id.textViewtatcasanpham);
            viewHolder.txtgiahanghoa = convertView.findViewById(R.id.textViewgiatatcasanpham);
            viewHolder.txtmotahanghoa = convertView.findViewById(R.id.textViewmotatatcasanpham);
            viewHolder.imghanghoa = convertView.findViewById(R.id.imageViewtatcasanpham);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Sanpham sanpham = (Sanpham) getItem(position);
        viewHolder.txthanghoa.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiahanghoa.setText("Price: " + decimalFormat.format(sanpham.getGiasanpham()) + " VND");
        Picasso.get().load(sanpham.getHinhanhsanpham())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(viewHolder.imghanghoa);
        viewHolder.txtmotahanghoa.setMaxLines(2);
        viewHolder.txtmotahanghoa.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotahanghoa.setText(sanpham.getMotasanpham());

        //xóa & sửa

        return convertView;
    }

    public class ViewHolder {
        public TextView txthanghoa, txtgiahanghoa, txtmotahanghoa;
        public ImageView imghanghoa;

    }

}
