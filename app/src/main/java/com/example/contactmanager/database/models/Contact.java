package com.example.contactmanager.database.models;

/**
 * Created by Fred Adewole on 18/05/2018.
 */
public class Contact {
    /**
     * The constant TABLE_NAME.
     */
    public static final String TABLE_NAME = "contacts";

    /**
     * The constant COLUMN_ID.
     */
    public static final String COLUMN_ID = "id";
    /**
     * The constant COLUMN_NAME.
     */
    public static final String COLUMN_NAME = "name";
    /**
     * The constant COLUMN_NUMBER.
     */
    public static final String COLUMN_NUMBER = "number";
    /**
     * The constant COLUMN_TIMESTAMP.
     */
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String number;
    private String name;
    private String timestamp;

    /**
     * query to Contacts create table.
     */
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_NUMBER + " INTEGER,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Contact() {
        // Intentionally left blank
    }

    /**
     * Instantiates a new Contact.
     *  @param id        the id
     * @param number    the number
     * @param name      the name
     * @param timestamp the timestamp
     */
    public Contact(int id, String number, String name, String timestamp) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
