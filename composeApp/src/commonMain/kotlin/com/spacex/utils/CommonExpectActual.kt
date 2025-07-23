package com.spacex.utils

import androidx.room.RoomDatabase
import com.spacex.database.AppDatabase

//expect fun shareLink(url: String)
//
//expect fun randomUUIDStr(): String
//
//expect fun getType(): Type
//
//@Composable
//expect fun getScreenSize(): Size

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>