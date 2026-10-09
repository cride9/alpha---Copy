# Compose Tetris – Első statikus elemzési riport

**Azonosító:** ALPHA-TETRIS-SA-002 · **Verzió:** 1.0-tervezet · **Dátum:** 2026-10-09

**Állapot:** eszközös elemzés és első triázs kész; csapattársi felülvizsgálat és GitLab-issue még hiányzik. A termékkódot nem javítottuk.

## Hatókör és reprodukálás

Upstream: `vitaviva/compose-tetris`, commit `234416c455cd0b5524b7f2a7e91aaa9f6206457a`. A vizsgált másolat az ALPHA repo `software/compose-tetris` könyvtára. Az eredeti `app/src/main` forrásai és erőforrásai változatlanok; a build tesztfüggőségekkel egészül ki. A pontos forrásazonosítás a [környezeti leírásban](Compose_Tetris_Kornyezet.md) található.

Lint 7.1.2: `:app:lintDebug`, a debug variáns Kotlin/manifest/erőforrás és build-konfiguráció vizsgálata. Detekt CLI 1.23.8: csak `app/src/main/java`, alapkonfigurációra épített szabálykészlet, **classpath és típusfeloldás nélkül**. A tesztkód, generált kód, külső könyvtárimplementáció nem része a Detekt-hatókörnek. Az ilyen futás típusfeloldást igénylő szabályai nem tekinthetők lefedettnek.

```powershell
.\software\compose-tetris\gradlew.bat --project-dir software\compose-tetris :app:lintDebug
java -jar .tools/detekt-cli-1.23.8-all.jar --input software/compose-tetris/app/src/main/java --build-upon-default-config --report xml:reports/static/detekt.xml --report html:reports/static/detekt.html --report txt:reports/static/detekt.txt --base-path .
```

A Lint sikeresen lefutott (exit 0); a Detekt exit **2** értéke a 101 súlyozott találat miatt elért hibaküszöböt jelzi. A Detekt létrehozta a riportokat, nem konfigurációs hibán állt meg.

**Detekt-kalibráció:** azonos beállítással a [szándékosan hibás mintában](../tools/calibration/positive/Probe.kt) a nem használt privát függvényt és a konstans-visszaadást jelzi; a [negatív kontrollon](../tools/calibration/negative/Probe.kt) nincs jelzés. Pozitív exit 2, negatív exit 0; [pozitív XML](../reports/static/calibration-positive.xml), [negatív XML](../reports/static/calibration-negative.xml), [napló](../reports/environment/detekt-calibration.log). Ez az AST-alapú szabályok alapműködését igazolja, nem minden szabályt és nem a típusfeloldást. Reprodukálás: a fenti Detekt-parancs `--input` értékét cseréld `tools/calibration/positive` vagy `tools/calibration/negative` értékre, a riportnak pedig külön fájlnevet adj. A Lint külön pozitív/negatív kontrollja még nincs elkészítve.

## Nyers statisztika

| Lint-szabály | Találatok |
|---|---:|
| GradleDependency | 12 |
| UnusedResources | 7 |
| RedundantLabel | 1 |
| ModifierParameter | 1 |
| IconLauncherShape | 1 |
| IconLocation | 1 |
| **Összesen** | **23 warning, 0 error** |

| Detekt-szabály | Találatok |
|---|---:|
| MagicNumber | 52 |
| FunctionNaming | 14 |
| NewLineAtEndOfFile | 13 |
| LongMethod | 4 |
| ForEachOnRange | 4 |
| WildcardImport | 4 |
| TopLevelPropertyNaming | 3 |
| VariableNaming | 3 |
| CyclomaticComplexMethod | 1 |
| LongParameterList | 1 |
| ImplicitDefaultLocale | 1 |
| UnusedPrivateMember | 1 |
| **Összesen** | **101** |

