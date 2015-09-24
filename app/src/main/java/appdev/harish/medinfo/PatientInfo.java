package appdev.harish.medinfo;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;


public class PatientInfo extends ActionBarActivity {

    private String authenticate_url;
    private ArrayList<HashMap<String, String>> patientInfo;
    JSONArray patientArray;
    JSONObject patientObject;
    JSONObject patient;
    TextView nameView, ageView, sexView, phoneView, hospitalView;
    ImageView imageView;
    Timer t;

    int mInterval = 5000; // 5 seconds by default, can be changed later
    Handler mHandler;

    class LoadImageTask extends TimerTask {
        public void run()  {
            new ListPatientInfo().execute();
        }
    }

    LoadImageTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);

        Log.d("Patient_Info", "Patient_Info");
        //patientInfo = new ArrayList<HashMap<String, String>>();
        //ListView lv = getListView();

        Bundle extras = new Bundle();
        extras = getIntent().getExtras();
        authenticate_url = extras.getString("url");
        Log.d("Got URL", "Got URL");

        t = new Timer();
        task = new LoadImageTask();
        t.schedule(task, 0, 5000);
    }

    class ListPatientInfo extends AsyncTask<URL, Integer, Integer> {

        ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        String pat_id, name, age, sex, phone, hospital;

        ListPatientInfo () {
            pDialog = new ProgressDialog(PatientInfo.this);
        }

        protected Integer doInBackground(URL... urls) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params = null;
            Log.d("Inside doInBAckground", "Inside doInBAckground");

            try {
                patientObject = jsonParser.makeHttpRequest(authenticate_url, "GET", params);
                patientArray = patientObject.getJSONArray("patient");
                patient = patientArray.getJSONObject(0);
                Log.d("Got JSONArray", "Got JSONArray");

                // Storing each json item in variable
                pat_id = patient.getString("pat_id");
                name = patient.getString("name");
                age = patient.getString("age");
                sex = patient.getString("sex");
                hospital = patient.getString("hospital");
                phone = patient.getString("phone");

            } catch (Exception e) {
                e.printStackTrace();
            }

            imageView = (ImageView) findViewById(R.id.imageView);
            try {
                URL url = new URL("https://thoughtcatalog.files.wordpress.com/2012/12/shutterstock_93326353.jpg");
                //try this url = "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg"
                HttpGet httpRequest = null;

                httpRequest = new HttpGet(url.toURI());

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) httpclient
                        .execute(httpRequest);

                HttpEntity entity = response.getEntity();
                BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
                InputStream input = b_entity.getContent();

                Bitmap bitmap = BitmapFactory.decodeStream(input);

                imageView.setImageBitmap(bitmap);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return 0;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Logging in. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("", "onpreExecute");
        }

        protected void onPostExecute(Integer result) {
            Log.d("", "onPostExecute");
            pDialog.dismiss();
            nameView = (TextView) findViewById(R.id.textView);
            ageView = (TextView) findViewById(R.id.textView4);
            sexView = (TextView) findViewById(R.id.textView2);
            phoneView = (TextView) findViewById(R.id.textView6);
            hospitalView = (TextView) findViewById(R.id.textView8);

            nameView.setText(name);
            ageView.setText(age);
            sexView.setText(sex);
            phoneView.setText(phone);
            hospitalView.setText(hospital);
        }
    }

}
