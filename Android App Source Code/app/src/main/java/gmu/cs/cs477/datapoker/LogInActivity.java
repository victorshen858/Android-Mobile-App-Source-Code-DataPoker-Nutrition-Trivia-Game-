package gmu.cs.cs477.datapoker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import java.text.ParseException;

import javax.security.auth.login.LoginException;
//Data Poker Game App by Victor Shen, Kai Johnson, and Chris Lee
public class LogInActivity extends AppCompatActivity {
    Button signUpButton;
    Button logInButton;
    Button skipButton;

    EditText usernameTextField;
    EditText passwordTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        usernameTextField = (EditText) findViewById(R.id.usernameTextField);
        passwordTextField = (EditText) findViewById(R.id.passwordTextField);

        logInButton = (Button) findViewById(R.id.logInButton);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        skipButton = (Button) findViewById(R.id.buttonSkip);

        ParseUser user = ParseUser.getCurrentUser();
        if (user != null)
        {
            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(intent);
        }

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser.logInInBackground(usernameTextField.getText().toString(), passwordTextField.getText().toString(), new com.parse.LogInCallback() {

                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            // Hooray! The user is logged in.
                            Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT);
                            // TODO: Naviagate to Main activity
                            usernameTextField.setText("");
                            passwordTextField.setText("");

                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            // Signup failed. Look at the ParseException to see what happened.
                            // TODO: Ask for resubmission.
                            Toast.makeText(getApplicationContext(), "Failure to log in: " + e.toString(), Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent skip = new Intent(LogInActivity.this, MainActivity.class);
                startActivity(skip);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
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
}
