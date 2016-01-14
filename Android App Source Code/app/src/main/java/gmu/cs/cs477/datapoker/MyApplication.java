package gmu.cs.cs477.datapoker;

import android.app.Application;

import com.parse.Parse;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        // Enable Local Datastore.
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "7kCjPWsQDWhufqt9s8STOk1RvMevpMWH6fjNn10p", "YRahLv8whSrn3DiJrQcY9Z6HnDSkK21jJpzVE1gz");

    }
}