package de.seuhd.worldcup
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.collections.mutableMapOf

fun main() {
    //TODO: Load JSON data
    val jsonText = File("src/main/resources/world_cup_2026_full_data.json").readText()
    val worldCupData = Json.decodeFromString<WorldCupData>(jsonText)
    //TODO: Implement interactive menu
    val betList = mutableListOf<Bet>()
    while(true){
        println("===== FIFA World Cup 2026 ? Betting Console =====")
        println("1) Show Standings")
        println("2) Show Matches")
        println("3) Place Bets")
        println("4) Show Betting Score")
        println("5) Exit")

        val userInput = readln().toIntOrNull()
        when(userInput){
            1 -> showStandings(worldCupData.groups)
            2 -> showMatches(worldCupData.groups)
            3 -> placeBets(worldCupData.groups, betList)
            4 -> showBettingScore(worldCupData.groups, betList)
            5 -> {
                println("Take care!")
                break
            }
        }
    }
}

/* -------------------------------------------------------------
   1) Show Standings
   ------------------------------------------------------------- */
private fun showStandings(allGroups: List<Group>) {
    //TODO
    while(true){
        println("===== Show Standings =====")
        println("0) Show Standings for a Single Group")
        println("1) Show Standings for a All Groups")
        println("2) Exit to Main Menu")
        val userInput = readln().toIntOrNull() ?: continue

        when(userInput){
            0 ->  {
                println("Please Choose a Group to View")
                allGroups.forEachIndexed { index, group -> println("$index) ${group.name}") }

                val groupIndex = readln().toIntOrNull() ?: continue
                if(groupIndex !in allGroups.indices) continue

                val group = allGroups[groupIndex]
                printGroupStanding(group)
            }
            1 -> allGroups.forEach { printGroupStanding(it) }
            2 -> break
        }
    }

}

// Helper to print one group.
private fun printGroupStanding(group: Group){
    println("=================")
    println(group.name)
    println("Team\t|\tMP\t|\tW\t|\tD\t|\tL\t|\tGF\t|\tGA\t|\tGD\t|\tP")

    // Goal is to map teamIds to their stats to access them easily in updates
    val standings = mutableMapOf<String, TeamStats>()
    group.teams.forEach { standings[it.id] = TeamStats(name = it.name)}
    group.matches.forEach{updateStats(standings,it)}

    // sort first by descending points, then by descending goal difference
    val sortedStandings = standings.values.sortedWith(
        compareByDescending<TeamStats> { it.points }
            .thenByDescending { it.goalDifference }
    )

    // print the standings
    sortedStandings.forEach { println("${it.name}\t|\t${it.matchesPlayed}\t|\t${it.wins}" +
            "\t|\t${it.draws}\t|\t${it.losses}\t|\t${it.goalsFor}\t|\t${it.goalsAgainst}" +
    "\t|\t${it.goalDifference}\t|\t${it.points}") }

}

private fun updateStats(standings: MutableMap<String, TeamStats>, match: Match){
    if (match.homeScore == null || match.awayScore == null) return
    val homeScore = match.homeScore
    val awayScore = match.awayScore
    standings[match.homeTeam]?.goalsFor += homeScore
    standings[match.homeTeam]?.goalsAgainst += awayScore
    standings[match.homeTeam]?.matchesPlayed++
    standings[match.awayTeam]?.goalsFor += awayScore
    standings[match.awayTeam]?.goalsAgainst += homeScore
    standings[match.awayTeam]?.matchesPlayed++

    if(homeScore> awayScore) {
        standings[match.homeTeam]?.wins++
        standings[match.awayTeam]?.losses++
    } else if(homeScore < awayScore) {
        standings[match.homeTeam]?.losses++
        standings[match.awayTeam]?.wins++
    } else {
        standings[match.homeTeam]?.draws++
        standings[match.awayTeam]?.draws++
    }

}

