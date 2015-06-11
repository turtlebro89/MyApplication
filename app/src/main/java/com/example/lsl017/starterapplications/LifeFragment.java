package com.example.lsl017.starterapplications;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class LifeFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private List<JSONObject> finalJSONList;

    public LifeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.list_view_fragment, container, false);
        setHasOptionsMenu(true);
        listView = (ListView) view.findViewById(R.id.listView);
        Bundle JSONBundle = null;


        if (getArguments() != null) {
            JSONBundle = getArguments();
        }

        JSONArray jsonEntriesArray = parseJSON(JSONBundle);
        finalJSONList = sortByState(jsonEntriesArray);

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);

        setFinalAdapter(adapter);

        listView.setAdapter(adapter);

        return view;
    }


    public JSONArray parseJSON(Bundle JSONBundle){
        JSONArray parsedJSONArray = null;
        try {
            String jsonString = JSONBundle.getString("LIFEENTRIES");
            parsedJSONArray = new JSONArray(jsonString);
        } catch (org.json.JSONException e){
            e.printStackTrace();
            System.out.println("JSON exception");
        }
        return parsedJSONArray;
    }

    public List sortByState(JSONArray JSONEntriesArray) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Set<String> states = sharedPref.getStringSet("AFISelectedStates", new HashSet<String>());
        JSONArray includedJSONObjects = new JSONArray();


        for(int i = 0; JSONEntriesArray.length() > i; i++){

            try{
                for(int i2 = 0;JSONEntriesArray.getJSONObject(i).getJSONArray("States").length() > i2; i2++){
                    if(states.contains(JSONEntriesArray.getJSONObject(i).getJSONArray("States").get(i2))){
                        includedJSONObjects.put(JSONEntriesArray.getJSONObject(i));
                    }
                }
            } catch (org.json.JSONException e){
                System.out.println("error in sort by state");
            }
        }

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < includedJSONObjects.length(); i++) {
            try {
                jsonValues.add(includedJSONObjects.getJSONObject(i));
            } catch (org.json.JSONException e) {
                System.out.println("error in sort by state");
            }
        }



        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {
                    valA = (String) a.get("Title");
                    valB = (String) b.get("Title");
                } catch (org.json.JSONException e) {
                    System.out.println("error in sort by state");
                }

                int comp = valA.compareTo(valB);

                if (comp > 0)
                    return 1;
                if (comp < 0)
                    return -1;
                return 0;
            }
        });

        return jsonValues;//Something
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.share_screen:
                View view = getView();
                listView = (ListView) view.findViewById(R.id.listView);
                adapter = new ArrayAdapter(getActivity().getBaseContext(), R.layout.abc_list_menu_item_checkbox);
                setFinalAdapter(adapter);
                listView.setAdapter(adapter);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setFinalAdapter(ArrayAdapter adapter){
        for (int i = 0; finalJSONList.size() > i; i++) {
            try {
                adapter.add(finalJSONList.get(i).get("Title"));
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
