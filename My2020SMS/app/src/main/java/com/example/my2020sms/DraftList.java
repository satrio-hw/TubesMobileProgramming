package com.example.my2020sms;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DraftList extends AppCompatActivity {

    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X","Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X","Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X","Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X","Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_list);

        /* Btn Create NEW DRAFT START */
        (findViewById(R.id.btnNewDraft)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DraftList.this, DraftManagement.class);
                DraftList.this.startActivity(intent);
            }
        });
        /* Btn Create NEW DRAFT END */


        SQLiteHelper sqh = new SQLiteHelper(this);
        SQLiteDatabase sqdb = sqh.getWritableDatabase();

        // READ
        Cursor c = sqdb.query(SQLiteHelper.TABLE_NAME,
                new  String[]{SQLiteHelper.UID, SQLiteHelper.RECIEVER, SQLiteHelper.BODY},
                null,null,null,null,null);

        String[] reciever = new String[c.getCount()];
        String[] body = new String[c.getCount()];
        int j = 0;
        while (c.moveToNext()){
            int id = c.getInt(c.getColumnIndex(SQLiteHelper.UID));
            /*
            String reciever =
                    c.getString(c.getColumnIndex(SQLiteHelper.RECIEVER));
            */
            reciever[j] = c.getString(c.getColumnIndex(SQLiteHelper.RECIEVER));
            body[j] = c.getString(c.getColumnIndex(SQLiteHelper.BODY));
            Log.i("LOG_TAG","ROW "+id+" IS RECIEVER "+reciever[j]+" c.Count = "+c.getCount());
            j++;
        }

        listAdapterDraft adapter = new listAdapterDraft(this, reciever,body);

        ListView listView = (ListView) findViewById(R.id.draft_list);
        listView.setAdapter(adapter);



    }

    //Force go to MainActivity when "back"nav pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DraftList.this, MainActivity.class);
        DraftList.this.startActivity(intent);
    }
}
