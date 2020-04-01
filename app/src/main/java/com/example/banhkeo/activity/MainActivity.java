package com.example.banhkeo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.banhkeo.R;
import com.example.banhkeo.adapter.LoaispAdapter;
import com.example.banhkeo.adapter.SanphamAdapter;
import com.example.banhkeo.model.GioHang;
import com.example.banhkeo.model.Loaisp;
import com.example.banhkeo.model.Sanpham;
import com.example.banhkeo.ultil.CheckConnection;
import com.example.banhkeo.ultil.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<GioHang> manggiohang;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    ArrayList<Loaisp> mangloaisanpham;
    LoaispAdapter loaispAdapter;
    ArrayList<Sanpham> mangsanpham;
    SanphamAdapter sanphamAdapter;
    ProgressDialog pDialog;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Đang xử lý..");
        pDialog.setCanceledOnTouchOutside(false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        viewFlipper = findViewById(R.id.viewflipper);
        recyclerView = findViewById(R.id.recyclerview);
        mangloaisanpham = new ArrayList<>();
        loaispAdapter = new LoaispAdapter(mangloaisanpham, getApplicationContext());
        mangsanpham = new ArrayList<>();
        sanphamAdapter = new SanphamAdapter(getApplicationContext(), mangsanpham);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(sanphamAdapter);
        if (manggiohang != null) {

        } else {
            manggiohang = new ArrayList<>();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);


        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_info, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        GetDuLieuSanPhamMoiNhat();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {

            ActionViewFlipper();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), com.example.banhkeo.activity.GioHang.class);
                    startActivity(intent);
                }
            });
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    int id = menuItem.getItemId();
                    if (id == R.id.nav_banh) {
                        Intent intent = new Intent(MainActivity.this, BanhActivity.class);
                        //intent.putExtra("idloaisanpham",mangloaisanpham.get(1).getId());
                        startActivity(intent);
                    } else if (id == R.id.nav_keo) {
                        Intent intent = new Intent(MainActivity.this, KeoActivity.class);
                        //intent.putExtra("idloaisanpham",mangloaisanpham.get(2).getId());
                        startActivity(intent);
                    } else if (id == R.id.nav_info) {
                        Intent intent = new Intent(MainActivity.this, ThongTinLienHe.class);
                        startActivity(intent);
                    } else if (id == R.id.nav_exit) {
                        finish();
                    }
                    DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;

                }
            });

        } else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Check your Internet connection!!");
            finish();
        }
    }


    private void GetDuLieuSanPhamMoiNhat() {
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.Duongdansanphammoinhat, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    int ID = 0;
                    String Tensanpham = "";
                    Integer Giasanpham = 0;
                    String Hinhanhsanpham = "";
                    String Motasanpham = "";
                    int IDsanpham = 0;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ID = jsonObject.getInt("id");
                            Tensanpham = jsonObject.getString("tensp");
                            Giasanpham = jsonObject.getInt("giasp");
                            Hinhanhsanpham = jsonObject.getString("hinhanhsp");
                            Motasanpham = jsonObject.getString("motasp");
                            IDsanpham = jsonObject.getInt("idsanpham");
                            mangsanpham.add(new Sanpham(ID, Tensanpham, Giasanpham, Hinhanhsanpham, Motasanpham, IDsanpham));
                            sanphamAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                //CheckConnection.ShowToast_Short(getApplicationContext(),error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void ActionViewFlipper() {
        ArrayList<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://banhkeogiare.com/wp-content/uploads/2019/10/66.jpg");
        mangquangcao.add("https://banhkeogiare.com/wp-content/uploads/2019/10/3-5-600x600.jpg");
        mangquangcao.add("https://banhkeogiare.com/wp-content/uploads/2019/10/18.jpg");
        mangquangcao.add("https://banhkeogiare.com/wp-content/uploads/2019/10/1-7.jpg");
        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.get().load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(10000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
        Button btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        });
        Button btnPrevious = (Button) findViewById(R.id.btn_prev);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewFlipper.showPrevious();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
