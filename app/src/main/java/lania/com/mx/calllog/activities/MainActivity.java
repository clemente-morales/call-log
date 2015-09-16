package lania.com.mx.calllog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import lania.com.mx.calllog.R;
import lania.com.mx.calllog.fragments.MainActivityFragment;

public class MainActivity extends ActionBarActivity {

    private static final String PHONE_CALL_HISTORY_FRAGMENT_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.phoneCallHistoryFrameLayout,
                    new MainActivityFragment(), PHONE_CALL_HISTORY_FRAGMENT_TAG).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.phoneCallSettings) {
            Intent intent = new Intent(this, PhoneCallSettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
