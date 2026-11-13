# Changelog

## 26.2-1.49.9 — 2026-11-14

### Fixed

- **Dragon Tamer** (`dragon_rider`): the baby dragon no longer breaks
  interactive workstations/containers (crafting table, furnaces, anvil,
  smithing table, blast furnace, chests and any other block with a block
  entity) nor obsidian, bedrock, end stone, iron bars or nether/end portals.
  It now only destroys ordinary terrain blocks (uses `DRAGON_IMMUNE` and
  `PORTALS` block tags plus an explicit workstation list).

## 26.2-1.49.8 — 2026-11-14

### Fixed

- **World Shrink** (`world_shrink`): the destination chunk is now loaded before
  reading the surface height, so players are no longer teleported onto the
  bedrock layer; a top-down fallback scan is used if the heightmap still
  returns the world minimum.
- **World Shrink**: player-vs-player damage is now allowed while the effect is
  active (same `LivingIncomingDamageEvent` override used by Trap Room),
  instead of being cancelled by Bingo's invulnerability.
- **World Shrink**: the item is now genuinely single-use — it is consumed on a
  successful cast and the player is recorded as having used it, so relogging
  does not re-grant it and it cannot be cast again.
- **World Shrink**: the pre-cast position of every teleported player is
  recorded and restored when the effect ends (or when the game ends).

### Changed

- **Ascetic** (`ascetic`): third-hex injection chance lowered from 35% to 15%.

## 26.2-1.49.7 — 2026-11-14

### Fixed

- **Team Backpack Expansion** (`team_backpack`): changes made in the 72-slot
  backpack were never marked dirty on the Bingo saved data, so the contents
  could be lost on logout or server restart. The expansion itself and every
  later content change now call `Bingo.setDirty()`, matching the vanilla
  `/bp` container behaviour.

## 26.2-1.49.6 — 2026-11-14

### Fixed

- **Team Backpack Expansion** (`team_backpack`): no longer wipes the team
  backpack — resizing the handler rebuilt its internal list and dropped every
  stored item; the contents are now backed up and restored.
- **Team Backpack Expansion**: `/bp` (`/bingo backpack`) now opens a dedicated
  72-slot menu (8 backpack rows + player inventory) instead of the fixed
  54-slot vanilla chest, so the extra 18 slots are actually visible and usable.

## 26.2-1.49.5 — 2026-11-14

### Changed

- **All Mine** (`all_mine`): locked tasks now show a small lock icon in the
  bottom-right corner of their cell on the Bingo card (visible to everyone),
  synced via a new client payload on cast, join and respawn.

## 26.2-1.49.4 — 2026-11-14

### Fixed

- **Ascetic** (`ascetic`): no longer injected into the third hex offer in
  all-prismatic games — the all-prismatic branch previously reused the normal
  35% third-slot injection, so a silver hex could show up in an all-prismatic
  offer. Normal games still roll Ascetic in the third selection as before.

## 26.2-1.49.3 — 2026-11-14

### Changed

- **Silver Ascension** (`silver_shift`): moved from silver to gold tier.
- **It's Over!** (`ended`): moved from prismatic to gold tier.

## 26.2-1.49.2 — 2026-11-14

### Fixed

- **Fisherman** (`fisherman`): removed the extra `damageRodBy(1)` call in the
  fishing event handler — vanilla already applies the event's initial rod
  damage, so the extra call made the rod lose durability twice per catch.

## 26.2-1.49.1 — 2026-11-14

### Changed

- **It's Over!** (`ended`): the End-side drawback is now "lose invulnerability"
  instead of periodic damage — the owning team takes normal damage from
  endermen, the dragon, fall damage and any other source while in the End
  (like the duel arena of "Trapped With Me"). The Overworld and Nether keep
  normal invulnerability, and the per-3-second damage tick is removed.

## 26.2-1.49.0 — 2026-11-14

### Added

- **Fisherman** (`fisherman`, gold): gain a fishing rod enchanted with Luck of
  the Sea X. Fishing reels in anything except fish — drops are replaced with
  1-2 random items from the Pet Master item pool (fish excluded).
- **Silver Ascension** (`silver_shift`, gold since 1.49.3): immediately gain 2
  random silver hexes.
- **It's Over!** (`ended`, gold since 1.49.3): killing the Ender Dragon
  immediately ends the current game with the killer's team winning; the owning
  team loses invulnerability in the End and takes normal damage there
  (Overworld and Nether are unaffected).

## 26.2-1.48.0 — 2026-11-14

### Added

