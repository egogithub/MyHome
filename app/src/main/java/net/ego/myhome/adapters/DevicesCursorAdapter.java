package net.ego.myhome.adapters;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import net.ego.myhome.R;
import net.ego.myhome.providers.DevicesProvider;

import androidx.recyclerview.widget.RecyclerView;

public class DevicesCursorAdapter  extends CursorAdapter {

    public DevicesCursorAdapter (Context context, Cursor c, int flags) {
        super(context, c, flags);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.device_listitem, null);
        final ViewHolder holder = new ViewHolder(view);

        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder)view.getTag();
        holder.deviceId.setText(cursor.getString(cursor.getColumnIndex(DevicesProvider.VALUE)));
        holder.deviceName.setText(cursor.getString(cursor.getColumnIndex(DevicesProvider.NAME)));

    }

    private class ViewHolder {
        public TextView deviceId;
        public TextView deviceName;

        public ViewHolder(View view) {
            deviceId = (TextView)view.findViewById(R.id.device_id);
            deviceName = (TextView)view.findViewById(R.id.device_name);
        }
    }
}
