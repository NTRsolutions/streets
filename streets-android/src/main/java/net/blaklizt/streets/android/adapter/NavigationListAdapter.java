package net.blaklizt.streets.android.adapter;

/**
* User: tkaviya
* Date: 9/13/14
* Time: 1:56 PM
*/

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import net.blaklizt.streets.android.R;
import net.blaklizt.streets.android.activity.Startup;
import net.blaklizt.streets.android.common.Group;

public class NavigationListAdapter extends BaseExpandableListAdapter
{
	private final SparseArray<Group> groups;
	public LayoutInflater inflater;
	public Activity activity;

	public NavigationListAdapter(Activity act, SparseArray<Group> groups) {
		this.activity = act;
		this.groups = groups;
        this.inflater = act.getLayoutInflater();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
	                         boolean isLastChild, View convertView, ViewGroup parent) {
		final String children = (String) getChild(groupPosition, childPosition);
		TextView text;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_details, parent);
		}
		text = (TextView) convertView.findViewById(R.id.navigationChildItemText);
		text.setText(children);
		convertView.setOnClickListener(v -> Startup.getStreetsCommon().speak(children));
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).children.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.drawer_list_item, parent);
		}
		Group group = (Group) getGroup(groupPosition);
		((CheckedTextView) convertView).setText(group.string);
		((CheckedTextView) convertView).setChecked(isExpanded);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}
