package com.noleme.flow.connect.tablesaw.dataframe.descriptor;

import tech.tablesaw.api.*;
import tech.tablesaw.columns.Column;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This class is expected to simplify the signature of other classes interacting with tablesaw Columns and Rows by providing a centralized "repository" of references to Column-related methods.
 * These methods have type-sensitive names and can be "hard" to abstract away ; eg. in order to abstract both the creation of a column and its subsequent modification within a row, you need to know which factory method and which setter to call.
 * By having these singleton descriptors, one can create simpler class signatures that simply request a coherent set of references.
 * Before these, we had to manually require Function and BiFunction of all sorts in TableProcessor implementations, which was rather confusing and got out of hand very quickly.
 *
 * At the time of this writing, the class has a private constructor, but this could be modified if it turns out we want to allow for custom column types or custom descriptors.
 *
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/08/14
 */
public final class ColumnDescriptor<T, C extends Column<T>>
{
    private final ColumnType type;
    private final Class<C> columnType;
    private final BiFunction<Table, String, C> columnGetter;
    private final BiFunction<String, Integer, C> columnFactory;
    private final Function<Row, Function<String, T>> rowGetterProvider;
    private final Function<Row, BiConsumer<String, T>> rowSetterProvider;

    public static final ColumnDescriptor<String, StringColumn> STRING = new ColumnDescriptor<>(
        ColumnType.STRING,
        StringColumn.class,
        Table::stringColumn,
        StringColumn::create,
        row -> row::getString,
        row -> row::setString
    );
    public static final ColumnDescriptor<String, TextColumn> TEXT = new ColumnDescriptor<>(
        ColumnType.TEXT,
        TextColumn.class,
        Table::textColumn,
        TextColumn::create,
        row -> row::getText,
        row -> row::setText
    );
    public static final ColumnDescriptor<Short, ShortColumn> SHORT = new ColumnDescriptor<>(
        ColumnType.SHORT,
        ShortColumn.class,
        Table::shortColumn,
        ShortColumn::create,
        row -> row::getShort,
        row -> row::setShort
    );
    public static final ColumnDescriptor<Integer, IntColumn> INTEGER = new ColumnDescriptor<>(
        ColumnType.INTEGER,
        IntColumn.class,
        Table::intColumn,
        IntColumn::create,
        row -> row::getInt,
        row -> row::setInt
    );
    public static final ColumnDescriptor<Long, LongColumn> LONG = new ColumnDescriptor<>(
        ColumnType.LONG,
        LongColumn.class,
        Table::longColumn,
        LongColumn::create,
        row -> row::getLong,
        row -> row::setLong
    );
    public static final ColumnDescriptor<Float, FloatColumn> FLOAT = new ColumnDescriptor<>(
        ColumnType.FLOAT,
        FloatColumn.class,
        Table::floatColumn,
        FloatColumn::create,
        row -> row::getFloat,
        row -> row::setFloat
    );
    public static final ColumnDescriptor<Double, DoubleColumn> DOUBLE = new ColumnDescriptor<>(
        ColumnType.DOUBLE,
        DoubleColumn.class,
        Table::doubleColumn,
        DoubleColumn::create,
        row -> row::getDouble,
        row -> row::setDouble
    );
    public static final ColumnDescriptor<Boolean, BooleanColumn> BOOLEAN = new ColumnDescriptor<>(
        ColumnType.BOOLEAN,
        BooleanColumn.class,
        Table::booleanColumn,
        BooleanColumn::create,
        row -> row::getBoolean,
        row -> row::setBoolean
    );
    public static final ColumnDescriptor<Instant, InstantColumn> INSTANT = new ColumnDescriptor<>(
        ColumnType.INSTANT,
        InstantColumn.class,
        Table::instantColumn,
        InstantColumn::create,
        row -> row::getInstant,
        row -> row::setInstant
    );

    private static final Map<ColumnType, ColumnDescriptor<?, ?>> byType = new HashMap<>();

    static {
        try {
            for (Field field : ColumnDescriptor.class.getDeclaredFields())
            {
                if (Modifier.isStatic(field.getModifiers()) && ColumnDescriptor.class.isAssignableFrom(field.getType()))
                {
                    ColumnDescriptor<?, ?> cd = (ColumnDescriptor<?, ?>) field.get(null);
                    byType.put(cd.type, cd);
                }
            }
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     *
     * @param columnType
     * @param columnGetter
     * @param columnFactory
     * @param rowGetterProvider
     * @param rowSetterProvider
     */
    private ColumnDescriptor(
        ColumnType type,
        Class<C> columnType,
        BiFunction<Table, String, C> columnGetter,
        BiFunction<String, Integer, C> columnFactory,
        Function<Row, Function<String, T>> rowGetterProvider,
        Function<Row, BiConsumer<String, T>> rowSetterProvider
    )
    {
        this.type = type;
        this.columnType = columnType;
        this.columnGetter = columnGetter;
        this.columnFactory = columnFactory;
        this.rowGetterProvider = rowGetterProvider;
        this.rowSetterProvider = rowSetterProvider;
    }

    /**
     *
     * @param type
     * @return
     */
    public static ColumnDescriptor<?, ?> forType(ColumnType type)
    {
        return byType.get(type);
    }

    /**
     *
     * @return
     */
    public ColumnType getType()
    {
        return this.type;
    }

    /**
     *
     * @return
     */
    public Class<C> getColumnType()
    {
        return this.columnType;
    }

    /**
     *
     * @param table
     * @param name
     * @return
     */
    public Column<T> getColumn(Table table, String name)
    {
        return this.columnGetter.apply(table, name);
    }

    /**
     *
     * @param columnName
     * @param rowCount
     * @return
     */
    public Column<T> createColumn(String columnName, int rowCount)
    {
        return this.columnFactory.apply(columnName, rowCount);
    }

    /**
     *
     * @param row
     * @param columnName
     * @return
     */
    public T getValue(Row row, String columnName)
    {
        return this.rowGetterProvider.apply(row).apply(columnName);
    }

    /**
     *
     * @param row
     * @param columnName
     * @param value
     */
    public void setValue(Row row, String columnName, T value)
    {
        this.rowSetterProvider.apply(row).accept(columnName, value);
    }
}
