package de.seuhd.worldcup

import kotlinx.serialization.Serializable

@Serializable
data class WorldCupData(
    val tournament: String,
    val groups: List<Group>,
    val knockouts: List<Knockout>
)

@Serializable
data class Group(
   //TODO
    val todo: String,
)

@Serializable
data class Team(
    //TODO
    val todo: String,
)

@Serializable
data class Match(
    //TODO
    val todo: String,
)

@Serializable
data class Knockout(
    //TODO
    val todo: String,
)