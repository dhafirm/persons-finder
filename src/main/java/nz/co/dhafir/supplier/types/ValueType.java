package nz.co.dhafir.supplier.types;

import lombok.AllArgsConstructor;

import java.util.Objects;

/**
 * A wrapper around primitive types to make them readable and type safe.
 * @param <T>
 */
@AllArgsConstructor
public class ValueType<T> {
    private T value;

    public <t> T value() {
        return value;
    }

    public static <T> ValueType of(T value) {
        return new ValueType<T>(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ValueType<?> valueType)) return false;
        return Objects.equals(value, valueType.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
