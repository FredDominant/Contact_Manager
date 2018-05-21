package com.example.contactmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.contactmanager.database.models.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Fred Adewole on 18/05/2018.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactManager_db";


    /**
     * Instantiates a new Database helper.
     *
     * @param context the context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default_avatar
     * @param version number of the database (starting at 1); if the database is older,                {@link #onUpgrade} will be used to upgrade the database; if the database is                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create contact manager db.
        db.execSQL(Contact.CREATE_TABLE);

    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Run migrations and stuff here.

        // Drop older table if it exists here
        db.execSQL("DROP TABLE IF EXISTS " + Contact.TABLE_NAME);

        // Then create tables again
        onCreate(db);
    }

    public long insertContact(String name, String number) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Add fields to row.
        ContentValues values = new ContentValues();
        values.put(Contact.COLUMN_NAME, name);
        values.put(Contact.COLUMN_NUMBER, number);

        // Add row to database.
        long id = db.insert(Contact.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public Contact getSingleContact(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Contact.TABLE_NAME,
                new String[] {Contact.COLUMN_ID, Contact.COLUMN_NAME, Contact.COLUMN_NUMBER,
                        Contact.COLUMN_TIMESTAMP}, Contact.COLUMN_ID + "= ?",
                new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        // create a Contact object.
        Contact contact = new Contact(
                cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NUMBER)),
                cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)),
                cursor.getString( cursor.getColumnIndex(Contact.COLUMN_TIMESTAMP))
               );
        cursor.close();
        return contact;
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();

        String query = "SELECT * FROM " + Contact.TABLE_NAME + " ORDER BY "
                + Contact.COLUMN_NAME + " DESC";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_ID)));
                contact.setNumber(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NUMBER)));
                contact.setName(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)));
                contact.setTimestamp(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_TIMESTAMP)));

                contacts.add(contact);
            } while (cursor.moveToNext());
        }

        db.close();
        return contacts;
    }

    public int getContactsCount() {
        String query = "SELECT * FROM " + Contact.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();

        db.close();

        return count;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        String name = contact.getName();
        String number = contact.getNumber();

        values.put(Contact.COLUMN_NUMBER, number);
        values.put(Contact.COLUMN_NAME, name);

        return db.update(Contact.TABLE_NAME, values, Contact.COLUMN_ID + "= ?",
                new String[] {String.valueOf(contact.getId())});
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Contact.TABLE_NAME, Contact.COLUMN_ID + "= ?",
                new String[] {String.valueOf(contact.getId())});

        db.close();
    }

    public void deleteAllContacts() {
        SQLiteDatabase db = getWritableDatabase();
        this.onUpgrade(db, 1, 2);
    }
}
