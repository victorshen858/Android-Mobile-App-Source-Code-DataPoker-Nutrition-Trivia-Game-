package gmu.cs.cs477.datapoker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Iterator;
/**
 * Created by 2013vshen on 11/5/2015.
 */
public class SignUpActivity extends AppCompatActivity {
    EditText usernameTextField;
    EditText passwordTextField;
    EditText retypePasswordTextField;
    //EditText emailTextField;

    Button signUpButton;

    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameTextField = (EditText) findViewById(R.id.usernameTextField);
        passwordTextField = (EditText) findViewById(R.id.passwordTextField);
        retypePasswordTextField = (EditText) findViewById(R.id.retypePasswordTextField);
        //emailTextField = (EditText) findViewById(R.id.emailTextField);

        signUpButton = (Button) findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("You pressed sign up");
                signUpButtonPress();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    public void signUpButtonPress()
    {
        System.out.println("Inside signUpButtonPress");
        if (isPromptValuesValid())
        {
            System.out.println("isPromptValuesValid() = TRUE");
            user = new ParseUser();
            user.setUsername(usernameTextField.getText().toString());
            user.setPassword(passwordTextField.getText().toString());
            //user.setEmail(emailTextField.getText().toString());

            user.signUpInBackground(new com.parse.SignUpCallback() {

                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        // Hooray! Let them use the app now.
                        Toast.makeText(SignUpActivity.this, "Successfully signed up", Toast.LENGTH_SHORT).show();
                        usernameTextField.setText("");
                        passwordTextField.setText("");
                        retypePasswordTextField.setText("");
                        //emailTextField.setText("");

                        finish();
                    } else {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                        Toast.makeText(SignUpActivity.this, "Failure to sign up", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            System.out.println("isPromptValuesValid() = FALSE");
            ArrayList<InputError> errors = getPromptErrors();
            String errorText = getErrorText(errors);
            System.out.println(errorText);
            Toast.makeText(SignUpActivity.this, errorText, Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isPromptValuesValid()
    {
        if (getPromptErrors().size() == 0)
        {
            return true;
        }

        return false;
    }

    private boolean usernameTextFieldEmpty()
    {
        if (usernameTextField.getText().toString().trim().equals(""))
        {
            return true;
        }
        return false;
    }

    private boolean passwordTextFieldEmpty()
    {
        if (passwordTextField.getText().toString().trim().equals(""))
        {
            return true;
        }
        return false;
    }

    private boolean retypePasswordTextFieldEmpty()
    {
        if (retypePasswordTextField.getText().toString().trim().equals(""))
        {
            return true;
        }
        return false;
    }

    private boolean passwordFieldsMatch()
    {
        if (passwordTextField.getText().toString().equals(retypePasswordTextField.getText().toString()))
        {
            return true;
        }
        return false;
    }

    /*private boolean emailFieldEmpty()
    {
        if (emailTextField.getText().toString().trim().equals(""))
        {
            return true;
        }
        return false;
    }*/

    private ArrayList<InputError> getPromptErrors()
    {
        ArrayList<InputError> errors = new ArrayList<InputError>();

        if (usernameTextFieldEmpty())
        {
            errors.add(InputError.EMPTY_USERNAME_FIELD);
        }

        if (passwordTextFieldEmpty())
        {
            errors.add(InputError.EMPTY_PASSWORD_FIELD);
        }

        if (retypePasswordTextFieldEmpty())
        {
            errors.add(InputError.EMPTY_RETYPE_PASSWORD_FIELD);
        }

        /*if (emailFieldEmpty())
        {
            errors.add(InputError.EMPTY_EMAIL_FIELD);
        }*/

        if (!passwordFieldsMatch() && (!passwordTextFieldEmpty() && !retypePasswordTextFieldEmpty()))
        {
            errors.add(InputError.PASSWORDS_DO_NOT_MATCH);
        }

        return errors;
    }

    private String getErrorText(ArrayList<InputError> errors)
    {
        String errorMessage = "Form incorrect: ";
        Iterator<InputError> errorIterator = errors.iterator();

        while (errorIterator.hasNext())
        {
            InputError error = errorIterator.next();
            errorMessage += " " + error.toString();
            if (errorIterator.hasNext())
            {
                errorMessage += ", ";
            }
            else
            {
                errorMessage += ". ";
            }
        }

        return errorMessage;
    }

    private enum InputError
    {
        //INCOMPLETE_PROMPT, PASSWORDS_DO_NOT_MATCH, PASSWORD_INVALID,
        //EMAIL_INVALID, USERNAME_INVALID, USERNAME_IN_USE,
        EMPTY_USERNAME_FIELD("empty username field"),
        EMPTY_PASSWORD_FIELD("empty password field"),
        EMPTY_RETYPE_PASSWORD_FIELD("empty retype-password field"),
        EMPTY_EMAIL_FIELD("empty email field"),
        PASSWORDS_DO_NOT_MATCH("password fields do not match");

        private final String name;

        private InputError(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName == null) ? false : name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }
}
