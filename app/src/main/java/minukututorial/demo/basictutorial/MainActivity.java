package minukututorial.demo.basictutorial;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuNotificationManager;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minuku.streamgenerator.LocationStreamGenerator;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;

public class MainActivity extends AppCompatActivity {

    TextView latitude;
    TextView longitude;
    LocationDataRecord currentLocation;
    public static final String TAG = "MainActivity";
    private static final int READ_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the app name as it is being used by MinukuExtended
        if (!Constants.getInstance().hasAppNameBeenSet()) {
            Log.d(TAG, "updating app name if not set already");
            Constants.getInstance().setAppName(getString(R.string.app_name));
        }

        /**
         * This code is not needed if you are not going to use Firebase.
         * I have still put this code here to demonstrate that user configurations can be
         * stored in UserPreferences.
         * When using Firebase, we would need to set user email in UserPreferences.
         */
        if (UserPreferences.getInstance().getPreference(Constants.ID_SHAREDPREF_EMAIL) == null) {
            Log.d(TAG, "Setting user email");
            String email = "coolluke@gmail.com";
            UserPreferences.getInstance().writePreference(Constants.ID_SHAREDPREF_EMAIL, email);
            UserPreferences.getInstance().writePreference(Constants.KEY_ENCODED_EMAIL,
                    encodeEmail(email));
        }


        Log.d(TAG, "MainActivity onCreate called.");
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity initalized.");

        //this service is needed to update the streams according to their update frequency
        startService(new Intent(getBaseContext(), BackgroundService.class));
        Log.d(TAG, "Background service started.");

        //this service is needed to manage the notifications
        startService(new Intent(getBaseContext(), MinukuNotificationManager.class));
        Log.d(TAG, "MinukuNotificationManager started.");

        //get the text views that would hold the coordinate values
        latitude = (TextView) findViewById(R.id.current_latitude);
        longitude = (TextView) findViewById(R.id.current_longitude);

        Log.d(TAG, "initializing Minuku components");
        initialize();
    }

    /**
     * This method takes care of initializing Minuku components.
     * LocationChangeSituation, LocationChangeAction
     * MinukuDAOManager, LocationDAO, NotificationDAO
     * LocationStreamGenerator
     */
    private void initialize() {
        Log.d(TAG, "Getting MinukuDAOManager");
        MinukuDAOManager daoManager = MinukuDAOManager.getInstance();

        //For location, create location data record DAO and register it with the DAO manager
        Log.d(TAG, "Creating location DAO and registering it with MinukuDAOManager");
        LocationDAO locationDAO = new LocationDAO();
        daoManager.registerDaoFor(LocationDataRecord.class, locationDAO);

        //For notifications, create notification data record DAO and register it with the DAO manager
        Log.d(TAG, "Creating notification DAO and registering it with MinukuDAOManager");
        NotificationDAO notificationDAO = new NotificationDAO();
        daoManager.registerDaoFor(ShowNotificationEvent.class, notificationDAO);

        //initialize the stream generator so that it gets registered with the StreamManager
        Log.d(TAG, "Creating and initializing the location stream generator");
        LocationStreamGenerator locationStreamGenerator =
                new LocationStreamGenerator(getApplicationContext());

        Log.d(TAG, "Creating and Initializing the situation so that it can register with the situation manager");
        LocationChangeSituation locationChangeSituation = new LocationChangeSituation();

        Log.d(TAG, "Creating and Initializing the action so that it can be invoked when situation is true.");
        LocationChangeAction locationChangeAction = new LocationChangeAction();
    }

    public static final String encodeEmail(String unencodedEmail) {
        if (unencodedEmail == null) return null;
        return unencodedEmail.replace(".", ",");
    }

    /**
     * It gets the current location by accessing the current value in the location stream
     * For this, it asks the StreamManager for the location stream and then calls a getter
     * for current value in location stream. If location stream does not exist, it throws
     * an exception.
     * It sets the value of current location's latitude and longitude in the main view
     */
    private void refreshLocation(){
        try {
            Log.d(TAG, "Trying to get current location.");
            currentLocation =
                    MinukuStreamManager.getInstance().getStreamFor(LocationDataRecord.class).getCurrentValue();
        } catch (StreamNotFoundException e) {
            e.printStackTrace();
        }

        if(currentLocation!= null) {
            Log.d(TAG, "Updating UI with current location.");
            latitude.setText(String.valueOf(currentLocation.getLatitude()));
            longitude.setText(String.valueOf(currentLocation.getLongitude()));
        }
        else {
            Log.d(TAG, "Current location is null.");
            latitude.setText("unknown");
            longitude.setText("unknown");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "activity resuming now");
        /*
        Check for permission to access location.
        If permission has not already been granted, ask for permission
        If permission has been granted, refresh location every time the
        activity resumes.
         */
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    READ_LOCATION);
        } else {
            refreshLocation();
        }
    }

    /*
    A listener for permissions
    If permission has already been granted by the user, refresh location
    Standard code from android developer documentation
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case READ_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    refreshLocation();
                } else {
                    finish();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
