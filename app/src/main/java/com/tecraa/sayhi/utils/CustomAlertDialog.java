package com.tecraa.sayhi.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.tecraa.sayhi.R;

public class CustomAlertDialog {

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
