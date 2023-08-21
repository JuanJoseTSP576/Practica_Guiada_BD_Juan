package com.example.basesdedatos.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.basesdedatos.classes.User;

public class ManageDB extends SQLiteOpenHelper {

    //Declaramos variables
    private static final String DATA_BASE_USERS = "dbusuarios";
    private Context context;
    private static final int VERSION = 2; // Incrementar la versión para forzar onUpgrade
    private static final String TABLE_USERS = "users";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_USERS +
            " (usu_document INTEGER PRIMARY KEY, usu_user varchar(100) NOT NULL, " +
            "usu_names varchar(100) NOT NULL, usu_last_names varchar(100) NOT NULL, usu_pass varchar(100) NOT NULL, usu_status INTEGER DEFAULT 1 NOT NULL);"; // Corregir la definición de usu_status

    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_USERS;

    public ManageDB(Context context) {
        super(context, DATA_BASE_USERS, null, VERSION);
        this.context = context;
    }


    //Este metodo permite añadir o actualizar usuarios
    public long insertOrUpdateUser(User user, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;

        ContentValues values = new ContentValues();
        values.put("usu_document", user.getDocument());
        values.put("usu_user", user.getUser());
        values.put("usu_names", user.getNames());
        values.put("usu_last_names", user.getLastNames());
        values.put("usu_pass", user.getPass());
        values.put("usu_status", user.getStatus());


        //validación, si ya existe añadimos un mensaje con la nueva actualización
        if (userExists(user.getDocument())) {
            result = db.update(TABLE_USERS, values, "usu_document = ?", new String[]{String.valueOf(user.getDocument())});
            if (result != -1) {
                Toast.makeText(context, "Usuario actualizado con éxito.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al actualizar el usuario.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // El usuario no existe, lo insertamos
            result = db.insert(TABLE_USERS, null, values);
            if (result != -1) {
                Toast.makeText(context, "Usuario añadido con éxito con ID: " + result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al añadir el usuario.", Toast.LENGTH_SHORT).show();
            }
        }

        db.close();
        return result;
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_USERS, null, null);
            Log.i("DATABASE", "Todos los usuarios han sido eliminados.");
        } catch (Exception e) {
            Log.e("DATABASE", "Error al eliminar todos los usuarios: " + e.getMessage());
        } finally {
            db.close();
        }
    }


    public boolean userExists(int document) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{"usu_document"},
                "usu_document = ?",
                new String[]{String.valueOf(document)},
                null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        Log.i("DATABASE", "se creó la tabla: " + CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN usu_status INTEGER DEFAULT 1"); // Cambiar "status" a "usu_status"
        }
        sqLiteDatabase.execSQL(DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }
}


