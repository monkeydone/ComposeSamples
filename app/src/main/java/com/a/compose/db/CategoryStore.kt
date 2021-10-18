package com.a.compose.db

import kotlinx.coroutines.flow.Flow

/**
 * A data repository for [Category] instances.
 */
class CategoryStore(
    private val categoriesDao: CategoriesDao

) {
    /**
     * Adds the category to the database if it doesn't already exist.
     *
     * @return the id of the newly inserted/existing category
     */
    suspend fun addCategory(category: Category): Long {
        return when (val local = categoriesDao.getCategoryWithName(category.name)) {
            null -> categoriesDao.insert(category)
            else -> local.id
        }
    }

    suspend fun listCategory():List<Category> {
        return categoriesDao.listAllCategory()
    }

}
