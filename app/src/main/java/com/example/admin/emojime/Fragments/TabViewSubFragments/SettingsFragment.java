package com.example.admin.emojime.Fragments.TabViewSubFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.example.admin.emojime.Adapter.SettingsListViewAdapter;
import com.example.admin.emojime.Activity.MainActivity;
import com.example.admin.emojime.R;
import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment implements AdapterView.OnItemClickListener  {

    ListView listView;
    View temp_fragmentView;
    List<String> settingsArray;
    SettingsListViewAdapter adapter;
    FrameLayout frame;

    static SettingsFragment fragmentInstance = null;
    public static SettingsFragment newInstance()
    {
        if (fragmentInstance == null)   {
            fragmentInstance = new SettingsFragment();
        }
        return fragmentInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        settingsArray = new ArrayList<String>();
        settingsArray.add("How to use this app");
        settingsArray.add("Share app");
        settingsArray.add("Terms & Conditions");
        settingsArray.add("Rate this app");
        settingsArray.add("Report a Problem");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.settings_fragment, container, false);
        temp_fragmentView = fragmentView;

        initView();
        return fragmentView;
    }

    public void initView()
    {
        frame = (FrameLayout) temp_fragmentView.findViewById(R.id.setting_child_frame);
        frame.setVisibility(View.GONE);
        listView = (ListView) temp_fragmentView.findViewById(R.id.settings_listView);
        adapter = new SettingsListViewAdapter(getContext(),settingsArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }
    //Case clicking the ListView cell
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        adapter.selectedIndex = position;
        Log.d("position",String.valueOf(adapter.selectedIndex));
        adapter.notifyDataSetChanged();
        switch (position)
        {
            case 1:  //share app
                Intent i=new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "I use Emoji Me http://www.soemojime.com");
                startActivity(Intent.createChooser(i,"Share via"));
                break;
            case 2:  //Terms & Conditions
                transFragment(new TermsFragment());
                break;
            case 3:  //Rate this app
                transFragment(new RateFragment());
                break;
            case 0:  //How to use this app
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0,0);
                break;

            case 4: //Report a Problem
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setType("message/rfc822");
                intent1.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@soemojime.com"});
                intent1.putExtra(Intent.EXTRA_SUBJECT, "I would like to report an issue.");
                intent1.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent1, "Report a problem via Email"));
                break;
        }
    }

    //Fragment transaction using FragmentLayout
    private void transFragment(Fragment fragment)
    {
        frame.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.setting_child_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
