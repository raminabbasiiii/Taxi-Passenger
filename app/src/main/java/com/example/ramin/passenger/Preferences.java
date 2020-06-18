package com.example.ramin.passenger;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private final Context context;
    private  static final String myPref = "PassengerPreferences";
    private  final String passengerName ="passengerName";
    private  final String passengerFamily = "passengerFamily";
    private  final String passengerSexuality = "passengerSexuality";
    private  final String passengerMail = "passengerMail";
    private  final String passengerMobile = "passengerMobile";
    private  final String passengerPassword = "passengerPassword";
    private  final String passengerId = "passengerId";
    private  final SharedPreferences passengerPreferences;

    public Preferences(Context context) {
        this.context = context;
        passengerPreferences = context.getSharedPreferences(myPref,Context.MODE_PRIVATE);
    }

    public void exitAccount() {
        SharedPreferences.Editor editor = passengerPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setPassengerSharedPreferences (int pId,String pName,String pFamily,String pSexuality,String pMail,String pMobile, String pPassword) {

        SharedPreferences.Editor editor = passengerPreferences.edit();
        editor.putInt(passengerId,pId);
        editor.putString(passengerName,pName);
        editor.putString(passengerFamily,pFamily);
        editor.putString(passengerSexuality,pSexuality);
        editor.putString(passengerMail,pMail);
        editor.putString(passengerMobile,pMobile);
        editor.putString(passengerPassword,pPassword);
        editor.apply();

    }

    public String getPassengerName() {
        String pName = null;
        if (passengerPreferences.contains(passengerName)) {
            pName = passengerPreferences.getString(passengerName,"not exist");
        }
        return pName;
    }

    public String getPassengerFamily() {
        String pFamily = null;
        if (passengerPreferences.contains(passengerFamily)) {
            pFamily = passengerPreferences.getString(passengerFamily,"not exist");
        }
        return pFamily;
    }

    public String getPassengerSexuality() {
        String pSexuality = null;
        if (passengerPreferences.contains(passengerSexuality)) {
            pSexuality = passengerPreferences.getString(passengerSexuality,"not exist");
        }
        return pSexuality;
    }

    public String getPassengerMail() {
        String pMail = null;
        if (passengerPreferences.contains(passengerMail)) {
            pMail = passengerPreferences.getString(passengerMail,"not exist");
        }
        return pMail;
    }

    public String getPassengerMobile() {
        String pMobile = null;
        if (passengerPreferences.contains(passengerMobile)) {
            pMobile = passengerPreferences.getString(passengerMobile,"not exist");
        }
        return pMobile;
    }

    public String getPassengerPassword() {
        String pPassword = null;
        if (passengerPreferences.contains(passengerPassword)) {
            pPassword = passengerPreferences.getString(passengerPassword,"not exist");
        }
        return pPassword;
    }

    public int getPassengerId() {
        int pId = 0;
        if (passengerPreferences.contains(passengerId)) {
            pId = passengerPreferences.getInt(passengerId,0);
        }
        return pId;
    }
}
