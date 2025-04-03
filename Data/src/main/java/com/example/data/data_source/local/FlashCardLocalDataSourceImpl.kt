package com.example.data.data_source.local

import com.example.data.data_source.mapper.toDomain
import com.example.data.data_source.mapper.toEntity
import com.example.domain.model.FlashCardModel
import com.example.persistence.dao.FlashCardDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FlashCardLocalDataSourceImpl @Inject constructor(
    private val flashCardDao: FlashCardDao,
) : FlashCardLocalDataSource {
    override suspend fun insertFlashCard(flashCard: FlashCardModel) {
        flashCardDao.insert(flashCard.toEntity())
    }

    override fun getFlashCards(): Flow<Result<List<FlashCardModel>>> =
        flashCardDao.getAll()
            .map { flashCards -> Result.success(flashCards.map { it.toDomain() }) }
            .catch { exception -> emit(Result.failure(exception)) }

    override suspend fun removeFlashCard(flashCard: FlashCardModel) {
        flashCardDao.delete(flashCard.toEntity())
    }
}