package com.spacex.repository

import com.spacex.database.SpaceXDao
import com.spacex.model.FalconEntity
import kotlinx.coroutines.flow.Flow

class FalconRepository(
    private val falconDao: SpaceXDao
) {

    fun loadData(): Flow<List<FalconEntity>> = falconDao.getAllRockets()

    fun insertFalcons(list: List<FalconEntity>) = falconDao.insertAllRockets(list)

    fun getCount() : Int = falconDao.count()



//    suspend fun upsertArticle(article: Article) {
//        newsDao.upsert(article)
//    }
//
//    suspend fun deleteArticle(article: Article) {
//        newsDao.delete(article)
//    }
//
//    suspend fun deleteAllBookmark() {
//        newsDao.deleteAllBookmark()
//    }
//
//    fun getArticles() = falconDao.getArticles().flowOn(Dispatchers.IO)
//
//    suspend fun getArticle(articleId: String): Article? {
//        return newsDao.getArticle(articleId = articleId)
//    }
}