/* -------------------------------------------------------------
   2) Show Matches
   ------------------------------------------------------------- */
private fun showMatches(allGroups: List<Group>) {
    //TODO

    while(true) {
        println("===== Show Matches =====")
        println("Please Choose a Group to View It's Matches")
        allGroups.forEachIndexed { index, group -> println("$index) ${group.name}") }

        println("${allGroups.size}) Exit to Main Menu")

        // Read user input
        val groupIndex = readln().toIntOrNull() ?: continue

        val group: Group
        when (groupIndex) {
            // Exit clause
            allGroups.size -> break
            // Out of bounds
            !in allGroups.indices -> continue
            else -> {
                group = allGroups[groupIndex]
            }
        }


        println("=================")
        println(group.name)
        println("Date\t|\tHome Team\t|\t Away Team\t|\tScore")
        group.matches.forEach { printMatchDetails(it) }
        println("=================")

    }
}

private fun printMatchDetails(match: Match){
    println("${match.date}\t|\t${match.homeTeam}\t|\t${match.awayTeam}" +
            "\t|\t${match.homeScore?: "?"}-${match.awayScore?: "?"}")
}

/* -------------------------------------------------------------
   3) Place Bets
   ------------------------------------------------------------- */
private fun placeBets(allGroups: List<Group>, betList: MutableList<Bet>) {
    //TODO

    while(true){
        println("===== Place Bets =====")
        println("Select a Group to bet on")
        allGroups.forEachIndexed { index, group -> println("$index) ${group.name}") }

        println("${allGroups.size}) Exit to Main Menu")

        // Read user input
        val groupIndex = readln().toIntOrNull() ?: continue

        val group: Group
        when (groupIndex) {
            // Exit clause
            allGroups.size -> break
            // Out of bounds
            !in allGroups.indices -> continue
            else -> {
                group = allGroups[groupIndex]
            }
        }

        // Print matches one by one to read bets
        println("=================")
        println(group.name)
        println("Date\t|\tHome Team\t|\t Away Team\t|\tScore")
        readGroupBets(group, betList)
        println("=================")

    }
}

private fun readGroupBets(group: Group, betList: MutableList<Bet>){
    group.matches.forEach {
        while(true){
            printMatchDetails(it)
            println("Enter Your Bet: 1 (Home Win), 2 (Away Win), or 0 (Draw)")

            val betOutcome = readln().toIntOrNull()?: continue
            if (betOutcome !in 0..2) continue

            val bet = Bet(groupName = group.name, matchId = it.matchId, bet = betOutcome)
            betList.add(bet)

            break
        }
    }
}

/* -------------------------------------------------------------
   4) Show Betting Score
   ------------------------------------------------------------- */
private fun showBettingScore(allGroups: List<Group>, betList: MutableList<Bet>) {
    //TODO
    println("===== Show Betting Scores =====")

    var totalScore = 0
    var totalMatchesPlayed = 0
    val allMatches = allGroups.flatMap { it.matches }
    betList.groupBy { it.groupName }
        .forEach { (groupName, groupBets) ->

            println("Group $groupName")

            var groupScore = 0
            var groupMatchesPlayed = 0
            groupBets.forEach {

                val correctGuess = checkBet(allMatches, it)
                if (correctGuess != null){
                    // Keep track of how many matches were played from the bet pool
                    groupMatchesPlayed++
                    totalMatchesPlayed++
                    if (correctGuess){
                        groupScore += 1
                        totalScore += 1
                    }
                }
            }
            val groupIncorrect = groupMatchesPlayed - groupScore
            println("Group score: $groupScore | Correct vs Incorrect guesses: $groupScore x $groupIncorrect")
        }

    val totalIncorrect = totalMatchesPlayed - totalScore
    println("Total score: $totalScore | Correct vs Incorrect guesses: $totalScore x $totalIncorrect")}

//Print the bet details and result (if the match is played)
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