// MY2020SMS : R. Satrio Hariomurti Wicaksono (0102517023) ; Dyah Retno Palupi (0102517006)
package com.example.my2020sms;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class listAdapterContact extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] number;
    private final String[] name;

    public listAdapterContact(Activity context, String[] name, String[] number) {
        super(context, R.layout.contact_row, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.number=number;
        this.name=name;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.contact_row, null,true);

        TextView recieverName = (TextView) rowView.findViewById(R.id.recipientName);
        TextView recieverNumber = (TextView) rowView.findViewById(R.id.recipientPhoneNumber);

        recieverName.setText(name[position]);
        recieverNumber.setText(number[position]);

        //Log.i("LOG_TAG","listAdapterContact --> name[position] :"+name[position] );

        return rowView;

    };
}
