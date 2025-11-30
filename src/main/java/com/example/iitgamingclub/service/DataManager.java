package com.example.iitgamingclub.service;

import com.example.iitgamingclub.model.Participant;
import java.io.*;
import java.util.*;

public class DataManager {
    private static DataManager instance;
    private Map<String, List<Participant>> loadedFiles; // Caches loaded CSVs
    private String currentActiveFile; // Tracks the currently selected CSV path

    private DataManager() {
        loadedFiles = new HashMap<>();
    }

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }

    public void importFile(File file) throws IOException {
        if (!file.getName().toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("Format is not supported. Please import a CSV file");
        }

        List<Participant> participants = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 8) {
                    participants.add(new Participant(
                            data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(),
                            Integer.parseInt(data[4].trim()), data[5].trim(),
                            Integer.parseInt(data[6].trim()), data[7].trim()
                    ));
                }
            }
        }
        loadedFiles.put(file.getAbsolutePath(), participants);
        currentActiveFile = file.getAbsolutePath();
    }

    public void saveToFile(String filePath) throws IOException {
        if (filePath == null || !loadedFiles.containsKey(filePath)) return;

        List<Participant> list = loadedFiles.get(filePath);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write Header
            bw.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType\n");
            for (Participant p : list) {
                bw.write(p.toString() + "\n");
            }
        }
    }

    // Creates a new empty CSV file
    public void createNewFile(String filePath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType\n");
        }
        loadedFiles.put(filePath, new ArrayList<>());
        currentActiveFile = filePath;
    }

    public List<Participant> getParticipants(String filePath) {
        return loadedFiles.getOrDefault(filePath, new ArrayList<>());
    }

    public List<String> getLoadedFilePaths() {
        return new ArrayList<>(loadedFiles.keySet());
    }

    public String getCurrentActiveFile() { return currentActiveFile; }

    // Generates Next ID (e.g., P101) based on list size
    public String generateNextId(String filePath) {
        List<Participant> list = getParticipants(filePath);
        return "P" + (list.size() + 101);
    }

    public void addParticipant(String filePath, Participant p) {
        if (loadedFiles.containsKey(filePath)) {
            loadedFiles.get(filePath).add(p);
        }
    }
}