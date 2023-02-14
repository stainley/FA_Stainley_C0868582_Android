package com.stainley.fa.android.view;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stainley.fa.android.R;
import com.stainley.fa.android.databinding.ActivityMapsBinding;
import com.stainley.fa.android.model.Location;
import com.stainley.fa.android.model.Product;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Product productLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = binding.toolbar;
        setActionBar(toolbar);
        if (getActionBar() != null) getActionBar().setDisplayHomeAsUpEnabled(true);


        productLocation = (Product) getIntent().getSerializableExtra("product");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(13);
        LatLng toronto = new LatLng(43.6532, -79.3832);

        if (productLocation != null) {
            MarkerOptions productOptions = new MarkerOptions()
                    .title(productLocation.getName())
                    .position(new LatLng(productLocation.getLocation().getLatitude(), productLocation.getLocation().getLongitude()));

            Marker productMarker = mMap.addMarker(productOptions);
            productMarker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(productLocation.getLocation().getLatitude(), productLocation.getLocation().getLongitude())));
            return;
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(toronto));
    }
}