package com.dheerendrakumar.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class SocialMediaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        ListView listView = findViewById(R.id.listview);
        arrayList = new ArrayList<>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(SocialMediaActivity.this, android.R.layout.simple_list_item_checked,arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);


        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    if(e == null) {
                        if (users.size() > 0) {
                            for(ParseUser user : users) {
                                arrayList.add(user.getUsername());
                            }
                            listView.setAdapter(arrayAdapter);

                            for(String twitterUser : arrayList) {
                                if(ParseUser.getCurrentUser().getList("fanOf") != null) {
                                    if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)) {
                                        listView.setItemChecked(arrayList.indexOf(twitterUser), true);
                                    }
                                }
                            }
                        }
                    }
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.logoutuserItem) {
            ParseUser.getCurrentUser().logOut();
            finish();
            Intent intent = new Intent(SocialMediaActivity.this,LogInActivity.class);
            startActivity(intent);
        } else if(item.getItemId() == R.id.sendTweet) {

            Intent intent = new Intent(SocialMediaActivity.this,ShareTweet.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView = (CheckedTextView) view;

        if(checkedTextView.isChecked()) {
            Toast.makeText(this, "Followed", Toast.LENGTH_SHORT).show();
            ParseUser.getCurrentUser().add("fanOf",arrayList.get(position));
        } else {
            Toast.makeText(this, "Unfollowed", Toast.LENGTH_SHORT).show();

            ParseUser.getCurrentUser().getList("fanOf").remove(arrayList.get(position));
            List currentUserFanList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",currentUserFanList);
        }


        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(SocialMediaActivity.this, "Done.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}