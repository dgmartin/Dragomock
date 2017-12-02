package com.github.dgmartin.objects;

/**
 * This class is used for storing data obtained from existing translation files.
 *
 * @since 1.0
 */
public class ExistingTranslation {
    private String key;
    private String value;
    private String translatable;
    private String isMock;

    public ExistingTranslation(String key, String value, String translatable) {
        this.key = key;
        this.value = value;
        this.translatable = translatable;
    }

    /**
     * @return The key for this translation
     * @since 1.0
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key The string key for this translation
     * @since 1.0
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return The value for this translation
     * @since 1.0
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value The value for this translation
     * @since 1.0
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return True if this translation is translatable
     * @since 1.0
     */
    public String isTranslatable() {
        return translatable;
    }

    /**
     * @param translatable Set whether this value is translatable or not
     * @since 1.0
     */
    public void setTranslatable(String translatable) {
        this.translatable = translatable;
    }

    /**
     * @return True if this is a mock translation.
     * @since 1.0
     */
    public String isMock() {
        return isMock;
    }

    /**
     * @param isMock Set to True if this is a mock value. If this is a final translation this should always be set
     *               to False.
     * @since 1.0
     */
    public void setIsMock(String isMock) {
        this.isMock = isMock;
    }

    @Override
    public String toString() {
        return "ExistingTranslation{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", translatable='" + translatable + '\'' +
                '}';
    }
}