- **Hidden Compartment** (`hidden_pocket`, gold): opening a chest that has a
  loot table has a 3% chance to spawn an extra random vanilla loot chest near
  you.
- **World Shrink** (`world_shrink`, gold): single-use skill item. On use the
  whole server gets a title warning for 20 seconds (PVP is enabled), then the
  world border shrinks to a 500-block diameter and all overworld players are
  teleported to random spots inside the circle, restoring the original border
  and PVP state after 5 minutes. Never offered as the fourth hex or with less
  than 15 minutes of game time remaining (unlimited-time games are exempt).
- **All Mine** (`all_mine`, prismatic): single-use skill item. Right-click to
  open the task picker and choose a task; every other team can never complete
  that cell (your own team is unaffected). Only offered as the third hex with a
  30% chance in non-lockout modes, and never obtained through transformations.

### Changed

- **Cutting Edge Inventor** (`cutting_edge`): skill cooldown reduction
  increased from 30% to 50%.
- **X-Ray** (`xray`): ore glow outlines are now colored per ore type —
  redstone red, lapis blue, diamond aqua, emerald green, gold yellow, copper
  orange, coal dark gray, iron white, ancient debris dark red.

## 26.2-1.47.2 — 2026-11-14

### Changed

- **Sword Flight** (`sword_fly`): durability consumption order is now
  main hand → offhand → own inventory → team backpack, always preferring the
  lowest-tier sword first. Durability is only consumed while airborne.

## 26.2-1.47.1 — 2026-11-14

### Changed

- **Mind Control** (`mind_control`): the caster now stands still (rooted in
  place, immune to knockback and water currents) and steers the target's
  movement with WASD, instead of the target automatically walking toward the
  caster's look direction. Both players are notified when the control ends or
  either side logs out / changes dimension.

## 26.2-1.47.0 — 2026-11-13

### Added

- **Sword Flight** (`sword_fly`, gold): press R to gain creative-style flight by
  consuming sword durability from your inventory and team backpack — a wooden
  sword lasts 10 seconds, gold 15s, stone 20s, iron 30s, diamond 45s, netherite
  60s. The lowest-tier sword is consumed first (main hand, then offhand, then
  the team backpack); when a sword breaks the next one is picked up
  automatically, and with no swords left flight stops and grants 5 seconds of
  Slow Falling. Flight speed ramps up with the sword tier, never faster than
  elytra, and the sword being consumed is shown by a display entity at your
  feet.

## 26.2-1.46.0 — 2026-11-13

### Added

- **Team Backpack Expansion** (`team_backpack`, silver): expands the team
  `/bp` backpack from 54 to 72 slots (existing items are kept).
- **Near Line** (`near_line`, gold): when your team is 1 cell away from a
  bingo line, the whole team gains Haste II and Speed I.
- **Card Recast** (`card_recast`, prismatic): immediately rerolls the contents
  of your team's completed tasks (replaced with new tasks from the pool),
  keeping them completed.
- **Seal** (`seal`, prismatic): right-click to open the Bingo card and pick a
  task; no player can complete it and a barrier icon is drawn over its cell.
  Cooldown 15 minutes.

## 26.2-1.45.0 — 2026-11-13

### Added

- **Omen Compatible** (`bad_omen`, silver): gain one level of Bad Omen every
  30 seconds, up to level III.
- **Backpack Thief** (`backpack_thief`, gold): right-click to randomly steal
  3 items from the nearest enemy's inventory (bound skill items and glass
  cannon lock markers excluded). Cooldown 3 minutes.
- **Pan Is Right** (`pan_is_right`, prismatic): right-click to reroll every
  hex of your whole team. One-time item.
- **Loot Hunter** (`loot_hunter`, prismatic): kills have a 3% chance to spawn
  a chest with a random vanilla loot table at the mob's death spot.

### Fixed

- **Ender Slayer** (`ender_slayer`): drop chance raised from 1% to 3% (the
  code and description both said 1%; 3% was the intended value).

## 26.2-1.44.0 — 2026-11-13

### Added

- **DJ** (`dj`, silver): gain 1 note block and 3 random music discs.
- **Blackstone Walker** (`blackstone_walker`, gold): walk on lava like Frost
  Walker — the lava under your feet turns into temporary blackstone that melts
  back to lava after a few seconds.
- **Shrink** (`shrink`, gold): right-click to shrink the nearest enemy to 25%
  size for 60 seconds; jump height and block/entity interaction range are
  reduced to 25% as well. Cooldown 8 minutes.

## 26.2-1.43.18 — 2026-11-13

### Fixed / Adjusted

