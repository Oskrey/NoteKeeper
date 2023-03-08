package com.skillberg.notes.ui;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skillberg.notes.R;
import com.skillberg.notes.db.NotesContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Адаптер для заметок
 */
public class NotesAdapter extends CursorRecyclerAdapter<NotesAdapter.ViewHolder> {

    private final OnNoteClickListener onNoteClickListener;
    private int catID;
    public NotesAdapter(Cursor cursor, OnNoteClickListener onNoteClickListener, int catID) {
        super(cursor);
        this.catID = catID;


        this.onNoteClickListener = onNoteClickListener;
        return;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

        //if(catID == -1){
            int idColumnIndex = cursor.getColumnIndexOrThrow(NotesContract.Notes._ID);
            long id = cursor.getLong(idColumnIndex);

            viewHolder.itemView.setTag(id);

            int titleColumnIndex = cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_TITLE);
            String title = cursor.getString(titleColumnIndex);

            viewHolder.titleTv.setText(title);

            int dateColumnIndex = cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_UPDATED_TS);
            long updatedTs = cursor.getLong(dateColumnIndex);
            Date date = new Date(updatedTs);

            viewHolder.dateTv.setText(viewHolder.SDF.format(date));
        /*}
        else {//кринжатина
            int columnIndex = cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_CATEGORY);
            int cat = cursor.getInt(columnIndex);

            if (cat == catID){
                int idColumnIndex = cursor.getColumnIndexOrThrow(NotesContract.Notes._ID);
                long id = cursor.getLong(idColumnIndex);

                viewHolder.itemView.setTag(id);

                int titleColumnIndex = cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_TITLE);
                String title = cursor.getString(titleColumnIndex);

                viewHolder.titleTv.setText(title);

                int dateColumnIndex = cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_UPDATED_TS);
                long updatedTs = cursor.getLong(dateColumnIndex);
                Date date = new Date(updatedTs);

                viewHolder.dateTv.setText(viewHolder.SDF.format(date));
            }
            else return;
        }*/

    }
    //@Nullable
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //int columnIndex = cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_CATEGORY);
        //int cat = cursor.getInt(columnIndex);

        //if (cat == catID){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

            View view = layoutInflater.inflate(R.layout.view_item_note, parent, false);

            return new ViewHolder(view);
        // } else return null;

    }

    /**
     * View holder
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

        private final TextView titleTv;
        private final TextView dateTv;

        public ViewHolder(View itemView) {
            super(itemView);
            this.titleTv = itemView.findViewById(R.id.title_tv);
            this.dateTv = itemView.findViewById(R.id.date_tv);
            //if (catID == -1){


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long noteId = (Long) v.getTag();

                        onNoteClickListener.onNoteClick(noteId);
                    }
                });
           /* }else {//кринжатина
                int columnIndex = cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_CATEGORY);
                int cat = cursor.getInt(columnIndex);

                if (cat == catID){


                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long noteId = (Long) v.getTag();

                            onNoteClickListener.onNoteClick(noteId);
                        }
                    });

                } else return;
            }*/

        }

    }

    /**
     * Слушатель для обработки кликов
     */
    public interface OnNoteClickListener {
        void onNoteClick(long noteId);
    }
}
