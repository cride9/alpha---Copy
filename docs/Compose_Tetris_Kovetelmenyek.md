# Compose Tetris – Követelmények és készültség

**Azonosító:** ALPHA-TETRIS-REQ-002 · **Verzió:** 1.0-tervezet · **Dátum:** 2026-10-09

A kiindulópont a csapat [tesztterve](Compose_Tetris_Tesztterv.md) és [50 tesztötlete](Compose_Tetris_Kiegeszitesek.md). A választott projektfeladat továbbra is **statikus elemzés és funkcionális tesztelés**. A Tetrishez készült JUnit-csomag a játéklogika további ellenőrzése, nem váltja ki az Androidon végrehajtandó eseteket.

## Határidők és órák

A csapat hétfőn jár gyakorlatra. A [nyilvános menetrend](https://okt.inf.szte.hu/tesztalap/menetrend/) szerinti 5. hét egységtesztelés (október 5.), 6. hét lefedettség (október 12.), 7. hét feketedoboz (október 19.), 8. hét köztes bemutató (november 2.), 10. hét dokumentumfelülvizsgálat (november 16.).

| Esemény | Időpont (Europe/Budapest) | Megjegyzés |
|---|---|---|
| Következő gyakorlat | 2026-10-12, hétfő | 6. hét; az 5. gyakorlat anyaga addigra átveendő |
| M2 riport feltöltése | **2026-11-01, vasárnap 08:00** | A bemutató hetét megelőző vasárnap |
| M2 bemutató | 2026-11-02, hétfő | Nem az október 12-i alkalom |
| Végső riport feltöltése | **2026-12-06, vasárnap 08:00** | A december 7-i hét előtt |

Eltérő CooSpace-közlés esetén az ottani határidőt kell alkalmazni. A CooSpace-t ebben a munkában nem ellenőriztük.

## Köztes riport: minden értékelési szempont

Az [oktatói kiírás](https://okt.inf.szte.hu/tesztalap/gyakorlat/gyakp3/) és a [minta](https://okt.inf.szte.hu/tesztalap/gyakorlat/Intermediate_report/) alapján:

| Elvárás | Pont | Hol szerepel / mi hiányzik? |
|---|---:|---|
| Egyedi dokumentumazonosító, cím, dátum | 1 | Köztes riport dokumentumadatai elkészültek |
| Verziókövetési adatok | 1 | Verziótörténet elkészült; commit/push a csapat feladata |
| Strukturált dokumentum | 1 | A mintának megfelelő fejezetek elkészültek |
| Dokumentált GitLab-issue | 2 | **Létrehozandó**, kész szöveg az átadási dokumentumban |
| Becsült ráfordítás az issue-ban | 2 | **Rögzítendő:** 36 személyóra, estimate mező és címke |
| Vezetői összefoglaló | 1 | Elkészült; az Androidos végrehajtás hiányát is jelzi |
| Elvégzett feladatok | 2 | Build, JUnit, Lint, Detekt, specifikáció és dokumentáció |
| Elért eredmények | 2 | Nyers eredmények és készültségi mutatók |
| Tényleges idő és erőforrások | 1 | Erőforrások rögzítve; **a 58 órás előzetes becslés még jóváhagyandó** |
| Előrehaladás a teszttervhez képest | 1 | Terv/tény táblázat, 0/25 Androidos eset |
| Eddigi és várható eltérések, indoklás | 1 | Kifejezetten dokumentálva |

A helyi tervezet még nem beadásra kész: a GitLab-issue, a tényleges munkanapló ellenőrzése, a csapatfelülvizsgálat és a repóba feltöltés szükséges. Az összes pontot az oktató ítéli meg, nem ez a táblázat.

## A terv vállalásai és a félév végére előírt mennyiségek

| Terület | Elvárás | Jelenlegi állapot |
|---|---|---|
| M2 belső cél | Első statikus riport + legalább 25 végrehajtott funkcionális eset | Statikus munkaváltozat kész; **0/25 Androidos eset** |
| Végső statikus feladat | Legalább **10 súlyos szabálysértés** részletes elemzése | 10 eszköztalálat részletes triázsa és külön kézi hibajelöltek; **10 súlyos találat még nem igazolt** |
| Végső funkcionális feladat | Legalább **50 különálló végrehajtott eset**, pass/fail értékeléssel | 50 specifikáció és jegyzőkönyv kész; **0/50 végrehajtott** |
| Másodlagos környezet | A tesztterv szerinti alaptesztek API 21-en is | Nem futott |
| Emberi felülvizsgálat | Másik csapattag ellenőrzi az anyagokat | Ellenőrzőlista kész; felülvizsgálat még nem történt |

Forrás: [projektkövetelmények](https://okt.inf.szte.hu/tesztalap/gyakorlat/kovetelmenyek/). Az eszközök által kiírt 124 jelzés nem jelent 124 külön hibát vagy 124 súlyos problémát. A „10 kiválasztott találat” eredeti tervszöveg szigorítása szükséges, mert a kiírás súlyos szabálysértéseket kér. Ha nem található 10 indokolható súlyos probléma, oktatói egyeztetés kell; a lefedettségi alternatívára önkényesen nem váltottunk át.

## Felkészülés az 5. gyakorlat anyagából

A [unit óra](https://okt.inf.szte.hu/tesztalap/gyakorlat/unit/) a JUnit, teszteset, assertion, fixture, egység- és integrációs teszt elkülönítését tanítja. A saját Tetris-csomag 64 logikai egységtesztből és 22 ViewModel-integrációs ellenőrzésből áll; az eredeti 2+2 teszt külön infrastruktúraellenőrzés. A csapatnak a forrás alapján be kell tudnia mutatni a kezdeti állapotot, bemenetet és elvárt kimenetet, valamint a határértékek választását.

Az óra Bookstore-feladatai külön gyakorlófeladatok; ezek elkészítését ez a Tetris-projektcsomag nem állítja. A [lefedettségi óra](https://okt.inf.szte.hu/tesztalap/gyakorlat/cover/) line/branch coverage, a [feketedoboz óra](https://okt.inf.szte.hu/tesztalap/gyakorlat/bbox/) ekvivalenciaparticionálás, határérték és döntési/állapotátmeneti esetek ismeretét kéri. Lefedettséget nem mértünk, erre számszerű eredményt nem adunk.

A [dokumentumfelülvizsgálati óra](https://okt.inf.szte.hu/tesztalap/gyakorlat/docrev/) szempontjaihoz a külön felülvizsgálati ellenőrzőlista használható. A készültség, bizonyíték, verzió és dokumentumközi következetesség ellenőrzését a csapat végzi el.
