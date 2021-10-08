package org.jetbrains.dataframe

import org.jetbrains.dataframe.aggregation.Aggregatable
import org.jetbrains.dataframe.aggregation.AggregateColumnsSelector
import org.jetbrains.dataframe.aggregation.GroupByAggregateBody
import org.jetbrains.dataframe.columns.ColumnReference
import org.jetbrains.dataframe.impl.aggregation.aggregateInternal
import org.jetbrains.dataframe.impl.aggregation.aggregators.Aggregators
import org.jetbrains.dataframe.impl.aggregation.comparableColumns
import org.jetbrains.dataframe.impl.aggregation.modes.aggregateAll
import org.jetbrains.dataframe.impl.aggregation.modes.aggregateFor
import org.jetbrains.dataframe.impl.aggregation.modes.of
import org.jetbrains.dataframe.impl.aggregation.numberColumns
import org.jetbrains.dataframe.impl.aggregation.receivers.AggregateBodyInternal
import org.jetbrains.dataframe.impl.aggregation.remainingColumns
import org.jetbrains.dataframe.impl.columns.*
import org.jetbrains.dataframe.impl.mapRows
import org.jetbrains.dataframe.impl.zero
import kotlin.reflect.KProperty

public interface DataFrameAggregations<out T> : Aggregatable<T>, DataFrameBase<T> {

    public fun <R> aggregate(body: GroupByAggregateBody<T, R>): DataRow<T> = aggregateInternal(body as AggregateBodyInternal<T, R>)[0]

    public fun count(predicate: RowFilter<T>? = null): Int =
        if (predicate == null) nrow() else rows().count { predicate(it, it) }

    // region min

    public fun min(): DataRow<T> = minFor(comparableColumns())

    public fun <C : Comparable<C>> minFor(columns: AggregateColumnsSelector<T, C?>): DataRow<T> = Aggregators.min.aggregateFor(this, columns)
    public fun minFor(vararg columns: String): DataRow<T> = minFor { columns.toComparableColumns() }
    public fun <C : Comparable<C>> minFor(vararg columns: ColumnReference<C?>): DataRow<T> = minFor { columns.toColumns() }

    public fun <C : Comparable<C>> min(columns: ColumnsSelector<T, C?>): C = minOrNull(columns)!!
    public fun min(vararg columns: String): Comparable<Any> = minOrNull(*columns)!!
    public fun <C : Comparable<C>> min(vararg columns: ColumnReference<C?>): C = minOrNull(*columns)!!
    public fun <C : Comparable<C>> min(vararg columns: KProperty<C?>): C = minOrNull(*columns)!!

    public fun <C : Comparable<C>> minOrNull(columns: ColumnsSelector<T, C?>): C? = Aggregators.min.aggregateAll(this, columns)
    public fun minOrNull(vararg columns: String): Comparable<Any?>? = minOrNull { columns.toComparableColumns() }
    public fun <C : Comparable<C>> minOrNull(vararg columns: ColumnReference<C?>): C? = minOrNull { columns.toColumns() }
    public fun <C : Comparable<C>> minOrNull(vararg columns: KProperty<C?>): C? = minOrNull { columns.toColumns() }

    public fun <C : Comparable<C>> minOf(selector: RowSelector<T, C>): C = minOfOrNull(selector)!!

    public fun <C : Comparable<C>> minOfOrNull(selector: RowSelector<T, C>): C? = rows().minOfOrNull { selector(it, it) }

    public fun <C : Comparable<C>> minBy(selector: RowSelector<T, C?>): DataRow<T> = minByOrNull(selector)!!
    public fun minBy(column: String): DataRow<T> = minByOrNull(column)!!
    public fun <C : Comparable<C>> minBy(column: ColumnReference<C?>): DataRow<T> = minByOrNull(column)!!
    public fun <C : Comparable<C>> minBy(column: KProperty<C?>): DataRow<T> = minByOrNull(column)!!

