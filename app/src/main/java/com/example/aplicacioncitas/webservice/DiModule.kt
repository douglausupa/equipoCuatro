package com.example.aplicacioncitas.di

import com.example.aplicacioncitas.auth.IAuthProvider
import com.example.aplicacioncitas.repository.CitaRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.aplicacioncitas.auth.FirebaseAuthProvider

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideAuthProvider(firebaseAuth: FirebaseAuth): IAuthProvider {
        return FirebaseAuthProvider(firebaseAuth)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCitaRepository(
        firestore: FirebaseFirestore
    ): CitaRepository {
        return CitaRepository()
    }
}