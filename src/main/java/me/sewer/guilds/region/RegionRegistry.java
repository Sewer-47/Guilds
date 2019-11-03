package me.sewer.guilds.region;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.*;
import org.bukkit.util.Vector;

public class RegionRegistry {

    private final UUID worldId;
    private final Set<Region> regions = new HashSet<>();
   // private final Multimap<Vector2, Region> chunks = ArrayListMultimap.create();

    public RegionRegistry(UUID worldId) {
        this.worldId = worldId;
    }

    public Set<Region> getRegions() {
        return this.regions;
    }

    public UUID getWorldId() {
        return this.worldId;
    }

    public Optional<Region> getRegion(String name) {
        return this.regions.stream().filter(abstractRegion -> abstractRegion.getName().equals(name)).findFirst();
    }

    public List<Region> getRegion(Location location) {
        return this.getRegion(location.toVector());
    }

    public List<Region> getRegion(Vector vector) {
        return this.regions.stream().filter(region -> region.contains(vector)).collect(Collectors.toList());
      /*  World world = Bukkit.getWorld(this.worldId);
        Chunk chunk = world.getChunkAt(vector.toLocation(world));
        Vector chunkLocation = new Vector(chunk.getX(), 0, chunk.getZ());
        Collection<Region> regions = this.chunks.get(Vector2.fromBukkit(chunkLocation));
        return regions.stream().filter(region -> region.contains(vector)).collect(Collectors.toList());*/
    }

    public void add(Region region) {
        this.regions.add(region);
      /*   Set<Vector2> chunks = new HashSet<>();
        World world = Bukkit.getWorld(this.worldId);
        RegionBounds bounds = region.getBounds();
        Vector boundMax = bounds.getVectorMax();
        Vector boundMin = bounds.getVectorMin();
        System.out.println(boundMax.toString());
        System.out.println(boundMin.toString());
        Chunk nextChunk = null;
        for (int x = boundMin.getBlockX(); x <= boundMax.getBlockX(); x++) {
            for (int z = boundMin.getBlockZ(); z <= boundMax.getBlockZ(); z++) {

                System.out.println(x + "    " + boundMax.getBlockX());
                Chunk bukkit = world.getChunkAt(x, z);
                Vector2 chunk = new Vector2(bukkit.getX(), bukkit.getZ());
                if (nextChunk == null || !nextChunk.equals(bukkit)) {
                    chunks.set(chunk);
                    nextChunk = bukkit;
                //    System.out.println(new Vector(x, 0, z).toString());
                //    System.out.println(bukkit.getX() + "    x  - y   " + bukkit.getZ());
                }
            }
        }
        chunks.forEach(chunk -> {

            Vector2 chunkMin = chunk;
            chunkMin.setX(chunkMin.getX() - 16);
            chunkMin.setY(chunkMin.getY() - 16);

            Vector2 chunkMax = chunk;
            chunkMax.setX(chunkMax.getX() + 16);
            chunkMax.setY(chunkMax.getY() + 16);

            this.chunks.put(chunkMin, region);
            this.chunks.put(chunkMax, region);
        });*/
    }

    public void remove(Region region) {
        this.regions.remove(region);

    /*    Set<Chunk> chunks = new HashSet<>();
        World world = Bukkit.getWorld(this.worldId);

        RegionBounds bounds = region.getBounds();
        Vector boundMax = bounds.getVectorMax();
        Vector boundMin = bounds.getVectorMin();

        for (int x = boundMax.getBlockX(); x <= boundMin.getBlockX(); x++) {
            for (int z = boundMax.getBlockZ(); z <= boundMin.getBlockZ(); z++) {

                Vector next = new Vector(x, 0, z);
                chunks.set(world.getChunkAt(next.toLocation(world)));
            }
        }
        chunks.forEach(chunk -> {
            Vector chunkLocation = new Vector(chunk.getX(), 0, chunk.getZ());

            Vector chunkMin = chunkLocation;
            chunkMin.setX(chunkMin.getX() - 16);
            chunkMin.setZ(chunkMin.getZ() - 16);

            Vector chunkMax = chunkLocation;
            chunkMax.setX(chunkMax.getX() + 16);
            chunkMax.setZ(chunkMax.getZ() + 16);

            this.chunks.remove(Vector2.fromBukkit(chunkMin), region);
            this.chunks.remove(Vector2.fromBukkit(chunkMax), region);
        );*/
    }
}
