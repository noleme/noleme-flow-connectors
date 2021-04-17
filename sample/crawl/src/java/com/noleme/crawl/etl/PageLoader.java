package com.noleme.crawl.etl;

import com.noleme.crawl.data.Page;
import com.noleme.flow.actor.loader.Loader;
import com.noleme.flow.actor.loader.LoadingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/24
 */
public class PageLoader implements Loader<Page>
{
    private final String outputDir;

    public PageLoader(String outputDir)
    {
        this.outputDir = outputDir;
    }

    @Override
    public void load(Page page) throws LoadingException
    {
        try {
            var title = page.getDocument().title().isBlank()
                ? UUID.randomUUID().toString()
                : page.getDocument().title().replaceAll("/", "-")
            ;

            Files.writeString(
                Path.of(this.outputDir+title),
                page.getDocument().wholeText()
            );
        }
        catch (IOException e) {
            throw new LoadingException(e.getMessage(), e);
        }
    }
}
