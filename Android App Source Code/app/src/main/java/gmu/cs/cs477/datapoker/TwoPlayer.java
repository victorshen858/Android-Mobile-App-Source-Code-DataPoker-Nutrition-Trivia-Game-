package gmu.cs.cs477.datapoker;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
//Data Poker Game App by Victor Shen, Kai Johnson, and Chris Lee
public class TwoPlayer extends AppCompatActivity {
    String spinnerTopic = "";
    TextView tv1, tv2, p1score1TV,p2score2TV,p1Upright,p2UpsideDown;
    Food item, f1, f2, f;
    public ArrayList<Food> foodArray = new ArrayList<Food>();
    ImageView image1, image2,image3, image4;
    private static MediaPlayer player;
    Bitmap b;
    boolean usergotAnswerRight = false;
    public int randomInt = 0;
    public String resultString = "";
    public static int CHANGE_BGCOLOR = 1;
    private RelativeLayout bgColor;
    String nextColor = "";
    Message m;
    public static int p1Score, p2Score=0;



    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                String color = (String) msg.obj;
                bgColor.setBackgroundColor(Color.parseColor(color));
                nextColor = "GREEN"; // Next background color;
                m = obtainMessage(CHANGE_BGCOLOR, nextColor);
                sendMessageDelayed(m, 1000);
                CHANGE_BGCOLOR = 2;
            }
            if (msg.what == 2) {
                String color = (String) msg.obj;
                bgColor.setBackgroundColor(Color.parseColor(color));
                nextColor = "YELLOW"; // Next background color;
                m = obtainMessage(CHANGE_BGCOLOR, nextColor);
                sendMessageDelayed(m, 1000);
                CHANGE_BGCOLOR = 3;
            }
            if (msg.what == 3) {
                String color = (String) msg.obj;
                bgColor.setBackgroundColor(Color.parseColor(color));
                nextColor = "CYAN"; // Next background color;
                m = obtainMessage(CHANGE_BGCOLOR, nextColor);
                sendMessageDelayed(m, 1000);
                CHANGE_BGCOLOR = 1;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);
        p1Score=0;
        p2Score=0;
        //ANIMATION TO CHANGE BACKGROUND COLOR EVERY TWO SECONDS FOR INITIAL STARTING OF GAME
        bgColor = (RelativeLayout) findViewById(R.id.relative_layout_two_player);
        getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
        nextColor = "CYAN"; // Next background color;
        m = handler.obtainMessage(CHANGE_BGCOLOR, nextColor);
        handler.sendMessageDelayed(m, 1000);
        nextColor = "MAGENTA"; // Next background color;
        m = handler.obtainMessage(CHANGE_BGCOLOR, nextColor);
        handler.sendMessageDelayed(m, 1000);
        nextColor = "CYAN";
        m = handler.obtainMessage(CHANGE_BGCOLOR, nextColor);
        handler.sendMessageDelayed(m, 1000);

        if(player!=null){ player.stop(); }
        player = MediaPlayer.create(TwoPlayer.this, R.raw.dapolkaman);
        player.setLooping(true);
        player.start();

        TextView dash = (TextView)findViewById(R.id.textView4);
       // dash.setVisibility(View.GONE);

        image1 = (ImageView) findViewById(R.id.imageView1);
        image2 = (ImageView) findViewById(R.id.imageView2);
        image3 = (ImageView) findViewById(R.id.imageView3);
        image4 = (ImageView) findViewById(R.id.imageView4);

        Intent intent = getIntent();
        spinnerTopic = intent.getStringExtra("Topic");
        tv1 = (TextView)findViewById(R.id.p1Category);
        tv1.setText("Nutrition Category: " + spinnerTopic);
        tv2 = (TextView)findViewById(R.id.p2Category);
        tv2.setText("Nutrition Category: " + spinnerTopic);
        p1score1TV = (TextView)findViewById(R.id.p1TextView);
        p1score1TV.setText(""+p1Score);
        p2score2TV = (TextView)findViewById(R.id.p2TextView);
        p2score2TV.setText(""+p2Score);
        p1Upright = (TextView)findViewById(R.id.p1clickUpright);
        p2UpsideDown = (TextView)findViewById(R.id.p2clickUpsideDown);

        //SETUP ARRAY OF ALL POSSIBLE FOODS 18 food items in array as of 11-8-15
        initializeFoodArray();
        //RANDOMLY GENERATE TWO FOODS AND DISPLAY THE IMAGES ON THE SCREEN TO USER
        generateRandomTwoImages();


        //ON CLICK LISTENER FOR IMAGE 1-player 1
        image1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkAnswers(f1, f2, spinnerTopic, "Player 1");
            }
        });
        //ON CLICK LISTENER FOR IMAGE 2-player 1
        image2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkAnswers(f2, f1, spinnerTopic, "Player 1");
            }
        });
        //ON CLICK LISTENER FOR IMAGE 3-player 2
        image3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkAnswers(f1, f2, spinnerTopic,"Player 2");
            }
        });
        //ON CLICK LISTENER FOR IMAGE 4-player 2
        image4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkAnswers(f2, f1, spinnerTopic, "Player 2");
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
        image1.setClickable(true);
        image2.setClickable(true);
        image3.setClickable(true);
        image4.setClickable(true);
        p1Upright.setText("Click on the more nutritious food FIRST! (first to 3 wins the game)");
        p2UpsideDown.setText("Click on the more nutritious food FIRST! (first to 3 wins the game)");
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
        image3.setImageBitmap(i1);
        image4.setImageBitmap(i2);

    }

    public void checkAnswers(Food chosenFood, Food otherFood, String cat, String playerNum){
        image1.setClickable(false);
        image2.setClickable(false);
        image3.setClickable(false);
        image4.setClickable(false);
        double chosenVal = chosenFood.getFoodValueFromCategory();
        double otherVal = otherFood.getFoodValueFromCategory();
        String chosenFoodName = chosenFood.getFoodName();
        String otherFoodName = otherFood.getFoodName();
        String c = cat.toLowerCase();
        String playerNumber = playerNum;
        usergotAnswerRight = false;
        System.out.println("Player " +playerNumber+"answered first!");
        System.out.println("Category: " + cat + " chosenFood val: " + chosenVal + " otherFoodVal: " + otherVal);
        //For categories calories, totalFat, cholesterol, sodium, totalCarbohydrates, sugar
        //the Food with the lower value is better
        if((c.equals("calories"))||(c.equals("totalfat"))||(c.equals("cholesterol"))||(c.equals("sodium"))
                ||(c.equals("totalCarbohydrates"))||(c.equals("totalCarbohydrates"))||(c.equals("sugar"))){
            if(chosenVal<otherVal){
                usergotAnswerRight = true;
                //Toast.makeText(OnePlayer.this,"USER GOT ANSWER RIGHT",Toast.LENGTH_SHORT).show();
                resultString = chosenFoodName.toUpperCase()+" beats "+otherFoodName.toUpperCase()+" with regard to "+c+".";

            }
            else if(chosenVal==otherVal){
                usergotAnswerRight = true;
                //Toast.makeText(OnePlayer.this,"TIE: EITHER ANSWER IS CORRECT",Toast.LENGTH_SHORT).show();
                resultString = chosenFoodName.toUpperCase()+" is the same(Tie) "+otherFoodName.toUpperCase()+" with regard to "+c+".";

            }
            else{
                usergotAnswerRight = false;
                // Toast.makeText(OnePlayer.this,"USER GOT ANSWER WRONG",Toast.LENGTH_SHORT).show();
                resultString = chosenFoodName.toUpperCase()+" is not as healthy as "+otherFoodName.toUpperCase()+" with regard to "+c+".";

            }

        }
        //For other categories the Food with the higher value wins
        else {
            if (chosenVal > otherVal) {
                usergotAnswerRight = true;
                //Toast.makeText(OnePlayer.this, "USER GOT ANSWER RIGHT", Toast.LENGTH_SHORT).show();
                resultString = chosenFoodName.toUpperCase()+" beats "+otherFoodName.toUpperCase()+" with regard to "+c+".";

            } else if (chosenVal == otherVal) {
                usergotAnswerRight = true;
                //Toast.makeText(OnePlayer.this, "TIE: EITHER ANSWER IS CORRECT", Toast.LENGTH_SHORT).show();
                resultString = chosenFoodName.toUpperCase()+" is the same(Tie) "+otherFoodName.toUpperCase()+" with regard to "+c+".";
            } else {
                usergotAnswerRight = false;
                //Toast.makeText(OnePlayer.this, "USER GOT ANSWER WRONG", Toast.LENGTH_SHORT).show();
                resultString = chosenFoodName.toUpperCase()+" is not as healthy as "+otherFoodName.toUpperCase()+" with regard to "+c+".";

            }
        }
        updateScore(playerNumber,usergotAnswerRight, resultString);
    }

    public void updateScore(String play1OR2, Boolean answerResult, String resultStr){
        String playerNum = play1OR2;
        Boolean rightOrWrong = answerResult;
        String foodResultString = resultStr;
        if(playerNum.equals("Player 1")){
            if(rightOrWrong==true){
                p1Score++;
                p2UpsideDown.setText("Player 1 answered correctly that "+foodResultString);
                p1Upright.setText("Correct! " + foodResultString + " +1 points");
            }
            else{
                p1Score--;
                p2UpsideDown.setText("Player 1 answered incorrectly since "+foodResultString);
                p1Upright.setText("Incorrect! " + foodResultString + " -1 points");
            }
            p1score1TV.setText(""+p1Score);
            //IF PLAYER 1 GETS 3 POINTS or player 2 gets -10 points HE/SHE WINS THE GAME
            if((p1Score==3)||(p2Score==-10)) {
                Toast toast = new Toast(TwoPlayer.this);
                ImageView view = new ImageView(TwoPlayer.this);
                //GAME ENDS IF NUMBER OF CHIPS EQUALS ZERO
                Bitmap bImage = BitmapFactory.decodeResource(getResources(), R.drawable.player1wins);
                view.setImageBitmap(bImage);
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                toast.setView(view);
                toast.setGravity(Gravity.FILL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                player.stop();
                //RETURN CHIP NUMBER TO ONE PLAYER but delay for a seconds to allow for image to display first
                Handler h2 = new Handler();
                h2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        p1Score=0;
                        p2Score=0;
                        Intent done = new Intent(TwoPlayer.this, MainActivity.class);
                        startActivity(done);
                    }
                }, 3000);
            }
            else{
                Handler h3 = new Handler();
                h3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        generateRandomTwoImages();
                    }
                }, 2000);

            }

        } else if (playerNum.equals("Player 2")) {
            if (rightOrWrong == true) {
                p2Score++;
                p1Upright.setText("Player 2 answered correctly that "+foodResultString);
                p2UpsideDown.setText("Correct! "+foodResultString +"+1 points");
            } else {
                p2Score--;
                p1Upright.setText("Player 2 answered incorrectly since "+foodResultString);
                p2UpsideDown.setText("Incorrect! "+foodResultString +" -1 points");
            }
            p2score2TV.setText(""+p2Score);
            //IF PLAYER 2 GETS 3 POINTS or player 1 gets -10 points HE/SHE WINS THE GAME
            if (p2Score == 3||(p1Score==-10)) {
                Toast toast = new Toast(TwoPlayer.this);
                ImageView view = new ImageView(TwoPlayer.this);
                //GAME ENDS IF NUMBER OF CHIPS EQUALS ZERO
                Bitmap bImage = BitmapFactory.decodeResource(getResources(), R.drawable.player2wins);
                view.setImageBitmap(bImage);
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                toast.setView(view);
                toast.setGravity(Gravity.FILL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                player.stop();
                //RETURN CHIP NUMBER TO ONE PLAYER but delay for a seconds to allow for image to display first
                Handler h4 = new Handler();
                h4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        p1Score=0;
                        p2Score=0;
                        Intent done = new Intent(TwoPlayer.this, MainActivity.class);
                        startActivity(done);
                    }
                }, 3000);
            }
            else{
                Handler h5 = new Handler();
                h5.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        generateRandomTwoImages();
                    }
                }, 2000);

            }

        }
    }



    @Override
    public void onBackPressed(){
        p1Score=0;
        p2Score=0;
        player.stop();
        finish();
    }

}//end of TwoPlayer.java
