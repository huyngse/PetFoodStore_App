package com.example.petfoodstore_app.activities.Map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.petfoodstore_app.R;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap gMap;
    private FrameLayout map;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng currentLocation;
    private LatLng predefinedLocation;
    private TextView distanceTextView;
    private Polyline currentPolyline;
    private FloatingActionButton fabDirections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        map = findViewById(R.id.map);
        distanceTextView = findViewById(R.id.distance_text);
        fabDirections = findViewById(R.id.fab_directions);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Khởi tạo predefinedLocation ngay tại onCreate để đảm bảo không bị null
        predefinedLocation = new LatLng(10.841216, 106.809838);

        fabDirections.setOnClickListener(v -> {
            if (currentLocation == null) {
                Toast.makeText(this, "Không tìm thấy vị trí hiện tại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (predefinedLocation == null) {
                Toast.makeText(this, "Không tìm thấy vị trí Petshop!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Xóa đường đi cũ nếu có
            if (currentPolyline != null) {
                currentPolyline.remove();
            }
            // Vẽ đường đi mới
            String url = getDirectionsUrl(currentLocation, predefinedLocation);
            new FetchUrlTask().execute(url);
            Toast.makeText(this, "Đang tìm đường đi ngắn nhất...", Toast.LENGTH_SHORT).show();
        });

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

        gMap.setOnInfoWindowClickListener(marker -> {
            if (marker.getTitle().equals("Petshop")) {
                if (currentLocation != null) {
                    if (currentPolyline != null) {
                        currentPolyline.remove();
                    }
                    String url = getDirectionsUrl(currentLocation, predefinedLocation);
                    new FetchUrlTask().execute(url);
                    Toast.makeText(MapActivity.this, "Đang tìm đường đi ngắn nhất...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MapActivity.this, "Không tìm thấy vị trí hiện tại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
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

                        // Vẽ đường đi ngay khi lấy được vị trí
                        String url = getDirectionsUrl(currentLocation, predefinedLocation);
                        new FetchUrlTask().execute(url);
                    } else {
                        Toast.makeText(this, "Không thể lấy vị trí hiện tại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi lấy vị trí: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        if (origin == null || dest == null) {
            return null;
        }
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=driving";
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    private class FetchUrlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            if (url[0] == null) {
                return null;
            }
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null || result.startsWith("Error")) {
                Toast.makeText(MapActivity.this, "Không thể lấy dữ liệu đường đi: " + result, Toast.LENGTH_LONG).show();
                return;
            }
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            throw new IOException("Lỗi khi tải dữ liệu: " + e.getMessage());
        } finally {
            if (iStream != null) iStream.close();
            if (urlConnection != null) urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        private String durationText = "";
        private String distanceText = "";

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);

                JSONObject route = jObject.getJSONArray("routes").getJSONObject(0);
                JSONObject leg = route.getJSONArray("legs").getJSONObject(0);
                durationText = leg.getJSONObject("duration").getString("text");
                distanceText = leg.getJSONObject("distance").getString("text");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            if (result == null) {
                Toast.makeText(MapActivity.this, "Không thể phân tích dữ liệu đường đi!", Toast.LENGTH_LONG).show();
                return;
            }

            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.parseColor("#FF6200EE"));
                lineOptions.geodesic(true);
            }

            if (lineOptions != null) {
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }
                currentPolyline = gMap.addPolyline(lineOptions);
                distanceTextView.setText("Khoảng cách: " + distanceText + "\nThời gian: " + durationText);
            } else {
                Toast.makeText(MapActivity.this, "Không tìm thấy đường đi!", Toast.LENGTH_LONG).show();
            }
        }
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