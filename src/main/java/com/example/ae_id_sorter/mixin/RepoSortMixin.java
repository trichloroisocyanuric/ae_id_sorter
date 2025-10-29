package com.example.ae_id_sorter.mixin;

import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.stacks.AEKey;
import appeng.client.gui.me.common.Repo;
import com.example.ae_id_sorter.IdSorter;
import com.example.ae_id_sorter.RegistryMekSorter;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Comparator;

@Mixin(Repo.class)
public abstract class RepoSortMixin {
    static {
        if (ModList.get().isLoaded("appmek")) {
            RegistryMekSorter.registry();
        }
    }

    @Unique
    @Nullable
    private Comparator<AEKey> ae_id_sorter$CUSTOM_ASC = null;

    @Unique
    @Nullable
    private Comparator<AEKey> ae_id_sorter$CUSTOM_DESC = null;

    @Inject(method = "getKeyComparator", at = @At("RETURN"), cancellable = true)
    public void onGetKeyComparator(SortOrder sortBy, SortDir sortDir, CallbackInfoReturnable<Comparator<AEKey>> cir) {
        if (sortBy == SortOrder.MOD) {
            if (ae_id_sorter$CUSTOM_ASC == null) {
                ae_id_sorter$CUSTOM_ASC = IdSorter.comparator().thenComparing(cir.getReturnValue());
            }
            if (sortDir == SortDir.ASCENDING) {
                cir.setReturnValue(ae_id_sorter$CUSTOM_ASC);
                return;
            }
            if (ae_id_sorter$CUSTOM_DESC == null) {
                ae_id_sorter$CUSTOM_DESC = ae_id_sorter$CUSTOM_ASC.reversed();
            }
            cir.setReturnValue(ae_id_sorter$CUSTOM_DESC);
        }
    }
}
