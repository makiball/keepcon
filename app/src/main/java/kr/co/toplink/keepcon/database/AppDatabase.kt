package kr.co.toplink.keepcon.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kr.co.toplink.keepcon.dto.GifticonItem
import kr.co.toplink.keepcon.dto.MMSItem

@Database(entities = [MMSItem::class, GifticonItem::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mmsDao(): MMSItemDao
}