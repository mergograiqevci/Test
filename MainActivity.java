package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


/*
	// Osht ka mvyn ni request me qu npastebin me marr ni "text" sikurse database me perdor sa mos me ble root(server) sa per do testime qe jom tu i bo
	// e psh kur te bojm update npastebin osht ka mvyn memu bo update edhe ktu nlinka , qeky kodi qetu osht njavafx osht ka mbohet refresh e krejt ama kur pe qes nandroid studio
	// sosht qe po di mire me kthy , e provova edhe me URL masnej me scanner (url.openStream()) ama prap sboni...
	//pytje osht te funksioni sendRequest();
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(uri))
          .build();

    HttpResponse<String> response =
          client.send(request, BodyHandlers.ofString());
    String message=response.body();
		*/


public class MainActivity extends AppCompatActivity {
    ProgressDialog pg;
    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;
    TextView txt;
    String linku;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt=(TextView)findViewById(R.id.txt);

        try {
            videoPl(getLink());
           // txt.setText(getLink());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void videoPl(String videopath)
    {
        sendRequest();
        VideoView videoView=(VideoView)findViewById(R.id.videoView);
        //"https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4" test
        //String videopath= null;
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.pg = progressDialog;
        progressDialog.setMessage("Please Wait...");
        this.pg.show();
        Uri parse = Uri.parse(videopath);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(parse);
        videoView.requestFocus();
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                MainActivity.this.pg.dismiss();
            }
        });
    }
 //qekjo volley ish ni library e gatshme qe po mujshe me bo Requesta 
 // ama kur po boj update spom ndryshohet linku ktu 
 //
 //pytja osht qe a ki najfar ideja ose naj librari tjeter qe mun mi lexon qeto update kur ndryshoj najsen nqet pastebin psh tek url[0]...
 //
    public void sendRequest() {
        final String[] url = {""};
        new Thread(new Runnable() {
            public void run() {
                try {
                    url[0] = "https://pastebin.com/raw/AA6YN1ud";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        final String[] abc = {""};
        mRequestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.GET, url[0], new Response.Listener<String>()
			{
				@Override
				public void onResponse(final String response)
				{

					try
					{
						shkruaj(response.toString()); //Me filewriter,printwriter se spo muj me kthy qet funksion nString 
					} 
					catch (IOException e)
					{
						e.printStackTrace();
					}


				}
			},
			new Response.ErrorListener()
			{
				@Override
				public void onErrorResponse(VolleyError error) {

				}
			});
        mRequestQueue.add(stringRequest);
    }


    public void shkruaj(String s) throws IOException {
       FileOutputStream fos=null;
       try
       {
           fos=openFileOutput("forlink.txt",MODE_PRIVATE);
           fos.write(s.getBytes());
       } catch (IOException e) {
           e.printStackTrace();
       }
       finally
       {
           if(fos!=null)
               fos.close();
       }
    }
    public String getLink() throws Exception {
        sendRequest();

        FileInputStream fis=null;
        try
        {
            fis=openFileInput("forlink.txt");
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader br=new BufferedReader(isr);
            StringBuilder sb=new StringBuilder();
            String text;
            while((text=br.readLine())!=null)
            {
                sb.append(text).append("\n");
            }
            return sb.toString();
        }
		catch (IOException e)
		{
            throw new Exception("Error Loading Link");
        }
        finally
		{
            if(fis!=null)
            {
                fis.close();
            }
        }
    }

    public void onBtnClick(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }
}


