package com.example.affluence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MapsFragment extends Fragment implements LocationListener {

    private GoogleMap mMap;
    private LatLng position, otherPositions;
    private Marker lastPosition, lastOtherPositions;
    private MarkerOptions markerOptions, otherMarkersOptions;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("Marker");
    private myService service;
    private myMarker marker;
    private Intent intent;
    private Bundle extras;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                marker = new myMarker(location.getLatitude(),location.getLongitude());
                reference.child(marker.getId()).setValue(marker);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (!ds.child("id").equals(marker.getId())) {
                        /*
                        if (lastOtherPositions != null) {
                            lastOtherPositions.remove();
                        }
                        */
                        otherPositions = new LatLng(ds.child("latitude").getValue(double.class),ds.child("longitude").getValue(double.class));
                        otherMarkersOptions = new MarkerOptions();
                        otherMarkersOptions.position(otherPositions);
                        otherMarkersOptions.title(ds.child("id").getValue(String.class));
                        otherMarkersOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        lastOtherPositions = mMap.addMarker(otherMarkersOptions);
                    }
                }
                // value = dataSnapshot.getValue(String.class);
                Toast.makeText(getActivity().getApplicationContext(), String.valueOf(dataSnapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity().getApplicationContext(), "Failed to read positions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        position = new LatLng(location.getLatitude(),location.getLongitude());
        marker.setLatitude(location.getLatitude());
        marker.setLongitude(location.getLongitude());
        intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("Marker", marker);
        getActivity().startService(new Intent(getActivity(),myService.class));
        if (lastPosition != null) {
            lastPosition.remove();
        }
        // put the marker
        markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        lastPosition = mMap.addMarker(markerOptions);
        // move the marker
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,11));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //
    }

    @Override
    public void onProviderEnabled(String provider) {
        //
    }

    @Override
    public void onProviderDisabled(String provider) {
        //
    }

    @Override
    public void onDestroyView() {
        reference.child(marker.getId()).removeValue();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        reference.child(marker.getId()).removeValue();
        super.onDestroy();
    }
}
