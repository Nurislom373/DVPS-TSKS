package org.khasanof.core.executors.matcher.impls;

import org.khasanof.core.enums.MultiMatchScope;
import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.core.executors.matcher.MultiGenericMatcher;
import org.khasanof.main.annotation.methods.HandleVideo;
import org.khasanof.main.annotation.methods.HandleVideos;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls
 * @since 06.07.2023 23:42
 */
public class SimpleVideosMatcher extends MultiGenericMatcher<HandleVideos, HandleVideo, Video> {

    public SimpleVideosMatcher() {
        super(new SimpleVideoMatcher());
    }

    @Override
    public boolean matcher(HandleVideos annotation, Video value) {
        return multiMatchScopeFunctionMap.get(annotation.match())
                .apply(Arrays.stream(annotation.values()),
                        (handleVideo -> matcher.matcher(handleVideo, value)));
    }

    @Override
    public Class<HandleVideos> getType() {
        return HandleVideos.class;
    }


}
