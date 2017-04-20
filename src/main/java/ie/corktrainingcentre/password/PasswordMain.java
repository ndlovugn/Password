package ie.corktrainingcentre.password;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class PasswordMain extends ActionBarActivity implements OnClickListener {

    EditText editPassword;
    Button btnCheck;
    Button btnGenerate;
    TextView txtWithSubstitution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_main);

        editPassword = (EditText)findViewById(R.id.editPassword);
        editPassword.setOnClickListener(this);
        btnCheck = (Button)findViewById(R.id.btnCheck);
        btnGenerate = (Button)findViewById(R.id.btnGenerate);
        txtWithSubstitution = (TextView)findViewById(R.id.txtWithSubstitution);

    }

    public void onClick(View v) {
        editPassword.setText("");
        txtWithSubstitution.setText("");
    }

    public void checkPassword(View v) {
        String pw = editPassword.getText().toString();
        if (Password.isPassword(pw)) {
            txtWithSubstitution.setText(
                    "With number substitution: \n" +
                            Password.substituteNumsForLetters( pw ) );
            Toast.makeText(this, "Password is valid.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Password is NOT valid!", Toast.LENGTH_LONG).show();
            onClick(v);
            //editPassword.setText("");
            //txtWithSubstitution.setText("");
        }
    }

    public void generatePassword(View v) {
        String pw = Password.generatePassword();
        editPassword.setText( pw );
        txtWithSubstitution.setText(
                "With number substitution: \n" +
                        Password.substituteNumsForLetters( pw ) );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_password_main, menu);
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