    public fun <C : Comparable<C>> minByOrNull(selector: RowSelector<T, C?>): DataRow<T>? = getOrNull(mapRows(selector).indexOfMin())
    public fun minByOrNull(column: String): DataRow<T>? = minByOrNull(column.toColumnOf<Comparable<Any?>?>())
    public fun <C : Comparable<C>> minByOrNull(column: ColumnReference<C?>): DataRow<T>? = getOrNull(get(column).asSequence().indexOfMin())
    public fun <C : Comparable<C>> minByOrNull(column: KProperty<C?>): DataRow<T>? = minByOrNull(column.toColumnAccessor())

    // endregion

    // region max

    public fun max(): DataRow<T> = maxFor(comparableColumns())

    public fun <R : Comparable<R>> maxFor(columns: AggregateColumnsSelector<T, R?>): DataRow<T> = Aggregators.max.aggregateFor(this, columns)
    public fun maxFor(vararg columns: String): DataRow<T> = maxFor { columns.toComparableColumns() }
    public fun <C : Comparable<C>> maxFor(vararg columns: ColumnReference<C?>): DataRow<T> = maxFor { columns.toColumns() }

    public fun <R : Comparable<R>> max(columns: ColumnsSelector<T, R?>): R = maxOrNull(columns)!!
    public fun max(vararg columns: String): Comparable<Any?> = maxOrNull(*columns)!!
    public fun <C : Comparable<C>> max(vararg columns: ColumnReference<C?>): C = maxOrNull(*columns)!!
    public fun <C : Comparable<C>> max(vararg columns: KProperty<C?>): C = maxOrNull(*columns)!!

    public fun <R : Comparable<R>> maxOrNull(columns: ColumnsSelector<T, R?>): R? = Aggregators.max.aggregateAll(this, columns)
    public fun maxOrNull(vararg columns: String): Comparable<Any?>? = maxOrNull { columns.toComparableColumns() }
    public fun <C : Comparable<C>> maxOrNull(vararg columns: ColumnReference<C?>): C? = maxOrNull { columns.toColumns() }
    public fun <C : Comparable<C>> maxOrNull(vararg columns: KProperty<C?>): C? = maxOrNull { columns.toColumns() }

    public fun <R : Comparable<R>> maxOf(selector: RowSelector<T, R>): R = maxOfOrNull(selector)!!

    public fun <R : Comparable<R>> maxOfOrNull(selector: RowSelector<T, R>): R? = rows().maxOfOrNull { selector(it, it) }

    public fun <C : Comparable<C>> maxBy(selector: RowSelector<T, C?>): DataRow<T> = maxByOrNull(selector)!!
    public fun maxBy(column: String): DataRow<T> = maxByOrNull(column)!!
    public fun <C : Comparable<C>> maxBy(column: ColumnReference<C?>): DataRow<T> = maxByOrNull(column)!!
    public fun <C : Comparable<C>> maxBy(column: KProperty<C?>): DataRow<T> = maxByOrNull(column)!!

    public fun <C : Comparable<C>> maxByOrNull(selector: RowSelector<T, C?>): DataRow<T>? = getOrNull(mapRows(selector).indexOfMax())
    public fun maxByOrNull(column: String): DataRow<T>? = maxByOrNull(column.toColumnOf<Comparable<Any?>?>())
    public fun <C : Comparable<C>> maxByOrNull(column: ColumnReference<C?>): DataRow<T>? = getOrNull(get(column).asSequence().indexOfMax())
    public fun <C : Comparable<C>> maxByOrNull(column: KProperty<C?>): DataRow<T>? = maxByOrNull(column.toColumnAccessor())

    // endregion

    // region sum

    public fun sum(): DataRow<T> = sumFor(numberColumns())

    public fun <R : Number> sumFor(columns: AggregateColumnsSelector<T, R?>): DataRow<T> = Aggregators.sum.aggregateFor(this, columns)
    public fun sumFor(vararg columns: String): DataRow<T> = sumFor { columns.toColumnsOf() }

    public fun sum(vararg columns: String): Number = sum { columns.toColumnsOf() }

