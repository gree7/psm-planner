package cz.agents.dimap;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import cz.agents.dimap.planset.PlanSetType;

public final class Settings {

    public static final boolean IS_REPLACEMENT_FORCED = false;
    
    public static final boolean RUN_SEQUENTIALLY = false;
    public static final int THREAD_POOL_SIZE = 5;
    
    public static boolean IS_CODMAP = false;
    
    public enum InitialLandmarks {RELAXED, PUBLIC}

    public static boolean USE_AGENT_INDEXES = true;

    public static boolean USE_CAUSAL_LINKS = false;
    public static boolean USE_REQUIRED_CAUSAL_LANDMARKS = false;

    public static boolean REPLACE_INTERNALLY_DEPENDENT_ACTIONS = false;
    public static final boolean USE_INTERNAL_DEPENDENCIES_GRAPH = true;

    public static PlanSetType PLAN_SET_TYPE = PlanSetType.PSM;

    public static boolean RESET_IN_SECOND_ITERATION = false;

    public static Set<InitialLandmarks> USE_INITIAL_LANDMARKS = new HashSet<>();
    public static boolean USE_INITIAL_LANDMARKS_IN_FIRST_ITER_ONLY = true;

    public static boolean ALLOW_PRIVATE_GOALS = false;
    public static boolean USE_MASTRIPS = false;
    public static boolean MASTRIPS_PUBLIC_FACT_HAS_TO_BE_IN_EFFECT = false;

    public static final boolean MAKE_CONSTANTS_PUBLIC_IN_FMAP = false;

    public static boolean VERIFY_PLANS = false;
    public static boolean VERIFY_PLANS_STAR = true;
    public static final boolean VERIFY_RELAXED_PLAN = true;

    public static boolean VERBOSE = false;

    public static boolean USE_IPC_TRANSLATION_CACHE = false;
    public static boolean FORCE_IPC_TRANSLATION = false;

    public static boolean DOT_OUTPUT_ALLOWED = false;
    public static boolean DOT_OUTPUT_SOLUTION = false;
    public static boolean DOT_CAUSAL_GRAPH = false;
    public static boolean DOT_LANDMARKS = false;
    public static boolean DOT_BLACKWHITE = false;
    public static boolean DOT_OUTPUT_ASYNCHRONOUS = false;

    public static boolean DEBUG = false;
    public static boolean DEBUG_COSTS = false;
    public static boolean DEBUG_LANDMARKS = false;
    
    public static boolean DELETE_TMP_FILES_ON_EXIT = true;
    public static boolean DELETE_TMP_FILES_ON_RESTART = true;
    public static String TMP_FILES_PREFIX = "dimap-";

    public static int MAX_ITERATIONS = -1; // -1 for unlimited

    public static long TIMEOUT_MINUTES = 20;

    public static Options CMD_OPTIONS_BASIC = createBasicCmdLineOptions();
    public static Options CMD_OPTIONS_ADVANCED = createAdvancedCmdLineOptions();

    public static final boolean VERIFY_SOLUTION = true;

    public static final boolean LAZY_RELAXED_AGENTS = true;

    private Settings() {}

    public static CommandLine parseCmdLineOptions(String[] args, Options options) {
        CommandLine cmdLine;
        try {
            CommandLineParser parser = new PosixParser();
            cmdLine = parser.parse(options, args);
        } catch (ParseException e) {
            cmdLine = null;
        }
        return cmdLine;
    }

    private static Options createBasicCmdLineOptions() {
        Options cmdLineOptions = new Options();
        cmdLineOptions.addOption("p", "plan-set-type", true, "Representation of plans; valid values: PSM, SIMPLE, CONCURRENT");
        cmdLineOptions.addOption("D", "replace-internal-dependencies", false, "Replace internal dependencies between public actions");
        cmdLineOptions.addOption("r", "use-relaxed-plan", false, "Use a relaxed plan landmark heuristic");
        cmdLineOptions.addOption("I", "use-public-plan", false, "Use a public plan landmark heuristic");
        cmdLineOptions.addOption("S", "check-plans-star", false, "Check plans and forbid future plans with minimal unacceptable prefix of a rejected plan");
        cmdLineOptions.addOption("v", "verbose", false, "Display additional progress information");
        cmdLineOptions.addOption("d", "debug", false, "Display additional debug information");
        cmdLineOptions.addOption("t", "timeout", true, "Timeout in minutes");
        cmdLineOptions.addOption("C", "codmap", false, "Use format for CoDMAP competition");
        cmdLineOptions.addOption("h", "help", false, "Show advanced command line options");
        return cmdLineOptions;
    }

