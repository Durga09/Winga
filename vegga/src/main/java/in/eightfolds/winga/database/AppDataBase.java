package in.eightfolds.winga.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import android.content.Context;
import androidx.annotation.NonNull;

import in.eightfolds.winga.dao.DurationDetailsDao;
import in.eightfolds.winga.model.DurationDetails;


@Database(entities = {DurationDetails.class}, version = 3, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase instance;

    private static final String DB_NAME =  "winga_db";

    public abstract DurationDetailsDao getDurationDetailsDao();

    public static AppDataBase getAppDatabase(Context context) {


        if (instance == null) {

             final Migration MIGRATION_2_3 = new Migration(2, 3) {
                @Override
                public void migrate(SupportSQLiteDatabase database) {
//                    /, , isadddetail INTEGER

                    database.execSQL("ALTER TABLE 'durationdetails' ADD COLUMN 'homepageaddid' INTEGER  NOT NULL DEFAULT '0'");
                    database.execSQL("ALTER TABLE 'durationdetails' ADD COLUMN 'aduniqueid' INTEGER  NOT NULL DEFAULT '0'");
                    database.execSQL("ALTER TABLE 'durationdetails' ADD COLUMN 'isadddetail' INTEGER  NOT NULL DEFAULT '0'");
                }
            };


            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class,
                    DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_2_3)
                    .build();
        }
        return instance;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }



    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
