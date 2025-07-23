package com.spacex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spacex.model.FalconEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpaceXDao {

    @Query("SELECT * FROM SpaceX ORDER BY id")
    fun getAllRockets(): Flow<List<FalconEntity>>

    @Query("SELECT * FROM SpaceX WHERE name LIKE '%' || :filter || '%' ORDER BY id")
    fun getFilteredRockets(filter: String): Flow<List<FalconEntity>>

    @Query("SELECT * FROM SpaceX WHERE name LIKE '%' || :filter || '%' AND is_bookmark = true ORDER BY id")
    fun getFilteredFavoriteRockets(filter: String): Flow<List<FalconEntity>>

    @Query("SELECT * FROM SpaceX WHERE is_bookmark = true ORDER BY id")
    fun getFavoriteRockets(): Flow<List<FalconEntity>>

    @Query("SELECT * FROM SpaceX WHERE id = :id")
    suspend fun getRocket(id: String): FalconEntity

    @Query("SELECT COUNT(*) as count FROM SpaceX")
    suspend fun count(): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRockets(rockets: List<FalconEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertRocket(falcon: FalconEntity)


}
