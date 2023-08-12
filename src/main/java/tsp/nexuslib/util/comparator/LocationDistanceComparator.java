package tsp.nexuslib.util.comparator;

import org.bukkit.Location;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class LocationDistanceComparator implements Comparator<Location> {

    private Location target;

    public LocationDistanceComparator(Location target) {
        this.target = target;
    }

    @Override
    public int compare(Location current, Location next) {
        return Double.compare(current.distanceSquared(target), next.distanceSquared(target));
    }

    public Location getTarget() {
        return target;
    }

    public void setTarget(Location target) {
        this.target = target;
    }

    public static List<Location> sort(Location target, Collection<Location> list) {
        return list.stream().sorted(new LocationDistanceComparator(target)).toList();
    }

}