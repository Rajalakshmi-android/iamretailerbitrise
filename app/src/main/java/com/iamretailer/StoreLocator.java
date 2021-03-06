package com.iamretailer;

import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.logentries.android.AndroidLogger;
import com.iamretailer.POJO.OrdersPO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import stutzen.co.network.Connection;

public class StoreLocator extends FragmentActivity implements OnMapReadyCallback {



    private AndroidLogger logger;
    private String latitude,langtitude;
    private TextView cart_count;
    private ArrayList<OrdersPO> storelist;
    private ArrayList<OrdersPO> loactionlist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_locator);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maphome)).getMapAsync(this);
        LinearLayout backclick = findViewById(R.id.menu);
        TextView header = findViewById(R.id.header);
        header.setText(R.string.store);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        cart_count = findViewById(R.id.cart_count);
        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        DBController dbController = new DBController(StoreLocator.this);
        Appconstatants.sessiondata = dbController.getSession();
        Appconstatants.Lang = dbController.get_lang_code();
        Appconstatants.CUR = dbController.getCurCode();
        storelist = dbController.get_store_list();
        loactionlist=new ArrayList<>();
        if(storelist!=null&&storelist.size()>0){
            for(int i=0;i<storelist.size();i++){
                if(storelist.get(i).getGeocode()!=null&&!storelist.get(i).getGeocode().equalsIgnoreCase("")){
                    String val=storelist.get(i).getGeocode();
                    String[] separated = val.split(",");
                   OrdersPO bo= new OrdersPO();
                   bo.setLatitude(separated[0]);
                   bo.setLangtitude(separated[1]);
                   loactionlist.add(bo);

                }
            }

        }
        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StoreLocator.this, MyCart.class));
            }
        });
    }


    private Bitmap createBitmapFromLayoutWithText(Context context, int img) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = new LinearLayout(context);

        View dataView = mInflater.inflate(R.layout.marker_view, view, true);
        ImageView imge = dataView.findViewById(R.id.img);
        imge.setImageResource(img);
        view.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.draw(c);
        return bitmap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        for (int i = 0; i < loactionlist.size(); i++) {
            latitude=loactionlist.get(0).getLatitude();
            langtitude=loactionlist.get(0).getLangtitude();
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(loactionlist.get(i).getLatitude()), Double.parseDouble(loactionlist.get(i).getLangtitude())))
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(createBitmapFromLayoutWithText(StoreLocator.this, R.mipmap.rsz_destiny))));
        }



        if (googleMap != null) {
            LatLng location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(langtitude));
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            CameraPosition.Builder builder = CameraPosition.builder();
            builder.target(location);
            builder.zoom(9);
            CameraPosition cameraPosition = builder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.moveCamera(cameraUpdate);
            googleMap.setTrafficEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latitude), Double.parseDouble(langtitude)) , 9.0f));
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Uri gmmIntentUri = Uri.parse("geo:" + marker.getPosition().latitude + "," + marker.getPosition().longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

                return true;
            }
        });


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
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
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, StoreLocator.this);
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
                            cart_count.setText(String.valueOf(0));

                        } else if (dd instanceof JSONObject) {


                            JSONObject jsonObject = (JSONObject) dd;
                            JSONArray array = new JSONArray(jsonObject.getString("products"));

                            int qty = 0;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                            }
                            cart_count.setText(String.valueOf(qty));
                        }
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(StoreLocator.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }
    }

}
