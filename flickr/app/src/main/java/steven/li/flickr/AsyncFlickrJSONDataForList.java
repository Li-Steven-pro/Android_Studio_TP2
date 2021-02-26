package steven.li.flickr;

import android.os.AsyncTask;
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

    public AsyncFlickrJSONDataForList(ListView listView, MyAdapter myAdapter) throws MalformedURLException {

        this.listView = listView;
        this.myAdapter = myAdapter;
        url = new URL("https://www.flickr.com/services/feeds/photos_public.gne?tags=trees&format=json");//Ce n'est pas l'url du cours mais celle ci donne directement le JSON sans la fioriture au debut
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
            // Remove the callback part
            json = new JSONObject(s.substring(15,s.length()-1));
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
            // For each object in the json
            for(int i = 0; i<((JSONArray) jsonObject.getJSONArray("items")).length(); i++)
            {
                // Add url in the adaptor vector
                myAdapter.dd(((JSONObject) jsonObject.getJSONArray("items").get(i)).getJSONObject("media").getString("m"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Refresh any view reflecting the data
        myAdapter.notifyDataSetChanged();
    }
}
