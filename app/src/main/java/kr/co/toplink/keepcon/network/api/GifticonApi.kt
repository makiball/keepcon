package kr.co.toplink.keepcon.network.api

import kr.co.toplink.keepcon.dto.*
import retrofit2.http.*

interface GifticonApi {
    //사용자 기프티콘 목록
    //@GET("gifticons/{email}/{social}")
    @GET("gifticons.php")
    suspend fun getGifticonByUser(
        //@Path("email") email: String,
        //@Path("social") social: String
        @Query("email") email: String,
        @Query("social") social: String

    ): List<Gifticon>

    //@GET("gifticons/{email}/{social}/map")
    @GET("gifticons.php")
    suspend fun getGifticonMapByUser(
        @Path("email") email: String,
        @Path("social") social: String
    ): List<Gifticon>

    //바코드 번호로 기프티콘
    @GET("gifticons/{barcodeNum}")
    suspend fun getGifticonByBarNum(
        @Path("barcodeNum") barcodeNum: String
    ) : GifticonResponse

    //사용자 기프티콘 브랜드 목록
    //@GET("gifticons/brandsort/{email}/{social}")
    @GET("gifticons_brandsort.php")
    suspend fun getBrandHome(
        //@Path("email") email: String,
        //@Path("social") social: String
        @Query("email") email: String,
        @Query("social") social: String
    ) : List<BrandResponse>

    //현재위치에서 가능한 브랜드 목록
    @POST("local/shake")
    suspend fun getBrandsByLocation(@Body request: StoreRequest): List<Brand>

    //브랜드 별 사용자 기프티콘 목록
    @POST("gifticons/brand")
    suspend fun getGifticonByBrand(@Body gifticonByBrandRequest: GifticonByBrandRequest): List<Gifticon>

    //히스토리
    @POST("history.php")
    suspend fun getHistory(@Body req: UserDeleteRequest): List<Gifticon>

    //업데이트
    @HTTP(method = "PUT", path = "gifticons", hasBody = true)
    suspend fun updateGifticon(@Body gifticon: UpdateRequest): UpdateResponse

    //삭제
    @HTTP(method = "DELETE", path = "gifticons", hasBody = true)
    suspend fun deleteGifticon(@Body barcodeNum: DeleteRequest)

}