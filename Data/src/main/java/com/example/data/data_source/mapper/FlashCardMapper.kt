package com.example.data.data_source.mapper

import com.example.domain.model.FlashCardModel
import com.example.persistence.model.FlashCardEntity

fun FlashCardEntity.toDomain(): FlashCardModel {
    return FlashCardModel(
        id = id,
        question = question,
        answer = answer
    )
}

fun List<FlashCardEntity>?.toDomain(): List<FlashCardModel>? =
    this?.map { it.toDomain() }

fun FlashCardModel.toEntity(): FlashCardEntity {
    return FlashCardEntity(
        id = id,
        question = question,
        answer = answer
    )
}fun List<FlashCardModel>?.toEntity(): List<FlashCardEntity>? =
    this?.map { it.toEntity() }
