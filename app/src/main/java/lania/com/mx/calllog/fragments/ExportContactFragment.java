package lania.com.mx.calllog.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import lania.com.mx.calllog.R;
import lania.com.mx.calllog.helpers.ContactsHelper;
import lania.com.mx.calllog.models.Action;

/**
 * Created by clerks on 12/6/15.
 */
public class ExportContactFragment extends Fragment {
    public static final int IMPORT_CONTACTS_REQUEST_CODE = 123;
    public static final int CONTACTS_EXPORT_BOUNDARY = 50;
    private static final String TAG = ExportContactFragment.class.getSimpleName();
    private ProgressBar progressBar;
    private Button exportContactsButton;
    private Button importContactsButton;
    private static final String FILE_PATH_DIRECTORY = "sdcard/contacts/";
    private List<String> files;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.export_contacts_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        exportContactsButton = (Button) getActivity().findViewById(R.id.exportContactsButton);
        importContactsButton = (Button) getActivity().findViewById(R.id.importContactsButton);
        exportContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportContacts();
            }
        });
        importContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importContacts();
            }
        });
        progressBar = (ProgressBar) getActivity().findViewById(R.id.taskProgressBar);
    }

    private void importContacts() {
        if (files == null)
            publishOnMainThread("First you need to export the contacts.");


        disableOptions();

        for (String file :
                files) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(file)), "text/x-vcard");
            startActivityForResult(intent, IMPORT_CONTACTS_REQUEST_CODE);
        }
        enableOptions();
    }

    private void exportContacts() {
        disableOptions();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ContactsHelper.exportContacts(getActivity(), FILE_PATH_DIRECTORY, CONTACTS_EXPORT_BOUNDARY, new Action() {
                    @Override
                    public void execute(Object... params) {
                        files = (List<String>) params[0];
                        publishOnMainThread("Contacts exported to " + FILE_PATH_DIRECTORY);
                    }
                });
            }
        }).start();
    }

    private void publishOnMainThread(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                enableOptions();
                Toast.makeText(getActivity(),
                        message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void disableOptions() {
        exportContactsButton.setVisibility(View.GONE);
        importContactsButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void enableOptions() {
        exportContactsButton.setVisibility(View.VISIBLE);
        importContactsButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
