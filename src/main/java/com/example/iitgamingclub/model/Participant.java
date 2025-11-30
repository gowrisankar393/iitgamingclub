package com.example.iitgamingclub.model;

/**
 * Class: Participant
 * Represents a single member. Implements Encapsulation.
 */
public class Participant implements Comparable<Participant> {
    private String id;
    private String name;
    private String email;
    private String preferredGame;
    private int skillLevel;
    private String preferredRole;
    private int personalityScore;
    private String personalityType;

    public Participant(String id, String name, String email, String preferredGame,
                       int skillLevel, String preferredRole, int personalityScore, String personalityType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.preferredGame = preferredGame;
        this.skillLevel = skillLevel;
        this.preferredRole = preferredRole;
        this.personalityScore = personalityScore;
        this.personalityType = personalityType;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPreferredGame() { return preferredGame; }
    public void setPreferredGame(String preferredGame) { this.preferredGame = preferredGame; }
    public int getSkillLevel() { return skillLevel; }
    public String getPreferredRole() { return preferredRole; }
    public int getPersonalityScore() { return personalityScore; }
    public String getPersonalityType() { return personalityType; }

    @Override
    public String toString() {
        // Formats data for CSV storage
        return String.join(",", id, name, email, preferredGame,
                String.valueOf(skillLevel), preferredRole,
                String.valueOf(personalityScore), personalityType);
    }

    @Override
    public int compareTo(Participant o) {
        return this.id.compareTo(o.id);
    }
}