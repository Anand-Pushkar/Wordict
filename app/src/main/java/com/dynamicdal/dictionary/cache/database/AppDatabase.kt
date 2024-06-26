package com.dynamicdal.dictionary.cache.database

import androidx.annotation.NonNull
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration
import com.dynamicdal.dictionary.cache.definition.DefinitionDao
import com.dynamicdal.dictionary.cache.definition.entites.DefinitionEntity
import com.dynamicdal.dictionary.cache.rhyme.RhymeDao
import com.dynamicdal.dictionary.cache.rhyme.entities.MyRhymeEntity
import com.dynamicdal.dictionary.cache.rhyme.entities.RhymeEntity
import com.dynamicdal.dictionary.cache.rhyme.relations.RhymeZoneCrossRef


@Database(
    entities = [
        DefinitionEntity::class,
        MyRhymeEntity::class,
        RhymeEntity::class,
        RhymeZoneCrossRef::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun definitionDao(): DefinitionDao
    abstract fun rhymeDao(): RhymeDao

    companion object {
        val DATABASE_NAME = "app_db"
    }


    @VisibleForTesting
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(@NonNull database: SupportSQLiteDatabase) {
        }
    }
}