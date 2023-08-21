package com.example.basesdedatos.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import com.example.basesdedatos.classes.User;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class UserDAO {
    private ManageDB manageDB;
    Context context;
    View view;
    User user;

    public UserDAO(Context context, View view){
        this.context= context;
        this.view= view;
        manageDB = new ManageDB(context);
    }

    public void insertUser(User myUser){

        try {
            SQLiteDatabase db = manageDB.getWritableDatabase();
            if (db != null) {
                ContentValues values = new ContentValues();
                values.put("usu_document", myUser.getDocument());
                values.put("usu_user", myUser.getUser());
                values.put("usu_names", myUser.getNames());
                values.put("usu_last_names", myUser.getLastNames());
                values.put("usu_pass", myUser.getPass());
                values.put("usu_status", myUser.getStatus());


                long cod = db.insert("users", null, values);
                if (cod == -1) {
                    Log.e("DATABASE ERROR", "Error inserting data");
                    Snackbar.make(this.view, "Error registering user.", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(this.view, "The user register success: " + cod, Snackbar.LENGTH_LONG).show();
                }
            } else {
                Snackbar.make(this.view, "The user not register ERROR: ", Snackbar.LENGTH_LONG).show();
            }
        }catch (SQLException e){
            Log.i("ERROR",""+e);
        }
    }

    public ArrayList<User> getUserList(){
        SQLiteDatabase db = manageDB.getReadableDatabase();
        String query = "select * from users";
        ArrayList<User> userList = new ArrayList<User>();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                user = new User();
                user.setDocument(cursor.getInt(0));
                user.setUser(cursor.getString(1));
                user.setNames(cursor.getString(2));
                user.setLastNames(cursor.getString(3));
                user.setPass(cursor.getString(4));
                user.setStatus(cursor.getInt(5)); // Esta es la línea 68 que está causando el error
                userList.add(user);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

//Metodo auxiliar, sirve para obtener el índice de la columna y verificar si es-1, ya que me daba error el setDocument(cursor por esta misma razón)
    private int getColumnIndexOrThrow(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index == -1) {
            throw new IllegalArgumentException("Column " + columnName + " doesn't exist in the result set.");
        }
        return index;
    }

    //Metodo para buscar documento
    public User searchUserByDocument(int document) {
        SQLiteDatabase db = manageDB.getReadableDatabase();
        Cursor cursor = db.query("users", null, "usu_document=?", new String[]{String.valueOf(document)}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                User user = new User();
                user.setDocument(cursor.getInt(getColumnIndexOrThrow(cursor, "usu_document")));
                user.setUser(cursor.getString(getColumnIndexOrThrow(cursor, "usu_user")));
                user.setNames(cursor.getString(getColumnIndexOrThrow(cursor, "usu_names")));
                user.setLastNames(cursor.getString(getColumnIndexOrThrow(cursor, "usu_last_names")));
                user.setPass(cursor.getString(getColumnIndexOrThrow(cursor, "usu_pass")));
                user.setStatus(cursor.getInt(getColumnIndexOrThrow(cursor, "usu_status")));
                cursor.close();
                return user;
            }
            cursor.close();
        }
        return null;
    }

}