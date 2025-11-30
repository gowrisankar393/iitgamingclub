package com.example.iitgamingclub.service;

import com.example.iitgamingclub.model.Participant;
import com.example.iitgamingclub.model.Team;
import java.util.*;
import java.util.stream.Collectors;

public class TeamMatchingService {

    public List<Team> generateTeams(List<Participant> sourceData, int teamSize) {
        // Create a copy to manipulate
        List<Participant> pool = new ArrayList<>(sourceData);

        // Strategy: Sort by Skill (Highest First) to distribute strong players evenly
        pool.sort(Comparator.comparingInt(Participant::getSkillLevel).reversed());

        int totalPlayers = pool.size();
        int numTeams = (int) Math.ceil((double) totalPlayers / teamSize);

        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < numTeams; i++) {
            teams.add(new Team(i + 1));
        }

        // 1. Distribute Leaders (Try 1 per team)
        List<Participant> leaders = extractByType(pool, "Leader");
        roundRobinDistribute(teams, leaders, 1);

        // 2. Distribute Thinkers (Try 1-2 per team)
        List<Participant> thinkers = extractByType(pool, "Thinker");
        roundRobinDistribute(teams, thinkers, 2);

        // 3. Fill with Balanced and remaining Leaders/Thinkers
        roundRobinDistribute(teams, pool, teamSize); // 'pool' now only contains remaining

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
        // Sort players by skill to balance skill while distributing
        players.sort(Comparator.comparingInt(Participant::getSkillLevel).reversed());

        int teamIndex = 0;
        Iterator<Participant> it = players.iterator();

        while (it.hasNext()) {
            Participant p = it.next();
            boolean assigned = false;

            // Try to find a slot
            for (int i = 0; i < teams.size(); i++) {
                Team t = teams.get((teamIndex + i) % teams.size()); // Round robin

                // Check if team is not full regarding the current limit/constraint
                // We also check Game Variety (Max 2 of same game)
                if (t.getMemberCount() < limit || limit >= 5) { // Strict limit for roles, loose for filling
                    long sameGameCount = t.getMembers().stream()
                            .filter(m -> m.getPreferredGame().equalsIgnoreCase(p.getPreferredGame()))
                            .count();

                    if (sameGameCount < 2) {
                        t.addMember(p);
                        it.remove();
                        assigned = true;
                        teamIndex++;
                        break;
                    }
                }
            }

            // If could not assign due to constraints, force assign to smallest team (Weakest Link logic)
            if (!assigned && limit >= 5) {
                teams.sort(Comparator.comparingInt(Team::getMemberCount));
                teams.get(0).addMember(p);
                it.remove();
            }
        }
    }
}