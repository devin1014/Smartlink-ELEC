package com.android.smartlink.ui.widget.adapter;

import android.view.LayoutInflater;
import android.view.View;

import com.android.smartlink.R;
import com.android.smartlink.ui.model.UISetting;
import com.neulion.core.widget.recyclerview.adapter.DataBindingAdapter;
import com.neulion.core.widget.recyclerview.holder.DataBindingHolder;

/**
 * User: NeuLion(wei.liu@neulion.com.com)
 * Date: 2017-10-17
 * Time: 16:42
 */
public class SettingsAdapter extends BaseAdapter<UISetting>
{
    private int mSelectedPosition = -1;

    public SettingsAdapter(LayoutInflater layoutInflater, OnItemClickListener<UISetting> listener)
    {
        super(layoutInflater, listener);
    }

    @Override
    protected int getLayout(int i)
    {
        return R.layout.list_item_settings;
    }

    @Override
    protected void onItemClick(DataBindingAdapter<UISetting> adapter, View view, UISetting uiSetting, int position)
    {
        super.onItemClick(adapter, view, uiSetting, position);

        view.setSelected(true);

        if (mSelectedPosition != position)
        {
            mSelectedPosition = position;

            notifyDataSetChanged();//FIXME,should refresh selected one not all views.
        }
    }

    @Override
    public void onBindViewHolder(DataBindingHolder<UISetting> holder, int position)
    {
        super.onBindViewHolder(holder, position);

        holder.itemView.setSelected(mSelectedPosition == position);
    }
}
