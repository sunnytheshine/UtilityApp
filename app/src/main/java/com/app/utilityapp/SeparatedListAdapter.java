package com.app.utilityapp;

/**
 * Created by sukhvindersingh on 2017-09-16.
 */

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class SeparatedListAdapter extends BaseAdapter {
    List<PackageInfo> packageList;
    Activity context;
    PackageManager packageManager;
    private int lastPosition = -1;
    boolean[] itemChecked;

    public SeparatedListAdapter(Activity context, List<PackageInfo> packageList,
                                PackageManager packageManager) {
        super();
        this.context = context;
        this.packageList = packageList;
        this.packageManager = packageManager;
        itemChecked = new boolean[packageList.size()];
    }

    private class ViewHolder {
        TextView apkName;
        CheckBox cbBox;
    }

    public int getCount() {
        return packageList.size();
    }

    public Object getItem(int position) {
        return packageList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item, null);
            holder = new ViewHolder();

            holder.apkName = (TextView) convertView.findViewById(R.id.appname);
            holder.cbBox = (CheckBox) convertView.findViewById(R.id.cbBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PackageInfo packageInfo = (PackageInfo) getItem(position);
        Drawable appIcon = packageManager
                .getApplicationIcon(packageInfo.applicationInfo);
        String appName = packageManager.getApplicationLabel(
                packageInfo.applicationInfo).toString();
        String appconfigname = packageInfo.packageName;

        appIcon.setBounds(0, 0, 40, 40);
        holder.apkName.setCompoundDrawables(appIcon, null, null, null);
        holder.apkName.setCompoundDrawablePadding(15);

        int appNameSize = appName.length();
        if(appNameSize>=20)
        {
            String temp1 = appName.substring(0,20);
            String temp2 = temp1+"...";
            holder.apkName.setText(temp2);
        }
        else
        {
            holder.apkName.setText(appName);
        }

        // holder.cbBox.setOnCheckedChangeListener(myCheckChangList);
        holder.cbBox.setTag(appconfigname);
        holder.cbBox.setChecked(false);
        holder.cbBox.setOnCheckedChangeListener((Setting) context);
        if (itemChecked[position])
            holder.cbBox.setChecked(true);
        else
            holder.cbBox.setChecked(false);

        holder.cbBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (holder.cbBox.isChecked())
                    itemChecked[position] = true;
                else
                    itemChecked[position] = false;
            }
        });
        //set the google plus animation
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;
        return convertView;
    }

}