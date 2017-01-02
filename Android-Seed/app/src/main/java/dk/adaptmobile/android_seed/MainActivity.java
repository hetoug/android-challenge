package dk.adaptmobile.android_seed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dk.adaptmobile.amutil.misc.AMUtil;

public class MainActivity extends AppCompatActivity {

    private static int theme = R.style.AMTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(theme);
        setContentView(R.layout.activity_main);

        AMUtil.isFirstLaunch(this);

        Button buttonSwitch = (Button)findViewById(R.id.buttonSwitch);
        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(theme == R.style.AMTheme) {
                    theme = R.style.AM2Theme;
                } else {
                    theme = R.style.AMTheme;
                }
                recreate();
            }
        });

    }
}
