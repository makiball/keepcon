package kr.co.toplink.keepcon.network.api

import kr.co.toplink.keepcon.dto.*
import retrofit2.http.Body
import retrofit2.http.POST

interface MapApi {
    //현재위치에서 매장 검색
    @POST("local/search")
    suspend fun getStoreByLocation(@Body storeRequest : StoreRequest):  List<Store>

    //현재위치에서
    @POST("local/search/byBrand")
    suspend fun getStoreByBrand(@Body storeRequest: StoreByBrandRequest) : List<Store>

    @POST("presents/possible_list")
    suspend fun getPresents(@Body findPresentRequest: FindPresentRequest) : FindDonateResponse

    @POST("presents/give_present")
    suspend fun givePresent(@Body request: DonateRequest)

    @POST("presents/get_present")
    suspend fun getPresent(@Body request : GetPresentRequest)
}