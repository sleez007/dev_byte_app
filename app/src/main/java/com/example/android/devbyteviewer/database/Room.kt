package com.example.android.devbyteviewer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface  VideoDao{
    @Query("SELECT * FROM   databasevideos")
    fun getVideos() : LiveData<List<Databasevideos>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: Databasevideos)
}

@Database(entities = [Databasevideos::class], version = 1)
abstract class VideoDatabase : RoomDatabase(){
    abstract val videoDao : VideoDao
}


private lateinit var INSTANCE : VideoDatabase

fun getDatabase(context: Context) : VideoDatabase {
    synchronized(VideoDatabase::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext, VideoDatabase::class.java,"videos").build()
        }
    }
    return  INSTANCE

}