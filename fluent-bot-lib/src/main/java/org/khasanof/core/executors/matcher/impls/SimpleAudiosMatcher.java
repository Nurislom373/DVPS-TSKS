package org.khasanof.core.executors.matcher.impls;

import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.core.executors.matcher.MultiGenericMatcher;
import org.khasanof.main.annotation.methods.HandleAudio;
import org.khasanof.main.annotation.methods.HandleAudios;
import org.telegram.telegrambots.meta.api.objects.Audio;

import java.util.Arrays;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls
 * @since 09.07.2023 16:08
 */
public class SimpleAudiosMatcher extends MultiGenericMatcher<HandleAudios, HandleAudio, Audio> {

    public SimpleAudiosMatcher() {
        super(new SimpleAudioMatcher());
    }

    @Override
    public boolean matcher(HandleAudios annotation, Audio value) {
        return multiMatchScopeFunctionMap.get(annotation.match())
                .apply(Arrays.stream(annotation.values()),
                        (handleAudio -> matcher.matcher(handleAudio, value)));
    }

    @Override
    public Class<HandleAudios> getType() {
        return HandleAudios.class;
    }
}
