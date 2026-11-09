# Changelog

## 26.2-1.33.0 — 2026-11-09

Requires ReBongo `26.2-1.0.0-port.2` or newer (card tasks now carry their
drawn weight).

### Added

- **Ender Slayer** (`ender_slayer`, gold): killing an Enderman has a 1% chance
  to drop an enchanted book. Internal split: Silk Touch I 0.2%, Mending I 0.4%,
  Infinity I 0.4%.
- **Ultimate Refresh** (`ultimate_refresh`, gold): grants one extra refresh for
  your next hex selection and 2 vanilla loot chests. The refresh button only
  appears for players holding this hex, can be used once, and may be saved for
  a later selection until the game ends.

### Changed

- **Demon Pact** (`demon_pact`, gold): max-health loss reduced from 15 to 10.
  Now also grants a full set of randomly enchanted iron armor and completes the
  highest-weight unfinished Bingo task instead of a random one.
- **Rush Rush Rush** (`dash_charge`, gold): now a continuous 8-second dash that
  follows your view, keeps the cast height, breaks and drops blocks in the way,
  deals 10 damage to creatures hit, and stops at obsidian or bedrock. Cooldown
  is 15 seconds after the dash ends.

### Fixed

- The `mayhem` game setting is now `bongo:mayhem` (was accidentally shipped as
  `bingo:mayhem`), matching the `/bingo create` setting id check.

## 26.2-1.32.21 — 2026-11-09

### Fixed

- `/bingo hexes` now requires gamemaster (OP) permission, matching its
  "free pick any hex" behaviour. Previously any player could open it.
- README command table corrected: the commands live under `/bingo`
  (`/bingo hex`, `/bingo hexes`, `/bingo hexall`, `/bingo rtp`, `/bingo hook`,
  `/bingo boomerang`, `/bingo trap`, `/bingo spy`), not under a `/hex` root.
  Documented that `K` opens the read-only roster while `/bingo hexall` gives
  every online player a free pick.

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
