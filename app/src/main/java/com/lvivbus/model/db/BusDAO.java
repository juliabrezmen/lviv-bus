package com.lvivbus.model.db;

import android.support.annotation.NonNull;
import com.lvivbus.ui.data.Bus;
import io.realm.Realm;
import io.realm.RealmResults;

import java.util.List;

public class BusDAO {

    public static void save(@NonNull final List<Bus> busList) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(busList);
            }
        });
    }


    @NonNull
    public static RealmResults<Bus> getAll(@NonNull Realm realm) {
        return realm.where(Bus.class).findAll();
    }

    public static long getAllCount() {
        long result;
        Realm realm = Realm.getDefaultInstance();
        result = realm.where(Bus.class).count();
        realm.close();
        return result;
    }
}
