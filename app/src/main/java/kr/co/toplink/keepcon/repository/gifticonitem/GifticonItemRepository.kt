package kr.co.toplink.keepcon.repository.gifticonitem

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.toplink.keepcon.dto.*

class GifticonItemRepository(
    private val LocalDataSource: GifticonItemLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun addGifticonItem(Gifticon : GifticonItem){
        withContext(ioDispatcher){
            val gifticonItem = GifticonItem(
                barcodeNum = Gifticon.barcodeNum,
                barcodePos = Gifticon.barcodePos,
                gifticon_filepath = Gifticon.gifticon_filepath,
                productName = Gifticon.productName,
                productPos = Gifticon.productPos,
                brand = Gifticon.brand,
                due = Gifticon.due,
                price = Gifticon.price,
                memo = Gifticon.memo,
                state = Gifticon.state
            )
            LocalDataSource.addGifticonItem(gifticonItem)
        }
    }

    suspend fun getGifticonItem() : List<GifticonItem> {
        return withContext(ioDispatcher){
            LocalDataSource.getGifticonItem()
        }
    }

    suspend fun selectGifticonItem(barcodeNum: String) : GifticonItem  {
        return withContext(ioDispatcher){
            LocalDataSource.selectGifticonItem(barcodeNum)
        }
    }

    suspend fun deleteGifticonItem(barcodeNum: String)  {
        withContext(ioDispatcher){
            LocalDataSource.deleteGifticonItem(barcodeNum)
        }
    }

    suspend fun gifticondeleteAll()  {
        withContext(ioDispatcher){
            LocalDataSource.gifticondeleteAll()
        }
    }
}