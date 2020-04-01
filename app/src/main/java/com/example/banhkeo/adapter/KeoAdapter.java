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

public class KeoAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arraykeo;

    public KeoAdapter(Context context, ArrayList<Sanpham> arraykeo) {
        this.context = context;
        this.arraykeo = arraykeo;
    }

    @Override
    public int getCount() {
        return arraykeo.size();
    }

    @Override
    public Object getItem(int position) {
        return arraykeo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        KeoAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_keo, null);
            viewHolder.txttenkeo = convertView.findViewById(R.id.textViewkeo);
            viewHolder.txtgiakeo = convertView.findViewById(R.id.textViewgiakeo);
            viewHolder.txtmotakeo = convertView.findViewById(R.id.textViewmotakeo);
            viewHolder.imgkeo = convertView.findViewById(R.id.imageViewkeo);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (KeoAdapter.ViewHolder) convertView.getTag();
        }
        Sanpham sanpham = (Sanpham) getItem(position);
        viewHolder.txttenkeo.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiakeo.setText("Price: " + decimalFormat.format(sanpham.getGiasanpham()) + " VND");
        Picasso.get().load(sanpham.getHinhanhsanpham())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(viewHolder.imgkeo);
        viewHolder.txtmotakeo.setMaxLines(2);
        viewHolder.txtmotakeo.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotakeo.setText(sanpham.getMotasanpham());
        return convertView;
    }

    public class ViewHolder {
        public TextView txttenkeo, txtgiakeo, txtmotakeo;
        public ImageView imgkeo;
    }
}
