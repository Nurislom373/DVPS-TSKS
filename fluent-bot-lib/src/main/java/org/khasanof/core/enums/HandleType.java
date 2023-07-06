package org.khasanof.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Author: Nurislom
 * <br/>
 * Date: 20.06.2023
 * <br/>
 * Time: 18:21
 * <br/>
 * Package: org.khasanof.core.enums
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum HandleType {
    MESSAGE(HandleClasses.HANDLE_MESSAGE),
    CALLBACK(HandleClasses.HANDLE_CALLBACK), STICKER,
    PHOTO(HandleClasses.HANDLE_PHOTO),
    DOCUMENT(HandleClasses.HANDLE_DOCUMENT),
    VIDEO(HandleClasses.HANDLE_VIDEO),
    VOICE, CONTACT, VIDEO_NOTE,
    LOCATION, MEDIA_GROUP, AUDIO,
    ANIMATION, CHAT_ACTION, VENUE,
    DICE, POLL;

    private HandleClasses handleClasses;

    public static boolean hasHandleAnnotation(HandleType type) {
        return type.handleClasses != null;
    }

}
