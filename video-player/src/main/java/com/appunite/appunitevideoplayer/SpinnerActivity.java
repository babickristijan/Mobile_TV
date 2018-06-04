/*package com.appunite.appunitevideoplayer;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner spinner = (Spinner) findViewById(com.appunite.appunitevideoplayer.R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
    //  ArrayAdapter<String> adapter = new ArrayAdapter(this, mainData.title,R.id.spinner);

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.planets_array, android.R.layout.simple_spinner_item);
    //dodati com.androidcss.jsonexample umjesto  R.array.planets_array


// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Toast.makeText(getBaseContext(),parent.getItemAtPosition(pos)+" selected",Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
*/