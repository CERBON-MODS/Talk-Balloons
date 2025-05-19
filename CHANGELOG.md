# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.2.0] - 2025-05-20
- Add padding option to Talk Balloons ([#8](https://github.com/CERBON-MODS/Talk-Balloons/issues/8))
- Add per-player customizable Talk Balloons ([#7](https://github.com/CERBON-MODS/Talk-Balloons/issues/7))
- Added balloon styles (circular, rounded, and squared)
  - **BREAKING!** The old `balloons.png` texture has been replaced with `rounded.png`, please update your resource packs
    to fit in with this change! 
- Add ability to have only balloons appear instead of with chat ([#7](https://github.com/CERBON-MODS/Talk-Balloons/issues/7))
- Add an API for adding custom balloons
  - **BREAKING!** This renames `IAbstractClientPlayer` to `ITalkBalloonsPlayer`, which will break other mods.
    Please use the API for guaranteed stability.
- Added dependency on [ModernNetworking](https://modrinth.com/plugin/modernnetworking)
- Migrated codebase to Stonecutter for improved multiversion.