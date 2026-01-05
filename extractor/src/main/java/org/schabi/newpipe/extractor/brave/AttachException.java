package org.schabi.newpipe.extractor.brave;

import org.schabi.newpipe.extractor.exceptions.ParsingException;

import java.util.ArrayList;
import java.util.List;

/**
 * Substitute for ParsingException.
 * <p>
 * Attach data that might help the developer to fix the problem.
 */
public class AttachException extends ParsingException {
    private final List<String> exceptionData = new ArrayList<>();

    public AttachException(final String message) {
        super(message);
    }

    /**
     * Add useful data.
     *
     * @param data that might help the developer
     */
    public void addExceptionData(final String data) {
        exceptionData.add(data);
    }

    /**
     * Add useful data as key=value
     *
     * @param key   to describe the value
     * @param value that might help the developer
     */
    public void addExceptionData(final String key, final String value) {
        exceptionData.add(String.format("{%s}={%s}", key, value));
    }

    public List<String> getExceptionData() {
        return exceptionData;
    }
}
