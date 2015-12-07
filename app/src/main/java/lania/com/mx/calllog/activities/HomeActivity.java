package lania.com.mx.calllog.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import lania.com.mx.calllog.R;
import lania.com.mx.calllog.adapters.PhoneCallsAdapter;
import lania.com.mx.calllog.fragments.ExportContactFragment;
import lania.com.mx.calllog.fragments.MainActivityFragment;
import lania.com.mx.calllog.models.PhoneFunctionality;

/**
 * Created by clerks on 12/4/15.
 */
public class HomeActivity extends Activity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private ListView leftDrawerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftDrawerListView = (ListView) findViewById(R.id.leftDrawerListView);
        loadPhoneOptions();
    }

    private void loadPhoneOptions() {
        String[] phoneOptions = getResources().getStringArray(R.array.phoneOptions);
        List<PhoneFunctionality> functions = new ArrayList<>();

        for (int i = 0; i < phoneOptions.length; i++) {
            functions.add(new PhoneFunctionality(phoneOptions[i]));
        }

        leftDrawerListView.setAdapter(new PhoneCallsAdapter(this, functions));
        leftDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhoneFunctionality selectedElement = (PhoneFunctionality) parent.getItemAtPosition(position);
                setContentFragment(getFragmentToDisplay(selectedElement));

                leftDrawerListView.setItemChecked(position, true);

                drawerLayout.closeDrawer(leftDrawerListView);
            }
        });

        loadDefaultContentFragment();
    }

    private void loadDefaultContentFragment() {
        setContentFragment(new ExportContactFragment());
    }

    private void setContentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contentFrameLayout, fragment)
                .commit();
    }

    private Fragment getFragmentToDisplay(PhoneFunctionality functionality) {
        Log.d(TAG, "Option selected " + functionality);
        if (functionality.getName().equals(getString(R.string.phoneCallHistoryTitle)))
            return new MainActivityFragment();

        return new ExportContactFragment();

    }
}