- **X-Ray** (`xray`): the glowing invisible shulker markers were scaled down
  to 0.5 so their hitboxes sit inside the ore block; players can now mine the
  marked ores normally. X-ray duration increased from 20 seconds to 1 minute.
- **Rush Rush Rush** (`dash_charge`): dash duration reduced from 8 seconds to
  5 seconds, and the cooldown increased from 15 seconds to 30 seconds.

## 26.2-1.43.17 — 2026-11-13

### Fixed

- **Dice Roll / Destiny Dice**: the roll animation sampled degenerate UVs, which
  made the die texture tile into hundreds of tiny copies across a screen area.
  The full 32×32 die texture now renders as a single 96×96 die during the
  animation.
- **Structure / biome teleport hexes**: removed every "generate if not found"
  mention from descriptions and messages (`boomerang_biome`, `gaze_abyss`,
  `pillager_outpost`, `cherry_grove`, `pale_garden_map`, `mangrove_map`,
  `eighty_one_trials`). The fallback generation still happens silently, but
  players now only see the normal "teleported to the nearest …" / "received a
  map" message.

## 26.2-1.43.16 — 2026-11-13

### Fixed

- **Chaos Mind** (`inventory_shuffle`): shuffling an enemy's inventory also moved
  their bound skill items and glass-cannon lock markers into random armor/offhand
  slots. Like Spy and Common Wealth, bound items and lock markers now stay in
  their original slots.
- **Pet Master** (`pet_master`): when the pet was temporarily removed (chunk
  unload or a cross-dimension teleport), it was mistaken for dead and the item
  supply stopped permanently. Only an actual kill (e.g. `/kill`) dismisses the
  pet now; unloaded pets keep their reference and resume following once their
  chunk loads again.

## 26.2-1.43.15 — 2026-11-13

### Fixed

- **Dragon Rider** (`dragon_rider`): when the incubating dragon egg falls
  (gravity) or teleports away, it was treated as a plain vanilla egg, the
  countdown vanished and it could never hatch. Incubation now keeps the
  remaining time while the egg falls, relocates it below or around the old
  spot, and only cancels after a 10 second grace period.
- **Mind Control** (`mind_control`): the target no longer has to be within
  30 blocks. The nearest enemy on the whole server is picked (same-dimension
  players first, then any player in other dimensions).

## 26.2-1.43.14 — 2026-11-13

### Fixed

- **Blank Cell** (`blank_cell`): the description no longer calls it an
  "extra team-exclusive cell"; it is simply an item that opens the current
  Bingo card and lights up any incomplete task.

## 26.2-1.43.13 — 2026-11-13

### Fixed

- **Spy** (`spy`): the description no longer mentions the removed "彩虹大作战"
  bound item; the stale entry was also dropped from the bound-item name set
  and the dead rainbow-duel lang keys were removed.

## 26.2-1.43.12 — 2026-11-13

### Changed

- **Naming / tiers**: the prismatic `inventor` hex is renamed from a duplicate
  "尖端发明家" to **锻造大师** and demoted to gold; the old silver
  `forge_master` is renamed to **装备升级** to avoid the name clash. Hex counts
  are now 52 silver / 45 gold / 35 prismatic (still 132 total).
- **Immunity Shield** (`immunity_shield`): promoted from silver to gold and its
  block cooldown raised from 60 seconds to 3 minutes.
- **Meteor** (`meteor`): impact points are now centered on the caster (never
  more than 500 blocks away) instead of the average position of all players.
- **OK Boomerang** (`boomerang_biome`): flight speed reduced (1.5 -> 1.0
  blocks/tick).
- **X-Ray** (`xray`): ore highlights are now closed, invisible, glowing
  shulkers at the ore positions instead of floating item drops.

## 26.2-1.43.11 — 2026-11-13

### Added

- **Magnet** (`magnet`): press **T** to toggle the item-attraction on and off
  (overlay message on toggle; state is per player and cleared on game reset).
- **Dice animation**: the 3-Sided Die (`dice_roll`) and Destiny Dice
  (`destiny_dice`) now play a rolling triangle-die animation on screen before
  the result is revealed; the outcome is only applied by the server after the
  animation finishes. The d4 sprites are original art (no third-party assets).

### Changed

- **Want items** (`want_items` / `want_more_items` / `want_lots_items`): the
  random items now come from the same weighted pool as the Pet Master pet
  (valuable items are rarer) instead of a uniform full-item pool.

## 26.2-1.43.10 — 2026-11-12

### Fixed

- **Offer queue** (`presentNext`): when a reward tier cannot roll 3 hexes
  (player owns almost everything), the tier is now put back into the queue
  instead of being silently consumed, so the reward is no longer lost.
