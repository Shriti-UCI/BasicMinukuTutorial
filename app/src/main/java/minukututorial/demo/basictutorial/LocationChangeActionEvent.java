package minukututorial.demo.basictutorial;

import java.util.List;

import edu.umich.si.inteco.minukucore.event.ActionEvent;
import edu.umich.si.inteco.minukucore.model.DataRecord;

/**
 * Created by shriti on 6/13/17.
 * This is the event that is used to communicate
 * between Situation and Action
 * When Situation returns true, this is generated.
 */

public class LocationChangeActionEvent extends ActionEvent {

    public LocationChangeActionEvent(String typeOfEvent, List<DataRecord> dataRecords) {
        super(typeOfEvent, dataRecords);
    }
}
