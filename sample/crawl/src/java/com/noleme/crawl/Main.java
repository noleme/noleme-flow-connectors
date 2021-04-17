package com.noleme.crawl;

import com.noleme.flow.connect.etl.ETLCompilationException;
import com.noleme.flow.connect.etl.ETLRunException;
import com.noleme.flow.io.input.Input;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/22
 */
public class Main
{
    public static void main(String[] args) throws ETLCompilationException, ETLRunException
    {
        String url = args.length == 0
            ? "https://en.wikipedia.org/wiki/Extract,_transform,_load"
            : args[0]
        ;

        var pipe = new SampleCrawl("sample-crawl/", 32).compile();
        pipe.run(Input.of("url", url));
    }
}
