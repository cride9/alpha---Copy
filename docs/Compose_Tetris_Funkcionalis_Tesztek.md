# Compose Tetris – Funkcionális tesztspecifikáció

**Azonosító:** ALPHA-TETRIS-FT-002 · **Verzió:** 1.0-tervezet · **Dátum:** 2026-10-09

Az F-001–F-050 azonosítók és témák változatlanul az eredeti [kiegészítő munkanyag](Compose_Tetris_Kiegeszitesek.md) katalógusát követik. Az alábbiak végrehajtható lépések, **nem végrehajtási eredmények**. Mind az 50 eset kezdetben NOT RUN; [végrehajtási jegyzőkönyv](Compose_Tetris_Funkcionalis_Jegyzokonyv.md).

## Tesztbázis, adatok és közös előfeltételek

**B1:** README: Android minimum 21, hét alakzattípus, játékvezérlés és 1/2/3/4 sorra 100/300/700/1500 pont. **B2:** tesztterv és csapat által véglegesítendő forrásalapú elvárások: 12 pontos lerakási bónusz, 20 soronként szintlépés, 10-es szintkorlát, reset/lifecycle működés. B2 csapatjóváhagyása még szükséges; az implementáció nem független bizonyíték a specifikáció helyességére. A wall-kick esetek pályán belüli, ütközésmentes eredményt kérnek, konkrét verseny-Tetris forgatási rendszert nem feltételeznek.

Minden futás előtt: az APK SHA és az eszköz adatai rögzítettek; a telepítés/indítás sikeres; a 12×24 pálya látható; animáció nem folyamatban; a dátum, végrehajtó és bizonyíték azonosítója a jegyzőkönyvbe kerül. P1 magas, P2 normál prioritás. A PR-számok a tesztterv termékkockázatai. A grid koordinátái: x=0…11 balról, y=0…23 fentről; a negatív y a belépő alakzatnál megengedett.

**Bizonyíték:** egyértelműen feliratozott képernyőkép vagy rövid videó; hibánál Logcat és GitLab-issue. Ajánlott név: `F-029_API35_20261009_01.mp4`. A kézi végrehajtásból PASS/FAIL csak megfigyelt várt/tényleges eredmény alapján írható. Hiányzó környezet/állapot/orákulum esetén BLOCKED, a kihagyott eset NOT RUN. Ezek nem számítanak a 25/50-es célértékbe.

## Reprodukálható állapotok előkészítése

| Jel | Előkészített állapot / tesztadat |
|---|---|
| A | Frissen indított app, Onboard, pont/sor=0, szint=1 |
| R | Running, üres pálya, I alakzat origó=(5,8), pont/sor=0; következő alakzat T |
| P | Ugyanaz mint R, de Paused |
| W-L / W-R | Running; aktív alakzat legbaloldalibb x=0 / legjobboldalibb x=11 |
| FLOOR | Running, I origó=(5,21), legalsó cella y=23, nincs teljes sor |
| OBST | Running, I origó=(5,8), rögzített cella közvetlenül a vizsgált célmezőn |
| ROW-k | k=1…4; alsó k sor minden x-en foglalt x=5 kivételével; I origó=(5,21); pont/sor=0 |
| GAP | A ROW-1-ből az x=6,y=23 cella is hiányzik; az I lerakása után is marad rés |
| SHIFT | y=20 sor foglalt x=5 kivételével; I origó=(5,18), támasz=(5,21); jelölőcellák=(4,10) és (4,22) |
| LV-19 / LV-39 / LV-179 | ROW-1, de induló töröltsor-szám 19 / 39 / 179 |
| LV-200 | Running, üres pálya, 200 törölt sor, I origó=(5,8); szint 10 |
| FULL | Belépő alakzat celláival ütköző felső pálya; nem teljes sort választunk; pont/sor előre rögzítve |
| GO | A FULL állapotból megfigyelt GameOver; animáció befejeződött |

