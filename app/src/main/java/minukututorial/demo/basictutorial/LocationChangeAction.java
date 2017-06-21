package minukututorial.demo.basictutorial;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import edu.umich.si.inteco.minukucore.event.ShowNotificationEvent;
import edu.umich.si.inteco.minukucore.event.ShowNotificationEventBuilder;

/**
 * Created by shriti on 6/13/17.
 * This is the Action that is triggered
 * when an event of the type LocationChangeActionEvent
 * gets generated. This would happen when a Situation
 * returns true.
 */

public class LocationChangeAction {

    public LocationChangeAction() {
        EventBus.getDefault().register(this);
    }

    /**
     *
     * @param locationChangeActionEvent
     * handler for location change action event.
     * it posts a notification in response to the
     * situation that evaluated to be true.
     */
    @Subscribe
    public void handleLocationChangeEvent(LocationChangeActionEvent locationChangeActionEvent) {
        Log.d("LocationChangeAction", "Handling location change action event");
        EventBus.getDefault().post(
                new ShowNotificationEventBuilder()
                        .setExpirationAction(ShowNotificationEvent.ExpirationAction.DISMISS)
                        .setExpirationTimeSeconds(60)
                        .setViewToShow(MainActivity.class)
                        .setIconID(R.drawable.cast_ic_notification_small_icon)
                        .setTitle("Pinnngg!")
                        .setMessage("You haven't moved at all.")
                        .setCategory("")
                        .setParams(new HashMap<String, String>())
                        .createShowNotificationEvent()
        );
    }
}
