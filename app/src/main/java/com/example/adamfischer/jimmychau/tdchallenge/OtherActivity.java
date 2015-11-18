package com.example.adamfischer.jimmychau.tdchallenge;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.NumberFormat;
import java.util.Locale;

public class OtherActivity extends Activity {

    DatabaseAdapter databaseAdapter;

    // Data about project
    private static ProjectData pd;

    TextView pName;
    TextView pBlurb;
    TextView pDuration;
    TextView pType;
    TextView pGoal;
    TextView pDonate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        // create a instance of SQLite Database
        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter = databaseAdapter.open();

        pd = (ProjectData) getIntent().getExtras().getSerializable("iItem");

        pName = (TextView) findViewById(R.id.projectNameTextView);
        pBlurb = (TextView) findViewById(R.id.blurbTextView);
        pDuration = (TextView) findViewById(R.id.durationTextView);
        pType = (TextView) findViewById(R.id.typeTextView);
        pGoal = (TextView) findViewById(R.id.goalTextView);
        pDonate = (TextView) findViewById(R.id.donateTextView);

        pName.setText(pd.getName());
        pBlurb.setText(pd.getBlurb());
        pDuration.setText(pd.getDate());
        pType.setText(pd.getType());

        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        String donatedNum = n.format(pd.getDonated() / 100.0);
        String goalNum = n.format(pd.getGoal() / 100.0);

        pGoal.setText(goalNum);
        pDonate.setText(donatedNum);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, (pd.getDonated() / 100.0))
        });


        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(1, (pd.getGoal() / 100.0))
        });

        graph.addSeries(series2);
        graph.addSeries(series);

        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb(255, 0, 0);
            }
        });

        // styling
        series2.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb(255,255,0);
            }
        });

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);

        series2.setDrawValuesOnTop(true);
        series2.setValuesOnTopColor(Color.RED);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"Funding", "Goal"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_other, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
