# Compose Tetris – Funkcionális tesztesetek és végrehajtási jegyzőkönyv

## Dokumentumadatok

**Dokumentumazonosító:** ALPHA-TETRIS-IN1091LA8-TC  
**Cím:** Compose Tetris – 50 funkcionális tesztesetet tartalmazó tesztspecifikáció és jegyzőkönyv  
**Dátum:** 2026.10.09.  
**Verzió:** 1.0-tervezet  
**Készítők:** ALPHA csapat  
**Kapcsolódó dokumentumok:** [Tesztterv](Compose_Tetris_Tesztterv.md), [Kiegészítések](Compose_Tetris_Kiegeszitesek.md) (A. melléklet: F-001 – F-050 tesztötletek)

| Verzió | Dátum | Leírás |
|---|---|---|
| 1.0-tervezet | 2026.10.09. | Az A. mellékletben szereplő 50 tesztötlet részletes kidolgozása és a JUnit-tesztekhez rendelése. |

## A dokumentum célja és használata

Az A. melléklet 50 tesztötletét esetenként kidolgozza: előfeltétellel, bemenettel és lépésekkel, elvárt eredménnyel. Minden esethez tartozik egy **Eredmény** és egy **Megjegyzés** oszlop, amelyet a végrehajtáskor kell kitölteni (`Sikeres`, `Sikertelen`, `Blokkolt`, `Nem futtatott`).

**Elvárt működés forrása:** a projekt README-je és az alkalmazás forráskódja. Ahol a kód viselkedése eltérhet a README-től, azt a megjegyzés külön jelzi, és a végrehajtás előtt a csapatnak kell eldöntenie, mi az elvárt működés.

**Két végrehajtási mód van:**

- **Automatizált (A):** a logika JUnit-tesztje IntelliJ-ben fut (`app/src/test/java/com/jetgame/tetris/`), emulátor nélkül. Az **Egységteszt** oszlop megadja a teszt nevét.
- **Kézi (K):** az alkalmazást emulátoron vagy telefonon kell végigkattintani. Ez minden esetnél kötelező, mert a funkcionális teszt a futó alkalmazást vizsgálja; az egységteszt csak kiegészíti.

**Gombok az alkalmazásban:** `START/RESET`, `PAUSE/RESUME`, `SOUNDS`, irányok `▲ ◀ ▶ ▼`, `ROTATE`. A `▲` gomb az ejtés (Drop).

**Technikák rövidítése:** EP – ekvivalenciaparticionálás, HÉ – határérték-elemzés, ÁT – állapotátmenet-tesztelés, TA – tapasztalatalapú tesztelés, UC – use case.

**Környezet (minden esetre):** elsődleges emulátor Pixel 2, API 35, álló nézet; másodlagos emulátor API 21. A riportban a commit, az APK azonosítója és a tényleges környezet szerepel.

## Tesztesetek

### Indítás és állapotkezelés

