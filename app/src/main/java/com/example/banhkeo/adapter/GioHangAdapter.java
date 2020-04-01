package com.example.banhkeo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.banhkeo.R;
import com.example.banhkeo.activity.MainActivity;
import com.example.banhkeo.model.GioHang;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GioHangAdapter extends BaseAdapter {
    Context context;
    ArrayList<GioHang> arraygiohang;
    ViewHolder viewHolder = new ViewHolder();

    public GioHangAdapter(Context context, ArrayList<GioHang> arraygiohang) {
        this.context = context;
        this.arraygiohang = arraygiohang;
    }

    @Override
    public int getCount() {
        return arraygiohang.size();
    }

    @Override
    public Object getItem(int position) {
        return arraygiohang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_giohang, null);
            viewHolder.txtgiagiohang = convertView.findViewById(R.id.textViewgiagiohang);
            viewHolder.txttengiohang = convertView.findViewById(R.id.textViewtengiohang);
            viewHolder.imggiohang = convertView.findViewById(R.id.imageViewgiohang);
            viewHolder.btnminus = convertView.findViewById(R.id.btnminus);
            viewHolder.btnplus = convertView.findViewById(R.id.btnplus);
            viewHolder.btnvalue = convertView.findViewById(R.id.btnvalue);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GioHang gioHang = (GioHang) getItem(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiagiohang.setText(decimalFormat.format(gioHang.getGiasp()) + " VND");
        Picasso.get().load(gioHang.getHinhsp())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(viewHolder.imggiohang);
        viewHolder.txttengiohang.setText(gioHang.getTensp());
        viewHolder.btnvalue.setText(gioHang.getSoluongsp() + "");
        int sl = Integer.parseInt(viewHolder.btnvalue.getText().toString());
        if (sl > 10) {
            viewHolder.btnplus.setVisibility(View.INVISIBLE);
            viewHolder.btnminus.setVisibility(View.VISIBLE);
        } else if (sl == 1) {
            viewHolder.btnplus.setVisibility(View.VISIBLE);
            viewHolder.btnminus.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.btnplus.setVisibility(View.VISIBLE);
            viewHolder.btnminus.setVisibility(View.VISIBLE);
        }

        viewHolder.btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slnew = Integer.parseInt(viewHolder.btnvalue.getText().toString()) + 1;
                int slnow = MainActivity.manggiohang.get(position).getSoluongsp();
                long gianow = MainActivity.manggiohang.get(position).getGiasp();

                MainActivity.manggiohang.get(position).setSoluongsp(slnew);
                long gianew = (gianow * slnew) / slnow;
                MainActivity.manggiohang.get(position).setGiasp(gianew);

                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                viewHolder.txtgiagiohang.setText(decimalFormat.format(gianew) + " VND");
                com.example.banhkeo.activity.GioHang.EventUltil();
                if (slnew > 9) {
                    viewHolder.btnplus.setVisibility(View.INVISIBLE);
                    viewHolder.btnminus.setVisibility(View.VISIBLE);
                    viewHolder.btnvalue.setText(String.valueOf(slnew));
                } else {
                    viewHolder.btnplus.setVisibility(View.VISIBLE);
                    viewHolder.btnminus.setVisibility(View.VISIBLE);
                    viewHolder.btnvalue.setText(String.valueOf(slnew));

                }
            }
        });
        viewHolder.btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slnew = Integer.parseInt(viewHolder.btnvalue.getText().toString()) - 1;
                int slnow = MainActivity.manggiohang.get(position).getSoluongsp();
                long gianow = MainActivity.manggiohang.get(position).getGiasp();

                MainActivity.manggiohang.get(position).setSoluongsp(slnew);
                long gianew = (gianow * slnew) / slnow;
                MainActivity.manggiohang.get(position).setGiasp(gianew);

                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                viewHolder.txtgiagiohang.setText(decimalFormat.format(gianew) + " VND");
                com.example.banhkeo.activity.GioHang.EventUltil();
                if (slnew < 2) {
                    viewHolder.btnplus.setVisibility(View.VISIBLE);
                    viewHolder.btnminus.setVisibility(View.INVISIBLE);
                    viewHolder.btnvalue.setText(String.valueOf(slnew));
                } else {
                    viewHolder.btnplus.setVisibility(View.VISIBLE);
                    viewHolder.btnminus.setVisibility(View.VISIBLE);
                    viewHolder.btnvalue.setText(String.valueOf(slnew));

                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public TextView txttengiohang, txtgiagiohang;
        public ImageView imggiohang;
        public Button btnminus, btnvalue, btnplus;
    }
}
