package com.example.petfoodstore_app.activities.Map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.petfoodstore_app.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap gMap;
    private FrameLayout map;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng currentLocation;
    private LatLng predefinedLocation;
    private TextView distanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        map = findViewById(R.id.map);
        distanceTextView = findViewById(R.id.distance_text);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Khởi tạo predefinedLocation ngay tại onCreate để đảm bảo không bị null
        predefinedLocation = new LatLng(10.841216, 106.809838);

        if (savedInstanceState == null) {
            SupportMapFragment mapFragment = new SupportMapFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map, mapFragment)
                    .commit();
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setMapToolbarEnabled(false);

        // Đảm bảo predefinedLocation đã được khởi tạo trước đó
        gMap.addMarker(new MarkerOptions()
                .position(predefinedLocation)
                .title("Petshop")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        gMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                if (currentLocation != null && marker.getTitle().equals("Petshop")) {
                    float[] distance = new float[1];
                    Location.distanceBetween(
                            currentLocation.latitude, currentLocation.longitude,
                            predefinedLocation.latitude, predefinedLocation.longitude,
                            distance);
                    String distanceText = String.format("%.3f", distance[0] / 1000) + " km";

                    View view = LayoutInflater.from(MapActivity.this).inflate(R.layout.custom_info_window, null);
                    TextView titleTextView = view.findViewById(R.id.title);
                    TextView distanceTextView = view.findViewById(R.id.distance);

                    titleTextView.setText("Petshop");
                    distanceTextView.setText("Khoảng cách: " + distanceText);

                    return view;
                }
                return null;
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        float[] distance = new float[1];
                        Location.distanceBetween(
                                currentLocation.latitude, currentLocation.longitude,
                                predefinedLocation.latitude, predefinedLocation.longitude,
                                distance);
                        String distanceText = "Khoảng cách: " + String.format("%.3f", distance[0] / 1000) + " km";
                        distanceTextView.setText(distanceText);

                        LatLngBounds bounds = new LatLngBounds.Builder()
                                .include(currentLocation)
                                .include(predefinedLocation)
                                .build();
                        gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                    } else {
                        Toast.makeText(this, "Không thể lấy vị trí hiện tại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi lấy vị trí: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    gMap.setMyLocationEnabled(true);
                    getCurrentLocation();
                }
            } else {
                Toast.makeText(this, "Cần quyền vị trí để hiển thị vị trí hiện tại",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}