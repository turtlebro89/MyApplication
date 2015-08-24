package com.example.lsl017.starterapplications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSettingsFragment extends Fragment {

    SharedPreferences sharedPref;
    Set<String> selectedStates;

    public UserSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);
        final EditText enteredName = (EditText) view.findViewById(R.id.enteredName);
        final EditText enteredCity = (EditText) view.findViewById(R.id.enteredCity);
        final EditText enteredState = (EditText) view.findViewById(R.id.enteredState);
        final ListView statesList = (ListView) view.findViewById(R.id.statesList);
        final ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.states, R.layout.abc_list_menu_item_checkbox);
        final Button saveButton = (Button) view.findViewById(R.id.saveButton);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        statesList.setAdapter(adapter);

        selectedStates = new TreeSet<>();//searchForSelectedStates(statesList);

        if(sharedPref != null){
            enteredName.setText(sharedPref.getString("AFIName", ""));
            enteredCity.setText(sharedPref.getString("AFICity", ""));
            enteredState.setText(sharedPref.getString("AFIState",""));
            selectedStates.addAll(sharedPref.getStringSet("AFISelectedStates", selectedStates));

        }

        /**
         * On click listender for saveButton handles Saves to the shared User Preferences. Saving
         * All entered Information to the SharedPreferences.
         */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStates.addAll(searchForSelectedStates(statesList));
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("AFIName", enteredName.getText().toString());
                editor.putString("AFICity", enteredCity.getText().toString());
                editor.putString("AFIState", enteredState.getText().toString());
                editor.putStringSet("AFISelectedStates", selectedStates);

                editor.commit();

                System.out.println(selectedStates.toString());

                Toast.makeText(getActivity(),"Your settings have been saved",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    /**
     * searchForSelectedStates iterates through the statesList list view and saves the checked states
     * to a treeSet. Then returning the treeset to the calling method.
     *
     * @param statesList the listView holding states
     * @return selectedStates the treeSet with the selected states
     */
    private Set searchForSelectedStates(ListView statesList) {
        SparseBooleanArray checked = statesList.getCheckedItemPositions();


            for(int i = 0; i < statesList.getCount(); i++){
            if (checked.get(i)) {
                selectedStates.add(getResources().getStringArray(R.array.states)[i]);
            } else {
                if(selectedStates.contains(getResources().getStringArray(R.array.states)[i])) {
                    selectedStates.remove(getResources().getStringArray(R.array.states)[i]);
                }
            }
        }

        return selectedStates;
    }
}
