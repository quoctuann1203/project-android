package com.example.learnenglish2;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class CateGridAdapter extends BaseAdapter {
    private List<String> cateList;

    public CateGridAdapter(List<String> cateList) {
        this.cateList = cateList;
    }

    @Override
    public int getCount() {
        return cateList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View view1;
        if (view == null) {
            view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        }
        else {
            view1 = view;
        }

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent.getContext(), QuizNumberActivity.class);
                intent.putExtra("CATEGORY",cateList.get(position));
                intent.putExtra("CATEGORY_ID",position + 1);
                parent.getContext().startActivity(intent);
            }
        });

        ((TextView) view1.findViewById(R.id.cateName)).setText(cateList.get(position));
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(255),random.nextInt(255),random.nextInt(255));
        view1.setBackgroundColor(color);
        return view1;
    }
}
