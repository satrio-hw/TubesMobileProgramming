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
        final ContactList get_contact = new ContactList();

        Log.i("LOG_TAG","ID Draft "+get_value.id_draft);

        Log.i("LOG_TAG","get contact : "+get_contact.rec_name+get_contact.rec_phonenumber);
        (findViewById(R.id.btnContactPhone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DraftManagement.this, ContactList.class);
                DraftManagement.this.startActivity(intent);
            }
        });

        // Declare All items in View
        EditText textBody = (findViewById(R.id.textDraft));
        EditText textReciever = (findViewById(R.id.recipientNumber));
        TextView textTitle = (findViewById(R.id.title));
        TextView textContactName = (findViewById(R.id.recipientName));

        // ------------------------------------------------------- Compose NEW DRAFT -------------------------------------------------------
        if(get_value.mode.compareTo("new")==0) {
            Log.i("LOG_TAG","Draft new draft, mode:"+get_value.mode);
            Button backBtn = (findViewById(R.id.btnBackDeleteDraft));

            if(get_contact.rec_name!=null && get_contact.rec_phonenumber != null){
                textReciever.setText(get_contact.rec_phonenumber);
                textContactName.setText("send to : "+get_contact.rec_name);
            }


            backBtn.setText("CANCEL");
            textTitle.setText("New Draft");

            // CheckBox Handling START
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
            // CheckBox Handling END

            //ADD Button Start
            (findViewById(R.id.btnSaveDraft)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String draft_recipient = ((EditText) findViewById(R.id.recipientNumber)).getText().toString();
                    String body_draft = ((EditText) findViewById(R.id.textDraft)).getText().toString();
                    String recipient_name = ((TextView) findViewById(R.id.recipientName)).getText().toString();

                    // WRITE
                    ContentValues data_to_insert = new ContentValues();
                    data_to_insert.put(SQLiteHelper.RECIEVER, draft_recipient);
                    if(recipient_name!=null){
                        data_to_insert.put(SQLiteHelper.RECNAME, recipient_name);
                    }
                    data_to_insert.put(SQLiteHelper.BODY, body_draft);
                    data_to_insert.put(SQLiteHelper.STATUS,"");
                    data_to_insert.put(SQLiteHelper.SELECTED, selectedVal[0]);
                    sqdb.insert(SQLiteHelper.TABLE_NAME, SQLiteHelper.UID, data_to_insert);

                    sqdb.close();
                    sqh.close();

                    //restart saved variable
                    get_contact.rec_phonenumber=null;
                    get_contact.rec_name=null;
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
            // CANCEL Button End

        }

        // ------------------------------------------------- EDIT or DELETE existing draft -------------------------------------------------------
        if(get_value.mode.compareTo("edit")==0) {
            Log.i("LOG_TAG","Draft edit, mode:"+get_value.mode);

            Button deleteBtn = (findViewById(R.id.btnBackDeleteDraft));

            deleteBtn.setText("DELETE DRAFT");
            textTitle.setText("Edit Draft");
            textBody.setText(get_value.draftRowBody);
            textReciever.setText(get_value.draftRowRecipient);
            textContactName.setText(get_value.draftRowRecipientName);


            if(get_contact.rec_name!=null && get_contact.rec_phonenumber != null && get_contact.from_contact != null){
                textReciever.setText(get_contact.rec_phonenumber);
                textContactName.setText("send to : "+get_contact.rec_name);
            }

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
                    String new_recipient_name = ((TextView) findViewById(R.id.recipientName)).getText().toString();

                    // UPDATE
                    ContentValues data_to_update = new ContentValues();
                    data_to_update.put(SQLiteHelper.RECIEVER, new_draft_recipient);
                    if(new_recipient_name!=null){
                        data_to_update.put(SQLiteHelper.RECNAME, new_recipient_name);
                    }
                    data_to_update.put(SQLiteHelper.BODY, new_body_draft);
                    data_to_update.put(SQLiteHelper.STATUS,"");
                    data_to_update.put(SQLiteHelper.SELECTED, selectedVal[0]);
                    sqdb.update(SQLiteHelper.TABLE_NAME,data_to_update, SQLiteHelper.UID+"="+get_value.id_draft, null);

                    sqdb.close();
                    sqh.close();

                    //restart saved variable
                    get_contact.rec_phonenumber=null;
                    get_contact.rec_name=null;
                    // Switch view
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
