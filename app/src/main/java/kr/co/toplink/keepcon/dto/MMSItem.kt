package kr.co.toplink.keepcon.dto

import android.graphics.Bitmap
import android.net.Uri
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
    @PrimaryKey
    @ColumnInfo(name = "barcodeNum")
    val barcodeNum:	            String,
    val gifticon_filepath:	    String,
    val gifticon_file_width:    Int = 0,
    val gifticon_file_height:   Int = 0,
    val barcodePos:	            String? = null,
    val productName:		    String? = null,
    val productPos:             String? = null,
    val brand:			        String? = null,
    val due:                    String? = null,
    val price:		            Int = 0,
    val memo:		            String? = null,
    val state:		            Int = 0
)