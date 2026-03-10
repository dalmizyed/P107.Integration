import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;

/**
 * Tests for Frontend class.
 * These tests verify that each command type executes correctly
 * and produces expected output without crashing.
 */
public class FrontendTests {

    /**
     * Tests that a valid submit command prints confirmation.
     */
    @Test
    public void roleTest1_submitPrintsRecordAdded() {
        BackendInterface backend = new Backend_Placeholder();
        Frontend frontend = new Frontend(new Scanner(""), backend);

        // Capture printed output
        TextUITester tester = new TextUITester("");

        frontend.processSingleCommand(
            "submit John NA 1000 10 5 001:00:00"
        );

        String output = tester.checkOutput();

        // Verify confirmation message appears
        assertTrue(output.contains("Record added."));
    }

    /**
     * Tests level and time filter commands execute without errors.
     */
    @Test
    public void roleTest2_filterCommandsDoNotPrintErrors() {
        BackendInterface backend = new Backend_Placeholder();
        Frontend frontend = new Frontend(new Scanner(""), backend);

        TextUITester tester = new TextUITester("");

        frontend.processSingleCommand("level 10");
        frontend.processSingleCommand("level 5 to 15");
        frontend.processSingleCommand("time 001:00:00");

        String output = tester.checkOutput();

        // Ensure no syntax or numeric errors are printed
        assertFalse(output.contains("Invalid level syntax."));
        assertFalse(output.contains("Level must be numeric."));
        assertFalse(output.contains("Invalid time syntax."));
    }

    /**
     * Tests show commands execute without syntax errors.
     */
    @Test
    public void roleTest3_showCommandsDoNotPrintErrors() {
        BackendInterface backend = new Backend_Placeholder();
        Frontend frontend = new Frontend(new Scanner(""), backend);

        TextUITester tester = new TextUITester("");

        frontend.processSingleCommand("show 5");
        frontend.processSingleCommand("show most collectables");

        String output = tester.checkOutput();

        // Ensure no invalid syntax message appears
        assertFalse(output.contains("Invalid show syntax."));
    }

    /**
     * Tests the full command loop including help and quit behavior.
     */
    @Test
    public void roleTest4_runCommandLoopDisplaysHelpAndExits() {
        BackendInterface backend = new Backend_Placeholder();

        // Simulate user typing help and then quit
        Frontend frontend = new Frontend(
            new Scanner("help\nquit\n"),
            backend
        );

        TextUITester tester = new TextUITester("");

        frontend.runCommandLoop();

        String output = tester.checkOutput();

        // Verify help text and prompt were printed
        assertTrue(output.contains("Available Commands:"));
        assertTrue(output.contains("> "));
    }
}