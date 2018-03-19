package com.anje.kelvin.aconting.Classes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sala on 19-03-2018.
 */

public class Convertar_Datas {

    public Convertar_Datas() {
    }

    public Calendar date_para_calendario(Date date) throws ParseException {
        DateFormat format=new SimpleDateFormat("dd/mm/yyyy");
        date=(Date)format.parse(date.toString());
        Calendar calendar;
        calendar=Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }

    public Date calendar_para_date(Calendar calendar){

        Date date=calendar.getTime();
        return date;
    }

    public String data (Calendar calendar){
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        String dia=format.format(calendar.getTime());
        return dia;
    }
    public String datac(Date date){
        String data;
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        data=format.format(date);

        return data;
    }
}
