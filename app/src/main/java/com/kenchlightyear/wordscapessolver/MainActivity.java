package com.kenchlightyear.wordscapessolver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                button.setEnabled(false);
                button.setClickable(false);
                final TextView problem = findViewById(R.id.editText);
                final TextView solution = findViewById(R.id.editText2);
                button.setText("SOLVING");
                final ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                new JavaUrlConnectionReader().execute("https://ex.kenchlightyear.com/cgi-bin/aword.py?word=" + problem.getText().toString());
                hideKeyboardFrom(button.getContext(),button.getRootView());
            }
        });

        final Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://kenchlightyear.com/")));
            }
        });
    }

    public class JavaUrlConnectionReader extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String[] theUrls) {
            StringBuilder content = new StringBuilder();

            // many of these calls can throw exceptions, so i've just
            // wrapped them all in one try/catch statement.
            try
            {
                // create a url object
                URL url = new URL(theUrls[0]);

                // create a urlconnection object
                URLConnection urlConnection = url.openConnection();

                // wrap the urlconnection in a bufferedreader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;

                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line).append("\n");
                }
                bufferedReader.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String output) {
            final TextView solution = findViewById(R.id.editText2);
            solution.setText(output);
            final Button button = findViewById(R.id.button);
            final ProgressBar progressBar = findViewById(R.id.progressBar);
            button.setClickable(true);
            button.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
            button.setText("SOLVE");
            solution.setScrollbarFadingEnabled(false);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
