package gmu.cs.cs477.datapoker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class TwoPlayer extends AppCompatActivity {
    String spinnerTopic = "";
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);
        Intent intent = getIntent();
        spinnerTopic = intent.getStringExtra("Topic");
        if( spinnerTopic!=null) {
            tv = (TextView)findViewById(R.id.categoryTV);
            tv.setText("Nutrition Category: " + spinnerTopic);
        }

    }

}
