/*
 * This is the source code of AyuGram for Android.
 *
 * We do not and cannot prevent the use of our code,
 * but be respectful and credit the original author.
 *
 * Copyright @Radolyn, 2023
 */

package com.radolyn.ayugram;

import org.telegram.messenger.BuildVars;

public class AyuConstants {
    public static final long[] OFFICIAL_CHANNELS = {
            // 1905581924, // @ayugramchat
            // 1794457129, // @ayugram1338
            // 1434550607, // @radolyn
    };
    public static final long[] DEVS = {
            // 139303278, // @alexeyzavar
            // 778327202, // @sharapagorg
            // 963494570, // @Zanko_no_tachi
            // 238292700, // @MaxPlays
            // 1795176335, // @radolyn_services
    };

    public static final int DOCUMENT_TYPE_NONE = 0;
    public static final int DOCUMENT_TYPE_PHOTO = 1;
    public static final int DOCUMENT_TYPE_STICKER = 2;
    public static final int DOCUMENT_TYPE_FILE = 3;

    public static final int OPTION_HISTORY = 1338_01;
    public static final int OPTION_TTL = 1338_02;

    public static final int MESSAGE_EDITED_NOTIFICATION = 6968;
    public static final int MESSAGES_DELETED_NOTIFICATION = 6969;

    public static String AYU_DATABASE = "ayu-data";

    public static String APP_NAME = "Panda";
}
