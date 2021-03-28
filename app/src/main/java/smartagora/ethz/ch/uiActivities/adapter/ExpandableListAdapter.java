package smartagora.ethz.ch.uiActivities.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import smartagora.ethz.ch.R;
import smartagora.ethz.ch.models.SliderMenuModel;

/**
 * ExpandableListAdapter is and adapter class for slide menu in HomeActivity to
 * show header and child items in slide menu.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<SliderMenuModel> mListDataHeader; // header titles

    // child data in format of header title, child title
    private final HashMap<SliderMenuModel, List<String>> mListDataChild;

    public ExpandableListAdapter(Context context, List<SliderMenuModel> listDataHeader, HashMap<SliderMenuModel, List<String>> listChildData) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
    }

    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)) .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        SliderMenuModel headerTitle = (SliderMenuModel) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(layoutInflater != null)
                convertView = layoutInflater.inflate(R.layout.side_menu_header_items, null);
            else {
                Log.e("ExpandableListAdapter", "getGroupView: LayoutInflater was null, using an empty View");
                convertView = new View(mContext);
            }
        }
        TextView lblListHeader = convertView
                .findViewById(R.id.submenu);
        ImageView headerIcon = convertView.findViewById(R.id.icon_image);
        lblListHeader.setText(headerTitle.getIconName());
        headerIcon.setImageResource(R.drawable.ic_menu_send);
        return convertView;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(layoutInflater != null)
                convertView = layoutInflater.inflate(R.layout.side_menu_header_items, null);
            else {
                Log.e("ExpandableListAdapter", "getGroupView: LayoutInflater was null, using an empty View");
                convertView = new View(mContext);
            }
        }

        TextView txtListChild = convertView
                .findViewById(R.id.submenu);
        txtListChild.setText(childText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
