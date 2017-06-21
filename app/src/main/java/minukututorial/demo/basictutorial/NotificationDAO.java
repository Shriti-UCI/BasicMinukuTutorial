package minukututorial.demo.basictutorial;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minukucore.dao.DAO;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.user.User;

/**
 * Created by shriti on 6/13/17.
 */

public class NotificationDAO implements DAO<ShowNotificationEvent> {

    public static String TAG = "NotificationDAO";
    private LinkedList<ShowNotificationEvent> notificationList;

    public NotificationDAO() {
        this.notificationList = new LinkedList<>();
    }

    @Override
    public void setDevice(User user, UUID uuid) {

    }

    @Override
    public void add(ShowNotificationEvent showNotificationEvent) throws DAOException {
        Log.d(this.TAG, "Adding notification.");
        notificationList.add(showNotificationEvent);
    }

    @Override
    public void delete(ShowNotificationEvent showNotificationEvent) throws DAOException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public Future<List<ShowNotificationEvent>> getAll() throws DAOException {
        throw new DAOException();
    }

    @Override
    public Future<List<ShowNotificationEvent>> getLast(int i) throws DAOException {
        throw new DAOException();
    }

    @Override
    public void update(ShowNotificationEvent showNotificationEvent, ShowNotificationEvent t1) throws DAOException {
        throw new RuntimeException("Not Implemented");
    }
}
