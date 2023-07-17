package kr.co.toplink.keepcon.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.co.toplink.keepcon.dto.GifticonItem
import kr.co.toplink.keepcon.dto.MMSItem

@Database(entities = [MMSItem::class, GifticonItem::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mmsDao(): MMSItemDao
}