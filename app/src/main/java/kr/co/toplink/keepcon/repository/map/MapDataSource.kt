package kr.co.toplink.keepcon.repository.map

import kr.co.toplink.keepcon.dto.*

interface MapDataSource {
    // 1. 서버에 현재 위치 보내면 그에 맞는 근처 매장들 다 줘!
    suspend fun getStoreByLocation(storeRequest: StoreRequest) : List<Store>
    suspend fun getStoreByBrand(storeByBrandRequest: StoreByBrandRequest) : List<Store>
    suspend fun getPresent(presentRequest: FindPresentRequest) : FindDonateResponse
    suspend fun givePresent(donateRequest: DonateRequest)
    suspend fun getPresent(getPresentRequest: GetPresentRequest)
}