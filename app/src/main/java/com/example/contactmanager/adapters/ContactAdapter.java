package com.example.contactmanager.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.contactmanager.R;
import com.example.contactmanager.database.models.Contact;
import com.example.contactmanager.views.DetailActivity;


import java.util.List;

/**
 * Created by Fred Adewole on 18/05/2018.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    public List<Contact> contactList;
    TextView contactName, contactNumber;
    public ContactAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.view_contact_name);
            contactNumber = itemView.findViewById(R.id.view_contact_number);
        }

        public void setValues(int position) {
            contactName.setText(contactList.get(position).getName());
            contactNumber.setText(contactList.get(position).getNumber());
        }
    }
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout contactLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_contact_card, parent, false);
        return new ViewHolder(contactLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("NAME", contactList.get(position).getName());
                intent.putExtra("NUMBER", contactList.get(position).getNumber());
                intent.putExtra("ID", String.valueOf(contactList.get(position).getId()));
                v.getContext().startActivity(intent);
            }
        });
        holder.setValues(position);
    }


    public void refreshContacts(List<Contact> newContacts){
        contactList = newContacts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
