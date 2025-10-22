package com.courage.vibestickers

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.courage.vibestickers.data.domain.LocalUserManager
import com.courage.vibestickers.data.domain.LocalUserManagerImpl
import com.courage.vibestickers.repository.CategoryDetailRepository
import com.courage.vibestickers.repository.CategoryDetailStickerImpl
import com.courage.vibestickers.repository.CreatedImpl
import com.courage.vibestickers.repository.CreatedRepository
import com.courage.vibestickers.repository.FavoriteStickersImpl
import com.courage.vibestickers.repository.FavoriteStickersRepository
import com.courage.vibestickers.repository.StickerRepositoryImpl
import com.courage.vibestickers.repository.StickersRepository
import com.courage.vibestickers.repository.StoryRepository
import com.courage.vibestickers.repository.StoryRepositoryImpl
import com.courage.vibestickers.repository.UserRepository
import com.courage.vibestickers.repository.UserRepositoryImpl
import com.courage.vibestickers.repository.usecases.app_entry.AppEntryUseCases
import com.courage.vibestickers.repository.usecases.app_entry.ReadAppEntry
import com.courage.vibestickers.repository.usecases.app_entry.SaveAppEntry
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserManager(
        application: Application
    ): LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(localUserManager: LocalUserManager): AppEntryUseCases {
        return AppEntryUseCases(
            readAppEntry = ReadAppEntry(localUserManager),
            saveAppEntry = SaveAppEntry(localUserManager)
        )
    }

    @Provides
    @Singleton
    fun provideDatastore(@ApplicationContext context:Context): DataStore<Preferences>{
        return context.dataStore
    }



    @Provides
    @Singleton
    fun provideCreatedRepository(
        firestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): CreatedRepository {
        return CreatedImpl(firestore,firebaseStorage)
    }


    @Provides
    @Singleton
    fun provideFavoriteRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): FavoriteStickersRepository {
        return FavoriteStickersImpl(firestore, storage)
    }

    @Provides
    @Singleton
    fun provideStickersRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): StickersRepository {
        return StickerRepositoryImpl(firestore, storage)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore
    ): UserRepository {
        return UserRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideStoryRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): StoryRepository {
        return StoryRepositoryImpl(firestore, storage)
    }
    @Provides
    @Singleton
    fun provideCategoryDetailRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ) : CategoryDetailRepository {
        return CategoryDetailStickerImpl(firestore, storage)
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore.apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) // İsteğe bağlı: Offline destek
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideStorage(): FirebaseStorage {
        return Firebase.storage.apply {
            maxDownloadRetryTimeMillis = 60000 // İsteğe bağlı: Timeout süresi
        }
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_entry")
}