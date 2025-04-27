package com.courage.vibestickers.repository.usecases.app_entry

import com.courage.vibestickers.data.domain.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadAppEntry(
 private val localUserManager: LocalUserManager
) {
    operator fun invoke() : Flow<Boolean>{
        return localUserManager.readAppEntry
    }

}
