package com.example.basesdedatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.basesdedatos.classes.User;
import com.example.basesdedatos.model.ManageDB;
import com.example.basesdedatos.model.UserDAO;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //declaración de variables
    public static final String TAG= "Validate";
    private EditText etDocumento;
    private EditText etUsuario;
    private EditText etNombres;
    private EditText etBuscar;
    private EditText etApellidos;
    private EditText etContra;
    private ListView listUsers;
    private int documento;
    String usuario;
    String nombres;
    String apellidos;
    String contra;

    String buscar;


    ManageDB manageDB;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manageDB = new ManageDB(this); // Mover esta línea aquí

        begin();
        userList();
    }


//Metodo que llama los id del xml
    private void begin() {
        etDocumento = findViewById(R.id.etDocumento);
        etApellidos = findViewById(R.id.etApellidos);
        etNombres = findViewById(R.id.etNombre);
        etUsuario = findViewById(R.id.etUsuario);
        etContra = findViewById(R.id.etContra);
        etBuscar = findViewById(R.id.etBuscar);
        listUsers=findViewById(R.id.lvLista);

    }
    private boolean checkFields(){
        // Obtener valores de los campos
        String strDocumento = etDocumento.getText().toString().trim();
        usuario = etUsuario.getText().toString().trim();
        nombres = etNombres.getText().toString().trim();
        apellidos = etApellidos.getText().toString().trim();
        contra = etContra.getText().toString().trim();

        // Validamos datos, si algun campo está vacio, imprimimos un Toast que solicita ingresarlo
        if (strDocumento.isEmpty()) {
            showToast("Por favor, ingrese el documento.");
            return false;
        }
        if (usuario.isEmpty()) {
            showToast("Por favor, ingrese el usuario.");
            return false;
        }
        if (nombres.isEmpty()) {
            showToast("Por favor, ingrese los nombres.");
            return false;
        }
        if (apellidos.isEmpty()) {
            showToast("Por favor, ingrese los apellidos.");
            return false;
        }
        if (contra.isEmpty()) {
            showToast("Por favor, ingrese la contraseña.");
            return false;
        }

        // Convertir el documento a int
        try {
            documento = Integer.parseInt(strDocumento);
        } catch (NumberFormatException e) {
            showToast("El documento no es un número válido.");
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    public void registerUser(View view){
        if (checkFields()){
            // Asumiendo que un usuario registrado tendrá un status de 1
            User user = new User(documento, usuario, nombres, apellidos, contra, 1);
            manageDB.insertOrUpdateUser(user, this);  // Pasar 'this' como contexto
            userList();
        }
    }

    public void callUserList(View view) {
        if (checkFields()){

            User user = new User(documento, usuario, nombres, apellidos, contra, 1);
            long result = manageDB.insertOrUpdateUser(user, this);  // Pasar 'this' como contexto


            //Pequeño mensaje de prueba, verifico si se conectó correctamente y muestro un mensaje

            if (result != -1) {
                Toast.makeText(this, "Datos del usuario se han actualizado con éxito.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al actualizar los datos del usuario.", Toast.LENGTH_SHORT).show();
            }
            userList();
        } else {
            //Una pequeña validación de datos para que llenen todos los datos en actualizar
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void userList(){
        UserDAO userDAO = new UserDAO(this, listUsers);
        ArrayList<User> userArrayList = userDAO.getUserList();

        // Obtener el LinearLayout, esto se hace para poder usar la app como responsive
        LinearLayout linearUserList = findViewById(R.id.linearUserList);
        linearUserList.removeAllViews(); // Limpiar todas las vistas anteriores

        // Llenar el LinearLayout con los usuarios
        for (User user : userArrayList) {
            TextView textView = new TextView(this);
            textView.setText(user.toString());
            textView.setPadding(16, 16, 16, 16); // Añadir un poco de padding para mejor apariencia
            linearUserList.addView(textView);
        }
    }

    public void searchUser(View view) {
        String strDocument = etBuscar.getText().toString().trim();
        if (!strDocument.isEmpty()) {
            int document = Integer.parseInt(strDocument);
            UserDAO userDAO = new UserDAO(this, listUsers);
            User user = userDAO.searchUserByDocument(document);
            if (user != null) {
                ArrayList<User> userList = new ArrayList<>();
                userList.add(user);
                ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
                listUsers.setAdapter(adapter);
            } else {
                Toast.makeText(this, "El usuario " + strDocument + " no existe.", Toast.LENGTH_SHORT).show();
                userList();
            }
        } else {
            Toast.makeText(this, "Por favor, ingrese un documento para buscar.", Toast.LENGTH_SHORT).show();
        }
    }



//Método para borrar datos
    public void clearAllUsers(View view) {
        manageDB.deleteAllUsers();
        Toast.makeText(this, "Todos los usuarios han sido eliminados.", Toast.LENGTH_SHORT).show();
        userList(); // Actualizar la lista para reflejar que no hay usuarios.
    }
}