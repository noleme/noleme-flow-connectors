package com.noleme.flow.connect.biteydf.vault.config;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * @author Pierre Lecerf (pierre@noleme.com)
 * Created on 2021/10/03
 */
public class MappingConfig
{
    public String id;

    @JsonUnwrapped
    public TableProperties properties;
}
