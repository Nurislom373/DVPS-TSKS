package org.khasanof.springbootstarterfluent.core.executors.matcher.impls;

import org.khasanof.springbootstarterfluent.core.enums.scopes.AudioScope;
import org.khasanof.springbootstarterfluent.core.executors.matcher.GenericMatcher;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleAudio;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Audio;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls
 * @since 09.07.2023 16:04
 */
@Component
public class SimpleAudioMatcher extends GenericMatcher<HandleAudio, Audio> {

    private final Map<AudioScope, Function<Audio, Object>> biFunctionMap = new HashMap<>();

    {
        setBiFunctionMap();
    }

    @Override
    public boolean matcher(HandleAudio annotation, Audio value) {
        Object apply = biFunctionMap.get(annotation.scope()).apply(value);
        return matchFunctions.get(Map.entry(annotation.match(), getScopeType(apply, annotation.match())))
                .apply(annotation.value(), apply);
    }

    @Override
    public Class<HandleAudio> getType() {
        return HandleAudio.class;
    }

    void setBiFunctionMap() {
        biFunctionMap.put(AudioScope.FILE_NAME, Audio::getFileName);
        biFunctionMap.put(AudioScope.FILE_SIZE, Audio::getFileSize);
        biFunctionMap.put(AudioScope.TITLE, Audio::getTitle);
        biFunctionMap.put(AudioScope.PERFORMER, Audio::getPerformer);
        biFunctionMap.put(AudioScope.DURATION, Audio::getDuration);
        biFunctionMap.put(AudioScope.MIME_TYPE, Audio::getMimeType);
    }
}
