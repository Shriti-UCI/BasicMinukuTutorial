package minukututorial.demo.basictutorial;


import com.google.common.util.concurrent.SettableFuture;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by shriti on 6/12/17.
 */

public class LocationDAO implements DAO<LocationDataRecord> {

    private String TAG = "LocationDAO";
    private LinkedList<LocationDataRecord>locationList;

    public LocationDAO() {
        locationList = new LinkedList<>();
    }

    @Override
    public void setDevice(User user, UUID uuid) {

    }

    @Override
    public void add(LocationDataRecord locationDataRecord) throws DAOException {
        Log.d(this.TAG, "Adding location data record.");
        locationList.add(locationDataRecord);
    }

    @Override
    public void delete(LocationDataRecord locationDataRecord) throws DAOException {

    }

    @Override
    public Future<List<LocationDataRecord>> getAll() throws DAOException {
        final SettableFuture settableFuture = SettableFuture.create();
        settableFuture.set(locationList);
        return settableFuture;
    }

    @Override
    public Future<List<LocationDataRecord>> getLast(int i) throws DAOException {
        final SettableFuture settableFuture = SettableFuture.create();
        if (locationList != null) {
            settableFuture.set(locationList.subList(0, Math.min(locationList.size(), i)));
        } else {
            settableFuture.set(new LinkedList<>());
        }
        return settableFuture;
    }

    @Override
    public void update(LocationDataRecord locationDataRecord, LocationDataRecord t1) throws DAOException {

    }
}
