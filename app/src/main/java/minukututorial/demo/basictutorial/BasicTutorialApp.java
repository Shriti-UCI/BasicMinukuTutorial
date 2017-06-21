package minukututorial.demo.basictutorial;

import android.*;
import android.app.Application;
import android.util.Log;

import com.bugfender.sdk.Bugfender;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;

/**
 * Created by shriti on 6/12/17.
 * https://guides.codepath.com/android/Understanding-the-Android-Application-Class
 * Read above what the android application does.
 * This is the first entity that runs before
 * anything else happens at the time when the
 * application process starts.
 */

public class BasicTutorialApp extends Application {

    @Override
    public void onCreate() {
        Log.d("BasicTutorialApp", "in app on create");
        super.onCreate();

        //Initialize the user preferences where things related to the user can be configured.
        UserPreferences.getInstance().Initialize(getApplicationContext());

        //For this application instance, set the app name which will be used by the notification manager
        //The app name is first written as a part of the UserPreference and then made a parameter in Constants
        //that can be set only once.
        if (UserPreferences.getInstance().getPreference("APP_NAME") == null) {
            Log.d("BasicTutorialApp", "Setting app name  in preferences.");
            UserPreferences.getInstance().writePreference("APP_NAME", getString(R.string.app_name));
            Constants.getInstance().setAppName(UserPreferences.getInstance().getPreference("APP_NAME"));
        }
        //Logging and log monitoring mechanism.
        //https://bugfender.com/
        //Can get rid of this later, minuku extended needs to be changed
        Bugfender.init(this, "N7pdXEGbmKhK9k8YtpFPyXORtsAwgZa5", false);
        Bugfender.setForceEnabled(true);
    }
}
