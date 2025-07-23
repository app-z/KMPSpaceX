package com.spacex.repository

import com.spacex.database.SpaceXDao
import com.spacex.model.FalconEntity
import kotlinx.coroutines.flow.Flow

class FalconRepository(
    private val falconDao: SpaceXDao
) {

    fun loadData(): Flow<List<FalconEntity>> = falconDao.getAllRockets()

    fun insertFalcons(list: List<FalconEntity>) = falconDao.insertAllRockets(list)

    fun getCount(): Int = falconDao.count()

    fun bookmark(id: String) {
        val falcon = falconDao.getRocket(id)
        falcon.isBookMark = true
        falconDao.insertRocket(falcon)
    }

    fun unBookmark(id: String) {
        val falcon = falconDao.getRocket(id)
        falcon.isBookMark = false
        falconDao.insertRocket(falcon)
    }

    fun getRocket(id: String) : FalconEntity {
        val falcon = falconDao.getRocket(id)
        return falcon
    }
}