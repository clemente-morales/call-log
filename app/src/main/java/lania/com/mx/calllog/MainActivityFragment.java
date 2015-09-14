package lania.com.mx.calllog;

import android.app.DialogFragment;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import lania.com.mx.calllog.events.SelectionDateEvent;
import lania.com.mx.calllog.fragments.DatePickerFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();
    private Date startDate;
    private Date endDate;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ImageButton startDateButton = (ImageButton) getView().findViewById(R.id.startDateButton);
        addEventToOpenDatepicker(startDateButton);
        final ImageButton endDateButton = (ImageButton) getView().findViewById(R.id.endDateButton);
        addEventToOpenDatepicker(endDateButton);

        final Button getCallDetailsButton = (Button) getView().findViewById(R.id.getPhoCallsHistoryButton);
        getCallDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoneCallsHistory();
            }
        });

        loadDefaultValues();

    }

    private void loadDefaultValues() {
        Calendar calendar = GregorianCalendar.getInstance();
    }

    @Override
    public void onPause() {
        super.onPause();
        CallLogApplication.getEventBus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        CallLogApplication.getEventBus().register(this);
    }

    private void addEventToOpenDatepicker(final ImageButton selectionDateButton) {
        selectionDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(selectionDateButton.getId());
            }
        });
    }

    public void getPhoneCallsHistory() {
        if (startDate==null) {
            Toast.makeText(getActivity(), "You need to select the start date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endDate==null) {
            Calendar calendar = GregorianCalendar.getInstance();
            endDate = calendar.getTime();
            Toast.makeText(getActivity(), "You didn't select an end date. We took the current date.", Toast.LENGTH_SHORT).show();
        }

        String callDetails = getCallDetails();
        TextView callLogTextView = (TextView) getActivity().findViewById(R.id.callLogTextView);
        callLogTextView.setText(callDetails);
    }

    public void showDatePickerDialog(int invoker) {
        DialogFragment newFragment = DatePickerFragment.newInstance(invoker);
        newFragment.show(getActivity().getFragmentManager(), "datePicker");
    }

    private String getCallDetails() {
        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = getActivity().managedQuery(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        sb.append("Call Details :");
        StringBuffer result = new StringBuffer();

        while (managedCursor.moveToNext()) {
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));

            if (callDayTime.compareTo(startDate)>=0 && callDayTime.compareTo(endDate) < 0) {
                String phNumber = managedCursor.getString(number);
                String callType = managedCursor.getString(type);

                String callDuration = managedCursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }

                String phoneCall = String.format("%s\t%s\t%s\t%s\t%s\n", formatCallDate(callDayTime), "Clerks", phNumber, "Company x", "Android developer");
                result.append(phoneCall);
                System.out.println(phoneCall);

                sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                        + dir + " \nCall Date:--- " + callDayTime
                        + " \nCall duration in sec :--- " + callDuration);
                sb.append("\n----------------------------------");
            }
        }
        writeFile(result.toString());
        managedCursor.close();
        return sb.toString();

    }

    private void writeFile (String data) {
        try{
            File myFile = new File("/sdcard/phoneCalls.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();
        }catch(Exception e){}
    }

    private int getDateOfMonth(Date callDayTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return  Integer.parseInt(sdf.format(callDayTime));
    }

    private String formatCallDate(Date callDayTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        return  sdf.format(callDayTime);
    }

    @Subscribe
    public void onDateSelected(SelectionDateEvent event) {
        Log.d(TAG, String.format("Invoker %d Date %s", event.getInvoker(), event.getSelectedDate()));
        Log.d(TAG, String.format("StartDateId %d EndDateId %d", R.id.startDateButton, R.id.endDateButton));

        if (event.getInvoker() == R.id.startDateButton) {
            this.startDate = event.getSelectedDate();
            displayDate(startDate, R.id.startDateEditText);
        }

        if (event.getInvoker() == R.id.endDateButton) {
            this.endDate = event.getSelectedDate();
            displayDate(startDate, R.id.endDateEditText);
        }
    }

    private void displayDate(Date startDate, int controlId) {
        String formatDate = formatCallDate(startDate);
        setDateValue(controlId, formatDate);
    }

    private void setDateValue(int controlId, String formatDate) {
        EditText control = (EditText) getActivity().findViewById(controlId);
        control.setText(formatDate);
    }
}
