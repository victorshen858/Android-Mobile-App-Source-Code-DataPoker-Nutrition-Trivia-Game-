
package gmu.cs.cs477.datapoker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

//Data Poker Game App by Victor Shen, Kai Johnson, and Chris Lee
public class OnePlayer extends AppCompatActivity {
    String spinnerTopic = "";
    TextView tv, vsTV, clickTV, chipsTV;
    Food item, f1, f2, f;
    public ArrayList<Food> foodArray = new ArrayList<Food>();
    ImageView image1, image2;
    public static int CHANGE_BGCOLOR = 1;
    private RelativeLayout bgColor;
    String nextColor = "";
    Message m;
    private MediaPlayer player;
    Bitmap b;
    boolean usergotAnswerRight = false;
    public static int numOfQuestionsCorrect = 0, numOfQuestionsIncorrect = 0, numOfQuestions = 0;
    public static int numOfChips = 5;
    Button btnQuitGame;
    public int randomInt = 0;
    public String resultString = "";
    TextView facebookTVClickable;

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
        bgColor = (RelativeLayout) findViewById(R.id.relative_layout_one_player);
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
        if(player!=null){ player.stop(); }
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

        //---------SETUP OF INITIAL UI FOR A NUTRITION QUESTION------------
        chipsTV= (TextView) findViewById(R.id.chipsTV);
        chipsTV.setText("Number of Chips: "+numOfChips);


