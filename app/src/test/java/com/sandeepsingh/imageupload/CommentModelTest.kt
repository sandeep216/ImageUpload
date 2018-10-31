package com.sandeepsingh.imageupload

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.nhaarman.mockito_kotlin.*
import com.sandeepsingh.imageupload.core.IRetrofitCalls
import com.sandeepsingh.imageupload.feature.comment.IComment
import com.sandeepsingh.imageupload.feature.comment.model.CommentModel
import com.sandeepsingh.imageupload.feature.comment.presenter.CommentPresenter
import org.junit.Before
import org.junit.Test
import retrofit2.Call

/**
 * Created by Sandeep on 10/30/18.
 */
class CommentModelTest {

    lateinit var mockedPresenter : IComment.ModelToPresenter
    lateinit var mockedModel : CommentModel
    lateinit var presenter : CommentPresenter
    lateinit var iRetrofitCalls: IRetrofitCalls
    lateinit var mockedCalls: Call<JsonElement>

    @Before
    fun setUp(){
        mockedPresenter = mock()
        mockedModel = mock()
        presenter = mock()
        iRetrofitCalls = mock()
        mockedCalls = mock()

        whenever(iRetrofitCalls.getComments(any(), any())).thenReturn(mockedCalls)
    }

    @Test
    fun noImageId(){
        val mockedModel = CommentModel(mockedPresenter)
        mockedModel.imageId = ""
        mockedModel.fetchComments()
        verify(mockedPresenter).isDatabaseError(true)
        verify(iRetrofitCalls, never()).getComments("imageId",mockedModel.imageId!!)
    }

    @Test
    fun nullImageId(){
        val mockedModel = CommentModel(mockedPresenter)
        mockedModel.imageId = null
        mockedModel.fetchComments()
        verify(mockedPresenter).isDatabaseError(true)
        verify(iRetrofitCalls, never()).getComments("imageId",mockedModel.imageId!!)
    }

    @Test
    fun validImageId() {
        val mockedModel = CommentModel(mockedPresenter)
        mockedModel.imageId = "-LPtjEn9kobRIwhX-sVT"
        mockedModel.fetchComments()
        verify(mockedPresenter).isDatabaseError(true)
        verify(iRetrofitCalls, never()).getComments("imageId", mockedModel.imageId!!)
    }
}