    private static Options createAdvancedCmdLineOptions() {
        Options cmdLineOptions = createBasicCmdLineOptions();
        cmdLineOptions.addOption("s", "reset-second-iter", false, "Reset landmarks in second iteration");
        cmdLineOptions.addOption("P", "check-plans-part", false, "Check plans but keep acceptable prefix of a rejected plan");
        cmdLineOptions.addOption("l", "use-causal-links", false, "Use causal links instead of plan action landmarks");
        cmdLineOptions.addOption("i", "disable-agfalseent-indexes", false, "Disable agent indexes");
        cmdLineOptions.addOption("m", "use-ma-strips", false, "Use MA-Strips privacy classifications instead of FMAP (predicate-name based)");
        cmdLineOptions.addOption("g", "allow-private-goals", false, "Allow private goals (by compilation)");
        return cmdLineOptions;
    }

    public static void setFromCommandLine(CommandLine cmdLine) {
        REPLACE_INTERNALLY_DEPENDENT_ACTIONS = cmdLine.hasOption("D");
        IS_CODMAP = cmdLine.hasOption("C");
        USE_INITIAL_LANDMARKS_IN_FIRST_ITER_ONLY = false;
        if (cmdLine.hasOption("r")) {
            USE_INITIAL_LANDMARKS.add(InitialLandmarks.RELAXED);
            Settings.USE_INITIAL_LANDMARKS_IN_FIRST_ITER_ONLY = true;
        } 
        if (cmdLine.hasOption("I")) {
            USE_INITIAL_LANDMARKS.add(InitialLandmarks.PUBLIC);
            Settings.USE_INITIAL_LANDMARKS_IN_FIRST_ITER_ONLY = true;
        }

        Settings.RESET_IN_SECOND_ITERATION = cmdLine.hasOption("s");

        VERIFY_PLANS = false;
        if (cmdLine.hasOption("P")) {
            Settings.VERIFY_PLANS = true;
        }
        if (cmdLine.hasOption("S")) {
            Settings.VERIFY_PLANS = true;
            Settings.VERIFY_PLANS_STAR = true;
        }

        PLAN_SET_TYPE = PlanSetType.PSM;
        if (cmdLine.hasOption("p")) {
            try {
                Settings.PLAN_SET_TYPE = PlanSetType.valueOf(cmdLine.getOptionValue("p"));
            }
            catch (IllegalArgumentException e) {
                System.err.println("ERROR: Invalid value of the plan-set-type argument.  Valid values are PSM, SIMPLE, and CONCURRENT.");
                System.exit(-1);
            }
        }

        TIMEOUT_MINUTES = -1;
        if (cmdLine.hasOption("t")) {
            try {
                Settings.TIMEOUT_MINUTES = Integer.parseInt(cmdLine.getOptionValue("t"));
                if (Settings.TIMEOUT_MINUTES <= 0) {
                    throw new NumberFormatException();
                }
            }
            catch (NumberFormatException e) {
                System.err.println("ERROR: Invalid value of the timeout argument.  A positive integer value is required.");
                System.exit(-1);
            }
        }

        USE_CAUSAL_LINKS = false;
        if (cmdLine.hasOption("l")) {
            Settings.USE_CAUSAL_LINKS = true;
        }

        USE_AGENT_INDEXES = true;
        if (cmdLine.hasOption("i")) {
            Settings.USE_AGENT_INDEXES = false;
        }

        Settings.USE_MASTRIPS = cmdLine.hasOption("m");
        Settings.ALLOW_PRIVATE_GOALS = cmdLine.hasOption("g");
        Settings.VERBOSE = cmdLine.hasOption("v");
        Settings.DEBUG = cmdLine.hasOption("d");
    }

    public static String generateCurrentSettingsDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.PLAN_SET_TYPE).append("-");

        if (Settings.IS_CODMAP) {
            builder.append("CoDMAP-");
        }

        if (Settings.REPLACE_INTERNALLY_DEPENDENT_ACTIONS) {
            builder.append("D");
        }

        if (Settings.USE_INITIAL_LANDMARKS.contains(InitialLandmarks.RELAXED)) {
            builder.append("r");
        }

        if (Settings.USE_INITIAL_LANDMARKS.contains(InitialLandmarks.PUBLIC)) {
            builder.append("I");
        }

        if (Settings.VERIFY_PLANS) {
            if (Settings.VERIFY_PLANS_STAR) {
                builder.append("S");
            }
            else {
                builder.append("P");
            }
        }

        if (Settings.RESET_IN_SECOND_ITERATION) { builder.append("s"); }
        if (Settings.USE_CAUSAL_LINKS) { builder.append("l"); }
        if (!Settings.USE_AGENT_INDEXES) { builder.append("i"); }
        if (Settings.VERBOSE) { builder.append("v"); }
        if (Settings.DEBUG) { builder.append("d"); }
        if (Settings.USE_MASTRIPS) { builder.append("m"); }
        if (Settings.ALLOW_PRIVATE_GOALS) { builder.append("g"); }

        builder.append("-t").append(Settings.TIMEOUT_MINUTES <= 0 ? "X" : Settings.TIMEOUT_MINUTES);

        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(Settings.generateCurrentSettingsDescription());
    }

}