A hét alakzat pontos koordinátái a `SpiritType` forrásában és a parametrizált egységtesztben találhatók. Az R tartalék előre rögzített T-vel kezdődik; a tartalék további elemei tetszőleges, érvényes alakzatok.

Az állapotok normál játékkal is előállíthatók. A pont/sor küszöbök gyors beállításához Android Studio debugger használható: stabil, szüneteltetett játékban állj meg a `GameViewModel.emit` állapotértékadásánál, lépj túl az értékadáson, majd a metódus visszatérése előtt állítsd `_viewState.value` értékét az alábbihoz hasonló `ViewState`-re. Ezután töröld a breakpointot, folytasd a futást, és a képernyőn ellenőrizd a fixture-t. Animáció közben ne készíts fixture-t. Az elvárt eredmény ellenőrzése ezután a felületen történik; a debugger csak előfeltételt készít.

Példa ROW-4, először szünetben, majd a felületen folytatva:

```kotlin
_viewState.value = ViewState(
    bricks = Brick.of(0..11, 20..23).filter { it.location.x != 5f },
    spirit = Spirit(SpiritType[2], Offset(5, 21)),
    spiritReserve = listOf(Spirit(SpiritType[3], Offset(5, -1))),
    gameStatus = GameStatus.Paused,
    score = 0, line = 0, isMute = true
)
```

ROW-1/2/3 esetén az alsó sorok tartománya rendre 23..23 / 22..23 / 21..23. A szinthatárokhoz `line=19/39/179`; hangteszthez `isMute=false`. A megfelelő importokat a debugger kifejezésében teljesen minősített nevekkel is megadhatod.

**Korlát:** a debuggerrel történő előkészítést Androidon ebben a munkában nem próbáltuk ki; nincs kész debug-menü vagy automatizált UI-fixture. A csapat elsőként ezt ellenőrzi, és naplózza a tényleges előkészítést. Ha az állapot nem stabil vagy nem állítható elő, az eset BLOCKED és előkészítő feladat szükséges. A beállítás után látható pálya/pont/sor megfigyelése kötelező, a debugger kifejezés puszta lefutása nem elég.

## Az 50 eset

