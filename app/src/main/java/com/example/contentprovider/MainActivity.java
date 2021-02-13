package com.example.contentprovider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //Declaracion de elementos de la interfaz
    private Button btnActualizarUpdate;
    private Button btnInsertar;
    private Button btnEliminar;
    private TextView txtResultados;
    private EditText txtEliminar;
    //Array de tipo string que contiene los
    //datos necesarios para el registro del cliente
    String[] projection = new String[] {
            MiProveedorContenido.Clientes._ID,
            MiProveedorContenido.Clientes.COL_NOMBRE,
            MiProveedorContenido.Clientes.COL_TELEFONO,
            MiProveedorContenido.Clientes.COL_EMAIL };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Relacion de las variables locales con los elementos de la interfaz
        btnActualizarUpdate =(Button)findViewById(R.id.btnActualizarUpdate);
        btnInsertar=(Button)findViewById(R.id.btnInsertar);
        btnEliminar=(Button)findViewById(R.id.btnEliminar);
        txtResultados=(TextView)findViewById(R.id.txtResultados);
        txtEliminar=(EditText)findViewById(R.id.txtEliminar);
        //Metodo que actualiza la tabla
        ActualizarTabla();
        //Metodos que se accionan al rcibir un click
        btnActualizarUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatos();
                ActualizarTabla();
            }
        });
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertandoNuevosBoton();
                ActualizarTabla();
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminandoRegistrosBoton(txtEliminar.getText().toString());
                ActualizarTabla();
            }
        });
    }

    private void updateDatos() {
        ContentValues values = new ContentValues();
        //LA instancia de la clase nos ayuda a retener los valores que se insertaran
        values.put(MiProveedorContenido.Clientes.COL_NOMBRE, "ActualizadoCliente" + txtEliminar.getText().toString());
        values.put(MiProveedorContenido.Clientes.COL_TELEFONO, ""+ new Random().nextInt(1000));
        values.put(MiProveedorContenido.Clientes.COL_EMAIL, "nuevoActualizado@email.com");
        //Clase que nos permite acceder a los datos de un proveedor de contenido
        ContentResolver cr = getContentResolver();
        String cad="='Cliente"+txtEliminar.getText().toString()+"'";
        cr.update(MiProveedorContenido.CONTENT_URI,values,MiProveedorContenido.Clientes.COL_NOMBRE+cad,null);
        Toast.makeText(this,"Actualizando Valor",Toast.LENGTH_LONG).show();
    }

    private void eliminandoRegistrosBoton(String value) {
        //Clase que nos permite acceder a los datos de un proveedor de contenido
        ContentResolver cr = getContentResolver();
        String cad="='Cliente"+value+"'";
        //Se elimina del registro el cliente con el valor similar a cad
        cr.delete(MiProveedorContenido.CONTENT_URI,
                MiProveedorContenido.Clientes.COL_NOMBRE + cad, null);
        Toast.makeText(this,"Registro Eliminado!",Toast.LENGTH_LONG).show();
    }


    private void ActualizarTabla(){
        Uri clientesUri =  MiProveedorContenido.CONTENT_URI;
        //Se accede a los datos que se devolveran
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(clientesUri,
                projection, //Columnas a devolver
                null,
                null,
                null);
        if (cur.moveToFirst())
        {
            String nombre;
            String telefono;
            String email;
            int colNombre = cur.getColumnIndex(MiProveedorContenido.Clientes.COL_NOMBRE);
            int colTelefono = cur.getColumnIndex(MiProveedorContenido.Clientes.COL_TELEFONO);
            int colEmail = cur.getColumnIndex(MiProveedorContenido.Clientes.COL_EMAIL);
            txtResultados.setText("");
            //Se utiliza el ciclo para escribir los reesultados extraidos
            do
            {
                nombre = cur.getString(colNombre);
                telefono = cur.getString(colTelefono);
                email = cur.getString(colEmail);

                txtResultados.append(nombre + " - " + telefono + " - " + email + "\n");

            } while (cur.moveToNext());
        }
    }

    public void insertandoNuevosBoton(){
        Random num=new Random();
        ContentValues values = new ContentValues();
        //La instancia del ContentValues nos ayuda a retener los datos que se insertaran
        values.put(MiProveedorContenido.Clientes.COL_NOMBRE, "NuevoCliente" + num.nextInt(1000));
        values.put(MiProveedorContenido.Clientes.COL_TELEFONO, ""+num.nextInt(1000));
        values.put(MiProveedorContenido.Clientes.COL_EMAIL, "nuevo@email.com");
        //Mediante la clase ContentResolver se podran enviar los elementis de la consulta al proveedor correcto
        ContentResolver cr = getContentResolver();
        cr.insert(MiProveedorContenido.CONTENT_URI, values);
        Toast.makeText(this,"Insertando nuevo valor",Toast.LENGTH_LONG).show();
    }

}
