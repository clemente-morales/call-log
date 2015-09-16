package lania.com.mx.calllog.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

import lania.com.mx.calllog.CalendarHelper;
import lania.com.mx.calllog.CallLogApplication;
import lania.com.mx.calllog.events.SelectionDateEvent;

/**
 * Created by clerks on 9/14/15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final String INVOKER_ARGUMENT = "InvokerArgument";
    private static final String TAG = DatePickerFragment.class.getSimpleName();
    private int invoker;

    public static DatePickerFragment newInstance(int invoker) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INVOKER_ARGUMENT, invoker);
        fragment.setArguments(bundle);

        return  fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = GregorianCalendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        this.invoker = getArguments().getInt(INVOKER_ARGUMENT);

        return new DatePickerDialog(getActivity(), this, year, month, day);
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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        final Calendar calendar = CalendarHelper.getCalendarToBeginingOfCurrentDate();
        calendar.set(year, month, day);
        CallLogApplication.getEventBus().post(new SelectionDateEvent(invoker, calendar.getTime()));
    }
}
