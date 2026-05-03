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
    val name: String,
    val teams: List<Team>,
    val matches: List<Match>

)

@Serializable
data class Team(
    //TODO
    val id: String,
    val name: String
    )

@Serializable
data class Match(
    //TODO
    val matchId: Int,
    val round: String,
    val date: String,
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int?,
    val awayScore: Int?,
    val ground: String

)

@Serializable
data class Knockout(
    //TODO
    val matchId: Int,
    val round: String,
    val date: String,
    val homePlaceholder: String,
    val awayPlaceholder: String,
    val homeScore: Int?,
    val awayScore: Int?,
    val ground: String
)

// New data classes
data class Bet (
    val groupName: String,
    val matchId: Int,
    val bet: Int
)

data class TeamStats (
    val name: String,
    var matchesPlayed: Int = 0,
    var wins: Int = 0,
    var draws: Int = 0,
    var losses: Int = 0,
    var goalsFor: Int = 0,
    var goalsAgainst: Int = 0,
) {
    val points: Int
        get() = 3*this.wins + this.draws

    val goalDifference: Int
        get() = this.goalsFor - this.goalsAgainst
}