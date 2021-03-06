package com.iamretailer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.LocaleHelper;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;


import stutzen.co.network.Connection;

public class Webactivity extends AppCompatActivity {

    private TextView header;
    private FrameLayout loading;
    private FrameLayout error_network;
    private FrameLayout fullayout;
    private TextView errortxt1;
    private TextView errortxt2;
    private AndroidLogger logger;
    private int id = 0;
    private String title = "";
    private WebView description;
    private String pish = "";
    private String pas = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        }
        setContentView(R.layout.activity_webactivity);
        CommonFunctions.updateAndroidSecurityProvider(this);
        DBController dbCon = new DBController(Webactivity.this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        Appconstatants.sessiondata = dbCon.getSession();
        Appconstatants.Lang = dbCon.get_lang_code();
        Appconstatants.CUR = dbCon.getCurCode();
        Log.d("Session", Appconstatants.sessiondata + "Value");
        LinearLayout back = findViewById(R.id.menu);
        header = findViewById(R.id.header);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        cart_items.setVisibility(View.GONE);
        description = findViewById(R.id.description);
        loading = findViewById(R.id.loading);
        error_network = findViewById(R.id.error_network);
        LinearLayout retry = findViewById(R.id.retry);
        fullayout = findViewById(R.id.fullayout);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        Bundle bundle = getIntent().getExtras();

        pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/Heebo-Regular.ttf\")}body,li,p,span {font-family: MyFont;font-size: 14px;text-align: justify;color: #415163}</style></head><body>";
        pas = "</body></html>";

        if (bundle != null) {

            title = bundle.getString("title");
            id = bundle.getInt("id");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            header.setText(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
        } else {
            header.setText(Html.fromHtml(title));
        }

        AboutTask productTask = new AboutTask();
        productTask.execute(Appconstatants.ABOUT_IN + id);

        bundle = new Bundle();

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_network.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                AboutTask productTask = new AboutTask();
                productTask.execute(Appconstatants.ABOUT_IN + id);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });


    }

    private class AboutTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {

            logger.info("Product list search api" + param[0]);

            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Webactivity.this);
                logger.info("Product list search resp" + response);
                Log.d("list_products", param[0]);
                Log.d("list_products", Appconstatants.sessiondata);


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "SingleProducts--->  " + resp);
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {

                        JSONObject jsonObject = new JSONObject(json.getString("data"));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            header.setText(Html.fromHtml(jsonObject.isNull("title") ? "About App" : jsonObject.getString("title"), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            header.setText(Html.fromHtml(jsonObject.isNull("title") ? "About App" : jsonObject.getString("title")));
                        }

                        String myHtmlString = pish + (jsonObject.isNull("description") ? "" : jsonObject.getString("description")) + pas;
                        Log.i("string", myHtmlString);

                        description.loadDataWithBaseURL(null, myHtmlString, "text/html", "utf-8", null);
                        description.setBackgroundColor(getResources().getColor(R.color.white));

                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);

                    } else {
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);

                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        String error = array.getString(0) + "";
                        errortxt2.setText(error);

                        Toast.makeText(Webactivity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    AboutTask productTask = new AboutTask();
                                    productTask.execute(Appconstatants.ABOUT_IN + id);
                                }
                            })
                            .show();
                }
            } else {
                error_network.setVisibility(View.VISIBLE);
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                loading.setVisibility(View.GONE);

            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
