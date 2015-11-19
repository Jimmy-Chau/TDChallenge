package com.example.adamfischer.jimmychau.tdchallenge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class OtherActivity extends Activity {

    DatabaseAdapter databaseAdapter;

    // Data about project
    private static ProjectData pd;
    private UserData userData;

    TextView pName;
    TextView pBlurb;
    TextView pDuration;
    TextView pType;
    TextView pGoal;
    TextView pDonate;

    private long donateAmount;

    RelativeLayout donateModal;// = (RelativeLayout)findViewById(R.id.addFundsModal);

    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        // Set up modal
        donateModal = (RelativeLayout)findViewById(R.id.donateModal);
        donateModal.setVisibility(View.GONE);

        // create a instance of SQLite Database
        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter = databaseAdapter.open();

        userData = (UserData) getIntent().getExtras().getSerializable("userData");

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

        graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, (pd.getDonated() / 100.0))
        });


        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, (pd.getGoal() / 100.0))
        });

        graph.addSeries(series2);
        graph.addSeries(series);

        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb(149, 0, 0);
            }
        });

        // styling
        series2.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb(0,89,89);
            }
        });

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.WHITE);

        series2.setDrawValuesOnTop(true);
        series2.setValuesOnTopColor(Color.WHITE);

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

    public void cancelOnClick(View view) {
        finish();
    }

    public void onDonateClick(View view) {
        // account balance
        TextView txtBalance = (TextView)findViewById(R.id.textViewAccountBalance);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String balanceStr = formatter.format(userData.getBalance() / 100.0);
        txtBalance.setText(balanceStr);


        donateModal.setVisibility(View.VISIBLE);
    }


    public void onAcceptDonateClick(View view) {
        // Calculate new Amount
        EditText txtDonateAmount = (EditText)findViewById(R.id.editTextDonateAmount);
        BigDecimal donateAmountBD;

        donateAmount = -1;
        try {
            donateAmountBD = new BigDecimal(txtDonateAmount.getText().toString()).multiply(new BigDecimal("100"));
            donateAmount = donateAmountBD.longValueExact();
        } catch (RuntimeException rtx) {
            Toast.makeText(this, "Amount is invalid", Toast.LENGTH_LONG).show();
            return;
        }

        if (donateAmount > 0) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Donating Funds")
                    .setMessage("Are you sure you want to donate this amount?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Update balances
                            // idx 0 = userbalance
                            // idx 1 = projectbalance
                            long[] newBalances = databaseAdapter.donate(userData, pd, donateAmount);
                            userData.setBalance(newBalances[0]);
                            pd.setDonated(newBalances[1]);

                            Log.d("donate", " new donated amount: " + newBalances[1] + " actual cur donated: " + pd.getDonated());

                            graph.removeAllSeries();

                            graph = (GraphView) findViewById(R.id.graph);
                            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, (pd.getDonated() / 100.0))
                            });


                            BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, (pd.getGoal() / 100.0))
                            });

                            graph.addSeries(series2);
                            graph.addSeries(series);

                            // styling
                            series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                                @Override
                                public int get(DataPoint data) {
                                    return Color.rgb(149, 0, 0);
                                }
                            });

                            // styling
                            series2.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                                @Override
                                public int get(DataPoint data) {
                                    return Color.rgb(0,89,89);
                                }
                            });

                            // draw values on top
                            series.setDrawValuesOnTop(true);
                            series.setValuesOnTopColor(Color.WHITE);

                            series2.setDrawValuesOnTop(true);
                            series2.setValuesOnTopColor(Color.WHITE);

                            closeDonateModal();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            closeDonateModal();
                        }
                    })
                    .show();
        } else {
            Toast.makeText(this, "Amount must be greater than 0", Toast.LENGTH_LONG).show();
        }

    }

    public void onCancelDonateClick(View view) {
        closeDonateModal();
    }

    private void closeDonateModal() {
        EditText txtAmount = (EditText)findViewById(R.id.editTextDonateAmount);
        txtAmount.setText("");

        // Update donated amount on activity
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        String donatedNum = n.format(pd.getDonated() / 100.0);
        pDonate.setText(donatedNum);

        donateModal.setVisibility(View.INVISIBLE);
    }
}
