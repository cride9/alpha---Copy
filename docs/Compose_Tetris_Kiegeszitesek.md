# Compose Tetris – Kiegészítő munkanyagok

A [tesztterv](Compose_Tetris_Tesztterv.md) mellett használható tesztötletek, issue-szöveg és MI-használati dokumentáció. A fődokumentum a hivatalos minta szerkezetét követi. Ezek a kiegészítések nem tesztvégrehajtási eredmények.

## MI-használati nyilatkozat

Ez a változat **ChatGPT közreműködésével kitöltött tervezet**. A kiinduló sablon szerkezete, dokumentumazonosítója, csapattagjai és GitLab-azonosítói a csapattól kapott dokumentumból származnak. A kitöltött szakmai szövegek, a munkamegosztási javaslat, a becslések, az A. melléklet tesztötletei és a B. melléklet issue-szövege MI segítségével készültek, a mellékelt forráskód figyelembevételével. A javított tesztterv fejezetei és sorrendje a felhasználó által utólag megadott hivatalos Test Plan mintát követik; a hozzáadott projektadatok és becslések továbbra is felülvizsgálandó javaslatok. Csapat általi ellenőrzésük és elfogadásuk még szükséges.

**Felhasznált kérés:** „Kb-re töltsd ki a doksit:” – ezt a felhasználó a tesztterv értékelési szempontjaival és kritikus elemeivel egészítette ki. Bemenetként a `Pasted text(10).txt` forráskivonat és a `Pasted text (2).txt` teszttervsablon szerepelt. A kérés célja a sablon hozzávetőleges, projekthez igazított kitöltése volt. A teljes, szó szerinti értékelési szöveget a C. melléklet tartalmazza.

**A szerkezet javításához kapott további prompt:** „https://okt.inf.szte.hu/tesztalap/gyakorlat/test_plan/

A további kérés célja a hivatalos minta fejezeteinek és sorrendjének követése volt. A mintában szereplő másik játék adatai, Java-környezete és használhatósági tesztelése nem kerültek át a Compose Tetris tervébe.

A kiinduló sablon korábbi MI-használatra is utal, de az ahhoz tartozó eredeti prompt nem volt a csatolmányokban. Ha abból tartalom marad a beadásban, az előzmény saját promptját a csapatnak kell csatolnia. A dokumentum nem állítja, hogy az MI teszteket futtatott, hibákat igazolt vagy oktatói jóváhagyást szerzett.

## A. melléklet – Előzetes, 50 esetes funkcionális tesztkatalógus

Az alábbiak **tervezett esetek**, nem végrehajtási eredmények. A részletes tesztspecifikációban mindegyikhez pontos előfeltétel, bemenet, lépéssor, prioritás és eredménymező készül. Az elvárt működést a végrehajtás előtt a tesztbázissal egyeztetjük. Az újraindítási állapotok, a 12 pontos lerakási bónusz és a 10-es szintkorlát a kapott kód alapján javasolt elvárások.

