package com.example.gabrielmamani.carneteitorapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

public class Utils
{

    static void displayMessageDialog(Context context, String title, String message, DialogInterface.OnClickListener okAction)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, okAction)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