| ID | Tech. | Előfeltétel | Lépések | Elvárt eredmény | Egységteszt | Eredmény / Megj. |
|---|---|---|---|---|---|---|
| F-001 | UC | Az APK telepítve van tiszta emulátoron. | 1. Telepítsd az APK-t. 2. Indítsd el az alkalmazást. | Az alkalmazás összeomlás nélkül elindul; a kezdőképernyő ("TETRIS" felirat) és az összes gomb megjelenik. | – (K) | |
| F-002 | ÁT | Az alkalmazás a kezdőképernyőn áll. | Nyomd meg a `START/RESET` gombot. | Új játék indul, megjelenik az első alakzat. | `GameViewModelTest.f002_resetFromOnboardStartsNewGame` (A) | |
| F-003 | EP | Új játék indult. | Olvasd le a Score, Lines és Level kijelzőt. | Score: 0, Lines: 0, Level: 1. | `ViewStateTest.f003_defaultStateIsAFreshGame`, `GameViewModelTest.f002_…` (A) | |
| F-004 | ÁT | Futó játék. | Nyomd meg a `PAUSE/RESUME` gombot. | Az esés megáll, a szünet ikon aktív. | `GameViewModelTest.f004_pauseStopsRunningGame`, `ViewStateTest.f004_statusFlagsMatchGameStatus` (A) | |
| F-005 | ÁT | A játék szünetel, a pályán vannak elemek és pont. | Nyomd meg újra a `PAUSE/RESUME` gombot. | A pálya és a pontszám változatlan, az esés folytatódik. | `GameViewModelTest.f005_resumeKeepsBoardAndScore` (A) | |
| F-006 | ÁT | A játék szünetel. | Nyomd meg sorban: ◀, ▶, ▼, ▲, `ROTATE`. | A pálya és az aktív alakzat nem változik. | `GameViewModelTest.f006_controlsAreIgnoredWhilePaused` (A) | |
| F-007 | ÁT | Futó játék, a pontszám nem nulla. | Nyomd meg a `START/RESET` gombot. | A képernyőtörlés animációja lefut, utána a kezdőállapot jelenik meg; régi pont és sor nem marad. | – (K, animáció) | |
| F-008 | ÁT | Szüneteltetett játék, a pontszám nem nulla. | Nyomd meg a `START/RESET` gombot. | A képernyőtörlés után kezdőállapot jelenik meg, a pont és a sor törlődik. | – (K, animáció) | |

### Mozgatás

| ID | Tech. | Előfeltétel | Lépések | Elvárt eredmény | Egységteszt | Eredmény / Megj. |
|---|---|---|---|---|---|---|
| F-009 | EP | Futó játék, az alakzat a pálya közepén, bal oldalon szabad hely. | Nyomd meg a ◀ gombot egyszer. | Az alakzat egy oszloppal balra mozdul. | `SpiritTest.f009_moveLeftShiftsOneColumn` (A) | |
| F-010 | EP | Mint F-009, jobb oldalon szabad hely. | Nyomd meg a ▶ gombot egyszer. | Az alakzat egy oszloppal jobbra mozdul. | `SpiritTest.f010_moveRightShiftsOneColumn` (A) | |
| F-011 | EP | Futó játék, alatta szabad hely. | Nyomd meg a ▼ gombot egyszer. | Az alakzat egy sorral lejjebb mozdul. | `SpiritTest.f011_moveDownShiftsOneRow` (A) | |
| F-012 | HÉ | Az alakzat a bal falnál áll. | Nyomd meg a ◀ gombot többször. | Az alakzat nem lép ki a pályáról. | `SpiritTest.f012_leftWallBlocksMovement` (A) | |
| F-013 | HÉ | Az alakzat a jobb falnál áll. | Nyomd meg a ▶ gombot többször. | Az alakzat nem lép ki a pályáról. | `SpiritTest.f013_rightWallBlocksMovement` (A) | |
| F-014 | EP | Az alakzat mellett rögzített elem van. | Próbálj az elem irányába lépni (◀ vagy ▶). | Nincs átfedés, a tiltott mozgás nem történik meg. | `SpiritTest.f014_lockedBrickBlocksSidewaysMovement` (A) | |
| F-015 | HÉ | Az alakzat a pálya aljához közel esik. | Nyomd meg a ▼ gombot az alsó sorig, majd még egyszer. | Az alakzat nem jut a pálya alá, a következő ticknél rögzül. | `SpiritTest.f015_bottomOfBoardBlocksMovement` (A) | |
| F-016 | TA | Sortörlési animáció fut (előbb ki kell rakni egy teljes sort). | Az animáció alatt gyorsan nyomkodd a ◀, ▶, ▼, `ROTATE` gombokat. | Az animáció és a sorok elszámolása nem sérül, nincs átfedés vagy összeomlás. | `GameViewModelTest.f006_controlsAreIgnoredWhilePaused` (részben, A) | |

### Forgatás