- **HexColor**: unknown color ids now log a warning instead of silently
  falling back to silver, making `hexes.json` typos visible in the log.
- **Star Voyage** (`star_voyage`): the platform is now placed 256 blocks above
  the player's current height (clamped to the world build limit) instead of
  the absolute y=256, so it no longer buries the platform inside tall
  mountains. Completion/failure now compares against the pre-teleport height.
- **Spider Man** (`spider_man`): wall detection now uses the player's own
  collision (`horizontalCollision`) like vanilla spiders, so any wall the body
  touches can be climbed (fences, panes, stairs and non-full blocks included)
  instead of only full-collision blocks 1-2 above the feet.

## 26.2-1.43.9 — 2026-11-12

### Fixed

- **`/bingo tp` interception**: the target player of ReBongo's `/bingo tp` is an
  `EntitySelector`, not an `EntityArgument`; the exiled-target check now reads
  the argument with the correct type, so teleporting to an exiled player via
  selector is blocked reliably.
- **Star Voyage** (`star_voyage`): while the player is in the Nether the offer
  no longer rolls Star Voyage (its fixed y=256 landing point is meaningless
  below the Nether ceiling).
- **Demon Pact** (`demon_pact`): the max-health reduction now always leaves at
  least 2 health points, so stacking it with Bat Form or other max-health
  reductions can no longer drive max health negative and instantly kill the
  player.
- **Glass Cannon** (`glass_cannon_bag`): locked-slot markers are no longer
  moved by Spy (`spy`) inventory swaps or Common Wealth (`common_wealth`)
  redistribution; the barriers stay in their owner's locked slots.
- **Still Water** (`still_water`): locked players no longer receive the
  `/bingo hex` self-pick list or the free-pick list at all (previously the list
  was sent and only the final pick was rejected).
- **Dragon Tamer** (`dragon_rider`): drops from mobs killed by the baby dragon
  are given directly to its owner instead of falling where the mob died.
- **Big Brain** (`big_brain`): the description now mentions that items newly
  placed in the main inventory during the transform are cleared.

## 26.2-1.43.8 — 2026-11-12

### Fixed

- **Spy** (`spy`): overflow items that do not fit after the inventory swap now
  drop at both players' feet at their post-swap positions instead of the
  pre-swap spots; instances are re-fetched so the drops also land correctly
  when the swap crosses dimensions.
- **Bees** (`bees`): only bees summoned by the hex itself can bypass an
  already-cancelled damage event; wild bees no longer force damage through.
- **Logout cleanup**: the free pick, mirror-open and task-pick modes are
  cleared when a player logs out, so they cannot keep picker privileges after
  reconnecting.
- **Game data**: hex lists, granted sources, pending rewards, offers, extra
  choice and hex-locked sets are now saved as structured NBT lists
  (`format = 2`) instead of delimiter-joined strings; old saves are still
  readable via the legacy fallback.
- **Cell Swap** (`cell_swap`): the team slot update no longer uses reflection
  and a 25-bit mask (which wrapped for larger boards); it now goes through the
  public `Team` API with a completed-set rebuild for uncompleting, matching
  the existing uncomplete path.
- **Fae Magic** (`fae_magic`): attribute modifier IDs are now unique per
  target, so curses from different casters can no longer overwrite each
  other's modifiers.

## 26.2-1.43.7 — 2026-11-12

### Fixed

- **Cross-dimension entity references**: after a dimension change the mod kept
  stale entity references (26.2 rebuilds the entity instance on cross-dimension
  teleports and removes the old one). Fairy disguise, the minion pet and the
  bat-form bat now update their map references from the teleport result and
  re-apply `noPhysics`. Previously the old reference was treated as dead,
  which permanently dismissed the pet or spawned duplicate orphan disguises.
- **Rainbow Duel** (`rainbow_duel`): the 6000-tick timeout is no longer tied to
  `pvpOccurred`. After the first PVP hit both players could stall the duel
  forever inside the bedrock room; the duel now always times out.
- **Random teleport / wormhole**: candidate chunks are loaded before taking
  surface heights, so the search no longer reads world-minimum heights from
  unloaded chunks and fails almost every attempt.
- **Dragon Tamer** (`dragon_rider`): the baby dragon is discarded when its
  owner goes offline instead of being left to be saved with its chunk, and
  stale dragons loaded from disk (server restart / chunk reload) are removed
  on their first tick instead of lingering as invincible orphans.
