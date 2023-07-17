package kr.co.toplink.keepcon.repository.gifticonitem

import kr.co.toplink.keepcon.database.MMSItemDao
import kr.co.toplink.keepcon.dto.*
import kr.co.toplink.keepcon.network.api.GifticonApi
import kr.co.toplink.keepcon.repository.mms.MMSDataSource

class GifticonItemLocalDataSource (
    private val dao: MMSItemDao
) : GifticonItemDataSource {

    //add
    override suspend fun addGifticonItem(Gifticon : GifticonItem){
        dao.gifticon_insert(Gifticon)
    }

    //get
    override suspend fun getGifticonItem() : List<GifticonItem> {
        return dao.gifticon_rows()
    }

    // select gifticon
    override suspend fun selectGifticonItem(barcodeNum: String): GifticonItem {
        return  dao.gifticon_row(barcodeNum)
    }

    // delete gifticon
    override suspend fun deleteGifticonItem(barcodeNum: String) {
        dao.gifticon_delete(barcodeNum)
    }

    // delete
    override suspend fun deleteAll(){
        dao.gifticon_deleteAll()
    }
}