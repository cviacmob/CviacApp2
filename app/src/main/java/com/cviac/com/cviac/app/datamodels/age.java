package com.cviac.com.cviac.app.datamodels;

/**
 * Created by BALA on 12-01-2017.
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class age {


    public int age(String strDate1, String dateFormat1,String strDate2, String dateFormat2) {
            int years = 0;

            Date date1, date2 = null;
            SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1);
            SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormat2);
            SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

            try {
                date1 = (Date)sdf1.parse(strDate1);
                date2 = (Date)sdf2.parse(strDate2);

                int year1 = Integer.parseInt(sdfYear.format(date1));
                int year2 = Integer.parseInt(sdfYear.format(date2));

                years = year2 - year1;
            } catch (ParseException ex) {
                System.err.println(ex.getMessage());
            }

            return years;
        }

        public static void main(String[] args) {
            String strDate1 = "13-10-1988";
            String dateFormat1 = "dd-MM-yyyy";

            String strDate2 = "2011-10-13";
            String dateFormat2= "yyyy-MM-dd";

         age gnoybtd = new  age();
            int years = gnoybtd.age(strDate1, dateFormat1,
                    strDate2, dateFormat2);

            System.out.println("Number of Years: "+years+" Years");
        }


}