| ID | Tech. | Előfeltétel | Lépések | Elvárt eredmény | Egységteszt | Eredmény / Megj. |
|---|---|---|---|---|---|---|
| F-017 | EP | T alakzat szabad területen. | Nyomd meg a `ROTATE` gombot egyszer. | Az alakzat szabályosan 90 fokkal elfordul. | `SpiritTest.f017_rotateTShapeInFreeSpace` (A) | |
| F-018 | EP | Alakzat szabad területen. | Nyomd meg a `ROTATE` gombot négyszer. | Az eredeti orientáció és pozíció áll vissza. | `SpiritTest.f018_fourRotationsRestoreOriginalCells` (A) | |
| F-019 | HÉ | Függőleges I alakzat a bal fal mellett. | Nyomd meg a `ROTATE` gombot. | Az eredmény érvényes, pályán belüli pozíció, vagy a művelet elutasított. | `SpiritTest.f019_rotateAtLeftWallStaysInsideBoard` (A) | |
| F-020 | HÉ | Függőleges I alakzat a jobb fal mellett. | Nyomd meg a `ROTATE` gombot. | Az eredmény érvényes, pályán belüli pozíció, vagy a művelet elutasított. | `SpiritTest.f020_rotateAtRightWallStaysInsideBoard` (A) | |
| F-021 | HÉ | Alakzat a pálya alján. | Nyomd meg a `ROTATE` gombot. | Az alakzat nem kerül a pályán kívülre. | `SpiritTest.f021_rotateAtBottomStaysInsideBoard` (A) | |
| F-022 | EP | Az alakzat mellett rögzített elemek vannak. | Nyomd meg a `ROTATE` gombot úgy, hogy a forgatás átfedést okozna. | Átfedést okozó forgatás nem alkalmazható. | `SpiritTest.f022_rotationOverlappingBrickIsInvalid` (A) | |
| F-023 | EP | Futó játék, mind a hét alakzat megjelenik (Z, S, I, T, O, L, J). | Minden alakzatot forgass el egyszer. | Minden alakzat négy cellából áll, forgatás után is érvényes. | `SpiritTest.f023_allSevenShapesKeepFourCellsAfterRotation` (A) | |

### Ejtés és esés

| ID | Tech. | Előfeltétel | Lépések | Elvárt eredmény | Egységteszt | Eredmény / Megj. |
|---|---|---|---|---|---|---|
| F-024 | EP | Üres pálya, futó játék. | Nyomd meg a ▲ gombot. | Az alakzat a legalacsonyabb ütközésmentes helyre kerül. | `GameViewModelTest.f024_f026_dropLandsOnFloorAndOnStack` (A) | |
| F-025 | EP | A pályán rögzített alakzat van. | Mozgasd az új alakzatot fölé, nyomd meg a ▲ gombot. | Az alakzat a meglévő elemek fölött áll meg. | `GameViewModelTest.f024_f026_dropLandsOnFloorAndOnStack` (A) | |
| F-026 | TA | Az alakzat éppen leért az ejtés után. | Nyomd meg többször gyorsan a ▲ gombot a következő tick előtt. | Az alakzat nem mozdul érvénytelen helyre, és nem kap többször lerakási pontot. | `GameViewModelTest.f024_f026_dropLandsOnFloorAndOnStack` (A) | |
| F-027 | EP | Futó játék. | Ne nyomj semmit, figyeld az alakzatot. | Az alakzat lefelé halad, majd rögzül. | `GameViewModelTest.f027_f035_tickMovesDownThenLocksForTwelvePoints` (A) | |
| F-028 | UC | Futó játék. | Jegyezd fel a "Next" előnézetet, várd meg a következő alakzatot. | A következő alakzat típusa egyezik az előnézettel; utána az előnézet frissül. | `SpiritTest.f028_generatedBagContainsEverySevenShapes`, `ViewStateTest.f028_spiritNextIsFirstOfReserve` (A) | |

### Sortörlés, pontozás, szintek

