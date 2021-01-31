package com.nizamisadykhov.gif_client.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.nizamisadykhov.gif_client.R
import com.nizamisadykhov.gif_client.data.DataStorage
import com.nizamisadykhov.gif_client.model.PostData
import com.nizamisadykhov.gif_client.network.RetrofitConfigurator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_page.*

class PageFragment : Fragment() {
    private var currentPostPos = 0
    private var postType: Int = 0

    private var loadDisposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postType = arguments?.getInt(ARG_POS) ?: 0
        showCurrentPost()

        btNext.setOnClickListener {
            currentPostPos += 1
            showCurrentPost()
        }

        btPrev.setOnClickListener {
            currentPostPos -= 1
            showCurrentPost()
        }
    }

    private fun showCurrentPost() {
        val postData = when (postType) {
            LATEST_POSTS -> DataStorage.latestPosts.getOrNull(currentPostPos)
            HOT_POSTS -> DataStorage.hotPosts.getOrNull(currentPostPos)
            TOP_POSTS -> DataStorage.topPosts.getOrNull(currentPostPos)
            else -> throw IllegalArgumentException()
        }

        if (postData == null) {
            loadMoreData(postType)
        } else {
            showPost(postData)
        }

        styleControlButtons()
    }

    private fun loadMoreData(type: Int) {
        val nextPage = (currentPostPos + 1) / POSTS_PER_PAGE
        val request = when (type) {
            LATEST_POSTS -> RetrofitConfigurator.getApiClient().getLatestPosts(nextPage)
            HOT_POSTS -> RetrofitConfigurator.getApiClient().getHotPosts(nextPage)
            TOP_POSTS -> RetrofitConfigurator.getApiClient().getTopPosts(nextPage)
            else -> throw IllegalArgumentException()
        }

        loadDisposable?.dispose()
        vLoading.visibility = View.VISIBLE
        loadDisposable = request
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                vLoading.visibility = View.GONE
                when (postType) {
                    LATEST_POSTS -> DataStorage.latestPosts.addAll(it.result)
                    HOT_POSTS -> DataStorage.hotPosts.addAll(it.result)
                    TOP_POSTS -> DataStorage.topPosts.addAll(it.result)
                }

                if (it.result.isEmpty()) {
                    styleControlButtons()
                    tvError.text = getString(R.string.no_posts_error)
                    tvError.visibility = View.VISIBLE
                } else {
                    tvError.visibility = View.GONE
                    showCurrentPost()
                }
            }, {
                vLoading.visibility = View.GONE
                tvError.text = getString(R.string.loading_error)
                tvError.visibility = View.VISIBLE
                it.printStackTrace()
            })
    }

    private fun showPost(post: PostData) {
        val glide = Glide.with(this)


        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        glide.clear(ivGif)
        glide.load(post.gifURL)
            .placeholder(circularProgressDrawable)
            .centerCrop()
            .into(ivGif)

        tvPostText.text = post.description
    }

    private fun styleControlButtons() {
        btPrev.isEnabled = currentPostPos > 0
        btPrev.alpha = if (currentPostPos > 0) 1f else 0.4f
    }

    override fun onDestroyView() {
        loadDisposable?.dispose()
        super.onDestroyView()
    }

    companion object {
        fun newInstance(pos: Int): PageFragment {
            val args = Bundle()
            args.putInt(ARG_POS, pos)
            val fragment = PageFragment()
            fragment.arguments = args
            return fragment
        }

        private const val ARG_POS = "pos_arg"

        private const val LATEST_POSTS = 0
        private const val HOT_POSTS = 1
        private const val TOP_POSTS = 2

        private const val POSTS_PER_PAGE = 5
    }
}