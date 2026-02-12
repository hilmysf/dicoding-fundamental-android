package com.hilmysf.fundamental.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM bookmarked_events")
    fun getBookmarkedEvents(): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)

    @Query("SELECT EXISTS(SELECT * FROM bookmarked_events WHERE id = :eventId)")
    fun isEventBookmarked(eventId: Int): Flow<Boolean>

    @Query("SELECT * FROM bookmarked_events WHERE id = :eventId")
    fun getEventById(eventId: Int): Flow<EventEntity?>
}