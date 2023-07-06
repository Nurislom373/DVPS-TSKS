package org.khasanof.core.executors.matcher.impls;

import org.khasanof.core.enums.scopes.VideoScope;
import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.main.annotation.methods.HandleVideo;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls
 * @since 06.07.2023 23:34
 */
public class SimpleVideoMatcher extends GenericMatcher<HandleVideo, Video> {

    private final Map<VideoScope, Function<Video, Object>> biFunctionMap = new HashMap<>();

    {
        setBiFunctionMap();
    }

    @Override
    public boolean matcher(HandleVideo annotation, Video value) {
        Object apply = biFunctionMap.get(annotation.scope()).apply(value);
        return matchFunctions.get(Map.entry(annotation.match(), getScopeType(apply, annotation.match())))
                .apply(annotation.value(), apply);
    }

    @Override
    public Class<HandleVideo> getType() {
        return HandleVideo.class;
    }

    void setBiFunctionMap() {
        biFunctionMap.put(VideoScope.HEIGHT, Video::getHeight);
        biFunctionMap.put(VideoScope.WIDTH, Video::getWidth);
        biFunctionMap.put(VideoScope.DURATION, Video::getDuration);
        biFunctionMap.put(VideoScope.FILE_NAME, Video::getFileName);
        biFunctionMap.put(VideoScope.MIME_TYPE, Video::getMimeType);
        biFunctionMap.put(VideoScope.FILE_SIZE, Video::getFileSize);
    }
}
