package org.khasanof.springbootstarterfluent.core.executors.matcher.impls;

import org.khasanof.springbootstarterfluent.core.executors.matcher.MultiGenericMatcher;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleVideo;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleVideos;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.util.Arrays;

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
