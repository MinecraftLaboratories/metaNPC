package su.metalabs.npc;

import io.github.crucible.grimoire.common.api.grimmix.Grimmix;
import io.github.crucible.grimoire.common.api.grimmix.GrimmixController;
import io.github.crucible.grimoire.common.api.grimmix.lifecycle.IConfigBuildingEvent;
import io.github.crucible.grimoire.common.api.mixin.ConfigurationType;

@Grimmix(id = Reference.MOD_ID)
public class MetaNpcGrimmix extends GrimmixController {
    @Override
    public void buildMixinConfigs(IConfigBuildingEvent event) {
        event.createBuilder(Reference.MOD_ID + "/mixins." + Reference.MOD_ID + ".json")
                .mixinPackage(Reference.MOD_GROUP + ".mixins")
                .commonMixins("common.*")
                .clientMixins("client.*")
                .refmap("@MIXIN_REFMAP@")
                .verbose(true)
                .required(true)
                .configurationType(ConfigurationType.MOD)
                .build();
    }
}
