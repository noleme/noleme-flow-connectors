package com.noleme.crawl.data;

import org.jsoup.nodes.Document;

import java.util.Set;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/24
 */
public class Page
{
    private final String uri;
    private Document document;
    private Set<String> links;

    public Page(String uri)
    {
        this.uri = uri;
    }

    public String getUri()
    {
        return this.uri;
    }

    public Document getDocument()
    {
        return this.document;
    }

    public Page setDocument(Document document)
    {
        this.document = document;
        return this;
    }

    public Set<String> getLinks()
    {
        return this.links;
    }

    public Page setLinks(Set<String> links)
    {
        this.links = links;
        return this;
    }
}
