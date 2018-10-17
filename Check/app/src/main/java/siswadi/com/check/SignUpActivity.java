package siswadi.com.check;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
        Button submitButton = (Button) findViewById(R.id.submitButton);
        final EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        final EditText password2EditText = (EditText) findViewById(R.id.password2EditText);
        final EditText emailEditText = (EditText) findViewById(R.id.emailEditText);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account a = new Account();
                a.setUsername(usernameEditText.getText().toString());
                a.setEmail(emailEditText.getText().toString());
                a.setPassword(password2EditText.getText().toString());
                helper.addAccount(a);
                finish();
            }
        });
    }
}
