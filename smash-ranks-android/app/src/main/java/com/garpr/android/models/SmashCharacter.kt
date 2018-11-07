package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes
import com.garpr.android.R
import com.garpr.android.extensions.createParcel
import com.google.gson.annotations.SerializedName

enum class SmashCharacter(
        @StringRes val textResId: Int
) : Parcelable {

    @SerializedName("byo")
    BAYONETTA(R.string.bayonetta),

    @SerializedName("bow")
    BOWSER(R.string.bowser),

    @SerializedName("bjr")
    BOWSER_JR(R.string.bowser_jr),

    @SerializedName("fcn")
    CPTN_FALCON(R.string.captain_falcon),

    @SerializedName("olm")
    CPTN_OLIMAR(R.string.captain_olimar),

    @SerializedName("chr")
    CHARIZARD(R.string.charizard),

    @SerializedName("chm")
    CHROM(R.string.chrom),

    @SerializedName("cld")
    CLOUD(R.string.cloud),

    @SerializedName("crn")
    CORRIN(R.string.corrin),

    @SerializedName("dpt")
    DARK_PIT(R.string.dark_pit),

    @SerializedName("dks")
    DARK_SAMUS(R.string.dark_samus),

    @SerializedName("ddy")
    DIDDY_KONG(R.string.diddy_kong),

    @SerializedName("doc")
    DR_MARIO(R.string.dr_mario),

    @SerializedName("dnk")
    DONKEY_KONG(R.string.donkey_kong),

    @SerializedName("dck")
    DUCK_HUNT(R.string.duck_hunt),

    @SerializedName("fco")
    FALCO(R.string.falco),

    @SerializedName("fox")
    FOX(R.string.fox),

    @SerializedName("gnn")
    GANONDORF(R.string.ganondorf),

    @SerializedName("grn")
    GRENINJA(R.string.greninja),

    @SerializedName("ics")
    ICE_CLIMBERS(R.string.ice_climbers),

    @SerializedName("ike")
    IKE(R.string.ike),

    @SerializedName("inc")
    INCINEROAR(R.string.incineroar),

    @SerializedName("ink")
    INKLING(R.string.inkling),

    @SerializedName("ivy")
    IVYSAUR(R.string.ivysaur),

    @SerializedName("puf")
    JIGGLYPUFF(R.string.jigglypuff),

    @SerializedName("ken")
    KEN(R.string.ken),

    @SerializedName("ddd")
    KING_DEDEDE(R.string.king_dedede),

    @SerializedName("kkr")
    KING_K_ROOL(R.string.king_k_rool),

    @SerializedName("kby")
    KIRBY(R.string.kirby),

    @SerializedName("lnk")
    LINK(R.string.link),

    @SerializedName("lmc")
    LITTLE_MAC(R.string.little_mac),

    @SerializedName("lcr")
    LUCARIO(R.string.lucario),

    @SerializedName("lcs")
    LUCAS(R.string.lucas),

    @SerializedName("lcn")
    LUCINA(R.string.lucina),

    @SerializedName("lgi")
    LUIGI(R.string.luigi),

    @SerializedName("mar")
    MARIO(R.string.mario),

    @SerializedName("mrt")
    MARTH(R.string.marth),

    @SerializedName("meg")
    MEGA_MAN(R.string.mega_man),

    @SerializedName("mtk")
    META_KNIGHT(R.string.meta_knight),

    @SerializedName("mw2")
    MEWTWO(R.string.mewtwo),

    @SerializedName("mib")
    MII_BRAWLER(R.string.mii_brawler),

    @SerializedName("mig")
    MII_GUNNER(R.string.mii_gunner),

    @SerializedName("mis")
    MII_SWORDFIGHTER(R.string.mii_swordfighter),

    @SerializedName("gnw")
    MR_GAME_AND_WATCH(R.string.mr_game_and_watch),

    @SerializedName("nes")
    NESS(R.string.ness),

    @SerializedName("pac")
    PAC_MAN(R.string.pac_man),

    @SerializedName("pal")
    PALUTENA(R.string.palutena),

    @SerializedName("pch")
    PEACH(R.string.peach),

    @SerializedName("pic")
    PICHU(R.string.pichu),

    @SerializedName("pik")
    PIKACHU(R.string.pikachu),

    @SerializedName("pir")
    PIRANHA_PLANT(R.string.piranha_plant),

    @SerializedName("pit")
    PIT(R.string.pit),

    @SerializedName("pkt")
    POKEMON_TRAINER(R.string.pokemon_trainer),

    @SerializedName("ric")
    RICTER(R.string.ricter),

    @SerializedName("rid")
    RIDLEY(R.string.ridley),

    @SerializedName("rob")
    ROB(R.string.rob),

    @SerializedName("rbn")
    ROBIN(R.string.robin),

    @SerializedName("ros")
    ROSALINA(R.string.rosalina),

    @SerializedName("roy")
    ROY(R.string.roy),

    @SerializedName("ryu")
    RYU(R.string.ryu),

    @SerializedName("sam")
    SAMUS(R.string.samus),

    @SerializedName("shk")
    SHEIK(R.string.sheik),

    @SerializedName("slk")
    SHULK(R.string.shulk),

    @SerializedName("smn")
    SIMON(R.string.simon),

    @SerializedName("snk")
    SNAKE(R.string.snake),

    @SerializedName("snc")
    SONIC(R.string.sonic),

    @SerializedName("sqt")
    SQUIRTLE(R.string.squirtle),

    @SerializedName("tlk")
    TOON_LINK(R.string.toon_link),

    @SerializedName("vlg")
    VILLAGER(R.string.villager),

    @SerializedName("war")
    WARIO(R.string.wario),

    @SerializedName("wft")
    WII_FIT_TRAINER(R.string.wii_fit_trainer),

    @SerializedName("wlf")
    WOLF(R.string.wolf),

    @SerializedName("ysh")
    YOSHI(R.string.yoshi),

    @SerializedName("ylk")
    YOUNG_LINK(R.string.young_link),

    @SerializedName("zld")
    ZELDA(R.string.zelda),

    @SerializedName("zss")
    ZERO_SUIT_SAMUS(R.string.zero_suit_samus);


    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}
