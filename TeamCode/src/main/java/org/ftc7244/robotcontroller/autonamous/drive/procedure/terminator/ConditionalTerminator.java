package org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator;

/**
 * Is a way of mixing and matching terminators so that they can be conditional and allowing for
 * more than one terminator.
 */
public class ConditionalTerminator extends DriveTerminator {

    private TerminationMode mode;
    private DriveTerminator[] terminators;

    /**
     * It by default uses ${@link TerminationMode#OR} and allows for a dynmaic amount of terminators
     * to decided if the code should stop
     *
     * @param terminators dynamic amount of terminators
     */
    public ConditionalTerminator(DriveTerminator... terminators) {
        this(TerminationMode.OR, terminators);
    }

    /**
     * Similar to the single argument but allows for changing of modes.
     * <p>
     * There are different supported modes:
     * ${@link TerminationMode#OR} will stop if any are true
     * ${@link TerminationMode#AND} will stop if ONLY if all true
     *
     * @param mode        the mode to use
     * @param terminators dynamic amount of terminators
     */
    public ConditionalTerminator(TerminationMode mode, DriveTerminator... terminators) {
        this.mode = mode;
        this.terminators = terminators;
    }

    @Override
    public boolean shouldTerminate(double error) {
        switch (mode) {
            case OR:
                for (DriveTerminator terminator : terminators)
                    if (terminator.shouldTerminate(error)) return true;
                return false;
            case AND:
                for (DriveTerminator terminator : terminators)
                    if (!terminator.shouldTerminate(error)) return false;
                return true;
            default:
                return true;
        }
    }
}