- **State cleanup**: `BANISHED` now expires unconditionally (also while the
  player is offline) and is cleared on game reset; `clearAll` and
  `clearNewStates` now also clear RTP cooldowns, spy/mirror one-shot marks,
  untouchable / chaos mind / spider climb / banish / the-world cooldowns,
  kill stacks, beacons, voyage locks, gold-rush and bee-target maps.
- **Enchanting** (`enchant_gear`): curse enchantments are excluded via the
  `minecraft:curse` tag and enchants are only applied when
  `Enchantment#canEnchant` allows them (no more binding/vanishing curses or
  invalid enchantments on gear).
- **Snow Pumpkin** (`snow_pumpkin`): re-cursing an already-pumpkined player no
  longer overwrites the backed-up real helmet with the cursed pumpkin
  (`putIfAbsent`), so the original equipment cannot be lost.
- **Structure teleports**: structure bounding boxes are now filtered by the
  target structure tag, so a closer unrelated structure (e.g. a village next
  to a ruined portal) can no longer be picked as the teleport anchor; surface
  heights for structure spots and structure maps load the chunk first.
- **Task-pick packet**: the server now drops task-pick requests that were not
  preceded by an actual picker offer (client-controlled index is only applied
  after the mode was opened server-side).

## 26.2-1.43.6 — 2026-11-12

### Fixed

- **Dragon Tamer** (`dragon_rider`): the baby dragon's follow logic now runs
  from the server tick instead of the pet's own entity tick. It recovers from
  cross-dimension travel (26.2 rebuilds the entity instance on dimension
  change, which invalidated the old pet reference), long-range teleports and
  chunk unloads — if the tracked pet is removed, a fresh one is spawned next
  to the owner, and the orbit resumes.
- The baby dragon is now fully invincible: all incoming damage is cancelled,
  including creative-mode player attacks and void damage, which bypass the
  vanilla `Invulnerable` flag.

## 26.2-1.43.5 — 2026-11-12

### Fixed

- **Bound skill items**: Objective Deny, Blind, Bat Form, Mind Control, X-Ray,
  Mirror Copy and Blank Cell items are now bound like the OK Boomerang — they
  cannot be dropped or placed into chests, furnaces, backpacks or other
  containers, and are automatically pulled back if they end up in one.

## 26.2-1.43.4 — 2026-11-12

### Changed

- **Is It Over?** (`is_it_over`): while exiled in the End, the exiled player can
  no longer be teleported by anything, and other players can no longer teleport
  to their position via `/tp`, `/teleport`, `/bingo tp`, etc. Self-movement such
  as ender pearls and chorus fruit still works.

## 26.2-1.43.3 — 2026-11-12

### Changed

- **Hell Start** (`ruined_portal`): before teleporting, the structure area is
  scanned for obsidian, crying obsidian, netherrack, gold blocks, magma blocks,
  nether bricks and stone bricks to pin down the actual portal, then a safe spot
  next to it is used.
- **Eighty-One Trials** (`trial_chambers`): added a scan for tuff-family blocks,
  waxed copper blocks, copper grates and copper bulbs to locate the chamber
  interior before picking a landing spot; the old tuff/copper floor scan remains
  as a fallback.

## 26.2-1.43.2 — 2026-11-12

### Fixed

- **Item task "Enchanted Book"**: any enchanted book now completes the task,
  regardless of its enchantments or extra components. Previously only the
  enchantment components were stripped before comparison, so books obtained
  from loot tables or mods with additional components could never count.

## 26.2-1.43.1 — 2026-11-12

### Fixed

- **Structure / biome teleport accuracy**: `/locate` returns structure positions with
  Y fixed at 0, so the old code used the surface height above the structure. Teleports
  now resolve the real structure bounding box from structure starts and search for a
  safe spot around its center; underwater structures still land on the water surface.
  Affects Love Tribute (shipwreck), Hell Start (ruined portal), Pillager Outpost,
  All Roads Lead to Rome (village) and Eighty-One Trials (trial chambers).
- **Gaze Into the Abyss** (`gaze_abyss`): the deep dark search now requires the
  candidate spot to actually be inside the `deep_dark` biome and scans the full
  vertical column, so players are no longer dropped on the surface above the biome.

## 26.2-1.43.0 — 2026-11-12

### Added

- **Hatsune Miku** (`mangrove_map`, gold): gain a map pointing to the nearest
  mangrove swamp biome. If none is found within 12800 blocks, a mangrove swamp
  patch is created 500 blocks away and marked on the map.

## 26.2-1.42.10 — 2026-11-11

### Fixed

