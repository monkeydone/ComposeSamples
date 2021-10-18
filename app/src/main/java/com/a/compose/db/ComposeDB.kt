package com.a.compose.db

import androidx.compose.runtime.Immutable
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Database(
    entities = [
        Category::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeTypeConverters::class)
abstract class ComposeDB : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
}


@Entity(
    tableName = "categories",
    indices = [
        Index("name", unique = true)
    ]
)
@Immutable
data class Category(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val name: String
)

@Dao
abstract class CategoriesDao {
    @Query(
        """
        SELECT categories.* FROM categories
        INNER JOIN (
            SELECT category_id, COUNT(podcast_uri) AS podcast_count FROM podcast_category_entries
            GROUP BY category_id
        ) ON category_id = categories.id
        ORDER BY podcast_count DESC
        LIMIT :limit
        """
    )
    abstract fun categoriesSortedByPodcastCount(
        limit: Int
    ): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE name = :name")
    abstract suspend fun getCategoryWithName(name: String): Category?

    /**
     * The following methods should really live in a base interface. Unfortunately the Kotlin
     * Compiler which we need to use for Compose doesn't work with that.
     * TODO: remove this once we move to a more recent Kotlin compiler
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: Category): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(vararg entity: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(entities: Collection<Category>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: Category)

    @Delete
    abstract suspend fun delete(entity: Category): Int
}
