package com.example.admin.emojime.Fragments.TabViewSubFragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.admin.emojime.R;

/**
 * Created by admin on 1/19/2017.
 */

public class RateFragment extends Fragment implements View.OnClickListener
{
    ImageView rate_up, rate_down;
    WebView webView;
    View temp_fragmentView;
    RelativeLayout rate_backbtn;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.rate_screen, container, false);
        temp_fragmentView = fragmentView;

        initView();
        return fragmentView;
    }

    public void initView()
    {
        webView = (WebView) temp_fragmentView.findViewById(R.id.rate_webView);
        webView.setVisibility(View.GONE);
        rate_up = (ImageView) temp_fragmentView.findViewById(R.id.rate_up);
        rate_down = (ImageView) temp_fragmentView.findViewById(R.id.rate_down);
        rate_backbtn = (RelativeLayout) temp_fragmentView.findViewById(R.id.rate_rel);
        rate_up.setOnClickListener(this);
        rate_down.setOnClickListener(this);
        rate_backbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.rate_down:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://soemojime.com"));
                startActivity(browserIntent);
                break;
            case R.id.rate_up:
                Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com"));
                startActivity(browserIntent1);
                break;
            case R.id.rate_rel:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
