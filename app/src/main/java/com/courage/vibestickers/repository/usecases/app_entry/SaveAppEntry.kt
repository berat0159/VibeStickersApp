package com.courage.vibestickers.repository.usecases.app_entry

import com.courage.vibestickers.data.domain.LocalUserManager

class SaveAppEntry(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(){
        localUserManager.saveAppEntry()
    }
}
