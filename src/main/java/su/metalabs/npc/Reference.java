package su.metalabs.npc;

public class Reference {
    public static final String MOD_ID = "metanpc";
    public static final String NAME = "@MOD_NAME@";
    public static final String VERSION = "@VERSION@";

    public static final String DEPENDENCIES = "required-after:metalib;required-after:customnpcs";

    public static final String MOD_GROUP = "@MOD_GROUP@";
    public static final String RESOURCE_PREFIX = MOD_ID.toLowerCase() + ":";

    public static final String CLIENT_PROXY_LOCATION = MOD_GROUP + ".proxy.ClientProxy";
    public static final String COMMON_PROXY_LOCATION = MOD_GROUP + ".proxy.CommonProxy";
}
