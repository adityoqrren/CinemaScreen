package com.myapp.cinemascreen.data.source.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.myapp.cinemascreen.utils.MediaType
import com.myapp.cinemascreen.data.util.DataMapper
import com.myapp.cinemascreen.data.models.MovieTVListItem
import com.myapp.cinemascreen.data.source.remote.ApiResponse
import com.myapp.cinemascreen.data.source.remote.RemoteDataSource
import kotlinx.coroutines.flow.first

class MovieTVPagingSource(
    private val remoteDataSource: RemoteDataSource,
    private val query: String
) : PagingSource<Int, MovieTVListItem>() {

    override fun getRefreshKey(state: PagingState<Int, MovieTVListItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieTVListItem> {
        val page = params.key ?: 1
        val movieResult =
            remoteDataSource.searchMovieTV(query = query, page = page, mediaType = MediaType.Movie)
        val tvResult =
            remoteDataSource.searchMovieTV(query = query, page = page, mediaType = MediaType.TV)

        try {
            val mResult = movieResult.first()
            val tResult = tvResult.first()
            Log.d("check data paging", "mResult: $mResult")
            Log.d("check data paging", "tResult: $tResult")
            when {
                (mResult is ApiResponse.Success && tResult is ApiResponse.Success) -> {
                    Log.d("check data paging","mResult: ${mResult.data[0]}")
//                    Log.d("check data paging","tResult: ${tResult.data}")
                    //TODO: soon optimized this process using coroutines!
                    val combinedResult =
                        mResult.data.map {
                            MovieTVListItem(
                                it.id,
                                it.title ?: "",
                                it.poster_path ?: "",
                                MediaType.Movie,
                                it.release_date ?: "",
                                it.popularity,
                                it.vote_average
                            )
                        } +
                        tResult.data.map {
                            DataMapper.mapMediaListItemtoMovieTVListItem(it, MediaType.TV)
                        }

                    Log.d("check data paging", "combined: $combinedResult")

                    return LoadResult.Page(
                        data = combinedResult,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (combinedResult.isEmpty()) null else page + 1
                    )
                }

                mResult is ApiResponse.Error -> {
                    return LoadResult.Error(Exception(mResult.errorMessage))
                }

                tResult is ApiResponse.Error -> {
                    return LoadResult.Error(Exception(tResult.errorMessage))
                }

                else -> {
                    return LoadResult.Error(Exception("Unexpected error occurs. try again later"))
                }
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}