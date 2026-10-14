package org.schabi.newpipe.extractor.services.youtube.extractors;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;

import org.schabi.newpipe.extractor.ServiceList;
import org.schabi.newpipe.extractor.services.youtube.settings.YoutubeSettings;

public class BraveYoutubeStreamInfoItemHelper {
    private final JsonObject videoInfo;

    public BraveYoutubeStreamInfoItemHelper(final JsonObject videoInfoItem) {
        this.videoInfo = videoInfoItem;
    }

    protected boolean braveIsMembersOnly() {
        final JsonArray badges = videoInfo.getArray("badges");
        for (final Object badge : badges) {
            if (((JsonObject) badge).getObject("metadataBadgeRenderer")
                    .getString("label", "").equals("Members only")) {
                return true;
            }
        }
        return false;
    }

    protected boolean braveDoIgnoreMembersOnly() {
        if (ServiceList.YouTube.getServiceSettings()
                .isSettingEnabled(YoutubeSettings.HIDE_MEMBERS_ONLY_STREAMS)) {
            return braveIsMembersOnly();
        }
        return false;
    }
}
