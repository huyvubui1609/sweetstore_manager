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

public class BanhAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arraybanh;

    public BanhAdapter(Context context, ArrayList<Sanpham> arraybanh) {
        this.context = context;
        this.arraybanh = arraybanh;
    }

    @Override
    public int getCount() {
        return arraybanh.size();
    }

    @Override
    public Object getItem(int position) {
        return arraybanh.get(position);
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
            convertView = inflater.inflate(R.layout.dong_banh, null);
            viewHolder.txttenbanh = convertView.findViewById(R.id.textViewbanh);
            viewHolder.txtgiabanh = convertView.findViewById(R.id.textViewgiabanh);
            viewHolder.txtmotabanh = convertView.findViewById(R.id.textViewmotabanh);
            viewHolder.imgbanh = convertView.findViewById(R.id.imageViewbanh);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Sanpham sanpham = (Sanpham) getItem(position);
        viewHolder.txttenbanh.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiabanh.setText("Price: " + decimalFormat.format(sanpham.getGiasanpham()) + " VND");
        Picasso.get().load(sanpham.getHinhanhsanpham())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(viewHolder.imgbanh);
        viewHolder.txtmotabanh.setMaxLines(2);
        viewHolder.txtmotabanh.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotabanh.setText(sanpham.getMotasanpham());
        return convertView;
    }

    public class ViewHolder {
        public TextView txttenbanh, txtgiabanh, txtmotabanh;
        public ImageView imgbanh;
    }
}
