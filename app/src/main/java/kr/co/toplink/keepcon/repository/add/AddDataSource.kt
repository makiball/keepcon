package kr.co.toplink.keepcon.repository.add

import kr.co.toplink.keepcon.dto.*
import okhttp3.MultipartBody

interface AddDataSource {
    suspend fun addFileToGCP(files:Array<MultipartBody.Part>): List<GCPResult>
    suspend fun useOcr(fileName:Array<OCRSend>): List<OCRResult>
    suspend fun chkBrand(brandName: String): ChkValidation
    suspend fun chkBarcode(barcodeNum: String): ChkValidation
    suspend fun addGifticon(addInfo: List<AddInfoNoImg>): List<AddInfoNoImg>
    suspend fun addImgInfo(imgInfo: Array<AddImgInfo>): List<List<AddImgInfoResult>>
}