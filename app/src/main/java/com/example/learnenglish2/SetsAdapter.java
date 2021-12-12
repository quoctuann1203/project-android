package com.example.learnenglish2;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SetsAdapter extends BaseAdapter {
    private int numberOfSets;

    public SetsAdapter(int numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

    @Override
    public int getCount() {
        return numberOfSets;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 5;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.number_item,parent,false);
        }
        else  {
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent.getContext(),QuestionActivity.class);
                intent.putExtra("SETNO",position+1);
                parent.getContext().startActivity(intent);
            }
        });

        ((TextView) view.findViewById(R.id.setNo_tv)).setText(String.valueOf((position + 4)+1));
        return view;
    }
}
