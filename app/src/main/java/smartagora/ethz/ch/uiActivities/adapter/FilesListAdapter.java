package smartagora.ethz.ch.uiActivities.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import smartagora.ethz.ch.R;
import smartagora.ethz.ch.databases.DatabaseHandler;

/**
 * Adapter for showing files list in Load Trip.
 */

public class FilesListAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final List<String> fileName;
    private final DatabaseHandler databaseHandler;

    public FilesListAdapter(Context mContext, List<String> fileName) {
        super(mContext, R.layout.files_list_items, fileName);
        this.mContext = mContext;
        this.fileName = fileName;
        databaseHandler = new DatabaseHandler(mContext);
    }


    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView = inflater.inflate(R.layout.files_list_items, null, true);
        final TextView name = rowView.findViewById(R.id.fileName);

        final ImageView delete = rowView.findViewById(R.id.deleteButton);
        if (!fileName.get(position).equalsIgnoreCase(""))
            delete.setVisibility(View.VISIBLE);

        name.setText(fileName.get(position));
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHandler.deleteFile(fileName.get(position));
                fileName.remove(position);
                notifyDataSetChanged();

            }
        });

        return rowView;
    }

    @Override
    public boolean isEnabled(int position) {
        return !fileName.get(position).equalsIgnoreCase("");
    }
}

