package kr.co.toplink.keepcon.network.api

import kr.co.toplink.keepcon.dto.*
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface AddApi {
    @Multipart
    @POST("files_add_origin.php")
    suspend fun addFileToGCP(@Part files:Array<MultipartBody.Part>): List<GCPResult>

    @POST("gcp_ocr.php")
    suspend fun useOCR(@Body fileName:Array<OCRSend>): List<OCRResult>

    @GET("check_brand.php")
    suspend fun chkBrand(@Query("brandName") brandName: String): ChkValidation

    @GET("gcp/ocr/check_barcode")
    suspend fun chkBarcode(@Query("barcodeNum") barcodeNum: String): ChkValidation

    @POST("gifticons")
    suspend fun addGifticon(@Body addInfo: List<AddInfoNoImg>): List<AddInfoNoImg>

    @POST("files/register_gifticon")
    suspend fun addImgInfo(@Body imgInfo: Array<AddImgInfo>): List<List<AddImgInfoResult>>
}