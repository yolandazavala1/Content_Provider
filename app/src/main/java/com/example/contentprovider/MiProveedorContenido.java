package com.example.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MiProveedorContenido extends ContentProvider {
    private static final String uri =
            "content://com.example.contentprovider/clientes";
    public static final Uri CONTENT_URI = Uri.parse(uri);


    private ContactosSqliteHelper clidbh;
    private static final String BD_NOMBRE = "DBClientes";
    private static final int BD_VERSION = 1;
    private static final String TABLA_CLIENTES = "Clientes";
    //UriMatcher
    private static final int CLIENTES = 1;
    private static final int CLIENTES_ID = 2;
    private static final UriMatcher uriMatcher;
    //Inicializamos el UriMatcher

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.contentprovider/clientes", "clientes", CLIENTES);
        uriMatcher.addURI("com.example.contentprovider/clientes", "clientes/#", CLIENTES_ID);
    }

    @Override
    public boolean onCreate() {
        clidbh = new ContactosSqliteHelper(
                getContext(), BD_NOMBRE, null, BD_VERSION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if(uriMatcher.match(uri) == CLIENTES_ID){
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = clidbh.getWritableDatabase();

        Cursor c = db.query(TABLA_CLIENTES, projection, where,
                selectionArgs, null, null, sortOrder);

        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match)
        {
            case CLIENTES:
                return "vnd.android.cursor.dir/vnd.example.cliente";
            case CLIENTES_ID:
                return "vnd.android.cursor.item/vnd.example.cliente";
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long regId = 1;
        SQLiteDatabase db = clidbh.getWritableDatabase();
        regId = db.insert(TABLA_CLIENTES, null, values);
        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, regId);
        return newUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int cont;
        String where = selection;
        if(uriMatcher.match(uri) == CLIENTES_ID){
            where = "_id=" + uri.getLastPathSegment();
        }
        SQLiteDatabase db = clidbh.getWritableDatabase();
        cont = db.delete(TABLA_CLIENTES, where, selectionArgs);
        return cont;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int cont;

        String where = selection;
        if(uriMatcher.match(uri) == CLIENTES_ID){
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = clidbh.getWritableDatabase();

        cont = db.update(TABLA_CLIENTES, values, where, selectionArgs);

        return cont;
    }


    public static final class Clientes implements BaseColumns
    {
        private Clientes() {}

        public static final String COL_NOMBRE = "nombre";
        public static final String COL_TELEFONO = "telefono";
        public static final String COL_EMAIL = "email";
    }
}
