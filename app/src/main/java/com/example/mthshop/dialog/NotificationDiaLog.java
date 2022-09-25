package com.example.mthshop.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.mthshop.R;

public class NotificationDiaLog {
    private static Toast toast;
    private static Dialog dialog;


    private static Toast getToast(Context context) {
        if (toast == null)
            toast = new Toast(context);

        return toast;
    }

    public static void showDiaLogValidDate(String content, Context context) {
        getToast(context).setDuration(Toast.LENGTH_SHORT);
        getToast(context).setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_validate_form, null);
        ((TextView) view.findViewById(R.id.toastValid_tvContent)).setText(content);
        getToast(context).setView(view);

        getToast(context).show();
    }

    public static void showProgressBar(Context context) {
        if (dialog == null) {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progress_bar);
        }


        Window window = dialog.getWindow();
        if (window == null)
            return;

        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);


        if (Gravity.BOTTOM == Gravity.CENTER) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }

        dialog.show();
    }

    public static void dismissProgressBar() {
        if (dialog == null)
            return;

        dialog.dismiss();
        dialog = null;
    }

    public static void showDiaLogLogOut(Context context) {
        Dialog dialogLogOut = new Dialog(context);
        dialogLogOut.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLogOut.setContentView(R.layout.dialog_logout);

        Window window = dialogLogOut.getWindow();
        if (window == null)
            return;

        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == Gravity.CENTER) {
            dialogLogOut.setCancelable(true);
        } else {
            dialogLogOut.setCancelable(false);
        }
        TextView tvCancel = dialogLogOut.findViewById(R.id.dialog_tvCancel);
        TextView tvOk = dialogLogOut.findViewById(R.id.dialog_tvOk);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLogOut.dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLogOut.dismiss();
                ((Activity) context).finish();
            }
        });

        dialogLogOut.show();
    }

}




