/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import java.util.function.BiPredicate;
import pl.pgnig.serwis.auction.entity.AuctionItem;

/**
 *
 * @author bartosz.szymborski
 */
public enum EquipType {
    K("Komputery"), L("Laptopy"), M("Monitory");

    private final String pathChunk;

    private EquipType(String pathChunk) {
        this.pathChunk = pathChunk;
    }

    public String getPathChunk() {
        return pathChunk;
    }

    private static final BiPredicate<AuctionItem, String> STARTS_WITH = (auctionItem, start) -> auctionItem.getEquipmentType().toLowerCase().startsWith(start);

    public static EquipType translate(AuctionItem ai) {
        if (STARTS_WITH.test(ai, "komputer")) {
            return K;
        } else if (STARTS_WITH.test(ai, "laptop")) {
            return L;
        } else if (STARTS_WITH.test(ai, "monitor")) {
            return M;
        }
        throw new IllegalArgumentException(ai.getEquipmentType());
    }

}
