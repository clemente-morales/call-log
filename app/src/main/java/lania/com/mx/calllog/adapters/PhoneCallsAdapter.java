package lania.com.mx.calllog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import lania.com.mx.calllog.R;
import lania.com.mx.calllog.models.PhoneFunctionality;

/**
 * Created by clerks on 9/15/15.
 */
public class PhoneCallsAdapter extends BaseAdapter {

    private final Context context;

    private List<PhoneFunctionality> functions = Collections.emptyList();

    public PhoneCallsAdapter(Context context, List<PhoneFunctionality> functions) {
        this.context = context;
        this.functions = functions;
    }

    @Override
    public int getCount() {
        return functions.size();
    }

    @Override
    public Object getItem(int position) {
        return functions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.phone_functionality_item, null);
            holder = new ViewHolder();
            holder.phoneFunctionTextView = (TextView) convertView.findViewById(R.id.phoneFunctionTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PhoneFunctionality device = functions.get(position);
        holder.phoneFunctionTextView.setText(device.getName());
        return convertView;
    }

    private static class ViewHolder {

        private TextView phoneFunctionTextView;
    }
}

