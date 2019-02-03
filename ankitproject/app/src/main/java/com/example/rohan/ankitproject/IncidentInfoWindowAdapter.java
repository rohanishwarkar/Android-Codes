package com.example.rohan.ankitproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

/**
 * Created by rohan on 8/6/18.
 */

public class IncidentInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {


    /**
     * Created by rohan on 5/6/18.
     */


        private Context context;
        public IncidentInfoWindowAdapter(Context context) {
            this.context = context.getApplicationContext();
        }

        public String getvec(double[] v){
            String s="[";
            for(int i=0;i<v.length;i++){
                s=s+v[i];
                if(i!=v.length-1)
                    s=s+",";
            }
            s=s+"]";
            return s;
        }

        @Override
        public View getInfoWindow(Marker arg0) {
            return null;
        }

        @Override
        public View getInfoContents(Marker arg0) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v =  inflater.inflate(R.layout.infowindow, null);

            LatLng latLng = arg0.getPosition();
            TextView startyear = (TextView) v.findViewById(R.id.startyear);
            TextView initialvector = (TextView) v.findViewById(R.id.initialvector);
            TextView maxstate2 = (TextView) v.findViewById(R.id.maxstate2);
            TextView endyear = (TextView) v.findViewById(R.id.endyear);
            TextView finalvector = (TextView) v.findViewById(R.id.finalvector);

            Gson gson = new Gson();
            String ti = arg0.getTitle();
            windowdetails details  =gson.fromJson(arg0.getTitle(),windowdetails.class);
            double[] initialvec = details.getInitialvector(),finalvec=details.getFinalvec();
            int starty=details.getStartyear(),finaly=details.getFinalyear(),max2year=details.getState2year(),max3year=details.getState3year();
            double[] state2vec=details.getMax2(),state3vec=details.getMax3();

            startyear.setText("Start year: "+starty);
            initialvector.setText("Initial Vector: "+getvec(initialvec));
            if(max2year!=0)
                maxstate2.setText("Max at position 2:: Year: "+max2year+", Vector: "+getvec(state2vec));
            else
                maxstate2.setText("Max at position 2:: Not Available!");


            endyear.setText("End Year: "+finaly);
            finalvector.setText("Final Vector is: "+getvec(finalvec));
            return v;
        }
    }

