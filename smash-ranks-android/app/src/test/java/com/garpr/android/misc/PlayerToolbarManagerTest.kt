package com.garpr.android.misc

import android.app.Application
import com.garpr.android.BaseTest
import com.garpr.android.models.FullPlayer
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class PlayerToolbarManagerTest : BaseTest() {

    lateinit private var mFullPlayer1: FullPlayer
    lateinit private var mFullPlayer2: FullPlayer

    @Inject
    lateinit protected var mApplication: Application

    @Inject
    lateinit protected var mFavoritePlayersManager: FavoritePlayersManager

    @Inject
    lateinit protected var mGson: Gson

    @Inject
    lateinit protected var mIdentityManager: IdentityManager

    @Inject
    lateinit protected var mPlayerToolbarManager: PlayerToolbarManager

    @Inject
    lateinit protected var mRegionManager: RegionManager


    companion object {
        private const val JSON_FULL_PLAYER_1 = "{\"ratings\":{\"norcal\":{\"mu\":43.23022581617458,\"sigma\":1.180825856678202}},\"name\":\"Laudandus\",\"regions\":[\"norcal\"],\"merge_parent\":null,\"merge_children\":[\"588852e7d2994e3bbfa52d6e\"],\"id\":\"588852e7d2994e3bbfa52d6e\",\"merged\":false,\"aliases\":[\"laudandus\"]}"
        private const val JSON_FULL_PLAYER_2 = "{\"ratings\":{\"norcal\":{\"mu\":37.50924828000645,\"sigma\":0.916403703781555}},\"name\":\"Imyt\",\"regions\":[\"norcal\"],\"merge_parent\":null,\"merge_children\":[\"5877eb55d2994e15c7dea98b\"],\"id\":\"5877eb55d2994e15c7dea98b\",\"merged\":false,\"aliases\":[\"imyt\"]}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        mFullPlayer1 = mGson.fromJson(JSON_FULL_PLAYER_1, FullPlayer::class.java)
        mFullPlayer2 = mGson.fromJson(JSON_FULL_PLAYER_2, FullPlayer::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithOnlyNull() {
        val presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                null, null, null)
        assertFalse(presentation.mIsAddToFavoritesVisible)
        assertFalse(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertFalse(presentation.mIsFilterLossesVisible)
        assertFalse(presentation.mIsFilterWinsVisible)
        assertFalse(presentation.mIsRemoveFromFavoritesVisible)
        assertFalse(presentation.mIsSetAsYourIdentityVisible)
        assertFalse(presentation.mIsShareVisible)
        assertFalse(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertNull(presentation.mSetAsYourIdentityTitle)
        assertNull(presentation.mViewYourselfVsThisOpponentTitle)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithOnlyFullPlayer() {
        var presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                mFullPlayer1, null, null)
        assertTrue(presentation.mIsAddToFavoritesVisible)
        assertTrue(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertFalse(presentation.mIsFilterLossesVisible)
        assertFalse(presentation.mIsFilterWinsVisible)
        assertFalse(presentation.mIsRemoveFromFavoritesVisible)
        assertTrue(presentation.mIsSetAsYourIdentityVisible)
        assertTrue(presentation.mIsShareVisible)
        assertFalse(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.mSetAsYourIdentityTitle?.isNotBlank() == true)
        assertNull(presentation.mViewYourselfVsThisOpponentTitle)

        mFavoritePlayersManager.addPlayer(mFullPlayer1, mRegionManager.getRegion())

        presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                mFullPlayer1, null, null)
        assertTrue(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsAddToFavoritesVisible)
        assertTrue(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertFalse(presentation.mIsFilterLossesVisible)
        assertFalse(presentation.mIsFilterWinsVisible)
        assertTrue(presentation.mIsRemoveFromFavoritesVisible)
        assertTrue(presentation.mIsSetAsYourIdentityVisible)
        assertTrue(presentation.mIsShareVisible)
        assertFalse(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.mSetAsYourIdentityTitle?.isNotBlank() == true)
        assertNull(presentation.mViewYourselfVsThisOpponentTitle)

        mIdentityManager.setIdentity(mFullPlayer2, mRegionManager.getRegion())

        presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                mFullPlayer1, null, null)
        assertTrue(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsAddToFavoritesVisible)
        assertTrue(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertFalse(presentation.mIsFilterLossesVisible)
        assertFalse(presentation.mIsFilterWinsVisible)
        assertTrue(presentation.mIsRemoveFromFavoritesVisible)
        assertFalse(presentation.mIsSetAsYourIdentityVisible)
        assertTrue(presentation.mIsShareVisible)
        assertTrue(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertNull(presentation.mSetAsYourIdentityTitle)
        assertTrue(presentation.mViewYourselfVsThisOpponentTitle?.isNotBlank() == true)

        presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                mFullPlayer2, null, null)
        assertTrue(presentation.mIsAliasesVisible)
        assertTrue(presentation.mIsAddToFavoritesVisible)
        assertTrue(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertFalse(presentation.mIsFilterLossesVisible)
        assertFalse(presentation.mIsFilterWinsVisible)
        assertFalse(presentation.mIsRemoveFromFavoritesVisible)
        assertFalse(presentation.mIsSetAsYourIdentityVisible)
        assertTrue(presentation.mIsShareVisible)
        assertFalse(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertNull(presentation.mSetAsYourIdentityTitle)
        assertNull(presentation.mViewYourselfVsThisOpponentTitle)
    }

}
