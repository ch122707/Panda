package tw.nekomimi.nekogram.helpers.remote;

import android.text.TextUtils;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.SerializedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PagePreviewRulesHelper extends BaseRemoteHelper {
    private static final String PAGE_PREVIEW_TAG = "pagepreview";
    private static volatile PagePreviewRulesHelper Instance;
    private final ArrayList<DomainInfo> domains = new ArrayList<>();
    private final HashMap<String, DomainInfo> domainsMap = new HashMap<>();
    private boolean loading = false;

    public static PagePreviewRulesHelper getInstance() {
        PagePreviewRulesHelper localInstance = Instance;
        if (localInstance == null) {
            synchronized (PagePreviewRulesHelper.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new PagePreviewRulesHelper();
                }
                return localInstance;
            }
        }
        return localInstance;
    }

    @Override
    protected void onError(String text, Delegate delegate) {

    }

    @Override
    protected String getTag() {
        return PAGE_PREVIEW_TAG;
    }

    @Override
    protected void onLoadSuccess(ArrayList<JSONObject> responses, Delegate delegate) {
        var tag = getTag();
        var json = responses.size() > 0 ? responses.get(0) : null;
        if (json == null) {
            preferences.edit()
                    .remove(tag + "_update_time")
                    .remove(tag)
                    .apply();
            return;
        }

        try {
            ArrayList<DomainInfo> domainInfo = new ArrayList<>();
            var array = json.getJSONArray("domains");

            for (int i = 0; i < array.length(); i++) {
                var obj = array.getJSONObject(i);
                String domain = obj.getString("domain");
                var rules = obj.getJSONArray("rules");
                ArrayList<DomainRule> domainRules = new ArrayList<>();
                for (int j = 0; j < rules.length(); j++) {
                    var obj1 = rules.getJSONObject(j);
                    var rule = new DomainRule(
                            obj1.getString("regex"),
                            obj1.getString("replace")
                    );
                    domainRules.add(rule);
                }
                var info = new DomainInfo(
                        domain,
                        domainRules
                );
                domainInfo.add(info);
            }

            domains.clear();
            domains.addAll(domainInfo);
            domainsMap.clear();
            for (DomainInfo info : domains) {
                domainsMap.put(info.domain, info);
            }
            savePagePreviewRules();
        } catch (JSONException e) {
            FileLog.e(e);
        }
    }

    public void loadPagePreviewRules() {
        var tag = getTag();
        String list = preferences.getString(tag, "");
        domains.clear();
        domainsMap.clear();
        if (!TextUtils.isEmpty(list)) {
            byte[] bytes = Base64.decode(list, Base64.DEFAULT);
            SerializedData data = new SerializedData(bytes);
            int count = data.readInt32(false);
            for (int a = 0; a < count; a++) {
                DomainInfo info = DomainInfo.deserialize(data);
                domains.add(info);
                domainsMap.put(info.domain, info);
            }
            data.cleanup();
        }
    }

    public void savePagePreviewRules() {
        var tag = getTag();
        SerializedData serializedData = new SerializedData();
        serializedData.writeInt32(domains.size());
        for (DomainInfo info : domains) {
            info.serializeToStream(serializedData);
        }
        preferences.edit()
                .putLong(tag + "_update_time", System.currentTimeMillis())
                .putString(tag, Base64.encodeToString(serializedData.toByteArray(), Base64.NO_WRAP | Base64.NO_PADDING))
                .apply();
        serializedData.cleanup();
    }

    public boolean needUpdate() {
        var tag = getTag();
        long oldTime = preferences.getLong(tag + "_update_time", 0L);
        long nowTime = System.currentTimeMillis();
        int TTL = 15 * 60;
        return oldTime + TTL <= nowTime;
    }

    public void checkPagePreviewRules() {
        if (loading) {
            return;
        }
        loading = true;
        loadPagePreviewRules();
        if (needUpdate()) {
            load();
        }
        loading = false;
    }

    public String doRegex(CharSequence textToCheck) {
        String oldUrl;
        if (textToCheck instanceof String) {
            oldUrl = (String) textToCheck;
        } else {
            oldUrl = textToCheck.toString();
        }
        String host = AndroidUtilities.getHostAuthority(oldUrl.toLowerCase());
        DomainInfo info = domainsMap.get(host);
        if (info == null) {
            return oldUrl;
        }
        for (DomainRule rule : info.rules) {
            Pattern regex = rule.getRegexPattern();
            Matcher matcher = regex.matcher(oldUrl);
            if (matcher.find()) {
                oldUrl = matcher.replaceAll(rule.replace);
            }
        }
        return oldUrl;
    }

    public static class DomainRule {
        public String regex;
        public String replace;

        public DomainRule() {}

        public DomainRule(String regex, String replace) {
            this.regex = regex;
            this.replace = replace;
        }

        public Pattern getRegexPattern() {
            return Pattern.compile(regex);
        }

        public static DomainRule deserialize(AbstractSerializedData stream) {
            DomainRule domainRule = new DomainRule();
            domainRule.regex = stream.readString(false);
            domainRule.replace = stream.readString(false);
            return domainRule;
        }

        public void serializeToStream(AbstractSerializedData serializedData) {
            serializedData.writeString(regex);
            serializedData.writeString(replace);
        }
    }

    public static class DomainInfo {
        public String domain;
        public ArrayList<DomainRule> rules;

        public DomainInfo() {}

        public DomainInfo(String domain, ArrayList<DomainRule> rules) {
            this.domain = domain;
            this.rules = rules;
        }

        public static DomainInfo deserialize(AbstractSerializedData stream) {
            DomainInfo domainInfo = new DomainInfo();
            domainInfo.domain = stream.readString(false);
            int count = stream.readInt32(false);
            ArrayList<DomainRule> rules = new ArrayList<>();
            for (int a = 0; a < count; a++) {
                DomainRule rule = DomainRule.deserialize(stream);
                rules.add(rule);
            }
            domainInfo.rules = rules;
            return domainInfo;
        }

        public void serializeToStream(AbstractSerializedData serializedData) {
            serializedData.writeString(domain);
            serializedData.writeInt32(rules.size());
            for (DomainRule rule : rules) {
                rule.serializeToStream(serializedData);
            }
        }
    }
}
