package com.tecraa.sayhi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Utils {

    public static void showAlert(Context context,String message,Integer icon){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View dialogView= LayoutInflater.from(context).inflate(R.layout.item_aletr_simple, null);
        builder.setView(dialogView);

        TextView text = (TextView) dialogView.findViewById(R.id.alertMessage);
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.alertIcon);
        AppCompatButton button = (AppCompatButton) dialogView.findViewById(R.id.alertOkay);

        final AlertDialog  alertDialog=builder.create();



        text.setText(message);
        imageView.setImageResource(icon);

        alertDialog.show();

        button.setOnClickListener(v->{
            alertDialog.dismiss();
        });


    }

}
