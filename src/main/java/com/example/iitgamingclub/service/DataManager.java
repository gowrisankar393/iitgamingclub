package com.example.iitgamingclub.service;

import com.example.iitgamingclub.model.Participant;
import java.io.*;
import java.util.*;

/**
 * Class: DataManager
 * Pattern: Singleton
 * Handles all File I/O operations (Import/Export/Save).
 */
public class DataManager {
    private static DataManager instance;
    private Map<String, List<Participant>> loadedFiles;
    private String currentActiveFile;

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
            String line = br.readLine(); // Read header
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
        if (filePath == null) return;
        List<Participant> list = getParticipants(filePath);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType\n");
            for (Participant p : list) {
                bw.write(p.toString() + "\n");
            }
        }
    }

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
    public void setCurrentActiveFile(String f) { this.currentActiveFile = f; }

    public String generateNextId(String filePath) {
        return "P" + (getParticipants(filePath).size() + 101);
    }

    public void addParticipant(String filePath, Participant p) {
        if (loadedFiles.containsKey(filePath)) {
            loadedFiles.get(filePath).add(p);
        }
    }
}