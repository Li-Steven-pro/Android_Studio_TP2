package steven.li.flickr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get the button associated with the event "Get an image"
        Button btnGetImage = findViewById(R.id.btnGetImage);
        // Set the listener GetImageOnClick to the event on click
        btnGetImage.setOnClickListener(new GetImageOnClickListener(findViewById(R.id.image)));

        // Get the button that will change the main activity to listActivity
        Button toList = findViewById(R.id.button2);
        toList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This intent is used to start the activity for list of image
                Intent myIntent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(myIntent);
            }
        });

    }
}

