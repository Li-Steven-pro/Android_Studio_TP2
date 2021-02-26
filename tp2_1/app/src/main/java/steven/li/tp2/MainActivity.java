package steven.li.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    JSONObject res;
    Button buttonAuthenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get the button to set the authenticate event
        buttonAuthenticate = findViewById(R.id.buttonAuthenticate);
        buttonAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user inputs
                EditText textLogin =findViewById(R.id.textLogin);
                EditText textPassword = findViewById(R.id.textPassword);
                // Get the textView for showing result
                TextView viewResult = findViewById(R.id.viewResult);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        URL url = null;
                        try {
                            // Define the url
                            url = new URL("https://httpbin.org/basic-auth/bob/sympa");
                            String basicAuth = "Basic " + Base64.encodeToString((textLogin.getText()+":"+textPassword.getText()).getBytes(), Base64.NO_WRAP);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            // Add credentials as property under the key Authorization
                            urlConnection.setRequestProperty("Authorization", basicAuth);
                            urlConnection.connect();
                            try {
                                // Get the result of the request as stream
                                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                                String s = readStream(in);
                                // Shows the request result in the log
                                Log.i("JSF", s);
                                // Create the JSON from the string built from the stream
                                JSONObject result = new JSONObject(s);
                                res = result;
                                // Run the task of changing the textView in the UiThread. Cannot process modification in the current thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            // Show to the user if the authentication succeed
                                            viewResult.setText("My result here :" + MainActivity.this.res.getString("authenticated"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } finally {
                                urlConnection.disconnect();
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                // Run the thread
                thread.start();
            }
        });
    }

    //
    private String readStream(InputStream is) throws IOException{
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
        // For each line in the stream, add the line in the String builder
        for (String line = r.readLine(); line != null ; line = r.readLine()){
            sb.append(line);
        }
        is.close();
        // Return the String built from the StringBuilder
        return sb.toString();
    }
}