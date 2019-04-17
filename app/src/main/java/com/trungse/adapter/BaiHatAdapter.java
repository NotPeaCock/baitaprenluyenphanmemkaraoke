package com.trungse.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trungse.baitaprenluyenphanmemkaraoke.MainActivity;
import com.trungse.baitaprenluyenphanmemkaraoke.R;
import com.trungse.model.BaiHat;

public class BaiHatAdapter extends ArrayAdapter<BaiHat> {
    Activity context;
    int resource;
    public BaiHatAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = this.context.getLayoutInflater().inflate(this.resource,null);
        TextView tvMa = view.findViewById(R.id.tv_ma);
        TextView tvTen = view.findViewById(R.id.tv_ten);
        TextView tvTacGia =  view.findViewById(R.id.tv_tacgia);
        final ImageView imgLike = view.findViewById(R.id.img_like);
        final ImageView imgDislike = view.findViewById(R.id.img_dislike);
        final BaiHat baiHat = getItem(position);
        tvMa.setText(baiHat.getMa());
        tvTen.setText(baiHat.getTen());
        tvTacGia.setText(baiHat.getTacgia());
        if(baiHat.getThich()==1){
          imgLike.setVisibility(View.INVISIBLE);
          imgDislike.setVisibility(View.VISIBLE);
        }else{
            imgLike.setVisibility(View.VISIBLE);
            imgDislike.setVisibility(View.INVISIBLE);
        }

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgLike.setVisibility(View.INVISIBLE);
                imgLike.setVisibility(View.VISIBLE);
                xuLyLike(baiHat);
            }
        });
        
        imgDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDislike.setVisibility(View.INVISIBLE);
                imgLike.setVisibility(View.VISIBLE);
                xuLyDislike(baiHat);
            }
        });
        return view;
    }

    private void xuLyDislike(BaiHat baiHat) {
        ContentValues values = new ContentValues();
        values.put("YEUTHICH",0);
        long ketqua = MainActivity.database.update(MainActivity.TableName, values,
                "Ma=?",new String[] {baiHat.getMa()});
        if(ketqua>0){
            Toast.makeText(context,
                    "Da go bo bai hat["+ baiHat.getTen()+"] khoi danh sach yeu thich.",
                    Toast.LENGTH_SHORT).show();
            if(MainActivity.selectedTab==1){
                remove(baiHat);
            }
        }else{
            Toast.makeText(context, "Them bai hat yeu thich that bai?", Toast.LENGTH_SHORT).show();
        }
    }

    private void xuLyLike(BaiHat baiHat) {
        ContentValues values = new ContentValues();
        values.put("YEUTHICH",1);
        long ketqua = MainActivity.database.update(MainActivity.TableName, values,
                "Ma=?",new String[] {baiHat.getMa()});
        if(ketqua>0){
            Toast.makeText(context,
                    "Da them bai hat["+ baiHat.getTen()+"] vao danh sach yeu thich.",
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Them bai hat yeu thich that bai?", Toast.LENGTH_SHORT).show();
        }
    }
}
