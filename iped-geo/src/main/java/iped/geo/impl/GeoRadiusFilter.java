package iped.geo.impl;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import org.apache.lucene.document.LatLonPoint;
import org.apache.lucene.search.Query;

import iped.properties.ExtraProperties;
import iped.viewers.api.IQueryFilter;

/**
 * A geographic circle drawn on the Map tab. Instances retain their identity
 * when dragged into the Filters tab combination tree.
 */
public final class GeoRadiusFilter implements IQueryFilter {

    private final String id = UUID.randomUUID().toString();
    private final double latitude;
    private final double longitude;
    private final double radiusMeters;
    private final Query query;
    private volatile boolean enabled = true;

    public GeoRadiusFilter(double latitude, double longitude, double radiusMeters) {
        if (!Double.isFinite(latitude) || !Double.isFinite(longitude)
                || !Double.isFinite(radiusMeters) || latitude < -90 || latitude > 90
                || longitude < -180 || longitude > 180
                || radiusMeters <= 0 || radiusMeters > 20_000_000) {
            throw new IllegalArgumentException("Invalid geographic radius");
        }
        this.latitude = latitude;
        this.longitude = longitude;
        this.radiusMeters = radiusMeters;
        this.query = LatLonPoint.newDistanceQuery(ExtraProperties.LOCATIONS, latitude, longitude, radiusMeters);
    }

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRadiusMeters() {
        return radiusMeters;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Query getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Radius %.5f, %.5f (%.2f km)", latitude, longitude, radiusMeters / 1000.0);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GeoRadiusFilter && Objects.equals(id, ((GeoRadiusFilter) other).id);
    }
}
