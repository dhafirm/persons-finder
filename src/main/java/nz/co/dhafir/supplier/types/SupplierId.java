package nz.co.dhafir.supplier.types;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class SupplierId extends ValueType<Long> {
    public SupplierId(long value) {
        super(value);
    }

    public static SupplierId of(long supplierId) {
        return new SupplierId(supplierId);
    }

}

