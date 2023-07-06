package org.khasanof.core.executors.matcher.impls;

import org.khasanof.core.enums.scopes.PhotoScope;
import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.main.annotation.methods.HandlePhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls
 * @since 06.07.2023 22:37
 */
public class SimplePhotoMatcher extends GenericMatcher<HandlePhoto, List<PhotoSize>> {

    private final Map<PhotoScope, Function<List<PhotoSize>, Object>> biFunctionMap = new HashMap<>();

    {
        setBiFunctionMap();
    }

    @Override
    public boolean matcher(HandlePhoto annotation, List<PhotoSize> value) {
        Object apply = biFunctionMap.get(annotation.scope()).apply(value);
        return matchFunctions.get(Map.entry(annotation.match(), getScopeType(apply, annotation.match())))
                .apply(annotation.value(), apply);
    }

    @Override
    public Class<HandlePhoto> getType() {
        return HandlePhoto.class;
    }

    void setBiFunctionMap() {
        biFunctionMap.put(PhotoScope.HEIGHT, (list) -> list.get(list.size() - 1).getHeight());
        biFunctionMap.put(PhotoScope.WIDTH, (list) -> list.get(list.size() - 1).getWidth());
        biFunctionMap.put(PhotoScope.FILE_SIZE, (list) -> list.get(list.size() - 1).getFileSize());
    }
}
