package com.example.my2020sms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DraftManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_management);


        final SQLiteHelper sqh = new SQLiteHelper(this);
        final SQLiteDatabase sqdb = sqh.getWritableDatabase();


        (findViewById(R.id.btnSaveDraft)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String draft_recipient = ((EditText)findViewById(R.id.recipientNumber)).getText().toString();
                String body_draft = ((EditText)findViewById(R.id.textDraft)).getText().toString();

                // WRITE
                ContentValues data_to_insert = new ContentValues();
                data_to_insert.put(SQLiteHelper.RECIEVER,draft_recipient);
                data_to_insert.put(SQLiteHelper.BODY,body_draft);
                sqdb.insert(SQLiteHelper.TABLE_NAME, SQLiteHelper.UID,data_to_insert);
                //sqdb.insert(SQLiteHelper.TABLE_NAME, SQLiteHelper.BODY,data_to_insert);

                sqdb.close();
                sqh.close();

                // Switch view
                Intent intent = new Intent(DraftManagement.this, DraftList.class);
                DraftManagement.this.startActivity(intent);

            }
        });

    }

    //Force go to DraftList when "back"nav pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DraftManagement.this, DraftList.class);
        DraftManagement.this.startActivity(intent);
    }
}
