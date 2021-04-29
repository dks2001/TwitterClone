package com.dheerendrakumar.twitterclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("KcndIHWgi9znWUpFfpAlgmBXzKIQ0Fzn41vpjkfQ")
                // if defined
                .clientKey("ql4RhBNz359z3FtrhggtTYMLc956IGvLCW3Jfmnu")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}