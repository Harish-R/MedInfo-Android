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

    private String authenticate_urlurl;
    private ArrayList<HashMap<String, String>> patientInfo;
    JSONArray products = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);

        patientInfo = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();

        Bundle extras = Intent.getIntent().getExtras();
        authenticate_url = extras.getString("url");

        class AuthenticateUser extends AsyncTask<URL, Integer, Integer> {

            private String username;
            private String password;
            private TextView errorMessage = (TextView)findViewById(R.id.errorMessage);
            private ProgressDialog pDialog;
            private boolean loginCheck = false;
            JSONObject loginParams = null;
            JSONParser jsonParser = new JSONParser();

            AuthenticateUser(String username, String password) {
                pDialog = new ProgressDialog(PatientInfo.this);
                this.username = username;
                this.password = password;
            }

            protected Integer doInBackground(URL... urls) {
                //InputStream is = null;
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                Log.d("Zero", params.toString());
                try {
                    Log.d("One", "Before calling ");
                    //JSONObject response = jsonParser.makeHttpRequest(authenticate_url, "POST", params);
                    Globals.response = jsonParser.makeHttpRequest(authenticate_url, "POST", params);
                    Log.d("Two", "After server request");
                    Log.d("All products: ", Globals.response.toString());

                    if (Globals.response.getString("success").equals("false"))
                        loginCheck = false;
                    else if(Globals.response.getString("success").equals("success")) {
                        loginCheck = true;
                        Log.d("Create response", Globals.response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return 0;
            }

            protected void onPreExecute() {
                super.onPreExecute();
                pDialog.setMessage("Logging in. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            protected void onPostExecute(Integer result) {
                pDialog.dismiss();
                if(loginCheck) {
                    errorMessage.setText("valid User credentials");
                    Intent listPatients = new Intent(getApplicationContext(), ListPatientsActivity.class);
                    startActivity(listPatients);
                }
                else {
                    errorMessage.setText("Invalid User credentials");
                    //Intent listPatients = new Intent(getApplicationContext(), ListPatientsActivity.class);
                    //startActivity(listPatients);
                }
            }
        }
    }

/*@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_patient_info, menu);
    return true;
}*/

//@Override
/*public boolean onOptionsItemSelected(MenuItem item) {
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
