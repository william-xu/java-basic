package com.xwl41.common.basic.helper;

//import java.io.Serial;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Java BigDecimal simple decorator for simplifying BigDecimal operations
 */
public class Decimal extends Number implements Comparable<Decimal> {

//    @Serial
    private static final long serialVersionUID = 20220401L;

    private BigDecimal value;

    private RoundingMode roundingMode = RoundingMode.HALF_UP;

    //////////////////////////////////////////////////////////////////////////////
    //    Constructors
    //////////////////////////////////////////////////////////////////////////////

    /**
     * no argument
     */
    public Decimal() {
        value = BigDecimal.ZERO;
    }

    /**
     * String argument
     *
     * @param value number value in String format
     */
    public Decimal(String value) {
        this.value = new BigDecimal(value);
    }

    /**
     * BigDecimal argument
     *
     * @param value BigDecimal instance
     */
    public Decimal(BigDecimal value) {
        this.value = newDecimal(value);
    }

    /**
     * Number Type argument
     *
     * @param t   number value
     * @param <T> subtype of Number
     */
    public <T extends Number> Decimal(T t) {
        this.value = newDecimal(t);
    }

    /**
     * Number Type argument with scale setting
     *
     * @param t     number value
     * @param scale scale for BigDecimal
     * @param <T>   subtype of Number
     */
    public <T extends Number> Decimal(T t, int scale) {
        this.value = newDecimal(t).setScale(scale);
    }

    /**
     * Number Type argument with scale and rounding mode setting
     *
     * @param t            number value
     * @param scale        scale for BigDecimal
     * @param roundingMode rounding mode for BigDecimal
     * @param <T>          subtype of Number
     */
    public <T extends Number> Decimal(T t, int scale, RoundingMode roundingMode) {
        this.value = newDecimal(t).setScale(scale, roundingMode);
    }

    //////////////////////////////////////////////////////////////////////////////
    //    Operations
    //////////////////////////////////////////////////////////////////////////////

    /**
     * add
     *
     * @param t   number value
     * @param <T> subtype of Number
     * @return Decimal instance
     */
    public <T extends Number> Decimal add(T t) {
        this.value = this.value.add(nullToZero(t));
        return this;
    }

    /**
     * subtract
     *
     * @param subtrahend number value
     * @param <T>        subtype of Number
     * @return Decimal instance
     */
    public <T extends Number> Decimal subtract(T subtrahend) {
        this.value = this.value.subtract(nullToZero(subtrahend));
        return this;
    }

    /**
     * multiply
     *
     * @param t   number value
     * @param <T> subtype of Number
     * @return Decimal instance
     */
    public <T extends Number> Decimal multiply(T t) {
        this.value = this.value.multiply(nullToZero(t));
        return this;
    }

    /**
     * divide
     *
     * @param t   number value
     * @param <T> subtype of Number
     * @return Decimal instance
     */
    public <T extends Number> Decimal divide(T t) {
        this.value = this.value.divide(newDecimal(t), value.scale(), roundingMode);
        return this;
    }

    /**
     * divide with scale setting
     *
     * @param t     number value
     * @param scale scale of BigDecimal
     * @param <T>   subtype of Number
     * @return Decimal instance
     */
    public <T extends Number> Decimal divide(T t, int scale) {
        this.value = this.value.divide(newDecimal(t), scale, roundingMode);
        return this;
    }

    /**
     * divide with scale and rounding mode setting
     *
     * @param t            number value
     * @param scale        scale of BigDecimal
     * @param roundingMode rounding mode of BigDecimal
     * @param <T>          subtype of Number
     * @return Decimal instance
     */
    public <T extends Number> Decimal divide(T t, int scale, RoundingMode roundingMode) {
        value = this.value.divide(newDecimal(t), scale, roundingMode);
        return this;
    }

    /**
     * get BigDecimal value from Decimal instance
     *
     * @return BigDecimal instance
     */
    public BigDecimal get() {
        return value;
    }

    /**
     * set scale
     *
     * @param newScale new scale of BigDecimal
     * @return Decimal instance
     */
    public Decimal setScale(int newScale) {
        value = value.setScale(newScale);
        return this;
    }

    /**
     * set scale and rounding mode
     *
     * @param newScale     new scale of BigDecimal
     * @param roundingMode rounding mode of BigDecimal
     * @return Decimal instance
     */
    public Decimal setScale(int newScale, RoundingMode roundingMode) {
        value = value.setScale(newScale, roundingMode);
        this.roundingMode = roundingMode;
        return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    //    new instance methods
    //////////////////////////////////////////////////////////////////////////////
    public static Decimal newDecimal() {
        return new Decimal();
    }

    private static <T extends Number> BigDecimal newDecimal(T value) {
        return new BigDecimal(String.valueOf(value));
    }

    //////////////////////////////////////////////////////////////////////////////
    //    private methods
    //////////////////////////////////////////////////////////////////////////////
    private static <T extends Number> BigDecimal nullToZero(T value) {
        return value == null ? BigDecimal.ZERO : newDecimal(value);
    }

    //////////////////////////////////////////////////////////////////////////////
    //    override parent methods
    //////////////////////////////////////////////////////////////////////////////
    @Override
    public int compareTo(Decimal o) {
        return this.get().compareTo(o.get());
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Decimal that = (Decimal) o;
        return value.equals(that.get());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
