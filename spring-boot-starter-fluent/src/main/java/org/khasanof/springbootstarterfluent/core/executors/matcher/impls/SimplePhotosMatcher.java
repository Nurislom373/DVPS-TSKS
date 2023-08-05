package org.khasanof.springbootstarterfluent.core.executors.matcher.impls;

import org.khasanof.springbootstarterfluent.core.executors.matcher.GenericMatcher;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandlePhotos;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.Arrays;
import java.util.List;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls
 * @since 06.07.2023 22:57
 */
public class SimplePhotosMatcher extends GenericMatcher<HandlePhotos, List<PhotoSize>> {

    private final SimplePhotoMatcher matcher = new SimplePhotoMatcher();

    @Override
    public boolean matcher(HandlePhotos annotation, List<PhotoSize> value) {
        return Arrays.stream(annotation.values())
                .anyMatch(handlePhoto -> matcher.matcher(handlePhoto, value));
    }

    @Override
    public Class<HandlePhotos> getType() {
        return HandlePhotos.class;
    }
}
