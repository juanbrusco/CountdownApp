package ar.com.jb.countdownapp.utils;

import android.app.Application;
import org.greenrobot.greendao.database.Database;
import ar.com.jb.countdownapp.entities.DaoMaster;
import ar.com.jb.countdownapp.entities.DaoSession;

/**
 * Created by juan.brusco on 09-Jan-18.
 */

public class MyAppClass extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "countdowndb_dev");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
