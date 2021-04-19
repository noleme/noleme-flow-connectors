package com.noleme.flow.connect.tablesaw.loader;

import com.noleme.flow.actor.loader.Loader;
import com.noleme.flow.actor.loader.LoadingException;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.HorizontalBarPlot;
import tech.tablesaw.plotly.api.LinePlot;
import tech.tablesaw.plotly.api.TimeSeriesPlot;
import tech.tablesaw.plotly.api.VerticalBarPlot;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;

import java.io.File;
import java.util.function.Function;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 18/04/2021
 */
public class TablesawPlot implements Loader<Table>
{
    private final Function<Table, Figure> computer;
    private final String outputFile;

    public TablesawPlot(Function<Table, Figure> computer)
    {
        this(computer, null);
    }

    public TablesawPlot(Function<Table, Figure> computer, String outputFile)
    {
        this.computer = computer;
        this.outputFile = outputFile;
    }

    @Override
    public void load(Table table) throws LoadingException
    {
        if (this.outputFile == null)
            Plot.show(this.computer.apply(table));
        else {
            var file = new File(this.outputFile);
            Plot.show(this.computer.apply(table), file);
        }
    }

    public static Function<Table, Figure> line(String title, String xColumn, String yColumn)
    {
        return table -> LinePlot.create(title, table, xColumn, yColumn);
    }

    public static Function<Table, Figure> line(String title, String xColumn, String yColumn, String yGroupColumn)
    {
        return table -> LinePlot.create(title, table, xColumn, yColumn, yGroupColumn);
    }

    public static Function<Table, Figure> timeSeries(String title, String xColumn, String yColumn)
    {
        return table -> TimeSeriesPlot.create(title, table, xColumn, yColumn);
    }

    public static Function<Table, Figure> timeSeries(String title, String xColumn, String yColumn, String yGroupColumn)
    {
        return table -> TimeSeriesPlot.create(title, table, xColumn, yColumn, yGroupColumn);
    }

    public static Function<Table, Figure> horizontalBars(String title, String groupColumn, String valueColumn)
    {
        return table -> HorizontalBarPlot.create(title, table, groupColumn, valueColumn);
    }

    public static Function<Table, Figure> horizontalBars(String title, String groupColumn, Layout.BarMode mode, String... valueColumns)
    {
        return table -> HorizontalBarPlot.create(title, table, groupColumn, mode, valueColumns);
    }

    public static Function<Table, Figure> verticalBars(String title, String groupColumn, String valueColumn)
    {
        return table -> VerticalBarPlot.create(title, table, groupColumn, valueColumn);
    }

    public static Function<Table, Figure> verticalBars(String title, String groupColumn, Layout.BarMode mode, String... valueColumns)
    {
        return table -> HorizontalBarPlot.create(title, table, groupColumn, mode, valueColumns);
    }
}
