package com.dheerendrakumar.twitterclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShareTweet extends AppCompatActivity {

    EditText tweet;
    Button shareButton;
    Button viewTweet;
    ListView usersTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_tweet);

        tweet = findViewById(R.id.edtTweet);
        shareButton = findViewById(R.id.shareButton);
        viewTweet = findViewById(R.id.viewTweet);
        usersTweet = findViewById(R.id.othersTweetListview);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseObject parseObject = new ParseObject("MyTweet");
                parseObject.put("tweet",tweet.getText().toString());
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                ProgressDialog progressDialog = new ProgressDialog(ShareTweet.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null)  {
                            Toast.makeText(ShareTweet.this, "finished", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(ShareTweet.this, "try again", Toast.LENGTH_SHORT).show();
                        }
                        tweet.setText("");
                        progressDialog.dismiss();
                    }
                });

            }
        });


        viewTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<HashMap<String,String>> tweetList = new ArrayList<>();

                SimpleAdapter simpleAdapter = new SimpleAdapter(ShareTweet.this,tweetList, android.R.layout.simple_list_item_2,new String[] {"tweetUsername","tweetValue"},new int[] {android.R.id.text1,android.R.id.text2});
                try {

                    ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
                    parseQuery.whereContainedIn("username",ParseUser.getCurrentUser().getList("fanOf"));
                    parseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(objects.size() > 0 && e == null) {
                                for(ParseObject tweetObject: objects) {
                                    HashMap<String,String> userTweet = new HashMap<>();
                                    userTweet.put("tweetUsername",tweetObject.getString("username"));
                                    userTweet.put("tweetValue",tweetObject.getString("tweet"));
                                    tweetList.add(userTweet);
                                }
                                usersTweet.setAdapter(simpleAdapter);
                            }
                        }
                    });

                } catch(Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}