package com.garpr.android.misc;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.AbsTournament;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.models.Ranking;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.TournamentsBundle;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ListUtilsTest extends BaseTest {

    private static final String JSON_PLAYERS_BUNDLE = "{\"players\":[{\"ratings\":{\"norcal\":{\"mu\":35.642969629233434,\"sigma\":4.442569422290421}},\"name\":\"[A] | Android\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63c0a\"],\"id\":\"588999c5d2994e713ad63c0a\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":50.96457294276916,\"sigma\":2.9997629831017263}},\"name\":\"[A] | Armada\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63cac\"],\"id\":\"588999c5d2994e713ad63cac\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":28.611625678948517,\"sigma\":2.280982758285615}},\"name\":\"Charlezard\",\"regions\":[\"norcal\"],\"merge_children\":[\"587a951dd2994e15c7dea9fe\"],\"id\":\"587a951dd2994e15c7dea9fe\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"googlemtv\":{\"mu\":37.05546025182014,\"sigma\":2.0824461049194727},\"norcal\":{\"mu\":37.50056424808538,\"sigma\":2.011561699174561}},\"name\":\"gaR\",\"regions\":[\"norcal\",\"googlemtv\"],\"merge_children\":[\"58523b44d2994e15c7dea945\"],\"id\":\"58523b44d2994e15c7dea945\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":21.059537685630445,\"sigma\":4.151582405998713}},\"name\":\"Gargito\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63c9f\"],\"id\":\"588999c5d2994e713ad63c9f\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":21.250083492004936,\"sigma\":5.49086667542448}},\"name\":\"Garou\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63a6a\"],\"id\":\"588999c5d2994e713ad63a6a\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":18.51002707111179,\"sigma\":5.978660334849401}},\"name\":\"Gart\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63cba\"],\"id\":\"588999c5d2994e713ad63cba\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.998002359888382,\"sigma\":6.321198783453136}},\"name\":\"GC | D2NG\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63cd4\"],\"id\":\"588999c5d2994e713ad63cd4\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":34.15571825215487,\"sigma\":3.7620068285447914}},\"name\":\"GC | | Chip\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63c35\"],\"id\":\"588999c5d2994e713ad63c35\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":27.518790714839767,\"sigma\":4.875442201831361}},\"name\":\"gct3\",\"regions\":[\"norcal\"],\"merge_children\":[\"583a4a15d2994e0577b05c8f\"],\"id\":\"583a4a15d2994e0577b05c8f\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":28.996881123103265,\"sigma\":3.3823888253266685}},\"name\":\"GDO | Buttlet\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63a58\"],\"id\":\"588999c5d2994e713ad63a58\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":22.088629843642124,\"sigma\":3.6324173267118995}},\"name\":\"GDO | Corduroy\",\"regions\":[\"norcal\"],\"merge_children\":[\"58a007dcd2994e4d0f2e25af\"],\"id\":\"58a007dcd2994e4d0f2e25af\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":24.656464958839347,\"sigma\":2.917133044631605}},\"name\":\"GDO | CrazyOtamatoneKid249\",\"regions\":[\"norcal\"],\"merge_children\":[\"588852e8d2994e3bbfa52da2\"],\"id\":\"588852e8d2994e3bbfa52da2\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":26.663714673326623,\"sigma\":2.6022914924272853}},\"name\":\"GDO | The Trashman\",\"regions\":[\"norcal\"],\"merge_children\":[\"588852e8d2994e3bbfa52db4\"],\"id\":\"588852e8d2994e3bbfa52db4\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.998002359888382,\"sigma\":6.321198783453136}},\"name\":\"GeeSlice\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63cab\"],\"id\":\"588999c5d2994e713ad63cab\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":22.82816325587265,\"sigma\":5.05521632404939}},\"name\":\"Gemini\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad63818\"],\"id\":\"588999c4d2994e713ad63818\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":22.246223637560476,\"sigma\":4.724484111007406}},\"name\":\"Genghis Fong\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63c4a\"],\"id\":\"588999c5d2994e713ad63c4a\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.998002359888382,\"sigma\":6.321198783453136}},\"name\":\"Geodude\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63965\"],\"id\":\"588999c5d2994e713ad63965\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":25.295658247214362,\"sigma\":4.717223800144487}},\"name\":\"George Washington\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63cc5\"],\"id\":\"588999c5d2994e713ad63cc5\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":33.412156713831756,\"sigma\":3.4323916922293956}},\"name\":\"Geruyop\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63a8e\"],\"id\":\"588999c5d2994e713ad63a8e\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":28.93803812306148,\"sigma\":4.2101902240304785}},\"name\":\"gg EGtv | Calvin\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63a8a\"],\"id\":\"588999c5d2994e713ad63a8a\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":22.43550097559226,\"sigma\":5.544893866413979}},\"name\":\"gg | Chuck Lee\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad6377a\"],\"id\":\"588999c4d2994e713ad6377a\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":29.203661929132597,\"sigma\":4.69625986822465}},\"name\":\"GG | i4n\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63c13\"],\"id\":\"588999c5d2994e713ad63c13\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":16.927317836767397,\"sigma\":6.239835843329803}},\"name\":\"gg | JimmyNoJohns\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad637de\"],\"id\":\"588999c4d2994e713ad637de\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":26.887991175711957,\"sigma\":4.604656659052483}},\"name\":\"gg | Minz\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad637cc\"],\"id\":\"588999c4d2994e713ad637cc\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":16.927317836767397,\"sigma\":6.239835843329803}},\"name\":\"gg | Ogle\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63b1b\"],\"id\":\"588999c5d2994e713ad63b1b\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":19.419422494971876,\"sigma\":6.362447013454361}},\"name\":\"gg | Suck\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63a93\"],\"id\":\"588999c5d2994e713ad63a93\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":21.573711603718827,\"sigma\":4.610007744568868}},\"name\":\"gg | UW | leahboo\",\"regions\":[\"norcal\"],\"merge_children\":[\"588852e8d2994e3bbfa52d96\"],\"id\":\"588852e8d2994e3bbfa52d96\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":21.66666562007478,\"sigma\":5.490063613903319}},\"name\":\"GG | Zephyr\",\"regions\":[\"norcal\"],\"merge_children\":[\"588852e8d2994e3bbfa52da0\"],\"id\":\"588852e8d2994e3bbfa52da0\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":34.38707433262756,\"sigma\":3.9674207064150324}},\"name\":\"gg.MIOM.BBeS.TMG | MattDotZeb\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad6394a\"],\"id\":\"588999c5d2994e713ad6394a\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":25.588528947970815,\"sigma\":4.740284816979575}},\"name\":\"GhettoTastic\",\"regions\":[\"norcal\"],\"merge_children\":[\"588ec13fd2994e6485b3d551\"],\"id\":\"588ec13fd2994e6485b3d551\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":25.82452523122893,\"sigma\":3.825228351995281}},\"name\":\"Ghidorah\",\"regions\":[\"norcal\"],\"merge_children\":[\"588852e8d2994e3bbfa52db7\"],\"id\":\"588852e8d2994e3bbfa52db7\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.15733291489348,\"sigma\":6.26923433457268}},\"name\":\"Ghoul\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63ca3\"],\"id\":\"588999c5d2994e713ad63ca3\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.827000552409757,\"sigma\":6.293886154726804}},\"name\":\"GHQ | Daddy\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63966\"],\"id\":\"588999c5d2994e713ad63966\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":32.00139209827007,\"sigma\":4.125028233408192}},\"name\":\"GHQ | JackKenney\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad6393e\"],\"id\":\"588999c4d2994e713ad6393e\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":38.640035465130616,\"sigma\":3.76830947878785}},\"name\":\"GHQ | Kels\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63aa0\"],\"id\":\"588999c5d2994e713ad63aa0\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":32.270205428255565,\"sigma\":3.9482777741313346}},\"name\":\"GHQ | Nox\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63b8a\"],\"id\":\"588999c5d2994e713ad63b8a\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":22.098537586957033,\"sigma\":5.43499729999703}},\"name\":\"Gimli\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad63901\"],\"id\":\"588999c4d2994e713ad63901\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.827000552409757,\"sigma\":6.293886154726804}},\"name\":\"Gin\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63b81\"],\"id\":\"588999c5d2994e713ad63b81\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":22.295063003334835,\"sigma\":5.458819545072157}},\"name\":\"gin x\",\"regions\":[\"norcal\"],\"merge_children\":[\"583a4a15d2994e0577b05c86\"],\"id\":\"583a4a15d2994e0577b05c86\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.333809273137486,\"sigma\":6.0381433836907155}},\"name\":\"GING\",\"regions\":[\"norcal\"],\"merge_children\":[\"5888542ad2994e3bbfa52e02\"],\"id\":\"5888542ad2994e3bbfa52e02\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":33.88720670623763,\"sigma\":4.069513181400047}},\"name\":\"Giovonni\",\"regions\":[\"norcal\"],\"merge_children\":[\"5877ec16d2994e15c7dea9ad\"],\"id\":\"5877ec16d2994e15c7dea9ad\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":23.0719360808223,\"sigma\":3.9946507611236504}},\"name\":\"Girony\",\"regions\":[\"norcal\"],\"merge_children\":[\"5888542ad2994e3bbfa52de1\"],\"id\":\"5888542ad2994e3bbfa52de1\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.19471345275092,\"sigma\":6.26133931308351}},\"name\":\"gkinfinity\",\"regions\":[\"norcal\"],\"merge_children\":[\"588852e8d2994e3bbfa52d97\"],\"id\":\"588852e8d2994e3bbfa52d97\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":19.026721224054874,\"sigma\":6.416665602401265}},\"name\":\"Glenjamin\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63985\"],\"id\":\"588999c5d2994e713ad63985\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"googlemtv\":{\"mu\":16.927317836767397,\"sigma\":6.239835843329803}},\"name\":\"Glenn\",\"regions\":[\"googlemtv\"],\"merge_children\":[\"58523b44d2994e15c7dea944\"],\"id\":\"58523b44d2994e15c7dea944\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":34.51639233659131,\"sigma\":3.425060213773536}},\"name\":\"GLORIOUS | Atomsk\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63bcb\"],\"id\":\"588999c5d2994e713ad63bcb\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":33.352609044251615,\"sigma\":1.774054476215711}},\"name\":\"gme\",\"regions\":[\"norcal\"],\"merge_children\":[\"58882af1d2994e0d53b14579\"],\"id\":\"58882af1d2994e0d53b14579\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":23.28154981128458,\"sigma\":5.324860188068914}},\"name\":\"Gnasher-\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad639d6\"],\"id\":\"588999c5d2994e713ad639d6\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":16.18416685517358,\"sigma\":6.097238747431597}},\"name\":\"goblin\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63c11\"],\"id\":\"588999c5d2994e713ad63c11\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":22.76412449549114,\"sigma\":5.525730930570492}},\"name\":\"Godzillaz\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63c8b\"],\"id\":\"588999c5d2994e713ad63c8b\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":27.942967295979784,\"sigma\":4.467339385373452}},\"name\":\"Gold\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63cd6\"],\"id\":\"588999c5d2994e713ad63cd6\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.608823234388826,\"sigma\":6.234439914796083}},\"name\":\"golf\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63ac3\"],\"id\":\"588999c5d2994e713ad63ac3\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.483566175370257,\"sigma\":6.249411007915249}},\"name\":\"Goliath\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad63775\"],\"id\":\"588999c4d2994e713ad63775\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":29.24809523740608,\"sigma\":4.619592158888176}},\"name\":\"Goo\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63aa5\"],\"id\":\"588999c5d2994e713ad63aa5\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":34.51493047312897,\"sigma\":2.0808969913058615}},\"name\":\"Goose\",\"regions\":[\"norcal\"],\"merge_children\":[\"5877ec16d2994e15c7dea9b6\"],\"id\":\"5877ec16d2994e15c7dea9b6\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":14.298634646351898,\"sigma\":4.963375792137797}},\"name\":\"Gopu\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63c97\"],\"id\":\"588999c5d2994e713ad63c97\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":16.630059563209176,\"sigma\":5.537717082068906}},\"name\":\"Gutiger\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63ace\"],\"id\":\"588999c5d2994e713ad63ace\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":20.345279138371012,\"sigma\":4.6785637833551705}},\"name\":\"Guts\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad638c0\"],\"id\":\"588999c4d2994e713ad638c0\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":21.42620495210016,\"sigma\":5.474656985162472}},\"name\":\"GW | Phlops\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad63935\"],\"id\":\"588999c4d2994e713ad63935\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.004107999282727,\"sigma\":5.958252438562547}},\"name\":\"GW | YungBitch\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63a06\"],\"id\":\"588999c5d2994e713ad63a06\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":16.763989154786675,\"sigma\":5.82525280691389}},\"name\":\"Gworm\",\"regions\":[\"norcal\"],\"merge_children\":[\"588ec13fd2994e6485b3d54c\"],\"id\":\"588ec13fd2994e6485b3d54c\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":31.08442353600666,\"sigma\":4.0128123887220735}},\"name\":\"Habilecho\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad637ae\"],\"id\":\"588999c4d2994e713ad637ae\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":25.89978907701045,\"sigma\":3.2643816206998544}},\"name\":\"Hungdaddy12inches\",\"regions\":[\"norcal\"],\"merge_children\":[\"5877eb55d2994e15c7dea990\"],\"id\":\"5877eb55d2994e15c7dea990\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.167009598492413,\"sigma\":5.711697552513964}},\"name\":\"Hungry boy\",\"regions\":[\"norcal\"],\"merge_children\":[\"5896dee5d2994e73ee0b1732\"],\"id\":\"5896dee5d2994e73ee0b1732\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":21.070209471979933,\"sigma\":5.510021696061425}},\"name\":\"Hurricane\",\"regions\":[\"norcal\"],\"merge_children\":[\"583a4a15d2994e0577b05c83\"],\"id\":\"583a4a15d2994e0577b05c83\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":18.241228416563082,\"sigma\":6.360365204275829}},\"name\":\"hurtbox\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63c80\"],\"id\":\"588999c5d2994e713ad63c80\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":26.599266238023677,\"sigma\":4.89623578883907}},\"name\":\"Hyena | Lotus\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad63897\"],\"id\":\"588999c4d2994e713ad63897\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.859275969519288,\"sigma\":6.094503577301557}},\"name\":\"I am | Windul\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63b75\"],\"id\":\"588999c5d2994e713ad63b75\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":25.641135337219723,\"sigma\":5.180632593071609}},\"name\":\"I Seeded You | Dingus\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63ce1\"],\"id\":\"588999c5d2994e713ad63ce1\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":29.224094988808385,\"sigma\":4.47800798795124}},\"name\":\"IamCreditCard\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63b14\"],\"id\":\"588999c5d2994e713ad63b14\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.2745794834446,\"sigma\":6.108357955324915}},\"name\":\"ikeprewii\",\"regions\":[\"norcal\"],\"merge_children\":[\"58882af1d2994e0d53b14585\"],\"id\":\"58882af1d2994e0d53b14585\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":35.407036973663644,\"sigma\":3.626728490282389}},\"name\":\"iluvcrispix\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad6399e\"],\"id\":\"588999c5d2994e713ad6399e\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":22.346686250569842,\"sigma\":5.394967492243456}},\"name\":\"Imayswingby | Winky\",\"regions\":[\"norcal\"],\"merge_children\":[\"588852e8d2994e3bbfa52d86\"],\"id\":\"588852e8d2994e3bbfa52d86\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":18.21498036670931,\"sigma\":6.3225949326621995}},\"name\":\"Impact Series\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63957\"],\"id\":\"588999c5d2994e713ad63957\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.315313731482895,\"sigma\":6.16649288367582}},\"name\":\"Improv\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad638e7\"],\"id\":\"588999c4d2994e713ad638e7\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.998002359888382,\"sigma\":6.321198783453136}},\"name\":\"ImpulsE\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad6387e\"],\"id\":\"588999c4d2994e713ad6387e\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":40.933772493132935,\"sigma\":3.3622792474355845}},\"name\":\"IMT | Shroomed\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63a8c\"],\"id\":\"588999c5d2994e713ad63a8c\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":34.765512277774434,\"sigma\":1.635846675001654}},\"name\":\"Imyt\",\"regions\":[\"norcal\"],\"merge_children\":[\"5877eb55d2994e15c7dea98b\"],\"id\":\"5877eb55d2994e15c7dea98b\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":17.827000552409757,\"sigma\":6.293886154726804}},\"name\":\"Incronaut\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad6391d\"],\"id\":\"588999c4d2994e713ad6391d\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":31.064060991952996,\"sigma\":4.142811237193602}},\"name\":\"Indie\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad637b9\"],\"id\":\"588999c4d2994e713ad637b9\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":16.927317836767397,\"sigma\":6.239835843329803}},\"name\":\"IndoAznBoy\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63bf0\"],\"id\":\"588999c5d2994e713ad63bf0\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":35.99034852088388,\"sigma\":3.2650831040165302}},\"name\":\"InfernoJesus\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c5d2994e713ad63988\"],\"id\":\"588999c5d2994e713ad63988\",\"merged\":false,\"merge_parent\":null},{\"ratings\":{\"norcal\":{\"mu\":24.481284005579568,\"sigma\":5.0564354266315545}},\"name\":\"Info\",\"regions\":[\"norcal\"],\"merge_children\":[\"588999c4d2994e713ad63811\"],\"id\":\"588999c4d2994e713ad63811\",\"merged\":false,\"merge_parent\":null}]}";

    private static final String JSON_RANKINGS_BUNDLE = "{\"ranking\":[{\"id\":\"588852e8d2994e3bbfa52d88\",\"name\":\"CLG. | SFAT\",\"rank\":1,\"rating\":41.90311956349805},{\"id\":\"5877eb55d2994e15c7dea96d\",\"name\":\"dizzkidboogie\",\"rank\":2,\"rating\":41.86481698258525},{\"id\":\"583a4a15d2994e0577b05c8a\",\"name\":\"NMW\",\"rank\":3,\"rating\":38.59012267250557},{\"id\":\"5877eb55d2994e15c7dea970\",\"name\":\"Boulevard\",\"rank\":4,\"rating\":37.47969483713437},{\"id\":\"588852e8d2994e3bbfa52da6\",\"name\":\"SPY | Nintendude\",\"rank\":5,\"rating\":36.939863303391704},{\"id\":\"5877eb55d2994e15c7dea97e\",\"name\":\"Spark\",\"rank\":6,\"rating\":36.81398929804276},{\"id\":\"588852e8d2994e3bbfa52dc5\",\"name\":\"Kalamazhu\",\"rank\":7,\"rating\":36.35961949935507},{\"id\":\"588852e8d2994e3bbfa52dcf\",\"name\":\"SAB | Ralph\",\"rank\":8,\"rating\":35.472338678908045},{\"id\":\"588852e7d2994e3bbfa52d6e\",\"name\":\"Laudandus\",\"rank\":9,\"rating\":35.112668193966016},{\"id\":\"5890303cd2994e04409f19ff\",\"name\":\"Berto\",\"rank\":10,\"rating\":34.74308195595823},{\"id\":\"5888542ad2994e3bbfa52e0c\",\"name\":\"s0ft\",\"rank\":11,\"rating\":34.72856194803048},{\"id\":\"588852e8d2994e3bbfa52d8c\",\"name\":\"Azusa\",\"rank\":12,\"rating\":34.311379299384186},{\"id\":\"588999c5d2994e713ad63ac0\",\"name\":\"BarDulL\",\"rank\":13,\"rating\":34.11431176395806},{\"id\":\"588999c5d2994e713ad6398c\",\"name\":\"Phil\",\"rank\":14,\"rating\":33.99656872209344},{\"id\":\"588852e8d2994e3bbfa52dd8\",\"name\":\"Cactuar\",\"rank\":15,\"rating\":33.6715260029736},{\"id\":\"587a951dd2994e15c7deaa00\",\"name\":\"Darrell\",\"rank\":16,\"rating\":33.58531115918937},{\"id\":\"5888542ad2994e3bbfa52e2d\",\"name\":\"Reno\",\"rank\":17,\"rating\":33.425426844029786},{\"id\":\"5877eb55d2994e15c7dea977\",\"name\":\"Umarth\",\"rank\":18,\"rating\":33.38756606943736},{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\",\"rank\":19,\"rating\":33.158801726300666},{\"id\":\"588852e7d2994e3bbfa52d74\",\"name\":\"L\",\"rank\":20,\"rating\":33.07044631911386},{\"id\":\"588999c4d2994e713ad637f3\",\"name\":\"Frotaz\",\"rank\":21,\"rating\":32.74714094341646},{\"id\":\"5877eb55d2994e15c7dea982\",\"name\":\"Rocky\",\"rank\":22,\"rating\":32.63185174687976},{\"id\":\"5888542ad2994e3bbfa52de4\",\"name\":\"ycz6\",\"rank\":23,\"rating\":32.10157051912901},{\"id\":\"58523b44d2994e15c7dea945\",\"name\":\"gaR\",\"rank\":24,\"rating\":31.465879150561697},{\"id\":\"588852e8d2994e3bbfa52dc1\",\"name\":\"Moose\",\"rank\":25,\"rating\":31.306829676629192},{\"id\":\"5877eb55d2994e15c7dea986\",\"name\":\"Davis\",\"rank\":26,\"rating\":31.193598566735787},{\"id\":\"5877ec16d2994e15c7dea9af\",\"name\":\"Miguel\",\"rank\":27,\"rating\":30.394641635997427},{\"id\":\"5877eb55d2994e15c7dea981\",\"name\":\"Arcadia\",\"rank\":28,\"rating\":30.37763472949198},{\"id\":\"587a951dd2994e15c7dea9f9\",\"name\":\"Untitled\",\"rank\":29,\"rating\":30.098793086703846},{\"id\":\"5877eb55d2994e15c7dea98b\",\"name\":\"Imyt\",\"rank\":30,\"rating\":29.85797225276947},{\"id\":\"58882a58d2994e0d53b1456b\",\"name\":\"dragonslayer69\",\"rank\":31,\"rating\":29.729451184382143},{\"id\":\"587a951dd2994e15c7dea9e9\",\"name\":\"TrueDong\",\"rank\":32,\"rating\":29.476921132335427},{\"id\":\"587895e3d2994e15c7dea9d6\",\"name\":\"Weedlar\",\"rank\":33,\"rating\":29.234667287889874},{\"id\":\"588852e8d2994e3bbfa52d79\",\"name\":\"MIOM | Dr. Z\",\"rank\":34,\"rating\":29.155418578445193},{\"id\":\"588852e8d2994e3bbfa52daf\",\"name\":\"DiplomaticTucan\",\"rank\":35,\"rating\":28.678535885960915},{\"id\":\"588852e8d2994e3bbfa52dd1\",\"name\":\"Machiavelli\",\"rank\":36,\"rating\":28.527050706599937},{\"id\":\"588852e8d2994e3bbfa52dbd\",\"name\":\"Kevbot\",\"rank\":37,\"rating\":28.45615974422645},{\"id\":\"5877ec16d2994e15c7dea9b6\",\"name\":\"Goose\",\"rank\":38,\"rating\":28.272239499211388},{\"id\":\"588852e8d2994e3bbfa52dad\",\"name\":\"Groovy Green Hat\",\"rank\":39,\"rating\":28.26194499297746},{\"id\":\"58882a58d2994e0d53b14566\",\"name\":\"bliutwo\",\"rank\":40,\"rating\":28.15377863955963},{\"id\":\"587a951dd2994e15c7dea9de\",\"name\":\"Rymo\",\"rank\":41,\"rating\":28.14650031428953},{\"id\":\"58882af1d2994e0d53b14579\",\"name\":\"gme\",\"rank\":42,\"rating\":28.03044561560448},{\"id\":\"5888542ad2994e3bbfa52e1f\",\"name\":\"boback\",\"rank\":43,\"rating\":27.934496736182524},{\"id\":\"5877eb55d2994e15c7dea97f\",\"name\":\"+ultra\",\"rank\":44,\"rating\":27.911141375754845},{\"id\":\"588999c5d2994e713ad63c54\",\"name\":\"DMC | Kenji\",\"rank\":45,\"rating\":27.625198731837948},{\"id\":\"588852e8d2994e3bbfa52da3\",\"name\":\"Chris Best\",\"rank\":46,\"rating\":27.59941639418892},{\"id\":\"588999c4d2994e713ad63860\",\"name\":\"Warwick Foe\",\"rank\":47,\"rating\":27.587152907369113},{\"id\":\"5888542ad2994e3bbfa52e01\",\"name\":\"MIOM | Toph\",\"rank\":48,\"rating\":27.5830484714771},{\"id\":\"583a4a15d2994e0577b05c84\",\"name\":\"dkuo\",\"rank\":49,\"rating\":27.419346089882723},{\"id\":\"5877eb55d2994e15c7dea98a\",\"name\":\"Zan\",\"rank\":50,\"rating\":27.316074012103275},{\"id\":\"5877eb55d2994e15c7dea976\",\"name\":\"TED | khale\",\"rank\":51,\"rating\":27.164888013934615},{\"id\":\"588852e8d2994e3bbfa52dcc\",\"name\":\"Ace\",\"rank\":52,\"rating\":27.135441694899352},{\"id\":\"588852e8d2994e3bbfa52d98\",\"name\":\"drewgong\",\"rank\":53,\"rating\":27.133035239323448},{\"id\":\"588852e8d2994e3bbfa52d8d\",\"name\":\"TED | Lego\",\"rank\":54,\"rating\":27.060804918050586},{\"id\":\"583a4a15d2994e0577b05c82\",\"name\":\"Tang\",\"rank\":55,\"rating\":26.918527154399115},{\"id\":\"58882af1d2994e0d53b1457a\",\"name\":\"blargh257\",\"rank\":56,\"rating\":26.823021319825997},{\"id\":\"58523b44d2994e15c7dea94d\",\"name\":\"Admiral\",\"rank\":57,\"rating\":26.687290399500988},{\"id\":\"588999c5d2994e713ad63a7a\",\"name\":\"Rad\",\"rank\":58,\"rating\":26.609652741713326},{\"id\":\"588999c5d2994e713ad63ba7\",\"name\":\"Light\",\"rank\":59,\"rating\":26.44816240671382},{\"id\":\"58882a58d2994e0d53b14568\",\"name\":\"CG | AND1\",\"rank\":60,\"rating\":26.247252586456092},{\"id\":\"588999c5d2994e713ad6394b\",\"name\":\"Sizzle\",\"rank\":61,\"rating\":26.22162817233442},{\"id\":\"5877ec16d2994e15c7dea9a7\",\"name\":\"Brand\",\"rank\":62,\"rating\":25.92527294610376},{\"id\":\"58882bfcd2994e0d53b1458a\",\"name\":\"Kalas\",\"rank\":63,\"rating\":25.66771070767513},{\"id\":\"583a4a15d2994e0577b05c85\",\"name\":\"K27\",\"rank\":64,\"rating\":25.49426470186715},{\"id\":\"5896dee5d2994e73ee0b172f\",\"name\":\"Captain Falco\",\"rank\":65,\"rating\":25.221671145979037},{\"id\":\"5888542ad2994e3bbfa52dec\",\"name\":\"PRLS | TimeMuffinPhD\",\"rank\":66,\"rating\":25.210692365250793},{\"id\":\"5877eb55d2994e15c7dea98d\",\"name\":\"The Other | NaCl\",\"rank\":67,\"rating\":25.09794951110247},{\"id\":\"58882c9bd2994e0d53b14597\",\"name\":\"c4D\",\"rank\":68,\"rating\":25.032863871986784},{\"id\":\"58882a58d2994e0d53b14561\",\"name\":\"0kay\",\"rank\":69,\"rating\":25.006514106268725},{\"id\":\"588999c4d2994e713ad63908\",\"name\":\"Kami\",\"rank\":70,\"rating\":24.872521426652753},{\"id\":\"58882c9bd2994e0d53b1459a\",\"name\":\"Maruf\",\"rank\":71,\"rating\":24.812374171111635},{\"id\":\"588852e8d2994e3bbfa52d7c\",\"name\":\"LAB | PoeFire\",\"rank\":72,\"rating\":24.6029768026683},{\"id\":\"5896d560d2994e73ee0b1724\",\"name\":\"Jakx\",\"rank\":73,\"rating\":24.39027100181488},{\"id\":\"5877eb55d2994e15c7dea975\",\"name\":\"LTigre\",\"rank\":74,\"rating\":24.365384010028578},{\"id\":\"587a951dd2994e15c7dea9fb\",\"name\":\"Branchamp\",\"rank\":75,\"rating\":24.14236757052747},{\"id\":\"587a951dd2994e15c7dea9fa\",\"name\":\"Grandmas Cleavage\",\"rank\":76,\"rating\":24.12250200147919},{\"id\":\"588999c5d2994e713ad63c52\",\"name\":\"ORR | Young Traplord\",\"rank\":77,\"rating\":24.084796944605046},{\"id\":\"58523b44d2994e15c7dea93e\",\"name\":\"Bryan\",\"rank\":78,\"rating\":24.06765132235516},{\"id\":\"588999c5d2994e713ad63b98\",\"name\":\"Panic\",\"rank\":79,\"rating\":24.01765839370716},{\"id\":\"588850b3d2994e3bbfa52d64\",\"name\":\"darkwizard123\",\"rank\":80,\"rating\":23.988644306438985},{\"id\":\"5888542ad2994e3bbfa52e20\",\"name\":\"Mikkuz\",\"rank\":81,\"rating\":23.86917316217991},{\"id\":\"588850b3d2994e3bbfa52d57\",\"name\":\"DJSwerve\",\"rank\":82,\"rating\":23.804427220941218},{\"id\":\"5888542ad2994e3bbfa52dfb\",\"name\":\"Cyan\",\"rank\":83,\"rating\":23.80288893335893},{\"id\":\"5877eb55d2994e15c7dea979\",\"name\":\"Kyza\",\"rank\":84,\"rating\":23.76172711610702},{\"id\":\"58882c9bd2994e0d53b1459d\",\"name\":\"Spaceghost\",\"rank\":85,\"rating\":23.675667230212312},{\"id\":\"5877ec16d2994e15c7dea9ab\",\"name\":\"Trev\",\"rank\":86,\"rating\":23.65851830968801},{\"id\":\"5877eb55d2994e15c7dea972\",\"name\":\"Bruv\",\"rank\":87,\"rating\":23.55047078482081},{\"id\":\"588852e8d2994e3bbfa52dd5\",\"name\":\"Weston\",\"rank\":88,\"rating\":23.51741377976102},{\"id\":\"5877ec16d2994e15c7dea9b1\",\"name\":\"Gabe\",\"rank\":89,\"rating\":23.482730835257797},{\"id\":\"588852e8d2994e3bbfa52d9c\",\"name\":\"PG | Treble\",\"rank\":90,\"rating\":23.456402685445944},{\"id\":\"5877ec15d2994e15c7dea9a1\",\"name\":\"Maverick\",\"rank\":91,\"rating\":23.39027308129691},{\"id\":\"58882bfcd2994e0d53b14590\",\"name\":\"Zoap\",\"rank\":92,\"rating\":23.33483971179136},{\"id\":\"5877ec16d2994e15c7dea9ac\",\"name\":\"Soju\",\"rank\":93,\"rating\":23.331805454072892},{\"id\":\"588999c5d2994e713ad63c4c\",\"name\":\"Smish\",\"rank\":94,\"rating\":23.29658211175529},{\"id\":\"588852e7d2994e3bbfa52d75\",\"name\":\"Ahmad\",\"rank\":95,\"rating\":23.246509100652283},{\"id\":\"588852e8d2994e3bbfa52d83\",\"name\":\"Smilotron\",\"rank\":96,\"rating\":23.17489178220177},{\"id\":\"58882a58d2994e0d53b14571\",\"name\":\"Gabezilla\",\"rank\":97,\"rating\":22.988188215048982},{\"id\":\"587a951dd2994e15c7dea9f8\",\"name\":\"asianson\",\"rank\":98,\"rating\":22.80902667894459},{\"id\":\"588999c4d2994e713ad6386f\",\"name\":\"Redcoat\",\"rank\":99,\"rating\":22.791608535437256},{\"id\":\"588999c5d2994e713ad639f6\",\"name\":\"NINTENDOKING\",\"rank\":100,\"rating\":22.76300910886422}],\"region\":\"norcal\",\"ranking_criteria\":{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":30,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000},\"time\":\"02/15/17\",\"tournaments\":[\"588827bad2994e0d53b14556\",\"58885dced2994e3d5659410e\",\"588828ced2994e0d53b1455b\",\"58882955d2994e0d53b1455d\",\"5888282dd2994e0d53b14559\",\"58882a16d2994e0d53b1455f\",\"58885e04d2994e3d56594118\",\"5888502cd2994e3bbfa52d56\",\"588850d5d2994e3bbfa52d67\",\"58882a7dd2994e0d53b14574\",\"58882affd2994e0d53b14589\",\"58885305d2994e3bbfa52ddb\",\"58882c34d2994e0d53b14593\",\"58898d7bd2994e6f7981b1c6\",\"588a45b2d2994e713ad63cfd\",\"588c5f2fd2994e713ad63d2a\",\"588d0857d2994e713ad63d2e\",\"588ec08fd2994e6485b3d54a\",\"58902ffed2994e04409f19fd\",\"5896d3c2d2994e73ee0b171e\",\"5896deb3d2994e73ee0b172e\",\"5896d44dd2994e73ee0b1720\",\"589cb48fd2994e4d0f2e2574\",\"58a002c7d2994e4d0f2e258f\",\"589ebbd3d2994e4d0f2e2583\",\"58a00342d2994e4d0f2e2592\",\"58a0047dd2994e4d0f2e2597\",\"58a00514d2994e4d0f2e25a6\",\"58a007f2d2994e4d0f2e25b8\",\"58a2b66ed2994e688c68c9df\",\"58a4cfafd2994e756952ad78\"],\"id\":\"58a4d0c6d2994e756952ad7c\"}";

    private static final String JSON_TOURNAMENTS_BUNDLE = "{\"tournaments\":[{\"date\":\"01/05/17\",\"regions\":[\"norcal\"],\"id\":\"588827bad2994e0d53b14556\",\"name\":\"The Beat Down Ep.14\"},{\"date\":\"01/06/17\",\"regions\":[\"norcal\"],\"id\":\"58885dced2994e3d5659410e\",\"name\":\"Last Chance Fridays #17\"},{\"date\":\"01/09/17\",\"regions\":[\"norcal\"],\"id\":\"588828ced2994e0d53b1455b\",\"name\":\"Phoenix Underground #36\"},{\"date\":\"01/12/17\",\"regions\":[\"norcal\"],\"id\":\"58882955d2994e0d53b1455d\",\"name\":\"The Beat Down Ep.15\"},{\"date\":\"01/13/17\",\"regions\":[\"norcal\"],\"id\":\"5888282dd2994e0d53b14559\",\"name\":\"Melee @ the Made 23\"},{\"date\":\"01/13/17\",\"regions\":[\"norcal\"],\"id\":\"58882a16d2994e0d53b1455f\",\"name\":\"Four Stock Friday #39\"},{\"date\":\"01/13/17\",\"regions\":[\"norcal\"],\"id\":\"58885e04d2994e3d56594118\",\"name\":\"Last Chance Fridays #18\"},{\"date\":\"01/14/17\",\"regions\":[\"norcal\"],\"id\":\"5888502cd2994e3bbfa52d56\",\"name\":\"BAM to Genesis! (31)\"},{\"date\":\"01/14/17\",\"regions\":[\"norcal\"],\"id\":\"588850d5d2994e3bbfa52d67\",\"name\":\"Norcal Validated 1\"},{\"date\":\"01/15/17\",\"regions\":[\"norcal\"],\"id\":\"58882a7dd2994e0d53b14574\",\"name\":\"Melee in the Mont #7\"},{\"date\":\"01/16/17\",\"regions\":[\"norcal\"],\"id\":\"58882affd2994e0d53b14589\",\"name\":\"Phoenix Underground #37\"},{\"date\":\"01/17/17\",\"regions\":[\"norcal\"],\"id\":\"58885305d2994e3bbfa52ddb\",\"name\":\"Get Smashed 105 - Special Pre-Genesis 4 Foundry Event\"},{\"date\":\"01/19/17\",\"regions\":[\"norcal\"],\"id\":\"58882c34d2994e0d53b14593\",\"name\":\"PRE G4 The Beat Down\"},{\"date\":\"01/20/17\",\"regions\":[\"norcal\"],\"id\":\"58898d7bd2994e6f7981b1c6\",\"name\":\"Genesis 4\"},{\"date\":\"01/26/17\",\"regions\":[\"norcal\"],\"id\":\"588a45b2d2994e713ad63cfd\",\"name\":\"Wombo Wednesday 33\"},{\"date\":\"01/27/17\",\"regions\":[\"norcal\"],\"id\":\"588c5f2fd2994e713ad63d2a\",\"name\":\"Melee @ the Made 24\"},{\"date\":\"01/27/17\",\"regions\":[\"norcal\"],\"id\":\"588d0857d2994e713ad63d2e\",\"name\":\"Four Stock Friday #40\"},{\"date\":\"01/29/17\",\"regions\":[\"norcal\"],\"id\":\"588ec08fd2994e6485b3d54a\",\"name\":\"Super South Bay Sunday #22\"},{\"date\":\"01/30/17\",\"regions\":[\"norcal\"],\"id\":\"58902ffed2994e04409f19fd\",\"name\":\"Phoenix Underground #38\"},{\"date\":\"01/31/17\",\"regions\":[\"norcal\"],\"id\":\"5896d3c2d2994e73ee0b171e\",\"name\":\"Get Smashed #106\"},{\"date\":\"02/02/17\",\"regions\":[\"norcal\"],\"id\":\"5896deb3d2994e73ee0b172e\",\"name\":\"The Beat Down Ep.15\"},{\"date\":\"02/03/17\",\"regions\":[\"norcal\"],\"id\":\"5896d44dd2994e73ee0b1720\",\"name\":\"Four Stock Friday #41\"}]}";

    @Inject
    Gson mGson;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testSearchPlayerListWithAlliance() throws Exception {
        final PlayersBundle bundle = mGson.fromJson(JSON_PLAYERS_BUNDLE, PlayersBundle.class);
        final List<AbsPlayer> players = ListUtils.searchPlayerList("[A]", bundle.getPlayers());

        // noinspection ConstantConditions
        assertEquals(players.size(), 2);
    }

    @Test
    public void testSearchPlayerListWithEmpty() throws Exception {
        final PlayersBundle bundle = mGson.fromJson(JSON_PLAYERS_BUNDLE, PlayersBundle.class);
        List<AbsPlayer> players = bundle.getPlayers();

        // noinspection ConstantConditions
        final int size = players.size();

        players = ListUtils.searchPlayerList("", players);

        // noinspection ConstantConditions
        assertEquals(size, players.size());
    }

    @Test
    public void testSearchPlayerListThatIsNull() throws Exception {
        assertNull(ListUtils.searchPlayerList(null, null));
        assertNull(ListUtils.searchPlayerList("", null));
        assertNull(ListUtils.searchPlayerList("s", null));
    }

    @Test
    public void testSearchPlayerListWithImyt() throws Exception {
        final PlayersBundle bundle = mGson.fromJson(JSON_PLAYERS_BUNDLE, PlayersBundle.class);
        List<AbsPlayer> players = bundle.getPlayers();

        players = ListUtils.searchPlayerList("Imyt", players);

        // noinspection ConstantConditions
        assertEquals(players.size(), 1);

        players = ListUtils.searchPlayerList("  IMYT ", players);

        // noinspection ConstantConditions
        assertEquals(players.size(), 1);
    }

    @Test
    public void testSearchPlayerListWithNull() throws Exception {
        final PlayersBundle bundle = mGson.fromJson(JSON_PLAYERS_BUNDLE, PlayersBundle.class);
        List<AbsPlayer> players = bundle.getPlayers();

        // noinspection ConstantConditions
        final int size = players.size();

        players = ListUtils.searchPlayerList(null, players);

        // noinspection ConstantConditions
        assertEquals(size, players.size());
    }

    @Test
    public void testSearchPlayerListWithWhitespace() throws Exception {
        final PlayersBundle bundle = mGson.fromJson(JSON_PLAYERS_BUNDLE, PlayersBundle.class);
        List<AbsPlayer> players = bundle.getPlayers();

        // noinspection ConstantConditions
        final int size = players.size();

        players = ListUtils.searchPlayerList(" ", players);

        // noinspection ConstantConditions
        assertEquals(size, players.size());
    }

    @Test
    public void testSearchPlayerListWithZebra() throws Exception {
        final PlayersBundle bundle = mGson.fromJson(JSON_PLAYERS_BUNDLE, PlayersBundle.class);
        List<AbsPlayer> players = bundle.getPlayers();

        players = ListUtils.searchPlayerList("Zebra", players);

        // noinspection ConstantConditions
        assertTrue(players.isEmpty());
    }

    @Test
    public void testSearchRankingListThatIsNull() throws Exception {
        assertNull(ListUtils.searchRankingList(null, null));
        assertNull(ListUtils.searchRankingList("", null));
        assertNull(ListUtils.searchRankingList("s", null));
    }

    @Test
    public void testSearchRankingListWithEmpty() throws Exception {
        final RankingsBundle bundle = mGson.fromJson(JSON_RANKINGS_BUNDLE, RankingsBundle.class);
        List<Ranking> rankings = bundle.getRankings();

        // noinspection ConstantConditions
        final int size = rankings.size();

        rankings = ListUtils.searchRankingList("", rankings);

        // noinspection ConstantConditions
        assertEquals(size, rankings.size());
    }

    @Test
    public void testSearchRankingListWithImyt() throws Exception {
        final RankingsBundle bundle = mGson.fromJson(JSON_RANKINGS_BUNDLE, RankingsBundle.class);
        List<Ranking> rankings = bundle.getRankings();

        rankings = ListUtils.searchRankingList("Imyt", rankings);

        // noinspection ConstantConditions
        assertEquals(rankings.size(), 1);

        rankings = ListUtils.searchRankingList("  IMYT ", rankings);

        // noinspection ConstantConditions
        assertEquals(rankings.size(), 1);
    }

    @Test
    public void testSearchRankingListWithNull() throws Exception {
        final RankingsBundle bundle = mGson.fromJson(JSON_RANKINGS_BUNDLE, RankingsBundle.class);
        List<Ranking> rankings = bundle.getRankings();

        // noinspection ConstantConditions
        final int size = rankings.size();

        rankings = ListUtils.searchRankingList(null, rankings);

        // noinspection ConstantConditions
        assertEquals(size, rankings.size());
    }

    @Test
    public void testSearchRankingListWithOng() throws Exception {
        final RankingsBundle bundle = mGson.fromJson(JSON_RANKINGS_BUNDLE, RankingsBundle.class);
        List<Ranking> rankings = bundle.getRankings();

        rankings = ListUtils.searchRankingList("ong", rankings);

        // noinspection ConstantConditions
        assertEquals(rankings.size(), 2);

        rankings = ListUtils.searchRankingList("  ONG ", rankings);

        // noinspection ConstantConditions
        assertEquals(rankings.size(), 2);
    }

    @Test
    public void testSearchRankingListWithWhitespace() throws Exception {
        final RankingsBundle bundle = mGson.fromJson(JSON_RANKINGS_BUNDLE, RankingsBundle.class);
        List<Ranking> rankings = bundle.getRankings();

        // noinspection ConstantConditions
        final int size = rankings.size();

        rankings = ListUtils.searchRankingList(" ", rankings);

        // noinspection ConstantConditions
        assertEquals(size, rankings.size());
    }

    @Test
    public void testSearchTournamentListThatIsNull() throws Exception {
        assertNull(ListUtils.searchTournamentList(null, null));
        assertNull(ListUtils.searchTournamentList("", null));
        assertNull(ListUtils.searchTournamentList("s", null));
    }

    @Test
    public void testSearchTournamentListWithEmpty() throws Exception {
        final TournamentsBundle bundle = mGson.fromJson(JSON_TOURNAMENTS_BUNDLE, TournamentsBundle.class);
        List<AbsTournament> tournaments = bundle.getTournaments();

        // noinspection ConstantConditions
        final int size = tournaments.size();

        tournaments = ListUtils.searchTournamentList("", tournaments);

        // noinspection ConstantConditions
        assertEquals(size, tournaments.size());
    }

    @Test
    public void testSearchTournamentListWithNull() throws Exception {
        final TournamentsBundle bundle = mGson.fromJson(JSON_TOURNAMENTS_BUNDLE, TournamentsBundle.class);
        List<AbsTournament> tournaments = bundle.getTournaments();

        // noinspection ConstantConditions
        final int size = tournaments.size();

        tournaments = ListUtils.searchTournamentList(null, tournaments);

        // noinspection ConstantConditions
        assertEquals(size, tournaments.size());
    }

    @Test
    public void testSearchTournamentListWith105() throws Exception {
        final TournamentsBundle bundle = mGson.fromJson(JSON_TOURNAMENTS_BUNDLE, TournamentsBundle.class);
        List<AbsTournament> tournaments = bundle.getTournaments();

        tournaments = ListUtils.searchTournamentList("105", tournaments);

        // noinspection ConstantConditions
        assertEquals(tournaments.size(), 1);

        tournaments = ListUtils.searchTournamentList("  105 ", tournaments);

        // noinspection ConstantConditions
        assertEquals(tournaments.size(), 1);
    }

    @Test
    public void testSearchTournamentListWithThe() throws Exception {
        final TournamentsBundle bundle = mGson.fromJson(JSON_TOURNAMENTS_BUNDLE, TournamentsBundle.class);
        List<AbsTournament> tournaments = bundle.getTournaments();

        tournaments = ListUtils.searchTournamentList("The", tournaments);

        // noinspection ConstantConditions
        assertEquals(tournaments.size(), 7);

        tournaments = ListUtils.searchTournamentList(" THE ", tournaments);

        // noinspection ConstantConditions
        assertEquals(tournaments.size(), 7);
    }

    @Test
    public void testSearchTournamentListWithWhitespace() throws Exception {
        final TournamentsBundle bundle = mGson.fromJson(JSON_TOURNAMENTS_BUNDLE, TournamentsBundle.class);
        List<AbsTournament> tournaments = bundle.getTournaments();

        // noinspection ConstantConditions
        final int size = tournaments.size();

        tournaments = ListUtils.searchTournamentList(" ", tournaments);

        // noinspection ConstantConditions
        assertEquals(size, tournaments.size());
    }

}
