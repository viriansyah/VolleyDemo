package com.example.volleydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<MainData> dataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);

        String url = "https://picsum.photos/v2/list";

        final ProgressDialog dialog = new ProgressDialog(this);

        dialog.setMessage("Please Wait A Moment ...");

        dialog.setCancelable(true);

        dialog.show();

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null){
                    dialog.dismiss();

                    try {
                        JSONArray jsonArray = new JSONArray(response);

                       parseArray (jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(request);
    }

    private void parseArray(JSONArray jsonArray) {
        for (int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject object = jsonArray.getJSONObject(i);

                MainData data = new MainData();

                data.setName(object.getString("author"));

                data.setImage(object.getString("download_url"));

                dataArrayList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return dataArrayList.size();
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = getLayoutInflater().inflate(R.layout.item_main, null);

                    MainData data = dataArrayList.get(position);

                    ImageView imageView = view.findViewById(R.id.image_view);
                    TextView textView = view.findViewById(R.id.text_view);

                    Glide.with(getApplicationContext())
                            .load(data.getImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);

                    textView.setText(data.getName());


                    return view;
                }
            });
        }
    }
}
