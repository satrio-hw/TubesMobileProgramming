// MY2020SMS : R. Satrio Hariomurti Wicaksono (0102517023) ; Dyah Retno Palupi (0102517006)
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DraftList extends AppCompatActivity {

    public static String id_draft;
    public static String mode;
    public static String add_mode;
    public static String draftRowBody;
    public static String draftRowRecipient;
    public static String draftRowRecipientName;
    public static String draftRowStatus;
    public static String draftRowSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_list);

        /* Btn Create NEW DRAFT START */
        (findViewById(R.id.btnNewDraft)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = "new";
                add_mode= null;
                Intent intent = new Intent(DraftList.this, DraftManagement.class);
                DraftList.this.startActivity(intent);
            }
        });
        /* Btn Create NEW DRAFT END */


        SQLiteHelper sqh = new SQLiteHelper(this);
        SQLiteDatabase sqdb = sqh.getWritableDatabase();

        // READ
        Cursor c = sqdb.query(SQLiteHelper.TABLE_NAME,
                new  String[]{SQLiteHelper.UID, SQLiteHelper.RECIEVER,SQLiteHelper.RECNAME, SQLiteHelper.BODY, SQLiteHelper.SELECTED, SQLiteHelper.STATUS},
                null,null,null,null,null);

        final String[] reciever = new String[c.getCount()];
        final String[] nametodisplay = new String[c.getCount()];
        final String[] body = new String[c.getCount()];
        final String[] status = new  String[c.getCount()];
        final String[] selected = new String[c.getCount()];
        final int[] id = new int[c.getCount()];
        int j = 0;
        while (c.moveToNext()){
            id[j] = c.getInt(c.getColumnIndex(SQLiteHelper.UID));
            nametodisplay[j] = c.getString(c.getColumnIndex(SQLiteHelper.RECNAME));
            reciever[j] = c.getString(c.getColumnIndex(SQLiteHelper.RECIEVER));
            body[j] = c.getString(c.getColumnIndex(SQLiteHelper.BODY));
            status[j] = c.getString(c.getColumnIndex(SQLiteHelper.STATUS));
            selected[j] = c.getString(c.getColumnIndex(SQLiteHelper.SELECTED));

            Log.i("LOG_TAG","RECIPIENT NAME = "+c.getString(c.getColumnIndex(SQLiteHelper.RECNAME)));

            Log.i("LOG_TAG","ID="+id[j]+" --> RECIEVER: "+reciever[j]+" BODY:"+body[j]+" STATUS:"+status[j]+" SELECTED:"+c.getString(c.getColumnIndex(SQLiteHelper.SELECTED)));
            j++;
        }

        listAdapterDraft adapter = new listAdapterDraft(this,nametodisplay, reciever,body,selected,status);

        ListView listView = (ListView) findViewById(R.id.draft_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("LOG_TAG","Clicked "+i+" Draft id: "+id[i]);
                id_draft = Integer.toString(id[i]);
                draftRowBody = body[i];
                draftRowRecipient = reciever[i];
                draftRowRecipientName = nametodisplay[i];
                draftRowSelected = selected[i];
                draftRowStatus = status[i];
                mode = "edit";
                add_mode= null;
                Intent intent = new Intent(DraftList.this, DraftManagement.class);
                DraftList.this.startActivity(intent);
            }
        });


    }

    //Force go to MainActivity when "back"nav pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DraftList.this, MainActivity.class);
        DraftList.this.startActivity(intent);
    }
}
