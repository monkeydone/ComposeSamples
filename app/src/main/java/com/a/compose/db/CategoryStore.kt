/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.a.compose.db

import kotlinx.coroutines.flow.Flow

/**
 * A data repository for [Category] instances.
 */
class CategoryStore(
    private val categoriesDao: CategoriesDao

) {
    /**
     * Returns a flow containing a list of categories which is sorted by the number
     * of podcasts in each category.
     */
    fun categoriesSortedByPodcastCount(
        limit: Int = Integer.MAX_VALUE
    ): Flow<List<Category>> {
        return categoriesDao.categoriesSortedByPodcastCount(limit)
    }



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

}
