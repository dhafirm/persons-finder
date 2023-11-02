package nz.co.dhafir.supplier.types;

import lombok.AllArgsConstructor;

/**
 * A wrapper around primitive types to give them readable and type safe
 * @param <T>
 */
@AllArgsConstructor
public class ValueType<T> {
    private T value;

    public <t> T value() {
        return value;
    }
}
