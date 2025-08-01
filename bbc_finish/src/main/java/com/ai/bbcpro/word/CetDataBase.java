package com.ai.bbcpro.word;



import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import android.content.Context;



@Database(entities = { CetRootWord.class}, version = 3,exportSchema = false)
public abstract class CetDataBase extends RoomDatabase {

    public static final String DB_NAME = "cet1.db";
    private static  CetDataBase instance;

    public static synchronized CetDataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static CetDataBase create(final Context context) {
        return Room.databaseBuilder(context, CetDataBase.class, DB_NAME)
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_2_3)
                .addMigrations(MIGRATION_1_3)
                .build();
    }

    public abstract CetDAO getUserDao();
    public abstract CetRootWordDAO getCetRootWordDao();

    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //此处对于数据库中的所有更新都需要写下面的代码

        }
    };

    static final Migration MIGRATION_1_3 = new Migration(1,3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //此处对于数据库中的所有更新都需要写下面的代码

        }
    };
}
