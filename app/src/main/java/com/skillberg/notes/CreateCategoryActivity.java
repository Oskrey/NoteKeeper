package com.skillberg.notes;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.skillberg.notes.db.NotesContract;

/**
 * Activity для создания новой заметки
 */
public class CreateCategoryActivity extends BaseNoteActivity {

    private TextInputEditText titleEt;

    private TextInputLayout titleTil;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleEt = findViewById(R.id.title_cat);

        titleTil = findViewById(R.id.title_cat_til);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.create_category, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                return true;

            case R.id.action_save:
                saveCat();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Метод для сохранения категории
     */
    private void saveCat() {
        String title = titleEt.getText().toString().trim();

        boolean isCorrect = true;

        if (TextUtils.isEmpty(title)) {
            isCorrect = false;

            titleTil.setError(getString(R.string.error_empty_field));
            titleTil.setErrorEnabled(true);
        } else {
            titleTil.setErrorEnabled(false);
        }


        if (isCorrect) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NotesContract.Categories.CATEGORIES_NAME, title);
            getContentResolver().insert(NotesContract.Categories.URI, contentValues);
            finish();
        }
    }



    @Override
    protected void displayNote(Cursor cursor) {

    }
}


