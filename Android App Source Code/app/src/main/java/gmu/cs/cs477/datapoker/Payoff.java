package gmu.cs.cs477.datapoker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Random;
//Data Poker Game App by Victor Shen, Kai Johnson, and Chris Lee
public class Payoff extends AppCompatActivity {
    TextView sliderTV, confidenceTV,userChipsTV;
    SeekBar userConfidenceSlider;
    int userConfidenceLevel = 0;
    ImageView payoffImage;
    private MediaPlayer player;
    Bitmap b;
    Button btn_submit;
    String result="";
    int userChips = 0;
    String resultString = "";
    Toast toast;
    LinearLayout toastLayout;
    TextView toastTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payoff);
        Bundle bundle = getIntent().getExtras();
        result = bundle.getString("Result").toLowerCase();
        userChips = bundle.getInt("numOfChips");
        resultString = bundle.getString("rString");
        //CHANGES BACKGROUND COLOR
        RelativeLayout bgColor = (RelativeLayout) findViewById(R.id.relative_layout_one_player);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#fff49e"));
        //MUSIC STUFF
        if(player!=null){ player.stop(); }
        player = MediaPlayer.create(Payoff.this, R.raw.retrowalk);
        player.setLooping(false);
        player.start();
        //UI DISPLAY FOR PAYOFF
        confidenceTV = (TextView)findViewById(R.id.confidenceText);
        confidenceTV.setText("          How sure are you that you got the answer right?");
        confidenceTV.setTextSize((float) 25.0);
        confidenceTV.setTextColor(Color.BLACK);
        payoffImage = (ImageView)findViewById(R.id.imageViewPayoff);
        b = BitmapFactory.decodeResource(getResources(), R.drawable.coins);
        payoffImage.setImageBitmap(b);
        userChipsTV = (TextView)findViewById(R.id.userchipTV);
        userChipsTV.setText("You currently have " + userChips + " chips to wager.");
        userChipsTV.setTextSize((float) 25.0);
        userChipsTV.setTextColor(Color.BLACK);

        //Slider display TextView
        sliderTV  = (TextView)findViewById(R.id.levelTV);
        sliderTV.setText("Confidence Level: 0%");
        sliderTV.setTextColor(Color.BLACK);
        sliderTV.setTextSize((float) 25.0);
        userConfidenceSlider = (SeekBar)findViewById(R.id.seekBar);
        userConfidenceSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
                sliderTV.setText("Confidence Level: " + progressChanged + "%");
                userConfidenceLevel = progressChanged;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                sliderTV.setText("Confidence Level: " + progressChanged);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userConfidenceLevel = progressChanged;
            }
        });

        btn_submit = (Button)findViewById(R.id.submit_button);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn_submit.setClickable(false);
                System.out.println("Result from questions: "+result);
                System.out.println("numofChips before wager: "+ userChips);
                Random generator = new Random();
                double userConfidenceDouble = userConfidenceLevel/100;
                toast = Toast.makeText(Payoff.this, resultString, Toast.LENGTH_LONG);
                toastLayout = (LinearLayout) toast.getView();
                toastTV = (TextView) toastLayout.getChildAt(0);
                toastTV.setTextSize(58);
                toastTV.setWidth(20500);
                toastTV.setHeight(500);
                toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                toast.setGravity(Gravity.FILL_HORIZONTAL,0,0);
                toast.show();
                if(result.equals("true"))
                {
                    double addChips = 0;
                    //100% confident and GOT ANSWER RIGHT
                    if(userConfidenceLevel==100){

                        int luckyBonus = generator.nextInt(userChips)+3;
                        addChips = luckyBonus;
                    }
                    //User is confident [0,100) based on percentage
                    else{
                        double randomValue = 1 + (4 - 1) * generator.nextDouble();
                        addChips = ((double)userChips*userConfidenceDouble)+randomValue;
                    }
                    userChips += (int)addChips;
                    toast = Toast.makeText(Payoff.this, "ANSWER IS CORRECT.\nYou gained "+(int)addChips+" chips based on your wager.", Toast.LENGTH_SHORT);
                    toastLayout = (LinearLayout) toast.getView();
                    toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(58);
                    toastTV.setWidth(20500);
                    toastTV.setHeight(500);
                    toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                    toast.setGravity(Gravity.FILL_HORIZONTAL,0,0);
                    toast.show();
                    toast.show();
                }
                else
                {
                    double subChips = 0;
                    //100% confident and GOT ANSWER WRONG
                    if(userConfidenceLevel==100){
                        subChips = generator.nextInt(userChips)+2;

                    }
                    //0% confident and GOT ANSWER WRONG penalty for getting answer right and not having guts to do anything
                    else if(userConfidenceLevel==0){
                        subChips = generator.nextInt(3);
                    }
                    else {
                        double randomValue = 1 + (4 - 1) * generator.nextDouble();
                        subChips = ((double)userChips*userConfidenceDouble)+randomValue;
                    }
                    userChips -= (int)subChips;
                    toast = Toast.makeText(Payoff.this, "ANSWER IS INCORRECT.\nYou lost " + (int) subChips + " chips based on your wager.", Toast.LENGTH_SHORT);
                    toastLayout = (LinearLayout) toast.getView();
                    toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(58);
                    toastTV.setWidth(20500);
                    toastTV.setHeight(500);
                    toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                    toast.setGravity(Gravity.FILL_HORIZONTAL,0,0);
                    toast.show();
                    toast.show();
                }
				    /* Parse changes */
                ParseUser user = ParseUser.getCurrentUser();
                if (user != null) {
                    System.out.println("User found");
                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("user_score");
                    query.whereEqualTo("user", user);
                    System.out.println("Beginning query");
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if (e == null) {
                                System.out.println("Query found");
                                parseObject.put("chips_amount", OnePlayer.numOfChips);
                            } else {
                                System.out.println("Query not found");

                                ParseUser user = ParseUser.getCurrentUser();

                                ParseObject chipsForUser = new com.parse.ParseObject("user_score");
                                chipsForUser.put("chips_amount", OnePlayer.numOfChips);
                                chipsForUser.put("user", user);

                                chipsForUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null)
                                        {
                                            //Toast.makeText(Payoff.this, "Saved score successfully", Toast.LENGTH_SHORT).show();
                                            System.out.println("Saved score successfully");
                                        }
                                        else
                                        {
                                            //Toast.makeText(Payoff.this, "Failed to save score", Toast.LENGTH_SHORT).show();
                                            System.out.println("Failed to save score");

                                        }
                                    }
                                });
                            }
                        }
                    });

                    System.out.println("Query completed");

                }
                else
                {
                    System.out.println("Score failed to save");
                    //Toast.makeText(Payoff.this, "Score failed to save", Toast.LENGTH_SHORT).show();
                }

                //DISPLAY CORRECT IMAGE TO USER BASED ON WHETHER ANSWER IS RIGHT, WRONG, OR GAME OVER
                displayResultBitmap();
                //RETURN CHIP NUMBER TO ONE PLAYER but delay for a seconds to allow for image to display first
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("chips", userChips);
                        player.stop();
                        setResult(OnePlayer.RESULT_OK, returnIntent);
                        finish();
                    }
                }, 8500);


            }
        });

    }


    public void displayResultBitmap(){
        Bitmap bImage;
        Toast toast = new Toast(Payoff.this);
        ImageView view = new ImageView(Payoff.this);
        //GAME ENDS IF NUMBER OF CHIPS EQUALS ZERO
        if(userChips<=0)
        {
            bImage = BitmapFactory.decodeResource(getResources(), R.drawable.gameover);
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
                    Intent done = new Intent(Payoff.this, MainActivity.class);
                    startActivity(done);
                }
            }, 8500);

        }
        //GAME CONTINUES SO LONG AS USER DOES NOT HAVE ZERO CHIPS
        else{
            //user got answer right
            if(result.equals("true")) {
                bImage = BitmapFactory.decodeResource(getResources(), R.drawable.answercorrect);
                view.setImageBitmap(bImage);
                //view.setImageResource(R.drawable.answercorrect);
            }
            //user got answer wrong
            else{
                bImage = BitmapFactory.decodeResource(getResources(), R.drawable.answerwrong);
                view.setImageBitmap(bImage);//view.setImageResource(R.drawable.answerwrong);
            }
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            toast.setView(view);
            toast.setGravity(Gravity.FILL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }

    }



    @Override
    public void onBackPressed() {
        Toast.makeText(Payoff.this,"Please indicate your confidence level and click the 'Submit' button",Toast.LENGTH_SHORT).show();
    }


}
