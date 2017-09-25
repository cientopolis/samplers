package org.cientopolis.samplers.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import org.cientopolis.samplers.R;

import java.io.InputStream;
import java.util.Scanner;

public class HelpActivity extends Activity {

    public static final String HELP_RESOURCE_ID = "org.cientopolis.samplers.HELP_RESOURCE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Intent intent = getIntent();
        int help_resource_id = intent.getIntExtra(HelpActivity.HELP_RESOURCE_ID, -1);

        if (help_resource_id != -1) {
            // Get the raw resource file
            InputStream inputStream = getResources().openRawResource(help_resource_id);

            // Get the text from the html file
            Scanner s = new Scanner(inputStream).useDelimiter("\\A");
            String html_text = s.hasNext() ? s.next() : "";

            // Load the html text in the WebView
            WebView webView = (WebView) findViewById(R.id.help_web_view);
            webView.loadData(html_text, "text/html", null);
        }
    }
}
