package top.ahdx.utils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class AhdxUtils {

    static boolean printFlag = true;

    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static Timestamp getTime(){
        return new Timestamp(new Date().getTime());
    }

    public static void print(String msg){
        if (printFlag){
            System.out.println("ahdx:=>"+msg);
        }
    }

}
