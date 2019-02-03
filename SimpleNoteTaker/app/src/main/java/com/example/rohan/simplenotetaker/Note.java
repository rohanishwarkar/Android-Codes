package com.example.rohan.simplenotetaker;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by rohan on 8/3/18.
 */

public class Note implements Serializable {
    private long DateTime;
    private String Title;
    private String Content;

    public Note(long dateTime, String title, String content) {
        DateTime = dateTime;
        Title = title;
        Content = content;
    }

    public long getDateTime() {
        return DateTime;
    }

    public String getTitle() {
        return Title;
    }

    public String getContent() {
        return Content;
    }
    public String getformatted(Context context){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",context.getResources().getConfiguration().locale);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(DateTime));

    }
}
