package com.example.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.repository.AppRepository
import com.example.network.model.Search

class MoviePagingSource(
    private val appRepo: AppRepository
) : PagingSource<Int, Search>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
        return try {
            val nextPage = params.key ?: 1
            val response = appRepo.fetchMovieList(
                movieTitle = "batman",
                page = nextPage
            )
            LoadResult.Page(
                data = response.Search,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        } catch (e: Exception) {
            Log.e("TAG", "error2: ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Search>): Int? {
        return state.anchorPosition
    }

}