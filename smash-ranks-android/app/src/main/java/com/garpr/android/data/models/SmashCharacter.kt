package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes
import com.garpr.android.R
import com.garpr.android.extensions.createParcel
import com.squareup.moshi.Json

enum class SmashCharacter(
        @StringRes val textResId: Int
) : Parcelable {

    @Json(name = "bnk")
    BANJO_N_KAZOOIE(R.string.banjo_n_kazooie),

    @Json(name = "byo")
    BAYONETTA(R.string.bayonetta),

    @Json(name = "bow")
    BOWSER(R.string.bowser),

    @Json(name = "bjr")
    BOWSER_JR(R.string.bowser_jr),

    @Json(name = "fcn")
    CPTN_FALCON(R.string.captain_falcon),

    @Json(name = "olm")
    CPTN_OLIMAR(R.string.captain_olimar),

    @Json(name = "chr")
    CHARIZARD(R.string.charizard),

    @Json(name = "chm")
    CHROM(R.string.chrom),

    @Json(name = "cld")
    CLOUD(R.string.cloud),

    @Json(name = "crn")
    CORRIN(R.string.corrin),

    @Json(name = "dpt")
    DARK_PIT(R.string.dark_pit),

    @Json(name = "dks")
    DARK_SAMUS(R.string.dark_samus),

    @Json(name = "ddy")
    DIDDY_KONG(R.string.diddy_kong),

    @Json(name = "doc")
    DR_MARIO(R.string.dr_mario),

    @Json(name = "dnk")
    DONKEY_KONG(R.string.donkey_kong),

    @Json(name = "dck")
    DUCK_HUNT(R.string.duck_hunt),

    @Json(name = "fco")
    FALCO(R.string.falco),

    @Json(name = "fox")
    FOX(R.string.fox),

    @Json(name = "gnn")
    GANONDORF(R.string.ganondorf),

    @Json(name = "grn")
    GRENINJA(R.string.greninja),

    @Json(name = "hro")
    HERO(R.string.hero),

    @Json(name = "ics")
    ICE_CLIMBERS(R.string.ice_climbers),

    @Json(name = "ike")
    IKE(R.string.ike),

    @Json(name = "inc")
    INCINEROAR(R.string.incineroar),

    @Json(name = "ink")
    INKLING(R.string.inkling),

    @Json(name = "ivy")
    IVYSAUR(R.string.ivysaur),

    @Json(name = "puf")
    JIGGLYPUFF(R.string.jigglypuff),

    @Json(name = "jok")
    JOKER(R.string.joker),

    @Json(name = "ken")
    KEN(R.string.ken),

    @Json(name = "ddd")
    KING_DEDEDE(R.string.king_dedede),

    @Json(name = "kkr")
    KING_K_ROOL(R.string.king_k_rool),

    @Json(name = "kby")
    KIRBY(R.string.kirby),

    @Json(name = "lnk")
    LINK(R.string.link),

    @Json(name = "lmc")
    LITTLE_MAC(R.string.little_mac),

    @Json(name = "lcr")
    LUCARIO(R.string.lucario),

    @Json(name = "lcs")
    LUCAS(R.string.lucas),

    @Json(name = "lcn")
    LUCINA(R.string.lucina),

    @Json(name = "lgi")
    LUIGI(R.string.luigi),

    @Json(name = "mar")
    MARIO(R.string.mario),

    @Json(name = "mrt")
    MARTH(R.string.marth),

    @Json(name = "meg")
    MEGA_MAN(R.string.mega_man),

    @Json(name = "mtk")
    META_KNIGHT(R.string.meta_knight),

    @Json(name = "mw2")
    MEWTWO(R.string.mewtwo),

    @Json(name = "mib")
    MII_BRAWLER(R.string.mii_brawler),

    @Json(name = "mig")
    MII_GUNNER(R.string.mii_gunner),

    @Json(name = "mis")
    MII_SWORDFIGHTER(R.string.mii_swordfighter),

    @Json(name = "gnw")
    MR_GAME_AND_WATCH(R.string.mr_game_and_watch),

    @Json(name = "nes")
    NESS(R.string.ness),

    @Json(name = "pac")
    PAC_MAN(R.string.pac_man),

    @Json(name = "pal")
    PALUTENA(R.string.palutena),

    @Json(name = "pch")
    PEACH(R.string.peach),

    @Json(name = "pic")
    PICHU(R.string.pichu),

    @Json(name = "pik")
    PIKACHU(R.string.pikachu),

    @Json(name = "pir")
    PIRANHA_PLANT(R.string.piranha_plant),

    @Json(name = "pit")
    PIT(R.string.pit),

    @Json(name = "pkt")
    POKEMON_TRAINER(R.string.pokemon_trainer),

    @Json(name = "ric")
    RICTER(R.string.ricter),

    @Json(name = "rid")
    RIDLEY(R.string.ridley),

    @Json(name = "rob")
    ROB(R.string.rob),

    @Json(name = "rbn")
    ROBIN(R.string.robin),

    @Json(name = "ros")
    ROSALINA(R.string.rosalina),

    @Json(name = "roy")
    ROY(R.string.roy),

    @Json(name = "ryu")
    RYU(R.string.ryu),

    @Json(name = "sam")
    SAMUS(R.string.samus),

    @Json(name = "shk")
    SHEIK(R.string.sheik),

    @Json(name = "slk")
    SHULK(R.string.shulk),

    @Json(name = "smn")
    SIMON(R.string.simon),

    @Json(name = "snk")
    SNAKE(R.string.snake),

    @Json(name = "snc")
    SONIC(R.string.sonic),

    @Json(name = "sqt")
    SQUIRTLE(R.string.squirtle),

    @Json(name = "tbg")
    TERRY_BOGARD(R.string.terry_bogard),

    @Json(name = "tlk")
    TOON_LINK(R.string.toon_link),

    @Json(name = "vlg")
    VILLAGER(R.string.villager),

    @Json(name = "war")
    WARIO(R.string.wario),

    @Json(name = "wft")
    WII_FIT_TRAINER(R.string.wii_fit_trainer),

    @Json(name = "wlf")
    WOLF(R.string.wolf),

    @Json(name = "ysh")
    YOSHI(R.string.yoshi),

    @Json(name = "ylk")
    YOUNG_LINK(R.string.young_link),

    @Json(name = "zld")
    ZELDA(R.string.zelda),

    @Json(name = "zss")
    ZERO_SUIT_SAMUS(R.string.zero_suit_samus);

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

}
