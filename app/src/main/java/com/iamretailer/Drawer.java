package com.iamretailer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.ScratchTextView;
import com.facebook.login.LoginManager;
import com.iamretailer.Adapter.AddressAdapter;
import com.iamretailer.Adapter.CurAdapter;
import com.iamretailer.Adapter.LangAdapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.LanguageList;
import com.iamretailer.Common.LocaleHelper;
import com.iamretailer.POJO.AddressPO;
import com.iamretailer.POJO.CurPO;
import com.iamretailer.POJO.LangPO;
import com.logentries.android.AndroidLogger;
import com.squareup.picasso.Picasso;
import com.iamretailer.POJO.OrdersPO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import stutzen.co.network.Connection;


public class Drawer extends Language {

    private LinearLayout myorders;
    TextView login;
    TextView email;
    DBController dbCon;
    private LinearLayout callus;
    private LinearLayout gorateus;
    private LinearLayout goshare;
    LinearLayout gologout;
    private LinearLayout home1;
    LinearLayout wish;
    private TextView cart_count1;
    LinearLayout change_pwd;
    private LinearLayout track_order;
    private LinearLayout my_profile;
    private AndroidLogger logger;
    LinearLayout address;
    private LinearLayout language;
    private ArrayList<LangPO> langs;
    private ArrayList<CurPO> curs;
    private LangAdapter langAdapter;
    private CurAdapter curAdapter;
    private int pos = 0;
    private LinearLayout wallet;
    private LinearLayout store;
    private LinearLayout aboutus;
    private LinearLayout currency;
    private LinearLayout helps;
    private LinearLayout returns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbCon = new DBController(Drawer.this);
        CommonFunctions.updateAndroidSecurityProvider(this);
        Appconstatants.sessiondata = dbCon.getSession();
        Appconstatants.Lang = dbCon.get_lang_code();
        Appconstatants.CUR = dbCon.getCurCode();
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);

    }

    public void drawerview(FrameLayout view, final DrawerLayout layout) {

        login = (TextView) view.findViewById(R.id.loginview);
        myorders = (LinearLayout) view.findViewById(R.id.myorders);
        callus = (LinearLayout) view.findViewById(R.id.callus);
        gorateus = (LinearLayout) view.findViewById(R.id.gorateus);
        goshare = (LinearLayout) view.findViewById(R.id.goshare);
        gologout = (LinearLayout) view.findViewById(R.id.gologout);
        home1 = (LinearLayout) view.findViewById(R.id.home);
        wish = (LinearLayout) view.findViewById(R.id.wish);
        email = (TextView) view.findViewById(R.id.user_mail);
        change_pwd = (LinearLayout) view.findViewById(R.id.change_pwd);
        cart_count1 = (TextView) view.findViewById(R.id.cart_count1);
        track_order = (LinearLayout) view.findViewById(R.id.track_order);
        my_profile = (LinearLayout) view.findViewById(R.id.my_profile);
        address = (LinearLayout) view.findViewById(R.id.address);
        helps = (LinearLayout) view.findViewById(R.id.help);
        language = (LinearLayout) findViewById(R.id.language);
        wallet = (LinearLayout) findViewById(R.id.wallet);
        store = (LinearLayout) view.findViewById(R.id.store);
        aboutus = (LinearLayout) view.findViewById(R.id.aboutus);
        currency = (LinearLayout) view.findViewById(R.id.currency);
        returns = (LinearLayout) view.findViewById(R.id.returns);
        TextView title = (TextView) view.findViewById(R.id.title);
        String s1 = getResources().getString(R.string.about);
        String s2 = getResources().getString(R.string.app_name);
        String s3 = s1 + " " + s2;
        title.setText(s3);
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
        if(Appconstatants.returns_menu.equalsIgnoreCase("1")){
            returns.setVisibility(View.VISIBLE);
        }else{
            returns.setVisibility(View.GONE);
        }
        if (Appconstatants.store_locator.equalsIgnoreCase("1") && dbCon.get_store_lists() > 0) {
            store.setVisibility(View.VISIBLE);
        } else {
            store.setVisibility(View.GONE);
        }
        if (dbCon.getLoginCount() > 0) {
            email.setVisibility(View.VISIBLE);
            email.setText(dbCon.getName());
            login.setVisibility(View.GONE);
            gologout.setVisibility(View.VISIBLE);
            change_pwd.setVisibility(View.VISIBLE);
            wish.setVisibility(View.VISIBLE);
            address.setVisibility(View.VISIBLE);
            //wallet.setVisibility(View.VISIBLE);
        } else {
            email.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            gologout.setVisibility(View.GONE);
            change_pwd.setVisibility(View.GONE);
            wish.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            //  wallet.setVisibility(View.GONE);
        }

        if (dbCon.get_lan_lists() > 1) {
            language.setVisibility(View.VISIBLE);
        } else {
            language.setVisibility(View.GONE);
        }
        if (dbCon.get_cur_count() > 1) {
            currency.setVisibility(View.VISIBLE);
        } else {
            currency.setVisibility(View.GONE);

        }
        setListener();


    }

    public void offerPopup(Context context) {
        CouponTask coupon = new CouponTask(context);
        coupon.execute();


    }
    public void offerPopupshow(Context context, String image, String coupon_code, String coupon_title, String amount) {

        android.app.AlertDialog.Builder dial = new android.app.AlertDialog.Builder(context);
        View popUpView = View.inflate(context,R.layout.offer_code_popup, null);
        ImageView imageView=popUpView.findViewById(R.id.close);
        TextView textView=popUpView.findViewById(R.id.copycode);
        TextView coupon_price=popUpView.findViewById(R.id.coupon_price);
        TextView amounts=popUpView.findViewById(R.id.amount);
        ImageView coupon_image=popUpView.findViewById(R.id.coupon_image);
        dial.setView(popUpView);
        final android.app.AlertDialog popupStore = dial.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popupStore.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        popupStore.getWindow().setAttributes(lp);
        popupStore.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupStore.show();
        final ScratchTextView scratchTextView = popUpView.findViewById(R.id.scratch);
        scratchTextView.setText(coupon_code+"");
        coupon_price.setText(coupon_title+"");
        amounts.setText("Coupon Maximum Order Amount is"+amount);
        if (image != null && image.length() != 0)
            Picasso.with(context).load(image).placeholder(R.mipmap.giftc).into(coupon_image);
        else
            Picasso.with(context).load(R.mipmap.giftc).placeholder(R.mipmap.giftc).into(coupon_image);
        scratchTextView.setRevealListener(new ScratchTextView.IRevealListener() {
            @Override
            public void onRevealed(ScratchTextView tv) {

            }

            @Override
            public void onRevealPercentChangedListener(ScratchTextView stv, float percent) {
                if (percent>=0.5) {
                    Log.d("Reveal Percentage", "onRevealPercentChangedListener: " + String.valueOf(percent));
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupStore.hide();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scratchTextView.isRevealed()==true){
                    Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();
                    // pushid=scratchTextView.getText().toString().trim();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("code", scratchTextView.getText().toString().trim());
                    clipboard.setPrimaryClip(clip);
                    Log.i("clip ",clip+" ");
                    //   System.out.print(clip.getItemAt(0));
                }else{
                    Toast.makeText(getApplicationContext(), "Scratch to reveal", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private class CouponTask extends AsyncTask<Object, Void, String> {
        Context cn;

        public CouponTask(Context context) {
            cn=context;
        }

        @Override
        protected void onPreExecute() {
            Log.d("coupon", "started");
        }

        protected String doInBackground(Object... param) {
            logger.info("coupon_api" + Appconstatants.COUPON);

            String response = null;
            Connection connection = new Connection();
            try {
                Log.d("coupon_api", Appconstatants.COUPON);
                Log.d("coupon_api", Appconstatants.sessiondata);

                response = connection.connStringResponse(Appconstatants.COUPON, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, cn);
                logger.info("coupon_resp" + response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("coupon", "coupon response  " + resp);
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success") == 1) {

                        JSONObject object = json.getJSONObject("data");
                        String image=json.getString("coupon_image");
                        String coupon_code=json.getString("coupon_code");
                        String coupon_title=json.getString("coupon_title");
                        String amount=json.getString("coupon_maximum_order_amount");
                        offerPopupshow(cn,image,coupon_code,coupon_title,amount);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            } else {

            }

        }
    }
    private class CartTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            Log.d("Cart_list", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Cart api:" + param[0]);

            String response = null;
            try {
                Connection connection = new Connection();
                Log.d("Cart_list_url", param[0]);
                Log.d("Cart_url_list", Appconstatants.sessiondata);
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Drawer.this);
                logger.info("Cart resp" + response);
                Log.d("Cart_list_resp", response + "");

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Cart_list_resp", "CartResp--->  " + resp);

            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Object dd = json.get("data");
                        if (dd instanceof JSONArray) {
                            cart_count1.setText(String.valueOf(0));

                        } else if (dd instanceof JSONObject) {


                            JSONObject jsonObject = (JSONObject) dd;
                            JSONArray array = new JSONArray(jsonObject.getString("products"));

                            int qty = 0;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                            }
                            cart_count1.setText(String.valueOf(qty));

                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }
    }


    private void setListener() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login.getText().toString().equalsIgnoreCase(getString(R.string.Log_reg))) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.putExtra("from", 1);
                    startActivity(intent);
                }

            }
        });
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Wallet.class));

            }
        });
        returns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReturnlistActivity.class));

            }
        });
        helps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HelpActivity.class));
            }
        });
        home1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), About_Activity.class));
            }
        });


        myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyOrders.class));
            }
        });
        wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WishList.class));
            }
        });


        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangePassword.class));
            }
        });
        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dbCon.getLoginCount() > 0) {

                    startActivity(new Intent(getApplicationContext(), MyProfile.class));
                } else {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.putExtra("from", 5);
                    startActivity(intent);
                }
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddressList.class);
                Bundle bundle = new Bundle();
                bundle.putInt("from", 4);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lang_popup();
            }
        });

        currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_popup();
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StoreLocator.class));
            }
        });


        callus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Drawer.this, ContactForm.class));


            }
        });

        gorateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.iamretailer"));
                startActivity(browserIntent);

            }
        });
        gologout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showLogoutPopup();
            }
        });
        goshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Download app at https://play.google.com/store/apps/details?id=com.iamretailer");
                startActivity(Intent.createChooser(intent, "Share Using"));
            }
        });


        track_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bb = new Intent(Drawer.this, MyCart.class);
                Bundle aa = new Bundle();
                aa.putInt("order", 2);
                bb.putExtras(aa);
                startActivity(bb);

            }
        });
    }

    private void lang_popup() {


        AlertDialog.Builder dial = new AlertDialog.Builder(Drawer.this);
        View popUpView = getLayoutInflater().inflate(R.layout.lang_popup, null);
        dial.setView(popUpView);
        final AlertDialog popupStore = dial.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popupStore.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        popupStore.getWindow().setAttributes(lp);
        popupStore.show();

        LinearLayout no = (LinearLayout) popUpView.findViewById(R.id.no);
        LinearLayout yes = (LinearLayout) popUpView.findViewById(R.id.yes);
        ListView lang_list = (ListView) popUpView.findViewById(R.id.lang_list);
        langs = dbCon.get_lan_list();
        langAdapter = new LangAdapter(Drawer.this, R.layout.lang_list, langs);
        lang_list.setAdapter(langAdapter);
        if (dbCon.get_lan_c() > 0) {
            for (int k = 0; k < langs.size(); k++) {
                if (langs.get(k).getLang_code().equals(dbCon.get_lang_code())) {
                    langs.get(k).setSelect_lang(true);
                }
            }

        }


        lang_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                for (int y = 0; y < langs.size(); y++) {
                    langs.get(y).setSelect_lang(false);
                }
                langs.get(position).setSelect_lang(true);
                langAdapter.notifyDataSetChanged();
            }
        });


        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupStore.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupStore.dismiss();
                dbCon.get_lang_code();
                dbCon.insert_app_lang(langs.get(pos).getLang_id(), langs.get(pos).getLang_name(), langs.get(pos).getLang_code());
                change_lang(dbCon.get_lang_code());


            }
        });
    }

    private void change_lang(String languageToLoad) {

        ArrayList<String> lang_list = LanguageList.getLang_list();
        String set_lan = "en";

        for (int h = 0; h < lang_list.size(); h++) {
            if (languageToLoad.contains(lang_list.get(h))) {
                set_lan = lang_list.get(h);

            }

        }
        LocaleHelper.setLocale(this, set_lan);
        Locale locale = new Locale(set_lan);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLayoutDirection(locale);
        }
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        startActivity(new Intent(Drawer.this, MainActivity.class));
    }


    private void showLogoutPopup() {
        AlertDialog.Builder dialLo = new AlertDialog.Builder(Drawer.this);
        View popUpView = getLayoutInflater().inflate(R.layout.logout_view, (ViewGroup) null, false);
        LinearLayout happy = (LinearLayout) popUpView.findViewById(R.id.happy);
        LinearLayout bad = (LinearLayout) popUpView.findViewById(R.id.bad);
        dialLo.setView(popUpView);
        final AlertDialog dialog = dialLo.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCancelable(true);
        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, getResources().getDisplayMetrics());
        int px1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = px;
        lp.height = px1;
        dialog.getWindow().setAttributes(lp);
        happy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                LogoutTask task = new LogoutTask();
                task.execute(Appconstatants.LOGOUT_URL);
            }
        });
        bad.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private class LogoutTask extends AsyncTask<String, Void, String> {


        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Drawer.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("confirm_order", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Logout api" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.sendHttpPostLogout(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR);
                logger.info("Logout api resp" + response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("confirm_order", "Logout--->  " + resp);
            if (pDialog != null)
                pDialog.dismiss();
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        dbCon.dropUser();
                        LoginManager.getInstance().logOut();
                        Toast.makeText(Drawer.this, R.string.log_suc, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Drawer.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(Drawer.this, R.string.log_fail, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Drawer.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    Toast.makeText(Drawer.this, R.string.log_fail, Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(Drawer.this, R.string.error_net, Toast.LENGTH_SHORT).show();
                Toast.makeText(Drawer.this, R.string.log_fail, Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 212: {
                Log.i("reeeeeee", grantResults.length + "==" + grantResults[0]);
                if ((grantResults.length == 1) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("reekkk", "inside");
                } else {
                    Log.i("reeaaaaaa", "inside");
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.per_enable),
                            Snackbar.LENGTH_INDEFINITE).setAction(getResources().getString(R.string.enable),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(intent);
                                }
                            }).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dbCon.getLoginCount() > 0) {
            email.setText(dbCon.getName());
            email.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            gologout.setVisibility(View.VISIBLE);
            change_pwd.setVisibility(View.VISIBLE);
            wish.setVisibility(View.VISIBLE);
            address.setVisibility(View.VISIBLE);
            //wallet.setVisibility(View.VISIBLE);
        } else {
            gologout.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            email.setVisibility(View.GONE);
            change_pwd.setVisibility(View.GONE);
            wish.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            // wallet.setVisibility(View.GONE);
        }
    }

    private void cur_popup() {


        AlertDialog.Builder dial = new AlertDialog.Builder(Drawer.this);
        View popUpView = getLayoutInflater().inflate(R.layout.cur_popup, null);
        dial.setView(popUpView);
        final AlertDialog popupStore = dial.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popupStore.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        popupStore.getWindow().setAttributes(lp);
        popupStore.show();

        LinearLayout no = (LinearLayout) popUpView.findViewById(R.id.no);
        LinearLayout yes = (LinearLayout) popUpView.findViewById(R.id.yes);
        ListView cur_list = (ListView) popUpView.findViewById(R.id.cur_list);
        curs = dbCon.get_cur_list();
        curAdapter = new CurAdapter(Drawer.this, R.layout.lang_list, curs);
        cur_list.setAdapter(curAdapter);
        if (dbCon.get_cur_count() > 0) {
            for (int k = 0; k < curs.size(); k++) {
                if (curs.get(k).getCur_code().equals(dbCon.getCurCode())) {
                    curs.get(k).setIsselected(true);
                }
            }

        }


        cur_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                for (int y = 0; y < curs.size(); y++) {
                    curs.get(y).setIsselected(false);
                }
                curs.get(position).setIsselected(true);
                curAdapter.notifyDataSetChanged();
            }
        });


        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupStore.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popupStore.dismiss();
                dbCon.drop_app_cur();
                dbCon.insert_app_cur(curs.get(pos).getCur_title(), curs.get(pos).getCur_code(), curs.get(pos).getCur_left(), curs.get(pos).getCur_right());
                startActivity(new Intent(Drawer.this, MainActivity.class));
            }
        });
    }


}


