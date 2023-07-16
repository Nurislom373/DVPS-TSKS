package org.khasanof.core.executors.matcher.impls;

import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.core.executors.matcher.MultiGenericMatcher;
import org.khasanof.main.annotation.methods.HandleVideoNote;
import org.khasanof.main.annotation.methods.HandleVideoNotes;
import org.telegram.telegrambots.meta.api.objects.VideoNote;

import java.util.Arrays;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls
 * @since 09.07.2023 16:54
 */
public class SimpleVideoNotesMatcher extends MultiGenericMatcher<HandleVideoNotes, HandleVideoNote, VideoNote> {

    public SimpleVideoNotesMatcher() {
        super(new SimpleVideoNoteMatcher());
    }

    @Override
    public boolean matcher(HandleVideoNotes annotation, VideoNote value) {
        return multiMatchScopeFunctionMap.get(annotation.match())
                .apply(Arrays.stream(annotation.values()),
                        (handleVideo -> matcher.matcher(handleVideo, value)));
    }

    @Override
    public Class<HandleVideoNotes> getType() {
        return HandleVideoNotes.class;
    }
}
