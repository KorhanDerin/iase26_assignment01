package de.seuhd.worldcup

import kotlin.test.*

class StandingsServiceTest {

    private fun updateStats(standings: MutableMap<String, TeamStats>, match: Match) {
        if (match.homeScore == null || match.awayScore == null) return
        val homeScore = match.homeScore
        val awayScore = match.awayScore
        standings[match.homeTeam]?.goalsFor += homeScore
        standings[match.homeTeam]?.goalsAgainst += awayScore
        standings[match.homeTeam]?.matchesPlayed++
        standings[match.awayTeam]?.goalsFor += awayScore
        standings[match.awayTeam]?.goalsAgainst += homeScore
        standings[match.awayTeam]?.matchesPlayed++

        if (homeScore > awayScore) {
            standings[match.homeTeam]?.wins++
            standings[match.awayTeam]?.losses++
        } else if (homeScore < awayScore) {
            standings[match.homeTeam]?.losses++
            standings[match.awayTeam]?.wins++
        } else {
            standings[match.homeTeam]?.draws++
            standings[match.awayTeam]?.draws++
        }
    }

    private fun checkBet(allMatches: List<Match>, bet: Bet): Boolean? {

        val match = allMatches.find { it.matchId == bet.matchId }?: return null
        val homeScore = match.homeScore
        val awayScore = match.awayScore

        val matchOutcome = when {
            homeScore == null || awayScore == null -> return null
            homeScore > awayScore -> 1
            homeScore < awayScore -> 2
            else -> 0
        }

        return bet.bet == matchOutcome
    }

    @Test
    fun `updateStats calculate win or loss for a match`() {
        //TODO
        val standings = mutableMapOf(
            "A" to TeamStats(name = "Team A"),
            "B" to TeamStats(name = "Team B")
        )

        val match = Match(
            matchId = 1,
            date = "2026-01-01",
            homeTeam = "A",
            awayTeam = "B",
            homeScore = 2,
            awayScore = 1,
            ground = "Heidelberg",
            round = "Matchday 1"
        )

        updateStats(standings, match)

        val teamA = standings["A"]!!
        val teamB = standings["B"]!!

        assertEquals(1, teamA.wins)
        assertEquals(1, teamB.losses)
        assertEquals(2, teamA.goalsFor)
        assertEquals(1, teamB.goalsFor)
        assertEquals(1, teamA.goalsAgainst)
        assertEquals(2, teamB.goalsAgainst)
    }

    @Test
    fun `checkBet returns correct result`() {

        val match = Match(
            matchId = 1,
            date = "2026-01-01",
            homeTeam = "A",
            awayTeam = "B",
            homeScore = 2,
            awayScore = 1,
            ground = "Heidelberg",
            round = "Matchday 1"
        )

        val allMatches = listOf(match)

        val bet = Bet(groupName = "Group A", matchId = 1, bet = 1)

        val result = checkBet(allMatches, bet)

        assertEquals(result, true)
    }
}