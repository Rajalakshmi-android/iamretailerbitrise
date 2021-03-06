package com.iamretailer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import stutzen.co.network.Connection;


public class ChangePassword extends Language {

    private FrameLayout change_pwd;
    private EditText pass1;
    private EditText pass2;
    private DBController db;
    private FrameLayout fullayout;
    private AndroidLogger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        db = new DBController(ChangePassword.this);
        Appconstatants.sessiondata = db.getSession();
        Appconstatants.Lang = db.get_lang_code();
        Appconstatants.CUR = db.getCurCode();
        change_pwd = findViewById(R.id.change_pwd);
        pass1 = findViewById(R.id.pwd1);
        pass2 = findViewById(R.id.pwd2);
        fullayout = findViewById(R.id.fullayout);
        TextView login_page = findViewById(R.id.login_page);
        LinearLayout home = findViewById(R.id.home);

        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pass1.getText().toString().trim().length() == 0) {
                    pass1.setError(getResources().getString(R.string.pass_valid));
                }
                if (pass2.getText().toString().trim().length() == 0) {
                    pass2.setError(getResources().getString(R.string.pass_valid));
                }
                if (pass1.getText().toString().trim().length() <= 5) {
                    pass1.setError(getResources().getString(R.string.pass_res));
                }

                if (!(pass1.getText().toString().equals(pass2.getText().toString()))) {
                    Toast.makeText(ChangePassword.this, R.string.pwd_mis, Toast.LENGTH_LONG).show();
                }
                Log.i("Fhgfhfh",pass1.getText().toString().trim()+"  "+pass2.getText().toString().trim());

                if (pass1.getText().toString().trim().equals(pass2.getText().toString().trim()) && pass1.getText().toString().length() > 5 && pass2.getText().toString().length() > 5) {

                    CHANGE_Task change_task = new CHANGE_Task();
                    change_task.execute(pass1.getText().toString().trim(), pass2.getText().toString().trim());
                }

            }
        });
        login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


    }

    private class CHANGE_Task extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            Log.d("Login", "started");
            pDialog = new ProgressDialog(ChangePassword.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {
            logger.info("Change pwd api" + Appconstatants.CHANGE_PWD);

            String response;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("password", param[0]);
                json.put("confirm", param[1]);
                Log.d("change_p", json.toString());
                Log.d("session", db.getSession());
                logger.info("Change pwd api req" + json);
                response = connection.sendHttpPutjson1(Appconstatants.CHANGE_PWD, json, db.getSession(), Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, ChangePassword.this);
                logger.info("Change pwd api resp" + response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            if (pDialog != null)
                pDialog.dismiss();
            Log.d("login_ss", resp + "");
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Toast.makeText(ChangePassword.this, R.string.change_suc, Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(ChangePassword.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    change_pwd.performClick();
                                }
                            })
                            .show();
                }

            } else {
                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                change_pwd.performClick();
                            }
                        })
                        .show();
            }
        }
    }


}
