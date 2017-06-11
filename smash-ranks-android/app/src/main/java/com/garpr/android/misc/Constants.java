package com.garpr.android.misc;

import com.garpr.android.models.Endpoint;
import com.garpr.android.models.Region;

public final class Constants {

    // Defaults
    // TODO this should be retrieved from the server via a splash screen...
    public static final Region DEFAULT_REGION = new Region("Norcal", "norcal", Endpoint.GAR_PR,
            45, 2);

    // GAR PR Paths
    public static final int GAR_PR_API_PORT = 3001;
    public static final String GAR_PR_BASE_PATH = "https://www.garpr.com";

    // Miscellaneous
    public static final int ERROR_CODE_BAD_REQUEST = 400;
    public static final int ERROR_CODE_UNKNOWN = Integer.MAX_VALUE;
    public static final String CHARLES_TWITTER_URL = "https://twitter.com/charlesmadere";
    public static final String GAR_TWITTER_URL = "https://twitter.com/garsh0p";
    public static final String GITHUB_URL = "https://github.com/charlesmadere/smash-ranks-android";
    public static final String PLAIN_TEXT = "text/plain";

    // NOT GAR PR Paths
    public static final int NOT_GAR_PR_API_PORT = 3001;
    public static final String NOT_GAR_PR_BASE_PATH = "https://www.notgarpr.com";

}
