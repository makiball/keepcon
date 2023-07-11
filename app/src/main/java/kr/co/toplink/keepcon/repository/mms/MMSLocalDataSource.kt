package kr.co.toplink.keepcon.repository.mms

import kr.co.toplink.keepcon.database.MMSItemDao
import kr.co.toplink.keepcon.dto.MMSItem

class MMSLocalDataSource (
    private val dao: MMSItemDao
) : MMSDataSource {
    override suspend fun addMMSItem(mmsItem: MMSItem) {
        dao.insert(mmsItem)
    }

    override suspend fun getMMSItems(): List<MMSItem> {
        return dao.load()
    }

    override suspend fun selectDate(phoneNumber: String): String {
        return dao.selectDate(phoneNumber)
    }

    override suspend fun updateDate(phoneNumber: String, beforeDate: String) {
        return dao.updateDate(phoneNumber, beforeDate)
    }

    override suspend fun deleteAll() {
        return dao.deleteAll()
    }
}