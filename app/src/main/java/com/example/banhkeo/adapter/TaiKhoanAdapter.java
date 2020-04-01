package com.example.banhkeo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.banhkeo.R;
import com.example.banhkeo.model.Account;

import java.util.ArrayList;

public class TaiKhoanAdapter extends BaseAdapter {
    Context context;
    ArrayList<Account> arraytaikhoan;

    public TaiKhoanAdapter(Context context, ArrayList<Account> arraytaikhoan) {
        this.context = context;
        this.arraytaikhoan = arraytaikhoan;
    }

    @Override
    public int getCount() {
        return arraytaikhoan.size();
    }

    @Override
    public Object getItem(int position) {
        return arraytaikhoan.get(position);
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
            convertView = inflater.inflate(R.layout.dong_taikhoan, null);
            viewHolder.txtidtaikhoan = convertView.findViewById(R.id.textViewIDTaiKhoan);
            viewHolder.txtusername = convertView.findViewById(R.id.textViewUserName);
            viewHolder.txtemail = convertView.findViewById(R.id.textViewEmailTaiKhoan);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Account account = (Account) getItem(position);
        viewHolder.txtidtaikhoan.setText(String.valueOf(account.getId()));
        viewHolder.txtusername.setText("User Name: " + account.getUserName());
        viewHolder.txtemail.setText("Email: " + account.getEmail());

        return convertView;
    }

    public class ViewHolder {
        TextView txtidtaikhoan, txtusername, txtemail;
    }
}