- **Dragon Tamer**: after a server restart an orphaned baby dragon (custom name
  `幼龙`) is no longer controlled by the pet map. It now has its AI disabled
  and goals cleared when ticked, so it cannot revert to a hostile Phantom and
  attack players.

## 26.2-1.42.9 — 2026-11-11

### Changed

- **Dragon Tamer** (`dragon_rider`): the baby dragon now keeps the vanilla
  Phantom AI for flight/turning, but all of its goal and target goals are
  removed on hatch, so it can no longer acquire or attack players. Its orbit
  point is written into `Phantom.moveTargetPoint` every tick, letting the
  vanilla `PhantomMoveControl` fly and bank toward it smoothly.
- The pet no longer damages any player (previously it only spared the owner,
  teammates and other Dragon Tamer owners); it still breaks blocks and knocks
  back non-player mobs in its path.
- If the owner is more than 32 blocks away (teleport / dimension change), the
  pet is pulled to the orbit point and the AI continues from there.

## 26.2-1.42.8 — 2026-11-11

### Fixed

- Removed leftover old-namespace task type checks:
  - `"bingo.item"` is replaced with `TaskTypeItem.INSTANCE`
    (holding-item speed, item task lookup).
  - `"bingo.empty"` is replaced with `TaskTypeEmpty.INSTANCE`
    (Wildcard cell picking, Demon Pact highest-weight task picking).
  Those checks could never match the current `bongo.*` type ids, so the
  related hex effects could silently fail or pick empty tasks.
- Re-searched the whole source tree for `"bingo."` literals; only the custom
  lang key `bingo.task.enchantment.name` (present in both languages) and the
  `/bingo` command literal remain.

## 26.2-1.42.7 — 2026-11-11

### Fixed

- **Dragon Tamer** (`dragon_rider`): the baby dragon now uses a Phantom
  instead of an EnderDragon. Vanilla `EnderDragon.sanitizeScale()` always
  returns 1.0 and the dragon has its own boss flight phases, so it cannot be
  scaled or used as a normal pet.
- The orbit movement now runs after the pet's own entity tick, so position
  updates reach the client and the dragon visibly circles the player.
- Baby dragon sound volume halved (0.7 -> 0.35); hatch growl reduced from 1.0
  to 0.5. The pet is also much smaller (Phantom size 0, scaled to 0.5).

## 26.2-1.42.6 — 2026-11-11

### Fixed

- **OK Boomerang** (`boomerang_biome`): the biome task check used the wrong
  type id (`bingo.biome` instead of `bongo.biome`), so it reported "no
  unfinished biome task" even when one existed. It now compares against
  `TaskTypeBiome.INSTANCE` directly.

## 26.2-1.42.5 — 2026-11-11

### Changed

- Renamed the **Boomerang OK** hex and item to **OK Boomerang** (`ok回力镖`).
- The boomerang now searches for the target biome with a radius of 12800. If
  the biome still cannot be found, an artificial patch of that biome is
  generated about 500 blocks ahead with `/fillbiome`, and the boomerang points
  there.

## 26.2-1.42.4 — 2026-11-11

### Changed

- **Objective Deny** (`objective_deny`): cooldown reduced from 15 minutes to
  6 minutes.

## 26.2-1.42.3 — 2026-11-11

### Fixed

- **Dragon Tamer** (`dragon_rider`): when the egg is pushed by a piston or
  teleported by vanilla, the incubation restarts at the new position and the
  countdown is displayed again. The countdown text display now initialises its
  billboard/see-through/view-range through NBT, so it is readable from any
  angle.
- **Blank Cell** (`blank_cell`): lighting a task no longer replaces its icon
  with the Wildcard texture; the original task icon is kept and only your team
  receives the cell.

## 26.2-1.42.2 — 2026-11-11

### Changed

- **Bat Form** (`bat_form`): flight speed is reduced by 35% while in bat
  form, so the following bat can keep up with the player.

## 26.2-1.42.1 — 2026-11-11

### Fixed

- **Bat Form** (`bat_form`): when the form ends, grant 1 minute of Slow
  Falling so players do not fall to their death.
- **Dragon Tamer** (`dragon_rider`): re-placing the egg at the same position no
  longer deletes the newly placed egg and leaves it unable to incubate.
  Hatching now removes the egg in the incubation's own dimension, plays a
  dragon growl, and notifies the owner and teammates.
- **Ascetic** (`ascetic`): added the missing `hexbingo.hex.prefix.ascetic`
  translation, so the granted hexes show "Ascetic: <name>" instead of the raw
  language key.
