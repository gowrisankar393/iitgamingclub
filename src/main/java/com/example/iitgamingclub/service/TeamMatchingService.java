package com.example.iitgamingclub.service;

import com.example.iitgamingclub.model.Participant;
import com.example.iitgamingclub.model.Team;
import java.util.*;
import java.util.stream.Collectors;

public class TeamMatchingService {

    public List<Team> generateTeams(List<Participant> sourceData, int targetSize) {
        //create a working copy of the pool
        List<Participant> mainPool = new ArrayList<>(sourceData);

        //sort by skill (strongest first) for better distribution
        mainPool.sort(Comparator.comparingInt(Participant::getSkillLevel).reversed());

        int totalPlayers = mainPool.size();

        //team size calculation
        int numTeams = Math.max(1, totalPlayers / targetSize);

        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < numTeams; i++) {
            teams.add(new Team(i + 1));
        }

        //distribute leaders first
        List<Participant> leaders = extractByType(mainPool, "Leader");
        //1 leader per team.
        List<Participant> unassignedLeaders = roundRobinDistribute(teams, leaders, 1);

        //distribute thinkers
        List<Participant> thinkers = extractByType(mainPool, "Thinker");
        //up to 2 thinkers per team.
        List<Participant> unassignedThinkers = roundRobinDistribute(teams, thinkers, 2);

        //fill the rest with the rest if the leaders and thinkers
        mainPool.addAll(unassignedLeaders);
        mainPool.addAll(unassignedThinkers);

        //re sort to mix the returned leaders/thinkers properly by skill (previous issue was that once leaders and thinkers were set, they did not get added to the teams which resulted in less members per team)
        mainPool.sort(Comparator.comparingInt(Participant::getSkillLevel).reversed());

        //fill teams until everyone is assigned (we use round robbin method)
        roundRobinDistribute(teams, mainPool, Integer.MAX_VALUE);

        return teams;
    }

    //remove specific types from the main pool so they can be handled separately
    private List<Participant> extractByType(List<Participant> pool, String type) {
        List<Participant> extracted = pool.stream()
                .filter(p -> p.getPersonalityType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
        pool.removeAll(extracted);
        return extracted;
    }

    //distributes players to teams and returns a list of players who could not be assigned due to limits/constraints
    private List<Participant> roundRobinDistribute(List<Team> teams, List<Participant> players, int limitPerTeam) {
        List<Participant> unassigned = new ArrayList<>();
        int teamIndex = 0;

        for (Participant p : players) {
            boolean assigned = false;

            //try every team once (starting from current index for round robin method)
            for (int i = 0; i < teams.size(); i++) {
                Team t = teams.get((teamIndex + i) % teams.size());

                //constraint check 1 - team limit (skip if this phase's limit is reached)
                if (t.getMemberCount() >= limitPerTeam) {
                    continue;
                }

                //constraint check 2 - game variety (max 2 of same game)
                long sameGameCount = t.getMembers().stream()
                        .filter(m -> m.getPreferredGame().equalsIgnoreCase(p.getPreferredGame()))
                        .count();

                if (sameGameCount < 2) {
                    t.addMember(p);
                    assigned = true;
                    teamIndex++; //move to next team for next player
                    break;
                }
            }

            //if not assigned after trying all teams
            if (!assigned) {
                //if we are in the filling phase (limit is high), force assign to the smallest team. this prevents people from being left out
                if (limitPerTeam > 5) {
                    Team smallest = teams.stream()
                            .min(Comparator.comparingInt(Team::getMemberCount))
                            .orElse(teams.get(0));
                    smallest.addMember(p);
                } else {
                    //if we are in priority phase (leader/thinker) save them for later
                    unassigned.add(p);
                }
            }
        }

        return unassigned;
    }
}