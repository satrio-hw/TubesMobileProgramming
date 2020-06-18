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
import android.widget.TextView;
import android.os.Bundle;

public class Inbox extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        requestPermissions();
        viewContact();
    }

    public void  viewContact(){
        TextView tv= (TextView)findViewById(R.id.viewInbox);
        String txt = getModelContact();
        tv.setText(txt);
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
        }else{

        }
    }

    public String getModelContact(){
        String smsBuilder = "";

        ContentResolver cr = this.getContentResolver();
        Cursor cur = cr.query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);
        if(cur.getCount()>0){
            int index_Address = cur.getColumnIndex("address");
            int index_Person = cur.getColumnIndex("person");
            int index_Body = cur.getColumnIndex("body");
            int index_Date = cur.getColumnIndex("date");
            int index_Type = cur.getColumnIndex("type");

            while(cur.moveToNext()) {
                String strAddress = cur.getString(index_Address);
                int intPerson = cur.getInt(index_Person);
                String strbody = cur.getString(index_Body);
                long longDate = cur.getLong(index_Date);
                int int_Type = cur.getInt(index_Type);

                smsBuilder+=("[ ");
                smsBuilder+=strAddress + ", ";
                smsBuilder+=intPerson + ", ";
                smsBuilder+=strbody + ", ";
                smsBuilder+=longDate + ", ";
                smsBuilder+=int_Type;
                smsBuilder+=" ]";
            }
        }else{
            smsBuilder+="no result!";
        }
        cur.close();

        return smsBuilder;
    }
    /*
    public String getModelContact(){
        String data;
        data = "";

        ContentResolver cr = this.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);
        if(cur.getCount()>0){
            Log.i("Content provider", "Reading contacts");
            short cnt;
            cnt=0;
            while(cur.moveToNext() && (cnt<600)) {
                cnt++;
                int noPhone = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String contactId = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                data += " " + cnt + " " + contactId + " " + noPhone + " Nama: " + name + "\n";


                data += "***,\n";
            }
        }else{
            data += " not found ";
        }
        cur.close();

        return data;
    }*/
}
