# Changelog

## 26.2-1.35.4 — 2026-11-09

### Fixed

- **Pet Master** (`pet_master`): teleporting (including cross-dimension
  teleports) no longer leaves the old pet behind and spawns a second one. The
  existing pet is moved with its owner, and duplicate/stale pets are discarded
  on summon, game reset and effect cleanup.

## 26.2-1.35.3 — 2026-11-09

### Changed

- **Lone Hero** (`lone_hero`, silver) is disabled. The hex is removed from the
  card pool, its effect handlers are commented out, and the third-party audio
  `assets/hexbingo/sounds/guyongzhe.ogg` is removed along with its
  `sounds.json` entry. Existing saves that already own the hex no longer
  receive any effect.
- Hex count is now 118 (46 silver / 36 gold / 36 prismatic).

## 26.2-1.35.2 — 2026-11-09

### Fixed

- **Is It Over?** (`is_it_over`): chorus fruit teleports are no longer blocked
  either; ender pearls and chorus fruit remain normal movement tools.
- **Is It Over?**: `/bingo tp`, `/bingo teleport`, `/tp`, `/teleport` and
  `/spreadplayers` are still blocked while exiled, including when wrapped in
  `/execute ... run`.

## 26.2-1.35.1 — 2026-11-09

### Fixed

- **Is It Over?** (`is_it_over`) no longer blocks ender pearl teleports. Pearls
  are a normal movement tool and keep working while you are exiled.

## 26.2-1.35.0 — 2026-11-09

Requires ReBongo `26.2-1.0.0-port.3` or newer (team backpacks now have 54
slots).

### Added

- **Meteor** (`meteor`, silver): average every team's position and call down
  5-10 amethyst meteors of varying size. Enemy players get Slowness III and a
  10x10 hole under their feet; a map with all impact points is given to the
  holder. Impact sites leave an amethyst crater, loot chests and geared
  skeletons.
- **Quality Upgrade** (`offer_upgrade`, silver): the next hex selection is
  guaranteed to contain at least one prismatic hex. Only appears in the first
  hex selection and is excluded from transform effects and free-pick lists.
- **Blind** (`blind`, silver): right-click to blind every enemy for 8 seconds.
  3 minute cooldown.
- **Is It Over?** (`is_it_over`, silver): exile yourself to the End outer
  islands for 10 minutes. You cannot be teleported by anything during it and
  return automatically.
- **Auto Smelt** (`auto_smelt`, gold): mined raw ores are smelted
  automatically, with extra experience.
- **Task Shield** (`task_shield`, gold): locks your team's 3 highest-weight
  tasks so other hexes cannot revert them.
- **Midas Touch** (`midas_touch`, gold): press G to turn your offhand item
  into the same amount of gold ingots (valuables excluded). 1 minute cooldown.
- **Bat Form** (`bat_form`, gold): right-click to become a bat for 60 seconds
  (flight, cannot attack, bat-sized health and body). 7 minute cooldown.
- **Mind Control** (`mind_control`, prismatic): right-click to steer one
  enemy's movement with your view for 15 seconds. 5 minute cooldown.
- **Objective Deny** (`objective_deny`, prismatic): pick a task; no team can
  complete it for 30 seconds. 15 minute cooldown.
- **X-Ray** (`xray`, prismatic): right-click for 20 seconds of ore x-ray.
  5 minute cooldown.
- **Scavenger** (`scavenger`, prismatic): breaking blocks has a 3% chance to
  drop the block itself.
- **Card Shuffle** (`card_shuffle`, prismatic): all teams' card positions are
  randomly rearranged while completion and lock states follow their tasks.
- **Mirror Hex** (`mirror_hex`, prismatic): right-click to open a list and
  copy one hex you do not own from the nearest other team.

### Changed

- Stack Dragon (`stack_dragon`) now shows its current stack count in the K
  roster, and its loot chest spawns totem particles plus an experience orb
  pickup sound.
- `/bingo bp` team backpacks are now 54 slots instead of 27.

## 26.2-1.34.0 — 2026-11-09

### Added

- **Fish God** (`dolphin_dash`, silver): permanent Dolphin's Grace II while in
  water.
- **Portable Furnace** (`portable_furnace`, silver): a bound furnace item that
  opens a furnace screen anywhere. Its three slots and progress are stored per
  player in the game save, and its smelting speed is doubled by Smelting Speed.
- **Smelting Speed** (`smelt_speed`, silver): nearby furnaces, blast furnaces
  and smokers tick twice per game tick (5x5 loaded chunks around the holder).
  Fuel efficiency is unchanged; only wall-clock speed doubles.

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
