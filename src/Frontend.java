import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Frontend implementation for Project 1.
 *
 * This class:
 * - Reads user input using a Scanner
 * - Parses commands
 * - Calls backend methods (must match BackendInterface exactly)
 * - Prints results to standard output
 */
public class Frontend implements FrontendInterface {

    private Scanner in;               // Scanner for user input
    private BackendInterface backend; // Reference to backend

    /**
     * Required constructor.
     * The frontend must use only this Scanner for input.
     */
    public Frontend(Scanner in, BackendInterface backend) {
        this.in = in;
        this.backend = backend;
    }

    /**
     * Displays instructions once.
     * Then repeatedly reads user commands until "quit" is entered.
     *
     * Using hasNextLine() avoids crashes on EOF in automated tests.
     */
    @Override
    public void runCommandLoop() {
        showCommandInstructions();

        while (in.hasNextLine()) {
            System.out.print("> ");
            String command = in.nextLine();

            if (command.equalsIgnoreCase("quit"))
                break;

            try {
                processSingleCommand(command);
            } catch (Exception e) {
                // Catch backend errors and allow program to continue
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Prints the command instructions.
     */
    @Override
    public void showCommandInstructions() {
        System.out.println("Available Commands:");
        System.out.println("submit NAME CONTINENT SCORE COLLECTABLES LEVEL COMPLETION_TIME");
        System.out.println("submit multiple FILEPATH");
        System.out.println("level MAX");
        System.out.println("level MIN to MAX");
        System.out.println("time TIME");
        System.out.println("show MAX_COUNT");
        System.out.println("show most collectables");
        System.out.println("help");
        System.out.println("quit");
    }

    /**
     * Parses and executes a single command string.
     */
    @Override
    public void processSingleCommand(String command) {

        if (command == null || command.trim().isEmpty()) {
            System.out.println("Invalid command.");
            return;
        }

        String[] parts = command.trim().split("\\s+");
        String keyword = parts[0].toLowerCase();

        switch (keyword) {
            case "submit":
                handleSubmit(parts);
                break;
            case "level":
                handleLevel(parts);
                break;
            case "time":
                handleTime(parts);
                break;
            case "show":
                handleShow(parts);
                break;
            case "help":
                showCommandInstructions();
                break;
            default:
                System.out.println("Invalid command.");
        }
    }

    /**
     * Handles submit commands.
     *
     * Supported forms:
     * - submit multiple FILEPATH
     * - submit NAME CONTINENT SCORE COLLECTABLES LEVEL COMPLETION_TIME
     */
    private void handleSubmit(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Invalid submit syntax.");
            return;
        }

        // submit multiple FILEPATH
        if (parts[1].equalsIgnoreCase("multiple")) {
            if (parts.length != 3) {
                System.out.println("Invalid submit multiple syntax.");
                return;
            }

            try {
                backend.readData(parts[2]);
                System.out.println("File loaded.");
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
            return;
        }

        // submit NAME CONTINENT SCORE COLLECTABLES LEVEL COMPLETION_TIME
        if (parts.length != 7) {
            System.out.println("Invalid submit syntax.");
            return;
        }

        try {
            String name = parts[1];
            String continentText = parts[2];
            int score = Integer.parseInt(parts[3]);
            int collectables = Integer.parseInt(parts[4]);
            int level = Integer.parseInt(parts[5]);
            String time = parts[6];

            GameRecord.Continent continent = parseContinent(continentText);
            if (continent == null) {
                System.out.println("Invalid continent.");
                return;
            }

            GameRecord record = new GameRecord(name, continent, score, collectables, level, time);
            backend.addRecord(record);

            System.out.println("Record added.");

        } catch (NumberFormatException e) {
            System.out.println("Numeric value expected.");
        }
    }

    /**
     * Converts user input into a GameRecord.Continent enum value.
     *
     * Accepts:
     * - Full enum names: AFRICA, ASIA, NORTH_AMERICA, ...
     * - Common abbreviations: NA, SA, EU, AU, AF, AS
     *
     * Returns null when the input does not match any supported value.
     */
    private GameRecord.Continent parseContinent(String input) {
        if (input == null) return null;

        String s = input.trim().toUpperCase();

        // Support common abbreviations
        switch (s) {
            case "NA": return GameRecord.Continent.NORTH_AMERICA;
            case "SA": return GameRecord.Continent.SOUTH_AMERICA;
            case "EU": return GameRecord.Continent.EUROPE;
            case "AU": return GameRecord.Continent.AUSTRALIA;
            case "AF": return GameRecord.Continent.AFRICA;
            case "AS": return GameRecord.Continent.ASIA;
        }

        // Support full enum names (including NORTH_AMERICA, SOUTH_AMERICA)
        try {
            return GameRecord.Continent.valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Handles level range command.
     * Uses backend.getAndSetRange(low, high).
     *
     * Supported forms:
     * - level MAX
     * - level MIN to MAX
     */
    private void handleLevel(String[] parts) {

        try {
            // level MAX -> low unbounded, high = MAX
            if (parts.length == 2) {
                Integer high = Integer.parseInt(parts[1]);
                backend.getAndSetRange(null, high);
                return;
            }

            // level MIN to MAX
            if (parts.length == 4 && parts[2].equalsIgnoreCase("to")) {
                Integer low = Integer.parseInt(parts[1]);
                Integer high = Integer.parseInt(parts[3]);
                backend.getAndSetRange(low, high);
                return;
            }

            System.out.println("Invalid level syntax.");
        } catch (NumberFormatException e) {
            System.out.println("Level must be numeric.");
        }
    }

    /**
     * Handles time filter command.
     * Uses backend.applyAndSetFilter(time).
     *
     * Supported form:
     * - time hhh:mm:ss
     */
    private void handleTime(String[] parts) {

        if (parts.length != 2) {
            System.out.println("Invalid time syntax.");
            return;
        }

        backend.applyAndSetFilter(parts[1]);
    }

    /**
     * Handles show commands.
     *
     * Supported forms:
     * - show MAX_COUNT
     * - show most collectables
     */
    private void handleShow(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Invalid show syntax.");
            return;
        }

        // show most collectables
        if (parts.length == 3
                && parts[1].equalsIgnoreCase("most")
                && parts[2].equalsIgnoreCase("collectables")) {

            List<String> topTen = backend.getTopTen();
            for (String name : topTen) {
                System.out.println(name);
            }
            return;
        }

        // show MAX_COUNT
        try {
            int max = Integer.parseInt(parts[1]);

            // BackendInterface does not provide getRecords().
            // getAndSetRange(null, null) retrieves record names using the currently set range/filter.
            List<String> records = backend.getAndSetRange(null, null);

            for (int i = 0; i < records.size() && i < max; i++) {
                System.out.println(records.get(i));
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid show syntax.");
        }
    }
}