- **Blank Cell / Objective Deny**: re-checked card-altering hexes (Flip Table,
  Card Shuffle, Wildcard, etc.); the picker always reads the current card and
  the latest task list.

## 26.2-1.42.0 — 2026-11-11

### Changed

- **Portable Furnace** (`portable_furnace`): smelting continues while the
  furnace screen is closed instead of only ticking while it is open.
- **Bat Form** (`bat_form`): the following bat is now placed at the player's
  feet and scaled down, so it no longer fills the first-person camera.
- **Dragon Tamer** (`dragon_rider`): the incubation countdown now uses a
  `text_display` entity instead of an invisible armor stand, and is recreated
  after a chunk reload.
- **Blank Cell / Objective Deny**: the task picker now renders the current
  5×5 Bingo card like the Y-key big overlay; click a cell to choose it.
  Fixes the blank cell reading the wrong task list.
- Advancements earned from hex-granted items (elytra, dragon egg, trident,
  etc.) still show normally but no longer complete advancement-type Bingo
  tasks. A chat notice explains why.

## 26.2-1.41.1 — 2026-11-10

### Changed

- **Dragon Tamer** (`dragon_rider`): the placed dragon egg now incubates for
  3 minutes with a floating countdown above it. Mining it, pushing it with a
  piston, or causing it to teleport resets the timer.
- The baby dragon no longer damages its owner or any player who owns Dragon
  Tamer.
- Added 4 random baby dragon sound variants (`hexbingo:baby_dragon`), mono
  64 kbps OGG, roughly 15-18 KB each.

## 26.2-1.41.0 — 2026-11-10

### Added

- **Advance Victory** (`advance_victory`, silver): instantly light one cell for
  5 minutes; it only stays if the team genuinely completes it in time.
- **Steal the Sky** (`cell_swap`, silver): swap ownership of one completed cell
  with an enemy team.
- **Journey to the Core** (`journey_core`, silver): teleport to a cave at Y=-50.
- **Ascetic** (`ascetic`, silver): clear all of your team's completed cells and
  gain 2 random prismatic hexes. Only offered in the third hex selection and
  excluded from every quality-shift/copy source.
- **Destiny Dice** (`destiny_dice`, gold): roll a die to complete a cell, remove
  an enemy cell, both complete a cell, or reshuffle all cards.
- **Great Migration** (`great_migration`, gold): scatter every player across
  the overworld within 1000 blocks of each other.
- **Dragon Tamer** (`dragon_rider`, gold): a placeable dragon egg hatches a
  small silent dragon that circles the owner, breaks blocks in its path with
  drops, and knocks back creatures for 4 damage.
- **Blank Cell** (`blank_cell`, prismatic): a team-exclusive wildcard item that
  lets you pick any incomplete task and light it for your team only.

### Changed

- **Wildcard** (`wildcard`) moved from prismatic to gold.

## 26.2-1.40.0 — 2026-11-10

### Changed

- Cherry Grove / Pale Garden / Deep Dark lookup range increased from 6400 to
  12800 blocks with a coarser sampling step.
- Added a biome fallback for all three hexes: if no target biome is found, a
  suitable spot 500 blocks away is converted with `/fillbiome` and populated
  with the matching features (cherry trees/flowers, pale oaks with creaking
  hearts/moss, or an underground deepslate cave filled with sculk for Deep
  Dark). The hexes now always take the player to the target biome.

## 26.2-1.39.0 — 2026-11-10

### Added

- **Who Is the Pillager Now** (`pillager_outpost`, silver): teleport to the
  nearest pillager outpost. If none is found, generate one 200 blocks away in
  one of 8 directions, preferring a suitable land biome.
- **The Cherry Blossoms of My Hometown Have Bloomed** (`cherry_grove`, silver):
  teleport to the nearest cherry grove biome.
- **Red Light, Green Light** (`pale_garden_map`, silver): receive a map
  pointing to the nearest pale garden biome.

### Changed

- Pet Master (`pet_companion`): `/kill` now removes the invulnerable pet for
  good (it no longer respawns automatically). Creative-mode and other
  invulnerability-bypassing damage is still blocked.
- Meteor (`meteor`): the earthquake now crumbles blocks one at a time in
  random order instead of clearing the whole area instantly.

## 26.2-1.38.1 — 2026-11-10

### Fixed

- Pet Master (`pet_companion`): the invulnerable companion can no longer be
  killed. Vanilla invulnerability is bypassed by creative-mode players (and by
  damage that bypasses invulnerability), which previously killed the pet.
  Incoming damage to the pet is now cancelled outright, and the pet's
  invulnerability and health are re-asserted every tick.

