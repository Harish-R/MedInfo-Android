package appdev.harish.medinfo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListPatientsActivity extends ListActivity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> patientsList;
    JSONArray patients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patients);

        patientsList = new ArrayList<HashMap<String, String>>();

        new LoadAllPatients().execute();

        ListView lv = getListView();

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            // getting values from selected ListItem
            String pid = ((TextView) view.findViewById(R.id.pat_id)).getText().toString();

            // Starting new intent
            Intent patientInfo = new Intent(getApplicationContext(), PatientInfo.class);
            // sending pid to next activity
            patientInfo.putExtra("url", "http://medinfo.esy.es/patientlist.php?pid="+pid);

            startActivity(patientInfo);
            }
        });
    }


    class LoadAllPatients extends AsyncTask<String, String, String> {

        public LoadAllPatients() {

            pDialog = new ProgressDialog(ListPatientsActivity.this);
        }

        protected String doInBackground(String... args) {

            try {
                patients = Globals.response.getJSONArray("patients");

                // looping through All Products
                for (int i = 0; i < patients.length(); i++) {

                    JSONObject patient = patients.getJSONObject(i);

                    // Storing each json item in variable
                    String pat_id = patient.getString("pat_id");
                    String name = patient.getString("name");
                    String age = patient.getString("age");
                    String sex = patient.getString("sex");
                    String hospital = patient.getString("hospital");
                    String phone = patient.getString("phone");

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put("pat_id", pat_id);
                    map.put("name", name);
                    map.put("age", age);
                    map.put("sex", sex);
                    map.put("hospital", hospital);
                    map.put("phone", phone);

                    // adding HashList to ArrayList
                    patientsList.add(map);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /*protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Logging in. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }*/

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            //pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    ListAdapter adapter = new SimpleAdapter(ListPatientsActivity.this,
                            patientsList,
                            R.layout.patients_list_row, new String[]{"name", "hospital", "pat_id"},
                            new int[]{R.id.patient_name, R.id.hospital_name, R.id.pat_id}
                    );
                    setListAdapter(adapter);
                }
            });

        }
    }

    //@Override
    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_patients, menu);
        return true;
    }*/

    /*@Override
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
    }*/
}
