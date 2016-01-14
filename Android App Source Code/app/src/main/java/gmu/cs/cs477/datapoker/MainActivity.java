package gmu.cs.cs477.datapoker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.os.*;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.zip.CheckedInputStream;

//Data Poker Game App by Victor Shen, Kai Johnson, and Chris Lee
public class MainActivity extends AppCompatActivity {
    private MediaPlayer player;
    CheckBox musicCheckBox;
    public static int CHANGE_BGCOLOR = 1;
    private RelativeLayout bgColor;
    String nextColor = "";
    Message m;
    String spinnerTopic = "";
    Spinner spinner;
    ArrayAdapter<String> myAdapter;
    private RatingBar ratingBar;
    private TextView txtRatingValue;
    Button btnOnePlayer, btnTwoPlayer;

    ParseUser user;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String color = (String) msg.obj;
                bgColor.setBackgroundColor(Color.parseColor(color));
                nextColor = "GREEN"; // Next background color;
                m = obtainMessage(CHANGE_BGCOLOR, nextColor);
                sendMessageDelayed(m, 2000);
                CHANGE_BGCOLOR = 2;
            }
            if (msg.what == 2) {
                String color = (String) msg.obj;
                bgColor.setBackgroundColor(Color.parseColor(color));
                nextColor = "YELLOW"; // Next background color;
                m = obtainMessage(CHANGE_BGCOLOR, nextColor);
                sendMessageDelayed(m, 2000);
                CHANGE_BGCOLOR = 3;
            }
            if (msg.what == 3) {
                String color = (String) msg.obj;
                bgColor.setBackgroundColor(Color.parseColor(color));
                nextColor = "CYAN"; // Next background color;
                m = obtainMessage(CHANGE_BGCOLOR, nextColor);
                sendMessageDelayed(m, 2000);
                CHANGE_BGCOLOR = 1;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ANIMATION TO CHANGE BACKGROUND COLOR EVERY TWO SECONDS FOR INITIAL STARTING OF GAME
        bgColor = (RelativeLayout) findViewById(R.id.backgroundColor);
        getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
        nextColor = "CYAN"; // Next background color;
        m = handler.obtainMessage(CHANGE_BGCOLOR, nextColor);
        handler.sendMessageDelayed(m, 2000);
        nextColor = "MAGENTA"; // Next background color;
        m = handler.obtainMessage(CHANGE_BGCOLOR, nextColor);
        handler.sendMessageDelayed(m, 2000);
        nextColor = "CYAN";
        m = handler.obtainMessage(CHANGE_BGCOLOR, nextColor);
        handler.sendMessageDelayed(m, 2000);

        //Plays music at start of app-music created by Victor Shen so no copyright infringement :)
        player = MediaPlayer.create(MainActivity.this, R.raw.magiclights);
        player.setLooping(false);
        player.start();

        user = ParseUser.getCurrentUser();

        musicCheckBox = (CheckBox)findViewById(R.id.checkBox);
        musicCheckBox.setChecked(true);
        musicCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    musicCheckBox.setText("Music playing");
                    player = MediaPlayer.create(MainActivity.this,
                            R.raw.magiclights);
                    player.setLooping(true);
                    player.start();
                } else {
                    musicCheckBox.setText("Music stopped");
                    player.stop();
                    player.setLooping(true);
                }
            }
        });


        //EMAIL TO A FRIEND FEATURE-sends a custom message via Gmail telling friend about app.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicCheckBox.setText("Music stopped");
                musicCheckBox.setChecked(false);
                Snackbar.make(view, "Share this app with a friend! :)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent gmail = new Intent(Intent.ACTION_VIEW);
                gmail.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                gmail.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
                gmail.setData(Uri.parse(""));
                gmail.putExtra(Intent.EXTRA_SUBJECT, "Awesome New Android App");
                gmail.setType("plain/text");
                gmail.putExtra(Intent.EXTRA_TEXT, "There is a cool game release called Data Poker which educates users about the foods they eat. Make healthier eating choices, play Data Poker today!");
                startActivity(gmail);
            }
        });

        //RATINGS BAR
        addListenerOnRatingBar();

        //SPINNER SETUP
        spinner = (Spinner) findViewById(R.id.mySpinner);
        spinnerTopic = spinner.getSelectedItem().toString();
        myAdapter = new ArrayAdapter(this, R.layout.spinner_layout, new ArrayList<String>());
        myAdapter.add("Calories");
        myAdapter.add("Total Fat");
        myAdapter.add("Cholesterol");
        myAdapter.add("Sodium");
        myAdapter.add("Potassium");
        myAdapter.add("Total Carbohydrate");
        myAdapter.add("Dietary fiber");
        myAdapter.add("Sugar");
        myAdapter.add("Protein");
        myAdapter.add("Vitamin A");
        myAdapter.add("Vitamin C");
        myAdapter.add("Calcium");
        myAdapter.add("Iron");
        myAdapter.add("Vitamin B-6");
        myAdapter.add("Magnesium");
        spinner.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        //On return intent set topic to one returned from
        Intent i = getIntent();
        spinnerTopic = i.getStringExtra("Topic");
        int spin = myAdapter.getPosition(spinnerTopic);
        spinner.setSelection(spin);
        myAdapter.notifyDataSetChanged();

        //'One Player Mode' Button Listener and setup
        btnOnePlayer = (Button) findViewById(R.id.one_player_btn);
        btnOnePlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                musicCheckBox.setText("Music stopped");
                musicCheckBox.setChecked(false);
                spinnerTopic = spinner.getSelectedItem().toString();
                System.out.println("Category Chosen: " + spinnerTopic);
                Intent onePlayerIntent = new Intent(MainActivity.this, OnePlayer.class);
                onePlayerIntent.putExtra("Topic", spinnerTopic);
                startActivity(onePlayerIntent);
            }
        });

        //'Two Player Mode' Button Listener and setup
        btnTwoPlayer = (Button) findViewById(R.id.two_player_btn);
        btnTwoPlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                musicCheckBox.setText("Music stopped");
                musicCheckBox.setChecked(false);
                spinnerTopic = spinner.getSelectedItem().toString();
                System.out.println("Category Chosen: " + spinnerTopic);
                Intent twoPlayerIntent = new Intent(MainActivity.this, TwoPlayer.class);
                twoPlayerIntent.putExtra("Topic", spinnerTopic);
                startActivity(twoPlayerIntent);
            }
        });

    }

    @Override
    public void onPause() {
        if (player != null) player.stop();
        super.onPause();
    }

    @Override
    public void onResume() {
        if (player != null) player.start();
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    //RATINGS BAR FEATURE
    public void addListenerOnRatingBar() {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.textViewRating);
        txtRatingValue.setText("");
        //change the default gray color to yellow
        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.MAGENTA);
        //if rating value is changed, display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                txtRatingValue.setText("You gave this app a rating of "+String.valueOf(rating)+" stars");
                if (rating<=1) {
                    Drawable progress = ratingBar.getProgressDrawable();
                    DrawableCompat.setTint(progress, Color.RED);
                }
                else if ((rating<=2)&&(rating>1)) {
                    Drawable progress = ratingBar.getProgressDrawable();
                    DrawableCompat.setTint(progress, Color.DKGRAY);
                }

                else if ((rating<=3)&&(rating>2)){
                    Drawable progress = ratingBar.getProgressDrawable();
                    DrawableCompat.setTint(progress, Color.MAGENTA);
                }
                else if((rating<=4)&&(rating>3)){
                    Drawable progress = ratingBar.getProgressDrawable();
                    DrawableCompat.setTint(progress, Color.WHITE);
                }
                else if(rating>4){
                    Drawable progress = ratingBar.getProgressDrawable();
                    DrawableCompat.setTint(progress, Color.YELLOW);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        player.stop();
        if (user == null)
        {
            super.onBackPressed();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to logout of the game?");
        builder.setCancelable(true);
        //GO BACK TO MAIN MENU IF USER CHOOSES TO CLICK 'YES'
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (user != null)
                {
                    user.logOut();
                    MainActivity.this.user = null;
                    finish();
                }

            }
        });
        //DO NOT QUIT GAME IF USER CHOOSES TO CLICK 'CANCEL' CHOICE
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();



    }
}
