package steven.li.geolocalized;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
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


public class AsyncFlickrJSONDataForList extends AsyncTask<String, Void, JSONObject> {
    URL url;
    ListView listView;
    MyAdapter myAdapter;

    public AsyncFlickrJSONDataForList(ListView listView, MyAdapter myAdapter, URL url) throws MalformedURLException {

        this.listView = listView;
        this.myAdapter = myAdapter;
        this.url = url;
        listView.setAdapter(myAdapter);
    }


    @Override
    protected JSONObject doInBackground(String... strings) {
        HttpURLConnection urlConnection;
        JSONObject json = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String s = readStream(in);
            Log.i("request", s);
            // Remove the jsonflickrApi part
            json = new JSONObject(s.substring(14,s.length()-1));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }

        return json;
    }

    private String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        try {

            for(int i = 0; i<( jsonObject.getJSONObject("photos").getJSONArray("photo")).length(); i++)
            {
                // Build the url of the image using object information
                String id = ((JSONObject)jsonObject.getJSONObject("photos").getJSONArray("photo").get(i)).getString("id");
                String server = ((JSONObject)jsonObject.getJSONObject("photos").getJSONArray("photo").get(i)).getString("server");
                String secret = ((JSONObject)jsonObject.getJSONObject("photos").getJSONArray("photo").get(i)).getString("secret");
                myAdapter.dd(new String("https://live.staticflickr.com/" + server+"/"+id+"_"+secret+".jpg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        myAdapter.notifyDataSetChanged();
    }
}
