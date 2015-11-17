package com.example.adamfischer.jimmychau.tdchallenge;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import io.karim.MaterialTabs;

//public class MainActivity extends Activity {
public class MainActivity extends Activity {

    // Data about logged in user
    public static UserData userData;

    // Tabs
    private TabFragment[] tabFragments;

    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userData = (UserData)getIntent().getExtras().getSerializable("userData");
        pagerAdapter = new PagerAdapter(getFragmentManager());

        // Create tabs/pages
        tabFragments = new TabFragment[] {
            new MyProjectsFragment(),
            new OtherProjectsFragment(),
            new ProfileFragment(),
            new AboutFragment()
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

    public static class TabFragment extends Fragment {
        public String tabTitle = "No Title";
    }

    /***********************************************************************************************
        My Projects tab
     **********************************************************************************************/

    public void addNewProjectClick(View view) {
        Intent i = new Intent(this, CreateProjectActivity.class);
        startActivity(i);
    }

    public static class MyProjectsFragment extends TabFragment {
        public MyProjectsFragment() {
            this.tabTitle = "My Projects";
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my_projects, container, false);

            return rootView;
        }
    }

    /***********************************************************************************************
        Other Projects tab
     **********************************************************************************************/
    public static class OtherProjectsFragment extends TabFragment {

        public OtherProjectsFragment() {
            this.tabTitle = "Other Projects";
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_other_projects, container, false);

            return rootView;
        }
    }

    /***********************************************************************************************
        My Profile tab
     **********************************************************************************************/
    public static class ProfileFragment extends TabFragment {

        public ProfileFragment() {
            this.tabTitle = "My Profile";
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_profile, container, false);



            return rootView;
        }
    }

    /***********************************************************************************************
        About / Help tab
     **********************************************************************************************/
    public static class AboutFragment extends TabFragment {

        public AboutFragment() {
            this.tabTitle = "About";
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_other_projects, container, false);


            return rootView;
        }
    }
}