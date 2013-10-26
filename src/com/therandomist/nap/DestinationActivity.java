package com.therandomist.nap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.therandomist.nap.util.FontSetter;

public class DestinationActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination);
        setupFonts();
    }

    private void setupFonts(){
        FontSetter.setFont((TextView) findViewById(R.id.name_label), getAssets());
        FontSetter.setFont((EditText) findViewById(R.id.name_textfield), getAssets());

        FontSetter.setFont((Button) findViewById(R.id.save_button), getAssets());
    }
}
