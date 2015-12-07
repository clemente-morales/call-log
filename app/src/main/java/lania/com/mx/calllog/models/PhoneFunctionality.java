package lania.com.mx.calllog.models;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by clerks on 12/6/15.
 */
public class PhoneFunctionality {
    private final String name;

    public PhoneFunctionality(String description) {
        this.name = description;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
