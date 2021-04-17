package com.noleme.flow.connect.text.transformer;

import com.noleme.flow.actor.transformer.Transformer;

import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/17
 */
public class RegexReplacer implements Transformer<String, String>
{
    private final Pattern pattern;
    private final Function<MatchResult, String> replacement;

    public RegexReplacer(Pattern pattern, Function<MatchResult, String> replacement)
    {
        this.pattern = pattern;
        this.replacement = replacement;
    }

    public RegexReplacer(Pattern pattern, String replacement)
    {
        this(pattern, mr -> replacement);
    }

    public RegexReplacer(String regex, Function<MatchResult, String> replacement)
    {
        this(Pattern.compile(regex), replacement);
    }

    public RegexReplacer(String regex, String replacement)
    {
        this(Pattern.compile(regex), mr -> replacement);
    }

    @Override
    public String transform(String input)
    {
        return this.pattern.matcher(input).replaceAll(this.replacement);
    }
}
