package com.example.adamfischer.jimmychau.tdchallenge;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import io.karim.MaterialTabs;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareDialog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

//public class MainActivity extends Activity {
public class MainActivity extends Activity {

    // Data about logged in user
    private static UserData userData;

    private DatabaseAdapter dbAdapter;

    // Tabs
    private TabFragment[] tabFragments;

    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = new DatabaseAdapter(this).open();

        FacebookSdk.sdkInitialize(getApplicationContext());

        shareDialog = new ShareDialog(this);

        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            userData = (UserData)getIntent().getExtras().getSerializable("userData");
        }

        pagerAdapter = new PagerAdapter(getFragmentManager());

        // Create tabs/pages
        tabFragments = new TabFragment[] {
            new MyProjectsFragment(),
            new OtherProjectsFragment(),
            new SocialFragment(),
            new ProfileFragment(),
        };

        // Add adapter to viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        // bind tabs to viewPager
        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    //--------- classes for working with tab views -------------
    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
             try {
                return tabFragments[position];
            } catch (IndexOutOfBoundsException ex) {
                 System.err.println("Error finding fragment: " + ex.getMessage());
                 return tabFragments[0];
            }
        }

        @Override
        public int getCount() {
            return tabFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabFragments[position].tabTitle;
        }
    }

    /**
     * Root class for the tab fragments
     */
    public static class TabFragment extends Fragment {
        public String tabTitle = "No Title";
        protected MainActivity mainActivity;
    }

    /***********************************************************************************************
        My Projects tab
     **********************************************************************************************/

    public void addNewProjectClick(View view) {
        Intent i = new Intent(this, CreateProjectActivity.class);
        i.putExtra("userID", userData.getID());
        startActivity(i);
    }

    public static class MyProjectsFragment extends TabFragment {
        private static ArrayList<ProjectData> myProjects;
        private ArrayAdapter<ProjectData> arrayAdapter;

        public MyProjectsFragment() {
            this.tabTitle = "My Projects";
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my_projects, container, false);
            mainActivity = (MainActivity)getActivity();

            // load projects for signed-on user
            myProjects = mainActivity.dbAdapter.getProjectsFor(userData.getID());

            // populate list
            ListView listView = (ListView)rootView.findViewById(R.id.listViewMyProjects);

            arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, myProjects);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ProjectData item = (ProjectData) parent.getItemAtPosition(position);

                    Intent i = new Intent(view.getContext(), EditActivity.class);
                    i.putExtra("iItem", item);
                    startActivity(i);

                }
            });

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            Log.d("MyProjectsFragment", "onResume called...");
            myProjects.clear();
            arrayAdapter.clear();

            myProjects.addAll(mainActivity.dbAdapter.getProjectsFor(userData.getID()));
            arrayAdapter.notifyDataSetChanged();
        }
    }

    /***********************************************************************************************
        Other Projects tab
     **********************************************************************************************/
    public static class OtherProjectsFragment extends TabFragment {

        private static ArrayList<ProjectData> otherProjects;

        private ArrayAdapter<ProjectData> arrayAdapter;

        public OtherProjectsFragment() {
            this.tabTitle = "Other Projects";
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_other_projects, container, false);
            mainActivity = (MainActivity)getActivity();

            // load projects for signed-on user
            otherProjects = mainActivity.dbAdapter.getOtherProjects(userData.getID());

            // populate list
            ListView listView = (ListView)rootView.findViewById(R.id.listViewOtherProjects);

            arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, otherProjects);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ProjectData item = (ProjectData)parent.getItemAtPosition(position);

                    Intent i = new Intent(view.getContext(), OtherActivity.class);

                    i.putExtra("iItem", item);

                    startActivity(i);
                }
            });

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            otherProjects.clear();
            arrayAdapter.clear();

            otherProjects.addAll(mainActivity.dbAdapter.getOtherProjects(userData.getID()));
            arrayAdapter.notifyDataSetChanged();
        }
    }

    /***********************************************************************************************
        My Profile tab
     **********************************************************************************************/
    public void onAddFundsClick(View view) {
        RelativeLayout addFundsModal = (RelativeLayout)findViewById(R.id.addFundsModal);

        // show modal
        addFundsModal.setVisibility(View.VISIBLE);
    }

    private void closeAddFundsModal() {
        RelativeLayout addFundsModal = (RelativeLayout)findViewById(R.id.addFundsModal);

        // TODO: Reset values on modal before showing

        addFundsModal.setVisibility(View.GONE);
    }

    public void onAcceptAddFundsClick(View view) {


        closeAddFundsModal();
    }

    public void onCancelAddFundsClick(View view) {
        closeAddFundsModal();
    }

    public static class ProfileFragment extends TabFragment {

        public ProfileFragment() {
            this.tabTitle = "Profile";
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

            // Set up modal
            RelativeLayout addFundsModal = (RelativeLayout)rootView.findViewById(R.id.addFundsModal);
            addFundsModal.setVisibility(View.GONE);

            // year spinner
            final ArrayList<Integer> yearArray = new ArrayList<>(20);
            int curYear = Calendar.getInstance().get(Calendar.YEAR);
            for (int i = 0; i <= 20; ++i) {
                yearArray.add(curYear + i);
            }
            Spinner spinnerYears = (Spinner)rootView.findViewById(R.id.spinnerYears);
            ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, yearArray);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerYears.setAdapter(arrayAdapter);

            // set up main page
            TextView txtMyName = (TextView)rootView.findViewById(R.id.textViewMyName);
            txtMyName.setText(userData.getFirstName());

            // account balance
            TextView txtBalance = (TextView)rootView.findViewById(R.id.textViewAccountBalance);
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String balanceStr = formatter.format(userData.getBalance());
            txtBalance.setText(balanceStr);

            return rootView;
        }
    }

    /***********************************************************************************************
        Social tab
     **********************************************************************************************/

    public void appInviteClick(View view) {
        if (isNetworkAvailable(this.getBaseContext())) {
            String appLinkUrl, previewImageUrl;

            appLinkUrl = "https://www.mydomain.com/myapplink";
            previewImageUrl = "http://creativecurio.com/wp-content/uploads/2007/vm-logo-sm-1.gif";

            if (AppInviteDialog.canShow()) {
                AppInviteContent content = new AppInviteContent.Builder()
                        .setApplinkUrl(appLinkUrl)
                        .setPreviewImageUrl(previewImageUrl)
                        .build();
                AppInviteDialog.show(this, content);
            }
        } else {
            Toast.makeText(MainActivity.this, "No Network Available - Please Connect Before Trying!", Toast.LENGTH_LONG).show();
        }
    }


    public void shareLinkOnClick(View view) {
        if (isNetworkAvailable(this.getBaseContext())) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Hello Facebook")
                    .setContentDescription(
                            "The 'Hello Facebook' sample  showcases simple Facebook integration")
                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                    .build();

            shareDialog.show(linkContent);
        }
        } else {
            Toast.makeText(MainActivity.this, "No Network Available - Please Connect Before Trying!", Toast.LENGTH_LONG).show();
        }
    }

    public static class SocialFragment extends TabFragment {
        public SocialFragment() {
            this.tabTitle = "Social";
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_social, container, false);

            return rootView;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppEventsLogger.activateApp(this);
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}