| ID · prioritás · kockázat | Előfeltétel / adat | Pontos lépések | Elvárt eredmény |
|---|---|---|---|
| F-001 · P1 · PR1 | Azonosított APK, telepíthető eszköz | Telepítsd az APK-t; nyisd meg az appot; várj 5 s-ot; ments képet és indítási Logcatet | Kezdőfelület és minden vezérlő megjelenik, nincs crash/ANR |
| F-002 · P1 · PR1,PR4 | A | Nyomj egyszer Resetet; figyeld az első két automatikus lépést | Running állapot és egy aktív alakzat jelenik meg; nincs több aktív alakzat |
| F-003 · P1 · PR3 | F-002 után, még nincs lerakott alakzat | Olvasd le és fényképezd a Score/Lines/Level mezőket | 0 pont, 0 sor, szint 1 |
| F-004 · P1 · PR4 | R | Nyomj Pause-t; rögzítsd a pályát; várj 2 s-ot; rögzítsd újra | Szünetjelzés aktív; alakzat és pont/sor nem változik |
| F-005 · P1 · PR4 | P | Rögzítsd a pályát és értékeket; nyomj folytatást; várj két tickre | Ugyanaz a játék folytatódik, az alakzat lejjebb jut, értékek nem nullázódnak |
| F-006 · P1 · PR2,PR4 | P | Balra, jobbra, le, forgatás és fel/ejtés gombot egyenként nyomd meg; várj 2 s-ot | Minden geometria, pont és sor változatlan; szünet megmarad |
| F-007 · P1 · PR4 | Running, van rögzített blokk és nem nulla pont | Nyomj Resetet egyszer; várd meg a teljes képernyőtörlést | Üres Onboard állapot, 0 pont, 0 sor, szint 1; régi blokk nincs (B2) |
| F-008 · P1 · PR4 | Előzővel azonos, Paused | Nyomj Resetet; várd meg a törlés végét | Üres Onboard, 0 pont/sor, szint 1; régi játék nem folytatódik (B2) |
| F-009 · P1 · PR2 | R, akadálytalan bal célmező | Rögzítsd a cellák x-ét; nyomj egyszer balra; hasonlítsd össze | Mind a négy cella x-e pontosan -1; forma változatlan |
| F-010 · P1 · PR2 | R, akadálytalan jobb célmező | Rögzítsd az x-eket; nyomj egyszer jobbra | Mind a négy x pontosan +1; forma változatlan |
| F-011 · P1 · PR2 | R, szabad alsó célmező | Videón egyetlen lefelé gombnyomást rögzíts két automatikus tick között | Az alakzat egy sorral lejjebb kerül; ha a tick egybeesik, ismételd meg a mérést |
| F-012 · P1 · PR2 | W-L | Három külön balra gombnyomás; rögzítsd a pályaszélt | Egyik cella x-e sem negatív, a bal határon nem mozdul tovább |
| F-013 · P1 · PR2 | W-R | Három külön jobbra gombnyomás | Egyik cella x-e sem >11, a jobb határon nem mozdul tovább |
| F-014 · P1 · PR2 | OBST, a jobb mozgás egyik célcellája foglalt | Fotózd az állapotot; nyomj jobbra; figyeld a rögzített és aktív cellákat | Az oldalirányú mozgás elutasított, nincs átfedés, rögzített blokkok megmaradnak |
| F-015 · P1 · PR2 | FLOOR | Nyomj lefelé; figyeld a következő automatikus ticket | Nincs y>23 cella; a következő tick az alakzatot rögzíti és újat ad |
| F-016 · P1 · PR3,PR4 | ROW-1, aktív videó | Hagyd a sortörlést elindulni; a villogás alatt nyomj 5-5 mozgás/forgatás/ejtés parancsot | Pontosan egy sor számolódik el, nincs átfedés/crash; 112 pont, 1 sor (B2) |
| F-017 · P1 · PR2 | R, szabad középső pálya | Rögzítsd a formát; nyomj egyszer Rotate-ot | Az I függőlegesből vízszintesre fordul, 4 cellával, ütközés nélkül |
| F-018 · P1 · PR2 | Szabad pálya, T origó=(5,8) | Két tick között vagy előkészített megfigyeléssel nyomj négy Rotate-ot; különítsd el az automatikus y-elmozdulást | Eredeti orientáció és x-origó visszaáll; forgatás nem okoz külön y-eltolást a szabad térben |
| F-019 · P1 · PR2 | W-L, a forgatás kilógna balra | Nyomj Rotate-ot; fotózd az eredményt | Korrekcióval vagy elutasítással minden x 0…11, nincs átfedés |
| F-020 · P1 · PR2 | W-R, a forgatás kilógna jobbra | Nyomj Rotate-ot; fotózd az eredményt | Korrekció vagy elutasítás, pályán belüli és ütközésmentes eredmény |
| F-021 · P1 · PR2 | Pályaalji, forgatható alakzat; rögzülés előtti tickablak | Nyomj Rotate-ot, rögzítsd a pályaaljat | Nem keletkezik y>23 cella; nincs rögzített cellával átfedés |
| F-022 · P1 · PR2 | T körül legalább egy foglalt, csak a forgatott alakzatot érintő célmező | Rögzítsd a formát; nyomj Rotate-ot | Az ütköző forgatás elutasított, rögzített cellák nem törlődnek |
| F-023 · P1 · PR2 | Egyenként Z,S,I,T,O,L,J szabad középső pályán | Mindegyiken nyomj négy Rotate-ot; orientációnként rögzíts 1 képet | Mindig 4 külön cella, pályán belüli forma; négy forgatás után eredeti orientáció (egy parametrizált eset, nem 7 külön F-ID) |
| F-024 · P1 · PR2 | R, üres pálya | Nyomj fel/Drop-ot; az első tick előtti eredményt rögzítsd | Az I alsó cellája y=23; nincs átfedés vagy pálya alá jutás |
| F-025 · P1 · PR2 | R, rögzített blokk x=5,y=20 | Nyomj Drop-ot; fotózd a célpozíciót | A legalsó aktív cella y=19; az akadály y=20-on megmarad |
| F-026 · P1 · PR2,PR4 | FLOOR, a következő tick előtt | Nyomj három gyors Drop-ot, majd várj egy rögzítésre | Nincs érvénytelen pozíció; ugyanaz az alakzat egyszer, 12 ponttal számolódik el (B2) |
| F-027 · P1 · PR2 | R | Ne adj bemenetet; videózd az automatikus esést a rögzülésig | Az alakzat lefelé halad, a pálya alján rögzül, következő alakzat jön |
| F-028 · P1 · PR2 | R, Next=T | Fotózd a Next-et; ejtsd/rögzítsd az I-t; hasonlítsd a megjelenő alakzatot | T jelenik meg a pályán, a Next az új tartalékkal frissül |
| F-029 · P1 · PR3 | ROW-1 | Rögzítsd a kezdő számlálókat; folytasd; várj a törlés végéig | 1 sor eltűnik, Lines=1, Score=112 (100+12), Level=1 (B2) |
| F-030 · P1 · PR3 | ROW-2 | Kezdőértékek mentése; folytatás; animáció végének megvárása | 2 sor eltűnik, Lines=2, Score=312, Level=1 (B2) |
| F-031 · P1 · PR3 | ROW-3 | Kezdőértékek mentése; folytatás; animáció végének megvárása | 3 sor eltűnik, Lines=3, Score=712, Level=1 (B2) |
| F-032 · P1 · PR3 | ROW-4 | Kezdőértékek mentése; folytatás; animáció végének megvárása | 4 sor eltűnik, Lines=4, Score=1512, Level=1 (B2) |
| F-033 · P1 · PR3 | GAP | Folytasd; várd meg az I rögzítését; figyeld az x=6 rést | Nincs sortörlés; Lines=0, Score=12; hiányos sor megmarad (B2) |
| F-034 · P1 · PR3 | SHIFT | Rögzítsd a jelölőcellákat; folytasd a sortörlés végéig | y=20 sor törlődik; (4,10)→(4,11); (4,22) helyben marad |
| F-035 · P1 · PR3 | FLOOR, Score=0, Lines=0 | Hagyd rögzülni az I-t; olvasd le az értékeket | Score=12, Lines=0, Level=1 (B2) |
| F-036 · P1 · PR3 | LV-19 | Kezdő Level=1 mentése; folytasd az egy sor törlését | Lines=20, Level=2; pontváltozás=112 (B2) |
| F-037 · P1 · PR3 | LV-39 | Kezdő Level=2 mentése; törölj egy sort | Lines=40, Level=3; pontváltozás=112 (B2) |
| F-038 · P1 · PR3 | LV-179, majd újabb sor-előkészítés | Törölj egy sort; rögzítsd 180-at; végezz további 20 sor törlést vagy külön LV-200 ellenőrzést | 179-nél Level=9, 180-nál 10; 200-nál is 10 (B2); a külön fixture nem egy folyamatos játékmenet bizonyítéka |
| F-039 · P1 · PR3,PR8 | Ugyanaz az eszköz, szabad pálya szint 1 és 2, majd 10 | Videózd legalább 10-10 tick intervallumát; első ticket hagyd ki; hasonlíts mediánt; ismételd új fixture-rel | A tickek a szinttel gyorsulnak; forrásból javasolt periódus 650/595/155 ms, mérési tolerancia előre rögzítendő (B2) |
| F-040 · P2 · PR5 | LV-200 | Fotózd normál rendszerbetűméretnél a Level mezőt | A 10 teljesen, olvashatóan jelenik meg; nincs levágott számjegy |
| F-041 · P2 · PR5 | R + ROW-1, isMute=false, eszköz médiahangerő hallható | Külön mozgás, Rotate, Drop és sortörlés; készíts hangos felvételt vagy tesztelői megfigyelést | Az eseményekhez tartozó hangok hallhatók, nincs hangkezelési hiba; a bemenet/rendszermixer is ellenőrzött |
| F-042 · P1 · PR5 | Hang engedélyezett, F-041-ben hallható mozgáshang | Nyomj Mute-ot; adj mozgás és forgatás parancsot | Némításjelzés aktív, új effektek nem hallhatók; már lejátszott hang végét külön kezeljük |
| F-043 · P2 · PR5 | Némított R | Nyomj Mute-ot; mozgás és forgatás | Jelzés kikapcsol, az új effektek újra hallhatók |
| F-044 · P2 · PR4,PR5 | Némított, nem üres játék | Reset; animáció vége; új játék indítása; mozgás | Némítás megmarad, új eseményhang sem szól (B2) |
| F-045 · P1 · PR4 | R, azonosított pont/sor | Home-mal háttérbe lépés; várj 3 s; térj vissza; a videón a visszatérés első állapotát nézd | A háttéridő alatt nem halad előre a játék, korábbi pont/pálya megmarad (B2) |
| F-046 · P1 · PR4 | F-045, folyamat nem állt le | Visszatérés után figyelj két tickig | Megőrzött állapotból automatikusan folytatódik a játék a jelenlegi lifecycle-elvárás szerint (B2) |
| F-047 · P1 · PR1,PR5 | Hallható hang, azonos alkalmazásfolyamat | Backkel zárd az activityt; nyisd újra process-kill nélkül; indíts és mozgasd/forgasd az alakzatot; PID és Logcat mentése | App működik, a hang újranyitás után is hallható; korábbi játék mentése nem elvárás (B2) |
| F-048 · P1 · PR4 | FULL, a megjelenő alakzat ütközik a felső blokkokkal | Hagyd a következő ticket/animációt végigfutni | GameOver állapot megjelenik; további játékbemenetek nem folytatják a játékot |
| F-049 · P1 · PR4 | GO, animáció befejeződött | Reset egyszer; várd meg az új alakzat megjelenését | Tiszta új játék, Score=0, Lines=0, Level=1; nincs régi blokk (B2) |
| F-050 · P2 · PR10 | Eszköz helyi ideje és időzónája rögzített | Hasonlítsd az órát a rendszeridőhöz; figyeld XX:59→következő perc átmenetét | Helyi óra/percek helyesek, a kijelzés legkésőbb 2 s-on belül frissül; villogó kettőspont nem módosítja az időt |

## M2-re kiválasztott első 25 végrehajtás

F-001…F-015, F-017, F-018, F-024, F-025, F-029, F-030, F-035, F-036, F-042, F-045. Ezek lefedik a telepítés/vezérlés, határok, állapotváltás, sortörlés, pontozás, szint, hang és háttérbe lépés alapjait. A kijelölt sorrend tervezési javaslat, nem készültségi állítás.

## Másodlagos környezet és további kockázatok

API 21 smoke: F-001, F-002, F-009, F-017, F-024, F-004, F-005, F-007, F-029, F-042, F-046. Ezek az elsődleges 50 ismétlései, nem növelik az egyedi esetszámot.

PR7 alakzatgenerálási eloszlás, PR8 tartós terhelés, PR9 konfigurációváltás további esetei a 50-es katalóguson túl szükségesek lehetnek. Az első automatizált tartalékteszt hét külön alakzatot ellenőriz, nem bizonyít statisztikai egyenletességet. Képernyőforgatási elvárást a csapatnak egyeztetnie kell a manifest által engedett orientációkkal; nem dokumentált orákulum alapján ne adjatok PASS-t.
