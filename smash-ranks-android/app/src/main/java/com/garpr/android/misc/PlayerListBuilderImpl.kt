package com.garpr.android.misc

import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.extensions.safeEquals
import com.garpr.android.misc.PlayerListBuilder.PlayerListItem
import com.garpr.android.repositories.IdentityRepository

class PlayerListBuilderImpl(
        private val identityRepository: IdentityRepository
) : PlayerListBuilder {

    override fun create(bundle: PlayersBundle?): List<PlayerListItem>? {
        val players = bundle?.players

        if (players.isNullOrEmpty()) {
            return null
        }

        val list = mutableListOf<PlayerListItem>()
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

                        list.add(PlayerListItem.Divider.Letter(
                                letter = char.toUpperCase().toString(),
                                listId = letterDividerListId
                        ))

                        ++letterDividerListId
                    }

                    list.add(PlayerListItem.Player(
                            player = it,
                            isIdentity = identityRepository.isPlayer(it)
                    ))
                }

        ///////////////////////
        // add digit players //
        ///////////////////////

        var addedDigitDivider = false

        players.filter { it.name.first().isDigit() }
                .forEach { player ->
                    if (!addedDigitDivider) {
                        addedDigitDivider = true
                        list.add(PlayerListItem.Divider.Digit)
                    }

                    list.add(PlayerListItem.Player(
                            player = player,
                            isIdentity = identityRepository.isPlayer(player)
                    ))
                }


        ///////////////////////
        // add other players //
        ///////////////////////

        var addedOtherDivider = false

        players.filter { !it.name.first().isLetterOrDigit() }
                .forEach { player ->
                    if (!addedOtherDivider) {
                        addedOtherDivider = true
                        list.add(PlayerListItem.Divider.Other)
                    }

                    list.add(PlayerListItem.Player(
                            player = player,
                            isIdentity = identityRepository.isPlayer(player)
                    ))
                }

        return list
    }

    override fun refresh(list: List<PlayerListItem>?): List<PlayerListItem>? {
        return if (list.isNullOrEmpty()) {
            list
        } else {
            list.map { listItem ->
                when (listItem) {
                    is PlayerListItem.Divider -> listItem
                    is PlayerListItem.NoResults -> listItem
                    is PlayerListItem.Player -> {
                        PlayerListItem.Player(
                                player = listItem.player,
                                isIdentity = identityRepository.isPlayer(listItem.player)
                        )
                    }
                }
            }
        }
    }

    override fun search(query: String?, list: List<PlayerListItem>?): List<PlayerListItem>? {
        if (query.isNullOrBlank() || list.isNullOrEmpty()) {
            return null
        }

        val results = mutableListOf<PlayerListItem>()
        val trimmedQuery = query.trim()

        list.forEachIndexed { index, listItem ->
            if (listItem is PlayerListItem.Divider) {
                var addedDivider = false
                var j = index + 1

                while (j < list.size) {
                    val current = list[j]

                    if (current is PlayerListItem.Player) {
                        if (current.player.name.contains(trimmedQuery, true)) {
                            if (!addedDivider) {
                                addedDivider = true
                                results.add(listItem)
                            }

                            results.add(current)
                        }

                        ++j
                    } else {
                        j = list.size
                    }
                }
            }
        }

        if (results.isEmpty()) {
            results.add(PlayerListItem.NoResults(query))
        }

        return results
    }

}
