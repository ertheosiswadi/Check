package siswadi.com.check;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Button signupButton = (Button) findViewById(R.id.signupButton);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        final EditText usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account account = helper.getAccount(usernameEditText.getText().toString());

                String realPassword = account.getPassword();
                String toBeCheckedPassword = passwordEditText.getText().toString();
                System.out.println("1 " + account.getId());
                /*Toast t2 = Toast.makeText(getApplicationContext(), helper.getAccount(usernameEditText.getText().toString()).getId() + " ID", Toast.LENGTH_LONG);
                t2.show();*/

                if(realPassword == null)
                {
                    Toast t1 = Toast.makeText(getApplicationContext(), "USERNAME/PASSWORD don't exist", Toast.LENGTH_LONG);
                    t1.show();
                }
                else if(realPassword.equals(toBeCheckedPassword))
                {
                    startDrawerActivity(account);
                }
                else
                {
                    Toast t1 = Toast.makeText(getApplicationContext(), "Wrong -> " + realPassword, Toast.LENGTH_LONG);
                    t1.show();
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignUpActivity();
            }
        });
    }



    private void startSignUpActivity()
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void startDrawerActivity(Account a)
    {
        System.out.println("2 " + a.getId());
        Intent intent = new Intent(this, DrawerActivity.class);
        intent.putExtra("AccountId", a.getId());
        startActivity(intent);
    }
}
