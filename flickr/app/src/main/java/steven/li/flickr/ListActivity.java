package steven.li.flickr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.net.MalformedURLException;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Button button = findViewById(R.id.button);
        ListView listView = findViewById(R.id.list);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // use AsyncTask to get image url and load them in the page
                    new AsyncFlickrJSONDataForList(listView,new MyAdapter()).execute();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}