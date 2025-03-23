package org.schabi.newpipe.extractor.services.youtube.extractors;

import com.grack.nanojson.JsonObject;

import org.schabi.newpipe.extractor.ServiceList;
import org.schabi.newpipe.extractor.services.youtube.settings.YoutubeSettings;

import static org.schabi.newpipe.extractor.utils.Utils.isNullOrEmpty;

public class BraveYoutubeShortsLockupHelper {
    private final JsonObject shortsLockupViewModel;

    public BraveYoutubeShortsLockupHelper(final JsonObject shortsLockupViewModel) {
        this.shortsLockupViewModel = shortsLockupViewModel;
    }

    // this code json property is used for both views or if it is members only
    protected String braveExtractViewsOrMembersOnlyText() {
        return shortsLockupViewModel.getObject("overlayMetadata")
                .getObject("secondaryText")
                .getString("content");
    }

    protected boolean braveIsMembersOnlyText(final String text) {
        return text.contains("Members only");
    }

    protected boolean braveDoIgnoreMembersOnly() {
        if (ServiceList.YouTube.getServiceSettings()
                .isSettingEnabled(YoutubeSettings.HIDE_MEMBERS_ONLY_STREAMS)) {
            return braveIsMembersOnly();
        }
        return false;
    }

    private boolean braveIsMembersOnly() {
        final String membersOnlyText = braveExtractViewsOrMembersOnlyText();
        if (!isNullOrEmpty(membersOnlyText)) {
            return braveIsMembersOnlyText(membersOnlyText);
        }
        return false;
    }
}
