package com.example.my2020sms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=0;
    String[] phoneNo;
    String[] sendtoID;
    String[] message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteHelper sqh = new SQLiteHelper(this);
        final SQLiteDatabase sqdb = sqh.getWritableDatabase();

        (findViewById(R.id.btnMessageSetting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DraftList.class);
                MainActivity.this.startActivity(intent);
            }
        });

        (findViewById(R.id.btnInbox)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Inbox.class);
                MainActivity.this.startActivity(intent);
            }
        });


        // Get Selected Draft

        Cursor c = sqdb.query(SQLiteHelper.TABLE_NAME,
                new  String[]{SQLiteHelper.UID, SQLiteHelper.RECIEVER, SQLiteHelper.RECNAME, SQLiteHelper.BODY, SQLiteHelper.SELECTED, SQLiteHelper.STATUS},
                null,null,null,null,null);

        final String[] reciever = new String[c.getCount()];
        final String[] body = new String[c.getCount()];
        final String[] status = new  String[c.getCount()];
        final String[] selected = new String[c.getCount()];
        final String[] id = new String[c.getCount()];
        int j = 0;

        while (c.moveToNext()){
            String selectedValue = c.getString(c.getColumnIndex(SQLiteHelper.SELECTED));
            if(c.getString(c.getColumnIndex(SQLiteHelper.SELECTED))!=null){
                id[j] = c.getString(c.getColumnIndex(SQLiteHelper.UID));
                reciever[j] = c.getString(c.getColumnIndex(SQLiteHelper.RECIEVER));
                body[j] = c.getString(c.getColumnIndex(SQLiteHelper.BODY));
                status[j] = c.getString(c.getColumnIndex(SQLiteHelper.STATUS));
                selected[j] = c.getString(c.getColumnIndex(SQLiteHelper.SELECTED));

                Log.i("LOG_TAG FIRST PAGE","ID="+id[j]+" --> RECIEVER: "+reciever[j]+" BODY:"+body[j]+" STATUS:"+status[j]+" SELECTED:"+c.getString(c.getColumnIndex(SQLiteHelper.SELECTED)));
                j++;
            }
        }

        phoneNo = reciever;
        sendtoID = id;
        message = body;

        //SEND SMS
        (findViewById(R.id.btnSendSMS)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int index_tochange=0; index_tochange<phoneNo.length;index_tochange++){
                    Log.i("LOG_TAG array :","phoneNo: "+phoneNo[index_tochange]+" --> message: "+message[index_tochange]+ "id:"+sendtoID[index_tochange]);
                    //update Status with timestamp START
                    if(sendtoID[index_tochange]!=null) {
                        ContentValues status_timestamp = new ContentValues();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String format = simpleDateFormat.format(new Date());
                        status_timestamp.put(SQLiteHelper.STATUS, " delivered :" + format + " ");
                        sqdb.update(SQLiteHelper.TABLE_NAME, status_timestamp, SQLiteHelper.UID + "=" + sendtoID[index_tochange], null);
                        Log.i("LOG_TAG", "Update Timestamp : " + format + " to ID : " + sendtoID[index_tochange]);
                        //update Status with timestamp END
                    }
                }

                sendSMSMessage();
            }
        });
    }

    protected void sendSMSMessage() {
        Log.i("LOG_TAG array :","IM HERE");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("LOG_TAG array :","IM HERE 1");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                Log.i("LOG_TAG array :","IM HERE 2");
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }else{
            Log.i("LOG_TAG array :","Something wrong");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        Log.i("LOG_TAG array :","IM HERE 3");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    for(int i=0; i<phoneNo.length;i++) {
                        if(phoneNo[i]!=null && message[i]!=null) {

                            Log.i("LOG_TAG array :","IM HERE 4");
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNo[i], null, "[MY2020SMS] :"+message[i], null, null);
                            Toast.makeText(getApplicationContext(), "SMS sent.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    /*
    private void sendSMSMessage() {

        SQLiteHelper sqh = new SQLiteHelper(this);
        SQLiteDatabase sqdb = sqh.getWritableDatabase();

        if(phoneNo.length!=0){
            SmsManager smsManager = SmsManager.getDefault();
            for(int i=0; i<phoneNo.length;i++){
                if(phoneNo[i]!=null && message[i]!=null){
                    Log.i("LOG_TAG Send SMS :","phoneNo: "+phoneNo[i]+" --> message: "+message[i]+" -- to ID: "+sendtoID[i]);

                    //update Status with timestamp START
                    ContentValues status_timestamp = new ContentValues();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String format = simpleDateFormat.format(new Date());
                    status_timestamp.put(SQLiteHelper.STATUS," delivered :"+format+" ");
                    sqdb.update(SQLiteHelper.TABLE_NAME,status_timestamp, SQLiteHelper.UID+"="+sendtoID[i], null);
                    Log.i("LOG_TAG", "Update Timestamp : "+format+" to ID : "+sendtoID[i]);
                    //update Status with timestamp END

                    smsManager.sendTextMessage(phoneNo[i], null, message[i], null, null);
                }
            }
        }
        //sqdb.close();
        //sqh.close();

        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
    }
     */

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