A 124 jelzés az eszközkimenetek összege; ismétlődő és egymást átfedő problémákat is tartalmaz. A Lint távoli függőségi metaadataitól függő frissítési javaslatok idővel változhatnak; ezek nem automatikus biztonsági sebezhetőségi bizonyítékok. A nyers riportban ajánlott könyvtárverziókat nem vezettük át a régi Kotlin/Compose/AGP stack kompatibilitásvizsgálata nélkül.

Nyers [Lint XML](../reports/static/lint-results-debug.xml), [Lint HTML](../reports/static/lint-results-debug.html), [Detekt XML](../reports/static/detekt.xml), [Detekt HTML](../reports/static/detekt.html), [Detekt szöveg](../reports/static/detekt.txt), [futtatási napló](../reports/environment/detekt-run.log).

## Tíz kiválasztott eszköztalálat részletes értékelése

A súlyosság a termékre gyakorolt hatás előzetes becslése, nem az eszköz prioritásszáma. A javítási órák javaslatok, nem elvégzett munka. A többi találat egyenkénti végső triázsa még hátravan.

### SA-001 – Összetett állapotcsökkentő

**Hely:** `logic/GameViewModel.kt:26`, Detekt `CyclomaticComplexMethod` (35/15), kapcsolódó `LongMethod` (129/60). **Minősítés:** valódi karbantarthatósági probléma; közepes, a játékállapot-kezelés miatt kiemelt prioritás. A mozgás, animáció, pontozás és hang egy metódusba kerül. A sok ág növeli a hibás állapotátmenet kockázatát, de önmagában nem bizonyít hibás futást. **Javaslat:** műveletenként tiszta állapotátmenetekre bontás, mellékhatások külön kezelése, meglévő 22 integrációs ellenőrzés megtartása. **Becslés:** 6 óra + regresszió. A két szabály egy problémakör, nem két független súlyos hiba.

### SA-002 – Hosszú felületépítő

**Hely:** `ui/GameBody.kt:37`, Detekt `LongMethod` (170/60). **Minősítés:** valódi szerkezeti jelzés, alacsony/közepes. A Compose deklaratív felépítése indokol hosszabb metódust, ezért a nyers küszöbtúllépés nem súlyos termékhiba. A kijelző és vezérlőegységek együtt változnak, a kis kijelzőn történő ellenőrzés nehéz. **Javaslat:** képernyő, iránygombok és beállításgombok elkülönített composable-jei. **Becslés:** 2 óra + vizuális ellenőrzés mindkét eszközön.

### SA-003 – Hosszú ikonkirajzolás

**Hely:** `ui/AppIcon.kt:30`, Detekt `LongMethod` (80/60). **Minősítés:** szándékos rajzolási kód, alacsony; súlyos szabálysértésként nem elszámolható. A hossz oka a grafika koordinátáinak leírása. **Javaslat:** csak olvashatósági igény esetén alakzatonként helper; a képet referencia-preview-hoz hasonlítani. **Becslés:** 1 óra; javítása nem előzi meg a logikai hibákat.

### SA-004 – Esemény- és ismétléskezelés egy komponensben

**Hely:** `ui/GameButton.kt:39`, Detekt `LongMethod` (73/60). **Minősítés:** valódi szerkezeti probléma, közepes. A touch-esemény, ripple és ismétlő coroutine együtt él; a ticker életciklusa emiatt nehezen áttekinthető. Az esetleges erőforrásproblémát külön MA-005 jelöli, nem a hossz küszöbe igazolja. **Javaslat:** ismétlő Job külön kezelése, DOWN/UP/CANCEL/dispose utak tesztjei. **Becslés:** 3 óra + eszközös ellenőrzés.

### SA-005 – Sok scoreboard paraméter

**Hely:** `ui/GameScreen.kt:116`, Detekt `LongParameterList` (8/6). **Minősítés:** kontextusfüggő, alacsony. A nyolc érték a kijelző természetes bemenete; név szerinti hívások csökkentik az összecserélés esélyét. **Javaslat:** külön megjelenítési állapotobjektum, ha a felület tovább bővül; a teljes ViewModel átadása kerülendő. **Becslés:** 1 óra. Nem igazolt működési hiba.

