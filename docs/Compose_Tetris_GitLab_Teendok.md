# Compose Tetris – Git és GitLab átadás

**Azonosító:** ALPHA-TETRIS-GL-002 · **Verzió:** 1.0 · **Dátum:** 2026-10-09

## Mi történt és mi nem?

**Elkészült a GitLabon:** [M2 – Köztes riport és tesztelési előrehaladás](https://git-okt.sed.inf.szte.hu/softwaretesting2026/in1091la8/alpha/-/milestones/2), október 9. kezdettel, november 1. céldátummal; a leírásban a **08:00** határidő is szerepel. Ez a felhasználó „ne commitolj/ne pusholj” kérése előtt készült.

**Helyi Git-állapot:** `codex/mk2-testing` ág, a `feature/docs` állapotából. **Új commit, staging, push vagy merge request nem készült.** Az új és módosított fájlok helyben vannak. A `.tools`, `.work-mk2`, build, helyi SDK-beállítás és APK ignorált.

**Nem készült a GitLabon:** új issue, új címke, tényleges időbejegyzés, riportlinkes komment, új MR vagy merge. Az eredeti [#1 tesztterv-issue](https://git-okt.sed.inf.szte.hu/softwaretesting2026/in1091la8/alpha/-/issues/1) és [!1 Dokumentumok hozzáadása MR](https://git-okt.sed.inf.szte.hu/softwaretesting2026/in1091la8/alpha/-/merge_requests/1) változatlan.

## A csapat következő GitLab-lépései

1. Ellenőrizzétek a dokumentumokat, a becsült órákat és a B2 elvárásokat, majd commitoljátok és pusholjátok a kívánt ágra a helyi változásokat. A [README](../README.md) az elkészült fájlokat összegyűjti. A nyers `reports/` kimenetek is kerüljenek a repóba; a `.tools/` és `build/` ne.
2. Az alábbi **7 feladat-issue** létrehozásakor állítsatok felelőst, M2 mérföldkövet, estimate mezőt és becslést jelölő címkét. A minták az eredeti terv órakereteit követik; nem tényleges spent time értékek. Az eddigi `estimate:18h` címke stílusához igazodva javasolt `estimate:12h`, `estimate:72h`, `estimate:36h`, `estimate:14h`, `estimate:4h`; ezek még nincsenek létrehozva.
3. A riport-issue-ban a feltöltött `docs/Compose_Tetris_Koztes_Riport.md` **tényleges GitLab-fájlhivatkozása** legyen. Az új issue URL-jét a riport Dokumentumadatok részébe írjátok, és a végleges commitot is rögzítsétek.
4. A megfelelő teszt- és riport-issue-kba írjátok a valóban ellenőrzött személyórákat, végrehajtási bizonyítékokat és eredményeket. Az 58 órás előzetes visszabecslést ne rögzítsétek igazolt tényként csapategyeztetés nélkül.
5. A kézi kódáttekintés MA-001…005 jelöltjeiből készítsetek követhető hibajegyeket, jelezve, melyik még csak kódalapú következtetés. Androidos reprodukció után frissítsétek őket.
6. Ha MR-rel dolgoztok: az ág `feature/docs`-ból indult, miközben az eredeti !1 még nyitott. A célágat az M1 dokumentumok merge-je alapján válasszátok; kerülni kell a két MR-ben párhuzamosan módosított azonos dokumentumok ütközését. Nem hoztunk létre és nem merge-eltünk MR-t.

GitLab-fájllink formája feltöltés után: a projektben nyissátok meg a megfelelő ágon a fájlt, és az ott kapott hivatkozást másoljátok. Jelenleg nincs feltöltött új fájl, ezért itt nem adunk nem létező riport-URL-t.

## Issue 1 – Környezet és tesztelt verzió rögzítése

**Cím:** Compose Tetris környezet és APK azonosítása  
**Felelős:** Bartl Bálint `@h449633`  
**Mérföldkő:** M2  
**Becslés:** 12 személyóra; estimate mező `12h`, címke `estimate:12h`  
**Belső cél:** 2026-10-16

**Leírás:** A fordítható upstream forrás, JDK/Gradle/SDK/AGP/Kotlin/Compose verziók, APK SHA és Androidos eszköz azonosítása. Helyben elkészült a build és a verzióleírás (`docs/Compose_Tetris_Kornyezet.md`); az eszközlista üres, telepítés/indítás még nem történt. A személyenkénti tényleges idő ellenőrzendő.

- [x] Teljes forrás és upstream SHA rögzített.
- [x] Build, APK és hash elkészült.
- [ ] Források és riport feltöltve, fájllink az issue-ban.
- [ ] API 35 elsődleges és API 21 másodlagos környezet azonosított.
- [ ] Telepítés és indítás bizonyítékkal ellenőrzött.
- [ ] Debuggeres fixture-előkészítés kipróbált.

## Issue 2 – Statikus elemzés

**Cím:** Compose Tetris statikus elemzés és találatok értékelése  
**Felelős:** Acsai Gergő `@h446322`; közreműködő Bartl Bálint  
**Mérföldkő:** M2  
**Becslés:** 72 személyóra; estimate `72h`, címke `estimate:72h`  
**Belső cél:** első csapatreview 2026-10-23; végső súlyos találati cél M3-ig

**Leírás:** A tesztterv szerinti Lint/Detekt elemzés, konfigurációellenőrzés, nyers riport, triázs és javítási javaslat. Helyi első eredmény: 23 Lint warning, 101 Detekt-jelzés; 10 kiválasztott találat részletes elemzése a `docs/Compose_Tetris_Statikus_Elemzes.md` fájlban. Ez még nem 10 súlyos szabálysértés. A kézi kódáttekintés külön hibajelölteket tartalmaz, Androidos reprodukció nélkül.

- [x] Nyers XML/HTML és első statisztika elkészült.
- [x] Tíz kiválasztott találat valódiság/ok/hatás/javítás/becslés alapján értékelt.
- [x] Detekt ismert hibás mintán és negatív kontrollon ellenőrzött.
- [ ] Lint külön pozitív/negatív kontrollja elkészült.
- [ ] Fennmaradó jelzések triázsa, téves pozitívok indoklása.
- [ ] Súlyos találatok külön igazolása; végső követelményhez legalább 10 indokolható szabálysértés.
- [ ] Csapattársi review, hibajegyek, tényleges idő és feltöltött fájllinkek.

## Issue 3 – Funkcionális specifikáció és tesztadatok

**Cím:** Compose Tetris 50 funkcionális eset és tesztadat előkészítése  
**Felelős:** Juhász Ferenc Márk `@h469636`; közreműködő Kamarás Levente  
**Mérföldkő:** M2  
**Becslés:** 36 személyóra; estimate `36h`, címke `estimate:36h`  
**Belső cél:** 2026-10-16

**Leírás:** Az eredeti F-001…050 katalógusból pontos előfeltétel, bemenet, lépés és várt eredmény. Helyi fájl: `docs/Compose_Tetris_Funkcionalis_Tesztek.md`. A terv kockázatai és black-box technikái szerepelnek; a forrásból javasolt B2 orákulumokat csapatként ellenőrizni kell. Nincs igazolt Androidos fixture vagy végrehajtás.

- [x] Mind az 50 azonosító részletezett.
- [x] Állapotadatok, M2 első 25 eset és API 21 smoke kiválasztva.
- [ ] B2 elvárások elfogadva/javítva a csapat által.
- [ ] Állapot-előkészítés Androidon működik, reprodukció leírva.
- [ ] Specifikáció csapattárs által review-zva, repólink és tényleges idő rögzítve.

## Issue 4 – Funkcionális végrehajtás

**Cím:** Compose Tetris funkcionális tesztek végrehajtása és hibajegyek  
**Felelős:** Kamarás Levente `@h470575`; közreműködők Juhász Ferenc Márk, Acsai Gergő, Szabó Larion  
**Mérföldkő:** M2  
**Becslés:** 14 személyóra, fixture-próba után újrabecsülendő; estimate `14h`, címke `estimate:14h`  
**Belső cél:** első 25 eset 2026-10-23; teljes 50 eset 2026-11-14

**Leírás:** Elsődleges Androidos környezeten az azonosított APK valódi végrehajtása, pass/fail, eredmény, tesztelő, dátum és bizonyíték rögzítése. M2 belső cél legalább 25 különálló eset, a végső cél legalább 50. Jelenleg 0/50 végrehajtott, 50 NOT RUN; JUnit nem számít ebbe. Fájl: `docs/Compose_Tetris_Funkcionalis_Jegyzokonyv.md`.

- [ ] Eszköz/Android/APK SHA és B2 orákulum elfogadva.
- [ ] Első 25 kijelölt eset bizonyítékkal végrehajtott, PASS/FAIL értékelt.
- [ ] Feltárt hibák reprodukálható GitLab-jegyei elkészültek.
- [ ] Tényleges végrehajtási idő rögzítve.
- [ ] Végső 50 egyedi eset és API 21 smoke lefutott (M3-ig).
- [ ] Riportmutatók frissítve a jegyzőkönyv alapján.

## Issue 5 – Kiegészítő JUnit-csomag

**Cím:** Compose Tetris játéklogika JUnit és integrációs ellenőrzése  
**Felelős:** Bartl Bálint `@h449633`; közreműködő Acsai Gergő  
**Mérföldkő:** M2  
**Becslés:** 12 személyóra; estimate `12h`, címke `estimate:12h`  
**Belső cél:** 2026-10-12

**Leírás:** Az 5. gyakorlat egységtesztelési technikáinak alkalmazása a saját Tetris logikájára. Helyi eredmény 87/87 sikeres: 64 logikai egység, 22 ViewModel-integráció, 1 eredeti infrastruktúrateszt. A tesztfüggőségek és nyers riportok a repóba feltöltendők. Fájl: `docs/Compose_Tetris_Egysegtesztek.md`. UI, hang, Android lifecycle és coverage nem igazolt; nem helyettesíti a funkcionális végrehajtást.

- [x] Határértékek és alakzatok automatizált ellenőrzése.
- [x] Valódi ViewModel sortörlés/dispatch integrációs ellenőrzése.
- [x] Sikeres XML/HTML és buildnapló megőrizve.
- [ ] Csapattársi kódreview és másik gépes reprodukálás.
- [ ] Tesztforrás és bizonyíték feltöltve, repólink és tényleges idő rögzítve.

## Issue 6 – Köztes riport (kötelező dokumentum-issue)

**Cím:** Compose Tetris köztes riport elkészítése és bemutatása  
**Felelős:** Szabó Larion `@h377633`; közreműködő Komáromi Norbert László  
**Mérföldkő:** M2  
**Becslés:** **36 személyóra**; estimate **`36h`**, címke **`estimate:36h`**  
**Belső véglegesítés:** 2026-10-30  
**Leadás:** **2026-11-01 08:00 Europe/Budapest**; bemutató 2026-11-02

**Leírás:** Az ALPHA-TETRIS-KR-002 dokumentum véglegesítése a [köztesriport-kiírás](https://okt.inf.szte.hu/tesztalap/gyakorlat/gyakp3/) összes formai és tartalmi szempontja alapján. A helyi `docs/Compose_Tetris_Koztes_Riport.md` tervezet tartalmazza az összefoglalót, elvégzett munkát, bizonyított eredményeket, erőforrásokat, terv/tény előrehaladást és az eddigi/várható eltérések indoklását. Az 58 órás előzetes visszabecslés még nem ténylegesként elfogadott időadat; Androidos végrehajtás jelenleg 0/50.

**A 36 óra tervezett bontása:** eredmények/bizonyítékok összegyűjtése 8; szakmai szöveg és összesítés 12; ráfordítás/terv/tény/eltérések 6; véglegesítés, GitLab-linkek és bemutató-előkészítés 10 óra. A külön 4 órás dokumentumreview nem ennek duplikált része.

**Riport hivatkozása:** feltöltés után ide másolandó a tényleges GitLab-fájl URL-je, majd ennek az issue-nak az URL-je a riportba.

- [x] Egyedi azonosító, cím, dátum, verziótörténet és strukturált tervezet kész.
- [x] Vezetői összefoglaló, feladatok/eredmények és eddigi/várható eltérések leírva.
- [ ] A csapat által igazolt tényleges órák és erőforrásadatok véglegesítve.
- [ ] A futtatás utáni Androidos eredmények és terv/tény mutatók frissítve.
- [ ] A riport repóban elérhető, tényleges fájlhivatkozás az issue-ban.
- [ ] Issue URL és végleges commit a riportban.
- [ ] Estimate mezőben 36h és becslést jelölő címke szerepel.
- [ ] Másik csapattag ellenőrizte; bemutató előtt, határidőre rendelkezésre áll.

## Issue 7 – Dokumentumreview

**Cím:** Compose Tetris M2 dokumentumok csapattársi felülvizsgálata  
**Felelős:** Komáromi Norbert László `@h265679`; reviewer a szerzőn kívüli csapattag  
**Mérföldkő:** M2  
**Becslés:** 4 személyóra; estimate `4h`, címke `estimate:4h`  
**Belső cél:** 2026-10-30

**Leírás:** A `docs/Compose_Tetris_Dokumentumfelulvizsgalat.md` ellenőrzőlistája alapján a követelmények, tényleges idő, státuszok, eredmények és linkek emberi ellenőrzése. Review még nem történt; az előkészített lista nem jóváhagyás.

- [ ] Reviewer, dátum, dokumentumverzió és commit rögzített.
- [ ] Eltérések és javításuk dokumentáltak.
- [ ] Tényleges ráfordítás rögzített.
- [ ] Riport elfogadása/nyitott problémái követhetően leírva.

## Hibajegyek előkészítése

A statikus riport MA-001…005 szakaszai adják a reprodukciós hipotézist, helyet, hatást és javítási becslést. Javasolt címek:

- MA-001: „Szintváltás után a játék tick-periódusa a kezdeti szinten maradhat” – F-039, magas előzetes súlyosság.
- MA-002: „Üres aktív alakzat ejtése nem termináló ciklust indíthat” – korai Reset→Drop, magas előzetes súlyosság.
- MA-003: „Activity újranyitás után felszabadított SoundPool kerülhet újrahasználatba” – F-047, közepes/magas.
- MA-004: „Párhuzamos játékakciók korábbi állapottal felülírhatják egymást” – F-016/F-026, reprodukció szükséges.
- MA-005: „Gombismétlő ticker életciklusa recomposition alatt bizonytalan” – hosszú nyomás/elengedés, reprodukció szükséges.

Minden jegyhez: bejelentő, vizsgált verzió, környezet, pontos előfeltétel/lépések, várt és tényleges eredmény, bizonyíték, súlyosság/prioritás/státusz, felelős, becslés. A „tényleges eredmény” mezőben addig **„kódáttekintésből származó következtetés, Androidon még nem reprodukált”** szerepeljen. Ne adjatok kitalált videót vagy pass/fail értéket. Emberi review és valós reprodukció után véglegesíthető a besorolás.
