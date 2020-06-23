package com.example.my2020sms;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class listAdapterDraft extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] reciever;
    private final String[] body;
    //private final Integer[] imgid;

    public listAdapterDraft(Activity context, String[] reciever, String[] body) {
        super(context, R.layout.inbox_row, reciever);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.reciever=reciever;
        this.body=body;
        //this.imgid=imgid;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.draft_row, null,true);

        TextView recieverText = (TextView) rowView.findViewById(R.id.recipientNumber);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView bodyText = (TextView) rowView.findViewById(R.id.textBody);

        recieverText.setText(reciever[position]);
        //imageView.setImageResource(imgid[position]);
        bodyText.setText(body[position]);

        return rowView;

    };
}
