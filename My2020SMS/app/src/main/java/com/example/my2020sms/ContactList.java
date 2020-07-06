package com.example.my2020sms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ContactList extends AppCompatActivity {
    public static String rec_name=null;
    public static String from_contact=null;
    public static String rec_phonenumber=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Log.i("LOG_TAG", "Reading Contacts");

        requestPermissions();

        ContentResolver cr = this.getContentResolver();
        Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);

        short cnt;
        cnt = 0;


        final String[] name = new String[c.getCount()];
        final String[] phonenumber = new String[c.getCount()];
        final String[] contact_id = new String[c.getCount()];

        while (c.moveToNext() && (cnt < 600)) {

            int noPhone = c.getInt(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            name[cnt] = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            if (noPhone > 0) {
                Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=? ",
                        new String[]{contactId},
                        null);
                int countPhoneNumber = 0;
                while (phone.moveToNext()) {
                    if (countPhoneNumber <1) { //statement IF bila hanya ingin mencetak no tlp yang pertama saja
                        int phoneIdx = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String phoneNumber = phone.getString(phoneIdx);
                        phonenumber[cnt] = phoneNumber;
                        countPhoneNumber += 1;
                    } // statement IF untuk mencetak no pertama END
                }
                phone.close();
            }

            //Log.i("LOG_TAG","ID="+contact_id+" --> RECIEVER: "+name[cnt]+" ( "+phonenumber[cnt]+" )");

            cnt++;
        }

        listAdapterContact adapter = new listAdapterContact(this, name,phonenumber);

        ListView listView = (ListView) findViewById(R.id.contact_listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                rec_name = name[i];
                rec_phonenumber = phonenumber[i];
                from_contact= "yes";
                //Log.i("LOG_TAG","Clicked "+i+" name : "+name[i]+" phonenumber : "+phonenumber[i]);
                Intent intent = new Intent(ContactList.this, DraftManagement.class);
                ContactList.this.startActivity(intent);
            }
        });

    }

    int MY_PERMISSION_REQUEST_READ_CONTACTS = 9;
    public void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission denied
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSION_REQUEST_READ_CONTACTS);
            }
        }else{
            Toast.makeText(this, "Fail to read contact", Toast.LENGTH_SHORT).show();
        }
    }
}
