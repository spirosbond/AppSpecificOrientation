package com.spydiko.rotationmanager;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PuR3v1L on 7/8/2013.
 */
public class InteractiveArrayAdapter extends ArrayAdapter<Model> {

	private final List<Model> list;
	private final Activity context;
	AppSpecificOrientation myapp;

	public InteractiveArrayAdapter(Activity context, List<Model> list, AppSpecificOrientation myapp) {
		super(context, R.layout.app_row, list);
		this.context = context;
		this.list = list;
		this.myapp = myapp;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.app_row, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) view.findViewById(R.id.list_item_view);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.list_item_check);
            viewHolder.landscape = (CheckBox) view.findViewById(R.id.list_item_landscape);
			viewHolder.label = (ImageView) view.findViewById(R.id.imageView);
			viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Model element = (Model) viewHolder.checkbox.getTag();
					Log.d("Adapter", "enter");
					element.setSelectedPortrait(buttonView.isChecked());
					myapp.savePreferences(element.getPackageName(), element.isSelectedPortrait(),true);
					Log.d("Adapter", element.getName() + " " + element.isSelectedPortrait());
				}
			});
            viewHolder.landscape.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Model element = (Model) viewHolder.landscape.getTag();
                    Log.d("Adapter", "enter");
                    element.setSelectedLandscape(buttonView.isChecked());
                    myapp.savePreferences(element.getPackageName(), element.isSelectedLandscape(),false);
                    Log.d("Adapter", element.getName() + " " + element.isSelectedLandscape());
                }
            });
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));
            viewHolder.landscape.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
            ((ViewHolder) view.getTag()).landscape.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.text.setText(list.get(position).getName());
		holder.checkbox.setChecked(list.get(position).isSelectedPortrait());
        holder.landscape.setChecked(list.get(position).isSelectedLandscape());
		holder.label.setImageDrawable(list.get(position).getLabel());
		return view;
	}

	static class ViewHolder {
		protected TextView text;
		protected CheckBox checkbox;
        protected CheckBox landscape;
		protected ImageView label;
	}
}



