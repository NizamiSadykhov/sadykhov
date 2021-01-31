package com.nizamisadykhov.gif_client.model


data class PostDataResponse(
    val result: List<PostData>,
    val totalCount: Int
)