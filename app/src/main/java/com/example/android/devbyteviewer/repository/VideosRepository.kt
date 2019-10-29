package com.example.android.devbyteviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.VideoDatabase
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.network.Network
import com.example.android.devbyteviewer.network.asDatabadeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * REPOSITORY FOR FETCHING DEVBYTE VIDEOS FROM TE NETWORK AND WRITING THEM TO DISK
 */
class VideosRepository(private val database: VideoDatabase){

    /**
     * A PLAYLIST OF VIDEOS THAT CAN BE SHOWN ON THE SCREEN
     */
    val vidoes: LiveData<List<Video>> = Transformations.map(database.videoDao.getVideos()){
        it.asDomainModel()
    }


    /**
     * REFRESH THE VIDEOS STORED IN THE OFFLINE CACHE
     *
     * THIS FUNCTION USES THE IO DISPATCHER TO ENSURE THE DATABASE INSERT DATABASE OOERATION HAPPENS ON THE IO DISPATCHER BY SWITCING TO THE IO
     * DISPATCHER USING WITHCPONTEXT . THIS FUNCTION IS NOW SAFE TO CALL FROM ANY THREAD INCLUDING THE MAIN THREAD
     */
    suspend fun refreshVideos(){
        withContext(Dispatchers.IO){
            val playList = Network.devbytes.getPlaylist().await()
            database.videoDao.insertAll(*playList.asDatabadeModel())
        }
    }
}
