package com.example.lsl017.starterapplications;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity  {

    InitialViewFragment initFragment;
    DrawerLayout drawerLayout;
    ListView drawerList;
    String mainJSONString;
    JSONArray auto = new JSONArray();
    JSONArray home = new JSONArray();
    JSONArray life = new JSONArray();
    JSONArray business = new JSONArray();
    JSONArray other = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] sampleNavDrawer = getResources().getStringArray(R.array.sampleArray);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, sampleNavDrawer));

        /**
         * Handles clicks on the navigation drawer. Populating the fragment container with the
         * appropriate list.
         */
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                drawerLayout.closeDrawers();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                Bundle args = new Bundle();

                switch (position) {
                    case 0:
                        AutoFragment autoFragment = new AutoFragment();
                        args.putString("AUTOENTRIES", auto.toString());
                        autoFragment.setArguments(args);
                        transaction.replace(R.id.content_frame, autoFragment);
                        break;
                    case 1:
                        BusinessFragment businessFragment = new BusinessFragment();
                        args.putString("BUSINESSENTRIES", business.toString());
                        businessFragment.setArguments(args);
                        transaction.replace(R.id.content_frame, businessFragment);
                        break;
                    case 2:
                        HomeFragment homeFragment = new HomeFragment();
                        args.putString("HOMEENTRIES", home.toString());
                        homeFragment.setArguments(args);
                        transaction.replace(R.id.content_frame, homeFragment);
                        break;
                    case 3:
                        LifeFragment lifeFragment = new LifeFragment();
                        args.putString("LIFEENTRIES", life.toString());
                        lifeFragment.setArguments(args);
                        transaction.replace(R.id.content_frame, lifeFragment);
                        break;
                    case 4:
                        OtherFragment otherFragment = new OtherFragment();
                        args.putString("OTHERENTRIES", other.toString());
                        otherFragment.setArguments(args);
                        transaction.replace(R.id.content_frame, otherFragment);
                        break;
                    case 5:
                        transaction.replace(R.id.content_frame, new UserSettingsFragment());
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Invalid selection", Toast.LENGTH_SHORT).show();
                }

                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        if (findViewById(R.id.content_frame) != null) {
            if (savedInstanceState == null) {
                initFragment = new InitialViewFragment();
                // In case the activity was started with special instructions from an Intent
                // pass the Intent's extras to the fragment as arguments
                initFragment.setArguments(getIntent().getExtras());

                getSupportFragmentManager().beginTransaction().add(R.id.content_frame, initFragment).commit();
            }
        }

        LoadJSON jsonData = new LoadJSON(new LoadJSON.AsyncResponse() {
            @Override
            public void processFinish(String JSONString) {
                mainJSONString = JSONString;
                parseJSONString(mainJSONString);
            }
        });
        jsonData.execute();
    }

    public void parseJSONString(String JSONString){
        try {
            JSONArray myJSON = new JSONArray(JSONString);

            for(int i = 0; myJSON.length() > i; i++){
                JSONObject currObject = (JSONObject) myJSON.get(i);
                String lob = (String)currObject.get("LOB");
                if(lob.equals("Auto")){
                    auto.put(currObject);
                } else if (lob.equals("Home")){
                    home.put(currObject);
                } else if (lob.equals("Life")) {
                    life.put(currObject);
                } else if (lob.equals("Business")) {
                    business.put(currObject);
                } else if (lob.equals("Other")) {
                    other.put(currObject);
                } else {
                    Toast.makeText(getApplicationContext(),"Not an applicable LOB", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (org.json.JSONException e){
            e.printStackTrace();
        }
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
        } else if (id == R.id.share_screen){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, new OtherFragment());
//            transaction.addToBackStack(null);
//            transaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }
}
