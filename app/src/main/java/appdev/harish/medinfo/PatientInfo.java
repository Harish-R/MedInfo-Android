package appdev.harish.medinfo;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PatientInfo extends ListActivity {

    private String authenticate_url;
    private ArrayList<HashMap<String, String>> patientInfo;
    JSONArray patientInf = null;
    JSONObject patientIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);

        patientInfo = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();

        Bundle extras = Intent.getIntent().getExtras();
        authenticate_url = extras.getString("url");

        class ListPatientInfo extends AsyncTask<URL, Integer, Integer> {

            JSONParser jsonParser = new JSONParser();

            ListPatientInfo () {
                //pDialog = new ProgressDialog(PatientInfo.this);
            }

            protected Integer doInBackground(URL... urls) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params = null;
                try {
                    patientIn = jsonParser.makeHttpRequest(authenticate_url, "GET", params);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return 0;
            }

            protected void onPreExecute() {
                super.onPreExecute();
                //pDialog.setMessage("Logging in. Please wait...");
                //pDialog.setIndeterminate(false);
                //pDialog.setCancelable(false);
                //pDialog.show();
            }

            protected void onPostExecute(Integer result) {
                //pDialog.dismiss();
            }
        }
    }

}
