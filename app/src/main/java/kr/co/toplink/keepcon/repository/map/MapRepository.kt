package kr.co.toplink.keepcon.repository.map

import kr.co.toplink.keepcon.dto.*

class MapRepository(private val remoteDataSource: MapRemoteDataSource) {
    suspend fun getStoreByLocation(storeRequest: StoreRequest): List<Store> {
        return remoteDataSource.getStoreByLocation(storeRequest)
    }

    suspend fun getStoreByBrand(storeByBrandRequest: StoreByBrandRequest): List<Store> {
        return remoteDataSource.getStoreByBrand(storeByBrandRequest)
    }

    suspend fun getPresents(presentRequest: FindPresentRequest): FindDonateResponse {
        return remoteDataSource.getPresent(presentRequest)
    }

    suspend fun donate(donateRequest: DonateRequest) {
        return remoteDataSource.givePresent(donateRequest)
    }

    suspend fun getPresent(getPresentRequest: GetPresentRequest) {
        return remoteDataSource.getPresent(getPresentRequest)
    }
}