| ID | Tech. | Előfeltétel | Lépések | Elvárt eredmény | Egységteszt | Eredmény / Megj. |
|---|---|---|---|---|---|---|
| F-029 | HÉ | Egy sor egy cella híján kész, Score ismert. | Rakj le egy alakzatot, amely befejezi a sort. | Egy sor eltűnik; Lines +1, Score +100 +12. | `LineClearTest.f029_oneCompleteRowIsCleared`, `GameViewModelTest.f029_singleLineClearViaTickGivesHundredTwelvePoints` (A) | |
| F-030 | HÉ | Két sor egy-egy cella híján kész. | Rakd le az I alakzatot függőlegesen. | Két sor eltűnik; Lines +2, Score +300 +12. | `LineClearTest.f030_twoCompleteRowsAreCleared`, `UtilsTest.f029_f032_scoreTableMatchesReadme` (A) | |
| F-031 | HÉ | Három sor egy-egy cella híján kész. | Rakd le az I alakzatot függőlegesen. | Három sor eltűnik; Lines +3, Score +700 +12. | `LineClearTest.f031_threeCompleteRowsAreCleared` (A) | |
| F-032 | HÉ | Négy sor egy-egy cella híján kész. | Rakd le az I alakzatot függőlegesen. | Négy sor eltűnik; Lines +4, Score +1500 +12. | `LineClearTest.f032_fourCompleteRowsAreCleared` (A) | |
| F-033 | EP | Egy sorból két cella hiányzik. | Rakj le egy alakzatot, amely csak az egyik rést tölti ki. | A hiányos sor nem törlődik. | `LineClearTest.f033_incompleteRowIsNotCleared` (A) | |
| F-034 | EP | A törlendő sor fölött és alatt is vannak elemek. | Fejezd be a sort. | A fölötte lévők süllyednek, az alatta lévők helyben maradnak. | `LineClearTest.f034_bricksAboveSinkAndBricksBelowStay` (A) | |
| F-035 | EP | Futó játék, Score ismert. | Rakj le egy alakzatot sortörlés nélkül. | Csak a 12 pontos lerakási bónusz jár, a sorszám nem nő. | `UtilsTest.f035_*`, `LineClearTest.f035_*`, `GameViewModelTest.f027_f035_*` (A) | |
| F-036 | HÉ | A Lines kijelzőn 19 sor. | Töröld a 20. sort. | A Level 1-ről 2-re nő. | `ViewStateTest.f036_levelChangesFromOneToTwoAtTwentyLines` (A, logika); a kijelző kézzel | Előkészítés: tesztsegéd vagy hosszú játék, lásd Feltételezések. |
| F-037 | HÉ | A Lines kijelzőn 39 sor. | Töröld a 40. sort. | A Level 2-ről 3-ra nő. | `ViewStateTest.f037_levelChangesFromTwoToThreeAtFortyLines` (A, logika) | |
| F-038 | HÉ | A Lines kijelzőn 179 sor. | Töröld a 180. sort, majd tovább. | A Level 9-ről 10-re nő, később nem lépi túl a 10-et. | `ViewStateTest.f038_*`, `ViewStateTest.f040_levelIsCappedAtTen` (A, logika) | |
| F-039 | TA | Level 1 és magasabb szint (pl. előkészített állapot) azonos eszközön. | Mérd stopperrel vagy videóval az automatikus lépések közötti időt. | Magasabb szinten rövidebb az idő. A kód képlete: 650 ms − 55 ms × (Level − 1). | – (K) | **Kódolvasás alapján ellenőrizendő:** a `MainActivity` `LaunchedEffect(Unit)` blokkja az első kompozíció `viewState` értékét tartja, ezért lehet, hogy a sebesség nem nő. Ezt a mérésnek kell igazolnia vagy cáfolnia. |
| F-040 | HÉ | Level 10 elérve. | Olvasd le a Level kijelzőt. | A 10-es szint egyértelműen olvasható, nem csonkolódik egy számjegyre. | `ViewStateTest.f040_levelIsCappedAtTen` (A, logika) | **Figyelem:** a Level kijelző 1 számjegyet jelenít meg (`LedNumber(..., level, 1)`), ezért a 10-es szint kijelzése hibás lehet. |

### Hang

