package kr.co.toplink.keepcon.repository.gifticonitem

import kr.co.toplink.keepcon.database.MMSItemDao
import kr.co.toplink.keepcon.dto.*
import kr.co.toplink.keepcon.repository.mms.MMSDataSource

interface GifticonItemDataSource {
    //add
    suspend fun addGifticonItem(Gifticon : GifticonItem)

    //get
    suspend fun getGifticonItem() : List<GifticonItem>

    // select gifticon
    suspend fun selectGifticonItem(barcodeNum: String): GifticonItem

    // delete gifticon
    suspend fun deleteGifticonItem(phoneNumber: String)

    // delete
    suspend fun gifticondeleteAll()
}