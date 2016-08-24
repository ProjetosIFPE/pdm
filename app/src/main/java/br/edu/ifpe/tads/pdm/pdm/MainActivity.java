package br.edu.ifpe.tads.pdm.pdm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends BaseActivity {

    public static final String EXTRA_MESSAGE = "br.edu.ifpe.tads.pdm.pdm.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        setUpNavDrawer();
        Button button = (Button) findViewById(R.id.send_button);
        button.setOnClickListener(sendMessage());
    }

    public View.OnClickListener sendMessage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.edit_text);
                Intent intent = new Intent(getContext(), DisplayMessageActivity.class);
                intent.putExtra(EXTRA_MESSAGE, editText.getText().toString());
                startActivity(intent);
            }
        };
    }

    public Context getContext() {
        return this;
    }

}
