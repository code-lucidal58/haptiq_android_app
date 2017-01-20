package hackfest.pheonix.haptiq.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import hackfest.pheonix.haptiq.Constants;
import hackfest.pheonix.haptiq.Models.UserCredential;

/**
 * Created by aanisha
 */

public class UserCredentialsDB extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserCredentialsDatabase";
    private static final String TABLE_NAME = "UserCredentials";

    // Contacts Table Columns names
    private static final String KEY_ID = Constants.KEY_ID;
    private static final String KEY_USERNAME = Constants.KEY_USERNAME;
    private static final String KEY_PASSWORD = Constants.KEY_PASSWORD;
    private static final String KEY_URL = Constants.KEY_URL;

    public UserCredentialsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT," + KEY_URL + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new credential
    void addUserCredential(UserCredential uc) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, uc.getUsername());
        values.put(KEY_PASSWORD, uc.getPassword());
        values.put(KEY_URL, uc.getUrl());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single credential
    UserCredential getCredential(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_USERNAME, KEY_PASSWORD, KEY_URL}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        UserCredential userCredential = new UserCredential(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));

        return userCredential;
    }

    // Getting All Credentials
    public List<UserCredential> getAllCredentials() {
        List<UserCredential> credentialList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                UserCredential userCredential = new UserCredential();
                userCredential.setId(Integer.parseInt(cursor.getString(0)));
                userCredential.setUsername(cursor.getString(1));
                userCredential.setPassword(cursor.getString(2));
                userCredential.setUrl(cursor.getString(3));
                credentialList.add(userCredential);
            } while (cursor.moveToNext());
        }
        return credentialList;
    }

    // Updating single credential
    public int updateCredential(UserCredential userCredential) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, userCredential.getUsername());
        values.put(KEY_PASSWORD, userCredential.getPassword());
        values.put(KEY_URL, userCredential.getUrl());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(userCredential.getId()) });
    }

    // Deleting single credential
    public void deleteCredential(UserCredential userCredential) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(userCredential.getId()) });
        db.close();
    }


    // Getting credential count
    public int getCredentialCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }
}
