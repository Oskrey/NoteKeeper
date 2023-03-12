package com.example.notes;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.notes.db.NotesContract;
import com.example.notes.db.NotesProvider;
import com.example.notes.ui.NotesAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = findViewById(R.id.notes_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Spinner spinHome = findViewById(R.id.spinner);
        notesAdapter = new NotesAdapter(null, onNoteClickListener, onNoteLongClickListener);
        recyclerView.setAdapter(notesAdapter);
        getLoaderManager().initLoader(
                0, // Идентификатор загрузчика
                null, // Аргументы
                this // Callback для событий загрузчика
        );
        findViewById(R.id.createNote_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
                startActivityForResult(intent,1);
            }
        });
        findViewById(R.id.createCat_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View  v) {
                Intent intent = new Intent(MainActivity.this, CreateCategoryActivity.class);
                startActivityForResult(intent,1);


            }
        });
        spinHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {

               if (selectedItemPosition+1 == 3)
                   notesAdapter = new NotesAdapter(getContentResolver().query(NotesContract.Notes.URI, null,    null,null,null), onNoteClickListener, onNoteLongClickListener);
               else
                   notesAdapter = new NotesAdapter(getContentResolver().query(NotesContract.Notes.URI, null, " category = ? ", new String[]{Integer.toString(selectedItemPosition + 1)}, null), onNoteClickListener, onNoteLongClickListener);

               recyclerView.setAdapter(notesAdapter);
           }
           public void onNothingSelected(AdapterView<?> parent) {
           }
        });


    }
    private void updateList(){
        NotesProvider np = new NotesProvider();
        ArrayList<String> list = np.GetCat(this);
        Spinner spinHome = findViewById(R.id.spinner);
        list.add("Все");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinHome.setAdapter(adapter);
        spinHome.setSelection(spinHome.getCount()-1);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,  // Контекст
                NotesContract.Notes.URI, // URI
                NotesContract.Notes.LIST_PROJECTION, // Столбцы
                null, // Параметры выборки
                null, // Аргументы выборки
                null // Сортировка по умолчанию
        );

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateList();
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i("Test", "Load finished: " + cursor.getCount());
        cursor.setNotificationUri(getContentResolver(), NotesContract.Notes.URI);
        notesAdapter.swapCursor(cursor);
        updateList();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Listener для клика по заметке
     */
    private final NotesAdapter.OnNoteClickListener onNoteClickListener = new NotesAdapter.OnNoteClickListener() {
        @Override
        public void onNoteClick(long noteId) {
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            intent.putExtra(NoteActivity.EXTRA_NOTE_ID, noteId);

            startActivity(intent);
        }
    };
    /**
     * Listener для долгого клика по заметке
     */
    private final NotesAdapter.OnNoteLongClickListener onNoteLongClickListener = new NotesAdapter.OnNoteLongClickListener() {
        @Override
        public void onNoteLongTap(long noteId) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.title_dialog_delete_warning)
                    .setItems(R.array.delete_warning, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                getContentResolver().delete(ContentUris.withAppendedId(NotesContract.Notes.URI, noteId),null,null);
                            } else if (which == 1) {
                                return;
                            }
                        }
                    })
                    .create();
            if (!isFinishing()) {
                alertDialog.show();
            }
        }
    };

}
