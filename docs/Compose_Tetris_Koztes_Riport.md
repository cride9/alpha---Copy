# Köztes riport – Compose Tetris

## Dokumentumadatok

**Dokumentumazonosító:** ALPHA-TETRIS-KR-002  
**Dátum és állapotfordulónap:** 2026.10.09.  
**Verzió:** 1.0-tervezet  
**Csapat:** ALPHA, IN1091LA8, Szoftvertesztelés gyakorlat, 2026/2027. I. félév  
**Készítők:** Acsai Gergő, Bartl Bálint, Juhász Ferenc Márk, Kamarás Levente, Komáromi Norbert László, Szabó Larion – csapatfelülvizsgálatra előkészített, MI segítségével összeállított munkaváltozat.  
**Riport felelőse a terv szerint:** Szabó Larion; koordinátor Komáromi Norbert László.  
**Kapcsolódó GitLab-issue:** **még létrehozandó**, a [kész issue-szöveg](Compose_Tetris_GitLab_Teendok.md) alapján.  
**Becsült riportkészítési keret:** 36 személyóra, az issue-ban is rögzítendő.  
**Mérföldkő:** [M2 – Köztes riport és tesztelési előrehaladás](https://git-okt.sed.inf.szte.hu/softwaretesting2026/in1091la8/alpha/-/milestones/2).  
**Tervezett leadás:** 2026.11.01. vasárnap 08:00, bemutató 2026.11.02. hétfő; eltérő CooSpace-közlés elsőbbséget élvez.

| Verzió | Dátum | Módosítás |
|---|---|---|
| 1.0-tervezet | 2026.10.09. | Első tényleges build/JUnit/Lint/Detekt eredmények, részletes katalógus, készültség és eltérések, felülvizsgálandó órabecslések. |

**Verziókövetés:** a dokumentum helyi munkaváltozat a `codex/mk2-testing` ágon; új commit/push nem történt a felhasználó kérésére. A csapat feltöltéskor rögzíti a GitLab commitazonosítót. Az alábbi eredmények upstream SHA-val és megőrzött kimenetekkel azonosított állapotra vonatkoznak. Alkalmazott szerkezet: [oktatói köztesriport-minta](https://okt.inf.szte.hu/tesztalap/gyakorlat/Intermediate_report/).

## Vezetői összefoglaló

A tesztterv szerint a Compose Tetris alkalmazást statikus kódelemzéssel és funkcionális teszteléssel vizsgáljuk. A teljes projekt beszerezve és fordítva rendelkezésre áll. Elkészült az első Lint/Detekt futás, 10 kiválasztott eszköztalálat értékelése, további kézi kódáttekintési hibajelöltek, az eredeti 50 funkcionális ötlet részletes specifikációja és végrehajtási jegyzőkönyve. Kiegészítésként 86 projektellenőrzés és az eredeti infrastruktúrateszt futott sikeresen.

**Igazolt eredmény:** 87/87 JUnit-futás sikeres; a debug APK elkészült; Lint 23 warning/0 error; Detekt 101 jelzés. **Funkcionális végrehajtás: 0/50**, mert nincs csatlakoztatott Android-eszköz/emulátor. A tervezett M2-célból a 25 Androidos futtatás még hátravan. A végső 10 súlyos statikus probléma követelménye sem teljesített; a kisebb és téves jelzéseket nem számoljuk annak.

A projekt a mérföldkő előtt munkaváltozatként áll rendelkezésre, **nem lezárt, beadásra kész M2**. Elsődleges következő feladat az Androidos környezet biztosítása, a fixture-ek kipróbálása, legalább 25 eset bizonyítékos futtatása, a csapat által vállalt orákulumok és tényleges munkaórák ellenőrzése, valamint a GitLab-issue és dokumentumhivatkozások létrehozása.

## Elvégzett feladatok

| Feladat | Elkészült termék / bizonyíték | Korlát |
|---|---|---|
| Követelmények összevetése a teszttervvel | [Követelménymátrix](Compose_Tetris_Kovetelmenyek.md), tesztterv 1.3 | CooSpace és csapatjóváhagyás még ellenőrzendő |
| Forrás és buildkörnyezet | [Környezeti leírás](Compose_Tetris_Kornyezet.md), upstream SHA, APK hash | Az Androidos telepítés/indítás nem futott |
| Logikai egység- és integrációs tesztek | [Tesztleírás](Compose_Tetris_Egysegtesztek.md), JUnit XML/HTML | UI/hang/lifecycle nincs igazolva; nincs coverage-mérés |
| Statikus elemzés és első triázs | [Statikus riport](Compose_Tetris_Statikus_Elemzes.md), Lint/Detekt nyers kimenetek, Detekt pozitív/negatív kontroll | Nem minden jelzés egyenként triázsolt; 10 súlyos még hiányzik |
| Funkcionális tervezés | [50 részletes eset](Compose_Tetris_Funkcionalis_Tesztek.md), [jegyzőkönyv](Compose_Tetris_Funkcionalis_Jegyzokonyv.md) | Az Androidos fixture-előkészítés még nem ellenőrzött |
| Projektadminisztráció | GitLab M2 mérföldkő, [issue-szövegek](Compose_Tetris_GitLab_Teendok.md) | Új issue, commit, push, MR nem készült |
| Dokumentumfelülvizsgálat előkészítése | [Review ellenőrzőlista](Compose_Tetris_Dokumentumfelulvizsgalat.md) | Emberi review/oktatói jóváhagyás nincs újonnan igazolva |

A fenti új munka MI-eszközökkel készült, a helyi futtatási kimenetek megőrzésével. Ez nem bizonyítja, hogy a megnevezett csapattagok személyenként elvégezték a tervezett feladataikat.

## Elért eredmények

| Mutató | Tény 2026.10.09-én |
|---|---:|
| Debug build | Sikeres |
| JUnit futások | 87 |
| JUnit sikeres / sikertelen / kihagyott | 87 / 0 / 0 |
| Projektellenőrzések / mintateszt | 86 / 1 |
| Lint warning / error | 23 / 0 |
| Detekt-jelzések | 101 |
| Részletesen értékelt eszköztalálatok | 10 kiválasztott |
| Kézi kódáttekintési hibajelöltek | 5, Androidos reprodukció nélkül |
| Tervezett és részletesített F-esetek | 50 |
| Androidos PASS / FAIL / BLOCKED / NOT RUN | 0 / 0 / 0 / 50 |
| M2 végrehajtási cél teljesítése | 0/25 = 0% |
| Végső egyedi funkcionális végrehajtás | 0/50 = 0% |
| Nyilvántartott új GitLab-hibajegyek | 0 |

A blokkoló környezethiány projektakadály, de az egyes eseteket még nem kíséreltük meg; ezért NOT RUN, nem 50 sikertelen vagy blokkoltként végrehajtott teszt. A JUnit-sikerek nem szerepelnek a funkcionális mutatóban. A Detekt exit 2 a találati küszöb túllépése, az elemzés kimenete létrejött.

Vizsgált upstream commit: `234416c455cd0b5524b7f2a7e91aaa9f6206457a`; APK SHA-256: `297f5c20cbff2a50392106642d6a79bba883202d9e262fc68f7507c85c93d2ee`. Összesített bizonyíték: [run-summary.json](../reports/run-summary.json).

## Ráfordított idő és egyéb erőforrások

### Tényleges ráfordítás nyilvántartási állapota

**A csapat által visszaigazolt tényleges munkaórák még nem állnak rendelkezésre.** A felhasználó kérésére az alábbi táblázat hozzávetőleges, utólagos **visszabecslési javaslat**. Nem GitLabban rögzített spent time, nem hitelesített személyes munkanapló. Beadás előtt minden sor javítandó/elfogadandó a tényleges munkát végző tag által; addig a tényleges időt értékelő riportkritérium nyitott marad.

| Munkacsomag | Előzetes visszabecslés | Ellenőrzés |
|---|---:|---|
| Korábbi tesztterv és felülvizsgálat | 18 óra | Csapat igazolja a valós előzményt |
| Környezet, forrásbeszerzés, build | 6 óra | Felülvizsgálandó |
| JUnit teszttervezés és futtatás | 8 óra | Felülvizsgálandó |
| Statikus futtatás és első elemzés | 8 óra | Felülvizsgálandó |
| Funkcionális specifikáció | 8 óra | Felülvizsgálandó |
| Köztes riport és metrikák | 6 óra | Felülvizsgálandó |
| Koordináció és GitLab-előkészítés | 4 óra | Felülvizsgálandó |
| **Összes javasolt visszabecslés** | **58 személyóra** | **Nem igazolt tényleges ráfordítás** |
| Androidos tesztvégrehajtás ebben a munkában | **0 óra végrehajtás** | Nem futott |

Lehetséges személyenkénti felosztás a tervbeli szerepek szerint, **egyeztetési javaslat**, nem állítás arról, ki mit dolgozott: Acsai 10, Bartl 12, Juhász 8, Kamarás 8, Komáromi 10, Szabó 10 óra (összesen 58). A csapat az igazolt munkanapló alapján szabadon átírja; a becsült és tényleges mezőket nem szabad összekeverni.

### Egyéb erőforrások

Helyi Windows-gép, hálózati függőségletöltés, JDK 17, Android SDK platform 32/build-tools 30.0.3, Gradle wrapperrel kompatibilis 7.3-rc-1, Lint 7.1.2, Detekt CLI 1.23.8, GitLab, Markdown-dokumentumok és MI-segédlet. Új fizetős erőforrás beszerzése nem történt. Androidos futtatási eszköz még nincs azonosítva. A gépidő és letöltések nem azonosak a csapat személyóráival.

## Előrehaladás a tesztterv alapján

| Teszttervbeli vállalás | Tény | Hátralévő munka |
|---|---|---|
| Fordítható forrás és verzióazonosítás | Elkészült | GitLab-commit rögzítése feltöltés után |
| Első statikus jelentés M2-re | Nyers futás, Detekt-kontroll és első részletes elemzés elkészült | Lint-kontroll, csapatreview, hibajegyek |
| Legalább 25 funkcionális végrehajtás M2-re | 0 végrehajtott; 50 specifikáció kész | Eszköz, fixture-próba, első 25 végrehajtás és bizonyíték |
| 50 funkcionális végrehajtás a végén | 0/50 | Teljes katalógus és API 21 smoke |
| 10 súlyos statikus probléma a végén | Nem igazolt | Súlyosságot indokló triázs és következmények vizsgálata |
| Ráfordítás követése issue-kban | Új becslések előkészítve | Issue-k, estimate/címke, tényleges spent time |
| Dokumentumok ellenőrzése és jóváhagyása | Review-lista előkészítve | Másik csapattag review-ja, valós jóváhagyási adatok |

Következő belső cél: környezet és fixture-ek október 16-ig; első 25 eset és statikus review október 23-ig; riport véglegesítése október 30-ig. Ezek frissített tervezési célok, nem elkészült eredmények.

## Eddigi és várható eltérések, indoklás

**Eddigi eltérések vannak; nem állítjuk, hogy a projekt eltérés nélkül halad.**

1. A korábban szeptember végére tervezett környezet-előkészítés igazolt buildje október 9-én készült el. A teljes projekt és kompatibilis eszközkészlet beszerzése szükséges volt; az Androidos eszköz még hiányzik. A belső környezeti/végrehajtási dátumokat ezért frissítettük.
2. Kiegészítő JUnit-csomag készült az 5. gyakorlat felkészüléséhez és a fontos logikai határok ellenőrzéséhez. Ez +12 óra tervezett keret, nem a funkcionális munka kiváltása. Külön M2-review +4 óra; az eredeti 226 órás feladatterv így 242 órás javaslat.
3. A „10 kiválasztott találat” tervszöveg a kiírás alapján „10 súlyos szabálysértésre” pontosult. A nyers warningok önmagukban nem elégségesek; a végső cél teljesítése még bizonytalan.
4. A hétfői gyakorlat alapján a korábbi november 2./december 7. dátumot a megelőző vasárnap 08:00 határidőre javítottuk. A kapacitásképlet számtani hibáját 393-ról 420 órára javítottuk; ez nem tényleges ledolgozott vagy igazolt rendelkezésre álló kapacitás.
5. A felhasználó kérésére az új munkát nem commitoltuk és nem pusholtuk; a dokumentumok jelenleg helyi tervezetek. Az M2 mérföldkő elkészült, új issue még nincs.

**Várható eltérések:** a 14 órás funkcionális végrehajtási keret a debuggeres előkészítés kipróbálása és bizonyítékmentés után növekedhet. Az eszköz elérhetőségétől függően a belső október 16./23. cél csúszhat; a hivatalos határidőt nem módosíthatjuk. Kevés indokolható súlyos statikus találat esetén oktatói egyeztetés kell. A statikus+funkcionális választás egyelőre változatlan; lefedettségi alternatívát nem kezdtünk el.

## Véglegesítés és felülvizsgálat

A [GitLab-teendők](Compose_Tetris_GitLab_Teendok.md) szerint a csapat commitol/pushol, issue-kat hoz létre becsléssel, a riportra való tényleges repóhivatkozást beilleszti a kapcsolódó issue-ba, majd a visszahivatkozást ide is beírja. Az 58 órás előzetes adatot, a B2 orákulumokat és a verziótörténetet csapattárs ellenőrzi. A végrehajtási eredményekkel frissítendő a fordulónap és a riport verziója; a bemutató csak az aktuális, repóban elérhető dokumentum alapján történjen.
