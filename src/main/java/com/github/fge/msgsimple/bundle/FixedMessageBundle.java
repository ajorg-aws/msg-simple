package com.github.fge.msgsimple.bundle;

import com.github.fge.msgsimple.source.MessageSource;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class FixedMessageBundle
    extends MessageBundle
{
    private final Map<Locale, List<MessageSource>> sources
        = new HashMap<Locale, List<MessageSource>>();

    private FixedMessageBundle(final Builder builder)
    {
        List<MessageSource> list;

        for (final Map.Entry<Locale, List<MessageSource>> entry:
            builder.sources.entrySet()) {
            list = new ArrayList<MessageSource>(entry.getValue());
            sources.put(entry.getKey(), Collections.unmodifiableList(list));
        }
    }

    @Override
    protected List<MessageSource> getSources(final Locale locale)
    {
        final List<MessageSource> ret = sources.get(locale);
        // We can return ret directly: on build, it was wrapped with
        // Collections.unmodifiableList()
        return ret == null ? Collections.<MessageSource>emptyList() : ret;
    }

    @Override
    public MessageBundle.Builder modify()
    {
        return new Builder(this);
    }

    @NotThreadSafe
    static final class Builder
        extends MessageBundle.Builder
    {
        private final Map<Locale, List<MessageSource>> sources
            = new HashMap<Locale, List<MessageSource>>();

        Builder()
        {
        }

        private Builder(final FixedMessageBundle bundle)
        {
            for (final Map.Entry<Locale, List<MessageSource>> entry:
                bundle.sources.entrySet())
                sources.put(entry.getKey(),
                    new ArrayList<MessageSource>(entry.getValue()));
        }

        @Override
        protected void doAppendSource(final Locale locale,
            final MessageSource source)
        {
            getSourceList(locale).add(source);
        }

        @Override
        protected void doPrependSource(final Locale locale,
            final MessageSource source)
        {
            getSourceList(locale).add(0, source);
        }

        @Override
        public MessageBundle build()
        {
            return new FixedMessageBundle(this);
        }

        private List<MessageSource> getSourceList(final Locale locale)
        {
            List<MessageSource> ret = sources.get(locale);

            if (ret == null) {
                ret = new ArrayList<MessageSource>();
                sources.put(locale, ret);
            }

            return ret;
        }
    }
}
