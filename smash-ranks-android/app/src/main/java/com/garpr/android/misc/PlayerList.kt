package com.garpr.android.misc

import androidx.annotation.WorkerThread
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.extensions.safeEquals

object PlayerList {

    @WorkerThread
    fun createList(bundle: PlayersBundle?): List<Item>? {
        val players = bundle?.players

        if (players.isNullOrEmpty()) {
            return null
        }

        val list = mutableListOf<Item>()
        var previousChar: Char? = null
        var letterDividerListId = Long.MIN_VALUE + 1L

        ////////////////////////
        // add letter players //
        ////////////////////////

        players.filter { it.name.first().isLetter() }
                .forEach {
                    val char = it.name.first()

                    if (!char.safeEquals(previousChar, true)) {
                        previousChar = char

                        list.add(Item.Divider.Letter(
                                letter = char.toUpperCase().toString(),
                                listId = letterDividerListId
                        ))

                        ++letterDividerListId
                    }

                    list.add(Item.Player(it))
                }

        ///////////////////////
        // add digit players //
        ///////////////////////

        var addedDigitDivider = false

        players.filter { it.name.first().isDigit() }
                .forEach {
                    if (!addedDigitDivider) {
                        addedDigitDivider = true
                        list.add(Item.Divider.Digit)
                    }

                    list.add(Item.Player(it))
                }


        ///////////////////////
        // add other players //
        ///////////////////////

        var addedOtherDivider = false

        players.filter { !it.name.first().isLetterOrDigit() }
                .forEach {
                    if (!addedOtherDivider) {
                        addedOtherDivider = true
                        list.add(Item.Divider.Other)
                    }

                    list.add(Item.Player(it))
                }

        return list
    }

    @WorkerThread
    fun search(query: String?, list: List<Item>?): List<Item>? {
        if (query.isNullOrBlank() || list.isNullOrEmpty()) {
            return null
        }

        val results = mutableListOf<Item>()
        val trimmedQuery = query.trim()

        list.forEachIndexed { index, item ->
            if (item is Item.Divider) {
                var addedDivider = false
                var j = index + 1

                while (j < list.size) {
                    val objectJ = list[j]

                    if (objectJ is Item.Player) {
                        if (objectJ.player.name.contains(trimmedQuery, true)) {
                            if (!addedDivider) {
                                addedDivider = true
                                results.add(item)
                            }

                            results.add(objectJ)
                        }

                        ++j
                    } else {
                        j = list.size
                    }
                }
            }
        }

        return results
    }

    sealed class Item {
        abstract val listId: Long

        sealed class Divider : Item() {
            object Digit : Divider() {
                override val listId: Long = Long.MAX_VALUE - 1L
            }

            class Letter(
                    override val listId: Long,
                    val letter: String
            ) : Divider()

            object Other : Divider() {
                override val listId: Long = Long.MAX_VALUE - 2L
            }
        }

        class Player(
                val player: AbsPlayer
        ) : Item() {
            override val listId: Long = player.hashCode().toLong()
        }
    }

}
