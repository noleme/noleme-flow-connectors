package com.noleme.flow.connect.tablesaw.dataframe.processor.string;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessorException;
import com.noleme.commons.file.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import tech.tablesaw.api.Table;

import java.nio.charset.Charset;

import static com.noleme.commons.function.RethrowFunction.rethrower;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/06/22
 */
public class HexToTextProcessor implements TableProcessor
{
    private final String columnName;
    private final Charset charset;

    /**
     *
     * @param columnName
     */
    public HexToTextProcessor(String columnName)
    {
        this(columnName, Charsets.UTF_8.getCharset());
    }

    /**
     *
     * @param columnName
     * @param charset
     */
    public HexToTextProcessor(String columnName, Charset charset)
    {
        this.columnName = columnName;
        this.charset = charset;
    }

    @Override
    public Table process(Table table) throws TableProcessorException
    {
        try {
            var column = table.textColumn(this.columnName)
                .map(rethrower(text -> new String(Hex.decodeHex(text), this.charset)))
            ;
            return table.replaceColumn(column);
        }
        catch (DecoderException e) {
            throw new TableProcessorException(e.getMessage(), e);
        }
    }
}
