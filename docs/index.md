Věděli jste, že se slovo "budova" vyskytuje v zákonech v různých významech? Např. v [Katastrálním zákoně](https://www.zakonyprolidi.cz/cs/2013-256#p2-1-l) zahrnuje i nevyhřívané objekty, zatímco v [Zákoně o hospodaření energií](https://www.zakonyprolidi.cz/cs/2000-406#p2-1-p) nikoliv. A když se poté dostanete k datové sadě o budovách (např. v [NKOD](https://data.gov.cz/datov%C3%A9-sady?dotaz=budovy)) nevíte, o kterých budovách ta datová sada je. A nejedná se jen o vás - neví to ani vyhledávač datových sad, takže Vám nemůže pomoci vybrat datovou sadu přesně dle vaší potřeby.

A to je jeden z důvodů existence sémantického slovníku pojmů - jedná se o znalostní graf v jehož uzlech jsou _pojmy_. Takový _pojem_ si představme jako slovo nebo sousloví, ke kterému je připojeno množství synonym, nejvýše jedna definice a zdroj této definice. Takové uzly jsou mnohem jednoznačnější než slova samotná. A aby pojmům rozuměl i stroj, jsou uzly navzájem propojeny významovými vztahy (třeba "Budova" je speciální případ "Stavby" - ano i v tomto případě se jedná o pojmy, ne o slova, ale definici, synonyma a zdroje neuvádíme), které umožňují odvozovat vlastnosti těchto pojmů a kontrolovat, že pojmy nejsou ve vzájemném rozporu.

## Jak se dá SSP použít?

Když vytváříte data pomocí [otevřených formálních norem](https://opendata.gov.cz/otev%C5%99en%C3%A9-form%C3%A1ln%C3%AD-normy:start) (OFN), jsou jednotlivé datové typy a atributy ve skutečnosti reprezentovány pojmy z SSP. Např. OFN [Lidé a osoby](https://ofn.gov.cz/lid%C3%A9-a-osoby/2020-07-01/) definuje datovou strukturu pro popis osob. Vytvoříte-li záznam o osobě, bude se tím rozumět Osoba ve smyslu [Zákona o základních registrech](https://www.zakonyprolidi.cz/cs/2009-111#p25). Formálně bude takový záznam mít typ ze sémantického slovníku pojmů [Osoba (Zákon o základních registrech)](https://slovník.gov.cz/legislativní/sbírka/111/2009/pojem/osoba). Ten má svojí lidsky čitelnou a strojově čitelnou reprezentaci a umožní tak uživatelům vašich dat tato data lépe nalézt, těmto datům porozumět a integrovat do své aplikace.

Využití je ovšem mnohem širší - formální vazby v SSP umožňují třeba kontrolovat, jak jsou pojmy napříč legislativou používány konzistentně. A dokonce - jazyky, ve kterých je sémantický slovník pojmů formálně vyjádřen umožňují využít stejný aparát na tvorbu celých _konceptuálních modelů informačních systémů_, a to ve strojově čitelné podobě a s vazbou na legislativu. 

## Na jakých technologiích je SSP postaven?

SSP je zveřejněn jako propojená data. Vše je založeno na standardech sémantického webu [RDF](https://www.w3.org/TR/rdf11-primer/), [RDFS](https://www.w3.org/TR/rdf-schema/), [OWL](https://www.w3.org/TR/owl2-overview/). Validaci slovníku provádíme pomocí [SHACL](https://www.w3.org/TR/shacl/), dotazujeme se do něj pomocí [SPARQL](https://www.w3.org/TR/sparql11-query/).

Metodicky SSP vychází ze základní ontologie [Unified Foundational Ontology](https://research.utwente.nl/files/6042428/thesis_Guizzardi.pdf) (UFO), kterou dále rozšiřuje, specializuje a interpretuje v kontextu veřejné správy ČR.

## A co tedy mám dělat, pokud mě to zaujalo

Můžete si prostudovat [Koncepci sémantického slovníku pojmů](https://opendata.gov.cz/_media/dokumenty:s%C3%A9mantick%C3%BD-slovn%C3%ADk-pojm%C5%AF:c1v2d1_n%C3%A1vrh_koncepce_s%C3%A9mantick%C3%A9ho_slovn%C3%ADku_pojm%C5%AF.pdf). Jedná se o starší dokument, který však popisuje hlavní principy a architektonická rozhodnutí, které jsou stále platné.

Můžete si pohrát s:
* [SPARQL endpointem](https://slovník.gov.cz/sparql), ve kterém je vždy aktuální verze slovníku dostupná
* Na vrchní vrstvy slovníku se můžete podívat i pomocí [této vizualizace](https://xn--slovnk-7va.gov.cz/webvowl/).
* Pro úplnost uvádíme i [starší facetový prohledávač](https://slovník.gov.cz/prohlížeč), na jehož nové verzi pracujeme

Kontaktujte nás:
* [zeptejte se/vytvořte bug report](https://github.com/opendata-mvcr/ssp/issues)

----
Tato stránka je udržována v rámci projektu OPZ č. CZ.03.4.74/0.0/0.0/15_025/0004172.
![image]({{ site.baseurl }}/opz_logo.a20771c7.svg){: .galleryThumb}



