package com.example.my2020sms;

import android.app.Activity;
import android.util.Log;
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
    private final String[] selected;
    private final String[] status;

    public listAdapterDraft(Activity context, String[] reciever, String[] body, String[] selected, String[] status) {
        super(context, R.layout.inbox_row, reciever);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.reciever=reciever;
        this.body=body;
        this.selected=selected;
        this.status=status;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.draft_row, null,true);

        TextView recieverText = (TextView) rowView.findViewById(R.id.recipientNumber);
        TextView statusText = (TextView) rowView.findViewById(R.id.sendStatus);
        TextView bodyText = (TextView) rowView.findViewById(R.id.textBody);
        ImageView selectedIcon = (ImageView) rowView.findViewById(R.id.selectedIcon);

        recieverText.setText(reciever[position]);
        statusText.setText(status[position]);
        bodyText.setText(body[position]);

        if(selected[position]!=null){
            selectedIcon.setImageResource(R.drawable.ic_check_circle);
        }else{
            selectedIcon.setImageResource(R.drawable.ic_check_circle_transparant);
        }

        //Log.i("LOG_TAG","listAdapterDraft --> selectedIcon[position] :"+selected[position] );

        return rowView;

    };
}
