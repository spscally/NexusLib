package tsp.mcnexus.player.info;

import java.util.UUID;

public record PlayerInfo(UUID uuid, SkinInfo skinInfo, NameHistory nameHistory) {}