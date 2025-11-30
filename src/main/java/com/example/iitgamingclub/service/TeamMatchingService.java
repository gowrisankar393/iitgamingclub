package com.example.iitgamingclub.service;

import com.example.iitgamingclub.model.Participant;
import com.example.iitgamingclub.model.Team;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class: TeamMatchingService
 * Logic: Implements the logic to form balanced teams based on roles, personality, and skill.
 */
public class TeamMatchingService {

    public List<Team> generateTeams(List<Participant> sourceData, int teamSize) {
        List<Participant> pool = new ArrayList<>(sourceData);
        // Sort pool by skill (descending) to prioritize placing strong players
        pool.sort(Comparator.comparingInt(Participant::getSkillLevel).reversed());

        int totalPlayers = pool.size();
        int numTeams = (int) Math.ceil((double) totalPlayers / teamSize);

        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < numTeams; i++) {
            teams.add(new Team(i + 1));
        }

        // 1. Distribute Leaders (1 per team)
        List<Participant> leaders = extractByType(pool, "Leader");
        roundRobinDistribute(teams, leaders, 1);

        // 2. Distribute Thinkers (2 per team)
        List<Participant> thinkers = extractByType(pool, "Thinker");
        roundRobinDistribute(teams, thinkers, 2);

        // 3. Fill with Balanced and remaining (Fill to teamSize)
        roundRobinDistribute(teams, pool, teamSize);

        // 4. Handle Remainders (If any unassigned remain due to constraints)
        // If the loop finished but 'pool' still has people, force assign them
        if(!pool.isEmpty()) {
            // Sort teams by size (smallest first)
            teams.sort(Comparator.comparingInt(Team::getMemberCount));
            for(Participant p : pool) {
                teams.get(0).addMember(p); // Add to smallest
                // Re-sort to keep balancing
                teams.sort(Comparator.comparingInt(Team::getMemberCount));
            }
        }

        return teams;
    }

    private List<Participant> extractByType(List<Participant> pool, String type) {
        List<Participant> extracted = pool.stream()
                .filter(p -> p.getPersonalityType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
        pool.removeAll(extracted);
        return extracted;
    }

    private void roundRobinDistribute(List<Team> teams, List<Participant> players, int limit) {
        // Sort players by skill to balance teams
        players.sort(Comparator.comparingInt(Participant::getSkillLevel).reversed());

        int teamIndex = 0;
        Iterator<Participant> it = players.iterator();

        while (it.hasNext()) {
            Participant p = it.next();
            boolean assigned = false;

            // Try to find a valid team
            for (int i = 0; i < teams.size(); i++) {
                Team t = teams.get((teamIndex + i) % teams.size());

                // Check Game Variety Constraint (Max 2 of same game)
                long sameGameCount = t.getMembers().stream()
                        .filter(m -> m.getPreferredGame().equalsIgnoreCase(p.getPreferredGame()))
                        .count();

                // Check if team has space (based on the current phase limit)
                // If limit >= 5, we are in filling phase, so strict role limits are relaxed, just fill capacity
                if ((t.getMemberCount() < limit || limit >= 5) && sameGameCount < 2) {
                    t.addMember(p);
                    it.remove();
                    assigned = true;
                    teamIndex++;
                    break;
                }
            }

            // If strictly filling and constraints block it, force assignment to avoid stragglers
            // (Only applies in the final phase where limit is high)
            if (!assigned && limit >= 5) {
                // Find smallest team
                Team smallest = teams.stream().min(Comparator.comparingInt(Team::getMemberCount)).orElse(teams.get(0));
                smallest.addMember(p);
                it.remove();
            }
        }
    }
}