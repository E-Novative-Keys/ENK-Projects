package net.enkeys.framework.utils;

import java.nio.charset.Charset;

public enum ECharsets
{
    ISO_8859_1("ISO-8859-1"),
    US_ASCII("US-ASCII"),
    UTF_8("UTF-8"),
    UTF_16("UTF-16"),
    UTF_16BE("UTF-16BE"),
    UTF_16LE("UTF-16LE");
    
    private final Charset charset;
    
    private ECharsets(String charset)
    {
        this.charset = Charset.forName(charset);
    }

    public Charset toCharset()
    {
        return charset == null ? Charset.defaultCharset() : charset;
    }
}
