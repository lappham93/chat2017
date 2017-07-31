
package com.mit.app.enums;

/**
 * Created by Hung Le on 3/20/17.
 */
public enum AppConstKey {
    PROVIDER_APPLICATION_STEP1("provider.application.step1"),
    PROVIDER_APPLICATION_STEP2("provider.application.step2"),
    PROVIDER_APPLICATION_STEP3("provider.application.step3"),
    PROVIDER_APPLICATION_STEP4("provider.application.step4"),
    PROVIDER_APPLICATION_STEP5("provider.application.step5"),
    PROVIDER_APPLICATION_STEP6("provider.application.step6"),
    PROVIDER_APPLICATION_STEP7("provider.application.step7"),
    PROVIDER_APPLICATION_STEP5_DISQUALIFIED("provider.application.step5Disqualified"),
    PROVIDER_APPLICATION_STEP5_CALL("provider.application.step5Call"),
    PROVIDER_APPLICATION_STEP7_REJECT("provider.application.step5Rejected"),
    PROVIDER_APPLICATION_STEP7_APPROVED("provider.application.step7Approved"),
    PROVIDER_APPLICATION_STYLE("provider.application.style"),
    APP_URL_IOS("app.url.ios"),
    APP_URL_ANDROID("app.url.android"),
    DEMAND_MAX_DISTANCE("demand.maxDistance"),
    DEMAND_DECISION_TIME("demand.decisionTime"),
    PROMOTION_INVITE_BONUS("promotion.invite.bonus"),
    PROMOTION_INVITE_USE_TIME("promotion.invite.useTime"),
    PROMOTION_INVITE_TUTORIAL_URL("promotion.tutorialUrl"),
    PROMOTION_INVITE_MESSAGE_BONUS("promotion.message.bonus"),
    PROMOTION_INVITE_MESSAGE_INVITE("promotion.message.invite"),
    PROMOTION_INVITE_MESSAGE_INVITE_WEB("promotion.message.invite.web"),
    PROMOTION_REWARD_TUTORIAL_URL("promotion.reward.tutorialUrl"),
    PROMOTION_DETAIL_TEMPLATE("promotion.detail.template"),
    CITY_NEAR_MAX_DISTANCE("city.near.maxDistance"),
    SERVICE_PRICE_DYNAMIC_RATE("service.price.dynamic.rate"),
    SERVICE_PRICE_DYNAMIC_BIAS("service.price.dynamic.bias"),
    SERVICE_PRICE_DYNAMIC_SCALE("service.price.dynamic.scale"),
	SERVICE_PROVIDER_PERCENT_RECEIVE("service.provider.percent.receive"),
    SERVICE_PROVIDER_HOMEBER_PERCENT_RECEIVE("service.provider.homeber.percent.receive"),
	SERVICE_PRICE_MIN("service.price.min"),
    REWARD_LAST_TIME("reward.lastTime"),
    BACKGROUND_CHECK_FEE("background.check.fee"),
    REGION_AVAILABLE("region.available"),
    SUPPORT_USER_ID("support.userId");

    private String key;

    AppConstKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static AppConstKey findByKey(String key) {
        for (AppConstKey value: values()) {
            if (value.getKey().equals(key)) {
                return value;
            }
        }

        return null;
    }
}
