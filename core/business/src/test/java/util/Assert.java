package util;

import java.util.Optional;

public class Assert {
    public static void assertThrows(String message, Class<? extends Exception> expected, Executable executable) {
        String exceptionMessage = "Threw a different exception that expected! Expected:%s != Actual:%s";
        try {
            executable.execute();
            throw new AssertionError(String.format("%s Expected:%s != Actual:NONE", message, expected.getCanonicalName()));
        } catch (Throwable t) {
            if (t.getClass().equals(AssertionError.class)) {
                throw t;
            } else if (t.getClass().equals(RuntimeException.class)) {
                if (!expected.equals(RuntimeException.class)) {
                    Throwable cause = Optional.ofNullable(t.getCause())
                            .orElseThrow(() -> new AssertionError(String.format(exceptionMessage,
                                    expected.getCanonicalName(),
                                    NullPointerException.class.getCanonicalName())));
                    if (!(cause.getClass().isAssignableFrom(expected)))
                        throw new AssertionError(String.format(exceptionMessage, expected.getCanonicalName(), cause.getClass().getCanonicalName()));
                }
            } else {
                if (!(t.getClass().isAssignableFrom(expected)))
                    throw new AssertionError(String.format(exceptionMessage, expected.getCanonicalName(), t.getClass().getCanonicalName()));
            }
        }
    }
}
