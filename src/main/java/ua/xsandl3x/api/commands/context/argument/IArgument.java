package ua.xsandl3x.api.commands.context.argument;

import java.util.Optional;
import java.util.function.Function;

/**
 * Represents a command argument as an object
 */
public interface IArgument {

    /**
     * A method that allows converting the argument value to the specified type.
     *
     * @param <T>     The type to which the argument needs to be converted.
     * @param mapper  A function that performs the conversion from a string to a specific type.
     * @return        An Optional containing the converted value if the conversion is successful, otherwise an empty Optional.
     */
    <T> Optional<T> as(Function<String, T> mapper);

    /**
     * Gets the index of the argument
     *
     * @return The ID of the argument
     */
    int getIndex();

    /**
     * Gets the value of the argument
     *
     * @return The value of the argument
     */
    String getArgumentValue();

    /**
     * Convert the argument value to an integer.
     *
     * @return Optional containing the converted integer if the conversion is successful, or an empty Optional otherwise.
     */
    default Optional<Integer> asInt() {
        return as(Integer::parseInt);
    }

    /**
     * Convert the argument value to a long number.
     *
     * @return Optional containing the converted long number if the conversion is successful, or an empty Optional otherwise.
     */
    default Optional<Long> asLong() {
        return as(Long::parseLong);
    }

    /**
     * Convert the argument value to a floating-point number.
     *
     * @return Optional containing the converted floating-point number if the conversion is successful, or an empty Optional otherwise.
     */
    default Optional<Double> asDouble() {
        return as(Double::parseDouble);
    }

    /**
     * Convert the argument value to a floating-point number.
     *
     * @return Optional containing the converted floating-point number if the conversion is successful, or an empty Optional otherwise.
     */
    default Optional<Float> asFloat() {
        return as(Float::parseFloat);
    }

    /**
     * Convert the argument value to a Boolean.
     *
     * @return Optional containing the converted Boolean value if the conversion is successful, otherwise an empty Optional.
     */
    default Optional<Boolean> asBoolean() {
        return as(Boolean::parseBoolean);
    }

    /**
     * Convert the argument value to a String.
     *
     * @return Optional containing the converted String value if the conversion is successful, otherwise an empty Optional.
     */
    default Optional<String> asString() {
        return as(mapper -> mapper);
    }
}
