package steven.li.flickr;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.Vector;

public class MyAdapter extends BaseAdapter {
    // Contains image's urls
    Vector<String> vector;

    public MyAdapter() {
        vector = new Vector<>();
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the layout for image using inflater
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.bitmaplayout, parent, false);

        // Get the imageView from the layout
        ImageView imageView = convertView.findViewById(R.id.imageView);

        // Set the request queue that will handle our multiple images request
        RequestQueue queue = MySingleton.getInstance(parent.getContext()).getRequestQueue();

        // Set an request using the vector to get the image and set in the imageView
        ImageRequest request = new ImageRequest(vector.get(position),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {

                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        });
        // Add the image request in the queue
        queue.add(request);

        Log.i("JFL", "TODO");
        return convertView;

    }

    public void dd(String url)
    {
        // Add image url in the vector
        vector.add(url);

        Log.i("JFL", "Adding to adapter url : " + url);
    }
}