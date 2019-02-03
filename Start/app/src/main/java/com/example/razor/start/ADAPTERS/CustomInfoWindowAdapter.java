/*
    Created By: Rohan Ishwarkar
*/

package com.example.razor.start.ADAPTERS;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.example.razor.start.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context){
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window,null);
    }

    private void renderWindowText(Marker marker, View view){
        String aqiValue  = marker.getSnippet();
        TextView tvaqiValue = view.findViewById(R.id.aqi_value);
        if (!aqiValue.equals("")){
            tvaqiValue.setText(aqiValue);
        }
        String aqiDec  = marker.getSnippet();
        TextView tvaqiDec = view.findViewById(R.id.aqi_decision);
        if (!aqiDec.equals("")){
            tvaqiValue.setText(aqiDec);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }
}