        facebookTVClickable = (TextView)findViewById(R.id.textViewFacebook);
        facebookTVClickable.setPaintFlags(facebookTVClickable.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        facebookTVClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/Data-Poker-477191005794572/timeline?ref=page_internal";
                Intent iFacebook = new Intent(Intent.ACTION_VIEW);
                iFacebook.setData(Uri.parse(url));
                startActivity(iFacebook);
            }
        });



        //IMAGE CLICKED ON
        clickTV= (TextView) findViewById(R.id.textView3);
        clickTV.setText("Click on the more nutritious food with regard to "+spinnerTopic+"!");
        vsTV = (TextView) findViewById(R.id.textView4);
        image1 = (ImageView) findViewById(R.id.imageView1);
        image2 = (ImageView) findViewById(R.id.imageView2);
        btnQuitGame = (Button)findViewById(R.id.quit_button);
        btnQuitGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OnePlayer.this);
                builder.setMessage("Do you really want to go back to the menu?\nYour current score will be saved.");
                builder.setCancelable(true);
                //GO BACK TO MAIN MENU IF USER CHOOSES TO CLICK 'YES'
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        player.stop();
                        finish();
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
        });


        //SETUP ARRAY OF ALL POSSIBLE FOODS 18 food items in array as of 11-8-15
        initializeFoodArray();
        //RANDOMLY GENERATE TWO FOODS AND DISPLAY THE IMAGES ON THE SCREEN TO USER
        generateRandomTwoImages();
        //ON CLICK LISTENER FOR IMAGE 1
        image1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast toast = Toast.makeText(OnePlayer.this, "You chose " + f1.getFoodName().toUpperCase() + " as your answer", Toast.LENGTH_SHORT);
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                TextView toastTV = (TextView) toastLayout.getChildAt(0);
                toastTV.setTextSize(58);
                toastTV.setWidth(20500);
                toastTV.setHeight(550);
                toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                toast.setGravity(Gravity.FILL_HORIZONTAL,0,0);
                toast.show();
                checkAnswers(f1, f2, spinnerTopic);
            }
        });
        //ON CLICK LISTENER FOR IMAGE 2
        image2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast toast = Toast.makeText(OnePlayer.this,"You chose "+f2.getFoodName().toUpperCase()+" as your answer",Toast.LENGTH_SHORT);
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                TextView toastTV = (TextView) toastLayout.getChildAt(0);
                toastTV.setTextSize(58);
                toastTV.setWidth(20500);
                toastTV.setHeight(550);
                toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                toast.setGravity(Gravity.FILL_HORIZONTAL,0,0);
                toast.show();
                checkAnswers(f2, f1, spinnerTopic);
            }

        });


    }

    public void initializeFoodArray(){
        //ADD BROCCOLI TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.broccoli);
        item = new Food("Broccoli","Vegetable",spinnerTopic,34,.4,0,.033,.316,7,2.6,1.7,2.8,623,.0892,.047,.0007,.0002,.021, b);
        foodArray.add(item);
        //ADD ORANGE TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.orange);
        item = new Food("Orange","Fruit",spinnerTopic,47,.1,0,0,.181,12,2.4,9,.9,225,.0532,.040,.0001,.0001,.010, b);
        foodArray.add(item);
        //ADD APPLE TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.apple);
        item = new Food("Apple","Fruit",spinnerTopic,52,.2,0,.001,.107,14,2.4,10,.3,54,.0046,.006,.0001,0,.005, b);
        foodArray.add(item);
        //ADD AVOCADO TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.avocado);
        item = new Food("Avocado","Fruit",spinnerTopic,160,15,0,.007,.485,9,7,.7,2,146,.0100,.012,.0006,.0003,.029, b);
        foodArray.add(item);
        //ADD BANANA TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.banana);
        item = new Food("Banana","Fruit",spinnerTopic,89,.3,0,.001,.358,23,2.6,12,1.1,64,.087,.005,.0003,.0004,.027, b);
        foodArray.add(item);
        //ADD BEETS TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.beets);
        item = new Food("Beets","Vegetable",spinnerTopic,43,.2,0,.078,.325,10,2.8,7,1.6,33,.049,.016,.0008,.0001,.023, b);
        foodArray.add(item);
        //ADD BLUEBERRIES TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.blueberry);
        item = new Food("Blueberry","Fruit",spinnerTopic,57,.3,0,.001,.077,14,2.4,10,0.7,54,.097,.006,.0003,.0001,.006, b);
        foodArray.add(item);
        //ADD CHERRY TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.cherry);
        item = new Food("Cherry","Fruit",spinnerTopic,50,.3,0,.003,.173,12,1.6,8,1,1283,.1,.016,.0003,.0000,.009, b);
        foodArray.add(item);
        //ADD CORN TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.corn);
        item = new Food("Corn","Vegetable",spinnerTopic,365,4.7,0,.035,.287,74,9,0,0,.007,.027,.007,.0027,.0006,.127, b);
        foodArray.add(item);
        //ADD EGG TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.egg);
        item = new Food("Egg","Meat",spinnerTopic,155,11,.373,.124,.126,1.1,0,1.1,13,520,0,.050,.0012,.0001,.010, b);
        foodArray.add(item);
        //ADD GRAPES TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.grapes);
        item = new Food("Grape","Fruit",spinnerTopic,67,0.4,0,.002,.191,17,.9,16,.6,100,4,.014,.0003,.0001,.005, b);
        foodArray.add(item);
        //ADD LEMON TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.lemon);
        item = new Food("Lemon","Fruit",spinnerTopic,29,0.3,0,.002,.138,9,2.8,2.5,1.1,22,53,.026,.0006,.0001,.008, b);
        foodArray.add(item);
        //ADD PEACH TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.peach);
        item = new Food("Peach","Fruit",spinnerTopic,39,0.3,0,0,.190,10,1.5,8,.9,326,6.6,.006,.0003,.0000,.009, b);
        foodArray.add(item);
        //ADD PEAR TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.pear);
        item = new Food("Pear","Fruit",spinnerTopic,57,0.1,0,.001,.116,15,3.1,10,.4,25,4.3,.009,.0002,.0000,.007, b);
        foodArray.add(item);
        //ADD PEPPER TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.pepper);
        item = new Food("Pepper","Vegetable",spinnerTopic,20,0.2,0,.003,.175,4.6,1.7,2.4,.9,370,80.4,.010,.0003,.0001,.010, b);
        foodArray.add(item);
        //ADD PINEAPPLE TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.pineapple);
        item = new Food("Pineapple","Fruit",spinnerTopic,50,0.1,0,.001,.109,13,1.4,10,.5,58,47.8,.013,.0003,.0001,.012, b);
        foodArray.add(item);
        //ADD STRAWBERRIES TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.strawberry);
        item = new Food("Strawberry","Fruit",spinnerTopic,33,0.3,0,.001,.153,8,2,4.9,.7,12,58.8,.016,.0004,.0000,.013, b);
        foodArray.add(item);
        //ADD WATERMELON TO ARRAY
        b = BitmapFactory.decodeResource(getResources(), R.drawable.watermelon);
        item = new Food("Watermelon","Fruit",spinnerTopic,30,0.2,0,.001,.112,8,.4,6,.6,569,8.1,.007,.0002,.0000,.010, b);
        foodArray.add(item);
    }

    public int getRandomInt(){
        Random ran = new Random();
        int r = ran.nextInt(18); //generate random value from [0,17]
        // System.out.println("randomInt() called, generated value of: "+r);
        return r;
    }

    public void generateRandomTwoImages(){
        randomInt = getRandomInt(); //get random string from choices array
        // System.out.println("random int generated: "+randomInt);
        f1 = foodArray.get(randomInt);
        randomInt = getRandomInt();
        f2 = foodArray.get(randomInt);
        //avoid having two duplicate food items
        while(f1.getFoodName().equals(f2.getFoodName())){
            randomInt = getRandomInt();
            f2 = foodArray.get(randomInt);
        }
        Bitmap i1 = f1.getBitmap();
        Bitmap i2 = f2.getBitmap();
        image1.setImageBitmap(i1);
        image2.setImageBitmap(i2);

        //for looping purposes
        image1.setVisibility(View.VISIBLE);
        image2.setVisibility(View.VISIBLE);
        clickTV.setVisibility(View.VISIBLE);
        vsTV.setVisibility(View.VISIBLE);
    }

    public void checkAnswers(Food chosenFood, Food otherFood, String cat){
        numOfQuestions++;
        double chosenVal = chosenFood.getFoodValueFromCategory();
        double otherVal = otherFood.getFoodValueFromCategory();
        String chosenFoodName = chosenFood.getFoodName();
        String otherFoodName = otherFood.getFoodName();
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
                //Toast.makeText(OnePlayer.this,"USER GOT ANSWER RIGHT",Toast.LENGTH_SHORT).show();
                resultString = "Correct, "+chosenFoodName+" beats "+otherFoodName+" with regard to "+c+".";
                numOfQuestionsCorrect++;
            }
            else if(chosenVal==otherVal){
                usergotAnswerRight = true;
                //Toast.makeText(OnePlayer.this,"TIE: EITHER ANSWER IS CORRECT",Toast.LENGTH_SHORT).show();
                resultString = "Correct, "+chosenFoodName+" is the same(Tie) "+otherFoodName+" with regard to "+c+".";
                numOfQuestionsCorrect++;
            }
            else{
                usergotAnswerRight = false;
                // Toast.makeText(OnePlayer.this,"USER GOT ANSWER WRONG",Toast.LENGTH_SHORT).show();
                resultString = "Incorrect, "+chosenFoodName+" is not as healthy as "+otherFoodName+" with regard to "+c+".";
                numOfQuestionsIncorrect++;
            }

        }
        //For other categories the Food with the higher value wins
        else {
            if (chosenVal > otherVal) {
                usergotAnswerRight = true;
                //Toast.makeText(OnePlayer.this, "USER GOT ANSWER RIGHT", Toast.LENGTH_SHORT).show();
                resultString = "Correct, "+chosenFoodName+" beats "+otherFoodName+" with regard to "+c+".";
                numOfQuestionsCorrect++;
            } else if (chosenVal == otherVal) {
                usergotAnswerRight = true;
                //Toast.makeText(OnePlayer.this, "TIE: EITHER ANSWER IS CORRECT", Toast.LENGTH_SHORT).show();
                resultString = "Correct, "+chosenFoodName+" is the same(Tie) "+otherFoodName+" with regard to "+c+".";
                numOfQuestionsCorrect++;
            } else {
                usergotAnswerRight = false;
                //Toast.makeText(OnePlayer.this, "USER GOT ANSWER WRONG", Toast.LENGTH_SHORT).show();
                resultString = "Incorrect, "+chosenFoodName+" is not as healthy as "+otherFoodName+" with regard to "+c+".";
                numOfQuestionsIncorrect++;
            }
        }
        //Display results at end of question
        //Toast.makeText(OnePlayer.this,"Stats for this question\nCorrect Questions so far: "+numOfQuestionsCorrect+
        //        " Incorrect Questions so far: "+numOfQuestionsIncorrect+"\nTotal Num of Questions Asked: "+numOfQuestions,Toast.LENGTH_LONG).show();
        payoff();
    }

    public void payoff()
    {
        player.stop();
        Intent p = new Intent(OnePlayer.this,Payoff.class);
        String bool = Boolean.toString(usergotAnswerRight).toLowerCase();
        p.putExtra("Result",bool);
        p.putExtra("rString",resultString);
        p.putExtra("numOfChips",numOfChips);
        startActivityForResult(p,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                player = MediaPlayer.create(OnePlayer.this, R.raw.reggaesamba);
                player.start();
                //update the total score
                int chipAmountFromIntent = data.getIntExtra("chips", 1);
                numOfChips = chipAmountFromIntent;
                chipsTV.setText("Number of Chips: " + numOfChips);
                //move onto next question
                generateRandomTwoImages();
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(OnePlayer.this, "No result returned :O", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void onBackPressed() {
        player.stop();
        finish();
    }

}
