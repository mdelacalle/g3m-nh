package glob3mobile.com.g3mnh;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

/**
 * Created by mdelacalle on 28/11/2016.
 */

public class MenuExpandableAdapter extends BaseExpandableListAdapter {


    private final Activity _parentActivity;
    private String[] groups = { "Solo", "En Familia",
            "En Pareja", "Con los Amigos" };
    private String[][] children = {{"Quiero ir a correr", "Quiero tomar algo"}, {"Dónde nos vamos a jugar?", "Queremos dar un paseo", "Emergencia! necesito Una farmacia!"}, {"Cena romántica", "Tomemos algo"}, {"Fiesta!"}};

    public MenuExpandableAdapter(Activity parentActivity) {
        this._parentActivity = parentActivity;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        int i = 0;
        i = children[groupPosition].length;
        return i;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return children[groupPosition][childPosition];
    }


    public Object getGroup(int groupPosition) {
        return groups[groupPosition];
    }

    public int getGroupCount() {
        return groups.length;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public TextView getGenericView() {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 150);
        TextView textView = new TextView(_parentActivity);
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        // Set the text starting position
        textView.setPadding(36, 10, 10, 10);
        return textView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        View rowparent = View.inflate(_parentActivity, R.layout.group_text, null);
        TextView textView = (TextView) rowparent.findViewById(R.id.group_text);
        textView.setText(getGroup(groupPosition).toString());
        return rowparent;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView = getGenericView();

        textView.setText(getChild(groupPosition, childPosition).toString());
        return textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
