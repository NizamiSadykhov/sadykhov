package com.nizamisadykhov.gif_client.data

import com.nizamisadykhov.gif_client.model.PostData

object DataStorage {
    var topPosts: MutableList<PostData> = ArrayList()
    var hotPosts: MutableList<PostData> = ArrayList()
    var latestPosts: MutableList<PostData> = ArrayList()
}