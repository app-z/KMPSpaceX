package com.spacex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spacex.entity.FalconEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpaceXDao {

    @Query("SELECT * FROM SpaceX")
    fun getAllRockets(): Flow<List<FalconEntity>>

    @Query("SELECT * FROM SpaceX WHERE id = :id")
    fun getRocket(id: Int): FalconEntity

    @Query("SELECT COUNT(*) as count FROM SpaceX")
    suspend fun count(): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRockets(rockets: List<FalconEntity>)
}
