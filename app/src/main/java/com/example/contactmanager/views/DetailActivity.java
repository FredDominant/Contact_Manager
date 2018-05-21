package com.example.contactmanager.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.contactmanager.R;
import com.example.contactmanager.database.DatabaseHelper;
import com.example.contactmanager.database.models.Contact;

public class DetailActivity extends AppCompatActivity {
    Button editContact, deleteContact, saveUpdatedContact;
    DatabaseHelper database;
    String id;
    TextView nameText, numberText, editNameText, editNumberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        database = new DatabaseHelper(this);

        Intent intent = getIntent();
        String contactName = intent.getStringExtra("NAME");
        String contactNumber = intent.getStringExtra("NUMBER");
        id = intent.getStringExtra("ID");
        getSupportActionBar().setTitle(contactName);

        setValue(contactName, contactNumber);

        editContact = findViewById(R.id.button_edit_contact);
        deleteContact = findViewById(R.id.button_delete_contact);
        saveUpdatedContact = findViewById(R.id.button_save_contact);

        editContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditFields();
            }
        });

        deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
            }
        });

        saveUpdatedContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEditFields();
                hideSoftKey(v);
                updateContact(v);
            }
        });

    }

    private void setValue(String name, String number) {
        nameText = findViewById(R.id.contact_detail_name);
        numberText = findViewById(R.id.contact_detail_number);

        nameText.setText(name);
        numberText.setText(number);
    }

    private void deleteContact() {
        Contact contact = database.getSingleContact(Integer.parseInt(id));
        showConfirmDelete(contact);
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    private void updateContact(View v) {
        String name = editNameText.getText().toString();
        String number = editNumberText.getText().toString();

        if (name.isEmpty() || number.isEmpty()) {
            // Do nothing.
        } else {
            database.updateContact(new Contact(Integer.parseInt(id), number, name, null));
            Snackbar.make(v, "UPDATED", Snackbar.LENGTH_SHORT).show();
        }

    }

    private void showEditFields() {
        nameText.setVisibility(View.GONE);
        numberText.setVisibility(View.GONE);
        editContact.setVisibility(View.GONE);
        saveUpdatedContact.setVisibility(View.VISIBLE);

        editNameText = findViewById(R.id.editText_name);
        editNumberText = findViewById(R.id.editText_number);


        editNumberText.setVisibility(View.VISIBLE);
        editNumberText.setText(numberText.getText());

        editNameText.setVisibility(View.VISIBLE);
        editNameText.setText(nameText.getText());
    }

    private void hideEditFields() {
        nameText.setVisibility(View.VISIBLE);
        numberText.setVisibility(View.VISIBLE);
        editContact.setVisibility(View.VISIBLE);
        saveUpdatedContact.setVisibility(View.GONE);

        editNumberText.setVisibility(View.GONE);
        editNameText.setVisibility(View.GONE);

        numberText.setText(editNumberText.getText().toString());
        nameText.setText(editNameText.getText().toString());
    }

    public void hideSoftKey(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void showConfirmDelete(final Contact contact) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder
                .setCancelable(true)
                .setMessage("Delete " + nameText.getText().toString())
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.deleteContact(contact);
                        goToHomeActivity();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Intentionally left blank.
                    }
                });
        mBuilder.create().show();
    }

}
