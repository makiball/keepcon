package kr.co.toplink.keepcon.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "mms_item")
data class MMSItem(
    @PrimaryKey @ColumnInfo(name = "phoneNumber")
    val phoneNumber : String,
    @ColumnInfo(name = "beforeDate")
    val beforeDate : String
)

@Entity(
    tableName = "gifticon_item"
)
data class GifticonItem(
    @PrimaryKey @ColumnInfo(name = "barcodeNum")
    val barcodeNum :	    String,
    val barcodePos :	    String?,
    val barcode_filepath :	String?,
    val productName:		String?,
    val productPost:		String?,
    val brand:			    String?,
    val due	:               Date?,
    val price	:		    Int = 0,
    val memo	:		    String?,
    val state	:		    Int = 0
)