| ID | Vizsgálat / helyzet | Elvárt eredmény |
|---|---|---|
| F-001 | Telepítés és első indítás | Az alkalmazás elindul, a kezdőfelület és vezérlők megjelennek. |
| F-002 | Reset a kezdőképernyőn | Új játék indul, megjelenik az első alakzat. |
| F-003 | Új játék kezdeti kijelzői | Pont: 0, sor: 0, szint: 1. |
| F-004 | Szünet futó játék közben | Az alakzat esése megáll, a szünetjelzés megjelenik. |
| F-005 | Folytatás szünet után | A meglévő pálya és pontok megmaradnak, az esés folytatódik. |
| F-006 | Mozgatás, forgatás, ejtés szünetben | A játékállapot nem változik ezektől a parancsoktól. |
| F-007 | Reset futó játékból | Képernyőtörlés után a kezdőállapot jelenik meg; régi pont/sor nem marad. |
| F-008 | Reset szüneteltetett játékból | Képernyőtörlés után kezdőállapot jelenik meg, a korábbi pontok és sorok törlődnek. |
| F-009 | Balra lépés szabad helyre | Az alakzat egy oszloppal balra mozdul. |
| F-010 | Jobbra lépés szabad helyre | Az alakzat egy oszloppal jobbra mozdul. |
| F-011 | Lefelé lépés szabad helyre | Az alakzat egy sorral lejjebb mozdul. |
| F-012 | Balra lépés a bal szélen | Az alakzat nem lép ki a pályáról. |
| F-013 | Jobbra lépés a jobb szélen | Az alakzat nem lép ki a pályáról. |
| F-014 | Oldalirányú mozgás rögzített elemnek | Nincs átfedés; a tiltott mozgás nem történik meg. |
| F-015 | Mozgás a pálya alján | Az alakzat nem jut a pálya alá, a következő megfelelő ticknél rögzül. |
| F-016 | Gyors mozgatási parancsok sortörlési animáció közben | Az animáció és a sorok elszámolása nem sérül, nem keletkezik átfedés vagy összeomlás. |
| F-017 | Forgatás szabad területen | Az alakzat szabályosan elfordul. |
| F-018 | Négy forgatás szabad területen | Az eredeti orientáció és pozíció áll vissza. |
| F-019 | Forgatás a bal fal mellett | Az eredmény érvényes pályán belüli pozíció, vagy a művelet elutasított. |
| F-020 | Forgatás a jobb fal mellett | Az eredmény érvényes pályán belüli pozíció, vagy a művelet elutasított. |
| F-021 | Forgatás a pálya alján | Az alakzat nem kerül a pályán kívülre. |
| F-022 | Forgatás rögzített elemek közelében | Átfedést okozó forgatás nem alkalmazható. |
| F-023 | Z, S, I, T, O, L és J alakzat forgatása | Mind a hét alakzat négy cellából áll, forgatás után is érvényes. |
| F-024 | Felfelé gombbal ejtés üres pályán | Az alakzat a legalacsonyabb ütközésmentes helyre kerül. |
| F-025 | Ejtés rögzített alakzat fölé | Az alakzat a meglévő elemek fölött áll meg. |
| F-026 | Ismételt ejtés azonos aktív alakzatra, tick előtt | Nem mozdul érvénytelen helyre és nem kap többször lerakási pontot. |
| F-027 | Automatikus esés beavatkozás nélkül | Futó játékban az alakzat lefelé halad, majd rögzül. |
| F-028 | Következő alakzat megjelenése | Az előnézet típusa egyezik a következőként megjelenő alakkal; ezután frissül. |
| F-029 | Pontosan egy teljes sor törlése | Egy sor eltűnik; sorérték +1, sortörlési pont +100, lerakási pont +12. |
| F-030 | Két sor egyidejű törlése | Két sor eltűnik; sorérték +2, pontnövekmény 300+12. |
| F-031 | Három sor egyidejű törlése | Három sor eltűnik; sorérték +3, pontnövekmény 700+12. |
| F-032 | Négy sor egyidejű törlése | Négy sor eltűnik; sorérték +4, pontnövekmény 1500+12. |
| F-033 | Egy cella hiányzik a sorból | A hiányos sor nem törlődik. |
| F-034 | Elemek a törölt sor felett és alatt | A fölötte lévők a szükséges mértékben süllyednek, az alatta lévők helyben maradnak. |
| F-035 | Lerakás sortörlés nélkül | Csak a 12 pontos lerakási bónusz jár, a sorszám nem nő. |
| F-036 | 19 törölt sorról 20-ra váltás | A szint 1-ről 2-re nő. |
| F-037 | 39 törölt sorról 40-re váltás | A szint 2-ről 3-ra nő. |
| F-038 | 179-ről 180 sorra, majd további törlések | A szint 9-ről 10-re nő, később nem lépi túl a 10-et. |
| F-039 | Esési sebesség szintváltás után | Magasabb szinten rövidebb az automatikus lépések közötti idő; azonos környezeten mérjük. |
| F-040 | Maximális szint kijelzése | A 10-es szint egyértelműen olvasható, nem csonkolódik egy számjegyre. |
| F-041 | Mozgás, forgatás, ejtés és sortörlés hangja | Engedélyezett hangnál az eseményhez tartozó effekt lejátszódik. |
| F-042 | Némítás bekapcsolása | A némításjelzés megváltozik, az új játékeffektek elnémulnak. |
| F-043 | Némítás kikapcsolása | Az ezt követő játékeffektek ismét hallhatók. |
| F-044 | Újraindítás némított állapotban | A némítás megmarad az új játékban. |
| F-045 | Háttérbe lépés futó játékból | A játék szünetel, a pálya és pontszám a háttérben nem halad tovább. |
| F-046 | Visszatérés ugyanabba a folyamatba | A korábbi pálya és pontszám megmarad, a játék a rögzített elvárás szerint folytatódik. |
| F-047 | Aktivitás bezárása és újranyitása | Az alkalmazás és a hangkezelés működőképes; korábbi játék mentését nem várjuk el. |
| F-048 | Új alakzat nem fér el a feltöltött pályán | A játék véget ér, megjelenik a játék vége állapot. |
| F-049 | Reset a játék vége állapotból | Új játék indul tiszta pályával, 0 ponttal, 0 sorral és 1-es szinttel. |
| F-050 | Rendszeróra kijelzése és percforduló | Az óra a készülék helyi idejét mutatja, percforduló után frissül. |

