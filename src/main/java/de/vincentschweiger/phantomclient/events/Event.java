package de.vincentschweiger.phantomclient.events;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Event {

    public Event call() {
        ArrayList<EventData> data = EventManager.get(this.getClass());
        if (data != null) {
            for (EventData d : data) {
                try {
                    d.target.invoke(d.source, this);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }
}