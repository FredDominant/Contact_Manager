package com.example.contactmanager.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.contactmanager.R;
import com.example.contactmanager.adapters.ContactAdapter;
import com.example.contactmanager.database.DatabaseHelper;
import com.example.contactmanager.database.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private DatabaseHelper database;
    private final List<Contact> contacts = new ArrayList<>();
    TextView noContactsTextView;
    RecyclerView recyclerView;
    ContactAdapter contactAdapter;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.layout_recycler_view);
        noContactsTextView = findViewById(R.id.text_no_contact);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAddContactDialog();
            }
        });

        database = new DatabaseHelper(getApplicationContext());
        contacts.addAll(database.getAllContacts());

        if (contacts.isEmpty()) {
            displayNoContact();
        } else {
            displayContacts(contacts);
        }

    }

    private void displayContacts(List<Contact> listOfContacts) {
        recyclerView.setVisibility(View.VISIBLE);
        noContactsTextView.setVisibility(View.GONE);
        
        contactAdapter = new ContactAdapter(listOfContacts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(contactAdapter);
    }

    private void displayNoContact() {
        recyclerView.setVisibility(View.GONE);
        noContactsTextView.setVisibility(View.VISIBLE);
    }

    private void displayAddContactDialog() {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.partial_contact_dialog, null);
        final EditText nameField = view.findViewById(R.id.edit_contact_name);
        final EditText numberField = view.findViewById(R.id.edit_contact_number);
        Button addContactButton = view.findViewById(R.id.button_add_contact);

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameField.getText().toString().isEmpty()
                        || numberField.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "One or more field is empty",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    database.insertContact(nameField.getText().toString(),
                            numberField.getText().toString());
                    nameField.setText("");
                    numberField.setText("");
                    refreshAdapter();
                    closeDialog();
                    Snackbar.make(recyclerView, nameField.getText() + " added.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });

        mBuilder.setView(view);
        dialog = mBuilder.create();
        dialog.show();
    }

    private void closeDialog() {
        dialog.cancel();
    }

    public void dropContacts(View view) {
        database.deleteAllContacts();
    }

    public void refreshAdapter() {
        List<Contact> contacts = database.getAllContacts();
        if (contacts.isEmpty()) {
            displayNoContact();
        } else {
            displayContacts(contacts);
            contactAdapter.refreshContacts(contacts);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume", "on resume called");
        refreshAdapter();
    }

}
