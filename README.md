# Bingo: Hex Mayhem — 非官方 Bingo 海克斯附属

> **原项目（上游）**
> - Bongo — <https://github.com/noeppi-noeppi/Bongo>
> - BingoLobby — <https://github.com/noeppi-noeppi/BingoLobby>
> - LibX — <https://github.com/ModdingX/LibX>
>
> **非官方项目**：这是自写附属模组，不是 Bongo / BingoLobby 的官方项目，与 **noeppi_noeppi**、**ModdingX** 没有隶属关系，也未获其背书或赞助。
>
> **许可**：Apache License 2.0。源码即本仓库。音频素材存在第三方版权待处理项，见「素材状态」。

## 简介

为 [ReBongo](https://github.com/Big-Jls/rebongo)（Bongo 的 NeoForge 26.2 移植版）增加 **132 个海克斯强化**：52 银色 / 45 金色 / 35 彩色。包含三选一选择界面、技能物品、被动效果，以及任务类与战斗类强化。

## 玩法

- 对局中会发放海克斯三选一，选择后立即生效。
- 默认节奏（可在 `config/hexbingo-common.toml` 调整）：
  - 第 1 个：完成第一个任务时（可改为完成第一条 Bingo 线时）
  - 第 2 个：开局 5 分钟
  - 第 3 个：开局 10 分钟
  - 第 4 个：开局 15 分钟，且必为彩色
- 部分海克斯会发放右键使用的技能物品（例如「混沌智」「不可触碰」「冲冲冲」），有独立冷却，不能被丢弃或塞进容器。
- 「冲冲冲」是持续 8 秒的自动冲刺：无需按前进键，只用视线控制方向、保持施放高度，沿途 3×3 范围方块会掉落，撞到黑曜石或基岩会中断，结束后进入 15 秒冷却。
- 「终极刷新」的刷新按钮只在拥有该海克斯且仍有次数时出现在选择界面底部；每获得一次只能用一次，可以保留到之后的某次选择，对局结束清空。
- 海克斯分三档：银色偏基础属性/资源，金色偏战术能力，彩色偏强力或改变规则。

## 环境要求

| 依赖 | 版本 |
|---|---|
| Minecraft | 26.2 |
| NeoForge | 26.2.0.93 或更高 |
| Java | 25 |
| ReBongo（`modId = bongo`） | 26.2-1.0.0-port.4 或更高 |
| LibX | 26.2-8.0.0（官方版） |

ReBongo：https://github.com/Big-Jls/rebongo

## 安装

1. 安装 NeoForge 26.2.0.93+ 与官方 LibX 26.2-8.0.0。
2. 安装 ReBongo（`ReBongo-26.2-1.0.0-port.9.jar`，`modId` 为 `bongo`）。
3. 把 `HexBingo-26.2-1.43.15.jar` 放入客户端与服务端的 `mods`。
4. 开一局 海克斯Bingo：/bingo create bongo:<难度> bongo:mayhem
5. 按提示选择海克斯。

## 按键与命令

| 按键 | 作用 |
|---|---|
| **K** | 打开「海克斯一览」：查看所有队伍/玩家已经选了哪些海克斯（只读名单），叠角龙会显示当前层数 |
| **V** | 切换蜘蛛类效果（获得对应海克斯后可用） |
| **T** | 开关吸铁石吸物功能（获得对应海克斯后可用） |
| **G** | 点石成金：把副手物品转化为等量金锭（获得对应海克斯后可用） |

| 命令 | 权限 | 说明 |
|---|---|---|
| `/bingo hex` | 所有人 | 重新打开当前待选的三选一界面（对应第 1 / 5 / 10 / 15 分钟那几次奖励） |
| `/bingo hexes` | **OP** | 自由选择：从你尚未拥有的海克斯里挑一个 |
| `/bingo hexall` | **OP** | 给**所有在线玩家**各打开一次自由选择界面，每人从自己未拥有的海克斯里选一个 |
| `/bingo rtp` | 所有人 | 随机传送（需对应海克斯） |
| `/bingo hook` | 所有人 | 钩爪（需对应海克斯） |
| `/bingo boomerang` | 所有人 | 回旋镖（需对应海克斯） |
| `/bingo trap` | 所有人 | 陷阱（需对应海克斯） |
| `/bingo spy <队友> <目标>` | 所有人 | 间谍交换 |

> `/bingo hexall` 不是 K 键的名单：K 只查看，`hexall` 是管理员给全服每人发一次「自由选一个」的机会。

<!-- HEX-TABLE-START -->
## 海克斯一览（132）

### 银色（52）

| 名称 | id | 效果 |
|---|---|---|
| 经验专精 | `experience_mastery` | 获得 500 点经验和 8–32 个随机青金石。 |
| 我看到你了 | `i_see_you` | 每 2 秒使周围 20 格内的敌对生物发光。 |
| 质变黄金阶 | `golden_shift` | 获得一个随机金色海克斯。 |
| 我想要 | `want_items` | 获得 5 个随机物品（与仆从大师宠物同款加权随机池）。 |
| 枯木生花 | `random_wood` | 获得 5 种随机木头和 5 种随机花。 |
| 练腿日 | `leg_day` | 永久获得速度 I。 |
| 大力 | `strong` | 永久获得力量 II。 |
| 更万用瞄准镜 | `scope_1` | 方块交互距离 +2，实体交互距离 +2。可与其他瞄准镜叠加。 |
| 家园卫士 | `homeguard` | 持续奔跑时越跑越快，每跑 10 秒提升一级，停止 0.2 秒后失去加速，最高速度 IV。 |
| 叠角龙 | `stack_dragon` | 每击杀一个生物累计 1 层，每 15 层获得一个战利品箱，层数越高奖励越好。 |
| 转到我眩晕了 | `hook_dizzy` | 获得 /bingo hook：把你拉向准星指向的方块，冷却 5 秒。 |
| 珍珠储备 | `pearl_stock` | 获得 8 个末影珍珠和 2 个烈焰粉。 |
| 练手日 | `practice_day` | 永久获得急迫 I。 |
| 地狱无门闯进来 | `nether_start` | 立即被传送到地狱，并禁用 /tp、/bp 与 /bingo tp；返回主世界后自动解除。 |
| 星际航行 | `star_voyage` | 传送到你当前高度上方 256 格并获得一个平台和水桶；必须用水桶安全落地，否则会摔死。成功落地后随机解锁 1 个 Bingo 格子。 |
| 换位思考 | `swap_tasks` | 随机交换当前 Bingo 卡片上两个任务的位置。 |
| 迅捷斥候 | `mushroom_scout` | 以你为中心 3 格内随机长出各种蘑菇（含地狱菌类）。 |
| 虫洞 | `wormhole` | 立即随机传送到距当前位置 1–1000 格的地方。 |
| 飞天猫猫神教 | `cat_cult` | 在周围召唤所有种类的猫猫。 |
| 汪汪队 | `dog_squad` | 在周围召唤所有种类的狗狗（包含不同群系变体）。 |
| 对方不在服务区 | `jam_bp` | 获得「对方不在服务区」：右键禁用敌方队伍 /bp 3 分钟，冷却 9 分钟。 |
| 淘金热 | `sand_gold` | 挖掘沙子时有 3% 概率获得金粒。 |
| 滴滴打眼 | `eye_taxi` | 你投出的末影之眼不再碎裂。 |
| 步步生花 | `flowers_walk` | 你走过草方块时会长出花。 |
| 装备升级 | `forge_master` | 将背包里的工具、武器替换成随机等级的同类型装备。 |
| 随便 | `random_bingo` | 随便提供一个bingo（包含已完成）。 |
| 致敬爱情 | `love_tribute` | 传送到最近的沉船。 |
| 地狱开局 | `ruined_portal` | 传送到最近的废弃地狱门结构。 |
| 古代遗迹 | `ancient_ruins` | 在你附近随机生成 4–15 个考古砂砾或考古沙子。 |
| 羊了个羊 | `sheep_wool` | 击杀羊时额外掉落 2 个羊毛，颜色随机。 |
| 随身工作台 | `portable_bench` | 获得「随身工作台」：右键直接打开 3×3 合成界面，无需放置工作台。 |
| 催熟喷雾 | `bone_meal_spray` | 每 10 秒向周围 9 格范围内的作物与植物洒下骨粉，随机催熟最多 12 处。 |
| 村庄英雄 | `village_hero` | 永久获得村庄英雄 I。 |
| 种子礼包 | `seed_gift` | 随机获得小麦、胡萝卜、马铃薯、甜菜根、西瓜、南瓜种子，每种 1-8 个。 |
| 考古刷 | `archaeology_brush` | 获得一把不会损耗耐久的刷子。 |
| 跳跳虎 | `jump_tiger` | 永久获得跳跃提升 II。 |
| 昼夜颠倒 | `day_night_flip` | 立即切换昼夜，之后 10 分钟内时间不再自然变化。 |
| 工具箱 | `toolbox` | 随机获得工作台、熔炉、铁砧、砂轮、切石机中的 1 个。 |
| 鱼神 | `dolphin_dash` | 在水中获得永久的海豚的恩惠 II。 |
| 便携熔炉 | `portable_furnace` | 获得「便携熔炉」：右键随时打开熔炉界面；关闭界面后仍会继续烧制，熔炼速度受「烧制加速」影响。 |
| 烧制加速 | `smelt_speed` | 附近的熔炉、高炉、烟熏炉烧制速度翻倍。 |
| 陨石 | `meteor` | 在你周围召唤 5-10 颗大小不一的紫水晶陨石坠落（最远不超过 500 格）；其他队伍玩家被减速 III，脚下地块会像真实地震一样逐格崩塌，并获得标记落点的地图。 |
| 品质提升 | `offer_upgrade` | 下一次海克斯选择必定出现至少 1 个彩色；只会在第一次海克斯选择中出现。 |
| 致盲 | `blind` | 获得「致盲」：右键使所有敌人失明 8 秒。冷却 3 分钟。 |
| 结束了? | `is_it_over` | 传送到末地外岛 10 分钟，期间无法被任何传送带走，结束后自动返回。 |
| 谁才是掠夺者 | `pillager_outpost` | 传送到最近的掠夺者前哨站；附近没有时在 8 个方位 200 格外生成一座。 |
| 故乡的樱花开了 | `cherry_grove` | 传送到最近的樱花群系；附近没有时在 500 格外改造出一片樱花林。 |
| 一二三木头人 | `pale_garden_map` | 获得一张指向最近苍白花园群系的地图；附近没有时在 500 格外改造一片并标记。 |
| 预支胜利 | `advance_victory` | 立即点亮一格，但 5 分钟后熄灭，除非期间真正完成它。 |
| 偷天换日 | `cell_swap` | 交换你与一名敌人的一个已完成格归属。 |
| 地心游记 | `journey_core` | 传送到 Y=-50 的地下洞穴。 |
| 苦行者 | `ascetic` | 清空本队所有已完成格，立即获得 2 个随机彩色海克斯。只在第三次海克斯选择中出现。 |

### 金色（45）

| 名称 | id | 效果 |
|---|---|---|
| 免疫护盾 | `immunity_shield` | 每 3 分钟免疫对手的下一个技能类海克斯。 |
| 锻造大师 | `inventor` | 获得时附魔当前背包里的工具、武器和盔甲；以后制作的同类物品也会获得随机附魔。 |
| 随机传送 | `random_teleport` | 获得 /bingo rtp 指令：在当前位置附近随机传送，冷却 5 秒。 |
| 尖端发明家 | `cutting_edge` | 所有技能型海克斯的冷却时间缩短 30%。 |
| 我很想要 | `want_more_items` | 获得 10 个随机物品（与仆从大师宠物同款加权随机池）。 |
| 更更万用瞄准镜 | `scope_2` | 方块交互距离 +5，实体交互距离 +5。可与其他瞄准镜叠加。 |
| 质变棱彩阶 | `prismatic_shift` | 立刻获得一个随机彩色海克斯。 |
| 小蜜蜂 | `bees` | 在其他所有队伍附近召唤 10 只愤怒的蜜蜂。蜜蜂可以无视无敌蜇伤玩家。 |
| 饕餮盛宴 | `gluttonous_feast` | 永久获得饥饿 II；每次吃下食物，随机获得一个等级 II 的增益，持续 15 秒。 |
| 超强大脑 | `big_brain` | 你的所有物品变成随机物品持续 5 秒，之后恢复原背包。注意：期间新放进主物品栏的物品会被清除。 |
| 掀桌子？ | `flip_table_one` | 重新随机一个尚未被任何队伍完成的任务。 |
| 灵魂炸弹 | `soul_bomb` | 获得 3 个灵魂沙、2 个凋零骷髅头和 1 个打火石。 |
| 条条大路通罗马 | `village_road` | 将你传送到最近的村庄。 |
| 旅行青蛙 | `travel_frog` | 召唤一只青蛙；主手持半组食物右键它，可换取一张随机结构地图（沉船 / 末地遗迹 / 林地府邸 / 村庄 / 幽匿城市 / 前哨站 / 沙漠神殿 / 丛林神庙；在地狱时为猪灵堡垒 / 地狱堡垒 / 废弃地狱门）。 |
| 健忘症 | `amnesia` | 随机将另一支队伍的一个已完成 Bingo 退回未完成。 |
| 冰！ | `ice_boots` | 获得冰霜行者 II 钻石靴。 |
| 雪傀儡互助会 | `snow_pumpkin` | 给敌方所有玩家戴上带绑定诅咒的雕刻南瓜，无法自行脱下；需要队友用剪刀右键你摘除，单人队伍可手持剪刀潜行右键自己。 |
| 宝可梦大师 | `pokemon_master` | 获得 6 个随机刷怪蛋（可重复）。 |
| 乌鸦嘴 | `raven_mouth` | 召唤一只黑色鹦鹉；它会随机退回你一个已完成任务，或随机完成一个任务。 |
| 和朋友一起困在这里 | `friends_here` | 附近每有一位队友，获得 +2% 移速和急迫 I（最多 +10%）。 |
| 得吃小曲 | `dechi_song` | 播放村民版《得吃小曲》；期间获得随机幸运 III–V，背包里的镐子获得时运 II–III。 |
| 理想国 | `utopia` | 打乱你身上所有药水效果，并重新获得 5 种永久随机增益或减益。 |
| 冲冲冲 | `dash_charge` | 获得「冲冲冲」：右键后自动向前冲刺 8 秒（无需按前进键），可用视线控制方向，破坏沿途 3×3 范围内除黑曜石、基岩外的方块并使其掉落，对撞到的生物造成 10 点伤害。冲刺结束后冷却 15 秒。 |
| 灵魂出窍 | `soul_out` | 选中后立即进入旁观状态 30 秒，期间可以自由飞行观察，结束后回到进入旁观前的位置。 |
| 九九八十一难 | `eighty_one_trials` | 传送到最近的试炼大厅。 |
| 混沌智 | `inventory_shuffle` | 右键使用技能物品：随机打乱其他队伍玩家的背包、副手与盔甲槽位（物品可能与装备互换位置）。冷却 8 分钟。 |
| Spider Man | `spider_man` | 像蜘蛛一样爬墙：身体贴到任意墙即可吸附，按住前进向上爬，松开贴墙不下落，潜行取消；按 V 可开关爬墙功能。 |
| 吸铁石 | `magnet` | 16 格内的掉落物会自动飞向你。按 T 可开关吸铁功能。 |
| 3面骰 | `dice_roll` | 每 5 分钟投一次六面骰（屏幕播放掷骰动画后才揭晓）：大于 3 获得速度 II 4 分钟，小于 3 获得缓慢 I 4 分钟，等于 3 禁锢 6 秒。 |
| 路易十六 | `louis_xvi` | 击杀的第一个生物必定掉落 1 个该生物的头颅。 |
| 快递到了 | `airdrop` | 在你头顶空投一个装有随机战利品箱奖励的箱子。 |
| 海神 | `sea_god` | 获得一把附魔三叉戟（忠诚 III、引雷 I、经验修补 I），掷出后自动回收。 |
| 图腾批发 | `totem_bulk` | 获得 3 个不死图腾；手持不死图腾时可受到伤害，用于完成不死图腾成就。 |
| 末影杀手 | `ender_slayer` | 击杀末影人时有 1% 概率掉落一本附魔书：精准采集 I、经验修补 I 或无限 I。 |
| 终极刷新 | `ultimate_refresh` | 下次海克斯选择可以额外刷新一次，并随机获得 2 个战利品箱。 |
| 自动熔炼 | `auto_smelt` | 挖掘矿物时自动烧炼掉落物，并额外获得经验。 |
| 任务护盾 | `task_shield` | 锁定当前队伍权重最高的 3 个任务，其他海克斯无法将它们退回。 |
| 点石成金 | `midas_touch` | 按 G 把副手物品转化为等量金锭（贵重物品除外）。冷却 1 分钟。 |
| 蝙蝠形态 | `bat_form` | 获得「蝙蝠形态」：右键变成蝙蝠 60 秒，蝙蝠跟在玩家脚边；可飞行（飞行速度降低 35%）、无法攻击、血量与体型变小，结束后获得 1 分钟缓降。冷却 7 分钟。 |
| 凝视深渊 | `gaze_abyss` | 传送到最近的深暗之域（deep_dark）群系，并自动寻找洞穴中的安全落脚点；附近没有时在 500 格外地下生成一处深暗洞穴。 |
| 万能牌 | `wildcard` | 在 Bingo 卡面随机点亮一格（所有队伍共享这一格）。 |
| 命运骰子 | `destiny_dice` | 掷骰（播放动画后揭晓）随机触发：完成一格 / 取消敌人一格 / 双方各完成一格 / 全体卡面重排。 |
| 随机大迁徙 | `great_migration` | 所有玩家被随机传送到主世界彼此 1000 格内的位置。 |
| 驯龙高手 | `dragon_rider` | 获得一颗龙蛋；放下后需孵化 3 分钟，蛋上方的文字展示实体显示倒计时，挖掉或移走会重新计时。孵化出的幼龙使用幻翼 AI 环绕你飞行，跨维度、远距离传送与区块卸载后都会回到你身边继续环绕；经过的方块会掉落，碰到的非玩家生物会被击退并受到 4 点伤害，不会攻击任何玩家，且完全无敌（任何伤害都会被取消，包括创造模式攻击）。 |
| 初音未来 | `mangrove_map` | 获得一张指向最近红树林群系的地图；附近没有时在 500 格外改造一片红树林并标记。 |

### 彩色（35）

| 名称 | id | 效果 |
|---|---|---|
| 铁锁连环 | `chain_master` | 每完成一个团队任务，有 1% + 0.1% × 已完成任务数 的概率额外完成 1 个相邻任务，最高 50%。 |
| 我真的很想要 | `want_lots_items` | 获得 15 个随机物品（与仆从大师宠物同款加权随机池）。 |
| 更更更万用瞄准镜 | `scope_3` | 方块交互距离 +9，实体交互距离 +9。可与其他瞄准镜叠加。 |
| 夺金 | `gold_rush` | 攻击生物时随机获得 1-3 个金粒，同一生物冷却 5 秒。 |
| 飞升仪式 | `ascension` | 击杀敌对生物时获得随机增益。 |
| 静水监狱 | `still_water` | 所有人无法获得下一个及以后所有海克斯。 |
| 潘多拉的盒子 | `pandora` | 重新随机你已有的所有海克斯。 |
| 歌莉娅巨人 | `goliath` | 体型、最大生命、挖掘速度和攻击伤害提高 30%。 |
| 你摸不到 | `untouchable` | 右键使用技能物品：敌对 Bingo 队伍 30 秒内无法破坏方块，冷却 5 分钟。 |
| 玻璃大炮 | `glass_cannon_bag` | 背包后 18 格被屏障占据（放入的物品会被移出或掉落），但方块与生物掉落变为 8 倍，并受时运与幸运加成（每级 +25%）。 |
| 回归基本功 | `back_to_basics` | 无法使用工作台，但获得的物品掉落翻倍（受抢夺加成），并永久获得速度 I。 |
| 大法师 | `archmage` | 制作物品后，每个材料有 10% 概率返还。 |
| 仆从大师 | `pet_master` | 获得一只无敌宠物，每 8 秒从全物品池中随机给你一个物品（贵重物品概率较低）。 |
| 尊我为王 | `crown_me_king` | 敌方位置出现信标光柱，抵达后获得一个彩色海克斯、一套随机附魔钻石盔甲、效率 IV 钻石镐与锋利 IV/火焰附加 I 钻石剑。 |
| 掀桌子！ | `flip_table` | 重新随机所有未被任意队伍完成的任务。 |
| ok回力镖 | `boomerang_biome` | 获得ok回力镖：投掷后飞向一个未完成群系任务所在的群系并返回；搜索范围内找不到时自动在前方生成一处目标群系。 |
| 和我一起困在这里 | `rainbow_duel` | 获得一个绑定锁链。右键与最近的敌人进入基岩决斗场并允许 PVP：先死亡者失败，死亡者复活后额外坐牢 20 秒；胜者立即出来；5 分钟无人交战则双方自动出来。冷却 3 分钟。 |
| 精怪魔法 | `fae_magic` | 获得「精怪魔法」：右键把敌对队伍变成小动物 1 分钟，冷却 5 分钟。 |
| 中国人会飞 | `chinese_can_fly` | 获得一个无限耐久的鞘翅，以及一个用掉后会自动补充的绑定烟花火箭。 |
| 间谍 | `spy` | 使用 /bingo spy <队友> <敌人> 交换两人的位置和背包。钩爪、ok回力镖等绑定物品会留在原主人身上。整局只能使用一次。 |
| 共同富裕 | `common_wealth` | 没收所有人的物品栏物品到公共池，然后随机重新分配。 |
| 质变混沌 | `chaos_shift` | 随机获得两个随机种类的海克斯。 |
| CV工程师 | `task_copy` | 随机复制一个对手已完成而你队未完成的任务；Lockout 模式下改为直接抢占该格。 |
| 维度放逐 | `nether_banish` | 将其他每支队伍的一名玩家放逐到下界随机位置 1 分钟。冷却 8 分钟。 |
| The World！ | `the_world` | 获得「The World」时停技能：右键使用后时间停止 8 秒，只有你能移动。冷却 3 分钟。 |
| 腐蚀背包 | `corrupt_inventory` | 除你所在队伍外，每队随机一名玩家的 5 个物品变成腐肉。 |
| 黑市开张 | `black_market` | 下次打开村民交易界面时，将该村民升到最高等级，并获得永久村庄英雄 II。 |
| 恶魔契约 | `demon_pact` | 永久失去 10 点生命上限，但允许你受到伤害，获得一整套随机附魔铁套，并立即完成当前权重最高的 1 个 Bingo 任务。 |
| 精神控制 | `mind_control` | 获得「精神控制」：右键控制一名敌人 15 秒的移动方向。冷却 5 分钟。 |
| 目标封锁 | `objective_deny` | 获得「目标封锁」：右键打开当前 Bingo 卡牌，点击一个任务，30 秒内所有队伍无法完成它。冷却 6 分钟。 |
| 透视 | `xray` | 获得「矿物透视」：右键获得 20 秒矿物透视。冷却 5 分钟。 |
| 拾荒者 | `scavenger` | 破坏方块时有 3% 概率额外掉落该方块本身。 |
| 洗牌 | `card_shuffle` | 所有队伍的卡面位置随机重排，完成与锁定状态跟随任务移动。 |
| 镜像复制 | `mirror_hex` | 获得「镜像复制」：右键打开列表，复制最近一支其他队伍的一个你未拥有的海克斯。道具一次性，使用后消失。 |
| 空白格 | `blank_cell` | 获得「空白格」：右键打开当前 Bingo 卡牌，点击任意未完成任务并直接点亮，只为本队点亮且保留原任务图标。道具一次性。 |

## Hex list (English)

### Silver (52)

| Name | id | Effect |
|---|---|---|
| Experience Mastery | `experience_mastery` | Gain 500 experience and 8-32 random lapis lazuli. |
| I See You | `i_see_you` | Every 2s, hostile mobs within 20 blocks glow. |
| Golden Ascension | `golden_shift` | Gain a random gold hex. |
| I Want It | `want_items` | Gain 5 random items (same weighted pool as the Pet Master pet). |
| Dead Wood Blooms | `random_wood` | Gain 5 random wood types and 5 random flower types. |
| Leg Day | `leg_day` | Permanent Speed I. |
| Strength | `strong` | Permanent Strength II. |
| Utility Scope | `scope_1` | Gain +1 block interaction range and +1 entity interaction range. |
| Homeguard | `homeguard` | Gain speed while running continuously; each 10s raises the speed level. Stop for 0.2s and it fades. Max Speed IV. |
| Stack Dragon | `stack_dragon` | Each mob kill adds a stack; every 15 stacks grants a loot chest. Higher stacks mean better loot. |
| Hook Dizzy | `hook_dizzy` | Unlocks /bingo hook: pull yourself toward the block you are looking at. 5s cooldown. |
| Pearl Stock | `pearl_stock` | Gain 8 ender pearls and 2 blaze powder. |
| Practice Day | `practice_day` | Permanent Haste I. |
| Breaking Into Hell | `nether_start` | Immediately sent to the Nether; /tp, /bp and /bingo tp are disabled until you return to the Overworld. |
| Star Voyage | `star_voyage` | Teleported 256 blocks above your current height with a platform and a water bucket. You MUST land in water or you will die. Landing safely unlocks 1 random Bingo slot. |
| Perspective Swap | `swap_tasks` | Randomly swap the positions of two tasks on the current Bingo card. |
| Swift Scout | `mushroom_scout` | Random mushrooms (including nether fungi) grow in a 3 block radius around you. |
| Wormhole | `wormhole` | Instantly teleport to a random spot 1-1000 blocks away. |
| Flying Cat Cult | `cat_cult` | Summon every cat variant around you. |
| Dog Squad | `dog_squad` | Summon every wolf variant around you. |
| Out of Service | `jam_bp` | Gain Out of Service: right-click to disable enemy /bp for 3 minutes. 9 minute cooldown. |
| Gold Rush | `sand_gold` | Mining sand has a 3% chance to drop a gold nugget. |
| Eye Taxi | `eye_taxi` | Your thrown eyes of ender no longer shatter. |
| Flowers on the Way | `flowers_walk` | Flowers grow on grass blocks you walk over. |
| Gear Upgrade | `forge_master` | Replace the tools and weapons in your inventory with random tiers of the same type. |
| Whatever | `random_bingo` | Gives you a random bingo (completed ones included). |
| Tribute to Love | `love_tribute` | Teleport to the nearest shipwreck. |
| Nether Start | `ruined_portal` | Teleport to the nearest ruined portal. |
| Ancient Ruins | `ancient_ruins` | Randomly spawn 4-15 suspicious gravel or suspicious sand near you. |
| Sheep Sheep Sheep | `sheep_wool` | Killing a sheep additionally drops 2 wool of random colors. |
| Portable Workbench | `portable_bench` | Gain "Portable Workbench": right-click to open a 3x3 crafting grid without placing a crafting table. |
| Growth Spray | `bone_meal_spray` | Every 10 seconds, sprinkles bone meal on crops and plants within a 9 block radius, randomly growing up to 12 of them. |
| Village Hero | `village_hero` | Gain Hero of the Village I permanently. |
| Seed Pack | `seed_gift` | Gain 1-8 each of random wheat, carrot, potato, beetroot, melon, and pumpkin seeds. |
| Archaeology Brush | `archaeology_brush` | Gain a brush that never loses durability. |
| Jumping Tiger | `jump_tiger` | Gain Jump Boost II permanently. |
| Day-Night Flip | `day_night_flip` | Instantly flip day and night; time will not change naturally for the next 10 minutes. |
| Toolbox | `toolbox` | Gain 1 random crafting table, furnace, anvil, grindstone or stonecutter. |
| Fish God | `dolphin_dash` | Gain permanent Dolphin's Grace II while in water. |
| Portable Furnace | `portable_furnace` | Gain 'Portable Furnace': right-click to open a furnace screen anywhere. Smelting continues after closing the screen. Its smelting speed is affected by Smelting Speed. |
| Smelting Speed | `smelt_speed` | Nearby furnaces, blast furnaces and smokers smelt twice as fast. |
| Meteor | `meteor` | Call down 5-10 amethyst meteors of varying size around you (never more than 500 blocks away). Enemy players get Slowness III while the ground under them crumbles block by block like a real earthquake; you gain a map marking every impact point. |
| Quality Upgrade | `offer_upgrade` | Your next hex selection is guaranteed to contain at least 1 prismatic hex. Only offered during the first hex selection. |
| Blind | `blind` | Gain 'Blind': right-click to blind all enemies for 8 seconds. Cooldown 3 minutes. |
| Is It Over? | `is_it_over` | Teleport to the End outer islands for 10 minutes. You cannot be teleported during this time and return automatically when it ends. |
| Who Is the Pillager Now | `pillager_outpost` | Teleport to the nearest pillager outpost; if none is nearby, one is generated 200 blocks away in one of 8 directions. |
| The Cherry Blossoms of My Hometown Have Bloomed | `cherry_grove` | Teleport to the nearest cherry grove biome; if none is nearby, create one 500 blocks away. |
| Red Light, Green Light | `pale_garden_map` | Receive a map pointing to the nearest pale garden biome; if none is nearby, create one 500 blocks away and mark it. |
| Advance Victory | `advance_victory` | Instantly light up one cell, but it goes out after 5 minutes unless you genuinely complete it in time. |
| Steal the Sky | `cell_swap` | Swap ownership of one completed cell between you and an enemy team. |
| Journey to the Core | `journey_core` | Teleport to a cave at Y=-50. |
| Ascetic | `ascetic` | Clear all of your team's completed cells and immediately gain 2 random prismatic hexes. Only appears in the third hex selection. |

### Gold (45)

| Name | id | Effect |
|---|---|---|
| Immunity Shield | `immunity_shield` | Every 3 minutes, become immune to the next skill-type hex from an opponent. |
| Forge Master | `inventor` | Tools, weapons and armor you craft in the future gain random enchantments. |
| Random Teleport | `random_teleport` | Unlocks /bingo rtp: randomly teleport nearby with a 5s cooldown. |
| Cutting Edge Inventor | `cutting_edge` | All skill-type hex cooldowns are reduced by 30%. |
| I Want More | `want_more_items` | Gain 10 random items (same weighted pool as the Pet Master pet). |
| Greater Utility Scope | `scope_2` | Gain +2 block interaction range and +2 entity interaction range. |
| Prismatic Ascension | `prismatic_shift` | Gain a random prismatic hex. |
| Little Bees | `bees` | Summon 10 angry bees around every enemy team. Bees can sting through invulnerability. |
| Gluttonous Feast | `gluttonous_feast` | Permanently gain Hunger II. Eating any food grants a random level II buff for 15s. |
| Big Brain | `big_brain` | All your inventory items become random items for 5s, then are restored. |
| Flip the Table? | `flip_table_one` | Reroll one task that no team has completed yet. |
| Soul Bomb | `soul_bomb` | Gain 3 soul sand, 2 wither skeleton skulls and 1 flint and steel. |
| All Roads Lead to Rome | `village_road` | Teleport to the nearest village. |
| Travel Frog | `travel_frog` | Summon a frog; right-click it with half a stack of food for a random structure map (shipwreck / End ruin / woodland mansion / village / ancient city / pillager outpost / desert pyramid / jungle temple; in the Nether: bastion / fortress / ruined portal). |
| Amnesia | `amnesia` | Randomly revert one completed Bingo task of another team. |
| Ice! | `ice_boots` | Gain Frost Walker II diamond boots. |
| Snow Golem Society | `snow_pumpkin` | Put cursed carved pumpkins on all enemy players; they cannot be removed by yourself — a teammate must right-click you with shears. A solo player can sneak-right-click with shears. |
| Pokemon Master | `pokemon_master` | Gain 6 random spawn eggs (duplicates allowed). |
| Raven Mouth | `raven_mouth` | Summon a black parrot; it randomly reverts one of your completed tasks or completes one random task. |
| Trapped With Friends | `friends_here` | Each nearby teammate grants +2% speed and Haste I (max +10%). |
| Dechi Song | `dechi_song` | Plays the villager version of the Dechi song; grants random Luck III-V during it, and all pickaxes in your inventory gain Fortune II-III. |
| Utopia | `utopia` | Scrambles all your potion effects and grants 5 new permanent random buffs or debuffs. |
| Rush Rush Rush | `dash_charge` | Gain "Rush Rush Rush": right-click to dash forward for 8 seconds, steering with your view. Blocks in the way are broken and drop (except obsidian and bedrock), and creatures hit take 10 damage. 15 second cooldown after the dash ends. |
| Soul Out | `soul_out` | Immediately enter spectator mode for 30 seconds to scout freely, then return to the position where you entered spectator mode. |
| Eighty-One Trials | `eighty_one_trials` | Teleport to the nearest trial chambers. |
| Chaos Mind | `inventory_shuffle` | Right-click the skill item: randomly shuffle the inventory, offhand and armor slots of all enemy players (items may end up in equipment slots). 8 minute cooldown. |
| Spider Man | `spider_man` | Climb walls like a spider: stick to any wall your body touches, hold forward to climb, release to hang, sneak to cancel. Press V to toggle. |
| Magnet | `magnet` | All dropped items within 16 blocks fly toward you. Press T to toggle. |
| 3-Sided Die | `dice_roll` | Every 5 minutes roll a d6 (a dice animation plays before the result is revealed): above 3 grants Speed II for 4 minutes, below 3 grants Slowness I for 4 minutes, and 3 roots you for 6 seconds. |
| Louis XVI | `louis_xvi` | The first mob you kill is guaranteed to drop 1 of its heads. |
| Delivery Arrived | `airdrop` | Airdrop a chest above you filled with loot from a random vanilla chest loot table. |
| Sea God | `sea_god` | Gain an enchanted trident (Loyalty III, Channeling I, Mending I) that returns when thrown. |
| Totem Wholesale | `totem_bulk` | Gain 3 Totems of Undying. While holding one, you can take damage, so the 'Postmortal' advancement can be completed. |
| Ender Slayer | `ender_slayer` | Killing an Enderman has a 1% chance to drop an enchanted book: Silk Touch I, Mending I or Infinity I. |
| Ultimate Refresh | `ultimate_refresh` | Your next hex selection can be refreshed one extra time, and you immediately gain 2 loot chests. |
| Auto Smelt | `auto_smelt` | Mining ores automatically smelts the drops and grants extra experience. |
| Task Shield | `task_shield` | Lock your team's 3 highest-weight tasks so other hexes cannot push them back. |
| Midas Touch | `midas_touch` | Press G to turn your offhand item into the same amount of gold ingots (valuables excluded). Cooldown 1 minute. |
| Bat Form | `bat_form` | Gain 'Bat Form': right-click to become a bat for 60 seconds (flight at 35% reduced speed, cannot attack, bat-sized health and body). The bat follows at your feet; the form ends with 1 minute of Slow Falling. Cooldown 7 minutes. |
| Gaze Into the Abyss | `gaze_abyss` | Teleport to the nearest deep dark biome and find a safe spot inside its caves; if none is nearby, create an artificial deep dark cave 500 blocks away underground. |
| Wildcard | `wildcard` | Randomly light up one cell on the Bingo card (shared by all teams). |
| Destiny Dice | `destiny_dice` | Roll a die (revealed after an animation): complete a cell / remove an enemy cell / both complete a cell / reshuffle all cards. |
| Great Migration | `great_migration` | All players are randomly teleported to overworld positions within 1000 blocks of each other. |
| Dragon Tamer | `dragon_rider` | Gain a dragon egg; after placing it, it incubates for 3 minutes with a text-display countdown above it, and mining or moving it resets the timer. The hatched baby dragon circles you, breaks blocks it passes through with drops, knocks back creatures for 4 damage, and never attacks you or other players who own this hex. |
| Hatsune Miku | `mangrove_map` | Receive a map pointing to the nearest mangrove swamp biome; if none is nearby, create one 500 blocks away and mark it. |

### Prismatic (35)

| Name | id | Effect |
|---|---|---|
| Iron Chain | `chain_master` | Each completed team task gives a 1% + 0.1% per-completed-task chance (max 50%) to complete 1 adjacent task. |
| I Really Want It | `want_lots_items` | Gain 15 random items (same weighted pool as the Pet Master pet). |
| Greatest Utility Scope | `scope_3` | Gain +3 block interaction range and +3 entity interaction range. |
| Gold Rush | `gold_rush` | Attacking a mob grants 1-3 gold nuggets, 5s cooldown per mob. |
| Ascension Ritual | `ascension` | Killing a hostile mob grants a random buff. |
| Still Water Prison | `still_water` | All players can no longer receive any hex rewards. |
| Pandora's Box | `pandora` | Reroll all of your current hexes. |
| Goliath | `goliath` | Gain +30% size, max health, block break speed and attack damage. |
| You Cannot Touch Me | `untouchable` | Right-click the skill item: enemy Bingo teams cannot break blocks for 30s. 5 minute cooldown. |
| Glass Cannon Bag | `glass_cannon_bag` | The last 18 inventory slots are filled with barrier markers (items are moved out or dropped). Item drops are doubled. |
| Back to Basics | `back_to_basics` | You cannot use a crafting table, but item drops are doubled (boosted by Looting) and you gain permanent Speed I. |
| Archmage | `archmage` | After crafting, each material has a 10% chance to be refunded. |
| Pet Master | `pet_master` | Gain an invincible pet that gives you a random item from the full item pool every 8s (valuable items are rarer). |
| Crown Me King | `crown_me_king` | A beacon marks an enemy position. Reach it to gain a prismatic hex, a randomly enchanted diamond armor set, an Efficiency IV diamond pickaxe and a Sharpness IV / Fire Aspect I diamond sword. |
| Flip the Table! | `flip_table` | Reroll every task that no team has completed. |
| OK Boomerang | `boomerang_biome` | Gain OK Boomerang: throw it toward an unfinished biome task and it returns; if the biome is not found nearby, one is generated ahead. |
| Trapped With Me | `rainbow_duel` | Gain a bound chain. Right-click to enter a bedrock duel with the nearest enemy; PvP is allowed, whoever dies first loses, the winner leaves at once, and both leave after 5 minutes without PvP. 3 minute cooldown. |
| Fey Magic | `fae_magic` | Gain Fey Magic: right-click to turn the enemy team into critters for 1 minute. 5 minute cooldown. |
| Chinese Can Fly | `chinese_can_fly` | Gain an unbreakable elytra and one bound firework rocket that is always replenished. |
| Spy | `spy` | Use /bingo spy <teammate> <enemy> to swap their positions and inventories. Bound items stay with their owner. Once per game. |
| Common Wealth | `common_wealth` | Confiscate everyone's inventory items into a common pool, then redistribute them randomly. |
| Chaos Shift | `chaos_shift` | Gain two random hexes of random colors. |
| CV Engineer | `task_copy` | Randomly copies a task an opponent has completed but your team has not; in Lockout mode, directly claims that slot instead. |
| Dimensional Banish | `nether_banish` | Banish one player from each enemy team to a random place in the Nether for 1 minute. Cooldown: 8 minutes. |
| The World! | `the_world` | Gain the "The World" time-stop skill; right-click to stop time for 8 seconds so only you can move. Cooldown: 3 minutes. |
| Corrupt Backpacks | `corrupt_inventory` | For every team except yours, 5 random items of one random player turn into rotten flesh. |
| Black Market | `black_market` | The next time you open a villager trade screen, that villager reaches max level and you gain Hero of the Village II permanently. |
| Demon Pact | `demon_pact` | Permanently lose 10 max health, but you can take damage; gain a full set of randomly enchanted iron armor and immediately complete the highest-weight Bingo task. |
| Mind Control | `mind_control` | Gain 'Mind Control': right-click to control one enemy's movement direction for 15 seconds. Cooldown 5 minutes. |
| Objective Deny | `objective_deny` | Gain 'Objective Deny': right-click to open the current Bingo card and click a task; no team can complete it for 30 seconds. Cooldown 6 minutes. |
| X-Ray | `xray` | Gain 'Ore Vision': right-click for 20 seconds of ore x-ray. Cooldown 5 minutes. |
| Scavenger | `scavenger` | Breaking blocks has a 3% chance to drop the block itself as an extra drop. |
| Card Shuffle | `card_shuffle` | Randomly rearrange every team's card positions; completion and lock states follow their tasks. |
| Mirror Hex | `mirror_hex` | Gain 'Mirror Hex': right-click to open a list and copy one hex you do not own from the nearest other team. |
| Blank Cell | `blank_cell` | Gain 'Blank Cell': right-click to open the current Bingo card and click any incomplete task to light it up for your team only, keeping its original icon. One-time item. |

<!-- HEX-TABLE-END -->

## 素材状态

- `dechi_song.ogg` 为第三方录音，**不在本仓库许可范围内**，计划替换为自制音频；替换完成前请勿再分发该文件。
- `sounds/dragon_1.ogg` ~ `dragon_4.ogg` 来自 freesoundsite.com 的 Dragon Purring 免费音效合集，站点声明免费、无需署名、可商用；已转成单声道 64kbps，单文件约 15–18 KB。
- 海克斯名称与文案参考《英雄联盟》强化符文，仅作玩法致敬；《英雄联盟》为 Riot Games, Inc. 的商标，本项目与 Riot Games 无关。
- `textures/gui/dice_d4_*.png`（掷骰动画用三角骰子贴图）为本项目自绘，无第三方素材。
- 贴图与其余语言文件为本项目原创。

## 构建

需要 JDK 25、NeoForge 26.2.0.93 安装、官方 LibX 与已构建的 ReBongo jar：

```powershell
.\build.ps1 -Version 26.2-1.43.15 `
  -MinecraftDir "D:\minecraft\.minecraft" `
  -NeoForgeVersion "26.2-NeoForge_26.2.0.93" `
  -LibXJar "D:\minecraft\.minecraft\versions\26.2-NeoForge_26.2.0.93\mods\LibX-26.2-8.0.0.jar" `
  -BongoJar ".\..\rebongo\dist\ReBongo-26.2-1.0.0-port.9.jar"
```

产物：`dist/HexBingo-<version>.jar`。

## 版本规则

- 修 bug：第三级 +1（`1.33.1`、`1.33.2`…）
- 新增海克斯：第二级 +1（`1.35.0`）

## 许可与致谢

- 本模组：**Big_Jls** 设计与实现，**DeepSeek-v4.1-Flash-1017** 编码协助，Apache License 2.0。
- 依赖 **Bongo**：**noeppi_noeppi**，Apache License 2.0。
- 依赖 **LibX**：**ModdingX（MelanX、noeppi_noeppi）**，Apache License 2.0。

本项目为非官方附属，未使用上游图标，也不暗示任何官方关系（Apache License 2.0 第 6 条不授予商标权）。

## 更新日志

### 1.43.15 修复

- **驯龙高手**：孵化中的龙蛋自然掉落或被瞬移后不再被当成原版龙蛋——下落期间保持剩余计时，落地后自动在新位置继续孵化，找不到时宽限 10 秒才取消，不再出现「计时消失、无法孵化」。
- **精神控制**：目标不再限制在 30 格内——全服范围内选取最近的敌人（同维度优先，跨维度兜底）。

### 1.43.14 修复

- **空白格**：描述不再称为「专属本队的额外格子」——它就是打开当前 Bingo 卡牌、点亮任意未完成任务的一次性道具。

### 1.43.13 修复

- **间谍**：描述不再提及已移除的「彩虹大作战」绑定物品；绑定物品名单与残留的旧彩虹决斗语言键一并清理。

### 1.43.12 修复

- **命名/分档**：彩色「尖端发明家」与金色「尖端发明家」重名——彩色版改名**锻造大师**并降为金色；原银色「锻造大师」改名**装备升级**。分档变为 52 银 / 45 金 / 35 彩（总数仍 132）。
- **免疫护盾**：升为金色，抵挡后冷却从 60 秒改为 **3 分钟**。
- **陨石**：落点改以**施放者为中心**（最远不超过 500 格），不再按全服玩家平均位置。
- **ok回力镖**：飞行速度降低（1.5 → 1.0）。
- **透视**：矿石高亮改为在矿石位置放置**不会开口的发光隐身潜影贝**（只显示发光轮廓），不再用漂浮的方块掉落物。

### 1.43.11 修复

- **吸铁石**：新增 T 键开关吸物功能（开关时有提示，对局重置自动清理）。
- **掷骰动画**：3面骰与命运骰子先播放屏幕中央的三角骰子旋转动画，动画结束后才揭晓并结算结果（贴图为自绘，无第三方素材）。
- **我想要 / 我很想要 / 我真的很想要**：随机物品改为与仆从大师宠物同款加权随机池（贵重物品概率更低）。

### 1.43.10 修复

- **三选一队列**：掷不满 3 个海克斯时该档奖励放回队列，不再被吞掉。
- **颜色解析**：未知颜色记录告警日志，不再静默当银色。
- **星际航行**：平台改放到玩家当前高度上方 256 格（不超过世界高度上限），高山不再把平台埋进山里；判定以出发高度为基准。
- **Spider Man**：爬墙判定改用身体碰撞（与原版蜘蛛一致），贴到任意墙都能爬（栅栏、玻璃板、楼梯等也行），不再只认完整方块。

### 1.43.9 修复

- **`/bingo tp` 拦截**：按 ReBongo 实际的 `EntitySelector` 参数类型解析目标，选择器方式传送到被放逐玩家也能可靠拦截。
- **星际航行**：玩家在地狱时不刷出该海克斯（y=256 落点在地狱无意义）。
- **恶魔契约**：扣生命上限至少保留 2 点，与蝙蝠形态等效果叠加不会再扣成负值直接死亡。
- **玻璃大炮**：锁定槽标记不再被间谍交换、共同富裕洗走。
- **静水监狱**：被锁定的玩家不再收到自选/全选海克斯列表。
- **驯龙高手**：幼龙击杀生物的掉落物直接给主人。
- **超强大脑**：描述补充"期间新放进主物品栏的物品会被清除"。

### 1.43.8 修复

- **间谍**：交换后放不下的溢出物品掉落到双方当前脚下，而不是交换前的位置（跨维度交换也正确）。
- **小蜜蜂**：只有本模组召唤的蜜蜂能穿透被取消的伤害事件，野生蜜蜂不再强制造成伤害。
- **下线清理**：自选/镜像/任务选择模式在玩家下线时清除，重连后不再保留资格。
- **存档**：海克斯数据改用结构化 NBT 列表（format=2）存储，旧存档仍可读取。
- **偷天换日**：移除反射位掩码，改用 Team 公开 API（修复超过 25 格时位掩码回绕与静默失败）。
- **精怪魔法**：属性修饰符 ID 按目标独立，不同施法者的诅咒不再互相覆盖。

### 1.43.7 修复

- **跨维度实体引用**：26.2 跨维度传送会重建实体实例（旧实例被移除）。精怪魔法伪装、仆从宠物、蝙蝠形态现在都会用传送返回值更新地图引用并重设 `noPhysics`，不再出现「宠物被误判死亡永久消失 / 每次跨维度多一只无敌孤儿伪装」。
- **彩虹决斗**：6000 tick 超时不再与「是否发生 PVP」挂钩，双方停手也能正常结束决斗，不会永久困在基岩房。
- **随机传送 / 虫洞**：取地表高度前先加载候选区块，不再因未加载区块返回世界最低点而导致几乎必然传送失败。
- **驯龙高手**：主人离线时直接丢弃幼龙（不再随区块保存后复活成无敌孤儿）；服务器重启/区块重载残留的旧幼龙首次 tick 即移除。
- **状态清理**：放逐状态到期无条件清除（离线也清）并在对局重置时清空；`clearAll` / `clearNewStates` 补齐 RTP 冷却、间谍/镜像一次性标记、不可触碰/混沌心智/蜘蛛爬墙/放逐/The World 冷却、击杀叠层、信标、航程锁定、淘金热、蜜蜂目标等跨局残留清理。
- **全身附魔**：排除诅咒类附魔（`minecraft:curse` 标签）并校验 `canEnchant`，不再给装备套绑定/消失诅咒或无效附魔。
- **雪人南瓜**：重复施放不再用诅咒南瓜覆盖已备份的真实头盔（`putIfAbsent`）。
- **结构传送**：结构包围盒按目标结构标签过滤（不会再选中旁边的无关结构）；结构落点与结构地图取高度前先加载区块。
- **任务选择包**：服务端丢弃未经过界面下发的任务选择请求（客户端 index 不再可信）。

### 1.43.6 修复

- **驯龙高手**：幼龙跟随改为服务端每 tick 驱动。跨维度、远距离传送或所在区块卸载后都会恢复跟随并继续环绕飞行（26.2 跨维度传送会重建实体实例，旧引用失效的问题已修复；幼龙因区块卸载丢失时会在主人身边重新生成）。
- 幼龙现在完全无敌：取消一切来源的伤害，包括创造模式玩家的攻击、虚空等穿透性伤害。

### 1.43.5 修复

- **技能物品防存放**：目标封锁、致盲、蝙蝠形态、精神控制、矿物透视、镜像复制、空白格现在和 ok回力镖 一样，无法丢弃，也无法放入箱子、熔炉、背包等其它容器；放入后会被自动收回。

### 1.43.4 调整

- **结束了?**：放逐末地期间，被放逐的玩家无法被任何传送带走，其他玩家也无法通过 `/tp`、`/teleport`、`/bingo tp` 等传送到其位置；末影珍珠、紫颂果等自身位移仍可用。

### 1.43.3 调整

- **地狱开局（废弃地狱门）**：传送前在结构附近扫描黑曜石、哭泣的黑曜石、地狱岩、金块、岩浆块、地狱砖与石砖，锁定传送门本体后再找安全落脚点。
- **九九八十一难（试炼大厅）**：新增凝灰岩类方块、涂蜡铜块、铜格栅与铜灯扫描，先定位到大厅内部再落脚；找不到特征方块时回退到原有的凝灰岩/铜块地面搜索。

### 1.43.2 修复

- **物品任务「附魔书」**：任意附魔、任意附加组件的附魔书现在都能完成该任务。此前只忽略附魔组件，来自战利品或模组的附魔书若带有其它组件会无法计数。

### 1.43.1 修复

- **结构与群系定位传送**：`/locate` 返回的结构坐标 Y 恒为 0，之前直接按该 X/Z 取地表高度，导致地下结构（废弃地狱门、试炼大厅等）会把玩家传到结构正上方的地表。现在会反查结构实际包围盒中心，并在中心附近寻找安全落脚点；水下结构仍落到水面上方。受益海克斯：致敬爱情（沉船）、地狱开局（废弃地狱门）、谁才是掠夺者、条条大路通罗马（村庄）、九九八十一难（试炼大厅）。
- **凝视深渊**：深暗之域搜索现在会校验落脚点确实位于 `deep_dark` 群系，并扫描整条垂直柱，不再因为先遇到地表空气而把玩家放在群系正上方。

### 1.43.0 新增

- 新增金色 **初音未来**（`mangrove_map`）：获得一张指向最近红树林群系的地图；附近没有时在 500 格外改造一片红树林并标记。

### 1.42.10 修复

- **驯龙高手**：服务器重启后，脱离宠物表的旧幼龙会自动关闭 AI 并清空目标，避免它恢复成敌对幻翼攻击玩家。

### 1.42.9 调整

- **驯龙高手**：幼龙保留幻翼原版 AI 的飞行与转向，但孵化时清空全部 AI 目标（goalSelector / targetSelector），不会再锁定或攻击玩家。
- 每 tick 把环绕点写入幻翼的 `moveTargetPoint`，由原版 `PhantomMoveControl` 平滑飞行，不再用瞬移方式摆放位置。
- 幼龙不再对任何玩家造成伤害（之前只保护自己、队友和同样拥有该海克斯的玩家），仍会破坏沿途方块、击退并伤害非玩家生物。
- 玩家距离超过 32 格（传送、换维度）时，幼龙会被拉回环绕点继续跟随。

### 1.42.8 修复

- 清理上次从 `bingo` 改名到 `bongo` 时残留的任务类型 id 判断：
  - `"bingo.item"` → 直接比较 `TaskTypeItem.INSTANCE`（影响手持物品移速、物品任务取物品）；
  - `"bingo.empty"` → 直接比较 `TaskTypeEmpty.INSTANCE`（影响万能牌选格、恶魔契约选最高权重任务）。
  之前这些判断永远不成立，可能导致手持物品加速不触发、万能牌/恶魔契约选中空格或空任务。
- 已全量搜索源码中的 `"bingo."` 字符串，剩余只有自定义语言键 `bingo.task.enchantment.name`（中英文件均有，功能正常）和 `/bingo` 命令字面量，不再有任务类型 id 残留。

### 1.42.7 修复

- **驯龙高手**：幼龙改用幻翼实体。原版末影龙的 `sanitizeScale()` 会强制保持 1.0 大小，且自带 BOSS 飞行阶段逻辑，无法作为可缩放的普通宠物；幻翼支持缩放，且 NoAI 下可以被稳定移动。
- 幼龙的环绕移动改到实体 tick 之后执行，修复服务端位置更新早于实体跟踪、客户端看起来不跟随/不转圈的问题。
- 幼龙随机叫声音量减半（0.7 → 0.35），孵化时的龙吼也从 1.0 降到 0.5。
- 幼龙体型再缩小：使用幻翼最小体型（size 0）并额外缩放到 0.5。

### 1.42.6 修复

- **ok回力镖**：修复群系任务类型判断用错命名空间（写成 `bingo.biome`，实际是 `bongo.biome`）导致明明有未完成群系任务却提示“没有找到未完成的群系任务”的问题；现在直接按 `TaskTypeBiome` 判断。

### 1.42.5 调整

- **ok回力镖**（原「回力ok镖」）：已改名。搜索未完成群系任务时半径扩大到 12800；仍然找不到目标群系时，会在玩家前方约 500 格用 `/fillbiome` 生成一处目标群系，并把回力镖指向那里。

### 1.42.4 调整

- **目标封锁**：冷却时间由 15 分钟缩短为 6 分钟。

### 1.42.3 修复

- **驯龙高手**：龙蛋被活塞推动或原版随机瞬移后，会在新位置重新计时并重新显示倒计时；倒计时文字改用 NBT 初始化（始终面向玩家、可穿透方块、视距提升），各个角度都能看清。
- **空白格**：点亮任务时不再把该格替换成万能牌图标，保留原任务图标，只为本队点亮。

### 1.42.2 修复

- **蝙蝠形态**：飞行速度降低 35%，跟随的生物蝙蝠不会再被甩开。

### 1.42.1 修复

- **蝙蝠形态**：形态结束时额外获得 1 分钟缓降。
- **驯龙高手**：修复同一坐标重新放蛋时误删新蛋导致无法孵化、跨维度孵化时龙蛋不消失的问题；孵化成功会播放龙吼并向自己和队友提示。
- **苦行者**：修复「海克斯一览」中展示其赠送海克斯时显示 `hexbingo.hex.prefix.ascetic` 原始键名、看不到海克斯名字的问题。
- **空白格 / 目标封锁**：复查了掀桌子、洗牌、万能牌等会改动卡面的海克斯；选择界面始终读取当前卡牌与最新任务列表。

### 1.42.0 更新摘要

- **便携熔炉**：关闭界面后仍会继续烧制，不再只在打开熔炉时工作。
- **蝙蝠形态**：跟随的蝙蝠移到玩家脚边并缩小，第一人称视角不再糊在镜头前。
- **驯龙高手**：龙蛋上方的孵化倒计时改用文字展示实体（TextDisplay），区块重载后会重新生成，不再是隐形的盔甲架。
- **成就任务**：海克斯发放的道具（鞘翅、龙蛋、三叉戟等）触发的原版成就仍会正常显示，但不再点亮 Bingo 的成就类任务，并会提示「海克斯道具获得的成就不计入 Bingo 任务」。
- **空白格 / 目标封锁**：选择界面改为与 Bingo 大地图（快捷键 Y）一致的大卡牌，直接点击对应格子选择任务；修复空白格读取任务不正确、容易点到其它任务的问题。

### 1.41.1 修复

- **驯龙高手**：龙蛋放下后需要孵化 3 分钟，蛋上方显示倒计时；挖掉、被活塞推动或瞬移走都会重新计时。孵化出的幼龙不会攻击自己或其它拥有该海克斯的玩家，并加入 4 种随机幼龙叫声。

### 1.41.0 更新摘要

- **万能牌**从彩色调整为金色。
- 新增银色 **预支胜利**、**偷天换日**、**地心游记**、**苦行者**；苦行者只在第三次海克斯选择中出现，不会出现在任何质变/复制来源。
- 新增金色 **命运骰子**、**随机大迁徙**、**驯龙高手**。
- 新增彩色 **空白格**：专属本队的万能格，可自选任意未完成任务点亮。

### 1.40.0 更新摘要

- **群系兜底**：樱花群系、苍白花园、深暗之域的搜索半径从 6400 扩大到 12800；仍然找不到时，会在玩家 500 格外选一块合适位置，用 `/fillbiome` 改造成目标群系并补种对应植被（深暗为地下洞穴 + 幽匿块），保证海克斯一定能把玩家带到目标群系。

### 1.39.0 更新摘要

- 新增银色 **谁才是掠夺者**（`pillager_outpost`）：传送到最近的掠夺者前哨站；附近没有时在 8 个方位 200 格外寻找陆地进行生成。
- 新增银色 **故乡的樱花开了**（`cherry_grove`）：传送到最近的樱花群系。
- 新增银色 **一二三木头人**（`pale_garden_map`）：获得一张指向最近苍白花园群系的地图。
- **仆从大师**：`/kill` 现在可以正常移除无敌小宠物，且不会自动补发；创造模式等绕过无敌的伤害仍会被拦截。
- **陨石**：地震改为随机逐格崩塌，不再瞬间清空整个区域。

### 1.38.1 修复

- **仆从大师**：无敌小宠物现在免疫所有伤害来源，包括创造模式玩家和可绕过原版无敌的伤害；每 tick 兜底维持无敌并回满生命。`/kill` 仍可正常移除，且不会被自动补发（1.39.0 起）。

### 1.38.0 更新摘要

- 新增金色 **尖端发明家**（`cutting_edge`）：当前玩家所有技能型海克斯的冷却时间缩短 30%。
- **蝙蝠形态**：改为有 AI、无敌、有声的真蝙蝠每 tick 跟随（含跨维度）；本体隐形并缩小到 0.5 格高。

### 1.37.2 变更

- **蝙蝠形态**：参照精怪魔法改成真正的蝙蝠——本体隐形，生成一只无 AI、无敌、无声的真蝙蝠每 tick 跟随玩家（含跨维度），形态结束/死亡/下线/对局重置时一并清除。

### 1.37.1 修复

- **致盲**：施放时播放鱿鱼喷墨音效。
- **结束了?**：修复提示「无法打开末地维度」。末地外岛区块未加载时取高度会得到世界最低点，导致找不到落点；现在先加载区块再取高度，找不到落点时提示改为单独的「没有找到合适的末地外岛落点」。
- **便携熔炉**：烧制逻辑此前每秒才执行一次，导致烧制速度慢 20 倍、燃烧进度条也异常；现在改为每 tick 执行，与普通熔炉一致。
- **烧制加速**：额外熔炉 tick 此前每秒只补一次（实际约 +5%），现在每 tick 补一次（真正的 2 倍），便携熔炉同样享受 2 倍烧制。

### 1.37.0 更新摘要

- **陨石**：落点改为真正的地表，不再砸进基岩层（此前对未加载区块取高度会得到世界最低点）。
- **冲冲冲**：右键后自动向前冲刺（无需按前进键），只用视线控制方向；沿途破坏 3×3 范围（黑曜石、基岩除外）。
- **镜像复制**：改为一次性道具，选择列表弹出即消耗，使用后消失且不再自动补发。
- 配套 BingoLobby `26.2-1.0.0-port.2`：胜负分出后不再自动执行 `bingo stop` 结束当场对局。

### 1.36.1 修复

- 需要 ReBongo `26.2-1.0.0-port.4` 或更高。该版本补上了命令参数类型注册，修复进入世界/服务器时因 `Unrecognized argument type: GameTasksArgument` 被踢出并提示「无效的玩家数据」的问题。

### 1.36.0 更新摘要

- 新增金色 **凝视深渊**（`gaze_abyss`）：传送到最近的深暗之域（deep_dark）群系，并自动寻找洞穴中的安全落脚点。
- **结构传送修复**：村庄、沉船、废弃地狱门、试炼大厅现在固定在世界对应的维度中搜索/生成，站在地狱或末地使用也能正常传送；地狱中的废弃地狱门兜底生成改为 `ruined_portal_nether`；水下找到的沉船/海底废弃门会传送到水面上方而不是重新生成；旅行青蛙的兜底结构也会生成在地图对应的维度。

### 1.35.5 修复

- **精怪魔法**：跨维度传送时把伪装一起带过去，不再在旧维度遗留无 AI 小动物；重置/结束对局时会清理已遗留的伪装生物。

### 1.35.4 修复

- **仆从大师**：玩家传送（含跨维度）后不再遗留旧宠物并额外生成第二只；现有宠物会被一起带过去，召唤/重置/清理时也会移除重复或过期宠物。

### 1.35.3 变更

- 注释掉银色海克斯 **孤勇者**（`lone_hero`），并从卡池与音效表中移除；同时删除第三方音频 `guyongzhe.ogg`。
- 现有存档中已获得的孤勇者不再生效（海克斯 id 不再注册）。

### 1.35.2 修复

- **结束了?**：不再拦截紫颂果传送，珍珠与紫颂果都保留正常位移。
- **结束了?**：`/bingo tp`、`/bingo teleport`、`/tp`、`/teleport`、`/spreadplayers` 仍会被拦截，`/execute ... run` 包裹的写法也会被识别。

### 1.35.1 修复

- **结束了?**：不再拦截末影珍珠传送，珍珠仍然可以正常位移。

### 1.35.0 更新摘要

- 新增银色：**陨石**、**品质提升**、**致盲**、**结束了?**。
- 新增金色：**自动熔炼**、**任务护盾**、**点石成金**、**蝙蝠形态**。
- 新增彩色：**精神控制**、**目标封锁**、**透视**、**拾荒者**、**洗牌**、**镜像复制**。
- **叠角龙**：K 键名单显示当前层数；开箱时增加不死图腾粒子与经验「叮」声。
- **/bingo bp**：队伍背包从 27 格扩大到 54 格（需 ReBongo `26.2-1.0.0-port.3` 或更高）。
- **品质提升**只会在第一次海克斯选择中出现，不会进入质变与自由选择列表。

### 1.34.0 更新摘要

- 新增 **鱼神**（银色）：在水中获得永久海豚的恩惠 II。
- 新增 **便携熔炉**（银色）：右键随时打开熔炉界面，熔炼速度受「烧制加速」影响。
- 新增 **烧制加速**（银色）：附近的熔炉、高炉、烟熏炉烧制速度翻倍。

### 1.33.0 更新摘要

- 新增 **末影杀手**（金色）：击杀末影人 1% 掉落附魔书（精准采集 I / 经验修补 I / 无限 I）。
- 新增 **终极刷新**（金色）：下次选择可额外刷新一次，并立即获得 2 个原版战利品箱。
- **恶魔契约**：生命上限惩罚 15 → 10，新增整套随机附魔铁套，改为完成当前权重最高的未完成任务。
- **冲冲冲**：改为持续 8 秒的可转向冲刺，撞生物造成 10 点伤害，结束后冷却 15 秒。
- **修复**：`mayhem` 设置 id 统一为 `bongo:mayhem`。

