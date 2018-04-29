 package com.example.designmodal.newsfeed;

import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

 public class MainActivity extends AppCompatActivity {

    ListView lVRss;
     ArrayList<String> titles;
     ArrayList<String> Links;
     ArrayList<String> description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lVRss = (ListView) findViewById(R.id.Lv_feed);
        titles = new ArrayList<String>();
        Links = new ArrayList<String>();
        description = new ArrayList<String>();
        lVRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = String.valueOf(Uri.parse(Links.get(position)));
                Toast.makeText(MainActivity.this,url, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,second.class);
                intent.putExtra("link",url);
                startActivity(intent);

            }

        });
        new ProcessInBackground().execute();
    }

        public InputStream getInputStream(URL url)
     {
         try
         {
             return url.openConnection().getInputStream();
         }
         catch (IOException e)
         {
             return null;
         }
     }
     public class ProcessInBackground extends AsyncTask<Integer, Void , Exception>
     {
         ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
         Exception exception = null;
         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             progressDialog.setMessage("Busy Loading RSS feed......Please Wait");
             progressDialog.show();
         }

         @Override
         protected Exception doInBackground(Integer... params) {
             try{
                 URL url = new URL("https://yubapost.com/feed/");
                 XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                 factory.setNamespaceAware(false);
                 XmlPullParser xpp = factory.newPullParser();
                 xpp.setInput(getInputStream(url),"UTF_8");
                 boolean insideItem = false;
                 int eventType = xpp.getEventType();

                 while(eventType!=XmlPullParser.END_DOCUMENT)
                 {
                     if(eventType == XmlPullParser.START_TAG)
                     {
                         if(xpp.getName().equalsIgnoreCase("item"))
                         {
                             insideItem = true;
                         }
                         else if(xpp.getName().equalsIgnoreCase("title"))
                         {
                             if(insideItem)
                             {
                                 titles.add(xpp.nextText());
                             }
                         }
                         else if(xpp.getName().equalsIgnoreCase("description"))
                         {
                             if(insideItem)
                             {
                                 String desc = String.valueOf(Html.fromHtml(xpp.nextText()));
                                 description.add(desc);
                             }

                         }
                         else if(xpp.getName().equalsIgnoreCase("link"))
                         {
                             if(insideItem)
                             {
                                 Links.add(xpp.nextText());
                             }

                         }
                     }
                     else if(eventType ==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item"))
                     {
                         insideItem = false;

                     }
                     eventType = xpp.next();
                 }


             }
             catch (MalformedURLException e)
             {
                 exception = e;

             }
             catch (XmlPullParserException e)
             {
                 exception = e;
             }
             catch (IOException e)
             {
                 exception = e;
             }
             return exception;
         }

         @Override
         protected void onPostExecute(Exception s) {
             super.onPostExecute(s);
             ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,titles);
             ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,description);
             lVRss.setAdapter(adapter);
             lVRss.setAdapter(adapter1);
             progressDialog.dismiss();
         }
     }


}
