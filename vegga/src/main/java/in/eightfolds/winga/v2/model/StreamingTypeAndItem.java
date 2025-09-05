package in.eightfolds.winga.v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamingTypeAndItem implements Serializable {

    public ArrayList<StreamingItem> streamingItems;
    public ArrayList<StreamingType> streamingTypes;

    public ArrayList<StreamingItem> getStreamingItems() {
        return streamingItems;
    }

    public void setStreamingItems(ArrayList<StreamingItem> streamingItems) {
        this.streamingItems = streamingItems;
    }

    public ArrayList<StreamingType> getStreamingTypes() {
        return streamingTypes;
    }

    public void setStreamingTypes(ArrayList<StreamingType> streamingTypes) {
        this.streamingTypes = streamingTypes;
    }
}
