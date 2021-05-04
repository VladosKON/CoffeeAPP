package com.example.coffeeapp.di

import android.app.Application
import androidx.room.Room
import com.example.coffeeapp.data.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton
//Установка зависимостей
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application, callback: NoteDatabase.Callback)
        = Room.databaseBuilder(app, NoteDatabase::class.java, "note_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Provides
    fun provideNoteDao(db: NoteDatabase) = db.noteDao()

    @Provides
    fun provideTagDao(db: NoteDatabase) = db.tagDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope