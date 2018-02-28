package com.kenchlightyear.wordscapesolver;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

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
                solution.setText("Solving...");
                new JavaUrlConnectionReader().execute("https://ex.kenchlightyear.com/cgi-bin/aword.py?word=" + problem.getText().toString());
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
            button.setClickable(true);
            button.setEnabled(true);
        }
    }
}
