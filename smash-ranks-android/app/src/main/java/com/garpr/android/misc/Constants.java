package com.garpr.android.misc;

import com.garpr.android.models.Endpoint;
import com.garpr.android.models.Region;

public final class Constants {

    // Defaults
    public static final Region DEFAULT_REGION = new Region("Norcal", "norcal", Endpoint.GAR_PR);

    // GAR PR Paths
    public static final int GAR_PR_API_PORT = 3001;
    public static final String GAR_PR_BASE_PATH = "https://www.garpr.com";

    // Miscellaneous
    public static final String CHARLES_TWITTER_URL = "https://twitter.com/charlesmadere";
    public static final String GAR_TWITTER_URL = "https://twitter.com/garsh0p";
    public static final String GITHUB_URL = "https://github.com/charlesmadere/smash-ranks-android";

    // NOT GAR PR Paths
    public static final int NOT_GAR_PR_API_PORT = 3001;
    public static final String NOT_GAR_PR_BASE_PATH = "https://www.notgarpr.com";

}
