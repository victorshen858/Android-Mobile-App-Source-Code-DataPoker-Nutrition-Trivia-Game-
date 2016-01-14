package gmu.cs.cs477.datapoker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OnePlayer extends AppCompatActivity {
    String spinnerTopic = "";
    TextView tv, vsTV, clickTV;
    Food item, f1, f2;
    public ArrayList<Food> foodArray = new ArrayList<Food>();
    ImageView image1, image2;
    public static int CHANGE_BGCOLOR = 1;
    private RelativeLayout bgColor;
    String nextColor = "";
    Message m;
    private MediaPlayer player;
    Bitmap b;
    boolean usergotAnswerRight = false;
    public int points = 0, totalPoints = 0, numOfChips = 5, wagerLevel = 0;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
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
        setContentView(R.layout.activity_one_player);
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
        player = MediaPlayer.create(OnePlayer.this, R.raw.reggaesamba);
        player.setLooping(false);
        player.start();

        //Get Topic from Intent
        Intent intent = getIntent();
        spinnerTopic = intent.getStringExtra("Topic");
        if( spinnerTopic!=null) {
            tv = (TextView)findViewById(R.id.categoryTV);
            tv.setText("Nutrition Category: " + spinnerTopic);
        }
        //SETUP ARRAY OF ALL POSSIBLE FOODS
        /*public Food(String name, String type, String category, double calories, double totalFat, double cholesterol,
        double sodium, double potassium, double totalCarbohydrate, double dietaryFiber, double sugar,
        double protein, double vitaminA, double vitaminC, double calcium, double iron, double vitaminB6,
        double magnesium, int image)*/

        //ADD BROCCOLI TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.broccoli);
        item = new Food("Broccoli","Vegetable",spinnerTopic,34,.4,0,.033,.316,7,2.6,1.7,2.8,623,.0892,.047,.0007,.0002,.021, b);
        foodArray.add(item);
        //ADD ORANGE TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.orange);
        item = new Food("Orange","Fruit",spinnerTopic,47,.1,0,0,.181,12,2.4,9,.9,225,.0532,.040,.0001,.0001,.010, b);
        foodArray.add(item);


        //IMAGE CLICKED ON
        clickTV= (TextView) findViewById(R.id.textView3);
        vsTV = (TextView) findViewById(R.id.textView4);
        image1 = (ImageView) findViewById(R.id.imageView1);
        image2 = (ImageView) findViewById(R.id.imageView2);
        f1 = foodArray.get(0);
        f2 = foodArray.get(1);
        Bitmap i1 = foodArray.get(0).getBitmap();
        Bitmap i2 = foodArray.get(1).getBitmap();
        image1.setImageBitmap(i1);
        image2.setImageBitmap(i2);
        //ON CLICK LISTENER FOR IMAGE 1
        image1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(OnePlayer.this,"User chose "+f1.getFoodName().toUpperCase()+" image",Toast.LENGTH_SHORT).show();
                checkAnswers(f1, f2, spinnerTopic);
            }
        });
        //ON CLICK LISTENER FOR IMAGE 2
        image2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(OnePlayer.this,"User chose "+f2.getFoodName().toUpperCase()+" image",Toast.LENGTH_SHORT).show();
                checkAnswers(f2, f1, spinnerTopic);
            }

        });


    }

    public void checkAnswers(Food chosenFood, Food otherFood, String cat){
        double chosenVal = chosenFood.getFoodValueFromCategory();
        double otherVal = otherFood.getFoodValueFromCategory();
        String c = cat.toLowerCase();
        usergotAnswerRight = false;

        image1.setVisibility(View.GONE);
        image2.setVisibility(View.GONE);
        clickTV.setVisibility(View.GONE);
        vsTV.setVisibility(View.GONE);
        System.out.println("Category: "+cat+" chosenFood val: "+chosenVal+" otherFoodVal: " + otherVal);
        //For categories calories, totalFat, cholesterol, sodium, totalCarbohydrates, sugar
        //the Food with the lower value is better
        if((c.equals("calories"))||(c.equals("totalfat"))||(c.equals("cholesterol"))||(c.equals("sodium"))
                ||(c.equals("totalCarbohydrates"))||(c.equals("totalCarbohydrates"))||(c.equals("sugar"))){

            if(chosenVal<otherVal){
                usergotAnswerRight = true;
                Toast.makeText(OnePlayer.this,"USER GOT ANSWER RIGHT",Toast.LENGTH_SHORT).show();
                points++;
            }
            else if(chosenVal==otherVal){
                usergotAnswerRight = true;
                Toast.makeText(OnePlayer.this,"TIE: EITHER ANSWER IS CORRECT",Toast.LENGTH_SHORT).show();
                points++;
            }
            else{
                usergotAnswerRight = false;
                Toast.makeText(OnePlayer.this,"USER GOT ANSWER WRONG",Toast.LENGTH_SHORT).show();
                points++;
            }

        }
        //For other categories the Food with the higher value wins
        else{
            if(chosenVal>otherVal){
                usergotAnswerRight = true;
                Toast.makeText(OnePlayer.this,"USER GOT ANSWER RIGHT",Toast.LENGTH_SHORT).show();
                points++;
            }
            else if(chosenVal==otherVal){
                usergotAnswerRight = true;
                Toast.makeText(OnePlayer.this,"TIE: EITHER ANSWER IS CORRECT",Toast.LENGTH_SHORT).show();
                points++;
            }
            else{
                usergotAnswerRight = false;
                Toast.makeText(OnePlayer.this,"USER GOT ANSWER WRONG",Toast.LENGTH_SHORT).show();
                points++;
            }
        }

    }

    @Override
    public void onBackPressed() {
        player.stop();
        Intent i = new Intent(OnePlayer.this,MainActivity.class);
        i.putExtra("Topic",spinnerTopic);
        startActivity(i);
    }

}
