package com.garpr.android.models

import com.garpr.android.BaseTest
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Collections
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class AbsTournamentTest : BaseTest() {

    private lateinit var fullTournament: AbsTournament
    private lateinit var liteTournament1: AbsTournament
    private lateinit var liteTournament2: AbsTournament
    private lateinit var liteTournament3: AbsTournament

    @Inject
    protected lateinit var mGson: Gson


    companion object {
        private const val JSON_FULL_TOURNAMENT = "{\"name\":\"Melee @ the Made 23\",\"players\":[{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\"},{\"id\":\"5877eb55d2994e15c7dea97e\",\"name\":\"Spark\"},{\"id\":\"587a951dd2994e15c7deaa00\",\"name\":\"Darrell\"},{\"id\":\"5877eb55d2994e15c7dea970\",\"name\":\"Boulevard\"},{\"id\":\"587a951dd2994e15c7dea9e4\",\"name\":\"Zorc\"},{\"id\":\"587a951dd2994e15c7dea9ec\",\"name\":\"BrTarolg\"},{\"id\":\"587a951dd2994e15c7dea9ff\",\"name\":\"Tucan\"},{\"id\":\"5877eb55d2994e15c7dea98b\",\"name\":\"Imyt\"},{\"id\":\"587a951dd2994e15c7dea9fa\",\"name\":\"Grandmas Cleavage\"},{\"id\":\"587a951dd2994e15c7dea9e9\",\"name\":\"TrueDong\"},{\"id\":\"587a951dd2994e15c7dea9fb\",\"name\":\"Branchamp\"},{\"id\":\"587a951dd2994e15c7dea9de\",\"name\":\"Rymo\"},{\"id\":\"587a951dd2994e15c7dea9e0\",\"name\":\"ShaqFu\"},{\"id\":\"587a951dd2994e15c7dea9e5\",\"name\":\"Yasu\"},{\"id\":\"587a951dd2994e15c7dea9fe\",\"name\":\"Charlezard\"},{\"id\":\"587a951dd2994e15c7dea9f7\",\"name\":\"Dragmire Jr\"},{\"id\":\"5877eb55d2994e15c7dea97a\",\"name\":\"Deadstorm\"},{\"id\":\"587a951dd2994e15c7dea9f8\",\"name\":\"asianson\"},{\"id\":\"587a951dd2994e15c7dea9e1\",\"name\":\"TheRedSock\"},{\"id\":\"587a951dd2994e15c7dea9f1\",\"name\":\"DestinyFan21\"},{\"id\":\"587a951dd2994e15c7dea9fc\",\"name\":\"KvillSniper\"},{\"id\":\"587a951dd2994e15c7dea9e2\",\"name\":\"Betus\"},{\"id\":\"583a4a15d2994e0577b05c77\",\"name\":\"Megaman\"},{\"id\":\"5877eb55d2994e15c7dea96e\",\"name\":\"Young $\"},{\"id\":\"587a951dd2994e15c7dea9fd\",\"name\":\"CarrierPig\"},{\"id\":\"587a951dd2994e15c7dea9e3\",\"name\":\"Beer\"},{\"id\":\"587a951dd2994e15c7dea9ef\",\"name\":\"QT3.14\"},{\"id\":\"587a951dd2994e15c7dea9f3\",\"name\":\"buddyboy\"},{\"id\":\"587a951dd2994e15c7dea9f9\",\"name\":\"Untitled\"},{\"id\":\"587a951dd2994e15c7dea9df\",\"name\":\"666\"},{\"id\":\"587a951dd2994e15c7dea9ea\",\"name\":\"mcmintymentos\"},{\"id\":\"587a951dd2994e15c7dea9e8\",\"name\":\"Holden\"},{\"id\":\"587a951dd2994e15c7dea9e7\",\"name\":\"Ross\"},{\"id\":\"587a951dd2994e15c7dea9f6\",\"name\":\"SlugNasty\"},{\"id\":\"587a951dd2994e15c7dea9e6\",\"name\":\"SlyCo\"},{\"id\":\"587a951dd2994e15c7deaa01\",\"name\":\"Thomdore\"},{\"id\":\"587a951dd2994e15c7dea9f5\",\"name\":\"Kwang\"},{\"id\":\"587894e9d2994e15c7dea9c7\",\"name\":\"SpacePigeon\"},{\"id\":\"587a951dd2994e15c7dea9f2\",\"name\":\"mar\"},{\"id\":\"587a951dd2994e15c7dea9eb\",\"name\":\"DarkSilence\"},{\"id\":\"587a951dd2994e15c7dea9f4\",\"name\":\"ALLNITTE\"},{\"id\":\"587a951dd2994e15c7dea9f0\",\"name\":\"The D\"},{\"id\":\"587a951dd2994e15c7dea9ee\",\"name\":\"oldbaby\"}],\"url\":\"http://challonge.com/MADE23Singless\",\"regions\":[\"norcal\"],\"matches\":[{\"loser_name\":\"Ross\",\"match_id\":0,\"winner_id\":\"587a951dd2994e15c7dea9e8\",\"winner_name\":\"Holden\",\"loser_id\":\"587a951dd2994e15c7dea9e7\",\"excluded\":false},{\"loser_name\":\"DarkSilence\",\"match_id\":1,\"winner_id\":\"587a951dd2994e15c7dea9fd\",\"winner_name\":\"CarrierPig\",\"loser_id\":\"587a951dd2994e15c7dea9eb\",\"excluded\":false},{\"loser_name\":\"ALLNITTE\",\"match_id\":2,\"winner_id\":\"5877eb55d2994e15c7dea96e\",\"winner_name\":\"Young $\",\"loser_id\":\"587a951dd2994e15c7dea9f4\",\"excluded\":false},{\"loser_name\":\"Thomdore\",\"match_id\":3,\"winner_id\":\"587a951dd2994e15c7dea9f9\",\"winner_name\":\"Untitled\",\"loser_id\":\"587a951dd2994e15c7deaa01\",\"excluded\":false},{\"loser_name\":\"Kwang\",\"match_id\":4,\"winner_id\":\"587a951dd2994e15c7dea9f3\",\"winner_name\":\"buddyboy\",\"loser_id\":\"587a951dd2994e15c7dea9f5\",\"excluded\":false},{\"loser_name\":\"mcmintymentos\",\"match_id\":5,\"winner_id\":\"587a951dd2994e15c7dea9f6\",\"winner_name\":\"SlugNasty\",\"loser_id\":\"587a951dd2994e15c7dea9ea\",\"excluded\":false},{\"loser_name\":\"Beer\",\"match_id\":6,\"winner_id\":\"587a951dd2994e15c7dea9f2\",\"winner_name\":\"mar\",\"loser_id\":\"587a951dd2994e15c7dea9e3\",\"excluded\":false},{\"loser_name\":\"Megaman\",\"match_id\":7,\"winner_id\":\"587a951dd2994e15c7dea9f0\",\"winner_name\":\"The D\",\"loser_id\":\"583a4a15d2994e0577b05c77\",\"excluded\":false},{\"loser_name\":\"SlyCo\",\"match_id\":8,\"winner_id\":\"587a951dd2994e15c7dea9df\",\"winner_name\":\"666\",\"loser_id\":\"587a951dd2994e15c7dea9e6\",\"excluded\":false},{\"loser_name\":\"QT3.14\",\"match_id\":9,\"winner_id\":\"587894e9d2994e15c7dea9c7\",\"winner_name\":\"SpacePigeon\",\"loser_id\":\"587a951dd2994e15c7dea9ef\",\"excluded\":false},{\"loser_name\":\"oldbaby\",\"match_id\":10,\"winner_id\":\"587a951dd2994e15c7dea9e2\",\"winner_name\":\"Betus\",\"loser_id\":\"587a951dd2994e15c7dea9ee\",\"excluded\":false},{\"loser_name\":\"Holden\",\"match_id\":11,\"winner_id\":\"583a4a15d2994e0577b05c74\",\"winner_name\":\"homemadewaffles\",\"loser_id\":\"587a951dd2994e15c7dea9e8\",\"excluded\":false},{\"loser_name\":\"Dragmire Jr\",\"match_id\":12,\"winner_id\":\"5877eb55d2994e15c7dea97a\",\"winner_name\":\"Deadstorm\",\"loser_id\":\"587a951dd2994e15c7dea9f7\",\"excluded\":false},{\"loser_name\":\"CarrierPig\",\"match_id\":13,\"winner_id\":\"5877eb55d2994e15c7dea98b\",\"winner_name\":\"Imyt\",\"loser_id\":\"587a951dd2994e15c7dea9fd\",\"excluded\":false},{\"loser_name\":\"Young $\",\"match_id\":14,\"winner_id\":\"587a951dd2994e15c7dea9fa\",\"winner_name\":\"Grandmas Cleavage\",\"loser_id\":\"5877eb55d2994e15c7dea96e\",\"excluded\":false},{\"loser_name\":\"Untitled\",\"match_id\":15,\"winner_id\":\"5877eb55d2994e15c7dea970\",\"winner_name\":\"Boulevard\",\"loser_id\":\"587a951dd2994e15c7dea9f9\",\"excluded\":false},{\"loser_name\":\"DestinyFan21\",\"match_id\":16,\"winner_id\":\"587a951dd2994e15c7dea9e0\",\"winner_name\":\"ShaqFu\",\"loser_id\":\"587a951dd2994e15c7dea9f1\",\"excluded\":false},{\"loser_name\":\"buddyboy\",\"match_id\":17,\"winner_id\":\"587a951dd2994e15c7dea9e4\",\"winner_name\":\"Zorc\",\"loser_id\":\"587a951dd2994e15c7dea9f3\",\"excluded\":false},{\"loser_name\":\"KvillSniper\",\"match_id\":18,\"winner_id\":\"587a951dd2994e15c7dea9de\",\"winner_name\":\"Rymo\",\"loser_id\":\"587a951dd2994e15c7dea9fc\",\"excluded\":false},{\"loser_name\":\"SlugNasty\",\"match_id\":19,\"winner_id\":\"5877eb55d2994e15c7dea97e\",\"winner_name\":\"Spark\",\"loser_id\":\"587a951dd2994e15c7dea9f6\",\"excluded\":false},{\"loser_name\":\"asianson\",\"match_id\":20,\"winner_id\":\"587a951dd2994e15c7dea9fe\",\"winner_name\":\"Charlezard\",\"loser_id\":\"587a951dd2994e15c7dea9f8\",\"excluded\":false},{\"loser_name\":\"mar\",\"match_id\":21,\"winner_id\":\"587a951dd2994e15c7dea9ff\",\"winner_name\":\"Tucan\",\"loser_id\":\"587a951dd2994e15c7dea9f2\",\"excluded\":false},{\"loser_name\":\"The D\",\"match_id\":22,\"winner_id\":\"587a951dd2994e15c7dea9e9\",\"winner_name\":\"TrueDong\",\"loser_id\":\"587a951dd2994e15c7dea9f0\",\"excluded\":false},{\"loser_name\":\"666\",\"match_id\":23,\"winner_id\":\"587a951dd2994e15c7deaa00\",\"winner_name\":\"Darrell\",\"loser_id\":\"587a951dd2994e15c7dea9df\",\"excluded\":false},{\"loser_name\":\"TheRedSock\",\"match_id\":24,\"winner_id\":\"587a951dd2994e15c7dea9e5\",\"winner_name\":\"Yasu\",\"loser_id\":\"587a951dd2994e15c7dea9e1\",\"excluded\":false},{\"loser_name\":\"SpacePigeon\",\"match_id\":25,\"winner_id\":\"587a951dd2994e15c7dea9ec\",\"winner_name\":\"BrTarolg\",\"loser_id\":\"587894e9d2994e15c7dea9c7\",\"excluded\":false},{\"loser_name\":\"Betus\",\"match_id\":26,\"winner_id\":\"587a951dd2994e15c7dea9fb\",\"winner_name\":\"Branchamp\",\"loser_id\":\"587a951dd2994e15c7dea9e2\",\"excluded\":false},{\"loser_name\":\"Deadstorm\",\"match_id\":27,\"winner_id\":\"583a4a15d2994e0577b05c74\",\"winner_name\":\"homemadewaffles\",\"loser_id\":\"5877eb55d2994e15c7dea97a\",\"excluded\":false},{\"loser_name\":\"Grandmas Cleavage\",\"match_id\":28,\"winner_id\":\"5877eb55d2994e15c7dea98b\",\"winner_name\":\"Imyt\",\"loser_id\":\"587a951dd2994e15c7dea9fa\",\"excluded\":false},{\"loser_name\":\"ShaqFu\",\"match_id\":29,\"winner_id\":\"5877eb55d2994e15c7dea970\",\"winner_name\":\"Boulevard\",\"loser_id\":\"587a951dd2994e15c7dea9e0\",\"excluded\":false},{\"loser_name\":\"Rymo\",\"match_id\":30,\"winner_id\":\"587a951dd2994e15c7dea9e4\",\"winner_name\":\"Zorc\",\"loser_id\":\"587a951dd2994e15c7dea9de\",\"excluded\":false},{\"loser_name\":\"Charlezard\",\"match_id\":31,\"winner_id\":\"5877eb55d2994e15c7dea97e\",\"winner_name\":\"Spark\",\"loser_id\":\"587a951dd2994e15c7dea9fe\",\"excluded\":false},{\"loser_name\":\"TrueDong\",\"match_id\":32,\"winner_id\":\"587a951dd2994e15c7dea9ff\",\"winner_name\":\"Tucan\",\"loser_id\":\"587a951dd2994e15c7dea9e9\",\"excluded\":false},{\"loser_name\":\"Yasu\",\"match_id\":33,\"winner_id\":\"587a951dd2994e15c7deaa00\",\"winner_name\":\"Darrell\",\"loser_id\":\"587a951dd2994e15c7dea9e5\",\"excluded\":false},{\"loser_name\":\"Branchamp\",\"match_id\":34,\"winner_id\":\"587a951dd2994e15c7dea9ec\",\"winner_name\":\"BrTarolg\",\"loser_id\":\"587a951dd2994e15c7dea9fb\",\"excluded\":false},{\"loser_name\":\"Imyt\",\"match_id\":35,\"winner_id\":\"583a4a15d2994e0577b05c74\",\"winner_name\":\"homemadewaffles\",\"loser_id\":\"5877eb55d2994e15c7dea98b\",\"excluded\":false},{\"loser_name\":\"Zorc\",\"match_id\":36,\"winner_id\":\"5877eb55d2994e15c7dea970\",\"winner_name\":\"Boulevard\",\"loser_id\":\"587a951dd2994e15c7dea9e4\",\"excluded\":false},{\"loser_name\":\"Tucan\",\"match_id\":37,\"winner_id\":\"5877eb55d2994e15c7dea97e\",\"winner_name\":\"Spark\",\"loser_id\":\"587a951dd2994e15c7dea9ff\",\"excluded\":false},{\"loser_name\":\"BrTarolg\",\"match_id\":38,\"winner_id\":\"587a951dd2994e15c7deaa00\",\"winner_name\":\"Darrell\",\"loser_id\":\"587a951dd2994e15c7dea9ec\",\"excluded\":false},{\"loser_name\":\"homemadewaffles\",\"match_id\":39,\"winner_id\":\"5877eb55d2994e15c7dea970\",\"winner_name\":\"Boulevard\",\"loser_id\":\"583a4a15d2994e0577b05c74\",\"excluded\":false},{\"loser_name\":\"Darrell\",\"match_id\":40,\"winner_id\":\"5877eb55d2994e15c7dea97e\",\"winner_name\":\"Spark\",\"loser_id\":\"587a951dd2994e15c7deaa00\",\"excluded\":false},{\"loser_name\":\"Boulevard\",\"match_id\":41,\"winner_id\":\"5877eb55d2994e15c7dea97e\",\"winner_name\":\"Spark\",\"loser_id\":\"5877eb55d2994e15c7dea970\",\"excluded\":false},{\"loser_name\":\"Ross\",\"match_id\":42,\"winner_id\":\"587a951dd2994e15c7dea9e2\",\"winner_name\":\"Betus\",\"loser_id\":\"587a951dd2994e15c7dea9e7\",\"excluded\":false},{\"loser_name\":\"DarkSilence\",\"match_id\":43,\"winner_id\":\"587a951dd2994e15c7dea9e1\",\"winner_name\":\"TheRedSock\",\"loser_id\":\"587a951dd2994e15c7dea9eb\",\"excluded\":false},{\"loser_name\":\"ALLNITTE\",\"match_id\":44,\"winner_id\":\"587a951dd2994e15c7dea9df\",\"winner_name\":\"666\",\"loser_id\":\"587a951dd2994e15c7dea9f4\",\"excluded\":false},{\"loser_name\":\"The D\",\"match_id\":45,\"winner_id\":\"587a951dd2994e15c7deaa01\",\"winner_name\":\"Thomdore\",\"loser_id\":\"587a951dd2994e15c7dea9f0\",\"excluded\":false},{\"loser_name\":\"Kwang\",\"match_id\":46,\"winner_id\":\"587a951dd2994e15c7dea9f8\",\"winner_name\":\"asianson\",\"loser_id\":\"587a951dd2994e15c7dea9f5\",\"excluded\":false},{\"loser_name\":\"mcmintymentos\",\"match_id\":47,\"winner_id\":\"587a951dd2994e15c7dea9fc\",\"winner_name\":\"KvillSniper\",\"loser_id\":\"587a951dd2994e15c7dea9ea\",\"excluded\":false},{\"loser_name\":\"Beer\",\"match_id\":48,\"winner_id\":\"587a951dd2994e15c7dea9f1\",\"winner_name\":\"DestinyFan21\",\"loser_id\":\"587a951dd2994e15c7dea9e3\",\"excluded\":false},{\"loser_name\":\"Megaman\",\"match_id\":49,\"winner_id\":\"587a951dd2994e15c7dea9f9\",\"winner_name\":\"Untitled\",\"loser_id\":\"583a4a15d2994e0577b05c77\",\"excluded\":false},{\"loser_name\":\"SlyCo\",\"match_id\":50,\"winner_id\":\"5877eb55d2994e15c7dea96e\",\"winner_name\":\"Young $\",\"loser_id\":\"587a951dd2994e15c7dea9e6\",\"excluded\":false},{\"loser_name\":\"QT3.14\",\"match_id\":51,\"winner_id\":\"587a951dd2994e15c7dea9f7\",\"winner_name\":\"Dragmire Jr\",\"loser_id\":\"587a951dd2994e15c7dea9ef\",\"excluded\":false},{\"loser_name\":\"oldbaby\",\"match_id\":52,\"winner_id\":\"587a951dd2994e15c7dea9e8\",\"winner_name\":\"Holden\",\"loser_id\":\"587a951dd2994e15c7dea9ee\",\"excluded\":false},{\"loser_name\":\"Betus\",\"match_id\":53,\"winner_id\":\"587894e9d2994e15c7dea9c7\",\"winner_name\":\"SpacePigeon\",\"loser_id\":\"587a951dd2994e15c7dea9e2\",\"excluded\":false},{\"loser_name\":\"666\",\"match_id\":54,\"winner_id\":\"587a951dd2994e15c7dea9e1\",\"winner_name\":\"TheRedSock\",\"loser_id\":\"587a951dd2994e15c7dea9df\",\"excluded\":false},{\"loser_name\":\"Thomdore\",\"match_id\":55,\"winner_id\":\"587a951dd2994e15c7dea9f2\",\"winner_name\":\"mar\",\"loser_id\":\"587a951dd2994e15c7deaa01\",\"excluded\":false},{\"loser_name\":\"SlugNasty\",\"match_id\":56,\"winner_id\":\"587a951dd2994e15c7dea9f8\",\"winner_name\":\"asianson\",\"loser_id\":\"587a951dd2994e15c7dea9f6\",\"excluded\":false},{\"loser_name\":\"buddyboy\",\"match_id\":57,\"winner_id\":\"587a951dd2994e15c7dea9fc\",\"winner_name\":\"KvillSniper\",\"loser_id\":\"587a951dd2994e15c7dea9f3\",\"excluded\":false},{\"loser_name\":\"DestinyFan21\",\"match_id\":58,\"winner_id\":\"587a951dd2994e15c7dea9f9\",\"winner_name\":\"Untitled\",\"loser_id\":\"587a951dd2994e15c7dea9f1\",\"excluded\":false},{\"loser_name\":\"Young $\",\"match_id\":59,\"winner_id\":\"587a951dd2994e15c7dea9fd\",\"winner_name\":\"CarrierPig\",\"loser_id\":\"5877eb55d2994e15c7dea96e\",\"excluded\":false},{\"loser_name\":\"Holden\",\"match_id\":60,\"winner_id\":\"587a951dd2994e15c7dea9f7\",\"winner_name\":\"Dragmire Jr\",\"loser_id\":\"587a951dd2994e15c7dea9e8\",\"excluded\":false},{\"loser_name\":\"SpacePigeon\",\"match_id\":61,\"winner_id\":\"587a951dd2994e15c7dea9de\",\"winner_name\":\"Rymo\",\"loser_id\":\"587894e9d2994e15c7dea9c7\",\"excluded\":false},{\"loser_name\":\"TheRedSock\",\"match_id\":62,\"winner_id\":\"587a951dd2994e15c7dea9e0\",\"winner_name\":\"ShaqFu\",\"loser_id\":\"587a951dd2994e15c7dea9e1\",\"excluded\":false},{\"loser_name\":\"mar\",\"match_id\":63,\"winner_id\":\"587a951dd2994e15c7dea9fa\",\"winner_name\":\"Grandmas Cleavage\",\"loser_id\":\"587a951dd2994e15c7dea9f2\",\"excluded\":false},{\"loser_name\":\"Deadstorm\",\"match_id\":64,\"winner_id\":\"587a951dd2994e15c7dea9f8\",\"winner_name\":\"asianson\",\"loser_id\":\"5877eb55d2994e15c7dea97a\",\"excluded\":false},{\"loser_name\":\"KvillSniper\",\"match_id\":65,\"winner_id\":\"587a951dd2994e15c7dea9fb\",\"winner_name\":\"Branchamp\",\"loser_id\":\"587a951dd2994e15c7dea9fc\",\"excluded\":false},{\"loser_name\":\"Yasu\",\"match_id\":66,\"winner_id\":\"587a951dd2994e15c7dea9f9\",\"winner_name\":\"Untitled\",\"loser_id\":\"587a951dd2994e15c7dea9e5\",\"excluded\":false},{\"loser_name\":\"CarrierPig\",\"match_id\":67,\"winner_id\":\"587a951dd2994e15c7dea9e9\",\"winner_name\":\"TrueDong\",\"loser_id\":\"587a951dd2994e15c7dea9fd\",\"excluded\":false},{\"loser_name\":\"Dragmire Jr\",\"match_id\":68,\"winner_id\":\"587a951dd2994e15c7dea9fe\",\"winner_name\":\"Charlezard\",\"loser_id\":\"587a951dd2994e15c7dea9f7\",\"excluded\":false},{\"loser_name\":\"Rymo\",\"match_id\":69,\"winner_id\":\"587a951dd2994e15c7dea9e0\",\"winner_name\":\"ShaqFu\",\"loser_id\":\"587a951dd2994e15c7dea9de\",\"excluded\":false},{\"loser_name\":\"asianson\",\"match_id\":70,\"winner_id\":\"587a951dd2994e15c7dea9fa\",\"winner_name\":\"Grandmas Cleavage\",\"loser_id\":\"587a951dd2994e15c7dea9f8\",\"excluded\":false},{\"loser_name\":\"Branchamp\",\"match_id\":71,\"winner_id\":\"587a951dd2994e15c7dea9f9\",\"winner_name\":\"Untitled\",\"loser_id\":\"587a951dd2994e15c7dea9fb\",\"excluded\":false},{\"loser_name\":\"Charlezard\",\"match_id\":72,\"winner_id\":\"587a951dd2994e15c7dea9e9\",\"winner_name\":\"TrueDong\",\"loser_id\":\"587a951dd2994e15c7dea9fe\",\"excluded\":false},{\"loser_name\":\"Tucan\",\"match_id\":73,\"winner_id\":\"587a951dd2994e15c7dea9e0\",\"winner_name\":\"ShaqFu\",\"loser_id\":\"587a951dd2994e15c7dea9ff\",\"excluded\":false},{\"loser_name\":\"Grandmas Cleavage\",\"match_id\":74,\"winner_id\":\"587a951dd2994e15c7dea9ec\",\"winner_name\":\"BrTarolg\",\"loser_id\":\"587a951dd2994e15c7dea9fa\",\"excluded\":false},{\"loser_name\":\"Imyt\",\"match_id\":75,\"winner_id\":\"587a951dd2994e15c7dea9f9\",\"winner_name\":\"Untitled\",\"loser_id\":\"5877eb55d2994e15c7dea98b\",\"excluded\":false},{\"loser_name\":\"TrueDong\",\"match_id\":76,\"winner_id\":\"587a951dd2994e15c7dea9e4\",\"winner_name\":\"Zorc\",\"loser_id\":\"587a951dd2994e15c7dea9e9\",\"excluded\":false},{\"loser_name\":\"ShaqFu\",\"match_id\":77,\"winner_id\":\"587a951dd2994e15c7dea9ec\",\"winner_name\":\"BrTarolg\",\"loser_id\":\"587a951dd2994e15c7dea9e0\",\"excluded\":false},{\"loser_name\":\"Untitled\",\"match_id\":78,\"winner_id\":\"587a951dd2994e15c7dea9e4\",\"winner_name\":\"Zorc\",\"loser_id\":\"587a951dd2994e15c7dea9f9\",\"excluded\":false},{\"loser_name\":\"BrTarolg\",\"match_id\":79,\"winner_id\":\"583a4a15d2994e0577b05c74\",\"winner_name\":\"homemadewaffles\",\"loser_id\":\"587a951dd2994e15c7dea9ec\",\"excluded\":false},{\"loser_name\":\"Darrell\",\"match_id\":80,\"winner_id\":\"587a951dd2994e15c7dea9e4\",\"winner_name\":\"Zorc\",\"loser_id\":\"587a951dd2994e15c7deaa00\",\"excluded\":false},{\"loser_name\":\"homemadewaffles\",\"match_id\":81,\"winner_id\":\"587a951dd2994e15c7dea9e4\",\"winner_name\":\"Zorc\",\"loser_id\":\"583a4a15d2994e0577b05c74\",\"excluded\":false},{\"loser_name\":\"Zorc\",\"match_id\":82,\"winner_id\":\"5877eb55d2994e15c7dea970\",\"winner_name\":\"Boulevard\",\"loser_id\":\"587a951dd2994e15c7dea9e4\",\"excluded\":false},{\"loser_name\":\"Spark\",\"match_id\":83,\"winner_id\":\"5877eb55d2994e15c7dea970\",\"winner_name\":\"Boulevard\",\"loser_id\":\"5877eb55d2994e15c7dea97e\",\"excluded\":false},{\"loser_name\":\"Boulevard\",\"match_id\":84,\"winner_id\":\"5877eb55d2994e15c7dea97e\",\"winner_name\":\"Spark\",\"loser_id\":\"5877eb55d2994e15c7dea970\",\"excluded\":false}],\"raw_id\":\"5888282dd2994e0d53b14558\",\"date\":\"01/13/17\",\"type\":\"challonge\",\"id\":\"5888282dd2994e0d53b14559\"}"

        private const val JSON_LITE_TOURNAMENT_1 = "{\"date\":\"01/05/17\",\"regions\":[\"norcal\"],\"id\":\"588827bad2994e0d53b14556\",\"name\":\"The Beat Down Ep.14\"}"
        private const val JSON_LITE_TOURNAMENT_2 = "{\"date\":\"02/21/17\",\"regions\":[\"norcal\"],\"id\":\"58ad3b1cd2994e756952adba\",\"name\":\"Get MADE at the Foundry\"}"
        private const val JSON_LITE_TOURNAMENT_3 = "{\"date\":\"07/09/17\",\"regions\":[\"norcal\"],\"id\":\"597d2903d2994e34028b4cc4\",\"name\":\"GENESIS: RED\"}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        fullTournament = mGson.fromJson(JSON_FULL_TOURNAMENT, AbsTournament::class.java)
        liteTournament1 = mGson.fromJson(JSON_LITE_TOURNAMENT_1, AbsTournament::class.java)
        liteTournament2 = mGson.fromJson(JSON_LITE_TOURNAMENT_2, AbsTournament::class.java)
        liteTournament3 = mGson.fromJson(JSON_LITE_TOURNAMENT_3, AbsTournament::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testComparatorChronologicalOrder() {
        val list = listOf(liteTournament2, fullTournament, liteTournament3, liteTournament1)

        Collections.sort(list, AbsTournament.CHRONOLOGICAL_ORDER)
        assertEquals(liteTournament1, list[0])
        assertEquals(fullTournament, list[1])
        assertEquals(liteTournament2, list[2])
        assertEquals(liteTournament3, list[3])
    }

    @Test
    @Throws(Exception::class)
    fun testComparatorReverseChronologicalOrder() {
        val list = listOf(liteTournament2, fullTournament, liteTournament3, liteTournament1)

        Collections.sort(list, AbsTournament.REVERSE_CHRONOLOGICAL_ORDER)
        assertEquals(liteTournament3, list[0])
        assertEquals(liteTournament2, list[1])
        assertEquals(fullTournament, list[2])
        assertEquals(liteTournament1, list[3])
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonFullTournament() {
        assertEquals("Melee @ the Made 23", fullTournament.name)
        assertEquals("5888282dd2994e0d53b14559", fullTournament.id)
        assertEquals(AbsTournament.Kind.FULL, fullTournament.kind)
        assertTrue(fullTournament is FullTournament)

        val fullTournament = fullTournament as FullTournament
        assertTrue(fullTournament.matches?.isNotEmpty() == true)
        assertTrue(fullTournament.players?.isNotEmpty() == true)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonLiteTournament1() {
        assertEquals("The Beat Down Ep.14", liteTournament1.name)
        assertEquals("588827bad2994e0d53b14556", liteTournament1.id)
        assertEquals(AbsTournament.Kind.LITE, liteTournament1.kind)
        assertTrue(liteTournament1 is LiteTournament)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonLiteTournament2() {
        assertEquals("Get MADE at the Foundry", liteTournament2.name)
        assertEquals("58ad3b1cd2994e756952adba", liteTournament2.id)
        assertEquals(AbsTournament.Kind.LITE, liteTournament2.kind)
        assertTrue(liteTournament2 is LiteTournament)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonLiteTournament3() {
        assertEquals("GENESIS: RED", liteTournament3.name)
        assertEquals("597d2903d2994e34028b4cc4", liteTournament3.id)
        assertEquals(AbsTournament.Kind.LITE, liteTournament3.kind)
        assertTrue(liteTournament3 is LiteTournament)
    }

    @Test
    @Throws(Exception::class)
    fun testFromNull() {
        val tournament = mGson.fromJson(null as String?, AbsTournament::class.java)
        assertNull(tournament)
    }

}
