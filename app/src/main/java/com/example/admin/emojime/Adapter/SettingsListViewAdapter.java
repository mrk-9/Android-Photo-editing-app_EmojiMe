package com.example.admin.emojime.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.admin.emojime.R;
import java.util.List;

public class SettingsListViewAdapter extends BaseAdapter
{
    private Context mContext;
    private List<String> settings_name;
    public int selectedIndex = 10000;

    public SettingsListViewAdapter(Context context, List<String> settings_name)
    {
        mContext = context;
        this.settings_name = settings_name;
    }

    @Override
    public int getCount()
    {
        if(settings_name.size() > 0)
        {
            return settings_name.size();
        }
            return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return settings_name.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String temp_Settings_name = settings_name.get(position);
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.settingslistview_customcell, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.settings_customlistcell_txt);
        textView.setText(temp_Settings_name);

        int [] color = {R.color.whiteColor, R.color.grayColor};
        RelativeLayout background = (RelativeLayout) convertView.findViewById(R.id.cell_Rel);
        background.setBackgroundResource(color[position%2]);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.director);

        if (selectedIndex != 10000 && selectedIndex == position)
        {
            textView.setTextColor(Color.parseColor("#FE5D26"));
            imageView.setImageResource(R.drawable.director);
        }
        else
        {
            textView.setTextColor(Color.BLACK);
            imageView.setImageResource(R.drawable.directorblack);
        }

        return convertView;
    }
}
