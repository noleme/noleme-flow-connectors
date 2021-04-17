package com.noleme.crawl.etl;

import com.noleme.crawl.data.Page;
import com.noleme.flow.actor.transformer.Transformer;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/24
 */
public class PageLinkExtractor implements Transformer<Page, Page>
{
    @Override
    public Page transform(Page page)
    {
        Set<String> links = page.getDocument()
            .getElementsByTag("a")
            .eachAttr("href")
            .stream()
            .filter(l -> l.startsWith("http"))
            .collect(Collectors.toSet())
        ;

        return page.setLinks(links);
    }
}
