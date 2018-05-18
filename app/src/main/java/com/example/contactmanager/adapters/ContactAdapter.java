package com.example.contactmanager.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.contactmanager.R;
import com.example.contactmanager.database.models.Contact;


import java.util.List;

/**
 * Created by Fred Adewole on 18/05/2018.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    public List<Contact> contactList;

    public ContactAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;
        TextView contactNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.view_contact_name);
            contactNumber = itemView.findViewById(R.id.view_contact_number);
        }

        public void setValues(int position) {
            contactName.setText(contactList.get(position).getName());
            contactNumber.setText(contactList.get(position).getNumber());

            Log.e("Name", contactList.get(position).getName());
            Log.e("Number", contactList.get(position).getNumber());
        }
    }
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout contactLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_contact_card, parent, false);

        contactLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("LongPress", "called");
                return true;
            }
        });
        return new ViewHolder(contactLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        holder.setValues(position);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
