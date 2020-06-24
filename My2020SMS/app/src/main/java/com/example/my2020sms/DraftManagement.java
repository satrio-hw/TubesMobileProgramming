package com.example.my2020sms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DraftManagement extends AppCompatActivity {

    private Button btnDeleteDraft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_management);


        final SQLiteHelper sqh = new SQLiteHelper(this);
        final SQLiteDatabase sqdb = sqh.getWritableDatabase();

        final DraftList get_value = new DraftList();
        Log.i("LOG_TAG","ID Draft "+get_value.id_draft);

        // Compose NEW DRAFT
        if(get_value.mode.compareTo("new")==0) {
            Log.i("LOG_TAG","Draft new draft, mode:"+get_value.mode);
            Button backBtn = (findViewById(R.id.btnBackDeleteDraft));
            TextView textTitle = (findViewById(R.id.title));


            backBtn.setText("CANCEL");
            textTitle.setText("New Draft");

            // CheckBox Handling
            final CheckBox checkboxSelected = (CheckBox)findViewById(R.id.checkSelected);
            final String[] selectedVal = new String[1];

            checkboxSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((CheckBox) view).isChecked()){
                        selectedVal[0] ="selected";
                    }else {
                        selectedVal[0] ="";
                    }
                    Log.i("LOG_TAG","CheckBox = "+selectedVal[0]);
                }
            });

            //ADD Button Start
            (findViewById(R.id.btnSaveDraft)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String draft_recipient = ((EditText) findViewById(R.id.recipientNumber)).getText().toString();
                    String body_draft = ((EditText) findViewById(R.id.textDraft)).getText().toString();

                    // WRITE
                    ContentValues data_to_insert = new ContentValues();
                    data_to_insert.put(SQLiteHelper.RECIEVER, draft_recipient);
                    data_to_insert.put(SQLiteHelper.BODY, body_draft);
                    data_to_insert.put(SQLiteHelper.SELECTED, selectedVal[0]);
                    sqdb.insert(SQLiteHelper.TABLE_NAME, SQLiteHelper.UID, data_to_insert);

                    sqdb.close();
                    sqh.close();

                    // Switch view
                    Intent intent = new Intent(DraftManagement.this, DraftList.class);
                    DraftManagement.this.startActivity(intent);
                }
            });
            //ADD Button End

            // CANCEL Button Start
            (findViewById(R.id.btnBackDeleteDraft)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DraftManagement.this, DraftList.class);
                    DraftManagement.this.startActivity(intent);
                }
            });
            // CANSEL Button End

        }

        // EDIT or DELETE existing draft
        if(get_value.mode.compareTo("edit")==0) {
            Log.i("LOG_TAG","Draft edit, mode:"+get_value.mode);
            Button deleteBtn = (findViewById(R.id.btnBackDeleteDraft));
            EditText textBody = (findViewById(R.id.textDraft));
            EditText textReciever = (findViewById(R.id.recipientNumber));
            TextView textTitle = (findViewById(R.id.title));

            deleteBtn.setText("DELETE DRAFT");
            textTitle.setText("Edit Draft");
            textBody.setText(get_value.draftRowBody);
            textReciever.setText(get_value.draftRowRecipient);

            // CheckBox Handling
            final CheckBox checkboxSelected = (CheckBox)findViewById(R.id.checkSelected);
            final String[] selectedVal = new String[1];

            checkboxSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((CheckBox) view).isChecked()){
                        selectedVal[0] ="selected";
                    }else {
                        selectedVal[0] ="";
                    }
                    Log.i("LOG_TAG","CheckBox = "+selectedVal[0]);
                }
            });

            //DELETE Button Start
            (findViewById(R.id.btnBackDeleteDraft)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sqdb.delete(SQLiteHelper.TABLE_NAME, SQLiteHelper.UID+"="+get_value.id_draft, null);
                    sqdb.close();
                    sqh.close();

                    Intent intent = new Intent(DraftManagement.this, DraftList.class);
                    DraftManagement.this.startActivity(intent);
                }
            });
            //DELETE Button End

            //UPDATE Button Start
            (findViewById(R.id.btnSaveDraft)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String new_draft_recipient = ((EditText) findViewById(R.id.recipientNumber)).getText().toString();
                    String new_body_draft = ((EditText) findViewById(R.id.textDraft)).getText().toString();

                    // UPDATE
                    ContentValues data_to_update = new ContentValues();
                    data_to_update.put(SQLiteHelper.RECIEVER, new_draft_recipient);
                    data_to_update.put(SQLiteHelper.BODY, new_body_draft);
                    data_to_update.put(SQLiteHelper.SELECTED, selectedVal[0]);
                    sqdb.update(SQLiteHelper.TABLE_NAME,data_to_update, SQLiteHelper.UID+"="+get_value.id_draft, null);

                    sqdb.close();
                    sqh.close();

                    Intent intent = new Intent(DraftManagement.this, DraftList.class);
                    DraftManagement.this.startActivity(intent);
                }
            });
            //UPDATE Button End

        }

    }

    //Force go to DraftList when "back"nav pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DraftManagement.this, DraftList.class);
        DraftManagement.this.startActivity(intent);
    }
}
