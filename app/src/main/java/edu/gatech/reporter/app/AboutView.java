package edu.gatech.reporter.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.gatech.reporter.R;
import edu.gatech.reporter.utils.ParameterManager.ParameterOptions;
import edu.gatech.reporter.utils.ParameterManager.Parameters;


public class AboutView extends AppCompatActivity {
    private TextView versionTextView;
    private TextView updateDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_view);
        versionTextView = (TextView)findViewById(R.id.version_number);
        updateDateTextView = (TextView)findViewById(R.id.last_updated_date);
        versionTextView.setText(Parameters.getInstance().version);
        updateDateTextView.setText(Parameters.getInstance().lastUpdatedDate);

    }
}
