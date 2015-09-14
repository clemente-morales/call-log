package lania.com.mx.calllog.events;

import java.util.Date;

/**
 * Created by clerks on 9/14/15.
 */
public class SelectionDateEvent {
    private final int invoker;
    private final Date selectedDate;

    public SelectionDateEvent(int invoker, Date selectedDate) {
        this.invoker = invoker;
        this.selectedDate = selectedDate;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public int getInvoker() {
        return invoker;
    }
}
