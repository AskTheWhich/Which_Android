package com.which;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.which.data.db.WhichContract;

/**
 * Created by tomeramir on 01/09/2016.
 */
public class AskAdapter extends CursorAdapter {
    public AskAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.ask_item_layout, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if (cursor.moveToNext()) {
            viewHolder.askText.setText(cursor.getString(cursor.getColumnIndex(WhichContract.AskEntry.COLUMN_TEXT)));
        }
    }

    public static class ViewHolder {
        private final TextView askText;
        private final ImageButton leftImage;
        private final ImageButton rightImage;

        public ViewHolder(View view) {
            askText = (TextView) view.findViewById(R.id.ask_text_view);
            leftImage = (ImageButton) view.findViewById(R.id.left_image_button);
            rightImage = (ImageButton) view.findViewById(R.id.right_image_button);
        }
    }
}
