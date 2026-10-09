# ALPHA – Compose Tetris tesztelési projekt

A csapat tesztterve szerinti **statikus kódelemzés és funkcionális tesztelés**, kiegészítő JUnit-ellenőrzésekkel. Állapot: 2026-10-09, helyi munkaváltozat; commit/push nincs.

| Anyag | Állapot |
|---|---|
| [Tesztterv](docs/Compose_Tetris_Tesztterv.md) | 1.3-tervezet, eredeti megközelítés megtartva |
| [Kiegészítő munkanyag / MI-nyilatkozat](docs/Compose_Tetris_Kiegeszitesek.md) | Eredeti katalógus és új közreműködés |
| [Követelmények](docs/Compose_Tetris_Kovetelmenyek.md) | Oktatói kritériumok és hiányok |
| [Köztes riport](docs/Compose_Tetris_Koztes_Riport.md) | Tervezet; tényleges órák, Androidos futás, issue és review hiányzik |
| [Statikus riport](docs/Compose_Tetris_Statikus_Elemzes.md) | Lint 23 / Detekt 101 jelzés; első részletes triázs |
| [50 funkcionális eset](docs/Compose_Tetris_Funkcionalis_Tesztek.md) | Részletes specifikáció, csapat-orákulum és fixture-próba szükséges |
| [Végrehajtási jegyzőkönyv](docs/Compose_Tetris_Funkcionalis_Jegyzokonyv.md) | 0/50 Androidos végrehajtás |
| [JUnit-csomag](docs/Compose_Tetris_Egysegtesztek.md) | 87/87 futás sikeres, ebből 86 projektellenőrzés |
| [Környezet és APK](docs/Compose_Tetris_Kornyezet.md) | Build sikeres, eszköz még nincs |
| [Dokumentumreview](docs/Compose_Tetris_Dokumentumfelulvizsgalat.md) | Emberi ellenőrzésre előkészítve |
| [Git/GitLab átadás és 7 issue-szöveg](docs/Compose_Tetris_GitLab_Teendok.md) | A csapat végzi a Git-műveleteket és issue-kat |

## Forrás, futtatás és bizonyíték

[Teljes alkalmazás](software/compose-tetris/), [eredeti README](software/compose-tetris/README.md), [eredeti licence](software/compose-tetris/LICENSE), [tesztforrás](software/compose-tetris/app/src/test/java/com/jetgame/tetris/logic/).

[Gépi eredményösszesítő](reports/run-summary.json), [JUnit HTML](reports/unit/html/index.html), [JUnit XML](reports/unit/xml/), [Lint/Detekt](reports/static/), [környezeti naplók](reports/environment/). GitLabon a HTML-forrás megjelenítése nem feltétlenül rendereli a jelentést; helyben böngészőben nyitható meg.

```powershell
.\tools\Verify-Mk2.ps1 -JavaHome 'JDK17 mappa' -AndroidSdk 'SDK mappa' -DetektJar 'detekt-cli-1.23.8-all.jar' -Python 'python'
```

JDK 17, SDK platform 32/build-tools 30.0.3 és Python 3 szükséges. A wrapper Gradle 7.3-rc-1-et tölt le; a projekt rögzíti az AGP/Kotlin/Compose és tesztfüggőségeket. A helyi `.tools/` segédkörnyezet, build és APK nem verziókövetett; a nyers `reports/` eredményeket meg kell őrizni. A futtató nem commitol, pushol, hoz létre issue-t vagy mér lefedettséget.

M2 leadás a hétfői csoporthoz: **2026-11-01 vasárnap 08:00**, bemutató november 2. Eltérő CooSpace-közlés esetén az az irányadó. Az M2 belső 25 és végső 50 Androidos eset, illetve 10 súlyos statikus szabálysértés még nem teljesült.