### SA-006 – Területi beállítás implicit használata

**Hely:** `ui/LedNumber.kt:119`, Detekt `ImplicitDefaultLocale`. **Minősítés:** valódi hordozhatósági kockázat, közepes; eszközön még nem reprodukált. A nullával kitöltött óra `String.format` hívása az eszköz locale-ját használja, miközben a LED-betűkészlet támogatása nincs igazolva minden számjegyre. **Javaslat:** a kívánt számjegyformátum egyeztetése után explicit Locale vagy locale-független padding; magyar és eltérő számjegykészletű locale ellenőrzése. **Becslés:** 1 óra + 1 óra kompatibilitási próba.

### SA-007 – „Nem használt” preview

**Hely:** `ui/AppIcon.kt:128`, Detekt `UnusedPrivateMember`, `PreviewAppIcon`. **Minősítés:** téves pozitív az Android Studio preview használati módja miatt. A `@Preview` funkciót a tooling használja, normál Kotlin-hívóhely nem szükséges. **Javaslat:** célzott szabálykivétel a preview-ra, indoklással; ne töröljük a fejlesztői előnézetet pusztán emiatt. **Becslés:** 0,25 óra. Súlyos találatként nem számítható be.

### SA-008 – Preview Modifier alapértéke

**Hely:** `ui/GameScreen.kt:319`, Lint `ModifierParameter`. **Minősítés:** a Compose API-konvenciót ténylegesen sérti, de preview-ról van szó; alacsony hatás. **Javaslat:** `modifier: Modifier = Modifier`, a preview konkrét méretezése a törzsben vagy wrapperben. **Becslés:** 0,25 óra + preview. Nem igazolt játékhiba.

### SA-009 – Duplikált activity-címke

**Hely:** `app/src/main/AndroidManifest.xml:14`, Lint `RedundantLabel`. **Minősítés:** valódi redundancia, alacsony. Az application címkéjével megegyezik, így elhagyható; jelenleg az app nevét nem teszi hibássá. **Javaslat:** activity-label törlése és alkalmazásnév ellenőrzése. **Becslés:** 0,25 óra. Súlyos problémaként nem védhető.

### SA-010 – Bitmap launcher ikon density nélkül

**Hely:** `app/src/main/res/drawable/ic_launcher.png`, Lint `IconLocation`; kapcsolódó `IconLauncherShape`. **Minősítés:** valódi erőforrás-konfigurációs kockázat, alacsony/közepes; vizuális következmény eszközön nem ellenőrzött. A skálázás és az eltérő launcherek maszkolása ronthatja az ikont. **Javaslat:** megfelelő density/mipmap és adaptive icon variánsok, kerek és normál launcher ellenőrzése. **Becslés:** 2 óra. A kapcsolódó alakjelzés nem számolható automatikusan külön súlyos hibának.

## Kézi kódáttekintésből származó hibajelöltek

Ezek **nem Lint/Detekt által igazolt jelzések**, és nem végrehajtott Androidos hibareprodukciók. Külön GitLab-jegyet érdemelnek a következő ellenőrzéshez. Javításukat nem végeztük el.

### MA-001 – A timer a kezdeti szintet használja

**Hely:** `MainActivity.kt:38–42`. A `LaunchedEffect(Unit)` a belépéskor kiolvasott `viewState` objektumot zárja körül. A későbbi recomposition ugyanazon kulcsnál nem indítja újra a coroutine-t. **Következmény:** a kód alapján a szintkijelző nőhet, az automatikus esés mégis a kezdeti 650 ms-on marad. **Előzetes súlyosság:** magas. **Ellenőrzés:** F-039, szint 1 és 2/10 legalább 10 tick-intervalluma ugyanazon eszközön. **Javaslat:** friss állapot olvasása vagy `rememberUpdatedState`, illetve jól választott effect-kulcs; becslés 1,5 óra + mérés. A játékóra globális ütemezése miatt az első tick külön kezelendő.

