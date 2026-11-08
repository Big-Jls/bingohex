# Changelog

## 26.2-1.32.20 — 2026-11-08

Source release for the ReBongo (Bongo NeoForge 26.2 port) line.

### Changed

- Dependency switched from the old `bingo` mod id to the port's upstream id
  `bongo` (`io.github.noeppi_noeppi.mods.bongo`) throughout the source.
- Added an explicit `libx` dependency (`[26.2-8.0.0,)`) and pinned the Bongo
  dependency to `[26.2-1.0.0-port.1,)`.
- Metadata: unofficial name suffix, own issue tracker/URL, authors list,
  non-affiliation note.

### Content

- 100 hex augments: 40 silver / 30 gold / 30 prismatic.
- Chaos Mind (`inventory_shuffle`) is a right-click skill item with an 8 minute
  cooldown; it shuffles the main inventory, offhand and armor slots of enemy
  players into one pool.
- Bound skill items cannot be dropped, stored in containers, item frames or
  armor stands, and are re-granted after logout/respawn.

### Known issues

- `assets/hexbingo/sounds/dechi_song.ogg` and `guyongzhe.ogg` are third-party
  recordings and are pending replacement with original audio. They are not
  covered by this project's license.
