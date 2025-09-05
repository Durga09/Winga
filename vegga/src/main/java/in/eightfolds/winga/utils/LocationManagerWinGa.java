package in.eightfolds.winga.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;


import in.eightfolds.utils.EightfoldsUtils;

public class LocationManagerWinGa implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnSuccessListener<Location> {


    private boolean isMakingRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private android.location.LocationManager manager;
    private String bestProvider;
    private Location lastKnownLocation;
    public ProgressDialog progressDialog;
    private Context context;

    public void initLocationPermission(Context context) {
        this.context = context;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
           // mFusedLocationClient.getLastLocation().addOnSuccessListener((Activity) context, LocationManagerWinGa.class);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            mFusedLocationClient.requestLocationUpdates(createLocationRequest(), mLocationCallback, null);
            initMyCurrentLocation(context);
        } else {
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    EightfoldsUtils.getInstance().PERMISSIONS_LOCATION,
                    EightfoldsUtils.getInstance().REQUEST_EXTERNAL_LOCATION
            );
        }
    }

    public void initGoogleObject(Context context) {
        this.context = context;
        new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        new GoogleApiClient
                .Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage((FragmentActivity) context, this)
                .build();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        manager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        initProvider();
    }

    private void initProvider() {
        Criteria mCriteria = new Criteria();
        mCriteria.setPowerRequirement(Criteria.POWER_LOW);
        mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        bestProvider = manager.getBestProvider(mCriteria, true);
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    protected LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    Logg.d("", "");
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    initMyCurrentLocation(context);
                    lastKnownLocation = location;
                }
            }
        }
    };

    public void initMyCurrentLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
          //  onSuccess(lastKnownLocation);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onSuccess(Location location) {

    }
}
