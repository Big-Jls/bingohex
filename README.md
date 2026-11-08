# Bingo: Hex Mayhem — 非官方 Bingo 海克斯附属

> **非官方项目**：这是自写附属模组，不是 Bongo / BingoLobby 的官方项目，与 **noeppi_noeppi**、**ModdingX** 没有隶属关系，也未获其背书或赞助。
>
> **许可**：Apache License 2.0。源码即本仓库。音频素材存在第三方版权待处理项，见下方「素材状态」。

## 简介

为 ReBongo（Bongo 的 NeoForge 26.2 移植版）增加 **100 个海克斯强化**：40 银色 / 30 金色 / 30 彩色。包含三选一选择界面、技能物品、被动效果，以及任务类与战斗类强化。

## 环境要求

| 依赖 | 版本 |
|---|---|
| Minecraft | 26.2 |
| NeoForge | 26.2.0.93 或更高 |
| Java | 25 |
| ReBongo（`modId = bongo`） | 26.2-1.0.0-port.1 或更高 |
| LibX | 26.2-8.0.0（官方版） |

ReBongo：https://github.com/Big-Jls/rebongo

## 安装

1. 安装 NeoForge 26.2.0.93+ 与官方 LibX 26.2-8.0.0。
2. 安装 ReBongo（`Bingo-26.2-1.0.0-port.1.jar`）。
3. 把 `HexBingo-26.2-1.32.20.jar` 放入客户端与服务端的 `mods`。
4. 开一局 Bingo，按提示选择海克斯。

## 内容与按键

- 100 个海克斯：40 银 / 30 金 / 30 彩。
- 默认 **K**：打开海克斯一览。
- 默认 **V**：切换蜘蛛类效果（获得对应海克斯后可用）。
- 部分海克斯会发放右键使用的技能物品（例如「混沌智」「不可触碰」），有独立冷却与丢弃/收纳保护。
- 命令：
  - `/hex hexes` — 查看自己的海克斯
  - `/hex rtp` — 随机传送
  - `/hex hook` — 钩爪
  - `/hex boomerang` — 回旋镖
  - `/hex trap` — 陷阱
  - `/spy <teammate> <target>` — 间谍交换
  - `/hexall` — 管理员批量授予（需要 GM 权限）

## 素材状态

- `dechi_song.ogg`、`guyongzhe.ogg` 为第三方录音，**不在本仓库许可范围内**，正在替换为自制音频；在替换完成前请勿再分发这两个文件。
- 海克斯名称与文案参考《英雄联盟》强化符文，仅作玩法致敬；《英雄联盟》为 Riot Games, Inc. 的商标，本项目与 Riot Games 无关。
- 贴图与语言文件为本项目原创。

## 构建

需要 JDK 25、NeoForge 26.2.0.93 安装、官方 LibX 与已构建的 ReBongo jar：

```powershell
.\build.ps1 -Version 26.2-1.32.20 `
  -MinecraftDir "D:\minecraft\.minecraft" `
  -NeoForgeVersion "26.2-NeoForge_26.2.0.93" `
  -LibXJar "D:\minecraft\.minecraft\mods\LibX-26.2-8.0.0.jar" `
  -BongoJar ".\..\rebongo\dist\Bingo-26.2-1.0.0-port.1.jar"
```

产物：`dist/HexBingo-<version>.jar`。

## 版本规则

- 修 bug：第三级 +1（`1.32.21`、`1.32.22`…）
- 新增海克斯：第二级 +1（`1.33.0`）

## 许可与致谢

- 本模组：**Big_Jls** 设计与实现，**DeepSeek-v4.1-Flash-1017** 编码协助，Apache License 2.0。
- 依赖 **Bongo**：**noeppi_noeppi**，Apache License 2.0。
- 依赖 **LibX**：**ModdingX（MelanX、noeppi_noeppi）**，Apache License 2.0。

本项目为非官方附属，未使用上游图标，也不暗示任何官方关系（Apache License 2.0 第 6 条不授予商标权）。