    // endregion

    // region std

    public fun <R : Number> stdFor(columns: AggregateColumnsSelector<T, R>): DataRow<T> = Aggregators.std.aggregateFor(this, columns)
    public fun std(): DataRow<T> = stdFor(remainingColumns { it.isNumber() } as ColumnsSelector<T, Number>)

    // endregion

    // region mean

    public fun mean(skipNa: Boolean = false): DataRow<T> = meanFor(skipNa, numberColumns())

    public fun <C : Number> meanFor(skipNa: Boolean = false, columns: AggregateColumnsSelector<T, C?>): DataRow<T> = Aggregators.mean(skipNa).aggregateFor(this, columns)
    public fun meanFor(vararg columns: String): DataRow<T> = meanFor { columns.toNumberColumns() }

    public fun <C : Number> mean(skipNa: Boolean = false, columns: ColumnsSelector<T, C?>): Double = Aggregators.mean(skipNa).aggregateAll(this, columns) as Double? ?: Double.NaN
    public fun mean(vararg columns: String, skipNa: Boolean = false): Double = mean(skipNa) { columns.toNumberColumns() }
    public fun <C : Number> mean(vararg columns: ColumnReference<C?>, skipNa: Boolean = false): Double = mean(skipNa) { columns.toColumns() }
    public fun <C : Number> mean(vararg columns: KProperty<C?>, skipNa: Boolean = false): Double = mean(skipNa) { columns.toColumns() }

    // endregion

    // region median

    public fun median(): DataRow<T> = medianFor(comparableColumns())

    public fun <C : Comparable<C>> medianFor(columns: AggregateColumnsSelector<T, C?>): DataRow<T> = Aggregators.median.aggregateFor(this, columns)
    public fun medianFor(vararg columns: String): DataRow<T> = medianFor { columns.toComparableColumns() }

    public fun <C : Comparable<C>> median(columns: ColumnsSelector<T, C?>): C = medianOrNull(columns)!!
    public fun median(vararg columns: String, skipNa: Boolean = false): Any = median { columns.toComparableColumns() }
    public fun <C : Comparable<C>> median(vararg columns: ColumnReference<C?>): C = median { columns.toColumns() }
    public fun <C : Comparable<C>> median(vararg columns: KProperty<C?>): C = median { columns.toColumns() }

    public fun <C : Comparable<C>> medianOrNull(columns: ColumnsSelector<T, C?>): C? = Aggregators.median.aggregateAll(this, columns)
    public fun medianOrNull(vararg columns: String, skipNa: Boolean = false): Any? = medianOrNull { columns.toComparableColumns() }
    public fun <C : Comparable<C>> medianOrNull(vararg columns: ColumnReference<C?>): C? = medianOrNull { columns.toColumns() }
    public fun <C : Comparable<C>> medianOrNull(vararg columns: KProperty<C?>): C? = medianOrNull { columns.toColumns() }

    // endregion
}

// region inlines

public inline fun <T, reified D : Number> DataFrame<T>.meanOf(
    skipNa: Boolean = false,
    noinline selector: RowSelector<T, D?>
): Double {
    return Aggregators.mean(skipNa).of(this, selector) ?: Double.NaN
}

public inline fun <T, reified R : Number> DataFrameAggregations<T>.sum(noinline columns: ColumnsSelector<T, R?>): R =
    Aggregators.sum.aggregateAll(this, columns) ?: R::class.zero()

public inline fun <T, reified R : Number> DataFrameAggregations<T>.sum(vararg columns: ColumnReference<R?>): R = sum { columns.toColumns() }

public inline fun <T, reified R : Number?> DataFrameAggregations<T>.sumOf(crossinline selector: RowSelector<T, R>): R =
    rows().sumOf(getType<R>()) { selector(it, it) }

public inline fun <T, reified R : Comparable<R>> DataFrameAggregations<T>.medianOf(
    crossinline selector: RowSelector<T, R?>
): R? =
    Aggregators.median.of(this, selector) as R?

// endregion
