<img src="https://user-images.githubusercontent.com/1140626/118179053-109e2c80-b435-11eb-9400-e960efecc284.png" alt="drawing" width="200" align="right"/>

Výkon veřejné správy vychází z legislativních předpisů. Veškeré činnosti, včetně získávání, analýzy a zpracování dat (a jejich popis), vychází z platných legislativních předpisů. Pojmy použité v legislativě by tedy měly být jednoznačně definované. Ale je tomu skutečně tak?

Jenom slovo "budova" se vyskytuje v zákonech v několika různých významech, např. v [Katastrálním zákoně](https://www.zakonyprolidi.cz/cs/2013-256#p2-1-l) zahrnuje i nevyhřívané objekty, zatímco v [Zákoně o hospodaření energií](https://www.zakonyprolidi.cz/cs/2000-406#p2-1-p) nikoliv. Podle definice lze tedy rozlišit nejméně dva významy tohoto slova, které je potřeba rozlišit. Proč? Představte si nařízení, které zavádí speciální daň z budovy. Jak zjistíte, kterých budov se nařízení týká? Podobně je potřeba rozlišit datové sady poskytující data o budovách v různém kontextu.

Řešením tohoto problému je Sémantický slovník pojmů, který pracuje s _pojmy_ v konkrétním kontextu. Takový _pojem_ si představme jako slovo nebo sousloví s jednoznačnou definicí a jejím zdrojem. Zdrojem je například znění zákona, konkrétní paragraf v tomto zákoně, ale i obecná znalost, například význam pojmu tak, jak mu rozumí a používá ho určitá skupina expertů.

<!-- _Pojem_ může být pojmenován pomocí synonym a _pojmy_ jsou mezi sebou propojeny prostřednictvím významových vazeb. Příkladem významové vazby je specializace, kdy _pojem_ "Budova" je speciálním případem _pojmu_ "Stavba", ale významovou vazbou je i vztah "budovy" a její vlastnosti (například "číslo popisné" je také _pojmem_). Tyto významové vazby umožňují odvozovat vlastnosti _pojmů_ a kontrolovat, že _pojmy_ nejsou ve vzájemném rozporu. -->

<!-- Datové typy a atrobuty popsané v [otevřených formálních normách](https://opendata.gov.cz/otev%C5%99en%C3%A9-form%C3%A1ln%C3%AD-normy:start) (OFN), jsou ve skutečnosti také reprezentovány pojmy z SSP. Např. OFN [Lidé a osoby](https://ofn.gov.cz/lid%C3%A9-a-osoby/2020-07-01/) definuje datovou strukturu pro popis osob. Vytvoříte-li záznam o osobě, bude se tím rozumět osoba ve smyslu [Zákona o základních registrech](https://www.zakonyprolidi.cz/cs/2009-111#p25), která bude reprezentována jako pojem [Osoba (Zákon o základních registrech)](https://slovník.gov.cz/legislativní/sbírka/111/2009/pojem/osoba). Ten má svojí lidsky čitelnou a strojově čitelnou reprezentaci a umožní tak uživatelům vašich dat tato data lépe nalézt, porozumět jim a integrovat do svých aplikací. Využití je ovšem mnohem širší - formální vazby v SSP umožňují třeba kontrolovat, zda jsou pojmy napříč legislativou používány konzistentně. Formální jazyky, ve kterých je sémantický slovník pojmů vyjádřen, umožňují na základě slovníků automatizovaně generovat konceptuální datových modelů (např. pro popis [datové architektury veřejné správy](https://archi.gov.cz/nar-dokument:architektonicke_uloziste_a_nastroj)), a to ve strojově čitelné podobě a s vazbou na konkrétní legislativu. -->

## Prohlížení, vytváření a využití sémantického slovníku pojmů

[![](showit.png)](https://slovník.gov.cz/prohlížíme/)

Systém ShowIt umožňuje intuitivní vyhledávání pojmů Sémantického slovníku pojmů. Pojmy sdružuje podle klíčových slov, ale i podle slovníků a podle jejich vzájemných vztahů.

[![](mission.png)](https://slovník.gov.cz/modelujeme/)

 Výrobní linka slouží k vytváření a editaci slovníků a k jejich následné publikaci. Součástí výrobní linky jsou v současné době nástroje Mission Control pro vytváření slovníků a jejich kopií pro úpravy, TermIt pro tvorbu glosářů -- tedy podrobně popsaných seznamů pojmů patřících do jednoho slovníku -- a OntoGrapher pro samotné modelování. V současné době provozujeme tři instance výrobní linky. Vedle produkční, která slouží k samotnému modelování máme ještě vývojovou instanci pro potřeby vývojářů a [demo instanci](https://slovník-test.dia.gov.cz/modelujeme/), která slouží pro výuku uživatelů.

[![](dataspecer.png)](https://slovník.gov.cz/generujeme/)

 Na základě publikovaných slovníků je možné pomocí nástroje Dataspecer automatizovaně generovat datové specifikace. Datová specifikace obsahuje textovou část a konceptuální model a díky tomu, že vzniká na základě sémantických slovníků je dokonale lidsky i strojově čitelná.

## Často kladené dotazy (FAQ)

* **Jaká je struktura sémantického slovníku pojmů?** Struktura sémantického slovníku pojmů a proces jeho publikace je popsán v dokumentu [Koncepce sémantického slovníku pojmů pro potřeby konceptuálního datového modelování agend](https://data.gov.cz/kodi/výstupy/C5V2.pdf)

* **Jak mám vytvářet nebo spravovat slovníky?** V rámci projektu byl vytvořen dokument [Metodika tvorby a údržby sémantického slovníku pojmů veřejné správy](https://data.gov.cz/kodi/výstupy/C5V4.pdf). K výuce modelování můžete využít [e-learningových videí k modelování významu dat ve veřejné správě](https://data.gov.cz/vzdělávání/e-learning/modelování-významu-dat-ve-veřejné-správě/).

* **Mohu k datům přistupovat prostřednictvím SPARQL end-pointu?**
Ano, obsah celého Sémantického slovníku pojmů je přístupný přes [SPARQL endpoint](https://slovník.gov.cz/sparql), ve kterém je dostupná vždy aktuální verze slovníku.

* **Kde získám přihlašovací údaje do výrobní linky?** Pro získání přístupových údajů k výrobní lince napište na mail data@dia.gov.cz. Do mailu uveďte, co chcete modelovat, zda máte s modelováním zkušenosti a ke které instanci chcete přistupovat.

* **Máte další otázky?** Sémantický slovník pojmů je udržován v repozitáři na GitHubu, ve kterém můžete [pokládat otázky a hlásit chyby](https://github.com/datagov-cz/ssp/issues).

----
Tato stránka byla vytvořena a udržována v rámci projektu OPZ č. [CZ.03.4.74/0.0/0.0/15_025/0013983](https://esf2014.esfcr.cz/PublicPortal/Views/Projekty/Public/ProjektDetailPublicPage.aspx?action=get&datovySkladId=F5E162B2-15EC-4BBE-9ABD-066388F3D412).
![image](opz_logo.a20771c7.svg)
%
