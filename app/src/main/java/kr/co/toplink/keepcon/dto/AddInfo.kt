package kr.co.toplink.keepcon.dto

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.PrimaryKey
import kr.co.toplink.keepcon.ui.home.ProductBox
import java.util.*

data class GifticonImg(
    val imgUri: Uri?
)

data class AddInfo(
    val barcodeNum:	        String? = null,
    val barcode_filepath:	Uri? = null,
    val barcodePos:	        String? = null,
    val barcode_bitmap:	    Bitmap? = null,
    val productName:		String? = null,
    val productPos:         String? = null,
    val productName_bitmap:	Bitmap? = null,
    val brand:			    String? = null,
    val due:                Date? = Date(),
    val price:		        Int = 0,
    val memo:		        String? = null,
    val state:		        Int = 0
)

/*
data class AddInfo(
    val originalImgUri:Uri,
    val gifticonImgUri: Uri,
    val barcodeImgUri:Uri,
    var barcodeNum:String,
    var brandName:String,
    var productName:String,
    var due:String,
    var isVoucher: Int,
    var price:Int,
    var state:Int,
    var memo:String,
    val email: String,
    val social: String
){
    constructor(
        originalImgUri:Uri,
        gifticonImgUri: Uri,
        barcodeImgUri:Uri,
        barcodeNum:String,
        brandName:String,
        product:String,
        due:String,
        isVoucher: Int,
        price: Int,
        memo:String,
        email: String,
        social: String
    ): this (
        originalImgUri,
        gifticonImgUri,
        barcodeImgUri,
        barcodeNum,
        brandName,
        product,
        due,
        isVoucher,
        price,
        0,
        memo,
        email,
        social
    )

    constructor(
    ):this (
        "".toUri(),
        "".toUri(),
        "".toUri(),
        "",
        "",
        "",
        "",
        0,
        -1,
        0,
        "",
        "",
        ""
    )
}
 */


data class AddInfoNoImg(
    val barcodeNum: String,
    val brandName: String,
    val productName: String,
    val due: String,
    val isVoucher: Int,
    val price: Int,
    val memo: String,
    val email: String,
    val social: String,
    val state: Int = 0
)

data class AddInfoNoImgBoolean(
    var productName: Boolean,
    var brandName: Boolean,
    var barcodeNum: Boolean,
    var due: Boolean,
    var isVoucher: Boolean,
    var price: Boolean
){
    constructor(): this(
        false,
        false,
        false,
        false,
        false,
        false
    )
}

data class GCPResult(
    val fileName: String,
    val filePath: String,
    val id: Long,
    val imageType: Int,
    val width: Int,
    val height: Int
)

data class OCRSend(
    val fileName: String,
    val width: Int,
    val height: Int
)

data class OCRResult(
    val isVoucher: Int,
    val barcodeImg: Map<String, String>,
    var barcodeNum: String,
    val brandName: String,
    val due: Map<String, String>,
    val productImg: Map<String, String>,
    var productName: String,
    val publisher: String,
    val price: Int,
    val validation: Int
)

data class OCRResultDate(
    val Y: String,
    val M: String,
    val D: String
)

data class OCRResultCoordinate(
    val y1: String,
    val x1: String,
    val y2: String,
    val x2: String,
    val y3: String,
    val x3: String,
    val y4: String,
    val x4: String
)

data class AddImgInfo(
    val barcodeNum: String,
    val originGcpFileName: String,
    val productGcpFileName: String,
    val barcodeGcpFileName: String
)

data class ChkValidation(
    val result: Int
)

data class AddImgInfoResult(
    val id: Long,
    val imageType: Int,
    val fileName: String,
    val filePath: String
)

data class GifticonItemList(
    var barcodeNum:	            String? = null,
    val gifticon_filepath:	    Uri,
    val gifticon_file_width:    Int = 0,
    val gifticon_file_height:   Int = 0,
    val barcodePos:	            String? = null,
    var barcode_bitmap:	        Bitmap? = null,
    var productName:		    String? = null,
    val productPos:             String? = null,
    var productName_bitmap:	    Bitmap? = null,
    var brand:			        String? = null,
    var due:                    String? = null,
    var price:		            Int = 0,
    var memo:		            String? = null,
    val state:		            Int = 0
)