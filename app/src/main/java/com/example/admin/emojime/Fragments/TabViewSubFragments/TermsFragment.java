package com.example.admin.emojime.Fragments.TabViewSubFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import com.example.admin.emojime.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TermsFragment extends Fragment{
    WebView terms_webView;
    View temp_fragmentView;
    RelativeLayout back_btn;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.terms_screen, container, false);
        temp_fragmentView = fragmentView;

        initView();
        showTextOfFile();
        return fragmentView;
    }

    public void initView()
    {
        terms_webView = (WebView) temp_fragmentView.findViewById(R.id.terms_webView);
        back_btn = (RelativeLayout) temp_fragmentView.findViewById(R.id.terms_rel);
        back_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getFragmentManager().popBackStack();
            }
        });
    }

    //Loading txt file to Webview
    public void showTextOfFile()
    {
        StringBuilder sb = new StringBuilder("<html><body>");
        sb.append(readTextFile());
        sb.append("</body></html>");
        terms_webView.loadData(sb.toString(), "text/html", "UTF-8");
    }

    //Read text file in raw folder
    public String readTextFile()
    {
        InputStream is = getResources().openRawResource(R.raw.terms);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        String entireFile = "";
        try {
            while((line = br.readLine()) != null) {
                entireFile += (line + "\n");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return entireFile;
    }
}