| ID | Tech. | Előfeltétel | Lépések | Elvárt eredmény | Egységteszt | Eredmény / Megj. |
|---|---|---|---|---|---|---|
| F-041 | EP | Hang bekapcsolva, az eszköz hangja be van állítva. | Mozgatás, forgatás, ejtés és sortörlés végrehajtása. | Az eseményhez tartozó effekt lejátszódik. | – (K) | A kód az érvénytelen mozgatásnál is lejátszhatja a hangot (a lejátszás az érvényesség vizsgálata előtt történik). Rögzítsd a megfigyelést. |
| F-042 | EP | Futó játék, hang be. | Nyomd meg a `SOUNDS` gombot. | A némításjelzés megváltozik, az új effektek elnémulnak. | `GameViewModelTest.f042_f043_muteTogglesTheFlag` (A, állapot) | |
| F-043 | EP | Némított állapot. | Nyomd meg újra a `SOUNDS` gombot. | A következő effektek ismét hallhatók. | `GameViewModelTest.f042_f043_muteTogglesTheFlag` (A, állapot) | |
| F-044 | ÁT | Némított állapot. | Nyomd meg a `START/RESET` gombot, indíts új játékot. | A némítás megmarad az új játékban. | `GameViewModelTest.f049_f044_resetAfterGameOverStartsCleanGameAndKeepsMute` (A) | |

### Életciklus, játék vége, óra

| ID | Tech. | Előfeltétel | Lépések | Elvárt eredmény | Egységteszt | Eredmény / Megj. |
|---|---|---|---|---|---|---|
| F-045 | ÁT | Futó játék. | Nyomd meg a Home gombot (háttérbe lépés), várj 10 másodpercet. | A játék szünetel, a pálya és a pontszám nem halad tovább. | – (K) | |
| F-046 | ÁT | A játék a háttérben van (F-045 után). | Lépj vissza az alkalmazásba. | A korábbi pálya és pontszám megmarad. Rögzítsd, hogy a játék automatikusan folytatódik-e vagy szünetel. | – (K) | A kód `onResume`-ban `Resume`-ot küld. Külön variáns: a játékot kézzel szüneteltetve lépj a háttérbe és vissza; rögzítsd, a játék szünetel-e. |
| F-047 | UC | Futó játék. | Zárd be az alkalmazást (kényszerített leállítás), nyisd meg újra. | Az alkalmazás és a hangkezelés működik; korábbi játék mentését nem várjuk el. | – (K) | |
| F-048 | ÁT | A pálya majdnem tele, új alakzat nem fér el. | Várd meg a következő alakzatot. | A játék véget ér, a "GAME OVER" felirat megjelenik. | `GameViewModelTest.f048_overlappingPieceEndsTheGame` (A) | |
| F-049 | ÁT | Játék vége állapot. | Nyomd meg a `START/RESET` gombot. | Új játék indul tiszta pályával, 0 ponttal, 0 sorral, 1-es szinttel. | `GameViewModelTest.f049_f044_resetAfterGameOverStartsCleanGameAndKeepsMute` (A) | |
| F-050 | EP | Az eszköz órája ismert. | Hasonlítsd össze a játék óráját az eszköz idejével, figyeld a percfordulót. | Az óra a helyi időt (óra:perc) mutatja, a percforduló után frissül. | – (K) | |

## Összesítő (végrehajtás után kitöltendő)

| Mutató | Érték |
|---|---|
| Összes eset | 50 |
| Sikeres | |
| Sikertelen | |
| Blokkolt | |
| Nem futtatott | |
| Végrehajtási arány | |
| Elsődleges környezet | Pixel 2, API 35 |
| Másodlagos környezet | API 21 |
| Commit / APK azonosító | |
| Végrehajtó(k) és dátum | |

## Megjegyzések

- Az automatizált tesztek a játéklogikát vizsgálják, ezért önmagukban nem elegendőek: a kijelzők, hang, animáció és életciklus esetén a kézi végrehajtás kötelező.
- Minden sikertelen esethez GitLab-hibajegy készül (bejelentő, verzió, környezet, reprodukció, várt és tényleges eredmény, súlyosság, prioritás), amelynek hivatkozását az **Eredmény / Megj.** oszlopba kell írni.
- A kódolvasásból származó észrevételek (F-039, F-040, F-041, F-046) kiindulópontok a teszteléshez, nem igazolt hibák. A mérés vagy a megfigyelés dönt.
