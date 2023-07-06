package org.khasanof.core.executors.matcher.impls;

import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.main.annotation.methods.HandleVideos;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.util.Arrays;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls
 * @since 06.07.2023 23:42
 */
public class SimpleVideosMatcher extends GenericMatcher<HandleVideos, Video> {

    private final SimpleVideoMatcher matcher = new SimpleVideoMatcher();

    @Override
    public boolean matcher(HandleVideos annotation, Video value) {
        return Arrays.stream(annotation.values())
                .anyMatch(handleVideo -> matcher.matcher(handleVideo, value));
    }

    @Override
    public Class<HandleVideos> getType() {
        return HandleVideos.class;
    }
}
