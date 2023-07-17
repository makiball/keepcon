package kr.co.toplink.keepcon.database
import androidx.room.*
import kr.co.toplink.keepcon.dto.MMSItem
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kr.co.toplink.keepcon.dto.GifticonItem

@Dao
interface MMSItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mmsItem: MMSItem)

    @Query("SELECT * FROM mms_item")
    suspend fun load(): List<MMSItem>

    @Query("SELECT beforeDate FROM mms_item WHERE phoneNumber=:phoneNumber")
    suspend fun selectDate(phoneNumber: String): String

    @Query("UPDATE mms_item SET beforeDate=:beforeDate WHERE phoneNumber=:phoneNumber")
    suspend fun updateDate(phoneNumber: String, beforeDate : String)

    @Query("DELETE FROM mms_item")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun gifticon_insert(gifticon: GifticonItem)

    @Query("SELECT * FROM gifticon_item ORDER BY due ASC")
    suspend fun gifticon_rows(): List<GifticonItem>

    @Query("SELECT * FROM gifticon_item WHERE barcodeNum =:barcodeNum")
    suspend fun gifticon_row(barcodeNum : String): GifticonItem

    @Query("DELETE FROM gifticon_item WHERE barcodeNum =:barcodeNum")
    suspend fun gifticon_delete(barcodeNum : String)

    @Query("DELETE FROM gifticon_item")
    suspend fun gifticon_deleteAll()

}