package com.garpr.android.data.database

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.garpr.android.data.models.Avatar
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCharacter
import com.garpr.android.misc.Constants
import com.garpr.android.test.BaseAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MigrationsTest : BaseAndroidTest() {

    private val avatarConverter = AvatarConverter()
    private val endpointConverter = EndpointConverter()
    private val listOfSmashCharacterConverter = ListOfSmashCharacterConverter()
    private val mapOfStringToStringConverter = MapOfStringToStringConverter()
    private val regionConverter = RegionConverter()

    @get:Rule
    val helper = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            AppDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun testMigrate1To2() {
        var db = helper.createDatabase(DATABASE_NAME, 1)
        db.execSQL("INSERT INTO favoritePlayers (id, name, region) VALUES ('${FAVORITE_CHARLEZARD.id}', '${FAVORITE_CHARLEZARD.name}', '${regionConverter.stringFromRegion(FAVORITE_CHARLEZARD.region)}')")
        db.close()

        db = helper.runMigrationsAndValidate(DATABASE_NAME, 2, true,
                Migrations.MIGRATION_1_2)

        var cursor = db.query("SELECT * FROM favoritePlayers")
        assertEquals(1, cursor.count)
        assertEquals(3, cursor.columnCount)

        assertTrue(cursor.moveToFirst())
        assertEquals(FAVORITE_CHARLEZARD.id, cursor.getString(0))
        assertEquals(FAVORITE_CHARLEZARD.name, cursor.getString(1))
        assertEquals(FAVORITE_CHARLEZARD.region, regionConverter.regionFromString(cursor.getString(2)))
        cursor.close()

        db.execSQL("INSERT INTO smashCompetitors (avatar, endpoint, mains, websites, id, name, tag) VALUES ('${avatarConverter.stringFromAvatar(SMASH_COMPETITOR_CHARLEZARD.avatar)}', '${endpointConverter.intFromEndpoint(SMASH_COMPETITOR_CHARLEZARD.endpoint)}', '${listOfSmashCharacterConverter.stringFromListOfSmashCharacter(SMASH_COMPETITOR_CHARLEZARD.mains)}', '${mapOfStringToStringConverter.stringFromMapOfStringToString(SMASH_COMPETITOR_CHARLEZARD.websites)}', '${SMASH_COMPETITOR_CHARLEZARD.id}', '${SMASH_COMPETITOR_CHARLEZARD.name}', '${SMASH_COMPETITOR_CHARLEZARD.tag}')")

        cursor = db.query("SELECT * FROM smashCompetitors")
        assertEquals(1, cursor.count)
        assertEquals(7, cursor.columnCount)

        assertTrue(cursor.moveToFirst())
        assertEquals(SMASH_COMPETITOR_CHARLEZARD.avatar, avatarConverter.avatarFromString(cursor.getString(0)))
        assertEquals(SMASH_COMPETITOR_CHARLEZARD.endpoint, endpointConverter.endpointFromInt(cursor.getInt(1)))
        assertEquals(SMASH_COMPETITOR_CHARLEZARD.mains, listOfSmashCharacterConverter.listOfSmashCharacterFromString(cursor.getString(2)))
        assertEquals(SMASH_COMPETITOR_CHARLEZARD.websites, mapOfStringToStringConverter.mapOfStringToStringFromString(cursor.getString(3)))
        assertEquals(SMASH_COMPETITOR_CHARLEZARD.id, cursor.getString(4))
        assertEquals(SMASH_COMPETITOR_CHARLEZARD.name, cursor.getString(5))
        assertEquals(SMASH_COMPETITOR_CHARLEZARD.tag, cursor.getString(6))
        cursor.close()

        db.close()
    }

    @Test
    fun testMigrate1To2WithEmptyFavoritePlayersAndEmptySmashCompetitors() {
        var db = helper.createDatabase(DATABASE_NAME, 1)
        db.close()

        db = helper.runMigrationsAndValidate(DATABASE_NAME, 2, true,
                Migrations.MIGRATION_1_2)

        var cursor = db.query("SELECT * FROM favoritePlayers")
        assertEquals(0, cursor.count)
        assertEquals(3, cursor.columnCount)
        cursor.close()

        cursor = db.query("SELECT * FROM smashCompetitors")
        assertEquals(0, cursor.count)
        assertEquals(7, cursor.columnCount)
        cursor.close()

        db.close()
    }

    @Test
    fun testMigrate1To2WithEmptySmashCompetitors() {
        var db = helper.createDatabase(DATABASE_NAME, 1)
        db.execSQL("INSERT INTO favoritePlayers (id, name, region) VALUES ('${FAVORITE_CHARLEZARD.id}', '${FAVORITE_CHARLEZARD.name}', '${regionConverter.stringFromRegion(FAVORITE_CHARLEZARD.region)}')")
        db.close()

        db = helper.runMigrationsAndValidate(DATABASE_NAME, 2, true,
                Migrations.MIGRATION_1_2)

        var cursor = db.query("SELECT * FROM favoritePlayers")
        assertEquals(1, cursor.count)
        assertEquals(3, cursor.columnCount)

        assertTrue(cursor.moveToFirst())
        assertEquals(FAVORITE_CHARLEZARD.id, cursor.getString(0))
        assertEquals(FAVORITE_CHARLEZARD.name, cursor.getString(1))
        assertEquals(FAVORITE_CHARLEZARD.region, regionConverter.regionFromString(cursor.getString(2)))
        cursor.close()

        cursor = db.query("SELECT * FROM smashCompetitors")
        assertEquals(0, cursor.count)
        assertEquals(7, cursor.columnCount)
        cursor.close()

        db.close()
    }

    companion object {
        private const val DATABASE_NAME = "GAR_PR_TEST_DATABASE"

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val FAVORITE_CHARLEZARD = DbFavoritePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard",
                region = NORCAL
        )

        private val SMASH_COMPETITOR_CHARLEZARD = DbSmashCompetitor(
                avatar = Avatar(
                        original = "original.jpg"
                ),
                endpoint = FAVORITE_CHARLEZARD.region.endpoint,
                mains = listOf(
                        SmashCharacter.SHEIK
                ),
                websites = mapOf(
                        Constants.TWITCH to "https://twitch.tv/smcharles"
                ),
                id = FAVORITE_CHARLEZARD.id,
                name = "Charles Madere",
                tag = FAVORITE_CHARLEZARD.name
        )
    }

}
