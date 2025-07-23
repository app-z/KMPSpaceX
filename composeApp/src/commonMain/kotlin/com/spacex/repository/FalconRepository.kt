package com.spacex.repository

import com.spacex.database.SpaceXDao
import com.spacex.model.FalconEntity
import kotlinx.coroutines.flow.Flow

class FalconRepository(
    private val falconDao: SpaceXDao
) {

    fun loadData(): Flow<List<FalconEntity>> =
        falconDao.getAllRockets()

    fun loadFilteredData(filter: String): Flow<List<FalconEntity>> =
        falconDao.getFilteredRockets(filter)

    fun loadFilteredFavoriteData(filter: String): Flow<List<FalconEntity>> =
        falconDao.getFilteredFavoriteRockets(filter)

    fun loadFavoriteData(): Flow<List<FalconEntity>> =
        falconDao.getFavoriteRockets()

    suspend fun insertFalcons(list: List<FalconEntity>) =
        falconDao.insertAllRockets(list)

    suspend fun getCount(): Int = falconDao.count()

    suspend fun bookmark(id: String) {
        val falcon = falconDao.getRocket(id)
        falcon.isBookMark = true
        falconDao.insertRocket(falcon)
    }

    suspend fun unBookmark(id: String) {
        val falcon = falconDao.getRocket(id)
        falcon.isBookMark = false
        falconDao.insertRocket(falcon)
    }

    suspend fun getRocket(id: String): FalconEntity {
        val falcon = falconDao.getRocket(id)
        return falcon
    }
}