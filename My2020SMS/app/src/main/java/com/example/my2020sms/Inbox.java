// MY2020SMS : R. Satrio Hariomurti Wicaksono (0102517023) ; Dyah Retno Palupi (0102517006)
package com.example.my2020sms;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inbox extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        Log.i("LOG_TAG array :","Inbox");
        requestPermissions();
        viewinbox();
    }

    public void  viewinbox(){
        Log.i("LOG_TAG array :","viewInbox");
        List<List> p = getModelSms();
        String[] maintitle = new String[p.get(0).size()];
        for (int j = 0; j < p.get(0).size(); j++) {
            maintitle[j] = p.get(0).get(j).toString();
        }

        String[] subtitle = new String[p.get(2).size()];
        for (int j = 0; j < p.get(2).size(); j++) {
            subtitle[j] = p.get(2).get(j).toString();
        }

        Integer[] imgid= new Integer[p.get(1).size()];
        for (int j = 0; j < p.get(1).size(); j++) {
            if(p.get(1).get(j).toString().compareTo("1")==0) {
                imgid[j] = R.drawable.ic_open_envelope;
            }else{
                imgid[j] = R.drawable.ic_close_envelope;
            }
        }

        listAdapterInbox adapter=new listAdapterInbox(this, maintitle,subtitle,imgid);

        ListView listView = (ListView) findViewById(R.id.inbox_list);
        listView.setAdapter(adapter);
    }

    int MY_PERMISSION_REQUEST_READ_SMS = 9;
    public void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission denied
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},
                        MY_PERMISSION_REQUEST_READ_SMS);
            }
        }
    }

    public List<List> getModelSms(){
        Log.i("LOG_TAG array :","getModelSms");

        // ArrayList<String> items_inbox = new ArrayList<String>();
        ArrayList<String> items_inbox_sender = new ArrayList<String>();
        ArrayList<Integer> items_inbox_stat = new ArrayList<Integer>();
        ArrayList<String> items_inbox_body = new ArrayList<String>();
        List<List> items_inbox = new ArrayList<List>();

        ContentResolver cr = this.getContentResolver();
        Cursor cur = cr.query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);
        if(cur.getCount()>0){
            int index_Address = cur.getColumnIndex("address");
            int index_Read = cur.getColumnIndex("read");
            int index_Body = cur.getColumnIndex("body");
            int index_Date = cur.getColumnIndex("sc_timestamp");
            int index_Type = cur.getColumnIndex("type");

            while(cur.moveToNext()) {
                String strAddress = cur.getString(index_Address);
                int statRead = cur.getInt(index_Read);
                String strbody = cur.getString(index_Body);
                int longDate = cur.getType(index_Date);
                int int_Type = cur.getInt(index_Type);
                String tocompare = "[MY2020SMS]";

                //Log.i("LOG_TAG :","SMS Inbox Body : "+strbody);
                //Log.i("LOG_TAG :","SMS Inbox Body(bool) : "+strbody.regionMatches(0, tocompare, 0, 10));
                if(strbody.regionMatches(0, tocompare, 0, 10)) {
                    items_inbox_sender.add(strAddress);
                    items_inbox_stat.add(statRead);
                    items_inbox_body.add(strbody);
                }
            }
            items_inbox.add(items_inbox_sender);
            items_inbox.add(items_inbox_stat);
            items_inbox.add(items_inbox_body);

        }else{

        }
        int countSMS = cur.getCount();
        cur.close();

        return (items_inbox);


    }
}