package com.lvivbus.model.db;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.data.BusStation;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

import java.util.List;

public class BusDAO {

    public static void save(@NonNull final List<Bus> busList) {
        executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(busList);
            }
        });
    }


    @NonNull
    public static RealmResults<Bus> getAll(@NonNull Realm realm) {
        return realm.where(Bus.class).findAllSorted("recentDate", Sort.DESCENDING);
    }

    public static long getAllCount() {
        long result;
        Realm realm = Realm.getDefaultInstance();
        result = realm.where(Bus.class).count();
        realm.close();
        return result;
    }

    public static void setRecentDate(final int busId, final long date) {
        executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Bus bus = getBus(realm, busId);
                if (bus != null) {
                    bus.setRecentDate(date);
                    realm.copyToRealmOrUpdate(bus);
                }
            }
        });
    }

    @Nullable
    public static Bus getBus(@NonNull Realm realm, int id) {
        return realm.where(Bus.class).equalTo("id", id).findFirst();
    }

    public static void setStationList(final int busId, final List<BusStation> busStationList) {
        executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Bus bus = getBus(realm, busId);
                if (bus != null) {
                    RealmList<BusStation> realmList = new RealmList<BusStation>();
                    for (BusStation busStation : busStationList) {
                        BusStation realmObject = realm.createObject(BusStation.class);
                        realmObject.setLat(busStation.getLat());
                        realmObject.setLon(busStation.getLon());
                        realmList.add(realmObject);
                    }
                    bus.setBusStations(realmList);
                    realm.copyToRealmOrUpdate(bus);
                }
            }
        });
    }

    private static void executeTransaction(@NonNull Realm.Transaction transaction) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(transaction);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            close(realm);
        }
    }

    private static void close(@Nullable Realm realm) {
        if (realm != null) {
            realm.close();
        }
    }

}
