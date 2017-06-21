package minukututorial.demo.basictutorial;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.umich.si.inteco.minuku.manager.MinukuSituationManager;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minukucore.event.ActionEvent;
import edu.umich.si.inteco.minukucore.event.MinukuEvent;
import edu.umich.si.inteco.minukucore.event.StateChangeEvent;
import edu.umich.si.inteco.minukucore.exception.DataRecordTypeNotFound;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.model.StreamSnapshot;
import edu.umich.si.inteco.minukucore.situation.Situation;

/**
 * Created by shriti on 6/13/17.
 * A Situation which is evaluated whenever there
 * is a state change event on LocationDataRecord
 * StreamManager while updating the location stream
 * generates a StateChangeEvent when location is
 * updated. This event is used by LocationChangeSituation
 * as a trigger to evaluate the situation. If the situation
 * is true, this class generates a LocationChangeActionEvent,
 * which is handled by the LocationChangeAction.
 * So, when LocationChangeSituation is true, LocationChangeAction
 * is executed.
 */

public class LocationChangeSituation implements Situation {

    private String TAG = "LocationChangeSituation";
    public LocationChangeSituation() {
        try {
            MinukuSituationManager.getInstance().register(this);
            Log.d(TAG, "Registered situation LocationChangeSituation");
        } catch (DataRecordTypeNotFound dataRecordTypeNotFound) {
            Log.d(TAG, "Registration failed for LocationChangeSituation");
            dataRecordTypeNotFound.printStackTrace();
        }
    }

    /**
     *
     * @param streamSnapshot
     * @param minukuEvent
     * @param <T>
     * @return a type of ActionEvent
     * this method use an event to trigger situation evaluation
     * and returns the result of the evaluation as either
     * another event or null
     */
    @Override
    public <T extends ActionEvent> T assertSituation(StreamSnapshot streamSnapshot, MinukuEvent minukuEvent) {
        if(minukuEvent instanceof StateChangeEvent) {
            List<DataRecord> dataRecords = new ArrayList<>();
            Log.d(TAG, "LocationChangeSituation has received a state change event from main activity");
            if(shouldShowNotification(streamSnapshot)){
                return (T) new LocationChangeActionEvent("LOCATION_CHANGE_SITUATION", dataRecords);
            }
        }
        Log.d(TAG, "Situation is returning null. No notification will be generated");
        return null;    }

    /**
     *
     * @return a list of data record types
     * @throws DataRecordTypeNotFound
     * a situation needs to tell the situation manager
     * about the types of data records it is interested in.
     * Situation manager uses this information to call a situation
     * if a data record of interest has been updated/changed
     */
    @Override
    public List<Class<? extends DataRecord>> dependsOnDataRecordType() throws DataRecordTypeNotFound {
        List<Class<? extends  DataRecord>> dependsOn = new ArrayList<>();
        dependsOn.add(LocationDataRecord.class);
        return dependsOn;    }

    private boolean shouldShowNotification(StreamSnapshot streamSnapshot){
        LocationDataRecord currentLocationDataRecord = streamSnapshot.getCurrentValue(LocationDataRecord.class);
        LocationDataRecord previousLocationLocationDataRecord = streamSnapshot.getPreviousValue(LocationDataRecord.class);
        if(currentLocationDataRecord == null || previousLocationLocationDataRecord == null) {
            Log.d(TAG, "One of the locations is null");
            return false;
        }
        Location currentLocation = new Location("");
        currentLocation.setLatitude(currentLocationDataRecord.getLatitude());
        currentLocation.setLongitude(currentLocationDataRecord.getLongitude());

        Location previousLocation = new Location("");
        previousLocation.setLatitude(previousLocationLocationDataRecord.getLatitude());
        previousLocation.setLongitude(previousLocationLocationDataRecord.getLongitude());

        float distanceInMeters = currentLocation.distanceTo(previousLocation);
        Log.d(TAG, String.valueOf(distanceInMeters));
        if(distanceInMeters == 0.0) {
            Log.d(TAG, "distance between two locations is 0 meters. Returning true");
            return true;
        }
        else {
            Log.d(TAG, "distance between two locations is not 0 meters. Returning false");
            return false;
        }
    }
}
