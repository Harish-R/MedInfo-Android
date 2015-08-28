package appdev.harish.medinfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.JsonParser;
import appdev.harish.medinfo.Globals;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import appdev.harish.medinfo.*;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends ActionBarActivity {

    private Button loginButton;
    private TextView errorMessage;
    private EditText usernameInput;
    private EditText passwordInput;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    // url to get all products list
    private static String authenticate_url = "http://medinfo.esy.es/app_login.php";
    // products JSONArray
    //JSONArray products = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_button);
        usernameInput = (EditText) findViewById(R.id.username);
        passwordInput = (EditText) findViewById(R.id.password);

        loginButton.setOnClickListener(new Button.OnClickListener() {
                                           public void onClick(View v) {

               String username = usernameInput.getText().toString();
               String password = passwordInput.getText().toString();

               errorMessage = (TextView) findViewById(R.id.errorMessage);
               //errorMessage.setText("Login Clicked");
               if ((username.trim().length() == 0) || (password.trim().length() == 0)) {
                   errorMessage.setText("Fields cannot be empty");
                   return;
               } else {
                    new AuthenticateUser(username, password).execute();
                }
            }
        }
        );
    }

    class AuthenticateUser extends AsyncTask<URL, Integer, Integer> {

        private String username;
        private String password;
        private TextView errorMessage = (TextView)findViewById(R.id.errorMessage);
        private ProgressDialog pDialog;
        private String authenticate_url = /*"http://localhost:8070/MedInfo/app_login.php";//*/"http://medinfo.esy.es/app_login.php";
        private boolean loginCheck = false;
        JSONObject loginParams = null;
        JSONParser jsonParser = new JSONParser();

        AuthenticateUser(String username, String password) {
            pDialog = new ProgressDialog(LoginActivity.this);
            this.username = username;
            this.password = password;
            Log.d("uname", username);
            Log.d("pass", password);
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
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
}
