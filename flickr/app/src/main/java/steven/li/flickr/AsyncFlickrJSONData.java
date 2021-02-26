package steven.li.flickr;

import android.os.AsyncTask;
import android.widget.ImageView;

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


public class AsyncFlickrJSONData extends AsyncTask<String,Void, JSONObject>
{
    URL url;
    ImageView imageView;

    public AsyncFlickrJSONData(URL url, ImageView imageView){
        this.url = url;
        this.imageView = imageView;
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
            // Remove the callback part that surrounds the json
            json = new JSONObject(s.substring(15,s.length()-1));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }

        return json;
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        String imageURL = null;

        try {
            // Get the image url from the json
            imageURL = ((JSONObject) jsonObject.getJSONArray("items").get(0)).getJSONObject("media").getString("m");//On recupere les urls des images via le JSON qu'on a obtenu precedement
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println(imageURL);
        try {
            //
            new AsyncBitmapDownloader(new URL(imageURL),imageView).execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
