package com.example.iitgamingclub.model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private int teamId;
    private List<Participant> members;

    public Team(int teamId) {
        this.teamId = teamId;
        this.members = new ArrayList<>();
    }

    public void addMember(Participant p) {
        members.add(p);
    }

    public List<Participant> getMembers() {
        return members;
    }

    public double getAverageSkill() {
        return members.stream().mapToInt(Participant::getSkillLevel).average().orElse(0.0);
    }

    public int getMemberCount() {
        return members.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== Team " + teamId + " (Avg Skill: " + String.format("%.2f", getAverageSkill()) + ") ===\n");
        for (Participant p : members) {
            sb.append(String.format("  - %s (%s, %s, %s)\n", p.getName(), p.getPreferredRole(), p.getPersonalityType(), p.getPreferredGame()));
        }
        return sb.toString();
    }
}