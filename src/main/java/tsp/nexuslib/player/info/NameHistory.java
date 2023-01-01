package tsp.nexuslib.player.info;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public record NameHistory(UUID uuid, Map<String, Long> history) {

    public Date getFormatted(String name) {
        return new Date(history.get(name));
    }

    public long getTimestamp(String name) {
        return history.get(name);
    }

}