## 26.2-1.38.0 — 2026-11-10

### Added

- **Cutting Edge Inventor** (`cutting_edge`, gold): all skill-type hex
  cooldowns of the current player are reduced by 30%. This applies to the
  skill items and abilities with cooldowns (blind, bat form, midas touch,
  mind control, objective deny, x-ray, dash, fae magic, trap, jam, hook,
  untouchable, chaos mind, banish, the world, random teleport, portal bench
  furnace skills that use cooldowns).

### Changed

- Bat Form (`bat_form`): the disguise bat now has AI, is invulnerable and
  audible (no longer silenced), still follows the player every tick including
  dimension changes. The player is now invisible and shrunk to 0.5 blocks tall.

## 26.2-1.37.2 — 2026-11-10

### Changed

- Bat Form (`bat_form`): the player now really turns into a bat, using the same
  disguise approach as Fae Magic. The player entity is set invisible and a
  no-AI, invulnerable, silent bat is spawned and follows the player every tick
  (with dimension changes). The disguise is removed when the form ends, on
  death, on logout, on game reset, and leftover disguises are cleaned up.

## 26.2-1.37.1 — 2026-11-10

### Fixed

- Blind (`blind`): casting it now plays the squid ink spray sound.
- Is It Over? (`is_it_over`): no longer fails with "the End dimension is
  unavailable". The outer-island chunks are force-loaded before reading the
  surface height, so the exile teleport finds a real island (previously the
  height of unloaded chunks resolved to the world bottom and the lookup always
  failed). A separate message is shown when no island spot is found.
- Portable Furnace (`portable_furnace`): the furnace logic ran only once per
  second, so cooking was 20x too slow and the burn bar advanced far too
  slowly. It now ticks every server tick, matching vanilla furnace speed.
- Smelting Speed (`smelt_speed`): the extra furnace tick was also only applied
  once per second, which came out at roughly +5% instead of the advertised
  doubling. Furnaces now receive one extra tick per server tick (2x), and the
  Portable Furnace cooks twice as fast as well.

## 26.2-1.37.0 — 2026-11-10

### Fixed

- Meteor (`meteor`): impacts now land on the real surface. The height is
  queried after force-loading the target chunk, so unloaded terrain no longer
  resolves to the world bottom (which made meteors carve into the bedrock
  layer).
- Rush Rush Rush (`dash_charge`): the dash now advances every server tick and
  is fully automatic - the forward key is no longer needed, the view only
  steers the direction. Blocks in a 3x3 area along the path are broken
  (obsidian and bedrock excluded).
- Mirror Hex (`mirror_hex`): the item is single use now. It is consumed as soon
  as the selection list opens and is no longer re-granted automatically.

### Notes

- Pair with BingoLobby `26.2-1.0.0-port.2`, which stops automatically running
  `bingo stop` after a game has been won.

## 26.2-1.36.1 — 2026-11-10

### Fixed

- Requires ReBongo `26.2-1.0.0-port.4` or newer. That ReBongo build registers
  its command argument types with `ArgumentTypeInfos`, which fixes players
  being disconnected with "Invalid player data"
  (`Unrecognized argument type: GameTasksArgument`) when joining a world or
  server.

## 26.2-1.36.0 — 2026-11-10

### Added

- **Gaze Into the Abyss** (`gaze_abyss`, gold): teleport to the nearest
  `deep_dark` biome and find a safe spot inside its caves.

### Fixed

- Structure teleport hexes now search and generate in the correct dimension:
  - Village Road (`village_road`) and Love Tribute (`love_tribute`) use the
    overworld.
  - Ruined Portal (`ruined_portal`) uses the Nether when cast there, otherwise
    the overworld; the Nether fallback generates `ruined_portal_nether`.
  - Eighty-One Trials (`eighty_one_trials`) uses the overworld.
  Previously, using these while in the Nether or the End could fail to find
  anything or try to place the structure in the wrong dimension.
- Love Tribute / Ruined Portal no longer regenerate a replacement when the
  found structure is underwater; the player is teleported to the water surface
  above the real structure instead.
- Travel Frog's fallback structure is now generated in the same dimension as
  the map it hands out, instead of the player's current dimension.

## 26.2-1.35.5 — 2026-11-09

### Fixed

- **Fae Magic** (`fae_magic`): cross-dimension teleports now move the disguise
  with the player instead of discarding it and spawning a replacement, so no
  no-AI critter is left behind in the old dimension.
- Stray `精怪魔法` disguise mobs in loaded chunks are cleaned up when a game is
  reset or stopped.

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