### MA-002 – Üres alakzat ejtése nem terminál

**Hely:** `logic/GameViewModel.kt:86–96`; `Spirit.kt:56`. A Reset Running állapotot ad `Spirit.Empty` értékkel az első tickig. Az üres alakzat `location.none` vizsgálata minden eltolásnál igaz, ezért a Drop `while` ciklusának nincs kilépési feltétele ebben az állapotban. **Következmény:** korai ejtés végtelen CPU-ciklust indíthat egy Default workerben, nincs suspension/cancellation pont. **Előzetes súlyosság:** magas. **Ellenőrzés:** az első tick előtt végzett gyors ejtés, elkülönített emulátoron és naplóval; szükség esetén az appfolyamat leállítása. A JUnitban csak az üres geometria igaz, veszélyes végtelen futást nem indítottunk. **Javaslat:** Empty guard és legfeljebb pályamagasságnyi iteráció; becslés 1 óra + regresszió.

### MA-003 – Felszabadított SoundPool újrahasználata

**Hely:** `logic/Utils.kt:71–91`, `MainActivity.kt:onCreate/onDestroy`. A singleton `val sp by lazy` csak egyszer készül el, `release()` után az `init()` ugyanazt a felszabadított poolt használja. **Következmény:** ugyanabban a folyamatban újranyitott vagy újralétrehozott activity hangja hibás lehet. **Előzetes súlyosság:** közepes/magas. **Ellenőrzés:** F-047, azonos folyamatban activity bezárás/újranyitás, hallható effekt + Logcat. **Javaslat:** újrakészíthető pool és applicationContext, aktív stream/loaded státusz kezelése; becslés 2 óra + Androidos regresszió. Összeomlást nem állítunk igazoltként.

### MA-004 – Párhuzamos műveletek régi állapotból dolgozhatnak

**Hely:** `logic/GameViewModel.kt:22–29`, animációk `launch`/`emit` útjai. A `dispatch` az induláskor átadja az állapotpillanatképet, külön coroutine-ok később felülírhatják egymás eredményét. **Következmény:** gyors bemenet, tick vagy némítás animáció közben állapotvesztést okozhat; reprodukció még hiányzik. **Előzetes súlyosság:** közepes/magas. **Ellenőrzés:** F-016 és F-026; serializált műveletsorhoz hasonlítás. **Javaslat:** egyetlen műveletfeldolgozó/actor és rendezett állapotfrissítés; becslés 4 óra + konkurens tesztek. Nem szabad az összes lehetséges versenyhelyzetet külön súlyos találatként számolni.

### MA-005 – A gombismétlő ticker nem remember-elt erőforrás

**Hely:** `ui/GameButton.kt:47,85–110`. A lokális `lateinit ticker` újrakompozíciónál új változó, az eseménycallback az új értéket használhatja, miközben a korábbi csatorna gyűjtése él. Külön dispose-lezárás nincs. **Következmény:** UP/CANCEL úton inicializálatlan hozzáférés vagy elmaradó csatornaleállítás lehetséges. **Előzetes súlyosság:** közepes/magas, **feltételezett**. **Ellenőrzés:** hosszú gombnyomás közbeni recomposition, elengedés és képernyőelhagyás; Logcat/CPU. **Javaslat:** remember-elt nullable Job/channel és onDispose cancel; becslés 2 óra + eszközös ellenőrzés.

## Következő lépés és elfogadás

A 10 eszköztalálat elemzése elkészült, de **a végső követelmény szerinti 10 súlyos szabálysértés nincs teljesítve**. A hibajelöltek súlyosságát a következmény és reprodukció alapján kell véglegesíteni; a kisebb stiláris eltérések nem tölthetik fel a darabszámot. Szükséges a fennmaradó jelzések triázsa, a Lint-kalibráció kiegészítése, a második csapattag review-ja és a hibajegyek létrehozása. A kiválasztott feladattípus változatlan.