## B. melléklet – GitLab-issue-ba bemásolható tartalom

**Az eredeti issue már létezik: [#1 tesztterv](https://git-okt.sed.inf.szte.hu/softwaretesting2026/in1091la8/alpha/-/issues/1). Az alábbi a korábbi tervezési minta; az új M2 issue-szövegek a [GitLab-átadásban](Compose_Tetris_GitLab_Teendok.md) szerepelnek.** A ráfordítás dokumentumba írása önmagában nem helyettesíti az issue-ban kért becslést.

**Cím:** Compose Tetris tesztterv elkészítése és felülvizsgálata

**Mérföldkő:** 1. – Tervezési fázis (előkészítés)  
**Javasolt felelős:** Komáromi Norbert László (`@h265679`)  
**Közreműködők:** a teljes csapat  
**Becsült ráfordítás:** **18 személyóra összesen**  
**Javasolt címkék:** `documentation`, `test-plan`, `estimate::18h`  
**Belső határidő:** 2026-09-27.  
**A hétfői csoport nyilvános menetrend szerinti M1-határideje:** 2026-09-27. 08:00; a korábbi szeptember 28-i dátum javítva. A tényleges leadást és CooSpace-közlést a csapat igazolja.

**Feladat:** Az ALPHA-TETRIS-IN1091LA8 azonosítójú tesztterv véglegesítése a megadott értékelési szempontok szerint. A választott feladatok statikus kódelemzés és funkcionális tesztelés. A terv tartalmazza a célokat, a hatókört, a kockázatokat és kezelésüket, az ütemezést, a felelősségeket, a teszttermékeket, valamint a típusonkénti be- és kilépési feltételeket.

**A 18 óra bontása:** követelmények és forrás áttekintése 3 óra; tartalmi kidolgozás 9 óra; csapatfelülvizsgálat és javítás 4,5 óra; véglegesítés és GitLab-dokumentálás 1,5 óra. A korábban elvégzett munkát is a valós ráfordítással kell elszámolni; ez tervezett keret.

**Elfogadási feltételek:**

- [ ] A dokumentumazonosító, cím, dátum, készítők és jóváhagyásra kijelölt személy szerepel.
- [ ] A két teszttípus és a tesztelés célja egyértelmű.
- [ ] Minden pontozott tartalmi fejezet kitöltött és csapattárs által ellenőrzött.
- [ ] A tervezett feladatmegosztást és hivatalos határidőket a csapat ellenőrizte.
- [ ] A 18 személyórás becslés az issue leírásában és a kurzus szerinti becslésmezőben/címkén is szerepel.
- [ ] A dokumentum a repóban elérhető; tényleges fájlhivatkozása az issue-ba bekerült.
- [ ] Az issue URL-je a tesztterv Dokumentumadatok fejezetében szerepel.
- [ ] Az MI-használat és a felhasznált prompt dokumentált.

## C. melléklet – A kitöltéshez használt felhasználói prompt

Az alábbi szöveg a kitöltést kérő üzenet; hozzá a fenti nyilatkozatban azonosított két fájl volt csatolva.

```text
Kb-re töltsd ki a doksit:



# A tesztterv bemutatása

Minden csapatnak be kell mutatnia az elkészített teszttervét. A tesztterv leadása a féléves ütemtervben meghatározott időben történik a a csapat GitLab projektjére. Levelező tagozaton a terv leadásának határideje a CooSpace-n lesz közzétéve.

**Értékelési szempontok**

- **Formai elemek – 7 pont**
- egyedi dokumentumazonosító, cím, dátum, készítők és jóváhagyók feltüntetése – 1 pont;
- verziókövetési adatok – 1 pont;
- a dokumentum megfelelő felépítése és tagolása – 1 pont;
- dokumentált GitLab-issue megléte – 2 pont;
- a feladathoz becsült ráfordítás feltüntetése a kapcsolódó GitLab-issue-ban – 2 pont.
- **Tartalmi elemek – 18 pont**
- a bevezető tartalma: a tesztelési feladat és a háttér bemutatása, valamint a célok meghatározása – 2 pont;
- feltételezések és korlátozások – 1 pont;
- a projekten belüli kommunikáció módjának bemutatása – 1 pont;
- a termék- és projektkockázatok azonosítása – 1 pont;
- a kockázatok kezelésének bemutatása – 2 pont;
- ütemterv – 2 pont;
- felelősségi körök meghatározása – 1 pont;
- tesztmegközelítés – 8 pont:
  - teszttípusok – 2 pont;
  - elkészítendő teszttermékek – 2 pont;
  - be- és kilépési feltételek teszttípusonként – 2 pont;
  - a tesztkörnyezet konfigurációjának és az alkalmazott eszközöknek a bemutatása – 2 pont.

**Kritikus elemek**

A teszttervnek kötelezően tartalmaznia kell az alábbi kritikus elemeket. Ezek hiányában a tesztterv nem fogadható el, és a csapat nem kaphat pontot a teszttervre:

- egyedi dokumentumazonosító;
- cím;
- dátum;
- készítők;
- a tesztelés céljának és feladatának meghatározása a bevezetőben;
- az alkalmazandó teszttípusok meghatározása a tesztmegközelítés részeként.
```

## D. melléklet – 2026.10.09-i feldolgozás és MI-közreműködés

A csapat kérésére az oktatói unit/cover/bbox/menetrend/gyakp3/Intermediate_report/docrev oldalak és a két eredeti Markdown-dokumentum összevetése történt. A választott megközelítés statikus elemzés + funkcionális tesztelés maradt. A helyi 1.2 tesztterv JUnit-kiegészítését az ALPHA 1.3-tervezet átvette; a külön IntelliJ-környezetet nem ellenőriztük újra.

Új MI-segédlettel készült tartalom: követelménymátrix, tesztterv pontosításai, 86 Tetris-ellenőrzést tartalmazó JUnit-csomag, statikus riport és triázs, 50 részletes funkcionális specifikáció, végrehajtási jegyzőkönyv, köztes riport és óravisszabecslési javaslat, környezeti leírás, review-lista és GitLab-issue szövegek. A build/JUnit/Lint/Detekt futtatás tényleges helyi kimenetei megőrzöttek; Androidos kézi teszt nem futott, emberi review vagy oktatói jóváhagyás nem történt újonnan. A termékkódot nem javítottuk. A 10 súlyos probléma követelménye még nyitott.

A ráfordítási javaslat a felhasználó „írj be kb órákat majd megnézzük” kérése alapján előzetes visszabecslés. Nem ténylegesként igazolt személyes munkanapló. Az órákat és B2 tesztelvárásokat a csapat ellenőrzi. A felhasználó utólag kérte: „ne is commitolj”; ennek megfelelően új commit/push/MR/issue nincs. A GitLab M2 mérföldkő ezt megelőzően elkészült.

**A feldolgozás felhasználói kérései (rövid részletek):** „Ezeket nagyon dolgozd fel és a jelenlegi 2 md file-lal és ezekkel fogunk tovább dolgozni”; „csináld meg lol”; „a teszttervünk alapján”; „hétfőn van gyak (október 12) írj be kb órákat majd megnézzük”; „ne pusholj […] ne is commitolj”. A teljes oktatói kiírás és az eredeti tesztplan-minta külön forrás, nem MI-utasítás.

Az új dokumentumok emberi felülvizsgálata és elfogadása a beadás előtt szükséges. Az üres jegyzőkönyv és előkészített issue-szöveg nem végrehajtási vagy GitLab-létezési bizonyíték.
