package steven.li.flickr;

import android.view.View;
import android.widget.ImageView;

import java.net.MalformedURLException;
import java.net.URL;

public class GetImageOnClickListener implements View.OnClickListener{

    ImageView imageView;
    public GetImageOnClickListener(ImageView imageView) {
        this.imageView = imageView;
    }
    @Override
    public void onClick(View v){
        URL url = null;

        try {
            url = new URL("https://www.flickr.com/services/feeds/photos_public.gne?tags=trees&format=json");

        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        new AsyncFlickrJSONData(url,imageView).execute();
    }
}
