package com.example.epomeroy.shutterdroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.epomeroy.shutterdroid.shutterstock.ShutterData;
import com.example.epomeroy.shutterdroid.shutterstock.ShutterStock;
import com.example.epomeroy.shutterdroid.shutterstock.ShutterStockService;
import com.example.epomeroy.shutterdroid.shutterstock.ShutterStockWebServiceResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    List<ShutterData> shutterDataList;
    private ImagesAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = (RecyclerView) findViewById(R.id.image_view);
        rv.setLayoutManager(new GridLayoutManager(this, 2));

        rv.addItemDecoration(new GridSpacingDecoration());

        shutterDataList = new ArrayList<>();
        imageAdapter = new ImagesAdapter(this, shutterDataList);

        rv.setAdapter(imageAdapter);

        ShutterStockService service = ShutterStock.getService();
        service.getRecentImages(new Callback<ShutterStockWebServiceResponse>() {
            @Override
            public void success(ShutterStockWebServiceResponse shutterStockWebServiceResponse, Response response) {
                shutterDataList.clear();
                shutterDataList.addAll(shutterStockWebServiceResponse.getShutterDataList());
                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
