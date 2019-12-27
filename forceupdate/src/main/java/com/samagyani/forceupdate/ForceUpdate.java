package com.samagyani.forceupdate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class ForceUpdate {

    private Context context;
    private String versionName = BuildConfig.VERSION_NAME;
    private String playStoreVersionName;
    private boolean cancelable = false;
    private boolean cancelableTouchOutSide = false;
    private CharSequence titleText = "Update Available";
    private String messageText = "An Update is Available With New Features";

    public ForceUpdate(Context context) {
        this.context = context;
    }


    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }


    public void setCanceledOnTouchOutside(boolean cancelableTouchOutSide) {
        this.cancelableTouchOutSide = cancelableTouchOutSide;
    }

    public void setTitle(@Nullable CharSequence title) {
        this.titleText = title;
    }

    public void setMessage(String message) {
        this.messageText = message;
    }


    public void build() {

        LoadUrl loadUrl = new LoadUrl();
        loadUrl.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadUrl extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            if (!isConnectivityOk()) {
                this.cancel(true);
                return;
            }

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                //String ver = Jsoup.connect("https://play.google.com/store/apps/details?id=com.snapchat.android&hl=it") // for testing purpose
                String ver = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(7)
                        .ownText();

                Log.d("From Playstore ", ver);
                playStoreVersionName = ver;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //if (playStoreVersionName > versionName) {
            //    Log.e("Current Version", String.valueOf(versionName));
            //    Log.e("Update Version", String.valueOf(playStoreVersionName));
            //    showUpdateDialog();
            //} else {
            //    Log.e("No Update Available", "Current Version: " + String.valueOf(playStoreVersionName));
            //}

            if (versionCompare(playStoreVersionName, versionName)) {
                Log.d("Current Version", String.valueOf(versionName));
                Log.d("Update Version", String.valueOf(playStoreVersionName));
                showUpdateDialog();
            } else {
                Log.d("No Update Available", "Current Version: " + String.valueOf(playStoreVersionName));
            }
        }
    }

    private boolean versionCompare(String NewVersion, String OldVersion) {
        String[] vals1 = NewVersion.split("\\.");
        String[] vals2 = OldVersion.split("\\.");
        try {
            int i = 0;
            // set index to first non-equal ordinal or length of shortest version string
            while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
                i++;
            }
            // compare first non-equal ordinal number
            if (i < vals1.length && i < vals2.length) {
                int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
                return Integer.signum(diff) > 0;
            }
            // the strings are equal or one string is a substring of the other
            // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
            return Integer.signum(vals1.length - vals2.length) > 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getUrl() {
        return "https://play.google.com/store/apps/details?id=" + context.getPackageName();
    }

    private void showUpdateDialog() {

        final Dialog updateDialog = new Dialog(context);
        updateDialog.setContentView(R.layout.update_dialog);
        updateDialog.setCanceledOnTouchOutside(cancelableTouchOutSide);

        TextView title = updateDialog.findViewById(R.id.title);
        TextView message = updateDialog.findViewById(R.id.message);
        ImageView updateIcon = updateDialog.findViewById(R.id.update_icon);
        updateIcon.setImageResource(android.R.drawable.ic_dialog_info);

        title.setText(titleText);
        message.setText(messageText);

        GradientDrawable updateShape = new GradientDrawable();
        updateShape.setColor(context.getResources().getColor(android.R.color.holo_green_dark));
        updateShape.setCornerRadii(new float[]{10f, 10f, 10f, 10f, 10f, 10f, 10f, 10f});

        Button updateButton = updateDialog.findViewById(R.id.updateButton);
        updateButton.setBackground(updateShape);

        GradientDrawable ignoreShape = new GradientDrawable();
        ignoreShape.setColor(context.getResources().getColor(android.R.color.holo_red_dark));
        ignoreShape.setCornerRadii(new float[]{10f, 10f, 10f, 10f, 10f, 10f, 10f, 10f});

        Button ignoreButton = updateDialog.findViewById(R.id.ignoreButton);
        ignoreButton.setBackground(ignoreShape);


        updateDialog.show();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getUrl()));
                context.startActivity(intent);
            }
        });

        ignoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDialog.dismiss();
            }
        });
    }

    private boolean isConnectivityOk() {
        try {
            NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            return activeNetwork != null && (activeNetwork.getType() == 1 || activeNetwork.getType() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}
